# twitter-platform
Send and receive messages from Twitter, and search or post Tweets.

## Overview

There are 4 twitter actions developed for this implementation:
- **PostATweet** to ask the chatbot to post a tweet on your behalf with an specific content.
- **SendDM** to ask the chatbot to send a direct message on your behalf with an specific content.
- **ReceiveDM** to ask the chatbot to show your latest direct messages sent to you.
- **LookForTweets** to ask the chatbot to show you a series of tweet related to a search term that is indicated to the chatbot.

## Installation

You need to have a working Xatkit installation to use this platform. You can download the latest release [here](https://xatkit-bot-platform.github.io/xatkit-docs/releases/v1.0.1/update/), or build Xatkit from sources by following [this tutorial](https://github.com/xatkit-bot-platform/xatkit/wiki/Installation).

You also need to build the Twitter platform jar from the sources. You can do it by running the following commands from the project's root directory:

```bash
cd runtime
mvn install
```
This will create a `twitter-platform-jar-with-dependencies.jar` file in the `runtime/target` folder.

## Execution

The Twitter platform is not part of Xatkit core platforms, and need to be manually added to the classpath when starting the application. You can run this command (windows version) to run Xatkit with the Twitter platform.

```bash
java -cp "xatkit.jar;runtime/target/twitter-platform-jar-with-dependencies.jar" com.xatkit.Xatkit TwitterBot.properties
```

> **Useful Tips** You can provide an absolute path for the `jar` files to include in the classpath.

## Requirements

- **twitter** you'll need to create a twitter developer account and create an APP https://developer.twitter.com/}
- **SendDM** the user that you are trying to reach must be someone who is following you and that you also follow.
- **ReceiveDM** you'll need to set a permission to be able to do this from the chatbot. This permission is set in the user account that is used by the chatbot (https://developer.twitter.com/en/apps/{YOURAPPID}).
- **Propierties** The properties file has to include these extra parameters:
    - xatkit.twitter.consumerKey 
    - xatkit.twitter.consumerSecret 
    - xatkit.twitter.accessToken
    - xatkit.twitter.accessSecretToken 
These can be found on the https://developer.twitter.com/en/apps/{YOURAPPID} under keys and tokens tab
