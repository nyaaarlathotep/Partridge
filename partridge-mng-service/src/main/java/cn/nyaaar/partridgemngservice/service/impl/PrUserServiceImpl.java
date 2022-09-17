package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.PrUser;
import cn.nyaaar.partridgemngservice.mapper.PrUserMapper;
import cn.nyaaar.partridgemngservice.service.PrUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-17
 */
@Service
public class PrUserServiceImpl extends ServiceImpl<PrUserMapper, PrUser> implements PrUserService {

    @Override
    public QueryData<PrUser> findListByPage(PrUser where, Integer page, Integer pageCount){
        IPage<PrUser> wherePage = new Page<>(page, pageCount);

        IPage<PrUser> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<PrUser> findList(PrUser where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(PrUser prUser){
 
        return baseMapper.insert(prUser);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(PrUser prUser){
    
        return baseMapper.updateById(prUser);
    }

    @Override
    public PrUser findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
