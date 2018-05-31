package net.yaliun.tosima;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import com.google.gson.Gson;



public class SampleClient {
	
	public void reqRest(String url) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		
		int httpResCode = 0;
		
		String reqJson = null;
		String resJson = null;
		
		StringEntity reqEntity = null;
		
		Gson gson = new Gson();
		
		try {
			//reqJson = gson.toJson(reqVo);
			reqEntity = new StringEntity(reqJson);
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8");
		httpPost.setEntity(reqEntity);
		
		try {
			response = getHttpClient(httpClient).execute(httpPost);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		httpResCode = response.getStatusLine().getStatusCode();
		
		try {
			resJson = EntityUtils.toString(response.getEntity());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public CloseableHttpClient getHttpClient(CloseableHttpClient httpClient) throws Exception{
		SSLContext sslContext = null;
		
		int reqTimeout = 1000 * 30;
		
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(getSSLContext(sslContext));
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(reqTimeout)
				.setConnectionRequestTimeout(reqTimeout)
				.setConnectionRequestTimeout(reqTimeout)
				.build();
		
		httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setSSLSocketFactory(csf)
				.build();
		
		return httpClient;
	}
	
	public SSLContext getSSLContext(SSLContext sslContext) throws Exception{
		File keystoreFile = new File("D:\\Project\\spring_workspace\\Tosima\\src\\main\\resources");
		File trustStoreFile = new File("D:\\Project\\spring_workspace\\Tosima\\src\\main\\resources");
		
		char[] passwd = "changeit".toCharArray();
		
		sslContext = SSLContexts.custom()
				.loadKeyMaterial(keystoreFile, passwd, passwd)
				.loadTrustMaterial(trustStoreFile, passwd)
				.build();				
		
		return sslContext;
	}
}
