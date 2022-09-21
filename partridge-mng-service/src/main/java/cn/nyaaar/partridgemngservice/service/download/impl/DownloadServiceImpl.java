package cn.nyaaar.partridgemngservice.service.download.impl;

import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.service.EleFileService;
import cn.nyaaar.partridgemngservice.service.download.DownloadService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.requestBuilder.EhRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

/**
 * @author nyaaar
 * @Version $Id: DownloadServiceImpl.java, v 0.1 2022-31 17:01 nyaaar Exp $$
 */
@Service
@Slf4j
public class DownloadServiceImpl implements DownloadService {
    private final OkHttpClient okHttpClient;

    private final ThreadPoolTaskExecutor downloadExecutor;

    private final EleFileService eleFileService;

    public DownloadServiceImpl(OkHttpClient okHttpClient, ThreadPoolTaskExecutor downloadExecutor, EleFileService eleFileService) {
        this.okHttpClient = okHttpClient;
        this.downloadExecutor = downloadExecutor;
        this.eleFileService = eleFileService;
    }

    @Override
    public void downloadUrlToDest(@NotNull String url, @NotNull String destDic, @NotNull String fileName, Runnable successHandle, Runnable failHandle) {
        downloadExecutor.submit(() -> {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    log.error("download error, url:{}", url, e);
                    if (failHandle != null) {
                        failHandle.run();
                    }
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    try {
                        byte[] bytes = Objects.requireNonNull(response.body()).bytes();
                        eleFileService.saveBytesToFile(bytes, destDic, fileName, false);
                        if (successHandle != null) {
                            successHandle.run();
                        }
                    } catch (IOException e) {
                        log.error("read response or save error, url:{}", url, e);
                        failHandle.run();
                    }
                }
            });
        });

    }

    @Override
    public String downloadUrlToBase64(String url) {
        Request request = new EhRequestBuilder(url, null).build();
        String urlBase64 = "";
        try {
            Response response = okHttpClient.newCall(request).execute();
            urlBase64 = FileUtil.bytes2Base64(Objects.requireNonNull(response.body()).bytes());
        } catch (IOException e) {
            BusinessExceptionEnum.HTTP_REQUEST_FAILED.assertFail();
        }
        return urlBase64;
    }
}