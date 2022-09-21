package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.mapper.ElementMapper;
import cn.nyaaar.partridgemngservice.service.ElementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 基本元素表 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-16
 */
@Service
public class ElementServiceImpl extends ServiceImpl<ElementMapper, Element> implements ElementService {

    @Override
    public QueryData<Element> findListByPage(Element where, Integer page, Integer pageCount){
        IPage<Element> wherePage = new Page<>(page, pageCount);

        IPage<Element> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<Element> findList(Element where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(Element element){
 
        return baseMapper.insert(element);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(Element element){
    
        return baseMapper.updateById(element);
    }

    @Override
    public Element findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
