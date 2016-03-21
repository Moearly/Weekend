package com.qmusic.volley;

import com.android.volley.toolbox.HttpStack;
import android.text.TextUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HurlStack;
import com.qmusic.db.UserPreference;
import com.socks.library.KLog;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;

/**
 * Title: Weekend
 * Package: com.qmusic.volley
 * Description: ("请描述功能")
 * Date 2016/3/21 14:48
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class QMusicHurlStack implements HttpStack {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private final SSLSocketFactory mSslSocketFactory;
    private final UrlRewriter mUrlRewriter;

    public interface UrlRewriter {
        String rewriteUrl(String str);
    }

    public QMusicHurlStack() {
        this(null);
    }

    public QMusicHurlStack(UrlRewriter urlRewriter) {
        this(urlRewriter, null);
    }

    public QMusicHurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        this.mUrlRewriter = urlRewriter;
        this.mSslSocketFactory = sslSocketFactory;
    }

    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        String url = request.getUrl();
        HashMap<String, String> map = new HashMap();
        map.putAll(request.getHeaders());
        map.putAll(additionalHeaders);
        addAddicationHeaders(request, map);
        if (this.mUrlRewriter != null) {
            String rewritten = this.mUrlRewriter.rewriteUrl(url);
            if (rewritten == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
            url = rewritten;
        }
        HttpURLConnection connection = openConnection(new URL(url), request);
        for (String headerName : map.keySet()) {
            connection.addRequestProperty(headerName, (String) map.get(headerName));
        }
        setConnectionParametersForRequest(connection, request);
        ProtocolVersion protocolVersion = new ProtocolVersion(HttpVersion.HTTP, 1, 1);
        if (connection.getResponseCode() == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        BasicHttpResponse response = new BasicHttpResponse(new BasicStatusLine(protocolVersion, connection.getResponseCode(), connection.getResponseMessage()));
        response.setEntity(entityFromConnection(connection));
        for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                String value = "";
                for (int i = 0; i < ((List) header.getValue()).size(); i++) {
                    value = new StringBuilder(String.valueOf(value)).append((String) ((List) header.getValue()).get(i)).append(";").toString();
                }
                response.addHeader(new BasicHeader((String) header.getKey(), value));
            }
        }
        return response;
    }

    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        InputStream inputStream;
        BasicHttpEntity entity = new BasicHttpEntity();
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength((long) connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = createConnection(url);
        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        if ("https".equals(url.getProtocol()) && this.mSslSocketFactory != null) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(this.mSslSocketFactory);
        }
        return connection;
    }

    static void setConnectionParametersForRequest(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case Request.Method.DEPRECATED_GET_OR_POST :
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getPostBodyContentType());
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(postBody);
                    out.close();
                }
                break;
            case Request.Method.GET:
                connection.setRequestMethod("GET");
                break;

            case Request.Method.POST:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
                break;

            case Request.Method.PUT:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
                break;

            case Request.Method.DELETE:
                connection.setRequestMethod("DELETE");
                break;

            case Request.Method.HEAD:
                connection.setRequestMethod("HEAD");
                break;

            case Request.Method.OPTIONS:
                connection.setRequestMethod("OPTIONS");
                break;

            case Request.Method.TRACE:
                connection.setRequestMethod("TRACE");
                break;

            case Request.Method.PATCH:
                connection.setRequestMethod("PATCH");
                addBodyIfExists(connection, request);
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void addBodyIfExists(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        DataOutputStream out;
        if (request instanceof QMusicMultipartRequest) {
            connection.setDoOutput(true);
            connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getBodyContentType());
            out = new DataOutputStream(connection.getOutputStream());
            ((QMusicMultipartRequest) request).wirteTo(out);
            out.close();
            return;
        }
        byte[] body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getBodyContentType());
            out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }
    }

    private void addAddicationHeaders(Request<?> request, Map<String, String> headers) {
        String cookie = UserPreference.getUserToken() + ";" + UserPreference.getCity();
        if (!TextUtils.isEmpty(UserPreference.getUserToken())) {
            headers.put("Cookie", cookie);
            KLog.e("cookie------> add : " + cookie);
        }
        if (request instanceof QmusicRequest) {
            QmusicRequest request2 = (QmusicRequest) request;
            String userAgent = QmusicRequest.getUserAgent();
            if (!TextUtils.isEmpty(userAgent)) {
                headers.put(HTTP.USER_AGENT, userAgent);
            }
        }
    }
}