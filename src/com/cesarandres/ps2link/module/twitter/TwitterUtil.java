package com.cesarandres.ps2link.module.twitter;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {
	
	public TwitterUtil() throws TwitterException{	
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("AdtZzl6c9v4QiqC6yHWSVw")
		  .setOAuthConsumerSecret("eGaL0bIj6Y0p84cs6RZdw7WvXoq9EkDF9KES0bPnhw")
		  .setOAuthAccessToken("752283427-RmHBauha1JAOWOcFkJvQvt7oLryQqDzBchGE33tG")
		  .setOAuthAccessTokenSecret("liJ8MHWltgNfVW9nORSPRNP6oogQNFr3D08eC0k8A");
		TwitterFactory tf = new TwitterFactory(cb.build());
		
	    // The factory instance is re-useable and thread safe.
	    Twitter twitter = TwitterFactory.getSingleton();
	    List<Status> statuses = twitter.getHomeTimeline();
	    System.out.println("Showing home timeline.");
	    for (Status status : statuses) {
	        System.out.println(status.getUser().getName() + ":" +
	                           status.getText());
	    }
	}
}
