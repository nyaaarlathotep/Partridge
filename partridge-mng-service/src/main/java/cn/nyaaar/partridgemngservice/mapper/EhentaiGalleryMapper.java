package cn.nyaaar.partridgemngservice.mapper;

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

    String querySql = "SELECT DISTINCT g.* FROM ehentai_gallery g WHERE" +
            "<foreach item = 'tagId' collection = 'tagIds' index = 'index' open = '' separator = 'and' close = ''>" +
            "EXISTS( SELECT * FROM  tag_info t, ele_tag_re r  WHERE  g.ELE_ID=r.ELE_ID AND r.TAG_ID = #{tagId} )" +
            "</foreach>";
    String wrapperSql = "<script>SELECT * FROM (" + querySql + ") as q  ${ew.customSqlSegment}</script>";

    @Select(wrapperSql)
    Page<EhentaiGallery> pageWithTag(Page<EhentaiGallery> page, @Param(Constants.WRAPPER) Wrapper<EhentaiGallery> wrapper,
                                     List<Integer> tagIds);

}
