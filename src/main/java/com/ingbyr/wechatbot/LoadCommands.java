package com.ingbyr.wechatbot;

import com.ingbyr.wechatbot.annotation.BotCommand;
import com.ingbyr.wechatbot.annotation.BotHelper;
import com.ingbyr.wechatbot.utils.DisplayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 17-2-11.
 *
 * @author ing
 * @version 1
 */
public class LoadCommands {
    private static Map<String, Method> commands = new HashMap<>();
    private static Logger log = LoggerFactory.getLogger(WechatBot.class);
    private static String helper = null;

    public static void init() {
        Class c = Commands.class;
        Method[] methods = c.getDeclaredMethods();
        StringBuilder helperBuilder = new StringBuilder();
        helperBuilder.append(DisplayUtils.BOT_HELP);
        for (Method method : methods) {
            BotCommand bcmd = method.getAnnotation(BotCommand.class);
            log.info("启动 " + method.getName());
            commands.put(bcmd.value(), method);

            // 构建帮助信息
            helperBuilder.append("\n- ");
            helperBuilder.append(method.getAnnotation(BotHelper.class).value());
        }
        helper = helperBuilder.toString();
    }

    public static String getHelper() {
        return helper;
    }

    public static Map<String, Method> getCommands() {
        return commands;
    }

    /**
     * 测试
     */
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        init();
//        String[] c = {"广州", "北京", "上海"};
//        for (String s : c) {
//            new Thread(() -> {
//                try {
//                    log.debug(Thread.currentThread().getName());
//                    System.out.print(commands.get("/天气").invoke(null, s));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//
//            }).start();
//        }

//        System.out.print(commands.get("/天气").invoke(null, "北京"));
//        System.out.print(getHelper());
    }
}
