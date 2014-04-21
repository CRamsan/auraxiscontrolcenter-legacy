package com.cesarandres.ps2link.module.twitter;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.cesarandres.ps2link.soe.util.Logger;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {

    public static final String PLANETSIDE2 = "planetside2";
    public static final String PLANETSIDE2EU = "ps2eu";
    public static final String PS2DAILYDEALS = "PS2DailyDeals";
    public static final String MHIDGY = "mhigby";
    public static final String PS_TRAY = "PS_TRay";
    public static final String RADARX = "radarx";
    public static final String ADAMCLEGG = "Arclegger";
    public static final String MULDOONX9 = "muldoonx9";
    public static final String XALORN = "xalorn";
    public static final String TAYRADACTYL = "Tayradactyl";
    public static final String XANDERCLAUSS = "XanderClauss";
    public static final String PURRFECTSTORM = "PurrfectStorm";

    public static final String CONSUMER_SECRET = "eGaL0bIj6Y0p84cs6RZdw7WvXoq9EkDF9KES0bPnhw";
    public static final String CONSUMER_KEY = "AdtZzl6c9v4QiqC6yHWSVw";
    public static final String ACCESS_TOKEN = "752283427-RmHBauha1JAOWOcFkJvQvt7oLryQqDzBchGE33tG";
    public static final String ACCESS_TOKEN_SECRET = "liJ8MHWltgNfVW9nORSPRNP6oogQNFr3D08eC0k8A";

    public static ArrayList<PS2Tweet> getTweets(String[] users) throws TwitterException {
	Twitter twitter = configureTwtitter();
	ArrayList<PS2Tweet> tweetsFound = new ArrayList<PS2Tweet>();
	tweetsFound = retrieveTweets(twitter, users);
	return tweetsFound;
    }

    public static ArrayList<PS2Tweet> getTweets(String user) throws TwitterException {
	Twitter twitter = configureTwtitter();
	ArrayList<PS2Tweet> tweetsFound = new ArrayList<PS2Tweet>();
	String[] twitterUser = new String[] { user };
	tweetsFound = retrieveTweets(twitter, twitterUser);
	return tweetsFound;
    }

    private static Twitter configureTwtitter() {
	ConfigurationBuilder cb = new ConfigurationBuilder();
	cb.setDebugEnabled(true).setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET).setOAuthAccessToken(ACCESS_TOKEN)
		.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET).setUseSSL(true);
	TwitterFactory tf = new TwitterFactory(cb.build());
	Twitter twitter = tf.getInstance();
	return twitter;
    }

    private static ArrayList<PS2Tweet> retrieveTweets(Twitter twitter, String[] users) throws TwitterException {
	ResponseList<User> usersFound = twitter.lookupUsers(users);
	ArrayList<PS2Tweet> tweetsFound = new ArrayList<PS2Tweet>();
	for (User foundUser : usersFound) {
	    if (foundUser.getStatus() != null) {
		List<Status> statusess = twitter.getUserTimeline(foundUser.getScreenName());
		String name, tag, imgUrl, text;
		for (Status status3 : statusess) {
		    if (status3.isRetweet() || status3.isRetweetedByMe()) {
			name = status3.getRetweetedStatus().getUser().getName();
			tag = status3.getRetweetedStatus().getUser().getScreenName();
			imgUrl = status3.getRetweetedStatus().getUser().getBiggerProfileImageURL();
			text = status3.getText() + "\nRetweeted by " + status3.getUser().getScreenName();
		    } else {
			name = status3.getUser().getName();
			tag = foundUser.getScreenName();
			imgUrl = status3.getUser().getBiggerProfileImageURL();
			text = status3.getText();
		    }
		    tweetsFound.add(new PS2Tweet(Long.toString(status3.getId()), name, (int) (status3.getCreatedAt().getTime() / 1000), text, tag, imgUrl));
		}
	    }
	}
	return tweetsFound;
    }
}
