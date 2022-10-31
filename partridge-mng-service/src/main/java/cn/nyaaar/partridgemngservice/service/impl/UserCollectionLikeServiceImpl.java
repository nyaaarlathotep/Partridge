package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.UserCollectionLike;
import cn.nyaaar.partridgemngservice.mapper.UserCollectionLikeMapper;
import cn.nyaaar.partridgemngservice.service.UserCollectionLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 用户合集喜爱表 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-31
 */
@Service
public class UserCollectionLikeServiceImpl extends ServiceImpl<UserCollectionLikeMapper, UserCollectionLike> implements UserCollectionLikeService {

    @Override
    public QueryData<UserCollectionLike> findListByPage(UserCollectionLike where, Integer page, Integer pageCount){
        IPage<UserCollectionLike> wherePage = new Page<>(page, pageCount);

        IPage<UserCollectionLike> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<UserCollectionLike> findList(UserCollectionLike where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(UserCollectionLike userCollectionLike){
 
        return baseMapper.insert(userCollectionLike);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(UserCollectionLike userCollectionLike){
    
        return baseMapper.updateById(userCollectionLike);
    }

    @Override
    public UserCollectionLike findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
