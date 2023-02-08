### 简体中文 | [English](./README_en.md)

![License](https://img.shields.io/badge/license-MIT-green)
[![release](https://img.shields.io/github/v/release/kylelin1998/PickImgForDesktop)](https://github.com/kylelin1998/PickImgForDesktop/releases/latest)

## 简介
一款图床软件， 目前支持Github, Imgur图片上传， 通过全局快捷键(Ctrl + Alt + U)触发上传动作

快速上传你想上传的图片

软件下载地址: 
[下载地址-Github](https://github.com/kylelin1998/PickImgForDesktop/releases)
[下载地址-Gitee](https://gitee.com/kylelin1998/PickImgForDesktop/releases)

> 注意： 目前暂不支持多图片上传， 仅支持Windows系统， 在未来可能会支持Linux, MacOS系统的支持

## 关于
我的Telegram: <https://t.me/KyleLin1998>

我的Telegram频道(软件最新通知会在此频道通知， 欢迎关注): <https://t.me/KyleLin1998Channel>

我的邮箱: email@kylelin1998.com


## 未来开发计划
如果你有更多对软件功能的建议， 可以通过邮箱或者Telegram联系我


## 使用说明
软件打开后默认运行在系统托盘， 你可以在系统托盘看到它， 你可以右键软件托盘图标以打开
配置软件设置， 或是检查软件更新

以下是软件设置的说明:

**设置:**
* 语言 -> 目前支持简体中文 和 英语显示
* 开机自启动 -> 随着电脑启动以启动软件
* 当前上传方案 -> 上传到你想要的图床上， 比如你想要上传到Github， 那就选择Github
* 当前更新源 -> 软件自动更新依赖于Github, Gitee渠道， 如果你是国内用户请尽量选择Gitee
* 代理 -> 目前仅支持Http代理
* 文件名规则 -> 你可以自定义你想要的上传后的文件名， 具体规则见下方说明
* * ${baseName} -> 文件基础名称, 文件前缀, 默认名称
* * ${extension} -> 文件后缀
* * ${uuid} -> uuid文件基础名称
* * ${datetime} -> yyyy-MM-dd_HH:mm:ss.S文件基础名称
* * ${timestamp} -> 13位时间戳文件基础名称
* * ${md5} -> 基于文件流md5后的文件基础名称
* * ${sha1} -> 基于文件流sha1后的文件基础名称
* 剪贴板复制方案 -> 目前支持外链链接直接复制到剪贴板，当然你也可以选择Markdown格式的链接
* 剪贴板复制弹窗提示选择 -> 在复制前你可以通过手动选择何种格式的链接
* 上传前提示确认 -> 在触发全局快捷键上传动作之后会弹窗询问是否上传图片， 该功能防止误上传图片导致隐私泄露

**设置修改完, 需要点击保存配置**

**Github设置:**
* Owner -> Github用户名称或组织名称
* Repo -> 项目名称
* Branch -> 分支， 比如Main
* Path -> 项目内存储路径， 比如你在项目中想放置到test目录中， 那就填写/test
* Token -> Github Token
* Custom Domain -> 自定义上传后链接前缀路径， 比如你可以填写https://kylelin1998.com/img， 会自动在前缀路径后加上文件名

**Imgur设置:**
* Client Id -> 你可以在 <https://api.imgur.com/oauth2/addclient> 申请Client Id

![d81c4747d51af548a80b25c8ed7eb00dcc264093.png](https://i.imgur.com/8IFk2sd.png)