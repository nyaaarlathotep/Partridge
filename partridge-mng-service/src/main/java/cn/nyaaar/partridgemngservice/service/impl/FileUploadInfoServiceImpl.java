package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.FileUploadInfo;
import cn.nyaaar.partridgemngservice.mapper.FileUploadInfoMapper;
import cn.nyaaar.partridgemngservice.service.FileUploadInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-29
 */
@Service
public class FileUploadInfoServiceImpl extends ServiceImpl<FileUploadInfoMapper, FileUploadInfo> implements FileUploadInfoService {

    @Override
    public QueryData<FileUploadInfo> findListByPage(FileUploadInfo where, Integer page, Integer pageCount){
        IPage<FileUploadInfo> wherePage = new Page<>(page, pageCount);

        IPage<FileUploadInfo> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<FileUploadInfo> findList(FileUploadInfo where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(FileUploadInfo fileUploadInfo){
 
        return baseMapper.insert(fileUploadInfo);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(FileUploadInfo fileUploadInfo){
    
        return baseMapper.updateById(fileUploadInfo);
    }

    @Override
    public FileUploadInfo findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
