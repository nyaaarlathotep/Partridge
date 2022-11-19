package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.CollectionEleRe;
import cn.nyaaar.partridgemngservice.mapper.CollectionEleReMapper;
import cn.nyaaar.partridgemngservice.service.CollectionEleReService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 合集元素关联表 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-11-01
 */
@Service
public class CollectionEleReServiceImpl extends ServiceImpl<CollectionEleReMapper, CollectionEleRe> implements CollectionEleReService {

    @Override
    public QueryData<CollectionEleRe> findListByPage(CollectionEleRe where, Integer page, Integer pageCount){
        IPage<CollectionEleRe> wherePage = new Page<>(page, pageCount);

        IPage<CollectionEleRe> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<CollectionEleRe> findList(CollectionEleRe where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(CollectionEleRe collectionEleRe){
 
        return baseMapper.insert(collectionEleRe);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(CollectionEleRe collectionEleRe){
    
        return baseMapper.updateById(collectionEleRe);
    }

    @Override
    public CollectionEleRe findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
