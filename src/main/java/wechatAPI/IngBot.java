package wechatAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 17-2-6.
 *
 * @author ing
 * @version 1
 */
public class IngBot {
    // todo 终端日志颜色区分
    private static Logger log = LoggerFactory.getLogger(IngBot.class);
    private static ObjectMapper mapper = new ObjectMapper();

    // 状态码
    private static final String SUCCESS = "200";
    private static final String SCANED = "201";
    private static final String TIMEOUT = "408";

    // 登陆参数
    private String loginUrl;
    private String uuid;
    private String qrcodeUrl;
    private String redirectUrl;
    private String baseUrl;
    private String baseRequestContent;

    //
    private Map<String, String> initData;
    private Map<String, String> baseRequest;

    public IngBot() {
        //　避免SSL报错
        System.setProperty("jsse.enableSNIExtension", "false");

        this.loginUrl = "https://login.weixin.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_=";
        this.uuid = "";

        this.qrcodeUrl = "https://login.weixin.qq.com/qrcode/";
        this.baseUrl = "https://wx.qq.com/cgi-bin/mmwebwx-bin";
        this.initData = new HashMap<>();
        this.baseRequest = new HashMap<>();
        this.baseRequestContent = "";
    }


    public void getUuid() throws IOException {
        loginUrl = loginUrl + new Date().getTime();
        log.info(loginUrl);
        Response response = NetUtils.request(loginUrl);
        if (response.isSuccessful()) {
            // e.g: window.QRLogin.code = 200; window.QRLogin.uuid = "wejZcbBd2w==";
            String res = response.body().string();
            String code = StringUtils.substringBetween(res, "window.QRLogin.code = ", ";");
            uuid = StringUtils.substringBetween(res, "window.QRLogin.uuid = \"", "\";");

            log.debug("window.QRLogin.code = " + code);
            log.debug("window.QRLogin.uuid = " + uuid);
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
        log.info("请扫描二维码登陆微信");
        String url = StringUtils.join("https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?uuid=", uuid, "&tip=1&_=", new Date().getTime());
        log.debug("login url: " + url);
        while (true) {
            Response response = NetUtils.request(url);
            String res = response.body().string();
            log.debug("response: " + res);

            // 登陆过程中
            if (StringUtils.startsWith(res, "window.code=")) {
                String code = StringUtils.substringBetween(res, "window.code=", ";");
                log.debug("code: " + code);
                if (StringUtils.equals(code, SUCCESS)) {
                    log.info("登陆成功");
                    response = NetUtils.request(url);
                    res = response.body().string();
                    redirectUrl = StringUtils.substringBetween(res, "window.redirect_uri=\"", "\";");
                    if (StringUtils.isNotEmpty(redirectUrl)) {
                        log.debug("redirectUrl: " + redirectUrl);
                        break;
                    }
                } else if (StringUtils.equals(code, SCANED))
                    log.info("请点击确认按钮");
                else if (StringUtils.equals(code, TIMEOUT)) {
                    log.info("登陆超时");
                    System.exit(-1);
                } else {
                    log.info("未知错误");
                    log.error(res);
                    System.exit(-1);
                }
            }

            //　轮询时间　1s
            Thread.sleep(1000);
        }
    }

    public void login() throws IOException {
        if (redirectUrl.length() < 4) {
            log.info("登陆失败");
            System.exit(-1);
        }
        //　获取uin和sid
        redirectUrl += "&fun=new";

        Response response = NetUtils.request(redirectUrl);
        String data = response.body().string();
        log.debug("data: " + data);
        initData = DomUtils.parseInitData(data);

        // 组装请求body
        baseRequest.put("Uin", initData.get("wxuin"));
        baseRequest.put("Sid", initData.get("wxsid"));
        baseRequest.put("Skey", initData.get("skey"));
        baseRequest.put("DeviceID", initData.get("deviceID"));

        Map<String, Map> tempData = new HashMap<>();
        tempData.put("BaseRequest", baseRequest);
        log.debug(tempData.toString());

        // 转换为json格式
        baseRequestContent = mapper.writeValueAsString(tempData);
    }

    public void init() throws IOException {
        String url = baseUrl + "/webwxinit?r=" + (new Date().getTime()) + "&lang=en_US&pass_ticket=" + initData.get("pass_ticket");
        log.debug("url: " + url);
        log.debug("baseRequestContent " + baseRequestContent);

        Response response = NetUtils.requestWithJson(url, baseRequestContent);

        // 保存json数据到文件temp.json
        byte[] data = response.body().bytes();
        try(FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/res/temp.json")) {
            fos.write(data);
        }

    }


    public void statusNotify() {
        String url = baseUrl + "/webwxstatusnotify?lang=zh_CN&pass_ticket=" + initData.get("pass_ticket");
        Map<String, Object> tempData = new HashMap<>();
        tempData.put("BaseRequest", baseRequest);
        tempData.put("Code", 3);
        tempData.put("FromUserName", null);
    }

    /**
     * 流程测试
     *
     * @param args
     */
    public static void main(String[] args) {
        IngBot bot = new IngBot();
        try {
            bot.getUuid();
            bot.generateQrcode();
            bot.waitForLogin();
            bot.login();
            bot.init();
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
}
