package com.ingbyr.wechatbot;

import com.ingbyr.wechatbot.annotation.BotCommand;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ingbyr.wechatbot.annotation.BotCommand;
import com.ingbyr.wechatbot.bots.WeatherBot;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created on 17-2-11.
 *
 * @author ing
 * @version 1
 */
public class Commands {
    private static final Logger log = LoggerFactory.getLogger(Commands.class);
    private static final String logo = "[ING BOT]\n";

    @BotCommand("/天气")
    public static String currentWeather(String city) {
        WeatherBot weatherBot = new WeatherBot();
        String data = weatherBot.start(city);

        if (StringUtils.isBlank(data)) {
            data = logo + "暂无此城市天气信息";
        } else {
            data = logo + data;
        }
        log.info("回复信息: " + data);
        return data;
    }

    @BotCommand("/时间")
    public static String currentTime() {
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now().withNano(0);
        String data = logo + today.toString() + " " + time.toString();
        log.info("回复信息: " + data);
        return data;
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
