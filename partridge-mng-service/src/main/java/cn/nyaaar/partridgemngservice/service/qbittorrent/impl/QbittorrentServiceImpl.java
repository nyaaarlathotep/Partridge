package cn.nyaaar.partridgemngservice.service.qbittorrent.impl;

import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.qbittorrent.Torrent;
import cn.nyaaar.partridgemngservice.service.qbittorrent.QbittorrentService;
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
public class QbittorrentServiceImpl implements QbittorrentService {

    private final RestTemplate restTemplate;

    public QbittorrentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
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
            log.error("qbittorrent login error", e);
            BusinessExceptionEnum.QBITTORRENT_LOGIN_ERROR.assertFail();
        }
        return Collections.emptyList();
    }

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
            BusinessExceptionEnum.QBITTORRENT_LOGIN_ERROR.assertFail();
        }
        return sid;
    }
}