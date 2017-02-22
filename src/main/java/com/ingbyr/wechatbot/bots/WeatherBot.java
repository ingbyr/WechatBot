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

    public WeatherBot(Request request) {
        this.request = request;
    }

    @Override
    public String requestData() {
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

    public static class Builder extends BaseBuilder {

        public Builder() {
            baseUrl = "http://wthrcdn.etouch.cn/WeatherApi?city=";
        }

        @Override
        public BaseBuilder setArgs(String args) {
            this.args = args;
            url = baseUrl + this.args;
            return this;
        }

        @Override
        public BaseBuilder initRequest() {
            request = new Request.Builder()
                    .addHeader(USER_AGENT, USER_AGENT_CONTENT)
                    .url(url)
                    .build();
            return this;
        }

        @Override
        public BaseBot build() {
            return new WeatherBot(request);
        }
    }
}
