package wechatAPI;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 17-2-6.
 *
 * @author ing
 * @version 1
 */
public class TempTest {
    private static final Logger log = LoggerFactory.getLogger(TempTest.class);

    public static void main(String[] args) {
        // e.g: window.QRLogin.code = 200; window.QRLogin.uuid = "wejZcbBd2w==";
        String str = "window.QRLogin.code = 200; window.QRLogin.uuid = \"wejZcbBd2w==\";";
        String code = StringUtils.substringBetween(str, "window.QRLogin.code = ", ";");
        String uuid = StringUtils.substringBetween(str, "window.QRLogin.uuid = \"", "\";");
        log.info(code);
        log.info(uuid);
    }
}
