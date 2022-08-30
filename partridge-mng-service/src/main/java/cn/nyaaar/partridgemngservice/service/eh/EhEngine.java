package cn.nyaaar.partridgemngservice.service.eh;

import cn.nyaaar.partridgemngservice.constants.EhUrl;
import cn.nyaaar.partridgemngservice.constants.Settings;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.exception.eh.ParseException;
import cn.nyaaar.partridgemngservice.model.eh.GalleryDetail;
import cn.nyaaar.partridgemngservice.model.eh.GalleryInfo;
import cn.nyaaar.partridgemngservice.util.ExceptionUtils;
import cn.nyaaar.partridgemngservice.util.StringUtils;
import cn.nyaaar.partridgemngservice.util.parser.*;
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

    private OkHttpClient okHttpClient;

    public static EhFilter sEhFilter = EhFilter.getInstance();
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    private static final String SAD_PANDA_TYPE = "image/gif";
    private static final String SAD_PANDA_LENGTH = "9615";
    private static final String SAD_PANDA_DISPOSITION = "inline; filename=\"sadpanda.jpg\"";
    private static final String KOKOMADE_URL = "https://exhentai.org/img/kokomade.jpg";


    @Autowired
    public void DI(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    private static void doThrowException(Call call, int code, @Nullable Headers headers,
                                         @Nullable String body, Exception e) throws Exception {
        if (call.isCanceled()) {
            BusinessExceptionEnum.HTTP_REQUEST_FAILED.assertFail();
        }

        // Check sad panda
        if (headers != null && SAD_PANDA_DISPOSITION.equals(headers.get("Content-Disposition")) &&
                SAD_PANDA_TYPE.equals(headers.get("Content-Type")) &&
                SAD_PANDA_LENGTH.equals(headers.get("Content-Length"))) {

            BusinessExceptionEnum.SAD_PANDA.assertFail();
        }

        // Check sad panda(without panda)
        if (headers != null && "text/html; charset=UTF-8".equals(headers.get("Content-Type")) &&
                "0".equals(headers.get("Content-Length"))) {
            BusinessExceptionEnum.SAD_PANDA_WITHOUT.assertFail();
        }

        // Check kokomade
        if (body != null && body.contains(KOKOMADE_URL)) {
            BusinessExceptionEnum.END_HERE.assertFail();
        }

        if (body != null && body.contains("Gallery Not Available - ")) {
            String error = GalleryNotAvailableParser.parse(body);
            if (!StringUtils.isEmpty(error)) {
                BusinessExceptionEnum.GALLERY_NOT_AVAILABLE.assertFail(error);
            }
        }

        if (e instanceof ParseException) {
            if (body != null && !body.contains("<")) {
                BusinessExceptionEnum.PARSE_ERROR.assertFail("body: " + body);
            } else if (StringUtils.isEmpty(body)) {
                BusinessExceptionEnum.PARSE_ERROR.assertFail("body is empty");
            } else {
                log.error("unrecognized parse error", e);
                BusinessExceptionEnum.PARSE_ERROR.assertFail("unrecognized parse error");
            }
        }

        if (code >= 400) {
            BusinessExceptionEnum.HTTP_REQUEST_FAILED.assertFail(code + " error");
        }
        if (e != null) {
            throw e;
        }
    }

    private static void throwException(Call call, int code, @Nullable Headers headers,
                                       @Nullable String body, Throwable e) {
        if (e instanceof Error) {
            ExceptionUtils.throwIfFatal(e);
        } else if (e instanceof Exception) {
            try {
                doThrowException(call, code, headers, body, (Exception) e);
            } catch (Exception exception) {
                log.error("unhandled exception", exception);
                BusinessExceptionEnum.SYSTEM_ERROR_CUSTOM.assertFail(exception.toString());
            }
        }
    }

    public String signIn(String username, String password) {
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
        String userName = "";
        try {
            Response response = call.execute();
            code = response.code();
            headers = response.headers();
            body = response.body().string();
            userName = SignInParser.parse(body);
        } catch (Throwable e) {
            throwException(call, code, headers, body, e);
        }
        return userName;
    }

    public GalleryListParser.Result getGalleryList(String url) {
        String referer = EhUrl.getReferer();
        log.info("getGalleryList url:{}", url);
        Request request = new EhRequestBuilder(url, referer).build();
        Call call = okHttpClient.newCall(request);
        String body = null;
        Headers headers = null;
        GalleryListParser.Result result = new GalleryListParser.Result();
        int code = -1;
        try {
            Response response = call.execute();
            code = response.code();
            headers = response.headers();
            body = response.body().string();
            result = GalleryListParser.parse(body);
        } catch (Throwable e) {
            throwException(call, code, headers, body, e);
        }

        fillGalleryList(result.galleryInfoList, url, true);
        return result;
    }

    private void fillGalleryList(List<GalleryInfo> list, String url, boolean filter) {
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

    // At least, GalleryInfo contain valid gid and token
    public List<GalleryInfo> fillGalleryListByApi(List<GalleryInfo> galleryInfoList, String referer) {
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


    private void doFillGalleryListByApi(List<GalleryInfo> galleryInfoList, String referer) {
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
            throwException(call, code, headers, body, e);
        }
    }

    public GalleryDetail getGalleryDetail(String url) {
        String referer = EhUrl.getReferer();
        log.info("getGalleryDetail url:{}", url);
        Request request = new EhRequestBuilder(url, referer).build();
        Call call = okHttpClient.newCall(request);

        String body = null;
        Headers headers = null;
        int code = -1;
        GalleryDetail galleryDetail = null;
        try {
            Response response = call.execute();
            code = response.code();
            headers = response.headers();
            body = response.body().string();
            String html = EventPaneParser.parse(body);
//            if (html != null) {
//                EhApplication.getInstance().showEventPane(html);
//            }
            galleryDetail = GalleryDetailParser.parse(body);
        } catch (Throwable e) {
            throwException(call, code, headers, body, e);
        }
        return galleryDetail;
    }

    public String getGalleryToken(long gid, String gtoken, int page) {
        JSONObject json = new JSONObject();
        json.put("method", "gtoken");
        JSONArray gidAndToken = new JSONArray();
        gidAndToken.add(gid);
        gidAndToken.add(gtoken);
        gidAndToken.add(page + 1);
        JSONArray outerArray = new JSONArray();
        outerArray.add(gidAndToken);
        json.put("pagelist", outerArray);
        log.info("request body:{}", json);
        final RequestBody requestBody = RequestBody.create(json.toString(), MEDIA_TYPE_JSON);
        String url = EhUrl.getApiUrl();
        String referer = EhUrl.getReferer();
        String origin = EhUrl.getOrigin();
        log.info("getGalleryToken url:{}", url);
        Request request = new EhRequestBuilder(url, referer, origin)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);

        String body = null;
        Headers headers = null;
        int code = -1;
        String res = "";
        try {
            Response response = call.execute();
            code = response.code();
            headers = response.headers();
            body = response.body().string();
            res = GalleryTokenApiParser.parse(body);
        } catch (Throwable e) {
            throwException(call, code, headers, body, e);
        }
        return res;
    }

    public GalleryPageParser.Result getGalleryPage(String url, long gid, String token) {
        String referer = EhUrl.getGalleryDetailUrl(gid, token);
        log.info("getGalleryPage url:{}", url);
        Request request = new EhRequestBuilder(url, referer).build();
        Call call = okHttpClient.newCall(request);


        String body = null;
        Headers headers = null;
        int code = -1;
        GalleryPageParser.Result result = new GalleryPageParser.Result();
        try {
            Response response = call.execute();
            code = response.code();
            headers = response.headers();
            body = response.body().string();
            result = GalleryPageParser.parse(body);
        } catch (Throwable e) {
            throwException(call, code, headers, body, e);
        }
        return result;
    }

    public GalleryPageApiParser.Result getGalleryPageApi(long gid, int index, String pToken, String showKey, String previousPToken) {
        final JSONObject json = new JSONObject();
        json.put("method", "showpage");
        json.put("gid", gid);
        json.put("page", index + 1);
        json.put("imgkey", pToken);
        json.put("showkey", showKey);
        final RequestBody requestBody = RequestBody.create(json.toString(), MEDIA_TYPE_JSON);
        String url = EhUrl.getApiUrl();
        String referer = null;
        if (index > 0 && previousPToken != null) {
            referer = EhUrl.getPageUrl(gid, index - 1, previousPToken);
        }
        String origin = EhUrl.getOrigin();
        log.info("getGalleryPageApi url:{}", url);
        Request request = new EhRequestBuilder(url, referer, origin)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);

        String body = null;
        Headers headers = null;
        int code = -1;
        GalleryPageApiParser.Result result = new GalleryPageApiParser.Result();
        try {
            Response response = call.execute();
            code = response.code();
            headers = response.headers();
            body = response.body().string();
            result = GalleryPageApiParser.parse(body);
        } catch (Throwable e) {
            throwException(call, code, headers, body, e);
        }
        return result;
    }
}