package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-03
 */
public interface TagService extends IService<Tag> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<Tag> findListByPage(Tag where,Integer page, Integer pageCount);

    /**
    * 查询所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<Tag> findList(Tag where);


    /**
     * 添加
     *
     * @param tag 
     * @return 影响条数
     */
    Integer add(Tag tag);

    /**
     * 删除
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改
     *
     * @param tag 
     * @return 影响条数
     */
    Integer updateData(Tag tag);

    /**
     * id查询数据
     *
     * @param id id
     * @return Tag
     */
    Tag findById(Integer id);
}
