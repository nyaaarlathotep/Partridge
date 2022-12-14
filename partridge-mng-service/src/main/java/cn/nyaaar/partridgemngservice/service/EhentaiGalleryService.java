package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * ehentai 画廊 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-26
 */
public interface EhentaiGalleryService extends IService<EhentaiGallery> {

    /**
     * 查询ehentai 画廊分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<EhentaiGallery> findListByPage(EhentaiGallery where,Integer page, Integer pageCount);

    /**
    * 查询ehentai 画廊所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<EhentaiGallery> findList(EhentaiGallery where);


    /**
     * 添加ehentai 画廊
     *
     * @param ehentaiGallery ehentai 画廊
     * @return 影响条数
     */
    Integer add(EhentaiGallery ehentaiGallery);

    /**
     * 删除ehentai 画廊
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改ehentai 画廊
     *
     * @param ehentaiGallery ehentai 画廊
     * @return 影响条数
     */
    Integer updateData(EhentaiGallery ehentaiGallery);

    /**
     * id查询数据
     *
     * @param id id
     * @return EhentaiGallery
     */
    EhentaiGallery findById(Long id);

    /**
     * 通过 tagIds 和 wrapper 查询 EhentaiGallery
     *
     * @param page    page
     * @param wrapper wrapper
     * @param tagIds  tagIds
     * @return EhentaiGallery page
     */
    Page<EhentaiGallery> pageWithTag(Page<EhentaiGallery> page, Wrapper<EhentaiGallery> wrapper,
                                     List<Integer> tagIds);
}
