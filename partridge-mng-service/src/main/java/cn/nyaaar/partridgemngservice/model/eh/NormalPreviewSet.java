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


import java.util.ArrayList;
import java.util.List;

public class NormalPreviewSet extends PreviewSet {

    private final List<Integer> mPositionList = new ArrayList<>();
    private final List<String> mImageKeyList = new ArrayList<>();
    private final List<String> mImageUrlList = new ArrayList<>();
    private final List<Integer> mOffsetXList = new ArrayList<>();
    private final List<Integer> mOffsetYList = new ArrayList<>();
    private final List<Integer> mClipWidthList = new ArrayList<>();
    private final List<Integer> mClipHeightList = new ArrayList<>();
    private final List<String> mPageUrlList = new ArrayList<>();

    public NormalPreviewSet() {
    }


    private String getImageKey(String imageUrl) {
        int index = imageUrl.indexOf('/');
        if (index >= 0) {
            return imageUrl.substring(index + 1);
        } else {
            return imageUrl;
        }
    }

    public void addItem(int position, String imageUrl, int xOffset, int yOffset, int width,
                        int height, String pageUrl) {
        mPositionList.add(position);
        mImageKeyList.add(getImageKey(imageUrl));
        mImageUrlList.add(imageUrl);
        mOffsetXList.add(xOffset);
        mOffsetYList.add(yOffset);
        mClipWidthList.add(width);
        mClipHeightList.add(height);
        mPageUrlList.add(pageUrl);
    }

    @Override
    public int size() {
        return mPositionList.size();
    }

    @Override
    public int getPosition(int index) {
        return mPositionList.get(index);
    }

    @Override
    public String getPageUrlAt(int index) {
        return mPageUrlList.get(index);
    }

    @Override
    public List<String> getUrlList() {
        return mPageUrlList;
    }
}
