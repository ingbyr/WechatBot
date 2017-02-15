package com.ingbyr.wechatbot.bots;

import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created on 17-2-9.
 *
 * @author ing
 * @version 1
 */
public class WeatherBot extends BaseBot {

    public WeatherBot() {
    }

    @Override
    public void initUrl(String arg) {
        baseUrl = "http://wthrcdn.etouch.cn/WeatherApi?city=";
        url = baseUrl + arg;
    }

    @Override
    public void initRequset() {
        request = new Request.Builder()
                .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                .url(url)
                .build();
    }

    @Override
    public String doRequest() {
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
                    sb.append("温度: " + ele.getStringValue() + "℃\n");
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
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        WeatherBot bot = new WeatherBot();
//        System.out.println(bot.start("北京"));
//    }
}
