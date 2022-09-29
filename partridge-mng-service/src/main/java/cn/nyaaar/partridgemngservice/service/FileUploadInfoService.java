package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.FileUploadInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-29
 */
public interface FileUploadInfoService extends IService<FileUploadInfo> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<FileUploadInfo> findListByPage(FileUploadInfo where,Integer page, Integer pageCount);

    /**
    * 查询所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<FileUploadInfo> findList(FileUploadInfo where);


    /**
     * 添加
     *
     * @param fileUploadInfo 
     * @return 影响条数
     */
    Integer add(FileUploadInfo fileUploadInfo);

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
     * @param fileUploadInfo 
     * @return 影响条数
     */
    Integer updateData(FileUploadInfo fileUploadInfo);

    /**
     * id查询数据
     *
     * @param id id
     * @return FileUploadInfo
     */
    FileUploadInfo findById(Integer id);
}
