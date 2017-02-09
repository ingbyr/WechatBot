package wechatAPI.bots;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wechatAPI.NetUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created on 17-2-9.
 *
 * @author ing
 * @version 1
 */
public class WeatherBot {
    private static final Logger log = LoggerFactory.getLogger(NetUtils.class);
    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_CONTENT = "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5";
    private static ObjectMapper mapper = new ObjectMapper();


    private String baseUrl = "http://www.weather.com.cn/data/cityinfo/";
    private String url = "";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build();

    public WeatherBot() {
        url = baseUrl + "101160701.html"; //张掖
    }

    public String getWeather() {
        Request request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .build();
        try (ResponseBody responseBody = client.newCall(request).execute().body()) {
//            {"weatherinfo":{"city":"张掖","cityid":"101160701","temp":"15","WD":"北风","WS":"3级","SD":"8%","WSE":"3","time":"17:00","isRadar":"1","Radar":"JC_RADAR_AZ9936_JB","njd":"28400","qy":"850","rain":"0"}}
            Map dataMap = mapper.readValue(responseBody.bytes(), Map.class);

            log.debug("weatherinfo: " + dataMap.get("weatherinfo").toString());

            Map<String, Object> weather = (Map<String, Object>) dataMap.get("weatherinfo");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("- 城市: " + weather.get("city"));
            stringBuilder.append("\n- 温度: " + weather.get("temp1"));
            stringBuilder.append("\n- 天气: " + weather.get("weather"));
            return stringBuilder.toString();
        } catch (IOException e) {
            log.error("weather bot 请求数据失败");
            log.error(e.toString());
        }
        return null;
    }

    public static void main(String[] args) {
        WeatherBot bot = new WeatherBot();
        log.debug(bot.getWeather());
    }
}
