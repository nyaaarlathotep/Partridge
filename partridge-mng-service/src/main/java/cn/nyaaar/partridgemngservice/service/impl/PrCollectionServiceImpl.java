package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.PrCollection;
import cn.nyaaar.partridgemngservice.mapper.PrCollectionMapper;
import cn.nyaaar.partridgemngservice.service.PrCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 合集表 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-11-01
 */
@Service
public class PrCollectionServiceImpl extends ServiceImpl<PrCollectionMapper, PrCollection> implements PrCollectionService {

    @Override
    public QueryData<PrCollection> findListByPage(PrCollection where, Integer page, Integer pageCount){
        IPage<PrCollection> wherePage = new Page<>(page, pageCount);

        IPage<PrCollection> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<PrCollection> findList(PrCollection where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(PrCollection prCollection){
 
        return baseMapper.insert(prCollection);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(PrCollection prCollection){
    
        return baseMapper.updateById(prCollection);
    }

    @Override
    public PrCollection findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
