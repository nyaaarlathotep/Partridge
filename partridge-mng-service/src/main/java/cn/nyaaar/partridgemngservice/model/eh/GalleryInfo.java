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


import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.regex.Pattern;

public class GalleryInfo {

    /**
     * ISO 639-1
     */
    public static final String S_LANG_JA = "JA";
    public static final String S_LANG_EN = "EN";
    public static final String S_LANG_ZH = "ZH";
    public static final String S_LANG_NL = "NL";
    public static final String S_LANG_FR = "FR";
    public static final String S_LANG_DE = "DE";
    public static final String S_LANG_HU = "HU";
    public static final String S_LANG_IT = "IT";
    public static final String S_LANG_KO = "KO";
    public static final String S_LANG_PL = "PL";
    public static final String S_LANG_PT = "PT";
    public static final String S_LANG_RU = "RU";
    public static final String S_LANG_ES = "ES";
    public static final String S_LANG_TH = "TH";
    public static final String S_LANG_VI = "VI";

    public static final String[] S_LANGS = {
            S_LANG_EN,
            S_LANG_ZH,
            S_LANG_ES,
            S_LANG_KO,
            S_LANG_RU,
            S_LANG_FR,
            S_LANG_PT,
            S_LANG_TH,
            S_LANG_DE,
            S_LANG_IT,
            S_LANG_VI,
            S_LANG_PL,
            S_LANG_HU,
            S_LANG_NL,
    };

    public static final Pattern[] S_LANG_PATTERNS = {
            Pattern.compile("[(\\[]eng(?:lish)?[)\\]]|英訳", Pattern.CASE_INSENSITIVE),
            // [(（\[]ch(?:inese)?[)）\]]|[汉漢]化|中[国國][语語]|中文|中国翻訳
            Pattern.compile("[(\uFF08\\[]ch(?:inese)?[)\uFF09\\]]|[汉漢]化|中[国國][语語]|中文|中国翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]spanish[)\\]]|[(\\[]Español[)\\]]|スペイン翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]korean?[)\\]]|韓国翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]rus(?:sian)?[)\\]]|ロシア翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]fr(?:ench)?[)\\]]|フランス翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]portuguese|ポルトガル翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]thai(?: ภาษาไทย)?[)\\]]|แปลไทย|タイ翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]german[)\\]]|ドイツ翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]italiano?[)\\]]|イタリア翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]vietnamese(?: Tiếng Việt)?[)\\]]|ベトナム翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]polish[)\\]]|ポーランド翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]hun(?:garian)?[)\\]]|ハンガリー翻訳", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[(\\[]dutch[)\\]]|オランダ翻訳", Pattern.CASE_INSENSITIVE),
    };

    public static final String[] S_LANG_TAGS = {
            "language:english",
            "language:chinese",
            "language:spanish",
            "language:korean",
            "language:russian",
            "language:french",
            "language:portuguese",
            "language:thai",
            "language:german",
            "language:italian",
            "language:vietnamese",
            "language:polish",
            "language:hungarian",
            "language:dutch",
    };

    public long gid;
    public String token;
    public String title;
    public String titleJpn;
    public String thumb;
    public int category;
    public String posted;
    public String uploader;
    public boolean disowned;
    public float rating;
    public boolean rated;
    @Nullable
    public String[] simpleTags;
    public int pages;
    public int thumbWidth;
    public int thumbHeight;
    public int spanSize;
    public int spanIndex;
    public int spanGroupIndex;
    /**
     * language from title
     */
    public String simpleLanguage;
    public int favoriteSlot = -2;
    public String favoriteName;

    public GalleryInfo() {
    }


    public final void generateSLang() {
        if (simpleTags != null) {
            generateSLangFromTags();
        }
        if (simpleLanguage == null && title != null) {
            generateSLangFromTitle();
        }
    }

    private void generateSLangFromTags() {
        for (String tag : simpleTags) {
            for (int i = 0; i < S_LANGS.length; i++) {
                if (S_LANG_TAGS[i].equals(tag)) {
                    simpleLanguage = S_LANGS[i];
                    return;
                }
            }
        }
    }

    private void generateSLangFromTitle() {
        for (int i = 0; i < S_LANGS.length; i++) {
            if (S_LANG_PATTERNS[i].matcher(title).find()) {
                simpleLanguage = S_LANGS[i];
                return;
            }
        }
        simpleLanguage = null;
    }

    @Override
    public String toString() {
        return "GalleryInfo{" +
                "gid=" + gid +
                ", token='" + token + '\'' +
                ", title='" + title + '\'' +
                ", titleJpn='" + titleJpn + '\'' +
                ", thumb='" + thumb + '\'' +
                ", category=" + category +
                ", posted='" + posted + '\'' +
                ", uploader='" + uploader + '\'' +
                ", disowned=" + disowned +
                ", rating=" + rating +
                ", rated=" + rated +
                ", simpleTags=" + Arrays.toString(simpleTags) +
                ", pages=" + pages +
                ", thumbWidth=" + thumbWidth +
                ", thumbHeight=" + thumbHeight +
                ", spanSize=" + spanSize +
                ", spanIndex=" + spanIndex +
                ", spanGroupIndex=" + spanGroupIndex +
                ", simpleLanguage='" + simpleLanguage + '\'' +
                ", favoriteSlot=" + favoriteSlot +
                ", favoriteName='" + favoriteName + '\'' +
                '}';
    }
}
