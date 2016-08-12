package com.lsl.huoqiu.net.http;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Forrest on 16/7/14.
 */
public class HttpManagerImpl implements IHttpManager {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();//创建一个默认的HttpClient
    public final static int CONNECTION_TIMEOUT = 15;//连接超时时间
    public final static int SO_TIMEOUT = 15;//连接超时时间
    public final static int WRITE_TIMEOUT = 30;//连接超时时间
    private static final String CHARSET_NAME = "UTF-8";

    static {
        mOkHttpClient.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(SO_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

    }

    @Override
    public Response executeHttpGet(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
        //在一个工作线程中下载文件，当响应可读时回调Callback接口。读取响应时会阻塞当前线程。OkHttp现阶段不提供异步api来接收响应体
        //execute()是同步请求；enqueue()是异步请求
        //在请求的时候要单独开启一个异步线程，将改请求放到异步线程里进行
        Response response = mOkHttpClient.newCall(addGetRequest(url, parameters, headers)).execute();
        if (response.isSuccessful()) {

        } else {
            throw new IOException("Unexpected  Code:" + response);
        }
        return response;

    }

    /**
     * 创建一个请求
     *
     * @param url
     * @param parameters
     * @param headers
     * @return
     */
    private Request addGetRequest(String url, Map<String, String> parameters, Map<String, String> headers) {
        //get方法将参数拼接成新的一个url
        StringBuilder sb = new StringBuilder();
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                sb.append("&" + key + "=" + parameters.get(key));
            }
        }
        if (sb.length() != 0) {
            sb.deleteCharAt(0);
            if (url.contains("?")) {
                url = url + "&" + sb.toString();
            } else {
                url = url + "?" + sb.toString();
            }
        }
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        //手机添加Header
//        requestBuilder.addHeader("version","1.0");

        if (headers != null) {
            Iterator<String> iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        return requestBuilder.build();
    }


    @Override
    public Response executeHttpPost(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {

        return null;
    }

    @Override
    public Response executeHttpPut(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
        return null;
    }

    @Override
    public OkHttpClient getHttpClient(boolean isHttps) {

        return mOkHttpClient;
    }

    @Override
    public InputStream getInputStream(String url) throws IOException {
        return null;
    }

    @Override
    public String getHttpText(String url) throws IOException {
        return null;
    }

    @Override
    public HttpURLConnection getUrlConnection(String url) throws IOException {
        return null;
    }

    private Request addPostRequest(String url, Map<String, String> parameters, Map<String, String> headers) {
//        RequestBody requestBody=new
        if (parameters != null) {

        }
        return  null;
    }
//    private HttpPost getHttpPost(String url,
//                                 Map<String, String> parameters,
//                                 Map<String, String> headers) throws IOException {
//        HttpPost httpPost = new HttpPost(url);
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        if (parameters != null) {
//            for (String key : parameters.keySet()) {
//                params.add(new BasicNameValuePair(key, parameters.get(key)));
//            }
//        }
//        String curtime = Long.toString(System.currentTimeMillis());
//        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
//        // 把实体数据设置到请求对象
//        httpPost.setEntity(entity);
//        SharedPreferences sp = AppContext.getInstance().getSharedPreferences("UserConfig", Context.MODE_PRIVATE);
//        String session = sp.getString("session", null);
//        String token = sp.getString("token", null);
//        httpPost.addHeader("version", Config.version+"");
//        httpPost.addHeader("User-Agent", Config.userAgent);
//        httpPost.addHeader("platform","android");
//        httpPost.addHeader("mobile-time", System.currentTimeMillis()+"");
//        httpPost.addHeader("channel",Config.CHANNEL);
//        httpPost.addHeader("device_token",Config.IMEI);
//        httpPost.addHeader("md5str", getPHONE_MD5(curtime));
//        httpPost.addHeader("csrf_time", curtime);
//        httpPost.addHeader("hq_phone", "true");
//        if (!TextUtils.isEmpty(session)) {
//            httpPost.addHeader("Cookie", session);
//        }
//        if (!TextUtils.isEmpty(token)) {
//            httpPost.addHeader("sso_token", token);
//        }else if(!TextUtils.isEmpty(AppContext.API_TOKEN)){
//            httpPost.addHeader("sso_token", AppContext.API_TOKEN);
//        }
//        if (headers != null) {
//            Iterator<String> iterator = headers.keySet().iterator();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                httpPost.addHeader(key, headers.get(key));
//            }
//        }
//        return httpPost;
//    }

}
