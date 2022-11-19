package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.CollectionEleRe;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 合集元素关联表 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-11-01
 */
public interface CollectionEleReService extends IService<CollectionEleRe> {

    /**
     * 查询合集元素关联表分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<CollectionEleRe> findListByPage(CollectionEleRe where,Integer page, Integer pageCount);

    /**
    * 查询合集元素关联表所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<CollectionEleRe> findList(CollectionEleRe where);


    /**
     * 添加合集元素关联表
     *
     * @param collectionEleRe 合集元素关联表
     * @return 影响条数
     */
    Integer add(CollectionEleRe collectionEleRe);

    /**
     * 删除合集元素关联表
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改合集元素关联表
     *
     * @param collectionEleRe 合集元素关联表
     * @return 影响条数
     */
    Integer updateData(CollectionEleRe collectionEleRe);

    /**
     * id查询数据
     *
     * @param id id
     * @return CollectionEleRe
     */
    CollectionEleRe findById(Integer id);
}
