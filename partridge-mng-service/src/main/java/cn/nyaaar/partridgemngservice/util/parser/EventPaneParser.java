package cn.nyaaar.partridgemngservice.util.parser;


import cn.nyaaar.partridgemngservice.exception.eh.EhException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class EventPaneParser {

    public static String parse(String body) throws EhException {
        String event = null;
        try {
            Document d = Jsoup.parse(body);
            Element eventpane = d.getElementById("eventpane");
            if (eventpane != null) {
                event = eventpane.html();
            }
        } catch (Exception e) {
            throw new EhException("EventPaneParser error, body: " + body);
        }
        return event;
    }


}
