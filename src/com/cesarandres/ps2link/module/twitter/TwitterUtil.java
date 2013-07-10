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

public class TwitterUtil {

	public static final String PLANETSIDE2 = "planetside2";
	public static final String PS2DAILYDEALS = "PS2DailyDeals";
	public static final String MHIDGY = "mhigby";
	public static final String PURRFECTSTORM = "PurrfectStorm";

	public static ArrayList<PS2Tweet> getTweets(String[] users)
			throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("AdtZzl6c9v4QiqC6yHWSVw")
				.setOAuthConsumerSecret(
						"eGaL0bIj6Y0p84cs6RZdw7WvXoq9EkDF9KES0bPnhw")
				.setOAuthAccessToken(
						"752283427-RmHBauha1JAOWOcFkJvQvt7oLryQqDzBchGE33tG")
				.setOAuthAccessTokenSecret(
						"liJ8MHWltgNfVW9nORSPRNP6oogQNFr3D08eC0k8A");
		TwitterFactory tf = new TwitterFactory(cb.build());

		Twitter twitter = tf.getInstance();
		ArrayList<PS2Tweet> twwetsFound = new ArrayList<PS2Tweet>();
		ResponseList<User> usersFound = twitter.lookupUsers(users);
		for (User user : usersFound) {
			String url = user.getProfileImageURL();
			if (user.getStatus() != null) {
				List<Status> statusess = twitter
						.getUserTimeline(user.getScreenName());
				for (Status status3 : statusess) {
					twwetsFound.add(new PS2Tweet(
							Long.toString(status3.getId()), user.getName(), (int)(status3
									.getCreatedAt().getTime() / 1000), status3
									.getText(), user.getScreenName(), user.getBiggerProfileImageURL()));
				}
			}
		}
		return twwetsFound;
	}
}
