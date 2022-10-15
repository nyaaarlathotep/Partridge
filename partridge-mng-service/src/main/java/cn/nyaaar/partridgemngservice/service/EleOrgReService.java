package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.EleOrgRe;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-09
 */
public interface EleOrgReService extends IService<EleOrgRe> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<EleOrgRe> findListByPage(EleOrgRe where,Integer page, Integer pageCount);

    /**
    * 查询所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<EleOrgRe> findList(EleOrgRe where);


    /**
     * 添加
     *
     * @param eleOrgRe 
     * @return 影响条数
     */
    Integer add(EleOrgRe eleOrgRe);

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
     * @param eleOrgRe 
     * @return 影响条数
     */
    Integer updateData(EleOrgRe eleOrgRe);

    /**
     * id查询数据
     *
     * @param id id
     * @return EleOrgRe
     */
    EleOrgRe findById(Integer id);
}
