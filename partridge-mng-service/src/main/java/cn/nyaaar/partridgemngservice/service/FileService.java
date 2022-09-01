package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.File;
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
public interface FileService extends IService<File> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<File> findListByPage(File where,Integer page, Integer pageCount);

    /**
    * 查询所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<File> findList(File where);


    /**
     * 添加
     *
     * @param file 
     * @return 影响条数
     */
    Integer add(File file);

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
     * @param file 
     * @return 影响条数
     */
    Integer updateData(File file);

    /**
     * id查询数据
     *
     * @param id id
     * @return File
     */
    File findById(Integer id);
}
