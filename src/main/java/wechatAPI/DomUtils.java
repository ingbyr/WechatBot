package wechatAPI;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 17-2-7.
 *
 * @author ing
 * @version 1
 */
public class DomUtils {
    private static final Logger log = LoggerFactory.getLogger(DomUtils.class);

    public static Map parseInitData(String data) {
        try {
            Map ussData = new HashMap();
            Document document = DocumentHelper.parseText(data);
            Element root = document.getRootElement();
            Iterator iter = root.elementIterator();
            while (iter.hasNext()) {
                Element ele = (Element) iter.next();
                log.debug("name:" + ele.getName() + " value:" + ele.getStringValue());
                ussData.put(ele.getName(), ele.getStringValue());
            }

            //　随机device id
            String deviceID = "e";
            for (int i = 0; i < 3; i++) {
                int randomNum = ThreadLocalRandom.current().nextInt(10000, 99999);
                deviceID += randomNum;
            }
            ussData.put("deviceID", deviceID);
            return ussData;
        } catch (DocumentException e) {
            log.error(e.toString());
        }
        return null;
    }

    public static void main(String[] args) {
        parseInitData("ing");
    }
}
