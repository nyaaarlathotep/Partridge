package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.PrCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 合集表 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-11-01
 */
public interface PrCollectionService extends IService<PrCollection> {

    /**
     * 查询合集表分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<PrCollection> findListByPage(PrCollection where,Integer page, Integer pageCount);

    /**
    * 查询合集表所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<PrCollection> findList(PrCollection where);


    /**
     * 添加合集表
     *
     * @param prCollection 合集表
     * @return 影响条数
     */
    Integer add(PrCollection prCollection);

    /**
     * 删除合集表
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改合集表
     *
     * @param prCollection 合集表
     * @return 影响条数
     */
    Integer updateData(PrCollection prCollection);

    /**
     * id查询数据
     *
     * @param id id
     * @return PrCollection
     */
    PrCollection findById(Integer id);
}
