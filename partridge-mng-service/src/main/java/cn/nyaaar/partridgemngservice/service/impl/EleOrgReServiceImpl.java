package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.EleOrgRe;
import cn.nyaaar.partridgemngservice.mapper.EleOrgReMapper;
import cn.nyaaar.partridgemngservice.service.EleOrgReService;
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
public class EleOrgReServiceImpl extends ServiceImpl<EleOrgReMapper, EleOrgRe> implements EleOrgReService {

    @Override
    public QueryData<EleOrgRe> findListByPage(EleOrgRe where, Integer page, Integer pageCount){
        IPage<EleOrgRe> wherePage = new Page<>(page, pageCount);

        IPage<EleOrgRe> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<EleOrgRe> findList(EleOrgRe where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(EleOrgRe eleOrgRe){
 
        return baseMapper.insert(eleOrgRe);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(EleOrgRe eleOrgRe){
    
        return baseMapper.updateById(eleOrgRe);
    }

    @Override
    public EleOrgRe findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
