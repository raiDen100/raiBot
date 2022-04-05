
# raiBot


Simple Discord music bot with Spotify support. It is not ready for serving to thousands of servers. Only use this bot for your personal needs.

## Features

#### Supported services

* Spotify
* YouTube
* SoundCloud
* Bandcamp
* Vimeo
* Twitch streams
* HTTP URLs

#### Audio filters
* Bassboost
* Nightcore
* Vaporwave
* Speed change
* Earrape
#### Button player
![bot player](https://i.imgur.com/0X9i3Cs.png)

## What's needed
##### 1. Java 16 or newer(never tested on lower versions)
##### 2. Application at [Discord](https://discord.com/developers/applications)
- Bot Token
##### 3. Application at [Spotify](https://developer.spotify.com/)
- Client ID
- Client Secret
##### 4. Add redirect URL in Spotify app settings. It can be any URL, but has to be the same as the URL you provide in config
##### 5. Youtube cookies for videos with age restriction
- [How to get these cookies](https://github.com/Walkyst/lavaplayer-fork/issues/18)


## How to run
#### JAR
##### 1. `git clone https://github.com/raiDen100/raiBot.git`
##### 2. `mvn package`
 * It will make two jar files in `/target` directory
##### 3. Set configs in `src/main/resources/configs`
* rename **config.properties.example** to **config.properties**
* fill config with your credentials
##### 4. Run:
* `java -jar file-with-dependencies.jar` for development config
* `java -jar file-with-dependencies.jar production` for production config
#### Docker
Set config files see [here](#3-set-configs-in-srcmainresourcesconfigs) in the JAR guide  

`mvn jib:build` will build and push Docker image to a specified registry. I do not recommend pushing an image to a public registry, because all configs will be inside.  

`mvn jib:dockerBuild` will build an image to a Docker daemon on your local machine
* Docker registry can be changed [here](https://github.com/raiDen100/raiBot/blob/master/pom.xml#L120)
* **By default Docker image run on production config**, this can be changed [here](https://github.com/raiDen100/raiBot/blob/master/pom.xml#L129)

## Commands

| Command | Aliases     | Parameters                       | Description|
| :-------- | :---- | :-------------------------------- |:-|
| `play`    | `p` | search query/url |Play audio from source|
| `join`    |  | n/a |Join your voice channel|
| `leave`    |  | n/a |Leave voice channel|
| `stop`    |  | n/a |Stop music and clear queue|
| `help`    |  | n/a |Show all commands|
| `skip`    |  | n/a |Skip current song|
| `loop`    | `repeat` | n/a |Loop current song|
| `loopq`    | `lq, rq` | n/a |Loop queue|
| `queue`    | `q` | n/a |Show songs in queue|
| `pause`    | `halp` | n/a |Pause playback|
| `resume`    | `unpause` | n/a |Resume playback|
| `seek`    |  | seconds |Move to timestamp|
| `shuffle`    |  | n/a | Shuffle queue|
| `skip`    |  | n/a |Skip current song|
| `counter`    |  | n/a |Counter of loop|
| `bassboost`    | `bb` | multiplier/reset |Apply bassboost|
| `nightcore`    | `nc` | speed(%)/reset |Apply nightcore|
| `vaporwave`    | `vw` | speed(%)/reset |Apply vaporwave|
| `volume`    | `er, earrape` | volume(%)/reset |Set volume|
| `reset`    |  | n/a |Reset all filters|
