package com.xatkit.plugins.twitter.platform.action;

import java.util.ArrayList;
import java.util.List;

import com.github.seratch.jslack.api.model.Attachment;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.core.session.XatkitSession;
import com.xatkit.plugins.twitter.platform.TwitterPlatform;

import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Shows the latest 50 incoming direct menssages.
 * <p>
 * This class relies on the {@link TwitterPlatform}'s {@link twitter4j.Twitter}
 * to integrate with twitter.
 */
public class ReceiveDM extends RuntimeAction<TwitterPlatform> {

	/**
	 * Shows the latest 50 incoming direct menssages {@link ReceiveDM} with the
	 * provided {@code runtimePlatform}, {@code session}.
	 *
	 * @param runtimePlatform the {@link TwitterPlatform} containing the database to
	 *                        store the created property
	 * @param session         the {@link XatkitSession} associated to this action
	 */
	public ReceiveDM(TwitterPlatform runtimePlatform, XatkitSession session) {
		super(runtimePlatform, session);
	}

	/**
	 * Retrieves the latest incoming direct messages. There's a limit of 50 direct
	 * messages that can be retrieved.
	 * 
	 * @return 0 if there are no messages, or a list of attachments with the DMs
	 *         formated for Slack
	 * 
	 *         TODO make formating flexibe create a formatter that is a parameter.
	 */
	@Override
	protected Object compute() {
		String result = "0";
		Twitter twitterService = this.runtimePlatform.getTwitterService();
		List<Attachment> attachments = new ArrayList<>();

		/*
		 * Gets the twitter API instance and calls getDirectMessages method to retrieve
		 * the latest 50 incoming direct messages.
		 */
		try {
			DirectMessageList DMList = twitterService.getDirectMessages(50);
			if (!DMList.isEmpty()) {
				for (DirectMessage DM : DMList) {
					if (!twitterService.getScreenName()
							.equals(twitterService.showUser(DM.getSenderId()).getScreenName())) {

						Attachment.AttachmentBuilder attachmentBuilder = Attachment.builder();
						String authorName = twitterService.showUser(DM.getSenderId()).getName() + " @"
								+ twitterService.showUser(DM.getSenderId()).getScreenName();
						String text = DM.getText();
						attachmentBuilder.authorName(authorName);
						attachmentBuilder.text(text);
						attachmentBuilder.color("#1da1f2");
						attachmentBuilder.ts(String.valueOf(DM.getCreatedAt().getTime() / 1000));
						attachments.add(attachmentBuilder.build());
					}
				}
			}
			
			if (attachments.size() > 0) {
				return attachments;
			}
		} catch (TwitterException e) {
			result = "1";
			e.printStackTrace();
		}
		return result;
	}
}
