### [简体中文](./README.md) | English

![License](https://img.shields.io/badge/license-MIT-green)
[![release](https://img.shields.io/github/v/release/kylelin1998/PickImgForDesktop)](https://github.com/kylelin1998/PickImgForDesktop/releases/latest)

## Introduction
A free image hosting software. 
So far, supports Github, and Imgur uploads. 
Trigger action upload by global shortcut key.

Fast upload your image.

> NOTE： So far, temporarily does not support multiple images to upload, only supports Windows system to run this software. Maybe in the future will support Linux or MacOS systems.

## About
My Telegram: <https://t.me/KyleLin1998>

My Telegram Channel(Software, if have a new version, will be in this channel to notify everyone. Welcome to subscribe to it.): <https://t.me/KyleLin1998Channel>

My email: email@kylelin1998.com


## Develop plans for the future
You can contact me by email or my Telegram if you have advice about this software.


## Usage
When you opened this software will appear in the system tray, then you can see it.
You can use your mouse and right-click it, you will see more software settings or check updates.

Below is the software how to usage:

**Settings:**
* Language -> So far support both languages, English and Chinese
* Startup -> Your computer completely started after will start the software
* Current Scheme -> Upload to you wants to Image Hosting. For example, if you want Github way then you just choose Github.
* Current Upgrade Source -> Software update ways,  according to your network to choose the right way.
* Proxy -> So far only support HTTP proxy
* Filename Rule -> You can custom file name, the rule below
* * ${baseName} -> file base name, prefix, default
* * ${extension} -> file suffix name
* * ${uuid} -> UUID base name
* * ${datetime} -> yyyy-MM-dd_HH:mm:ss.S base name
* * ${timestamp} -> 13bit system timestamp base name
* * ${md5} -> MD5 base name after encoding file bytes stream.
* * ${sha1} -> SHA1 base name after encoding file bytes stream.
* Copy Current Scheme -> support URL after upload to copy to the system clipboard
* Copy Prompt For choose -> manual to choose which links type you uploaded
* Prompt Before Upload To Confirm -> When you trigger the global shortcut key after will appear whether upload prompt. This feature avoid your personal information may be revealed.

**You must click the 'Save Config' button to save if you changed the config**

**Github Settings:**
* Owner -> GitHub personal name or organization name
* Repo -> Project name
* Branch -> For example, Main
* Path -> Image store path in Github project
* Token -> Github Token
* Custom Domain -> You can custom prefix of URL. For example, if you input 'https://kylelin1998.com/img', then will automatically be in the prefix after adding the filename.

**Imgur Settings:**
* Client Id -> You can in <https://api.imgur.com/oauth2/addclient> to apply Client Id