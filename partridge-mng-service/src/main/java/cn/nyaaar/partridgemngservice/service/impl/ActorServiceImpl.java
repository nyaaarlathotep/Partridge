package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.Actor;
import cn.nyaaar.partridgemngservice.mapper.ActorMapper;
import cn.nyaaar.partridgemngservice.service.ActorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.entity.QueryData;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-08-22
 */
@Service
public class ActorServiceImpl extends ServiceImpl<ActorMapper, Actor> implements ActorService {

    @Override
    public QueryData<Actor> findListByPage(Actor where, Integer page, Integer pageCount) {
        IPage<Actor> wherePage = new Page<>(page, pageCount);

        IPage<Actor> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<Actor> findList(Actor where) {

        return baseMapper.selectList(Wrappers.query(where));
    }


    @Override
    public Integer add(Actor actor) {

        return baseMapper.insert(actor);
    }

    @Override
    public Integer delete(Integer id) {

        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(Actor actor) {

        return baseMapper.updateById(actor);
    }

    @Override
    public Actor findById(Integer id) {

        return baseMapper.selectById(id);
    }
}
