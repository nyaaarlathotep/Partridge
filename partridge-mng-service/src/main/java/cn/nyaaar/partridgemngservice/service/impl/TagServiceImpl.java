package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.Tag;
import cn.nyaaar.partridgemngservice.mapper.TagMapper;
import cn.nyaaar.partridgemngservice.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;

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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public QueryData<Tag> findListByPage(Tag where, Integer page, Integer pageCount) {
        IPage<Tag> wherePage = new Page<>(page, pageCount);

        IPage<Tag> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<Tag> findList(Tag where) {

        return baseMapper.selectList(Wrappers.query(where));
    }


    @Override
    public Integer add(Tag tag) {

        return baseMapper.insert(tag);
    }

    @Override
    public Integer delete(Integer id) {

        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(Tag tag) {

        return baseMapper.updateById(tag);
    }

    @Override
    public Tag findById(Integer id) {

        return baseMapper.selectById(id);
    }
}
