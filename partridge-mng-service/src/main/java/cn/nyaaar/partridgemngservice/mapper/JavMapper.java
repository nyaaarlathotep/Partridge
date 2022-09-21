package cn.nyaaar.partridgemngservice.mapper;

import cn.nyaaar.partridgemngservice.common.constants.RawSql;
import cn.nyaaar.partridgemngservice.entity.Jav;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * jav 基本信息 Mapper 接口
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-08
 */
public interface JavMapper extends BaseMapper<Jav> {

    String querySql = "SELECT DISTINCT e.* FROM jav e ";

    String wrapperSql = "<script>SELECT * FROM (" +
            querySql + RawSql.whereStart + RawSql.tagIdsSql + RawSql.actorIds + RawSql.organIds + RawSql.whereEnd +
            ") as q  ${ew.customSqlSegment}</script>";

    @Select(wrapperSql)
    Page<Jav> pageWithTag(Page<Jav> page, @Param(Constants.WRAPPER) Wrapper<Jav> wrapper,
                          List<Integer> tagIds, List<Integer> actorIds, List<Integer> organIds);

}
