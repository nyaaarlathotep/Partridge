package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.EleFile;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-08
 */
public interface EleFileService extends IService<EleFile> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<EleFile> findListByPage(EleFile where,Integer page, Integer pageCount);

    /**
    * 查询所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<EleFile> findList(EleFile where);


    /**
     * 添加
     *
     * @param eleFile 
     * @return 影响条数
     */
    Integer add(EleFile eleFile);

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
     * @param eleFile 
     * @return 影响条数
     */
    Integer updateData(EleFile eleFile);

    /**
     * id查询数据
     *
     * @param id id
     * @return EleFile
     */
    EleFile findById(Long id);
}
