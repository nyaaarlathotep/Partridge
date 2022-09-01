package cn.nyaaar.partridgemngservice.service.download.impl;

import cn.nyaaar.partridgemngservice.constants.Settings;
import cn.nyaaar.partridgemngservice.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.service.download.DownloadService;
import cn.nyaaar.partridgemngservice.service.file.FileSaveService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

/**
 * @author yuegenhua
 * @Version $Id: DownloadServiceImpl.java, v 0.1 2022-31 17:01 yuegenhua Exp $$
 */
@Service
@Slf4j
public class DownloadServiceImpl implements DownloadService {
    private final OkHttpClient okHttpClient;

    private final ThreadPoolTaskExecutor downloadExecutor;

    private final FileSaveService fileSaveService;

    public DownloadServiceImpl(OkHttpClient okHttpClient, ThreadPoolTaskExecutor downloadExecutor, FileSaveService fileSaveService) {
        this.okHttpClient = okHttpClient;
        this.downloadExecutor = downloadExecutor;
        this.fileSaveService = fileSaveService;
    }

    @Override
    public void downloadUrlToDest(@NotNull String url, @NotNull String destDic, @NotNull String fileName) {
        downloadExecutor.submit(() -> {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    log.error("download error, url:{}", url, e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    byte[] bytes = Objects.requireNonNull(response.body()).bytes();
                    fileSaveService.saveBytesToFileWithSource(bytes, destDic, fileName, SourceEnum.Ehentai, false);
                    log.info("download success!");
                }
            });
        });

    }
}