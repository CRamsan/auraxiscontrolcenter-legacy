package com.cesarandres.ps2link.soe;

import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.soe.util.Collections.ImageCollection;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.Logger;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * 
 * 
 * This class will be in charge of formatting requests for SOE Census API and
 * retrieving the information. You can use the response directly from JSON or
 * they can be also automatically converted to objects to ease their
 * manipulation.
 * 
 * API Calls follow the following format:
 * /verb/game/collection/[identifier]?[queryString]
 * 
 * This class is been designed by following the design specified on
 * http://census.soe.com/.
 */

public class SOECensus {

	public static final String SERVICE_ID = "s:PS2Link";
    public static final String ENDPOINT_URL = "http://census.soe.com";
    public static final String IMG = "img";
    public static final String ITEM = "item";

    public static enum Verb {
	GET("get"), COUNT("count");

	private final String verb;

	private Verb(final String verb) {
	    this.verb = verb;
	}

	@Override
	public String toString() {
	    return this.verb;
	}
    }

    public static enum Game {
	PS2("ps2"),PS2V2("ps2:v2"); @Deprecated

	private final String game;

	private Game(final String game) {
	    this.game = game;
	}

	@Override
	public String toString() {
	    return this.game;
	}
    }

    public static enum ImageType {
	PAPERDOLL("paperdoll"), HEADSHOT("headshot");

	private final String imagetype;

	private ImageType(final String imagetype) {
	    this.imagetype = imagetype;
	}

	@Override
	public String toString() {
	    return this.imagetype;
	}
    }

    /**
     * @param game
     *            the game to retrieve information from
     * @param collection
     *            the data collection of the game
     * @param identifier
     *            id for a given resource
     * @return the url for this request
     * @throws MalformedURLException
     *             when there is a problem generating a valid url
     */
    public static URL generateGameImageRequest(Game game, ImageCollection collection, String identifier) throws MalformedURLException {
	URL requestDataURL = new URL(ENDPOINT_URL + "/" + SERVICE_ID + "/" + IMG + "/" + game.toString() + "/" + collection + "/" + identifier + "/" + ITEM);
	return requestDataURL;
    }

    /**
     * @param game
     *            the game to retrieve information from
     * @param collection
     *            the data collection of the game
     * @param identifier
     *            id for a given resource
     * @param type
     *            the type of image to retrieve
     * @return the url for this request
     * @throws MalformedURLException
     *             when there is a problem generating a valid url
     */
    public static URL generateGameImageRequest(Game game, ImageCollection collection, String identifier, ImageType type) throws MalformedURLException {
	URL requestDataURL = new URL(ENDPOINT_URL + "/" + SERVICE_ID + "/" + IMG + "/" + game.toString() + "/" + "icon" + "/" + identifier + "/"
		+ type.toString() + "/" + ITEM);
	return requestDataURL;
    }

    /**
     * @param verb
     *            action to realize, count or get
     * @param collection
     *            resource collection to retrieve
     * @param identifier
     *            id of the resource
     * @param query
     *            query with parameters for the search
     * @return the url to retrieve the requested resource
     */
    public static URL generateGameDataRequest(Verb verb, PS2Collection collection, String identifier, QueryString query) {
	if (identifier == null) {
	    identifier = "";
	}
	if (query == null) {
	    query = new QueryString();
	}
	URL requestDataURL = null;
	try {
	    requestDataURL = new URL(ENDPOINT_URL + "/" + SERVICE_ID + "/" + verb.toString() + "/" + Game.PS2V2 + "/" + collection.toString() + "/"
		    + identifier + "?" + query.toString());
	} catch (MalformedURLException e) {
	    Logger.log(Log.ERROR, "SOECensus", "There was a problem creating the URL");
	}
	return requestDataURL;
    }

    /**
     * @param url
     *            the url to request
     * @param responseClass
     *            the class to which retrieve data will be serialized into
     * @param success
     *            run this on success
     * @param error
     *            run this when the request fails
     * @param caller
     *            this is used to tag the call. Usually a fragment or activity
     *            is a good tag
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void sendGsonRequest(String url, Class responseClass, Listener success, ErrorListener error, Object caller) {
	GsonRequest gsonOject = new GsonRequest(url, responseClass, null, success, error);
	gsonOject.setTag(caller);
	ApplicationPS2Link.volley.add(gsonOject);
    }
}
