package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.Actor;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.entity.QueryData;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-08-22
 */
public interface ActorService extends IService<Actor> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<Actor> findListByPage(Actor where, Integer page, Integer pageCount);

    /**
     * 查询所有数据
     *
     * @param where 查询条件
     * @return List
     */
    List<Actor> findList(Actor where);


    /**
     * 添加
     *
     * @param actor
     * @return 影响条数
     */
    Integer add(Actor actor);

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
     * @param actor
     * @return 影响条数
     */
    Integer updateData(Actor actor);

    /**
     * id查询数据
     *
     * @param id id
     * @return Actor
     */
    Actor findById(Integer id);
}
