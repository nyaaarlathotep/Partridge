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

package cn.nyaaar.partridgemngservice.util.parser;



import cn.nyaaar.partridgemngservice.exception.eh.ParseException;
import cn.nyaaar.partridgemngservice.model.eh.GalleryInfo;
import cn.nyaaar.partridgemngservice.model.eh.GalleryTagGroup;
import cn.nyaaar.partridgemngservice.util.*;


import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class GalleryListParser {
    private static final String NO_UNFILTERED_TEXT = "No unfiltered results in this page range. You either requested an invalid page or used too aggressive filters.";

    private static final Pattern PATTERN_RATING = Pattern.compile("\\d+px");
    private static final Pattern PATTERN_THUMB_SIZE = Pattern.compile("height:(\\d+)px;width:(\\d+)px");
    private static final Pattern PATTERN_FAVORITE_SLOT = Pattern.compile("background-color:rgba\\((\\d+),(\\d+),(\\d+),");
    private static final Pattern PATTERN_PAGES = Pattern.compile("(\\d+) page");
    private static final Pattern PATTERN_NEXT_PAGE = Pattern.compile("page=(\\d+)");

    private static final String[][] FAVORITE_SLOT_RGB = new String[][]{
            new String[]{"0", "0", "0"},
            new String[]{"240", "0", "0"},
            new String[]{"240", "160", "0"},
            new String[]{"208", "208", "0"},
            new String[]{"0", "128", "0"},
            new String[]{"144", "240", "64"},
            new String[]{"64", "176", "240"},
            new String[]{"0", "0", "240"},
            new String[]{"80", "0", "128"},
            new String[]{"224", "128", "224"},
    };

//    private static int parsePages(Document d, String body) throws ParseException {
//        try {
//            Elements es = d.getElementsByClass("ptt").first().child(0).child(0).children();
//            return Integer.parseInt(es.get(es.size() - 2).text().trim());
//        } catch (Throwable e) {
//            ExceptionUtils.throwIfFatal(e);
//            throw new ParseException("Can't parse gallery list pages", body);
//        }
//    }

    private static String parseRating(String ratingStyle) {
        Matcher m = PATTERN_RATING.matcher(ratingStyle);
        int num1 = Integer.MIN_VALUE;
        int num2 = Integer.MIN_VALUE;
        int rate = 5;
        String re;
        if (m.find()) {
            num1 = ParserUtils.parseInt(m.group().replace("px", ""), Integer.MIN_VALUE);
        }
        if (m.find()) {
            num2 = ParserUtils.parseInt(m.group().replace("px", ""), Integer.MIN_VALUE);
        }
        if (num1 == Integer.MIN_VALUE || num2 == Integer.MIN_VALUE) {
            return null;
        }
        rate = rate - num1 / 16;
        if (num2 == 21) {
            rate--;
            re = Integer.toString(rate);
            re = re + ".5";
        } else
            re = Integer.toString(rate);
        return re;
    }

    private static int parseFavoriteSlot(String style) {
        Matcher m = PATTERN_FAVORITE_SLOT.matcher(style);
        if (m.find()) {
            String r = m.group(1);
            String g = m.group(2);
            String b = m.group(3);
            int slot = 0;
            for (String[] rgb : FAVORITE_SLOT_RGB) {
                if (r.equals(rgb[0]) && g.equals(rgb[1]) && b.equals(rgb[2])) {
                    return slot;
                }
                slot++;
            }
        }
        return -2;
    }

    private static GalleryInfo parseGalleryInfo(Element e) {
        GalleryInfo gi = new GalleryInfo();

        // Title, gid, token (required), tags
        Element glName = JsoupUtils.getElementByClass(e, "glName");
        if (glName != null) {
            Element a = JsoupUtils.getElementByTag(glName, "a");
            if (a == null) {
                Element parent = glName.parent();
                if (parent != null && "a".equals(parent.tagName())) {
                    a = parent;
                }
            }
            if (a != null) {
                GalleryDetailUrlParser.Result result = GalleryDetailUrlParser.parse(a.attr("href"));
                if (result != null) {
                    gi.gid = result.gid;
                    gi.token = result.token;
                }
            }

            Element child = glName;
            Elements children = glName.children();
            while (children.size() != 0) {
                child = children.get(0);
                children = child.children();
            }
            gi.title = child.text().trim();

            Element tbody = JsoupUtils.getElementByTag(glName, "tbody");
            if (tbody != null) {
                ArrayList<String> tags = new ArrayList<>();
                GalleryTagGroup[] groups = GalleryDetailParser.parseTagGroups(tbody.children());
                for (GalleryTagGroup group : groups) {
                    for (int j = 0; j < group.size(); j++) {
                        tags.add(group.groupName + ":" + group.getTagAt(j));
                    }
                }
                gi.simpleTags = tags.toArray(new String[0]);
            }
        }
        if (gi.title == null) {
            return null;
        }

        // Category
        gi.category = EhUtils.UNKNOWN;
        Element ce = JsoupUtils.getElementByClass(e, "cn");
        if (ce == null) {
            ce = JsoupUtils.getElementByClass(e, "cs");
        }
        if (ce != null) {
            gi.category = EhUtils.getCategory(ce.text());
        }

        // Thumb
        Element glthumb = JsoupUtils.getElementByClass(e, "glthumb");
        if (glthumb != null) {
            Element img = glthumb.select("div:nth-child(1)>img").first();
            if (img != null) {
                // Thumb size
                Matcher m = PATTERN_THUMB_SIZE.matcher(img.attr("style"));
                if (m.find()) {
                    gi.thumbWidth = NumberUtils.parseIntSafely(m.group(2), 0);
                    gi.thumbHeight = NumberUtils.parseIntSafely(m.group(1), 0);
                } else {
                    log.warn("Can't parse gallery info thumb size");
                    gi.thumbWidth = 0;
                    gi.thumbHeight = 0;
                }
                // Thumb url
                String url = img.attr("data-src");
                if (StringUtils.isEmpty(url)) {
                    url = img.attr("src");
                }
                if (StringUtils.isEmpty(url)) {
                    url = null;
                }
                gi.thumb = EhUtils.handleThumbUrlResolution(url);
            }

            // Pages
            Element div = glthumb.select("div:nth-child(2)>div:nth-child(2)>div:nth-child(2)").first();
            if (div != null) {
                Matcher matcher = PATTERN_PAGES.matcher(div.text());
                if (matcher.find()) {
                    gi.pages = NumberUtils.parseIntSafely(matcher.group(1), 0);
                }
            }
        }
        // Try extended and thumbnail version
        if (gi.thumb == null) {
            Element gl = JsoupUtils.getElementByClass(e, "gl1e");
            if (gl == null) {
                gl = JsoupUtils.getElementByClass(e, "gl3t");
            }
            if (gl != null) {
                Element img = JsoupUtils.getElementByTag(gl, "img");
                if (img != null) {
                    // Thumb size
                    Matcher m = PATTERN_THUMB_SIZE.matcher(img.attr("style"));
                    if (m.find()) {
                        gi.thumbWidth = NumberUtils.parseIntSafely(m.group(2), 0);
                        gi.thumbHeight = NumberUtils.parseIntSafely(m.group(1), 0);
                    } else {
                        log.warn("Can't parse gallery info thumb size");
                        gi.thumbWidth = 0;
                        gi.thumbHeight = 0;
                    }
                    gi.thumb = EhUtils.handleThumbUrlResolution(img.attr("src"));
                }
            }
        }

        // Posted
        gi.favoriteSlot = -2;
        Element posted = e.getElementById("posted_" + gi.gid);
        if (posted != null) {
            gi.posted = posted.text().trim();
            gi.favoriteSlot = parseFavoriteSlot(posted.attr("style"));
        }
//        if (gi.favoriteSlot == -2) {
//            gi.favoriteSlot = EhDB.containLocalFavorites(gi.gid) ? -1 : -2;
//        }

        // Rating
        Element ir = JsoupUtils.getElementByClass(e, "ir");
        if (ir != null) {
            gi.rating = NumberUtils.parseFloatSafely(parseRating(ir.attr("style")), -1.0f);
            // TODO The gallery may be rated even if it doesn't has one of these classes
            gi.rated = ir.hasClass("irr") || ir.hasClass("irg") || ir.hasClass("irb");
        }

        // Uploader and pages
        Element gl = JsoupUtils.getElementByClass(e, "glhide");
        int uploaderIndex = 0;
        int pagesIndex = 1;
        if (gl == null) {
            // For extended
            gl = JsoupUtils.getElementByClass(e, "gl3e");
            uploaderIndex = 3;
            pagesIndex = 4;
        }
        if (gl != null) {
            Elements children = gl.children();
            if (children.size() > uploaderIndex) {
                Element div = children.get(uploaderIndex);
                if (div != null) {
                    gi.disowned = "opacity:0.5".equals(div.attr("style"));
                    Element a = div.children().first();
                    if (a != null) {
                        gi.uploader = a.text().trim();
                    }
                }
            }
            if (children.size() > pagesIndex) {
                Matcher matcher = PATTERN_PAGES.matcher(children.get(pagesIndex).text());
                if (matcher.find()) {
                    gi.pages = NumberUtils.parseIntSafely(matcher.group(1), 0);
                }
            }
        }
        // For thumbnail
        Element gl5t = JsoupUtils.getElementByClass(e, "gl5t");
        if (gl5t != null) {
            Element div = gl5t.select("div:nth-child(2)>div:nth-child(2)").first();
            if (div != null) {
                Matcher matcher = PATTERN_PAGES.matcher(div.text());
                if (matcher.find()) {
                    gi.pages = NumberUtils.parseIntSafely(matcher.group(1), 0);
                }
            }
        }

        gi.generateSLang();

        return gi;
    }

    public static Result parse(@NonNull String body) throws Exception {
        Result result = new Result();
        Document d = Jsoup.parse(body);

        try {
            Element ptt = d.getElementsByClass("ptt").first();
            Elements es = ptt.child(0).child(0).children();
            result.pages = Integer.parseInt(es.get(es.size() - 2).text().trim());

            Element e = es.get(es.size() - 1);
            if (e != null) {
                e = e.children().first();
                if (e != null) {
                    String href = e.attr("href");
                    Matcher matcher = PATTERN_NEXT_PAGE.matcher(href);
                    if (matcher.find()) {
                        result.nextPage = NumberUtils.parseIntSafely(matcher.group(1), 0);
                    }
                }
            }
        } catch (Throwable e) {
            ExceptionUtils.throwIfFatal(e);
            result.noWatchedTags = body.contains("<p>You do not have any watched tags");
            if (body.contains("No hits found</p>")) {
                result.pages = 0;
                //noinspection unchecked
                result.galleryInfoList = Collections.EMPTY_LIST;
                return result;
            } else if (d.getElementsByClass("ptt").isEmpty()) {
                result.pages = 1;
            } else {
                result.pages = Integer.MAX_VALUE;
            }
        }

        try {
            Element itg = d.getElementsByClass("itg").first();
            Elements es;
            if ("table".equalsIgnoreCase(itg.tagName())) {
                es = itg.child(0).children();
            } else {
                es = itg.children();
            }
            List<GalleryInfo> list = new ArrayList<>(es.size());
            // First one is table header, skip it
            for (Element e : es) {
                GalleryInfo gi = parseGalleryInfo(e);
                if (null != gi) {
                    list.add(gi);
                }
            }
            if (list.isEmpty()) {
                if (es.size() < 2 || !NO_UNFILTERED_TEXT.equals(es.get(1).text())) {
                    throw new ParseException("No gallery", body);
                }
            }
            result.galleryInfoList = list;
        } catch (Throwable e) {
            ExceptionUtils.throwIfFatal(e);
            e.printStackTrace();
            throw new ParseException("Can't parse gallery list", body);
        }

        return result;
    }

    public static class Result {
        public int pages;
        public int nextPage;
        public boolean noWatchedTags;
        public List<GalleryInfo> galleryInfoList;

    }
}
