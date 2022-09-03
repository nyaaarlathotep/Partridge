package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.EleTagRe;
import cn.nyaaar.partridgemngservice.mapper.EleTagReMapper;
import cn.nyaaar.partridgemngservice.service.EleTagReService;
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
 * @since 2022-09-03
 */
@Service
public class EleTagReServiceImpl extends ServiceImpl<EleTagReMapper, EleTagRe> implements EleTagReService {

    @Override
    public QueryData<EleTagRe> findListByPage(EleTagRe where, Integer page, Integer pageCount){
        IPage<EleTagRe> wherePage = new Page<>(page, pageCount);

        IPage<EleTagRe> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<EleTagRe> findList(EleTagRe where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(EleTagRe eleTagRe){
 
        return baseMapper.insert(eleTagRe);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(EleTagRe eleTagRe){
    
        return baseMapper.updateById(eleTagRe);
    }

    @Override
    public EleTagRe findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
