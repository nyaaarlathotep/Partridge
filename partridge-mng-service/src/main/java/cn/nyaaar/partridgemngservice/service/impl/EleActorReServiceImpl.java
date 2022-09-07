package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.EleActorRe;
import cn.nyaaar.partridgemngservice.mapper.EleActorReMapper;
import cn.nyaaar.partridgemngservice.service.EleActorReService;
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
 * @since 2022-09-07
 */
@Service
public class EleActorReServiceImpl extends ServiceImpl<EleActorReMapper, EleActorRe> implements EleActorReService {

    @Override
    public QueryData<EleActorRe> findListByPage(EleActorRe where, Integer page, Integer pageCount){
        IPage<EleActorRe> wherePage = new Page<>(page, pageCount);

        IPage<EleActorRe> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<EleActorRe> findList(EleActorRe where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(EleActorRe eleActorRe){
 
        return baseMapper.insert(eleActorRe);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(EleActorRe eleActorRe){
    
        return baseMapper.updateById(eleActorRe);
    }

    @Override
    public EleActorRe findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
