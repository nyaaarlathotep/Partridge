package cn.nyaaar.partridgemngservice.service.element;

/**
 * @author yuegenhua
 * @Version $Id: ElementService.java, v 0.1 2022-30 17:31 yuegenhua Exp $$
 */
public interface ElementMngService {

    void share(Integer elementId);

    /**
     * 检查是否可以删除，删除文件相关，并释放配额
     *
     * @param eleId eleId
     */
    void delete(Integer eleId);

    void like(Integer elementId);

    void publishElement(Integer elementId);
}