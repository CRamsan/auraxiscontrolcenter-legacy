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
	public static final String PS_TRAY = "PS_TRay";
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
					if(status3.isRetweet() || status3.isRetweetedByMe()){
						twwetsFound.add(new PS2Tweet(
								Long.toString(status3.getId()), status3.getRetweetedStatus().getUser().getName(), (int)(status3
										.getCreatedAt().getTime() / 1000), status3
										.getText(), status3.getRetweetedStatus().getUser().getScreenName(), status3.getRetweetedStatus().getUser().getBiggerProfileImageURL()));
					}else{
						twwetsFound.add(new PS2Tweet(
								Long.toString(status3.getId()), status3.getUser().getName(), (int)(status3
										.getCreatedAt().getTime() / 1000), status3
										.getText(), user.getScreenName(), status3.getUser().getBiggerProfileImageURL()));
					}
				}
			}
		}
		return twwetsFound;
	}
	
	public static ArrayList<PS2Tweet> getTweets(String user)
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
		String[] twitterUser = new String[]{user};
		ResponseList<User> usersFound = twitter.lookupUsers(twitterUser);
		for (User foundUser : usersFound) {
			String url = foundUser.getProfileImageURL();
			if (foundUser.getStatus() != null) {
				List<Status> statusess = twitter
						.getUserTimeline(foundUser.getScreenName());
				for (Status status3 : statusess) {
					if(status3.isRetweet() || status3.isRetweetedByMe()){
						twwetsFound.add(new PS2Tweet(
								Long.toString(status3.getId()), status3.getRetweetedStatus().getUser().getName(), (int)(status3
										.getCreatedAt().getTime() / 1000), status3
										.getText(), status3.getRetweetedStatus().getUser().getScreenName(), status3.getRetweetedStatus().getUser().getBiggerProfileImageURL()));
					}else{
						twwetsFound.add(new PS2Tweet(
								Long.toString(status3.getId()), status3.getUser().getName(), (int)(status3
										.getCreatedAt().getTime() / 1000), status3
										.getText(), foundUser.getScreenName(), status3.getUser().getBiggerProfileImageURL()));
					}
				}
			}
		}
		return twwetsFound;
	}
}
