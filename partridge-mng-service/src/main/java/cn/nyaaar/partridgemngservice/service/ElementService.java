package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.Element;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * 基本元素表 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-08
 */
public interface ElementService extends IService<Element> {

    /**
     * 查询基本元素表分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<Element> findListByPage(Element where,Integer page, Integer pageCount);

    /**
    * 查询基本元素表所有数据
    *
    * @param where     查询条件
    * @return List
    */
    List<Element> findList(Element where);


    /**
     * 添加基本元素表
     *
     * @param element 基本元素表
     * @return 影响条数
     */
    Integer add(Element element);

    /**
     * 删除基本元素表
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改基本元素表
     *
     * @param element 基本元素表
     * @return 影响条数
     */
    Integer updateData(Element element);

    /**
     * id查询数据
     *
     * @param id id
     * @return Element
     */
    Element findById(Integer id);
}
