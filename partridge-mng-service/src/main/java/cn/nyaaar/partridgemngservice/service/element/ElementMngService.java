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
    // TODO elementControl

    /**
     * 分享 element 对所有用户可见
     *
     * @param elementId elementId
     */
    void share(Long elementId);

    /**
     * 检查是否可以删除，删除文件相关，并释放配额
     *
     * @param eleId eleId
     */
    void delete(Long eleId);

    void like(Long elementId);

    /**
     * 公开 element，上传者释放对应存储空间，但将无法删除此 element
     *
     * @param elementId elementId
     */
    void publish(Long elementId);

    /**
     * 检查当前登录用户对于 element 是否有读权限
     *
     * @param elementId elementId
     * @return 是否有权限
     */
    boolean checkReadPermission(Long elementId);

    /**
     * 检查当前登录用户对于 element 是否有写权限
     *
     * @param elementId elementId
     * @return 是否有权限
     */
    boolean checkWritePermission(Long elementId);

    /**
     * 获取当前用户正在上传，未上传完成的 elements
     *
     * @return List<CheckResp>
     */
    List<CheckResp> getUploadingElements();

    /**
     * 获得 element 对应的特定类型的机构
     *
     * @param elementId        elementId
     * @param eleOrgReTypeEnum eleOrgReTypeEnum
     * @return Optional<Organization>
     */
    Optional<Organization> getEleOrgan(Long elementId, EleOrgReTypeEnum eleOrgReTypeEnum);

    /**
     * 获得 element 对应的演员
     *
     * @param elementId elementId
     * @return List<Actor>
     */
    List<Actor> getEleActors(Long elementId);


    /**
     * 获取 eleId 对应的所有 tagInfo
     *
     * @param eleId eleId
     * @return List<TagInfo>
     */
    List<TagInfo> getTagInfos(long eleId);
}