package cn.nyaaar.partridgemngservice.service.qbittorrent;

import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.qbittorrent.Torrent;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.urlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author yuegenhua
 * @Version $Id: QbittorrentServiceImpl.java, v 0.1 2022-10 9:49 yuegenhua Exp $$
 */
@Slf4j
@Service
public class QbittorrentEngine {

    private final RestTemplate restTemplate;

    public QbittorrentEngine(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Torrent> getTorrents() {
        String url = urlUtil.simpleConcatUrl(Settings.getQbittorrentUrl(), "torrents/info");
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, Collections.singletonList(sid()));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            return JSON.parseObject(res.getBody(), new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("qbittorrent getTorrents error", e);
            BusinessExceptionEnum.QBITTORRENT_ERROR.assertFail();
        }
        return Collections.emptyList();
    }

    public void addTorrent(String torrentUrl, String userName, Element element) {
        String url = urlUtil.simpleConcatUrl(Settings.getQbittorrentUrl(), "torrents/add");
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, Collections.singletonList(sid()));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("urls", torrentUrl);
        map.add("savepath", getDownloadDir(userName, element));
        map.add("paused", "false");
        map.add("contentLayout", "Original");
        map.add("autoTMM", "false");
        map.add("category", element.getType());
        map.add("tags", "username:" + userName);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        try {
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            if (res.getStatusCodeValue() != 200) {
                log.error("torrent: {}, res: {}", torrentUrl, res.getBody());
                BusinessExceptionEnum.COMMON_BUSINESS_ERROR.assertFail("torrent 无效");
            }
        } catch (Exception e) {
            log.error("qbittorrent addTorrent error", e);
            BusinessExceptionEnum.QBITTORRENT_ERROR.assertFail();
        }
    }

    // Add new torrent
    // urls: magnet:?xt=urn:btih:33316737bd9afec091f389d057ae5613c3aaf06e&tr=http%3a%2f%2ft.nyaatracker.com%2fannounce&tr=http%3a%2f%2ftracker.kamigami.org%3a2710%2fannounce&tr=http%3a%2f%2fshare.camoe.cn%3a8080%2fannounce&tr=http%3a%2f%2fopentracker.acgnx.se%2fannounce&tr=http%3a%2f%2fanidex.moe%3a6969%2fannounce&tr=http%3a%2f%2ft.acg.rip%3a6699%2fannounce&tr=https%3a%2f%2ftr.bangumi.moe%3a9696%2fannounce&tr=udp%3a%2f%2ftr.bangumi.moe%3a6969%2fannounce&tr=http%3a%2f%2fopen.acgtracker.com%3a1096%2fannounce&tr=udp%3a%2f%2ftracker.opentrackr.org%3a1337%2fannounce
    //autoTMM: false
    //savepath: /downloads/anime/孤独摇滚/S01
    //cookie:
    //rename:
    //category:
    //paused: false
    //contentLayout: Original
    //dlLimit: NaN
    //upLimit: NaN

    public String sid() {
        String url = urlUtil.simpleConcatUrl(Settings.getQbittorrentUrl(), "auth/login");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", Settings.getQbittorrentUser());
        map.add("password", Settings.getQbittorrentPassword());
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, null);
        String sid = null;
        try {
            ResponseEntity<String> res = restTemplate.postForEntity(url, httpEntity, String.class);
            String sidString = Objects.requireNonNull(res.getHeaders().get("set-cookie")).get(0);
            sid = sidString.substring(0, sidString.indexOf(";"));
        } catch (Exception e) {
            log.error("qbittorrent login error", e);
            BusinessExceptionEnum.QBITTORRENT_ERROR.assertFail();
        }
        return sid;
    }

    private static String getDownloadDir(String userName, Element element) {
        return FileUtil.simpleConcatPath(Settings.getDownloadRootPath(),
                userName, element.getType(), String.valueOf(element.getId()));
    }
}