package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.Jav;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;

import java.util.List;

/**
 * <p>
 * jav 基本信息 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-08
 */
public interface JavService extends IService<Jav> {

    /**
     * 查询jav 基本信息分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<Jav> findListByPage(Jav where, Integer page, Integer pageCount);

    /**
     * 查询jav 基本信息所有数据
     *
     * @param where 查询条件
     * @return List
     */
    List<Jav> findList(Jav where);


    /**
     * 添加jav 基本信息
     *
     * @param jav jav 基本信息
     * @return 影响条数
     */
    Integer add(Jav jav);

    /**
     * 删除jav 基本信息
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改jav 基本信息
     *
     * @param jav jav 基本信息
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

    /**
     * 通过 tagIds, actorIds, organIds 和 wrapper 查询 EhentaiGallery
     *
     * @param page     page
     * @param wrapper  wrapper
     * @param tagIds   tagIds
     * @param actorIds actorIds
     * @param organIds organIds
     * @return Jav page
     */
    Page<Jav> pageWithTag(Page<Jav> page, Wrapper<Jav> wrapper, List<Integer> tagIds, List<Integer> actorIds, List<Integer> organIds);
}
