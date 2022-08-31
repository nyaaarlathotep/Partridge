/*
 * Copyright 2019 Hippo Seven
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
import cn.nyaaar.partridgemngservice.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GalleryPageApiParser {

    private static final Pattern PATTERN_IMAGE_URL = Pattern.compile("<img[^>]*src=\"([^\"]+)\" style");
    private static final Pattern PATTERN_SKIP_HATH_KEY = Pattern.compile("onclick=\"return nl\\('([^\\)]+)'\\)");
    private static final Pattern PATTERN_ORIGIN_IMAGE_URL = Pattern.compile("<a href=\"([^\"]+)fullimg.php([^\"]+)\">");

    public static Result parse(String body) throws ParseException {
        try {
            Matcher m;
            Result result = new Result();

            JSONObject jo = JSON.parseObject(body);
            if (jo.containsKey("error")) {
                throw new ParseException(jo.getString("error"), body);
            }

            String i3 = jo.getString("i3");
            m = PATTERN_IMAGE_URL.matcher(i3);
            if (m.find()) {
                result.imageUrl = StringUtils.unescapeXml(StringUtils.trim(m.group(1)));
            }
            String i6 = jo.getString("i6");
            m = PATTERN_SKIP_HATH_KEY.matcher(i6);
            if (m.find()) {
                result.skipHathKey = StringUtils.unescapeXml(StringUtils.trim(m.group(1)));
            }
            String i7 = jo.getString("i7");
            m = PATTERN_ORIGIN_IMAGE_URL.matcher(i7);
            if (m.find()) {
                result.originImageUrl = StringUtils.unescapeXml(m.group(1)) + "fullimg.php" + StringUtils.unescapeXml(m.group(2));
            }

            if (!StringUtils.isEmpty(result.imageUrl)) {
                return result;
            } else {
                throw new ParseException("Parse image url and skip hath key error", body);
            }
        } catch (JSONException e) {
            throw new ParseException("Can't parse json", body, e);
        }
    }

    public static class Result {
        public String imageUrl;
        public String skipHathKey;
        public String originImageUrl;
    }
}
