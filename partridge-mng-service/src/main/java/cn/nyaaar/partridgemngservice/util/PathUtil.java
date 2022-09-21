package cn.nyaaar.partridgemngservice.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class PathUtil {

    public static String simpleConcatUrl(String... urls) {
        StringBuilder finalUrl = new StringBuilder();
        if (ArrayUtils.isEmpty(urls)) {
            return finalUrl.toString();
        }

        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            if (StringUtils.isBlank(url)) {
                continue;
            }

            // 移除起始的分隔符
            if (url.startsWith(File.separator)) {
                url = url.substring(1);
            }

            // 如果是最后一个字符串，则不用添加分隔符
            if (i != urls.length - 1) {
                // 在url结尾添加分隔符
                if (!url.endsWith(File.separator)) {
                    url += File.separator;
                }
            }
            finalUrl.append(url);
        }

        return finalUrl.toString();
    }


}
