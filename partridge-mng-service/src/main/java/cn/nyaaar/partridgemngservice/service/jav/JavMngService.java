package cn.nyaaar.partridgemngservice.service.jav;

import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.model.jav.JavBasicInfo;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.jav.JavQuery;
import cn.nyaaar.partridgemngservice.model.jav.JavUploadReq;

import java.util.List;

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

    /**
     * 获取当前用户正在上传，未上传完成的 Jav
     *
     * @return List<CheckResp>
     */
    List<CheckResp> getUploadingJavs();
}
