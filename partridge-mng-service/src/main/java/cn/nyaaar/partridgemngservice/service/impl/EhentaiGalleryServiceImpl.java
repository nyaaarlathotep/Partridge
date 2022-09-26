package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import cn.nyaaar.partridgemngservice.mapper.EhentaiGalleryMapper;
import cn.nyaaar.partridgemngservice.service.EhentaiGalleryService;
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
 * ehentai 画廊 服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-26
 */
@Service
public class EhentaiGalleryServiceImpl extends ServiceImpl<EhentaiGalleryMapper, EhentaiGallery> implements EhentaiGalleryService {

    @Override
    public QueryData<EhentaiGallery> findListByPage(EhentaiGallery where, Integer page, Integer pageCount){
        IPage<EhentaiGallery> wherePage = new Page<>(page, pageCount);

        IPage<EhentaiGallery> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<EhentaiGallery> findList(EhentaiGallery where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(EhentaiGallery ehentaiGallery){
 
        return baseMapper.insert(ehentaiGallery);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(EhentaiGallery ehentaiGallery){
    
        return baseMapper.updateById(ehentaiGallery);
    }

    @Override
    public EhentaiGallery findById(Long id){
    
        return baseMapper.selectById(id);
    }

    @Override
    public Page<EhentaiGallery> pageWithTag(Page<EhentaiGallery> page, Wrapper<EhentaiGallery> wrapper, List<Integer> tagIds) {
        return baseMapper.pageWithTag(page, wrapper, tagIds);
    }
}
