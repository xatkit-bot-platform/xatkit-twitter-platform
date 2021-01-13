package com.xatkit.plugins.twitter;


import com.xatkit.core.XatkitBot;
import com.xatkit.plugins.twitter.platform.TwitterPlatform;
import org.apache.commons.configuration2.Configuration;

/**
 * An utility interface that holds Twitter-related helpers.
 * <p>
 * This class defines the xatkit configuration keys to store the Twitter app API token and consumer.
 */
public interface TwitterUtils {

    /**
     * The {@link Configuration} key to store the Twitter app API token.
     *
     * @see TwitterPlatform#start(XatkitBot, Configuration)
     */
    String TWITTER_ACCESS_TOKEN_KEY = "xatkit.twitter.accessToken";

    /**
     * The {@link Configuration} key to store the Twitter app API secret token.
     *
     * @see TwitterPlatform#start(XatkitBot, Configuration)
     */
    String TWITTER_ACCESS_SECRET_TOKEN_KEY = "xatkit.twitter.accessSecretToken";

    /**
     * The {@link Configuration} key to store the Twitter app API consumer.
     *
     * @see TwitterPlatform#start(XatkitBot, Configuration)
     */
    String TWITTER_CONSUMER_KEY = "xatkit.twitter.consumerKey";

    /**
     * The {@link Configuration} key to store the Twitter app API consumer secret.
     *
     * @see TwitterPlatform#start(XatkitBot, Configuration)
     */
    String TWITTER_CONSUMER_SECRET = "xatkit.twitter.consumerSecret";

}
