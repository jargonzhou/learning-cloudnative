package com.spike.springcloud.grafana.web.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

// ContentCachingRequestWrapper does not cache the request body content.
// see https://github.com/spring-projects/spring-framework/pull/24533#issuecomment-589188646
public class RepeatableContentCachingRequestWrapper extends ContentCachingRequestWrapper {

    public RepeatableContentCachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StreamUtils.drain(super.getInputStream());
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ByteServletInputStream(getContentAsByteArray());
    }


    private static class ByteServletInputStream extends ServletInputStream {

        private final InputStream is;

        private ByteServletInputStream(byte[] content) {
            this.is = new ByteArrayInputStream(content);
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return this.is.read();
        }

        @Override
        public void close() throws IOException {
            this.is.close();
        }
    }
}