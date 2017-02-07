package wechatAPI;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created on 17-2-6.
 *
 * @author ing
 * @version 1
 */
public class IngBot {
    private static Logger log = LoggerFactory.getLogger(IngBot.class);

    private final OkHttpClient client = new OkHttpClient();

    // 登陆参数
    private String loginUrl;
    private long standTime;
    private String uuid;
    private String qrcodeUrl;

    public IngBot() {
        //　避免SSL报错
        System.setProperty("jsse.enableSNIExtension", "false");

        this.loginUrl = "https://login.weixin.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_=";
        Date date = new Date();
        this.standTime = date.getTime();
        this.uuid = "";

        this.qrcodeUrl = "https://login.weixin.qq.com/qrcode/";
    }


    public void getUuid() throws IOException {
        loginUrl = loginUrl + standTime;
        log.info(loginUrl);
        Response response = NetUtils.request(loginUrl);
        if (response.isSuccessful()) {
            // e.g: window.QRLogin.code = 200; window.QRLogin.uuid = "wejZcbBd2w==";
            String res = response.body().string();
            String code = StringUtils.substringBetween(res, "window.QRLogin.code = ", ";");
            uuid = StringUtils.substringBetween(res, "window.QRLogin.uuid = \"", "\";");

            log.info("window.QRLogin.code = " + code);
            log.info("window.QRLogin.uuid = " + uuid);
        }
    }

    public void generateQrcode() throws IOException {
        qrcodeUrl += uuid;
        log.debug("qrcode url: " + qrcodeUrl);
        Response response = NetUtils.request(qrcodeUrl);
        byte[] qrcodeDate = response.body().bytes();
        String imageUrl = System.getProperty("user.dir") + "/res/temp.png";
        FileOutputStream fos = new FileOutputStream(imageUrl);
        fos.write(qrcodeDate);
        fos.close();
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(imageUrl));
    }

    public void waitForLogin() throws IOException, InterruptedException {
        String url = StringUtils.join("https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?uuid=", uuid, "&tip=1&_=", new Date().getTime());
        log.debug("wait for login url: " + url);
        while (true) {
            Response response = NetUtils.request(url);
            String res = response.body().string();
            log.debug("response: " + res);
            if (StringUtils.equals(res, "window.code=201;"))
                log.info("请在手机上确认登陆");
            if (StringUtils.startsWith(res, "window.redirect_uri")) {
                log.info("登陆成功");
                break;
            }
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) {
        IngBot bot = new IngBot();
        try {
            bot.getUuid();
            bot.generateQrcode();
            bot.waitForLogin();
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
}
