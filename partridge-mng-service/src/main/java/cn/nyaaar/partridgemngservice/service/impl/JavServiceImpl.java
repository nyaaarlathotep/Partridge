package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.Jav;
import cn.nyaaar.partridgemngservice.mapper.JavMapper;
import cn.nyaaar.partridgemngservice.service.JavService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;

import java.util.List;

/**
 * <p>
 * jav 基本信息 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-08
 */
@Service
public class JavServiceImpl extends ServiceImpl<JavMapper, Jav> implements JavService {

    @Override
    public QueryData<Jav> findListByPage(Jav where, Integer page, Integer pageCount) {
        IPage<Jav> wherePage = new Page<>(page, pageCount);

        IPage<Jav> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<Jav> findList(Jav where) {

        return baseMapper.selectList(Wrappers.query(where));
    }


    @Override
    public Integer add(Jav jav) {

        return baseMapper.insert(jav);
    }

    @Override
    public Integer delete(Integer id) {

        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(Jav jav) {

        return baseMapper.updateById(jav);
    }

    @Override
    public Jav findById(Integer id) {

        return baseMapper.selectById(id);
    }

    @Override
    public Page<Jav> pageWithTag(Page<Jav> page, Wrapper<Jav> wrapper,
                                 List<Integer> tagIds, List<Integer> actorIds, List<Integer> organIds) {
        return baseMapper.pageWithTag(page, wrapper, tagIds, actorIds, organIds);
    }
}
