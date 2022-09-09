package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.Organization;
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
public interface OrganizationService extends IService<Organization> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<Organization> findListByPage(Organization where, Integer page, Integer pageCount);

    /**
     * 查询所有数据
     *
     * @param where 查询条件
     * @return List
     */
    List<Organization> findList(Organization where);


    /**
     * 添加
     *
     * @param organization
     * @return 影响条数
     */
    Integer add(Organization organization);

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
     * @param organization
     * @return 影响条数
     */
    Integer updateData(Organization organization);

    /**
     * id查询数据
     *
     * @param id id
     * @return Organization
     */
    Organization findById(Integer id);
}
