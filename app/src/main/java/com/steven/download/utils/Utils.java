package com.steven.download.utils;

import android.text.TextUtils;

import com.steven.download.entity.AppEntity;

import java.io.Closeable;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Data：11/30/2017-10:40 AM
 *
 * @author: yanzhiwen
 */
public class Utils {
    public static String md5Url(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        return md5(url);

    }

    private static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            //LogDebugger.exception(e.getMessage());
        }
        return s;
    }

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float keepTwoBit(float value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return Float.parseFloat(df.format(value));
    }

    private static final String[] url = {"http://gdown.baidu.com/data/wisegame/27317dce71dedfbd/QQ_1010.apk",
            "http://gdown.baidu.com/data/wisegame/43b4382f3c757ebe/weixin_1400.apk",
            "http://gdown.baidu.com/data/wisegame/83cef7aa20d60c3f/zhifubao_140.apk",
            "http://gdown.baidu.com/data/wisegame/a4c4efefb730727b/jinritoutiao_720.apk",
            "http://gdown.baidu.com/data/wisegame/0ed27a50d8e7f531/douyinduanshipin_580.apk",
            "http://gdown.baidu.com/data/wisegame/ff4efd277de65cb8/weibo_3854.apk"};


    private static final String[] appIcons = {"http://e.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=a96dced3a151f3dec3e7b163a6c2c72d/30adcbef76094b3698d41df4adcc7cd98d109d60.jpg",
            "http://d.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=6c5fcff6c5fcc3ceb495c134a069e1ba/810a19d8bc3eb13518b5f46fab1ea8d3fc1f44f7.jpg",
            "http://d.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=279aea97adcc7cd9fa783cde0b2d160d/d439b6003af33a876265f160c85c10385343b544.jpg",
            "http://e.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=04e122514f34970a47261828a7e6e6fa/3b292df5e0fe99251003832e3aa85edf8db17145.jpg",
            "http://d.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=545196328b35e5dd9079add844ea90dd/0dd7912397dda144b6a71ad2bcb7d0a20cf48627.jpg",
            "http://g.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=2f4924e7c311728b30788425fad0f4fc/9d82d158ccbf6c817e43879eb23eb13533fa4054.jpg"};


    private static final String[] versions = {"7.9.9", "7.0.3", "10.1.60.8966", " 7.2.0", "5.8.0", "9.4.0"};
    private static final String[] sizes = {"57.6MB", "104.98MB", "64.4MB", "29.9MB", "72.5MB", "77.2MB"};
    private static final String[] downLoadCounts = {"27.7亿", "34.3亿", "7.1亿", " 6.3亿", "3142万", "7亿"};

    private static final String[] names = {"QQ", "微信", "支付宝", "今日头条", "抖音", "新浪微博"};


    public static List<AppEntity> generateApp() {
        List<AppEntity> apps = new ArrayList<>();
        for (int i = 0; i < url.length; i++) {
            AppEntity appEntity = new AppEntity(url[i], appIcons[i], names[i], versions[i], sizes[i], downLoadCounts[i]);
            apps.add(appEntity);
        }
        return apps;
    }

}
