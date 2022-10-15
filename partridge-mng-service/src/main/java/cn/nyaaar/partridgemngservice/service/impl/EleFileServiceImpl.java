package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.EleFile;
import cn.nyaaar.partridgemngservice.mapper.EleFileMapper;
import cn.nyaaar.partridgemngservice.service.EleFileService;
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
 * @since 2022-10-08
 */
@Service
public class EleFileServiceImpl extends ServiceImpl<EleFileMapper, EleFile> implements EleFileService {

    @Override
    public QueryData<EleFile> findListByPage(EleFile where, Integer page, Integer pageCount){
        IPage<EleFile> wherePage = new Page<>(page, pageCount);

        IPage<EleFile> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<EleFile> findList(EleFile where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(EleFile eleFile){
 
        return baseMapper.insert(eleFile);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(EleFile eleFile){
    
        return baseMapper.updateById(eleFile);
    }

    @Override
    public EleFile findById(Long id){
    
        return baseMapper.selectById(id);
    }
}
