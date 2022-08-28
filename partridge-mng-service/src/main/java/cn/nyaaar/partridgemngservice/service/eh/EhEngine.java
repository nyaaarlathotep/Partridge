/**
 * Saicfinance.com Inc.
 * Copyright (c) 1994-2022 All Rights Reserved.
 */
package cn.nyaaar.partridgemngservice.service.eh;

import cn.nyaaar.partridgemngservice.constants.EhUrl;
import cn.nyaaar.partridgemngservice.constants.Settings;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.exception.eh.EhException;
import cn.nyaaar.partridgemngservice.exception.eh.ParseException;
import cn.nyaaar.partridgemngservice.model.eh.GalleryInfo;
import cn.nyaaar.partridgemngservice.util.ExceptionUtils;
import cn.nyaaar.partridgemngservice.util.StringUtils;
import cn.nyaaar.partridgemngservice.util.parser.GalleryApiParser;
import cn.nyaaar.partridgemngservice.util.parser.GalleryListParser;
import cn.nyaaar.partridgemngservice.util.parser.GalleryNotAvailableParser;
import cn.nyaaar.partridgemngservice.util.parser.SignInParser;
import cn.nyaaar.partridgemngservice.util.requestBuilder.EhRequestBuilder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yuegenhua
 * @Version $Id: EhEngine.java, v 0.1 2022-28 12:03 yuegenhua Exp $$
 */
@Service
@Slf4j
public class EhEngine {

    @Autowired
    private OkHttpClient okHttpClient;
    public static EhFilter sEhFilter;
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SAD_PANDA_TYPE = "image/gif";
    private static final String SAD_PANDA_LENGTH = "9615";
    private static final String SAD_PANDA_DISPOSITION = "inline; filename=\"sadpanda.jpg\"";
    private static final String KOKOMADE_URL = "https://exhentai.org/img/kokomade.jpg";
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");


    private static void doThrowException(Call call, int code, @Nullable Headers headers,
                                         @Nullable String body, Throwable e) throws Throwable {
        if (call.isCanceled()) {
            throw new EhException("call cancelled");
        }

        // Check sad panda
        if (headers != null && SAD_PANDA_DISPOSITION.equals(headers.get("Content-Disposition")) &&
                SAD_PANDA_TYPE.equals(headers.get("Content-Type")) &&
                SAD_PANDA_LENGTH.equals(headers.get("Content-Length"))) {
            throw new EhException("Sad Panda");
        }

        // Check sad panda(without panda)
        if (headers != null && "text/html; charset=UTF-8".equals(headers.get("Content-Type")) &&
                "0".equals(headers.get("Content-Length"))) {
            throw new EhException("Sad Panda\n(without panda)");
        }

        // Check kokomade
        if (body != null && body.contains(KOKOMADE_URL)) {
            throw new EhException("今回はここまで\n\n");
        }

        if (body != null && body.contains("Gallery Not Available - ")) {
            String error = GalleryNotAvailableParser.parse(body);
            if (!StringUtils.isEmpty(error)) {
                throw new EhException(error);
            }
        }

        if (e instanceof ParseException) {
            if (body != null && !body.contains("<")) {
                throw new EhException(body);
            } else if (StringUtils.isEmpty(body)) {
                throw new EhException("body is empty");
            } else {
                if (Settings.getSaveParseErrorBody()) {
//                    AppConfig.saveParseErrorBody((ParseException) e);
                }
                throw new EhException("parse error");
            }
        }

        if (code >= 400) {
            throw new EhException(code + " error");
        }
        if (e != null) {
            throw e;
        }
    }

    private static void throwException(Call call, int code, @Nullable Headers headers,
                                       @Nullable String body, Throwable e) throws Throwable {
        try {
            doThrowException(call, code, headers, body, e);
        } catch (Throwable error) {
            error.printStackTrace();
            throw error;
        }
    }

    public String signIn(String username, String password) throws Throwable {
        String referer = "https://forums.e-hentai.org/index.php?act=Login&CODE=00";
        FormBody.Builder builder = new FormBody.Builder()
                .add("referer", referer)
                .add("b", "")
                .add("bt", "")
                .add("UserName", username)
                .add("PassWord", password)
                .add("CookieDate", "1");
        String url = EhUrl.API_SIGN_IN;
        String origin = "https://forums.e-hentai.org";
        log.info("signIn url:{}", url);
        Request request = new EhRequestBuilder(url, referer, origin)
                .post(builder.build())
                .build();
        Call call = okHttpClient.newCall(request);


        String body = null;
        Headers headers = null;
        int code = -1;
        try {
            Response response = call.execute();
            code = response.code();
            headers = response.headers();
            body = response.body().string();
            return SignInParser.parse(body);
        } catch (Throwable e) {
            log.error(String.valueOf(call), code, headers, body, e);
            throw e;
        }
    }

    public GalleryListParser.Result getGalleryList(String url) throws Throwable {
        String referer = EhUrl.getReferer();
        log.info("getGalleryList url:{}", url);
        Request request = new EhRequestBuilder(url, referer).build();
        Call call = okHttpClient.newCall(request);
        String body = null;
        Headers headers = null;
        GalleryListParser.Result result;
        int code = -1;
        try {
            Response response = call.execute();
            code = response.code();
            headers = response.headers();
            body = response.body().string();
            result = GalleryListParser.parse(body);
        } catch (Throwable e) {
            ExceptionUtils.throwIfFatal(e);
            throwException(call, code, headers, body, e);
            throw e;
        }

        fillGalleryList(result.galleryInfoList, url, true);
        return result;
    }

    private void fillGalleryList(List<GalleryInfo> list, String url, boolean filter) throws Throwable {
        // Filter title and uploader
        if (filter) {
            for (int i = 0, n = list.size(); i < n; i++) {
                GalleryInfo info = list.get(i);
                if (!sEhFilter.filterTitle(info) || !sEhFilter.filterUploader(info)) {
                    list.remove(i);
                    i--;
                    n--;
                }
            }
        }
        boolean hasTags = false;
        boolean hasPages = false;
        boolean hasRated = false;
        for (GalleryInfo gi : list) {
            if (gi.simpleTags != null) {
                hasTags = true;
            }
            if (gi.pages != 0) {
                hasPages = true;
            }
            if (gi.rated) {
                hasRated = true;
            }
        }

        boolean needApi = (filter && sEhFilter.needTags() && !hasTags) ||
                (Settings.getShowGalleryPages() && !hasPages) ||
                hasRated;
        if (needApi) {
            fillGalleryListByApi(list, url);
        }

        // Filter tag
        if (filter) {
            for (int i = 0, n = list.size(); i < n; i++) {
                GalleryInfo info = list.get(i);
                // Thumbnail mode need filter uploader again
                if (!sEhFilter.filterUploader(info) || !sEhFilter.filterTag(info) || !sEhFilter.filterTagNamespace(info)) {
                    list.remove(i);
                    i--;
                    n--;
                }
            }
        }

        if (Settings.getFixThumbUrl()) {
            for (GalleryInfo info : list) {
                info.thumb = EhUrl.getFixedPreviewThumbUrl(info.thumb);
            }
        }
    }

    public List<GalleryInfo> fillGalleryListByApi(
            List<GalleryInfo> galleryInfoList, String referer) throws Throwable {
        // We can only request 25 items one time at most
        final int MAX_REQUEST_SIZE = 25;
        List<GalleryInfo> requestItems = new ArrayList<>(MAX_REQUEST_SIZE);
        for (int i = 0, size = galleryInfoList.size(); i < size; i++) {
            requestItems.add(galleryInfoList.get(i));
            if (requestItems.size() == MAX_REQUEST_SIZE || i == size - 1) {
                doFillGalleryListByApi(requestItems, referer);
                requestItems.clear();
            }
        }
        return galleryInfoList;
    }


    private void doFillGalleryListByApi(
            List<GalleryInfo> galleryInfoList, String referer) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("method", "gdata");
        JSONArray ja = new JSONArray();
        for (GalleryInfo gi : galleryInfoList) {
            JSONArray g = new JSONArray();
            g.add(gi.gid);
            g.add(gi.token);
            ja.add(g);
        }
        json.put("gidlist", ja);
        json.put("namespace", 1);
        String url = EhUrl.getApiUrl();
        String origin = EhUrl.getOrigin();
        log.info("doFillGalleryListByApi url:{}", url);
        Request request = new EhRequestBuilder(url, referer, origin)
                .post(RequestBody.create(json.toString(), MEDIA_TYPE_JSON))
                .build();
        Call call = okHttpClient.newCall(request);


        String body = null;
        Headers headers = null;
        int code = -1;
        try {
            Response response = call.execute();
            code = response.code();
            headers = response.headers();
            body = response.body().string();
            GalleryApiParser.parse(body, galleryInfoList);
        } catch (Throwable e) {
            ExceptionUtils.throwIfFatal(e);
            log.error(e.toString());
            BusinessExceptionEnum.BAD_REQUEST.assertFail();
        }
    }
}