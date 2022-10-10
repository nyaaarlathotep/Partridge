package cn.nyaaar.partridgemngservice.service.qbittorrent.impl;

import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.service.qbittorrent.QbittorrentService;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    public void getJobs() {

    }

    public String sid() {
        String url = PathUtil.simpleConcatUrl(Settings.getQbittorrentUrl(), "auth/login");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "admin");
        map.add("password", "adminadmin");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, null);
        ResponseEntity<String> res = restTemplate.postForEntity(url, httpEntity, String.class);
        log.info(res.getBody());
        log.info(res.getHeaders().toString());
        String sidString = res.getHeaders().get("set-cookie").get(0);
        String sid = sidString.substring(0, sidString.indexOf(";"));
        return sid;
    }
}