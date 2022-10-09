package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.Organization;
import cn.nyaaar.partridgemngservice.mapper.OrganizationMapper;
import cn.nyaaar.partridgemngservice.service.OrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 组织表 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-09
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    @Override
    public QueryData<Organization> findListByPage(Organization where, Integer page, Integer pageCount){
        IPage<Organization> wherePage = new Page<>(page, pageCount);

        IPage<Organization> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<Organization> findList(Organization where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(Organization organization){
 
        return baseMapper.insert(organization);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(Organization organization){
    
        return baseMapper.updateById(organization);
    }

    @Override
    public Organization findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
