package com.toponpaydcb.sdk.tool;

import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetUtil{
    public static String TAG = "Yole_NetUtil";

    NetUtil()
    {
        Log.d(TAG, TAG);
    }
    public  static String sendPost(String url,JSONObject formBody) {

        //创建OkHttp客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        // 封装请求体
        MediaType mediaType = MediaType.parse("application/json");

        //创建请求对象
        RequestBody requestBody = RequestBody.create(mediaType, formBody.toString());
        Log.d(TAG, "FormBody:"+formBody.toString());

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();

        JSONObject jsonObject = new JSONObject();
        try {
            // 执行这个请求对象
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            Log.d(TAG, "onResponse2:"+result);
            return result;
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
            try {
                jsonObject.put("status","SocketTimeoutException");
                jsonObject.put("errorCode","-1");
                jsonObject.put("message",e.toString());

                okHttpClient.connectionPool().evictAll();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            return jsonObject.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                jsonObject.put("status","IOException");
                jsonObject.put("errorCode","-1");
                jsonObject.put("message",e.toString());

                okHttpClient.connectionPool().evictAll();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            return jsonObject.toString();
        }
    }
    public  static String sendGet(String url,String formBody) {

        return "";
    }

    public static String serializeMetadata(HashMap<String, String> metadata) throws IOException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(metadata);
            return baos.toString();
        }
    }
    public static String toString(Set<Map.Entry<String,String>> entrySet) {
        Iterator<Map.Entry<String,String>> i = entrySet.iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Map.Entry<String,String> e = i.next();
            String key = e.getKey();
            String value = e.getValue();
            sb.append(""+key+"");
            sb.append('=');
            sb.append(""+value+"");
            if (! i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }
    /**
     * requestBody 是请求参数 json 字符串
     * merchantSecret  cp 的密钥
     * 请求方式是 postbody, 请求内容 {"content": "content", "sign": "sign"}
     */
    public static EncodeBaseDataV2 RestApiRequest(String requestBody, String merchantSecret) {
        byte[] datautf8 = requestBody.getBytes(StandardCharsets.UTF_8);
        String base64str = new String(Base64.encodeBase64(datautf8));
        Log.e(TAG,"RestApiRequest base64str= "+base64str);
        String signStr = base64str + merchantSecret;
//        String md5sign = DigestUtils.md5Hex(signStr);
        Log.e(TAG,"RestApiRequest signStr= "+signStr);
        String md5sign = new String(Hex.encodeHex(DigestUtils.md5(signStr)));
        Log.e(TAG,"RestApiRequest md5sign= "+md5sign);
        return new EncodeBaseDataV2(base64str,md5sign);
    }

    public static String decodeBase64(String  content_conte)
    {
        Log.e(TAG,"decodeBase64 = "+content_conte);
        byte[] base64str = Base64.decodeBase64(content_conte.getBytes());
        Log.e(TAG,"decodeBase64 base64str= "+new String(base64str));
        return new String(base64str);
    }

}
