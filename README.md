# WechatBot
[wxBot](https://github.com/liuwons/wxBot) 的Java版本,是用Java包装Web微信协议实现的微信机器人框架

## 开发进度
- [ ] 联系人消息
    - [x] 回复文字消息
    - [x] 回复图片消息
    - [ ] 回复语音
    - [ ] 回复附件
- [ ] 群消息
    - [ ] 回复文字消息
    - [ ] 回复图片消息
    
## 使用
新类继承WechatBot，然后重写方法，
```java
public void handleMsgAll(int msgType, Map<String, Object> msg)
```
其他详细内容可以参考DemoBot类。

## 已完成BOT命令:

- 城市实时天气: /天气 [城市名称]
- 随机获取头像: /头像 [任意文字]
- BOT的使用帮助: /帮助