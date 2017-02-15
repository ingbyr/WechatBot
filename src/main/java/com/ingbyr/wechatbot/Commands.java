package com.ingbyr.wechatbot;

import com.ingbyr.wechatbot.annotation.BotCommand;
import com.ingbyr.wechatbot.annotation.BotHelper;
import com.ingbyr.wechatbot.bots.AlienAvatarBot;
import com.ingbyr.wechatbot.bots.WeatherBot;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 17-2-11.
 *
 * @author ing
 * @version 1
 */
public class Commands {
    private static final Logger log = LoggerFactory.getLogger(Commands.class);
    private static final String logo = "[BOT REPLY]\n";

    @BotCommand("/天气")
    @BotHelper("城市实时天气: /天气 [城市名称]")
    public static String currentWeather(String city) {
        WeatherBot weatherBot = new WeatherBot();
        String data = weatherBot.start(city);

        if (StringUtils.isBlank(data)) {
            data = logo + "暂无此城市天气信息";
        } else {
            data = logo + data;
        }
        return data;
    }

    @BotCommand("/头像")
    @BotHelper("随机获取头像: /头像 [任意文字]")
    public static String alianAvatar(String name) {
        AlienAvatarBot bot = new AlienAvatarBot();
        return bot.start(name);
    }

    @BotCommand("/帮助")
    @BotHelper("BOT的使用帮助: /帮助")
    public static String botHelper() {
        return WechatBot.help;
    }

    /**
     * 测试
     * 注意编译时要注释，否则反射会报错
     * @param args
     */
//    public static void main(String[] args) {
//        currentTime();
//        currentWeather("北京");
//    }
}
