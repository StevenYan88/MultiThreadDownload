package com.steven.download.okhttp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:
 * Data：11/29/2017-3:36 PM
 *
 * @author: yanzhiwen
 */
public class OkHttpManager {
    private static final OkHttpManager sOkHttpManager = new OkHttpManager();
    private OkHttpClient okHttpClient;

    private OkHttpManager() {
        okHttpClient = new OkHttpClient();
    }

    public static OkHttpManager getInstance() {
        return sOkHttpManager;
    }

    public Call asyncCall(String url) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        return okHttpClient.newCall(request);
    }

    public Response syncResponse(String url, long start, long end) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                //Range 请求头格式Range: bytes=start-end
                .addHeader("Range", "bytes=" + start + "-" + end)
                .build();
        return okHttpClient.newCall(request).execute();
    }
}
