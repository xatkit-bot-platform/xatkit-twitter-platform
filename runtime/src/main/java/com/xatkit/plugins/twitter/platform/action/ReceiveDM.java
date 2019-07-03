package com.xatkit.plugins.twitter.platform.action;

import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

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
	 * Shows the latest 50 incoming direct menssages {@link ReceiveDM} with the provided
	 * {@code runtimePlatform}, {@code session}.
	 *
	 * @param runtimePlatform the {@link TwitterPlatform} containing the database to
	 *                        store the created property
	 * @param session         the {@link XatkitSession} associated to this action
	 */
	public ReceiveDM(TwitterPlatform runtimePlatform, XatkitSession session) {
		super(runtimePlatform, session);
	}

	/**
	 * Retrieves the latest incoming direct messages.
	 * There's a limit of 50 direct messages that can be retrieved.
	 * 
	 * @return 0 if no errors; a string with the DMs formated for Slack
	 * // TODO make formating flexibe create a formatter that is a parameter.
	 */
	@Override
	protected Object compute() {
		String result = "";
		Twitter twitterService = this.runtimePlatform.getTwitterService();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
		JSONArray jsonAttachments = new JSONArray();
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonContent = null;
		/*
		 * Gets the twitter API instance and calls getDirectMessages method
		 * to retrieve the latest 50 incoming direct messages.
		 */
		try {
			DirectMessageList DMList = twitterService.getDirectMessages(50);
			if (!DMList.isEmpty()) {
				for (DirectMessage DM : DMList) {
					if (!twitterService.getScreenName()
							.equals(twitterService.showUser(DM.getSenderId()).getScreenName())) {

						jsonContent = new JSONObject();
						String pretext = "*" + twitterService.showUser(DM.getSenderId()).getName() + "* @"
								+ twitterService.showUser(DM.getSenderId()).getScreenName() + " `"
								+ dateFormatter.format(DM.getCreatedAt()) + "`";
						String text = DM.getText();

						jsonContent.put("pretext", pretext);
						jsonContent.put("text", text);
						jsonAttachments.put(jsonContent);
					}
				}
				jsonResult.put("attachments", jsonAttachments);

				if (jsonAttachments.length() > 0) {
					return jsonResult.toString();
				}

				result = "The are no DMs for you";
			} else {
				result = "The are no DMs at all, please communicate with someone";
			}
		} catch (TwitterException e) {
			result = "1";
			e.printStackTrace();
		}
		return result;
	}
}
