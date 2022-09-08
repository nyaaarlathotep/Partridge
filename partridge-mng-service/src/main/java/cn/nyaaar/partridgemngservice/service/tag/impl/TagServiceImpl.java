package cn.nyaaar.partridgemngservice.service.tag.impl;

import cn.nyaaar.partridgemngservice.entity.EleTagRe;
import cn.nyaaar.partridgemngservice.entity.TagInfo;
import cn.nyaaar.partridgemngservice.service.EleTagReService;
import cn.nyaaar.partridgemngservice.service.TagInfoService;
import cn.nyaaar.partridgemngservice.service.tag.TagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    private final EleTagReService eleTagReService;

    private final TagInfoService tagInfoService;

    public TagServiceImpl(EleTagReService eleTagReService,
                          TagInfoService tagInfoService) {
        this.eleTagReService = eleTagReService;
        this.tagInfoService = tagInfoService;
    }

    @Override
    public List<TagInfo> getTagInfos(long eleId) {
        List<EleTagRe> eleTagRes = eleTagReService.list(
                new LambdaQueryWrapper<EleTagRe>().eq(EleTagRe::getEleId, eleId));
        if (eleTagRes.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> tagIds = eleTagRes.stream().map(EleTagRe::getTagId).toList();
        return tagInfoService.list(new LambdaQueryWrapper<TagInfo>().in(TagInfo::getId, tagIds));
    }

    @Override
    public void saveOrUpdateTagInfo(List<TagInfo> galleryTags, Long eleId) {
        for (TagInfo tagInfo : galleryTags) {
            TagInfo oldTag = tagInfoService.getOne(
                    new LambdaQueryWrapper<TagInfo>()
                            .eq(TagInfo::getName, tagInfo.getName())
                            .eq(TagInfo::getGroupName, tagInfo.getGroupName())
                            .eq(TagInfo::getSource, tagInfo.getSource()));
            if (oldTag == null) {
                tagInfoService.add(tagInfo);
                EleTagRe eleTagRe = new EleTagRe()
                        .setEleId(eleId)
                        .setTagId(tagInfo.getId());
                eleTagReService.add(eleTagRe);
            } else {
                EleTagRe eleTagRe = new EleTagRe()
                        .setEleId(eleId)
                        .setTagId(oldTag.getId());
                eleTagReService.add(eleTagRe);
            }
        }
    }
}
