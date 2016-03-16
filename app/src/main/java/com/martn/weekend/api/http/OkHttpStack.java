package com.martn.weekend.api.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HurlStack;
import com.socks.library.KLog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

/**
 * Title: Juyixia
 * Package: com.lefu.juyixia.api.volley
 * Description: ("使用okhttp的请求栈")
 * Date 2015/8/27 15:34
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class OkHttpStack extends HurlStack {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private SSLSocketFactory mSslSocketFactory = null;
    private final UrlRewriter mUrlRewriter = null;
    private final OkUrlFactory okUrlFactory;


    public OkHttpStack() {

        this(new OkUrlFactory(new OkHttpClient()));
    }

    public OkHttpStack(OkUrlFactory factory) {

        if (factory == null)
            throw new NullPointerException("Client must not be null.");
        this.okUrlFactory = factory;
    }

    private static void addBodyIfExists(HttpURLConnection urlConnection, Request<?> request)
            throws IOException, AuthFailureError {

        byte[] data = request.getBody();
        if (data != null) {
            urlConnection.setDoOutput(true);
            String contentType = request.getBodyContentType();
            urlConnection.addRequestProperty(HEADER_CONTENT_TYPE, contentType);
            OutputStream os = urlConnection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.write(data);
            dos.close();
        }
    }

    private static HttpEntity entityFromConnection(HttpURLConnection urlConnection) {

        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        try {
            InputStream is = urlConnection.getInputStream();
            basicHttpEntity.setContent(is);
            long length = urlConnection.getContentLength();
            basicHttpEntity.setContentLength(length);
            String encoding = urlConnection.getContentEncoding();
            basicHttpEntity.setContentEncoding(encoding);
            String contentType = urlConnection.getContentType();
            basicHttpEntity.setContentType(contentType);
        } catch (IOException e) {
            InputStream inputStream = urlConnection.getErrorStream();
        }
        return basicHttpEntity;
    }

    @Override
    protected HttpURLConnection createConnection(URL url)
            throws IOException {

        return this.okUrlFactory.open(url);
    }


    /**
     * 打开链接
     * @param url
     * @param request
     * @return
     * @throws IOException
     */
    private HttpURLConnection openConnection(URL url, Request<?> request)
            throws IOException {

        HttpURLConnection urlConnection = createConnection(url);
        int timeoutMs = request.getTimeoutMs();
        urlConnection.setConnectTimeout(timeoutMs);
        urlConnection.setReadTimeout(timeoutMs);
        urlConnection.setUseCaches(false);
        urlConnection.setDoInput(true);
//        try {
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            TrustManager[] trustManagers = new TrustManager[1];
//            HttpsTrustManager manager = new HttpsTrustManager();
//            trustManagers[0] = manager;
//
//            SecureRandom random = new SecureRandom();
//            sslContext.init(null, trustManagers, random);
//            mSslSocketFactory = sslContext.getSocketFactory();
//
//            String protocol = url.getProtocol();
//            if (("https".equals(protocol)) && (mSslSocketFactory != null)) {
//                urlConnection.setSSLSocketFactory(mSslSocketFactory);
//            }
//        } catch (Exception e) {
//                e.printStackTrace();
//        }
        return urlConnection;
    }

    /**
     * 带请求参数的请求
     * @param urlConnection
     * @param request
     * @throws IOException
     * @throws AuthFailureError
     */
    static void setConnectionParametersForRequest(HttpURLConnection urlConnection, Request<?> request)
            throws IOException, AuthFailureError {

        switch (request.getMethod()) {

            case Request.Method.DEPRECATED_GET_OR_POST:
                byte[] body = request.getPostBody();
                if (body != null) {
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    String contentType = request.getPostBodyContentType();
                    urlConnection.addRequestProperty("Content-Type", contentType);

                    OutputStream os = urlConnection.getOutputStream();
                    DataOutputStream localDataOutputStream = new DataOutputStream(os);
                    localDataOutputStream.write(body);
                    localDataOutputStream.close();
                }
                break;
            case Request.Method.GET:
                urlConnection.setRequestMethod("GET");
                break;
            case Request.Method.DELETE:
                urlConnection.setRequestMethod("DELETE");
                addBodyIfExists(urlConnection, request);
                break;
            case Request.Method.POST:
                urlConnection.setRequestMethod("POST");
                addBodyIfExists(urlConnection, request);
                break;
            case Request.Method.PUT:
                urlConnection.setRequestMethod("PUT");
                addBodyIfExists(urlConnection, request);
                break;
            case Request.Method.HEAD:
                urlConnection.setRequestMethod("HEAD");
                break;
            case Request.Method.OPTIONS:
                urlConnection.setRequestMethod("OPTIONS");
                break;
            case Request.Method.TRACE:
                urlConnection.setRequestMethod("TRACE");
                break;
            case Request.Method.PATCH:
                urlConnection.setRequestMethod("PATCH");
                addBodyIfExists(urlConnection, request);
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    /**
     * 处理请求
     * @param request
     * @param value
     * @return
     * @throws IOException
     * @throws AuthFailureError
     */
    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> value)
            throws IOException, AuthFailureError {

        String url = request.getUrl();
        HashMap headers = new HashMap();
        //请求头部
        headers.putAll(request.getHeaders());
        headers.putAll(value);

        /**
         * 处理对url的重写----这里我们没有对url的重写机制
         */
        if (mUrlRewriter != null) {
            String newUrl = mUrlRewriter.rewriteUrl(url);
            if (newUrl == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
            url = newUrl;
        }
        URL Url = new URL(url);
        //打开网络连接
        HttpURLConnection urlConnection = openConnection(Url, request);
        //添加请求头部
        Iterator iterator = headers.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();

            String values = (String) headers.get(key);
            urlConnection.addRequestProperty(key, values);
        }

        setConnectionParametersForRequest(urlConnection, request);

        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);

        if (urlConnection.getResponseCode() == -1)
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");

        int responseCode = urlConnection.getResponseCode();
        String message = urlConnection.getResponseMessage();

        BasicStatusLine statusLine = new BasicStatusLine(protocolVersion, responseCode, message);

        BasicHttpResponse  httpResponse = new BasicHttpResponse(statusLine);

        HttpEntity entity = entityFromConnection(urlConnection);
        httpResponse.setEntity(entity);
        for (Map.Entry<String, List<String>> entry : urlConnection.getHeaderFields().entrySet()) {
            if (entry.getKey() != null) {
                httpResponse.addHeader(new BasicHeader(entry.getKey(), entry.getValue().get(0)));
            }

        }
        return httpResponse;
    }

    /**
     * juyixia---hostname验证
     */
    class ZeaHostnameVerifier implements HostnameVerifier {

        private ZeaHostnameVerifier() {
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            if ((hostname.contains("boohee.com")) || (hostname.contains("boohee.cn"))) {
                String str = "hostname-->" + hostname + "---isTrue:";
                KLog.e("OkHttpStack", str);
                return true;
            }
            return false;
        }
    }

    /**
     * url重新
     */
    public abstract interface UrlRewriter {

        public abstract String rewriteUrl(String url);
    }
}
