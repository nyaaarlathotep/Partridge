package cn.nyaaar.partridgemngservice.common.config;


import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;


/**
 * @author nyaaar
 */
@Configuration
@Slf4j
public class BeanConfig {
    // 读取超时时间
    private final int readTimeout = 60000;

    // 连接超时时间
    private final int connectTimeout = 5000;

    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(200, 5, TimeUnit.MINUTES);
    }

    @Bean
    public OkHttpClient okHttpClient() {

        return new OkHttpClient.Builder()
                // TODO setting
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .retryOnConnectionFailure(false)//是否开启缓存
                .connectionPool(pool())//连接池
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error(e.toString());
        }
        return null;
    }


    @Bean(name = "downloadExecutor")
    public ThreadPoolTaskExecutor downloadExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        //核心线程池数
        pool.setCorePoolSize(2);
        //最大线程数
        pool.setMaxPoolSize(5);
        //队列容量
        pool.setQueueCapacity(300);
        // 线程最大空闲时间
        pool.setKeepAliveSeconds(300);
        //队列满，调用线程中运行被拒绝的任务。
        pool.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        pool.setThreadNamePrefix("download-executor---");
        return pool;
    }
}
