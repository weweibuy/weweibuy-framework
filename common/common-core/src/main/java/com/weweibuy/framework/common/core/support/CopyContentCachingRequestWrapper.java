package com.weweibuy.framework.common.core.support;

import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
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

    private MultiValueMap<String, String> queryMap;

    public CopyContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
        int contentLength = request.getContentLength();
        this.cachedContent = new FastByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        String queryString = request.getQueryString();
        queryMap = HttpRequestUtils.parseQueryParams(queryString);
        if (isFormPost()) {
            readForm(request);
        }
    }

    private void readForm(HttpServletRequest request) {
        request.getParameterNames();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new ContentCachingInputStream(getRequest().getInputStream());
        }

        if (inputStream.isFinished()) {
            return withFinished();
        }
        return this.inputStream;
    }

    private ServletInputStream withFinished() {
        if (cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return new ByteArrayServletInputStream(cachedContent.getInputStream());
    }


    private void writeRequestParametersToCachedContent() {
        try {
            if (this.cachedContent.size() == 0) {
                Map<String, String[]> form = super.getParameterMap();
                for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
                    String name = nameIterator.next();
                    if (queryMap.containsKey(name)) {
                        continue;
                    }
                    List<String> values = Arrays.asList(form.get(name));
                    for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
                        String value = valueIterator.next();
                        this.cachedContent.write(name.getBytes());
                        if (value != null) {
                            this.cachedContent.write('=');
                            this.cachedContent.write(value.getBytes());
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
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write request parameters to cached content", ex);
        }
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


    private boolean isFormPost() {
        String contentType = getContentType();
        return (contentType != null && contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) && HttpMethod.POST.matches(getMethod()));
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
