package cn.nyaaar.partridgemngservice.service.torrent.impl;

import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.torrent.QBitTorrent;
import cn.nyaaar.partridgemngservice.model.torrent.QBitTorrentContent;
import cn.nyaaar.partridgemngservice.util.StringUtils;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

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

    public List<QBitTorrent> getTorrents(String userName, String hash) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, Collections.singletonList(sid()));
        String url = urlUtil.simpleConcatUrl(Settings.getQbittorrentUrl(), "torrents/info");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (StringUtils.isNotEmpty(userName)) {
            builder.queryParam("tag", "username:" + userName);
        }
        if (StringUtils.isNotEmpty(hash)) {
            builder.queryParam("hashes", hash);
        }
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> res = restTemplate.exchange(builder.build().encode().toString(),
                    HttpMethod.GET, httpEntity, String.class);
            return JSON.parseObject(res.getBody(), new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("qbittorrent getTorrents error", e);
            BusinessExceptionEnum.QBITTORRENT_ERROR.assertFail();
        }
        return Collections.emptyList();
    }

    public List<QBitTorrentContent> getTorrentContents(String torrentHash) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, Collections.singletonList(sid()));
        String url = urlUtil.simpleConcatUrl(Settings.getQbittorrentUrl(), "torrents/files");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("hash", torrentHash);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> res = restTemplate.exchange(builder.build().encode().toString(),
                    HttpMethod.GET, httpEntity, String.class);
            return JSON.parseObject(res.getBody(), new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("qbittorrent getTorrentContents error", e);
            BusinessExceptionEnum.QBITTORRENT_ERROR.assertFail();
        }
        return Collections.emptyList();
    }

    public void setTorrentContentPriority(String torrentHash, Integer contentIndex, Integer priority) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, Collections.singletonList(sid()));
        String partialUrl = "torrents/filePrio";

        String url = urlUtil.simpleConcatUrl(Settings.getQbittorrentUrl(), partialUrl);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("hash", torrentHash);
        map.add("id", contentIndex);
        map.add("priority", priority);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
        try {
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            if (res.getStatusCodeValue() != 200) {
                log.error("torrent: {}, res: {}", torrentHash, res.getBody());
                BusinessExceptionEnum.COMMON_BUSINESS_ERROR.assertFail("更改内容优先级失败");
            }
        } catch (Exception e) {
            log.error("qbittorrent getTorrentContents error", e);
            BusinessExceptionEnum.QBITTORRENT_ERROR.assertFail();
        }
    }

    public void deleteTorrent(String torrentHash, boolean deleteFiles) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, Collections.singletonList(sid()));
        String partialUrl = "torrents/delete";

        String url = urlUtil.simpleConcatUrl(Settings.getQbittorrentUrl(), partialUrl);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("hashes", torrentHash);
        map.add("deleteFiles", String.valueOf(deleteFiles));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        try {
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            if (res.getStatusCodeValue() != 200) {
                log.error("torrent: {}, res: {}", torrentHash, res.getBody());
                BusinessExceptionEnum.COMMON_BUSINESS_ERROR.assertFail("删除 torrent 失败");
            }
        } catch (Exception e) {
            log.error("qbittorrent getTorrentContents error", e);
            BusinessExceptionEnum.QBITTORRENT_ERROR.assertFail();
        }
    }


    public void addTorrent(String torrentUrl, String userName, String savePath, String category) {
        String url = urlUtil.simpleConcatUrl(Settings.getQbittorrentUrl(), "torrents/add");
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, Collections.singletonList(sid()));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("urls", torrentUrl);
        map.add("savepath", savePath);
        map.add("paused", "false");
        map.add("contentLayout", "Original");
        map.add("autoTMM", "false");
        map.add("category", category);
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

    private String sid() {
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
}