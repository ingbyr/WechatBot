package com.ingbyr.wechatbot.bots;

import okhttp3.Request;
import okhttp3.ResponseBody;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created on 17-2-12.
 *
 * @author ing
 * @version 1
 */
public class AlianAvatarBot extends BaseBot {
    @Override
    public void initUrl(String arg) {
        baseUrl = "https://robohash.org/";
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
            byte[] image = responseBody.bytes();
            String imageUrl = System.getProperty("user.home") + "/WechatBotRun/alian.png";
            try (FileOutputStream fos = new FileOutputStream(imageUrl)) {
                fos.write(image);
            }
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File(imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "avatar test";
    }

    public static void main(String[] args) {
        AlianAvatarBot bot = new AlianAvatarBot();
        System.out.println(bot.start("ing"));
    }
}
