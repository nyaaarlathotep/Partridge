package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.Author;
import cn.nyaaar.partridgemngservice.mapper.AuthorMapper;
import cn.nyaaar.partridgemngservice.service.AuthorService;
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
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper, Author> implements AuthorService {

    @Override
    public QueryData<Author> findListByPage(Author where, Integer page, Integer pageCount) {
        IPage<Author> wherePage = new Page<>(page, pageCount);

        IPage<Author> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<Author> findList(Author where) {

        return baseMapper.selectList(Wrappers.query(where));
    }


    @Override
    public Integer add(Author author) {

        return baseMapper.insert(author);
    }

    @Override
    public Integer delete(Integer id) {

        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(Author author) {

        return baseMapper.updateById(author);
    }

    @Override
    public Author findById(Integer id) {

        return baseMapper.selectById(id);
    }
}
