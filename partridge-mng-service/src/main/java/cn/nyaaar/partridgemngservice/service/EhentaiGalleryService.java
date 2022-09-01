package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-01
 */
public interface EhentaiGalleryService extends IService<EhentaiGallery> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<EhentaiGallery> findListByPage(EhentaiGallery where,Integer page, Integer pageCount);

    /**
    * 查询所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<EhentaiGallery> findList(EhentaiGallery where);


    /**
     * 添加
     *
     * @param ehentaiGallery 
     * @return 影响条数
     */
    Integer add(EhentaiGallery ehentaiGallery);

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
     * @param ehentaiGallery 
     * @return 影响条数
     */
    Integer updateData(EhentaiGallery ehentaiGallery);

    /**
     * id查询数据
     *
     * @param id id
     * @return EhentaiGallery
     */
    EhentaiGallery findById(Integer id);
}
