/*
 * Copyright 2016 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.nyaaar.partridgemngservice.model.eh;


import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;

import java.util.ArrayList;

public class GalleryDetail extends GalleryInfo {

    public long apiUid = -1L;
    public String apiKey;
    public int torrentCount;
    public String torrentUrl;
    public String archiveUrl;
    public String parent;
    public ArrayList<GalleryInfo> newerVersions = new ArrayList<>();
    public String visible;
    public String language;
    public String size;
    public int pages;
    public int favoriteCount;
    public boolean isFavorited;
    public int ratingCount;
    public GalleryTagGroup[] tags;
    public GalleryCommentList comments;
    public int previewPages;
//    public PreviewSet previewSet;

    public GalleryDetail() {
    }

    public EhentaiGallery transToEntity() {
        EhentaiGallery ehentaiGallery = new EhentaiGallery();
        ehentaiGallery.setGid(this.gid);
        ehentaiGallery.setTitle(this.title);
        ehentaiGallery.setTitleJpn(this.titleJpn);
        ehentaiGallery.setCategory(this.category);
        ehentaiGallery.setUploader(this.uploader);
        ehentaiGallery.setRating(String.valueOf(this.rating));
        ehentaiGallery.setRatingCount(this.ratingCount);
        ehentaiGallery.setPages(this.pages);
        ehentaiGallery.setToken(this.token);
        ehentaiGallery.setPosted(DateUtil.parse(this.posted));
        ehentaiGallery.setFavoriteCount(this.favoriteCount);
        return ehentaiGallery;
    }
}
