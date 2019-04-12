package com.steven.download.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Description:
 * Data：4/10/2019-11:52 AM
 *
 * @author yanzhiwen
 */
public class ToastUtil {
    private static Toast sToast;

    public static void toast(Context context, String message) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
        }
        sToast.show();

    }
}
