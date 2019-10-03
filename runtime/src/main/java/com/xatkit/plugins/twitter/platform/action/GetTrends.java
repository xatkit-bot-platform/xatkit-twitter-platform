package com.xatkit.plugins.twitter.platform.action;

import java.util.ArrayList;
import java.util.List;
import static fr.inria.atlanmod.commons.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

import com.github.seratch.jslack.api.model.block.LayoutBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock.SectionBlockBuilder;
import com.github.seratch.jslack.api.model.block.composition.TextObject;
import com.github.seratch.jslack.api.model.block.composition.MarkdownTextObject.MarkdownTextObjectBuilder;
import com.github.seratch.jslack.api.model.block.composition.MarkdownTextObject;
import com.github.seratch.jslack.api.model.block.composition.PlainTextObject;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.core.session.XatkitSession;
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
     * Search the top worldwide trending topics {@link LookForTweets} action with the provided {@code runtimePlatform},
     * {@code session}.
     *
     * @param runtimePlatform the {@link TwitterPlatform} containing the database to store the created property
     * @param session         the {@link XatkitSession} associated to this action
     */
    public GetTrends(TwitterPlatform runtimePlatform, XatkitSession session) {
        super(runtimePlatform, session);
        this.woeid = 1;
    }

    /**
     * Search the top trending topics {@link GetTrends} action with the provided {@code runtimePlatform},
     * {@code session} and {@code woeid}.
     *
     * @param runtimePlatform the {@link TwitterPlatform} containing the database to store the created property
     * @param session         the {@link XatkitSession} associated to this action
     * @param woeid           the woeid of the location to return trending information for
     */
    public GetTrends(TwitterPlatform runtimePlatform, XatkitSession session, Integer woeid) {
        super(runtimePlatform, session);
        checkArgument(nonNull(woeid) && (woeid > 0),
                "Cannot construct a %s action with the provided resultsPerPage %s, expected a non-null and greater than 0 integer",
                this.getClass().getSimpleName(), woeid);
        this.woeid = woeid;
    }

    /**
     * Search the top worldwide trending topics {@link GetTrends} action with the provided {@code runtimePlatform},
     * {@code session} and {@code locationName}.
     * <p>
     * If no woeid is obtained for locationName, worldwide trends are returned.
     *
     * @param runtimePlatform the {@link TwitterPlatform} containing the database to store the created property
     * @param session         the {@link XatkitSession} associated to this action
     * @param locationName    the name of the location, in english, to return trending information for.
     */
    public GetTrends(TwitterPlatform runtimePlatform, XatkitSession session, String locationName) {
        super(runtimePlatform, session);
        checkArgument(nonNull(locationName) && !locationName.isEmpty(),
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
        List<LayoutBlock> layoutBlocks = new ArrayList<>();

        // First WOEID is 1 which correspond to "Worldwide"
        if (woeid > 0) {
            try {
                Trends trends = twitterService.getPlaceTrends(woeid);
                if (trends.getTrends().length > 0) {
                    SectionBlockBuilder sectionBlockBuilder = SectionBlock.builder();
                    sectionBlockBuilder.text(PlainTextObject.builder().text("Trending topics:").build());
                    List<TextObject> fields = new ArrayList<>();
                    int idx = 1;

                    for (Trend trend : trends.getTrends()) {
                        trendsList.add(trend);

                        // Blocks can't have more than 10 fields, so after 10 trends, we add a new block
                        if ((idx > 1) && ((idx - 1) % 10) == 0) {
                            sectionBlockBuilder.fields(fields);
                            layoutBlocks.add(sectionBlockBuilder.build());
                            sectionBlockBuilder = SectionBlock.builder();
                            fields = new ArrayList<>();
                        }

                        MarkdownTextObjectBuilder mrkdwnTextObject = MarkdownTextObject.builder();
                        String fieldText = idx + ". *<" + trend.getURL() + "|" + trend.getName() + ">*";
                        Integer tweetVolume = trend.getTweetVolume();
                        if (tweetVolume > 0) {
                            fieldText += "\n" + tweetVolume;
                        }
                        mrkdwnTextObject.text(fieldText);
                        fields.add(mrkdwnTextObject.build());

                        idx++;
                    }
                    sectionBlockBuilder.fields(fields);
                    layoutBlocks.add(sectionBlockBuilder.build());

                }

                if (trendsList.size() > 0) {
                    // return trendsList;
                    return layoutBlocks;
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
