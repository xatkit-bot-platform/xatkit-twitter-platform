package com.xatkit.plugins.twitter.platform.action;

import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.twitter.platform.TwitterPlatform;
import lombok.NonNull;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Sends a direct message to a user whose username is {@code user}.
 * <p>
 * This class relies on the {@link TwitterPlatform}'s {@link twitter4j.Twitter} to integrate with twitter.
 */
public class SendDM extends RuntimeAction<TwitterPlatform> {

    /**
     * The content of the direct message to send.
     */
    private String text;

    /**
     * The user to send a direct message to.
     */
    private String user;

    /**
     * Sends a direct message to a user {@link SendDM} action with the provided {@code platform}, {@code context},
     * {@code user}, {@code text}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param user     the user to send the message
     * @param text     the content of the message
     */
    public SendDM(@NonNull TwitterPlatform platform, @NonNull StateContext context, @NonNull String user,
                  @NonNull String text) {
        super(platform, context);
        this.text = text;
        this.user = user;
    }

    /**
     * Sends a Direct Message to a user whose username is {@code user}. The content of the message is {@code text}.
     * <p>
     *
     * @return 0 if no errors; 1 if errors
     */
    @Override
    protected Object compute() {
        int error = 0;
        Twitter twitterService = this.runtimePlatform.getTwitterService();
        /*
         * Gets the twitter API instance and calls the sendDirectMessage method to send a direct message to the
         * user @code user} with the content {@code text}.
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
