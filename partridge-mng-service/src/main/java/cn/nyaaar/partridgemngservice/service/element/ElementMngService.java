package cn.nyaaar.partridgemngservice.service.element;

import cn.nyaaar.partridgemngservice.common.enums.EleOrgReTypeEnum;
import cn.nyaaar.partridgemngservice.entity.Actor;
import cn.nyaaar.partridgemngservice.entity.Organization;
import cn.nyaaar.partridgemngservice.entity.TagInfo;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;

import java.util.List;
import java.util.Optional;

/**
 * @author yuegenhua
 * @Version $Id: ElementService.java, v 0.1 2022-30 17:31 yuegenhua Exp $$
 */
public interface ElementMngService {

    void share(Long elementId);

    /**
     * 检查是否可以删除，删除文件相关，并释放配额
     *
     * @param eleId eleId
     */
    void delete(Long eleId);

    void like(Long elementId);

    void publishElement(Long elementId);

    boolean checkReadPermission(Long elementId);
    
    boolean checkWritePermission(Long elementId);

    /**
     * 获取当前用户正在上传，未上传完成的 elements
     *
     * @return List<CheckResp>
     */
    List<CheckResp> getUploadingElements();
    
    Optional<Organization> getEleOrgan(Long elementId, EleOrgReTypeEnum eleOrgReTypeEnum);
    List<Actor> getEleActors(Long elementId);


    /**
     * 获取 eleId 对应的所有 tagInfo
     *
     * @param eleId eleId
     * @return List<TagInfo>
     */
    List<TagInfo> getTagInfos(long eleId);
}