package com.cesarandres.ps2link.dbg.volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by
 * Gson.
 */
public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     * 
     * @param url
     *            URL of the request to make
     * @param clazz
     *            Relevant class object, for Gson's reflection
     * @param headers
     *            Map of request headers
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, Listener<T> listener, ErrorListener errorListener) {
	super(Method.GET, url, errorListener);
	this.clazz = clazz;
	this.headers = headers;
	this.listener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.volley.Request#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
	return headers != null ? headers : super.getHeaders();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.volley.Request#deliverResponse(java.lang.Object)
     */
    @Override
    protected void deliverResponse(T response) {
	listener.onResponse(response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.volley.Request#parseNetworkResponse(com.android.volley.
     * NetworkResponse)
     */
    @SuppressWarnings("unchecked")
	@Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
	try {
	    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	    Object jsonObject = gson.fromJson(json, clazz);
	    return (Response<T>) Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
	} catch (UnsupportedEncodingException e) {
	    return Response.error(new ParseError(e));
	} catch (JsonSyntaxException e) {
	    return Response.error(new ParseError(e));
	}
    }
}