package com.xatkit.plugins.twitter.platform.action;

import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.core.session.XatkitSession;
import com.xatkit.plugins.twitter.platform.TwitterPlatform;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Sends a direct message to a user whose username is {@code user}.
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
	 * Sends a direct messaage to a user {@link SendDM} action with the provided
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
	 * Sends a Direct Message to a user whose username is {@code user}.
	 * The content of the message is {@code text}.
	 * <p>
	 * 
	 * @return 0 if no errors; 1 if errors
	 */
	@Override
	protected Object compute() {
		int error = 0;
		Twitter twitterService = this.runtimePlatform.getTwitterService();
		/*
		 * Gets the twitter API instance and calls the sendDirectMessage method
		 * to send a direct message to the user @code user} with the content {@code text}.
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
