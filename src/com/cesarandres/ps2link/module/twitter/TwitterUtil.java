package com.cesarandres.ps2link.module.twitter;

import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {

	public enum TwitterUser{PLANETSIDE2, PS2DEAL, MHIDGY, PURRFECTSTORM};
	
	public TwitterUtil() throws TwitterException {
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
		String[] srch = new String[] { "PS2DailyDeals" };
		ResponseList<User> users = twitter.lookupUsers(srch);
		for (User user : users) {
			String url = user.getProfileImageURL();
			System.out.println("Friend's Name " + user.getName());
			if (user.getStatus() != null) {
				System.out.println("Friend timeline");
				List<Status> statusess = twitter
						.getUserTimeline(user.getName());
				for (Status status3 : statusess) {
					System.out.println(status3.getText());
				}
			}
		}
	}
}
