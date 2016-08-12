package com.lsl.huoqiu.net.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Set;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

public interface IHttpManager {
	
	enum HTTP_METHOD{
		GET,POST
	}

	/**
	 * 返回Http应答,若不是200,则将抛出异常
	 * GET方法
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public abstract Response executeHttpGet(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException;

	/**
	 * 返回Http应答,若不是200,则将抛出异常
	 * POST方法
	 * @param
	 * @return
	 * @throws IOException
	 */
	public abstract Response executeHttpPost(String url, Map<String, String> parameters, Map<String, String> headers)
			throws IOException;
	
	/**
	 * 返回Http应答,若不是200,则将抛出异常
	 * PUT方法
	 * @param
	 * @return
	 * @throws IOException
	 */
	public abstract Response executeHttpPut(String url, Map<String, String> parameters, Map<String, String> headers)
			throws IOException;

	/**
	 * 是否应当使用代理
	 * 
	 * @return
	 */
	// 代理相关事项移到ApnManager中
//	public abstract boolean shouldUseProxy();

	/**
	 * 返回OkHttClient
	 * @param isHttps
	 * @return
	 */
	public abstract OkHttpClient getHttpClient(boolean isHttps);

	/**
	 * Http应答的便捷方法,如果Http返回的不是200也将抛出异常
	 * 返回数据流
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public abstract InputStream getInputStream(String url) throws IOException;

	/**
	 * getInputStream的便捷方法,直接获得文本资源。
	 * 返回文本资源
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public abstract String getHttpText(String url) throws IOException;

	/**
	 * 得到HttpURLConnection
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public abstract HttpURLConnection getUrlConnection(String url) throws IOException;

}