package com.cesarandres.ps2link.soe.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cesar on 6/15/13.
 *
 * This class will be in charge of formatting requests for SOE Census API and retrieving the
 * information. You can use the response directly from JSON or they can be also automatically
 * converted to objects to ease their manipulation.
 *
 * API Calls follow the following format:
 *      Game Data:      /verb/game/collection/[identifier][?queryString]
 *      Game Images:    /img/game/collection/identifier[/imageType]
 *
 * This class is been designed by following the design specified on http://census.soe.com/.
 */

public class SOECensus {

    public static final String SERVICE_ID = "s:PS2Link";
    public static final String ENDPOINT_URL = "http://census.soe.com";
    public static final String IMG = "img";
    public static final String ITEM = "item";
    public static enum Verb {
        GET("get"),
        COUNT("count");

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
        PS2("ps2"),
        EQ2("eq2");

        private final String game;

        private Game(final String game) {
            this.game = game;
        }

        @Override
        public String toString() {
            return this.game;
        }
    }
    public static enum ImageType{
        PAPERDOLL("paperdoll"),
        HEADSHOT("headshot");

        private final String imagetype;

        private ImageType(final String imagetype) {
            this.imagetype = imagetype;
        }

        @Override
        public String toString() {
            return this.imagetype;
        }
    }

    public URL generateGameImageRequest(Game game, Collections.EQ2Collection collection,
                                       String identifier, ImageType type) throws MalformedURLException {
        URL requestDataURL;
        if(type == null){
            requestDataURL = new URL(ENDPOINT_URL + "/" + SERVICE_ID + "/"
                    + IMG + "/" + game.toString() + "/" + collection.toString() + "/"
                    + identifier + "/" + ITEM);
        }else {
            requestDataURL = new URL(ENDPOINT_URL + "/" + SERVICE_ID + "/"
                    + IMG + "/" + game.toString() + "/" + collection.toString() + "/"
                    + identifier + "/" + type.toString() + "/" + ITEM);
        }
        return requestDataURL;
    }

    public URL generateGameDataRequest(Verb verb, Game game, Collections.PS2Collection collection, String identifier, QueryString query) throws MalformedURLException {
        if(identifier == null){
            identifier = "";
        }
        URL requestDataURL = new URL(ENDPOINT_URL + "/" + SERVICE_ID + "/" + verb.toString() + "/"
                                    + game.toString() + "/" + collection.toString() + "/"
                                    + identifier + "/" + query.toString());
        return requestDataURL;
    }

    public URL generateGameDataRequest(Verb verb, Game game, Collections.EQ2Collection collection,
                                          String identifier, QueryString query) throws MalformedURLException {
        if(identifier == null){
            identifier = "";
        }
        URL requestDataURL = new URL(ENDPOINT_URL + "/" + SERVICE_ID + "/" + verb.toString() + "/"
                + game.toString() + "/" + collection.toString() + "/"
                + identifier + "/" + query.toString());
        return requestDataURL;
    }
}
