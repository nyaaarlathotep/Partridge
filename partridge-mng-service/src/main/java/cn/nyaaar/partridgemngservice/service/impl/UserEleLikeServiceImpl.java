package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.UserEleLike;
import cn.nyaaar.partridgemngservice.mapper.UserEleLikeMapper;
import cn.nyaaar.partridgemngservice.service.UserEleLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 用户元素喜爱表 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-31
 */
@Service
public class UserEleLikeServiceImpl extends ServiceImpl<UserEleLikeMapper, UserEleLike> implements UserEleLikeService {

    @Override
    public QueryData<UserEleLike> findListByPage(UserEleLike where, Integer page, Integer pageCount){
        IPage<UserEleLike> wherePage = new Page<>(page, pageCount);

        IPage<UserEleLike> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<UserEleLike> findList(UserEleLike where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(UserEleLike userEleLike){
 
        return baseMapper.insert(userEleLike);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(UserEleLike userEleLike){
    
        return baseMapper.updateById(userEleLike);
    }

    @Override
    public UserEleLike findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
