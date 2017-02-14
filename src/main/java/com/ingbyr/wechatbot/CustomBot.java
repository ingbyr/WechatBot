package com.ingbyr.wechatbot;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created on 17-2-14.
 *
 * @author ing
 * @version 1
 */
public class CustomBot extends WechatBot {
    private static Logger log = LoggerFactory.getLogger(CustomBot.class);

    @Override
    public void handleMsgAll(int msgType, Map<String, Object> msg) {
        String msgContent = msg.get("Content").toString();
        log.trace("msgType: " + msgType);
        log.trace("msgContent: " + msgContent);

        if (msgType == 1 && StringUtils.startsWith(msgContent, "/")) {
            // 自己发送给自己的消息
            log.info("自己发的消息: " + msgContent);
            replyByBot(msgContent, msg.get("FromUserName").toString());
        } else if (msgType == 4) {
            // 好友私聊的信息
            if (StringUtils.startsWith(msgContent, "/")) {
                String name = getNameByUidFromContact(msg.get("FromUserName").toString(), true);
                if (StringUtils.isNotEmpty(name)) {
                    log.info("好友 " + name + ": " + msgContent);
                    replyByBot(msgContent, msg.get("FromUserName").toString());
                }
            }
        }
    }

    public static void main(String[] args) {
        CustomBot bot = new CustomBot();
        bot.setDebugFile(false);
        bot.setServerRun(false);
        try {
            bot.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
