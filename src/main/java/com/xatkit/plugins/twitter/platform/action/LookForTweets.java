package com.xatkit.plugins.twitter.platform.action;

import com.github.seratch.jslack.api.model.Attachment;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.twitter.platform.TwitterPlatform;
import lombok.NonNull;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;

/**
 * Searches for tweets with the provided search terms {@code query}. Right now it can only return the first page of the
 * results obtained.
 * <p>
 * This class relies on the {@link TwitterPlatform}'s to integrate with twitter.
 */
public class LookForTweets extends RuntimeAction<TwitterPlatform> {

    /**
     * The default number of tweets to return per page.
     * <p>
     * This value can be customized with {@link #LookForTweets(TwitterPlatform, StateContext, String, Integer)}. Note
     * that this value needs to be in {@code [1..#MAX_RESULTS_PER_PAGE]}.
     *
     * @see #MAX_RESULTS_PER_PAGE
     */
    private static final int DEFAULT_RESULTS_PER_PAGE = 15;

    /**
     * The maximum number of tweets this action can return per page.
     */
    private static final int MAX_RESULTS_PER_PAGE = 100;

    /**
     * The query of the search for tweets.
     */
    private String query;

    /**
     * The number of tweets to return per page, up to a maximum of 100. Defaults to 15.
     */
    private Integer resultsPerPage;

    /**
     * Seach for tweets {@link LookForTweets} action with the provided {@code platform}, {@code context},
     * {@code query}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param query    the query to search for tweets
     */
    public LookForTweets(@NonNull TwitterPlatform platform, @NonNull StateContext context, @NonNull String query) {
        super(platform, context);
        checkArgument(!query.isEmpty(), "Cannot construct a %s action with the provided query"
                + " %s, expected a non-null and not empty String", this.getClass().getSimpleName(), query);
        this.query = query;
        this.resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
    }

    /**
     * Seach for tweets {@link LookForTweets} action with the provided {@code platform}, {@code context},
     * {@code query}.
     *
     * @param platform       the {@link TwitterPlatform} containing this action
     * @param context        the {@link StateContext} associated to this action
     * @param query          the query to search for tweets
     * @param resultsPerPage the number of tweets to return per page
     */
    public LookForTweets(@NonNull TwitterPlatform platform, @NonNull StateContext context, @NonNull String query,
                         @NonNull Integer resultsPerPage) {
        super(platform, context);
        checkArgument(!query.isEmpty(), "Cannot construct a %s action with the provided query"
                + " %s, expected a non-null and not empty String", this.getClass().getSimpleName(), query);
        checkArgument((resultsPerPage > 0) && (resultsPerPage <= MAX_RESULTS_PER_PAGE), "Cannot construct a %s "
                + "action with the provided resultsPerPage %s, expected a non-null, greater than 0 and less than or "
                + "equal to 100 integer", this.getClass().getSimpleName(), resultsPerPage);
        this.query = query;
        this.resultsPerPage = resultsPerPage;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This action opens a new conection to with twiter and searches for tweets with search terms {@code query} and
     * return {@code resultsPerPage} resultss.
     *
     * @return 0 if no errors; 1 if errors
     */
    @Override
    protected Object compute() {
        String result = "";
        Twitter twitterService = this.runtimePlatform.getTwitterService();
        List<Attachment> attachments = new ArrayList<>();
        /*
         * Gets the twitter API instance and calls search method to retrieve tweets that are the result of the search.
         */
        try {
            Query query = new Query(this.query);
            query.setCount(resultsPerPage);

            QueryResult queryResult = twitterService.search(query);
            List<Status> tweetsList = queryResult.getTweets();
            if (!tweetsList.isEmpty()) {
                for (Status tweet : tweetsList) {
                    Attachment.AttachmentBuilder attachmentBuilder = Attachment.builder();
                    String authorName = tweet.getUser().getName() + " @" + tweet.getUser().getScreenName();
                    String text = tweet.getText();

                    attachmentBuilder.authorName(authorName);
                    attachmentBuilder.text(text);
                    attachmentBuilder.color("#1da1f2");
                    attachmentBuilder.ts(String.valueOf(tweet.getCreatedAt().getTime() / 1000));
                    attachments.add(attachmentBuilder.build());
                }
                return attachments;
            }
            result = "0";
        } catch (TwitterException e) {
            result = "1";
            e.printStackTrace();
        }
        return result;
    }
}
