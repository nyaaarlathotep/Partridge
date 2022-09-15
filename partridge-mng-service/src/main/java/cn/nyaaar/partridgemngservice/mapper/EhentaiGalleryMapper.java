package cn.nyaaar.partridgemngservice.mapper;

import cn.nyaaar.partridgemngservice.common.constants.RawSql;
import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-02
 */
public interface EhentaiGalleryMapper extends BaseMapper<EhentaiGallery> {

    String querySql = "SELECT DISTINCT e.* FROM ehentai_gallery e ";
    String wrapperSql = "<script>SELECT * FROM (" +
            querySql + RawSql.whereStart + RawSql.tagIdsSql + RawSql.whereEnd +
            ") as q  ${ew.customSqlSegment}</script>";

    @Select(wrapperSql)
    Page<EhentaiGallery> pageWithTag(Page<EhentaiGallery> page, @Param(Constants.WRAPPER) Wrapper<EhentaiGallery> wrapper,
                                     List<Integer> tagIds);

}
