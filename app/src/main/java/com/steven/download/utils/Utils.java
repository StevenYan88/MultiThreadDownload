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

    private static final String[] url = {"https://imtt.dd.qq.com/16891/apk/1962EE9CFA69AC65231F488F9B978B69.apk?fsname=com.tencent.mobileqq_8.8.0_1792.apk",
            "https://imtt.dd.qq.com/16891/apk/5C0FF221A948463BCF9F3255E0112034.apk?fsname=com.tencent.mm_8.0.6_1900.apk&csr=1bbd",
            "https://imtt.dd.qq.com/16891/apk/8B91D1C0FFFAE9EB75427B2A02FF82D2.apk?fsname=com.eg.android.AlipayGphone_10.2.23.7100_400.apk&csr=1bbd"};


    private static final String[] appIcons = {"http://e.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=a96dced3a151f3dec3e7b163a6c2c72d/30adcbef76094b3698d41df4adcc7cd98d109d60.jpg",
            "http://d.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=6c5fcff6c5fcc3ceb495c134a069e1ba/810a19d8bc3eb13518b5f46fab1ea8d3fc1f44f7.jpg",
            "http://d.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=279aea97adcc7cd9fa783cde0b2d160d/d439b6003af33a876265f160c85c10385343b544.jpg"};


    private static final String[] versions = {"8.8.0", "8.0.6", "10.2.23.7100"};
    private static final String[] sizes = {"113.79MB", "190.69MB", "95.54MB"};
    private static final String[] downLoadCounts = {"27.7亿", "34.3亿", "7.1亿"};

    private static final String[] names = {"QQ", "微信", "支付宝"};


    public static List<AppEntity> generateApp() {
        List<AppEntity> apps = new ArrayList<>();
        for (int i = 0; i < url.length; i++) {
            AppEntity appEntity = new AppEntity(url[i], appIcons[i], names[i], versions[i], sizes[i], downLoadCounts[i]);
            apps.add(appEntity);
        }
        return apps;
    }

}
