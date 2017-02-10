package wechatAPI.bots;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wechatAPI.NetUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.*;

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

    private String baseUrl = "http://wthrcdn.etouch.cn/WeatherApi?city=";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build();

    public WeatherBot() {
    }

    public String getWeather(String city) {
        String url = baseUrl + city;
        Request request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .build();
        try (ResponseBody responseBody = client.newCall(request).execute().body()) {
            Document document = DocumentHelper.parseText(responseBody.string());
            Element root = document.getRootElement();
            Iterator iterator = root.elementIterator();
            StringBuilder sb = new StringBuilder();
            while (iterator.hasNext()) {
                Element ele = (Element) iterator.next();
                if (StringUtils.equals(ele.getName(), "city")) {
                    sb.append("城市: " + ele.getStringValue() + "\n");
                } else if (StringUtils.equals(ele.getName(), "updatetime")) {
                    sb.append("数据更新时间: " + ele.getStringValue() + "\n");
                } else if (StringUtils.equals(ele.getName(), "wendu")) {
                    sb.append("实时温度: " + ele.getStringValue() + "℃\n");
                } else if (StringUtils.equals(ele.getName(), "fengli")) {
                    sb.append("风力: " + ele.getStringValue() + "\n");
                } else if (StringUtils.equals(ele.getName(), "shidu")) {
                    sb.append("湿度: " + ele.getStringValue() + "\n");
                } else if (StringUtils.equals(ele.getName(), "fengxiang")) {
                    sb.append("风向: " + ele.getStringValue());
                } else {

                }

            }
            return sb.toString();
        } catch (IOException e) {
            log.error("weather bot 请求数据失败");
            log.error(e.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

}
