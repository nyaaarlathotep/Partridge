package cn.nyaaar.partridgemngservice.common.constants;

/**
 * @author nyaaar
 * @Version $Id: rawSql.java, v 0.1 2022-15 16:58 nyaaar Exp $$
 */
public class RawSql {

    public static final String whereStart = "<where>";
    public static final String whereEnd = "</where>";
    // e.ELE_ID, outer layer needs a specified element(like ehentai_gallery) e with ele_id
    public static final String tagIdsSql = "<if test= \"tagIds != null\">" +
            "<foreach item = 'tagId' collection = 'tagIds' index = 'index' open = '' separator = '' close = ''>" +
            " AND EXISTS( SELECT * FROM  tag_info t, ele_tag_re r  WHERE  e.ELE_ID=r.ELE_ID AND r.TAG_ID = #{tagId} )" +
            "</foreach>" +
            "</if>";
    // e.ELE_ID, outer layer needs a specified element(like ehentai_gallery) e with ele_id
    public static final String actorIds = "<if test= \"actorIds != null\">" +
            "<foreach item = 'actorId' collection = 'actorIds' index = 'index' open = '' separator = '' close = ''>" +
            " AND EXISTS( SELECT * FROM  actor t, ele_actor_re r  WHERE  e.ELE_ID=r.ELE_ID AND r.actor_id = #{actorId} )" +
            "</foreach>" +
            "</if>";
    // e.ELE_ID, outer layer needs a specified element(like ehentai_gallery) e with ele_id
    public static final String organIds = "<if test= \"organIds != null\">" +
            "<foreach item = 'organId' collection = 'organIds' index = 'index' open = '' separator = '' close = ''>" +
            " AND EXISTS( SELECT * FROM  organization t, ele_org_re r  WHERE  e.ELE_ID=r.ELE_ID AND r.ORG_ID = #{organId} )" +
            "</foreach>" +
            "</if>";
}