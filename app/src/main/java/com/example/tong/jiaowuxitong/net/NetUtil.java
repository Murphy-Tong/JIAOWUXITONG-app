package com.example.tong.jiaowuxitong.net;

import android.text.TextUtils;
import android.util.Log;

import com.example.tong.jiaowuxitong.entity.ActionState;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by TONG on 2017/1/5.
 */
public class NetUtil {


    /**
     * @param msg
     * @return ActionState 1 succ 0 fail
     */
    public static int handleMessage(Message msg) {
        ActionState actionState = GsonUtil.fromJson((String) msg.msg, ActionState.class);
        if (actionState == null) return -1;
        return actionState.tag;
    }

    private static final String JSONBODYTYPE = "application/json;charset=GBK";

    public static String login(Object user) {
        checkHost();

        if (user == null) {
            return null;
        }

        Gson gson = new Gson();
        String s = gson.toJson(user);

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse(JSONBODYTYPE);
        RequestBody requestBody = RequestBody.create(mediaType, s);

        try {
            Request request = new Request.Builder().url("http://localhost:10010/myapp/login")
                    .post
                            (requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String ss = new String(response.body().bytes(), "GBK");
                return ss;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static void checkHost() {


    }

    public static void asyncPost(String url, final int tag) {
        asyncPost(null, url, tag, -1);
    }

    public static Call asyncPost(String jbody, String url, final int tag) {
        return asyncPost(jbody, url, tag, -1);
    }

    public interface CallBack {
        void onPost();
    }

    public static Call asyncPost(String jbody, String url, final int tag, final int extra) {
        Log.i("url", url);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);
        OkHttpClient client = builder.build();

        Request request;
        Call call = null;

        try {
            if (!TextUtils.isEmpty(jbody)) {

                MediaType mediaType = MediaType.parse(JSONBODYTYPE);
                RequestBody requestBody = RequestBody.create(mediaType, jbody);

                request = new Request.Builder().url(url).post(requestBody).build();
            } else {
                request = new Request.Builder().url(url).build();
            }
            call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String ss = e.toString();
                    TestUtil.print(ss);
                    Message message = new Message();
                    message.msg = ss;
                    message.tag = Message.FAILED_TAG;
                    EventBus.getDefault().post(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String ss = new String(response.body().bytes(), "GBK");
                        TestUtil.print(ss);

                        Message message = new Message();
                        message.msg = ss;
                        message.tag = tag;
                        message.extra = extra;
                        EventBus.getDefault().post(message);
                    }
                }
            });
        } catch (Exception e) {

        }

        return call;
    }
}
