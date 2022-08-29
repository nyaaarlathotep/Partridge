package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.Jav;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-08-22
 */
public interface JavService extends IService<Jav> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<Jav> findListByPage(Jav where, Integer page, Integer pageCount);

    /**
     * 查询所有数据
     *
     * @param where 查询条件
     * @return List
     */
    List<Jav> findList(Jav where);


    /**
     * 添加
     *
     * @param jav
     * @return 影响条数
     */
    Integer add(Jav jav);

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
     * @param jav
     * @return 影响条数
     */
    Integer updateData(Jav jav);

    /**
     * id查询数据
     *
     * @param id id
     * @return Jav
     */
    Jav findById(Integer id);
}
