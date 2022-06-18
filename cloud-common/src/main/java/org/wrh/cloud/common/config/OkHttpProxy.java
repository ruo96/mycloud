package org.wrh.cloud.common.config;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author sunbo
 * 基于okhttp 的 http 框架
 */
// @Component
public class OkHttpProxy implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpProxy.class);

    @Autowired
    private OkhttpConfig okhttpConfig;

    private OkHttpClient okHttpClient;

    private int maxIdleConnections;
    private long keepAliveDuration;
    private long connectTimeout;
    private long readTimeout;

    private static final MediaType JSON = MediaType.parse("application/json;charset=UTF-8");

    @PostConstruct
    public void init() {
        connectTimeout = okhttpConfig.getConnectTimeout() == null ? 0 : Integer.parseInt(okhttpConfig.getConnectTimeout());
        readTimeout = okhttpConfig.getReadTimeout() == null ? 0 : Integer.parseInt(okhttpConfig.getReadTimeout());
        maxIdleConnections = okhttpConfig.getMaxIdleConnections() == null ? 0 : Integer.parseInt(okhttpConfig.getMaxIdleConnections());
        keepAliveDuration = okhttpConfig.getKeepAliveDuration() == null ? 0 : Integer.parseInt(okhttpConfig.getKeepAliveDuration());
    }

    @Override
    public void afterPropertiesSet() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (0 != connectTimeout) {
            builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        }
        if (0 != readTimeout) {
            builder.readTimeout(readTimeout, TimeUnit.SECONDS);
        }
        if (0 != maxIdleConnections && 0 != keepAliveDuration) {
            ConnectionPool pool = new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS);
            builder.connectionPool(pool);
        }
        okHttpClient = builder.build();
    }


    public String doGet(String url, String queryString, Map<String, String> headerMap) throws Exception {
        Response response;
        Request.Builder reqBuilder = this.prepareHeader(headerMap);
        Request getRequest = reqBuilder.url(url + "?" + queryString).get().build();
        response = okHttpClient.newCall(getRequest).execute();
        this.errorResponseLog(response, url);
        return response.body().string();
    }

    public String doPost(String url, String queryString, Map<String, String> paramMap, Map<String, String> headerMap)
            throws Exception {
        Response response;
        Request.Builder reqBuilder = new Request.Builder();
        if (headerMap != null) {
            for (Map.Entry entry : headerMap.entrySet()) {
                reqBuilder.addHeader(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (paramMap != null) {
            for (Map.Entry entry : paramMap.entrySet()) {
                formBodyBuilder.add(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        RequestBody body = formBodyBuilder.build();

        //POST JSON模式
        if(paramMap!=null && headerMap.containsKey("Content-Type") && headerMap.get("Content-Type").contains("json")){
            body = RequestBody.create(JSON, JSONObject.toJSONString(paramMap));
        }
        RequestBody reqBody;
        //FILE上传处理
        if(StringUtils.isNotEmpty(paramMap.get("file"))){
            File file=new File(paramMap.get("file"));
            RequestBody fileBody = RequestBody.create(MediaType.parse("text/plain"), file);
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            String filename = file.getName();
            requestBody.addFormDataPart("file", filename, fileBody);
            requestBody.addPart(body);
            reqBody = requestBody.build();
        }else{
            reqBody = body;
        }
        if (queryString != null && StringUtils.isNotEmpty(queryString)) {
            url = url + "?" + queryString;
        }
        Request postRequest = reqBuilder.url(url).post(reqBody).build();
        response = okHttpClient.newCall(postRequest).execute();
        this.errorResponseLog(response, url);
        return response.body().string();
    }

    public String doPut(String url, String queryString, Map<String, String> paramMap, Map<String, String> headerMap)
            throws Exception {
        Response response;
        Request.Builder reqBuilder = new Request.Builder();
        if (headerMap != null) {
            for (Map.Entry entry : headerMap.entrySet()) {
                reqBuilder.addHeader(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (paramMap != null) {
            for (Map.Entry entry : paramMap.entrySet()) {
                formBodyBuilder.add(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }

        RequestBody body = formBodyBuilder.build();
        RequestBody reqBody;

        //FILE上传处理
        if(StringUtils.isNotEmpty(paramMap.get("file"))){
            File file=new File(paramMap.get("file"));
            RequestBody fileBody = RequestBody.create(MediaType.parse("text/plain"), file);
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            String filename = file.getName();
            requestBody.addFormDataPart("file", filename, fileBody);
            requestBody.addPart(body);
            reqBody = requestBody.build();
        }else{
            reqBody = body;
        }

        if (queryString != null && StringUtils.isNotEmpty(queryString)) {
            url = url + "?" + queryString;
        }
        Request putRequest = reqBuilder.url(url).put(reqBody).build();
        response = okHttpClient.newCall(putRequest).execute();
        this.errorResponseLog(response, url);
        return response.body().string();
    }

    public String doMix(String url, String queryString, Map<String, String> headerMap) throws Exception {
        Response response = null;
        Request.Builder reqBuilder = this.prepareHeader(headerMap);
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        RequestBody body = formBodyBuilder.build();
        Request postRequest = reqBuilder.url(url + "?" + queryString).post(body).build();
        response = okHttpClient.newCall(postRequest).execute();
        this.errorResponseLog(response, url);
        return response.body().string();
    }

    public String doMix(String url, String queryString, Map<String, String> bodyParamMap, Map<String, String>
            headerMap) throws Exception {
        Response response = null;
        Request.Builder reqBuilder = this.prepareHeader(headerMap);
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (bodyParamMap != null) {
            for (String key : bodyParamMap.keySet()) {
                formBodyBuilder.add(key, headerMap.get(key));
            }
        }
        RequestBody body = formBodyBuilder.build();
        Request postRequest = reqBuilder.url(url + "?" + queryString).post(body).build();
        response = okHttpClient.newCall(postRequest).execute();
        this.errorResponseLog(response, url);
        return response.body().string();
    }

    public String doPostByJson(String url, String json, Map<String, String> headerMap) throws Exception {
        Response response = null;
        Request.Builder builder = this.prepareHeader(headerMap).url(url);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = builder.post(requestBody).build();
        response = okHttpClient.newCall(request).execute();
        this.errorResponseLog(response, url);
        return response.body().string();
    }

    public String doPutByJson(String url, String json, Map<String, String> headerMap) throws Exception {
        Response response = null;
        Request.Builder builder = this.prepareHeader(headerMap).url(url);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = builder.put(requestBody).build();
        response = okHttpClient.newCall(request).execute();
        this.errorResponseLog(response, url);
        return response.body().string();
    }


    private Request.Builder prepareHeader(Map<String, String> headerMap) {
        Request.Builder builder = new Request.Builder();
        if (headerMap != null) {
            for (String key : headerMap.keySet()) {
                builder.addHeader(key, headerMap.get(key));
            }
        }
        return builder;
    }


    private void errorResponseLog(Response response, String url) throws Exception {
        if (response == null) {
            LOGGER.error("okhttpclient 调用失败，url={},  message=response is null", url);
            throw new Exception(String.format("调用失败，url=%s,  status=null", url));
        }
        if (!response.isSuccessful()) {
            LOGGER.error("okhttpclient 调用失败，url={},  message={}", url, response.body().string());
            throw new Exception(String.format("调用失败，url=%s,  status=%s", url, response.code()));
        }
    }
}
