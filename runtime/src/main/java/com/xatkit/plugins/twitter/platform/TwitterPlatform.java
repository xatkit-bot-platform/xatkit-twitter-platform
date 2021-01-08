package com.xatkit.plugins.twitter.platform;

import com.xatkit.core.XatkitCore;
import com.xatkit.core.platform.RuntimePlatform;
import com.xatkit.plugins.twitter.platform.action.PostAtweet;
import com.xatkit.plugins.twitter.platform.action.SendDM;
import com.xatkit.plugins.twitter.platform.action.ReceiveDM;
import com.xatkit.plugins.twitter.TwitterUtils;
import com.xatkit.plugins.twitter.platform.action.LookForTweets;
import com.xatkit.plugins.twitter.platform.action.GetTrends;
import com.xatkit.execution.StateContext;
import com.xatkit.core.platform.action.RuntimeActionResult;

import lombok.NonNull;

import org.apache.commons.configuration2.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * A {@link RuntimePlatform} class that connects and interacts with the Twitter API.
 */
public class TwitterPlatform extends RuntimePlatform {

    private Twitter twitterService;

    /**
     * {@inheritDoc}
     * <p>
     * This method initializes the underlying {@link Twitter} service.
     * 
     */
    @Override
    public void start(XatkitCore xatkitCore, Configuration configuration) {
        super.start(xatkitCore, configuration);
        twitterService = TwitterFactory.getSingleton();
        twitterService.setOAuthConsumer(configuration.getString(TwitterUtils.TWITTER_CONSUMER_KEY),
                configuration.getString(TwitterUtils.TWITTER_CONSUMER_SECRET));
        AccessToken accessToken = new AccessToken(configuration.getString(TwitterUtils.TWITTER_ACCESS_TOKEN_KEY),
                configuration.getString(TwitterUtils.TWITTER_ACCESS_SECRET_TOKEN_KEY));
        twitterService.setOAuthAccessToken(accessToken);
    }

    /**
     * Search the top worldwide trending topics {@link LookForTweets}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     */
    public void GetTrends(@NonNull StateContext context) {
        GetTrends action = new GetTrends(this, context);
        RuntimeActionResult result = executeRuntimeAction(action);
    }

    /**
     * Search the top trending topics {@link GetTrends}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param woeid    the woeid of the location to return trending information for
     */
    public void GetTrends(@NonNull TwitterPlatform platform, @NonNull StateContext context, @NonNull Integer woeid) {
        GetTrends action = new GetTrends(this, context, woeid);
        RuntimeActionResult result = executeRuntimeAction(action);
    }

    /**
     * Search the top worldwide trending topics {@link GetTrends}.
     * <p>
     * If no woeid is obtained for locationName, worldwide trends are returned.
     *
     * @param platform     the {@link TwitterPlatform} containing this action
     * @param context      the {@link StateContext} associated to this action
     * @param locationName the name of the location, in english, to return trending information for.
     */
    public void GetTrends(@NonNull StateContext context, @NonNull String locationName) {
        GetTrends action = new GetTrends(this, context, locationName);
        RuntimeActionResult result = executeRuntimeAction(action);
    }

    /**
     * Seach for tweets {@link LookForTweets}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param query    the query to search for tweets
     */
    public void LookForTweets(@NonNull StateContext context, @NonNull String query) {
        LookForTweets action = new LookForTweets(this, context, query);
        RuntimeActionResult result = executeRuntimeAction(action);
    }

    /**
     * Seach for tweets {@link LookForTweets} paginated.
     *
     * @param platform       the {@link TwitterPlatform} containing this action
     * @param context        the {@link StateContext} associated to this action
     * @param query          the query to search for tweets
     * @param resultsPerPage the number of tweets to return per page
     */
    public void LookForTweets(@NonNull StateContext context, @NonNull String query, @NonNull Integer resultsPerPage) {
        LookForTweets action = new LookForTweets(this, context, query, resultsPerPage);
        RuntimeActionResult result = executeRuntimeAction(action);
    }

    /**
     * Post a new tweet {@link PostATweet}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param content  the content of the tweet to post
     */
    public void PostAtweet(@NonNull StateContext context, String content) {
        PostAtweet action = new PostAtweet(this, context, content);
        RuntimeActionResult result = executeRuntimeAction(action);
    }

    /**
     * Shows the latest incoming direct menssages {@link ReceiveDM}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     */
    public void ReceiveDM(@NonNull StateContext context) {
        ReceiveDM action = new ReceiveDM(this, context);
        RuntimeActionResult result = executeRuntimeAction(action);
    }

    /**
     * Shows the latest incoming direct menssages {@link ReceiveDM} paginated.
     *
     * @param platform        the {@link TwitterPlatform} containing this action
     * @param context         the {@link StateContext} associated to this action
     * @param messagesPerPage the number of messages to retrieve per page
     */
    public void ReceiveDM(@NonNull StateContext context, @NonNull Integer messagesPerPage) {
        ReceiveDM action = new ReceiveDM(this, context, messagesPerPage);
        RuntimeActionResult result = executeRuntimeAction(action);
    }

    /**
     * Sends a direct messaage to a user {@link SendDM}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param user     the user to send the message
     * @param text     the contentof the message
     */
    public void SendDM(@NonNull StateContext context, @NonNull String user, @NonNull String text) {
        SendDM action = new SendDM(this, context, user, text);
        RuntimeActionResult result = executeRuntimeAction(action);
    }

    public Twitter getTwitterService() {
        return this.twitterService;
    }
}
