package com.xatkit.plugins.twitter.platform.action;

import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.core.session.XatkitSession;
import com.xatkit.plugins.twitter.platform.TwitterPlatform;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Sends a tweet to a user {@code user}.
 * <p>
 * This class relies on the {@link TwitterPlatform}'s {@link twitter4j.Twitter}
 * to integrate with twitter.
 */
public class SendDM extends RuntimeAction<TwitterPlatform> {
	/**
	 * The inputs for the DM.
	 */
	private String text;
	private String user;

	/**
	 * Sends a tweet to a user {@link SendDM} action with the provided
	 * {@code runtimePlatform}, {@code session}, {@code user}, {@code text}.
	 *
	 * @param runtimePlatform the {@link TwitterPlatform} containing the database to
	 *                        store the created property
	 * @param session         the {@link XatkitSession} associated to this action
	 * @param user            the user to send the message
	 * @param text            the contentof the message
	 */
	public SendDM(TwitterPlatform runtimePlatform, XatkitSession session, String user, String text) {
		super(runtimePlatform, session);
		this.text = text;
		this.user = user;
	}

	/**
	 * Sends a DM to a user {@code user} with {@code text}.
	 * <p>
	 * 
	 * @return 0 if no errors; 1 if errors
	 */
	@Override
	protected Object compute() {
		int error = 0;
		Twitter twitterService = this.runtimePlatform.getTwitterService();
		/*
		 * Gets the twitter API instances to lookfor tweets.
		 */
		try {
			twitterService.sendDirectMessage(this.user, this.text);
		} catch (TwitterException e) {
			error = 1;
			e.printStackTrace();
		}
		return error;
	}
}
