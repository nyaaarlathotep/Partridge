package cn.nyaaar.partridgemngservice.service.video.jav;

import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.model.jav.JavBasicInfo;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.jav.JavQuery;
import cn.nyaaar.partridgemngservice.model.jav.JavUploadReq;


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
     * 上传 Jav 的提前准备
     *
     * @param javUploadReq javUploadReq
     * @return CheckResp
     */
    CheckResp uploadJav(JavUploadReq javUploadReq);

}
