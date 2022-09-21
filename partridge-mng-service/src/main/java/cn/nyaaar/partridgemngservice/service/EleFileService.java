package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.EleFile;
import cn.nyaaar.partridgemngservice.model.eh.GalleryPage;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-02
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
    QueryData<EleFile> findListByPage(EleFile where, Integer page, Integer pageCount);

    /**
     * 查询所有数据
     *
     * @param where 查询条件
     * @return List
     */
    List<EleFile> findList(EleFile where);


    /**
     * 添加
     *
     * @param eleFile eleFile
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
     * @param eleFile eleFile
     * @return 影响条数
     */
    Integer updateData(EleFile eleFile);

    /**
     * id查询数据
     *
     * @param id id
     * @return EleFile
     */
    EleFile findById(Integer id);

    /**
     * 通过 eleFile 获得 GalleryPage
     *
     * @param eleFile eleFile
     * @return GalleryPage`
     */
    GalleryPage getGalleryPage(EleFile eleFile);

    /**
     * 保存文件字节至目标位置
     *
     * @param bytes      bytes
     * @param destDic    destDic，不带fileName
     * @param fileName   fileName
     * @param reDownload 如果文件存在是否重新下载
     * @throws IOException IOException
     */
    void saveBytesToFile(byte[] bytes, String destDic, String fileName, boolean reDownload) throws IOException;
}
