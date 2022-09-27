package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.PrUser;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-27
 */
public interface PrUserService extends IService<PrUser> {

    /**
     * 查询用户表分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<PrUser> findListByPage(PrUser where,Integer page, Integer pageCount);

    /**
    * 查询用户表所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<PrUser> findList(PrUser where);


    /**
     * 添加用户表
     *
     * @param prUser 用户表
     * @return 影响条数
     */
    Integer add(PrUser prUser);

    /**
     * 删除用户表
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改用户表
     *
     * @param prUser 用户表
     * @return 影响条数
     */
    Integer updateData(PrUser prUser);

    /**
     * id查询数据
     *
     * @param id id
     * @return PrUser
     */
    PrUser findById(Integer id);
}
