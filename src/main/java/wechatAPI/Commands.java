package wechatAPI;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wechatAPI.annotation.BotCommand;
import wechatAPI.bots.WeatherBot;

import java.time.LocalDate;

/**
 * Created on 17-2-11.
 *
 * @author ing
 * @version 1
 */
public class Commands {
    private static final Logger log = LoggerFactory.getLogger(Commands.class);

    @BotCommand("/天气")
    public static String currentWeather(String city) {
        WeatherBot weatherBot = new WeatherBot();
        String data = weatherBot.getWeather(city);
        log.info("回复信息:\n " + data);

        if (StringUtils.isBlank(data)) {
            data = "暂无此城市天气信息";
        }
        return data;
    }

    @BotCommand("/时间")
    public static String currentTime() {
        LocalDate today = LocalDate.now();
        return today.toString();
    }
}
