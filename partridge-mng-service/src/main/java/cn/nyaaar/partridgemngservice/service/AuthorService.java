package cn.nyaaar.partridgemngservice.service;

import cn.nyaaar.partridgemngservice.entity.Author;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.nyaaar.partridgemngservice.entity.QueryData;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nyaaar
 * @since 2022-08-22
 */
public interface AuthorService extends IService<Author> {

    /**
     * 查询分页数据
     *
     * @param where     查询条件
     * @param page      页码
     * @param pageCount 每页条数
     * @return QueryData
     */
    QueryData<Author> findListByPage(Author where, Integer page, Integer pageCount);

    /**
     * 查询所有数据
     *
     * @param where 查询条件
     * @return List
     */
    List<Author> findList(Author where);


    /**
     * 添加
     *
     * @param author
     * @return 影响条数
     */
    Integer add(Author author);

    /**
     * 删除
     *
     * @param id 主键
     * @return 影响条数
     */
    Integer delete(Integer id);

    /**
     * 修改
     *
     * @param author
     * @return 影响条数
     */
    Integer updateData(Author author);

    /**
     * id查询数据
     *
     * @param id id
     * @return Author
     */
    Author findById(Integer id);
}
