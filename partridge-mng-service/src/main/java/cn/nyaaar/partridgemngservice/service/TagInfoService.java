package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.TagInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;

import java.util.List;

/**
 * <p>
 * 元素关联的 tag，多种来源。 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-08
 */
public interface TagInfoService extends IService<TagInfo> {

    /**
     * 查询元素关联的 tag，多种来源。分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<TagInfo> findListByPage(TagInfo where, Integer page, Integer pageCount);

    /**
     * 查询元素关联的 tag，多种来源。所有数据
     *
     * @param where 查询条件
     * @return List
     */
    List<TagInfo> findList(TagInfo where);


    /**
     * 添加元素关联的 tag，多种来源。
     *
     * @param tagInfo 元素关联的 tag，多种来源。
     * @return 影响条数
     */
    Integer add(TagInfo tagInfo);

    /**
     * 删除元素关联的 tag，多种来源。
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改元素关联的 tag，多种来源。
     *
     * @param tagInfo 元素关联的 tag，多种来源。
     * @return 影响条数
     */
    Integer updateData(TagInfo tagInfo);

    /**
     * id查询数据
     *
     * @param id id
     * @return TagInfo
     */
    TagInfo findById(Integer id);


    /**
     * 获取 eleId 对应的所有 tagInfo
     *
     * @param eleId eleId
     * @return List<TagInfo>
     */
    List<TagInfo> getTagInfos(long eleId);

    /**
     * 保存或更新 tagInfo，并新增对应的关系
     *
     * @param galleryTags galleryTags
     * @param eleId       eleId
     */
    void saveOrUpdateTagInfoWithRe(List<TagInfo> galleryTags, Long eleId);
}
