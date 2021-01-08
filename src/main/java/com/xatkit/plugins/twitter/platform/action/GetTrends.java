package com.xatkit.plugins.twitter.platform.action;

import java.util.ArrayList;
import java.util.List;
import static fr.inria.atlanmod.commons.Preconditions.checkArgument;
import lombok.NonNull;

import com.github.seratch.jslack.api.model.Attachment;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.twitter.platform.TwitterPlatform;

import twitter4j.Location;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Returns the top 50 trending topics for a specific WOEID, if trending information is available for it. The response is
 * an array of trend objects that encode the name of the trending topic, the query parameter that can be used to search
 * for the topic on Twitter Search, and the Twitter Search URL. This information is cached for 5 minutes. Requesting
 * more frequently than that will not return any more data, and will count against rate limit usage.
 * <p>
 * This class relies on the {@link TwitterPlatform}'s {@link twitter4j.Twitter} to integrate with twitter.
 */
public class GetTrends extends RuntimeAction<TwitterPlatform> {

    /**
     * The 'Yahoo! Where On Earth ID' of the location to return trending information for. Worldwide information is
     * available by using 1 as the WOEID .
     */
    private Integer woeid;

    /**
     * Search the top worldwide trending topics {@link LookForTweets} action with the provided {@code platform},
     * {@code context}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     */
    public GetTrends(@NonNull TwitterPlatform platform, @NonNull StateContext context) {
        super(platform, context);
        this.woeid = 1;
    }

    /**
     * Search the top trending topics {@link GetTrends} action with the provided {@code platform}, {@code context} and
     * {@code woeid}.
     *
     * @param platform the {@link TwitterPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param woeid    the woeid of the location to return trending information for
     */
    public GetTrends(@NonNull TwitterPlatform platform, @NonNull StateContext context, @NonNull Integer woeid) {
        super(platform, context);
        checkArgument((woeid > 0),
                "Cannot construct a %s action with the provided resultsPerPage %s, expected a non-null and greater than 0 integer",
                this.getClass().getSimpleName(), woeid);
        this.woeid = woeid;
    }

    /**
     * Search the top worldwide trending topics {@link GetTrends} action with the provided {@code platform},
     * {@code context} and {@code locationName}.
     * <p>
     * If no woeid is obtained for locationName, worldwide trends are returned.
     *
     * @param platform     the {@link TwitterPlatform} containing this action
     * @param context      the {@link StateContext} associated to this action
     * @param locationName the name of the location, in english, to return trending information for.
     */
    public GetTrends(@NonNull TwitterPlatform platform, @NonNull StateContext context, @NonNull String locationName) {
        super(platform, context);
        checkArgument(!locationName.isEmpty(),
                "Cannot construct a %s action with the provided locationName %s, expected a non-null and not empty String",
                this.getClass().getSimpleName(), locationName);

        this.woeid = getWoeIdByLocationName(locationName);
    }

    /**
     * Retrieves the top 50 trending topics for a specific WOEID, if trending information is available for it. This
     * information is cached for 5 minutes. Requesting more frequently than that will not return any more data, and will
     * count against rate limit usage.
     * <p>
     * 
     * @return 0 if there are no trends for the location, or a list of the trends for the WOEID
     */
    @Override
    protected Object compute() {
        String result = "0";
        Twitter twitterService = this.runtimePlatform.getTwitterService();
        List<Trend> trendsList = new ArrayList<>();
        List<Attachment> attachments = new ArrayList<>();

        // First WOEID is 1 which correspond to "Worldwide"
        if (woeid > 0) {
            try {
                Trends trends = twitterService.getPlaceTrends(woeid);
                if (trends.getTrends().length > 0) {
                    for (Trend trend : trends.getTrends()) {
                        trendsList.add(trend);

                        Attachment.AttachmentBuilder attachmentBuilder = Attachment.builder();
                        Integer tweetVolume = trend.getTweetVolume();
                        if (tweetVolume > 0) {
                            attachmentBuilder.text("Tweet volume: " + tweetVolume);
                        } else {
                            attachmentBuilder.text("Tweet volume: undefined");
                        }
                        attachmentBuilder.title(trend.getName());
                        attachmentBuilder.titleLink(trend.getURL());
                        attachmentBuilder.color("#1da1f2");
                        attachments.add(attachmentBuilder.build());
                    }
                }

                if (trendsList.size() > 0) {
                    // return trendsList;
                    return attachments;
                }
            } catch (TwitterException e) {
                result = "1";
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Returns the WOEID that corresponds with the {@code locationName}, in english, if there are any for that location
     * name. It searches for {@code locationName} in the list of available trends from Twitter API.
     *
     * @param locationName the name of the location to search the WOEID
     */
    private Integer getWoeIdByLocationName(String locationName) {
        Twitter twitterService = this.runtimePlatform.getTwitterService();
        Integer woeid = -1;

        try {
            for (Location location : twitterService.getAvailableTrends()) {
                if (location.getName().toLowerCase().equals(locationName.toLowerCase())) {
                    woeid = location.getWoeid();
                    break;
                }
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return woeid;
    }

}
