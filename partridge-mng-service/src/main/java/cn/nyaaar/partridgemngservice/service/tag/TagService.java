package cn.nyaaar.partridgemngservice.service.tag;

import cn.nyaaar.partridgemngservice.entity.TagInfo;

import java.util.List;

public interface TagService {

    List<TagInfo> getTagInfos(long eleId);

    void saveOrUpdateTagInfo(List<TagInfo> galleryTags, Long eleId);
}
