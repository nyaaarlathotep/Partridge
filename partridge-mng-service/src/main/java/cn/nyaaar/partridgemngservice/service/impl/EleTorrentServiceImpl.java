package cn.nyaaar.partridgemngservice.service.impl;

import cn.nyaaar.partridgemngservice.entity.EleTorrent;
import cn.nyaaar.partridgemngservice.mapper.EleTorrentMapper;
import cn.nyaaar.partridgemngservice.service.EleTorrentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-21
 */
@Service
public class EleTorrentServiceImpl extends ServiceImpl<EleTorrentMapper, EleTorrent> implements EleTorrentService {

    @Override
    public QueryData<EleTorrent> findListByPage(EleTorrent where, Integer page, Integer pageCount){
        IPage<EleTorrent> wherePage = new Page<>(page, pageCount);

        IPage<EleTorrent> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<EleTorrent> findList(EleTorrent where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(EleTorrent eleTorrent){
 
        return baseMapper.insert(eleTorrent);
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(EleTorrent eleTorrent){
    
        return baseMapper.updateById(eleTorrent);
    }

    @Override
    public EleTorrent findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
