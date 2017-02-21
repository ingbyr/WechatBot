package com.ingbyr.wechatbot;

import com.ingbyr.wechatbot.utils.DisplayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

/**
 * Created on 17-2-14.
 *
 * @author ing
 * @version 1
 */
public class CustomBot extends WechatBot {
    private static Logger log = LoggerFactory.getLogger(CustomBot.class);

    public void replyByBot(String msgContent, String toUser) {
        String[] cmd = StringUtils.split(msgContent, " ");
        log.debug("cmd: " + Arrays.toString(cmd));
        String replyStr;
        // 命令参数限制为 <=2
        if (cmd.length > 0) {
            try {
                Method bot = commands.get(cmd[0]);
                if (bot != null) {
                    int correctParaCount = bot.getParameterCount();
                    if ((cmd.length == (correctParaCount + 1)) && (cmd.length == 1)) {
                        replyStr = bot.invoke(null).toString();
                    } else if ((cmd.length == (correctParaCount + 1)) && (cmd.length == 2)) {
                        replyStr = bot.invoke(null, cmd[1]).toString();
                    } else {
                        replyStr = DisplayUtils.BOT_ERROR + "参数个数错误";
                    }
                } else {
                    replyStr = DisplayUtils.BOT_ERROR + "命令错误";
                }
            } catch (IllegalAccessException e) {
                replyStr = DisplayUtils.BOT_ERROR + "命令错误";
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                replyStr = DisplayUtils.BOT_ERROR + "命令错误";
                e.printStackTrace();
            }

            //回复消息
            log.info("BOT回复消息: " + replyStr);
            if (StringUtils.startsWith(replyStr, "/")) {
                // 发送文件
                sendImgMsgByUid(Paths.get(replyStr), toUser);
            } else {
                // 发送文字消息
                sendMsgByUid(replyStr, toUser);
            }
        }
    }

    @Override
    public void handleMsgAll(int msgType, Map<String, Object> msg) {
        String msgContent = msg.get("Content").toString();
        log.trace("msgType: " + msgType);
        log.trace("msgContent: " + msgContent);

        if (msgType == 1 && StringUtils.startsWith(msgContent, "/")) {
            // 自己发送给自己的消息
            log.info("自己发来的消息: " + msgContent);
            replyByBot(msgContent, msg.get("FromUserName").toString());
        } else if (msgType == 4) {
            // 好友私聊的信息
            if (StringUtils.startsWith(msgContent, "/")) {
                String name = getNameByUidFromContact(msg.get("FromUserName").toString(), true);
                if (StringUtils.isNotEmpty(name)) {
                    log.info("好友 " + name + "发来的消息: " + msgContent);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
