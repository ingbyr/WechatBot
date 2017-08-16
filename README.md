# WechatBot
[wxBot](https://github.com/liuwons/wxBot) 的Java版本,是用Java包装Web微信协议实现的微信机器人框架

## 开发进度
- [ ] 回复消息类型
    - [x] 回复文字消息
    - [x] 回复图片消息
    - [ ] 回复语音
    - [ ] 回复附件
- [ ] 检测消息类型
    - [x] 联系人消息
    - [x] 自己发送给自己的消息
    - [ ] 群消息
    - [ ] 公众号消息
    - [ ] 特殊公众号消息
    
## 使用
将WechatBot.jar作为依赖导入项目中，然后继承 WechatBot 类，重写方法：
```java
public void handleMsgAll(int msgType, Map<String, Object> msg)
```

例子：
```java
import com.ingbyr.wechatbot.WechatBot;

import java.util.Map;

public class Demobot extends WechatBot {

    @Override
    public void handleMsgAll(int msgType, Map<String, Object> msg) {
        super.handleMsgAll(msgType, msg);
        String msgContent = msg.get("Content").toString();
        System.out.println("msgType: " + msgType);
        System.out.println("msgContent: " + msgContent);

        if (msgType == 1 && msgContent.startsWith("/")) {
            // 自己发送给自己的消息
            System.out.println(("自己发来的消息: " + msgContent));
            sendMsgByUid("Test Wechatbot", msg.get("FromUserName").toString());

        }
    }

    public static void main(String[] args) {
        Demobot demobot = new Demobot();
        demobot.setServerRun(false);
        demobot.setDebugFile(false);
        try {
            demobot.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
程序输出
```
2017-08-16 13:16:59,182 [main] INFO  com.ingbyr.wechatbot.WechatBot.generateQrcode - 请扫描二维码登陆微信
2017-08-16 13:16:59,198 [main] INFO  com.ingbyr.wechatbot.WechatBot.generateQrcode - 二维码保存在　C:\Users\HP\WechatBotRun\qrcode.png
2017-08-16 13:17:03,066 [main] INFO  com.ingbyr.wechatbot.WechatBot.waitForLogin - 请点击确认按钮
2017-08-16 13:17:04,158 [main] INFO  com.ingbyr.wechatbot.WechatBot.waitForLogin - 请点击确认按钮
2017-08-16 13:17:05,236 [main] INFO  com.ingbyr.wechatbot.WechatBot.waitForLogin - 登陆成功
2017-08-16 13:17:08,330 [main] INFO  com.ingbyr.wechatbot.WechatBot.getContact - 欢迎:　【你的微信名】
2017-08-16 13:17:08,720 [main] INFO  com.ingbyr.wechatbot.WechatBot.procMsg    - Wechat Bot 启动完成
自己发来的消息: /
msgType: 1
msgContent: 
2017-08-16 13:17:31,196 [main] INFO  com.ingbyr.wechatbot.WechatBot.procMsg    - 从其它设备上登了网页微信
```