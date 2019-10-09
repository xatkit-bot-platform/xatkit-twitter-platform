Xatkit Twitter Platform
=====

[![License Badge](https://img.shields.io/badge/license-EPL%202.0-brightgreen.svg)](https://opensource.org/licenses/EPL-2.0)
[![Build Status](https://travis-ci.com/xatkit-bot-platform/xatkit-uml-platform.svg?branch=master)](https://travis-ci.com/xatkit-bot-platform/xatkit-uml-platform)
[![Wiki Badge](https://img.shields.io/badge/doc-wiki-blue)](https://github.com/xatkit-bot-platform/xatkit-releases/wiki/Xatkit-Twitter-Platform)


Send and receive messages from Twitter, and search or post Tweets.

## Providers

The Twitter platform does not define any provider.

## Actions

| Action  | Parameters | Return                                  | Return Type | Description                                     |
| ------- | ---------- | --------------------------------------- | ----------- | ----------------------------------------------- |
| PostATweet | - `content` (**String**): the content of the tweet to post          | `0` if the tweet has been posted, `1` otherwise   | Integer      | Posts a tweet on behalf of the configured user with the provided `content` |
| SendDM | - `user` (**String**): the twitter user to send a direct message to<br/>- `text` (**String**): the content of the direct message | `0` if the direct message has been sent, `1` otherwise | Integer | Sends a direct message to the provided `user` with the given `text` |
| ReceiveDM | - | A list of Slack [Attachments](https://github.com/seratch/jslack) containing the latest direct messages received | [List\<Attachment\>](https://github.com/seratch/jslack) | Retrieves the latest direct messages received by the configured user |
| ReceiveDM |  - `messagesPerPage` (**Integer**): the number of messages to retrieve per page up to a maximum of 50 | A list of Slack [Attachments](https://github.com/seratch/jslack) containing the latest `messagesPerPage` direct messages received | [List\<Attachment\>](https://github.com/seratch/jslack) | Retrieves the latest `messagesPerPage` direct messages received by the configured user |
| LookForTweets | - `query` (**String**): the search terms used to retrieve tweets | A list of Slack [Attachments](https://github.com/seratch/jslack) containing the tweets matching the provided `query` | [List\<Attachment\>](https://github.com/seratch/jslack) | Retrieves a series of tweets matching the provided search `query` |
| LookForTweets | - `query` (**String**): the search terms used to retrieve tweets<br/>- `resultsPerPage` (**Integer**): the number of tweets to retrieve per page up to a maximum of 100  | A list of Slack [Attachments](https://github.com/seratch/jslack) containing the tweets matching the provided `query` | [List\<Attachment\>](https://github.com/seratch/jslack) | Retrieves a series of tweets matching the provided search `query` |
| GetTrends | - | A list of Slack [Attachments](https://github.com/seratch/jslack) containing the top 50 trending topics worldwide if there are any, `0` if there aren't, `1` in case of an error | [List\<Attachment\>](https://github.com/seratch/jslack) | Retrieves the top 50 trending topics worldwide |
| GetTrends | - `woeid` (**Integer**): the WOEID (Where on Earth ID) of the location to ask for trends | A list of Slack [Attachments](https://github.com/seratch/jslack) containing the top 50 trending topics for the location if there are any, `0` if there aren't, `1` in case of an error | [List\<Attachment\>](https://github.com/seratch/jslack) | Retrieves the top 50 trending topics of the location identified by `woeid`  |
| GetTrends | - `locationName` (**String**): the (english) name of the location to ask for trends | A list of Slack [Attachments](https://github.com/seratch/jslack) containing the top 50 trending topics for the location if there are any, `0` if there aren't, `1` in case of an error | [List\<Attachment\>](https://github.com/seratch/jslack) | Retrieves the top 50 trending topics of the location with (english) name `locationName`  |


## Options

The Twitter platform supports the following configuration options

| Key                  | Values | Description                                                  | Constraint    |
| -------------------- | ------ | ------------------------------------------------------------ | ------------- |
| `xatkit.twitter.consumerKey` | String | The consumer key of the Twitter app used by Xatkit to deploy the bot | **Mandatory** |
| `xatkit.twitter.consumerSecret` | String | The consumer secret of the Twitter app used by Xatkit to deploy the bot | **Mandatory** |
| `xatkit.twitter.accessToken` | String | The access token of the Twitter app used by Xatkit to deploy the bot | **Mandatory** |
| `xatkit.twitter.accessSecretToken` | String | The access token secret of the Twitter app used by Xatkit to deploy the bot | **Mandatory** |

To fill these options you need to have a [Twitter developer account](https://developer.twitter.com/) and create an app. The different keys and access tokens can be found at this location: https://developer.twitter.com/en/apps/{YOURAPPID}.

**Note**

*SendDM* and *ReceiveDM* actions require additional permissions that must be set in your app settings (direct message permissions are not granted by default).
