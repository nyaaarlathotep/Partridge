package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.UserCollectionLike;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 用户合集喜爱表 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-31
 */
public interface UserCollectionLikeService extends IService<UserCollectionLike> {

    /**
     * 查询用户合集喜爱表分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<UserCollectionLike> findListByPage(UserCollectionLike where,Integer page, Integer pageCount);

    /**
    * 查询用户合集喜爱表所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<UserCollectionLike> findList(UserCollectionLike where);


    /**
     * 添加用户合集喜爱表
     *
     * @param userCollectionLike 用户合集喜爱表
     * @return 影响条数
     */
    Integer add(UserCollectionLike userCollectionLike);

    /**
     * 删除用户合集喜爱表
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改用户合集喜爱表
     *
     * @param userCollectionLike 用户合集喜爱表
     * @return 影响条数
     */
    Integer updateData(UserCollectionLike userCollectionLike);

    /**
     * id查询数据
     *
     * @param id id
     * @return UserCollectionLike
     */
    UserCollectionLike findById(Integer id);
}
