package com.xatkit.plugins.twitter.platform;

import com.xatkit.core.XatkitCore;
import com.xatkit.core.platform.RuntimePlatform;
import com.xatkit.plugins.twitter.platform.action.PostAtweet;
import com.xatkit.plugins.twitter.platform.action.SendDM;
import com.xatkit.plugins.twitter.platform.action.ReceiveDM;
import com.xatkit.plugins.twitter.platform.action.LookForTweets;

import org.apache.commons.configuration2.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Manages a connection to a Twitter app
 * <p>
 * This platform can be used in execution models by importing
 * <i><project_root>/platform/twitterPlatform*.xmi</i>, and provides the
 * following actions:
 * <ul>
 * <li>{@link PostAtweet}: posts a twit</li>
 * <li>{@link SendDM}: posts a twit</li>
 * <li>{@link ReceiveDM}: posts a twit</li>
 * <li>{@link LookForTweets}: posts a twit</li>
 * </ul>
 */
public class TwitterPlatform extends RuntimePlatform {
	/**
	 * The {@link Configuration} keys used to store the tokens needed to connect to
	 * Twitter app.
	 * <p>
	 * The provided path can be relative or absolute.
	 */
	public static final String TWITTER_CONSUMER_KEY = "xatkit.twitter.consumerKey";
	public static final String TWITTER_CONSUMER_SECRET = "xatkit.twitter.consumerSecret";
	public static final String TWITTER_ACCESS_TOKEN_KEY = "xatkit.twitter.accessToken";
	public static final String TWITTER_ACCESS_SECRET_TOKEN_KEY = "xatkit.twitter.accessSecretToken";

	private Twitter twitterService;

	public TwitterPlatform(XatkitCore XatkitCore, Configuration configuration) {
		super(XatkitCore, configuration);
		twitterService = TwitterFactory.getSingleton();
		twitterService.setOAuthConsumer(configuration.getString(TWITTER_CONSUMER_KEY),
				configuration.getString(TWITTER_CONSUMER_SECRET));
		AccessToken accessToken = new AccessToken(configuration.getString(TWITTER_ACCESS_TOKEN_KEY),
				configuration.getString(TWITTER_ACCESS_SECRET_TOKEN_KEY));
		twitterService.setOAuthAccessToken(accessToken);
	}

	public Twitter getTwitterService() {
		return this.twitterService;
	}
}
