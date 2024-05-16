package com.weweibuy.framework.common.core.support;

import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.buf.UDecoder;
import org.apache.tomcat.util.http.Parameters;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author durenhao
 * @date 2023/2/12 19:20
 **/
public class CopyContentCachingRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 缓存请求流的输入流
     */
    private final FastByteArrayOutputStream cachedContent;

    private ServletInputStream inputStream;

    private Parameters parameters;

    private Boolean parameterBodyRead = false;


    public CopyContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
        int contentLength = request.getContentLength();
        this.cachedContent = new FastByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new CopyContentCachingRequestWrapper.ContentCachingInputStream(getRequest().getInputStream());
        }

        if (inputStream.isFinished()) {
            return new CopyContentCachingRequestWrapper.ByteArrayServletInputStream(cachedContent.getInputStream());
        }
        return this.inputStream;
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(),
                getCharacterEncoding()));
    }

    /**
     * @param name
     * @return
     * @see ContentCachingRequestWrapper#getParameter
     */
    @Override
    public String getParameter(String name) {
        if (!parameterBodyRead && this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        if (cachedContent.size() == 0) {
            return null;
        }
        if (parameters == null) {
            parseParameters();
        }
        return parameters.getParameter(name);
    }

    /**
     * @return
     * @see ContentCachingRequestWrapper#getParameterMap
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        if (!parameterBodyRead && this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        if (cachedContent.size() == 0) {
            return null;
        }
        if (parameters == null) {
            parseParameters();
        }
        Map<String, String[]> parameterMap = new HashMap<>();
        Enumeration<String> enumeration = parameters.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String[] values = getParameterValues(name);
            parameterMap.put(name, values);
        }
        return parameterMap;
    }

    /**
     * @return
     * @see ContentCachingRequestWrapper#getParameterNames
     */
    @Override
    public Enumeration<String> getParameterNames() {
        if (!parameterBodyRead && this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        if (cachedContent.size() == 0) {
            return null;
        }
        return super.getParameterNames();
    }

    /**
     * @return
     * @see ContentCachingRequestWrapper#getParameterValues
     */
    @Override
    public String[] getParameterValues(String name) {
        if (!parameterBodyRead && this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        if (cachedContent.size() == 0) {
            return null;
        }
        if (parameters == null) {
            parseParameters();
        }
        return parameters.getParameterValues(name);
    }


    private boolean isFormPost() {
        String contentType = getContentType();
        return (contentType != null && contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) && HttpMethod.POST.matches(getMethod()));
    }

    private void parseParameters() {
        parameters = new Parameters();
        String queryString = getQueryString();
        if (queryString != null) {
            MessageBytes messageBytes = MessageBytes.newInstance();
            messageBytes.setString(getQueryString());
            parameters.setQuery(messageBytes);
            parameters.setURLDecoder(new UDecoder());
        }
        parameters.processParameters(cachedContent.toByteArray(), 0, cachedContent.size());
    }

    private void writeRequestParametersToCachedContent() {
        try {
            if (this.cachedContent.size() == 0) {
                String requestEncoding = getCharacterEncoding();
                Map<String, String[]> form = super.getParameterMap();
                for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
                    String name = nameIterator.next();
                    List<String> values = Arrays.asList(form.get(name));
                    for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
                        String value = valueIterator.next();
                        this.cachedContent.write(URLEncoder.encode(name, requestEncoding).getBytes());
                        if (value != null) {
                            this.cachedContent.write('=');
                            this.cachedContent.write(URLEncoder.encode(value, requestEncoding).getBytes());
                            if (valueIterator.hasNext()) {
                                this.cachedContent.write('&');
                            }
                        }
                    }
                    if (nameIterator.hasNext()) {
                        this.cachedContent.write('&');
                    }
                }
            }
            parameterBodyRead = true;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write request parameters to cached content", ex);
        }
    }


    private class ContentCachingInputStream extends ServletInputStream {

        private final ServletInputStream is;

        public ContentCachingInputStream(ServletInputStream is) {
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            int ch = this.is.read();
            if (ch != -1) {
                cachedContent.write(ch);
            }
            return ch;
        }

        @Override
        public int read(byte[] b) throws IOException {
            int count = this.is.read(b);
            writeToCache(b, 0, count);
            return count;
        }

        private void writeToCache(final byte[] b, final int off, int count) throws IOException {
            if (count > 0) {
                cachedContent.write(b, off, count);
            }
        }

        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            int count = this.is.read(b, off, len);
            writeToCache(b, off, count);
            return count;
        }

        @Override
        public int readLine(final byte[] b, final int off, final int len) throws IOException {
            int count = this.is.readLine(b, off, len);
            writeToCache(b, off, count);
            return count;
        }

        @Override
        public boolean isFinished() {
            return this.is.isFinished();
        }

        @Override
        public boolean isReady() {
            return this.is.isReady();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            this.is.setReadListener(readListener);
        }
    }

    private class ByteArrayServletInputStream extends ServletInputStream {

        private final InputStream is;

        public ByteArrayServletInputStream(InputStream is) {
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            return this.is.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return this.is.read(b);
        }

        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            return this.is.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            try {
                return is.available() <= 0;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }
    }


}
