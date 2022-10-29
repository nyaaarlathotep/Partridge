package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.EleTorrent;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-21
 */
public interface EleTorrentService extends IService<EleTorrent> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<EleTorrent> findListByPage(EleTorrent where,Integer page, Integer pageCount);

    /**
    * 查询所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<EleTorrent> findList(EleTorrent where);


    /**
     * 添加
     *
     * @param eleTorrent 
     * @return 影响条数
     */
    Integer add(EleTorrent eleTorrent);

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
     * @param eleTorrent 
     * @return 影响条数
     */
    Integer updateData(EleTorrent eleTorrent);

    /**
     * id查询数据
     *
     * @param id id
     * @return EleTorrent
     */
    EleTorrent findById(Integer id);
}
