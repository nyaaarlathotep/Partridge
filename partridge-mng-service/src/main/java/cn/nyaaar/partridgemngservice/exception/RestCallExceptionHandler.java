package cn.nyaaar.partridgemngservice.exception;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RestCallExceptionHandler extends DefaultResponseErrorHandler {
    /**
     * @see org.springframework.web.client.ResponseErrorHandler#handleError(ClientHttpResponse)
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        String body = inputStream2String(response.getBody());

        throw new IOException(body);
    }

    /**
     * @see org.springframework.web.client.ResponseErrorHandler#hasError(ClientHttpResponse)
     */
    @Override
    public boolean hasError(ClientHttpResponse arg0) throws IOException {
        return super.hasError(arg0);
    }

    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
}
