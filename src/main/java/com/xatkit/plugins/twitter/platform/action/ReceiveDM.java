package com.xatkit.plugins.twitter.platform.action;

import com.github.seratch.jslack.api.model.Attachment;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.twitter.platform.TwitterPlatform;
import lombok.NonNull;
import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;

/**
 * Shows the latest incoming direct messages. Right now it can only return the first page of the results obtained
 * <p>
 * This class relies on the {@link TwitterPlatform}'s {@link twitter4j.Twitter} to integrate with twitter.
 */
public class ReceiveDM extends RuntimeAction<TwitterPlatform> {

    /**
     * The default number of direct messages to return per page.
     * <p>
     * This value can be customized with {@link #ReceiveDM(TwitterPlatform, StateContext, Integer)}. Note that this
     * value needs to be in {@code [1..#MAX_MESSAGES_PER_PAGE]}.
     *
     * @see #MAX_MESSAGES_PER_PAGE
     */
    private static final int DEFAULT_MESSAGES_PER_PAGE = 20;

    /**
     * The maximum number of direct messages this action can return per page.
     */
    private static final int MAX_MESSAGES_PER_PAGE = 50;

    /**
     * The number of messages to retrieve per page, up to a maximum of 50. Defaults to 20.
     */
    private Integer messagesPerPage;

    /**
     * Shows the latest incoming direct messages {@link ReceiveDM} with the provided {@code platform}, {@code context}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     */
    public ReceiveDM(@NonNull TwitterPlatform platform, @NonNull StateContext context) {
        super(platform, context);
        this.messagesPerPage = DEFAULT_MESSAGES_PER_PAGE;
    }

    /**
     * Shows the latest incoming direct messages {@link ReceiveDM} with the provided {@code platform}, {@code context}.
     *
     * @param platform        the {@link TwitterPlatform} containing this action
     * @param context         the {@link StateContext} associated to this action
     * @param messagesPerPage the number of messages to retrieve per page
     */
    public ReceiveDM(@NonNull TwitterPlatform platform, @NonNull StateContext context,
                     @NonNull Integer messagesPerPage) {
        super(platform, context);
        checkArgument((messagesPerPage > 0) && (messagesPerPage <= MAX_MESSAGES_PER_PAGE), "Cannot construct a %s "
                + "action with the provided messagesPerPage %s, expected a non-null, greater than 0 and less "
                + "than or equal to 50 integer", this.getClass().getSimpleName(), messagesPerPage);
        this.messagesPerPage = messagesPerPage;
    }

    /**
     * Retrieves the latest incoming direct messages. Right now only the first page i retrieved.
     *
     * @return 0 if there are no messages, or a list of attachments with the DMs formated for Slack
     * <p>
     * TODO make formatting flexible create a formatter that is a parameter.
     */
    @Override
    protected Object compute() {
        String result = "0";
        Twitter twitterService = this.runtimePlatform.getTwitterService();
        List<Attachment> attachments = new ArrayList<>();

        try {
            DirectMessageList dmList = twitterService.getDirectMessages(messagesPerPage);
            if (!dmList.isEmpty()) {
                for (DirectMessage dm : dmList) {
                    if (!twitterService.getScreenName()
                            .equals(twitterService.showUser(dm.getSenderId()).getScreenName())) {

                        Attachment.AttachmentBuilder attachmentBuilder = Attachment.builder();
                        String authorName = twitterService.showUser(dm.getSenderId()).getName() + " @"
                                + twitterService.showUser(dm.getSenderId()).getScreenName();
                        String text = dm.getText();
                        attachmentBuilder.authorName(authorName);
                        attachmentBuilder.text(text);
                        attachmentBuilder.color("#1da1f2");
                        attachmentBuilder.ts(String.valueOf(dm.getCreatedAt().getTime() / 1000));
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
