package com.cesarandres.ps2link.module.twitter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This class is used to retrieve tweets from the requested users. The public
 * methods will take care of configuring the connection with the API
 */
public class TwitterUtil {

    public static final String PLANETSIDE2 = "planetside2";
    public static final String PLANETSIDE2EU = "ps2eu";
    public static final String PS2DAILYDEALS = "PS2DailyDeals";
    public static final String MHIDGY = "mhigby";
    public static final String RADARX = "radarx";
    public static final String MULDOONX9 = "muldoonx9";
    public static final String DCAREY = "dcarey7761";
    public static final String WREL = "WrelPlays";

    private static final String CONSUMER_SECRET = "eGaL0bIj6Y0p84cs6RZdw7WvXoq9EkDF9KES0bPnhw";
    private static final String CONSUMER_KEY = "AdtZzl6c9v4QiqC6yHWSVw";
    private static final String ACCESS_TOKEN = "752283427-RmHBauha1JAOWOcFkJvQvt7oLryQqDzBchGE33tG";
    private static final String ACCESS_TOKEN_SECRET = "liJ8MHWltgNfVW9nORSPRNP6oogQNFr3D08eC0k8A";

    /**
     * @param users
     *            an array with all the users to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException
     *             this exception will ocur when there is a problem contacting
     *             the twiter API
     */
    public static ArrayList<PS2Tweet> getTweets(String[] users) throws TwitterException {
	Twitter twitter = configureTwitter();
	ArrayList<PS2Tweet> tweetsFound = new ArrayList<PS2Tweet>();
	tweetsFound = retrieveTweets(twitter, users);
	return tweetsFound;
    }

    /**
     * @param users
     *            the user to retrieve tweets from
     * @return the list of tweets retrieved
     * @throws TwitterException
     *             this exception will ocur when there is a problem contacting
     *             the twiter API
     */
    public static ArrayList<PS2Tweet> getTweets(String user) throws TwitterException {
	Twitter twitter = configureTwitter();
	ArrayList<PS2Tweet> tweetsFound = new ArrayList<PS2Tweet>();
	String[] twitterUser = new String[] { user };
	tweetsFound = retrieveTweets(twitter, twitterUser);
	return tweetsFound;
    }

    /**
     * @return the twitter object after being configured with the parameters to
     *         contact the API
     */
    private static Twitter configureTwitter() {
	ConfigurationBuilder cb = new ConfigurationBuilder();
	cb.setDebugEnabled(true).setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET).setOAuthAccessToken(ACCESS_TOKEN)
		.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET).setUseSSL(true);
	TwitterFactory tf = new TwitterFactory(cb.build());
	Twitter twitter = tf.getInstance();
	return twitter;
    }

    /**
     * @param twitter
     *            a configured Twitter object
     * @param users
     *            a list of users to retrieve tweets from
     * @return an arraylist with all the tweets found
     * @throws TwitterException
     *             this exception is thrown where there is an error
     *             communicating with the twitter API
     */
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
