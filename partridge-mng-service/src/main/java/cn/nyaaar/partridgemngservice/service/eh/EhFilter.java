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

package cn.nyaaar.partridgemngservice.service.eh;


import cn.nyaaar.partridgemngservice.entity.eh.Filter;
import cn.nyaaar.partridgemngservice.model.eh.GalleryInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class EhFilter {

    public static final int MODE_TITLE = 0;
    public static final int MODE_UPLOADER = 1;
    public static final int MODE_TAG = 2;
    public static final int MODE_TAG_NAMESPACE = 3;
    private static final String TAG = EhFilter.class.getSimpleName();
    private static EhFilter sInstance;
    private final List<Filter> mTitleFilterList = new ArrayList<>();
    private final List<Filter> mUploaderFilterList = new ArrayList<>();
    private final List<Filter> mTagFilterList = new ArrayList<>();
    private final List<Filter> mTagNamespaceFilterList = new ArrayList<>();

    private EhFilter() {
//        List<Filter> list = EhDB.getAllFilter();
        List<Filter> list = new ArrayList<>();
        // TODO filter list
        for (Filter filter : list) {
            switch (filter.mode) {
                case MODE_TITLE -> {
                    filter.text = filter.text.toLowerCase();
                    mTitleFilterList.add(filter);
                }
                case MODE_UPLOADER -> mUploaderFilterList.add(filter);
                case MODE_TAG -> {
                    filter.text = filter.text.toLowerCase();
                    mTagFilterList.add(filter);
                }
                case MODE_TAG_NAMESPACE -> {
                    filter.text = filter.text.toLowerCase();
                    mTagNamespaceFilterList.add(filter);
                }
                default -> log.error("Unknown mode: " + filter.mode);
            }
        }
    }

    public static EhFilter getInstance() {
        if (sInstance == null) {
            sInstance = new EhFilter();
        }
        return sInstance;
    }

    public List<Filter> getTitleFilterList() {
        return mTitleFilterList;
    }

    public List<Filter> getUploaderFilterList() {
        return mUploaderFilterList;
    }

    public List<Filter> getTagFilterList() {
        return mTagFilterList;
    }

    public List<Filter> getTagNamespaceFilterList() {
        return mTagNamespaceFilterList;
    }

    public synchronized boolean addFilter(Filter filter) {
        // enable filter by default before it is added to database
        filter.enable = true;
//        if (!EhDB.addFilter(filter)) return false;

        switch (filter.mode) {
            case MODE_TITLE -> {
                filter.text = filter.text.toLowerCase();
                mTitleFilterList.add(filter);
            }
            case MODE_UPLOADER -> mUploaderFilterList.add(filter);
            case MODE_TAG -> {
                filter.text = filter.text.toLowerCase();
                mTagFilterList.add(filter);
            }
            case MODE_TAG_NAMESPACE -> {
                filter.text = filter.text.toLowerCase();
                mTagNamespaceFilterList.add(filter);
            }
            default -> log.error("Unknown mode: " + filter.mode);
        }
        return true;
    }

//    public synchronized void triggerFilter(Filter filter) {
//        EhDB.triggerFilter(filter);
//    }
//
//    public synchronized void deleteFilter(Filter filter) {
//        EhDB.deleteFilter(filter);
//
//        switch (filter.mode) {
//            case MODE_TITLE:
//                mTitleFilterList.remove(filter);
//                break;
//            case MODE_UPLOADER:
//                mUploaderFilterList.remove(filter);
//                break;
//            case MODE_TAG:
//                mTagFilterList.remove(filter);
//                break;
//            case MODE_TAG_NAMESPACE:
//                mTagNamespaceFilterList.remove(filter);
//                break;
//            default:
//                log.error(TAG, "Unknown mode: " + filter.mode);
//                break;
//        }
//    }

    public synchronized boolean needTags() {
        return 0 != mTagFilterList.size() || 0 != mTagNamespaceFilterList.size();
    }

    public synchronized boolean filterTitle(GalleryInfo info) {
        if (null == info) {
            return false;
        }

        // Title
        String title = info.title;
        List<Filter> filters = mTitleFilterList;
        if (null != title && filters.size() > 0) {
            for (Filter filter : filters) {
                if (filter.enable && title.toLowerCase().contains(filter.text)) {
                    return false;
                }
            }
        }

        return true;
    }

    public synchronized boolean filterUploader(GalleryInfo info) {
        if (null == info) {
            return false;
        }

        // Uploader
        String uploader = info.uploader;
        List<Filter> filters = mUploaderFilterList;
        if (null != uploader && filters.size() > 0) {
            for (Filter filter : filters) {
                if (filter.enable && uploader.equals(filter.text)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean matchTag(String tag, String filter) {
        if (null == tag || null == filter) {
            return false;
        }

        String tagNamespace;
        String tagName;
        String filterNamespace;
        String filterName;
        int index = tag.indexOf(':');
        if (index < 0) {
            tagNamespace = null;
            tagName = tag;
        } else {
            tagNamespace = tag.substring(0, index);
            tagName = tag.substring(index + 1);
        }
        index = filter.indexOf(':');
        if (index < 0) {
            filterNamespace = null;
            filterName = filter;
        } else {
            filterNamespace = filter.substring(0, index);
            filterName = filter.substring(index + 1);
        }

        if (null != tagNamespace && null != filterNamespace &&
                !tagNamespace.equals(filterNamespace)) {
            return false;
        }
        return tagName.equals(filterName);
    }

    public synchronized boolean filterTag(GalleryInfo info) {
        if (null == info) {
            return false;
        }

        // Tag
        String[] tags = info.simpleTags;
        List<Filter> filters = mTagFilterList;
        if (null != tags && filters.size() > 0) {
            for (String tag : tags) {
                for (Filter filter : filters) {
                    if (filter.enable && matchTag(tag, filter.text)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean matchTagNamespace(String tag, String filter) {
        if (null == tag || null == filter) {
            return false;
        }

        String tagNamespace;
        int index = tag.indexOf(':');
        if (index >= 0) {
            tagNamespace = tag.substring(0, index);
            return tagNamespace.equals(filter);
        } else {
            return false;
        }
    }

    public synchronized boolean filterTagNamespace(GalleryInfo info) {
        if (null == info) {
            return false;
        }

        String[] tags = info.simpleTags;
        List<Filter> filters = mTagNamespaceFilterList;
        if (null != tags && filters.size() > 0) {
            for (String tag : tags) {
                for (Filter filter : filters) {
                    if (filter.enable && matchTagNamespace(tag, filter.text)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
