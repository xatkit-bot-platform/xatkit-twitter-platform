# Changelog

All notable changes for the Twitter platform will be documented in this file.

Note that there is no changelog available for the initial release of the platform (1.0.0), you can find the release notes [here](https://github.com/xatkit-bot-platform/xatkit-twitter-platform/releases).

The changelog format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/v2.0.0.html)

## Unreleased

## [2.0.0] - 2019-12-01

### Changed
- Action parameters and return are now statically typed. **This change breaks the public API**: execution models relying on the generic `Object` type for parameter and return now need to cast values to the expected type. (e.g. `ChatPlatform.Reply(message)` now requires that `message` is a `String`, this can be fixed with the following syntax `ChatPlatform.Reply(message as String)`).  


## [1.0.0] - 2019-10-10 

See the release notes [here](https://github.com/xatkit-bot-platform/xatkit-twitter-platform/releases).

