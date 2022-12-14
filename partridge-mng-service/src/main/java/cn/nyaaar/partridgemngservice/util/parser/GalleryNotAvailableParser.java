package cn.nyaaar.partridgemngservice.util.parser;

import cn.nyaaar.partridgemngservice.util.ExceptionUtils;
import cn.nyaaar.partridgemngservice.util.JsoupUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GalleryNotAvailableParser {

    public static String parse(String body) {
        String error = null;
        try {
            Document document = Jsoup.parse(body);
            Element d = JsoupUtils.getElementByClass(document, "d");
            //noinspection ConstantConditions
            error = d.child(0).html();
            error = error.replace("<br>", "\n");
        } catch (Throwable e) {
            ExceptionUtils.throwIfFatal(e);
            e.printStackTrace();
        }
        return error;
    }
}
