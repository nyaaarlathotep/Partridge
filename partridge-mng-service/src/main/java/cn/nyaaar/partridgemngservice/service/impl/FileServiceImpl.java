package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.File;
import cn.nyaaar.partridgemngservice.mapper.FileMapper;
import cn.nyaaar.partridgemngservice.service.FileService;
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
 * @since 2022-09-01
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Override
    public QueryData<File> findListByPage(File where, Integer page, Integer pageCount){
        IPage<File> wherePage = new Page<>(page, pageCount);

        IPage<File> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<File> findList(File where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(File file){
 
        return baseMapper.insert(file);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(File file){
    
        return baseMapper.updateById(file);
    }

    @Override
    public File findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
