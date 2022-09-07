package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.EleActorRe;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-07
 */
public interface EleActorReService extends IService<EleActorRe> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<EleActorRe> findListByPage(EleActorRe where,Integer page, Integer pageCount);

    /**
    * 查询所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<EleActorRe> findList(EleActorRe where);


    /**
     * 添加
     *
     * @param eleActorRe 
     * @return 影响条数
     */
    Integer add(EleActorRe eleActorRe);

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
     * @param eleActorRe 
     * @return 影响条数
     */
    Integer updateData(EleActorRe eleActorRe);

    /**
     * id查询数据
     *
     * @param id id
     * @return EleActorRe
     */
    EleActorRe findById(Integer id);
}
