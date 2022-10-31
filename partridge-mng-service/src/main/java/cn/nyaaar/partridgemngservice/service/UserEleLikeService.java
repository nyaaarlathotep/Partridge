package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.UserEleLike;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 用户元素喜爱表 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-31
 */
public interface UserEleLikeService extends IService<UserEleLike> {

    /**
     * 查询用户元素喜爱表分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<UserEleLike> findListByPage(UserEleLike where,Integer page, Integer pageCount);

    /**
    * 查询用户元素喜爱表所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<UserEleLike> findList(UserEleLike where);


    /**
     * 添加用户元素喜爱表
     *
     * @param userEleLike 用户元素喜爱表
     * @return 影响条数
     */
    Integer add(UserEleLike userEleLike);

    /**
     * 删除用户元素喜爱表
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改用户元素喜爱表
     *
     * @param userEleLike 用户元素喜爱表
     * @return 影响条数
     */
    Integer updateData(UserEleLike userEleLike);

    /**
     * id查询数据
     *
     * @param id id
     * @return UserEleLike
     */
    UserEleLike findById(Integer id);
}
