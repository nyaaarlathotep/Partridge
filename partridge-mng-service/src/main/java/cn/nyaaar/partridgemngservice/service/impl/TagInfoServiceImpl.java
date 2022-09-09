package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.TagInfo;
import cn.nyaaar.partridgemngservice.mapper.TagInfoMapper;
import cn.nyaaar.partridgemngservice.service.TagInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 元素关联的 tag，多种来源。 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-08
 */
@Service
public class TagInfoServiceImpl extends ServiceImpl<TagInfoMapper, TagInfo> implements TagInfoService {

    @Override
    public QueryData<TagInfo> findListByPage(TagInfo where, Integer page, Integer pageCount){
        IPage<TagInfo> wherePage = new Page<>(page, pageCount);

        IPage<TagInfo> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<TagInfo> findList(TagInfo where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(TagInfo tagInfo){
 
        return baseMapper.insert(tagInfo);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(TagInfo tagInfo){
    
        return baseMapper.updateById(tagInfo);
    }

    @Override
    public TagInfo findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
