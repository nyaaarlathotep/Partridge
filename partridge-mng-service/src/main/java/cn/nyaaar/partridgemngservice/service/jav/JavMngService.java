package cn.nyaaar.partridgemngservice.service.jav;

import cn.nyaaar.partridgemngservice.model.jav.JavBasicInfo;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.jav.JavQuery;

public interface JavMngService {

    /**
     * jav.code -> JavBasicInfo
     *
     * @param code code
     * @return JavBasicInfo
     */
    JavBasicInfo getJavBasicInfoByCode(String code);

    /**
     * jav.name like JavBasicInfo
     *
     * @param javQuery javQuery
     * @return JavBasicInfos
     */
    ListResp<JavBasicInfo> getJavList(JavQuery javQuery, int pageIndex);

    /**
     * 浏览Jav，返回一个 jav 的 ListResp
     *
     * @param pageIndex 当前浏览页码
     * @return ListResp<JavBasicInfo>
     */
    ListResp<JavBasicInfo> getJavList(int pageIndex);
}
