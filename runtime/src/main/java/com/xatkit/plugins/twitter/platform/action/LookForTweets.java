package com.xatkit.plugins.twitter.platform.action;

import java.util.ArrayList;
import java.util.List;

import com.github.seratch.jslack.api.model.Attachment;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.core.session.XatkitSession;
import com.xatkit.plugins.twitter.platform.TwitterPlatform;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Adds a property to the provided {@code node}.
 * <p>
 * This class relies on the {@link TwitterPlatform}'s to integrate with twitter.
 */
public class LookForTweets extends RuntimeAction<TwitterPlatform> {
	/**
	 * The query for tweets.
	 */
	private String query;

	/**
	 * Look for tweets {@link LookForTweets} action with the provided
	 * {@code runtimePlatform}, {@code session}, {@code query}.
	 *
	 * @param runtimePlatform the {@link TwitterPlatform} containing the database to
	 *                        store the created property
	 * @param session         the {@link XatkitSession} associated to this action
	 * @param query           the query to look for tweets
	 */
	public LookForTweets(TwitterPlatform runtimePlatform, XatkitSession session, String query) {
		super(runtimePlatform, session);
		this.query = query;
	}

	/**
	 * Post a tweet with the provided {@code content}.
	 * <p>
	 * This action opens a new conection to with twiter and posts a tweet.
	 *
	 * @return 0 if no errors; 1 if errors
	 */
	@Override
	protected Object compute() {
		String result = "";
		Twitter twitterService = this.runtimePlatform.getTwitterService();
		List<Attachment> attachments = new ArrayList<>();
		/*
		 * Gets the twitter API instances to lookfor tweets.
		 */
		try {
			Query query = new Query(this.query);
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
