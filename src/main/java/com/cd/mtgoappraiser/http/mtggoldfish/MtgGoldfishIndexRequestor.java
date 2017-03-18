package com.cd.mtgoappraiser.http.mtggoldfish;

import com.cd.mtgoappraiser.http.JsoupCacheManager;
import com.cd.mtgoappraiser.model.MarketCard;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Cory on 8/10/2016.
 */
public class MtgGoldfishIndexRequestor {

    private static final String INDICES = "prices/select";

    private String mtgoGoldfishBaseUrl;
    private MtgGoldfishIndexParser mtgGoldfishIndexParser;
    private JsoupCacheManager jsoupCacheManager;

    public List<String> getIndexUrls() {
        try {
            Document theHtml = jsoupCacheManager.loadFromCache(mtgoGoldfishBaseUrl + INDICES);
            if(theHtml != null) {
                return mtgGoldfishIndexParser.getIndexUrls(theHtml);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<MarketCard> getCardsFromPage(String pageUrl) {
        Document theHtml;

        if(pageUrl.contains("DD") || pageUrl.contains("ATH")) {
            return new ArrayList<>();
        }

        try {
            theHtml = jsoupCacheManager.loadFromCache(mtgoGoldfishBaseUrl + pageUrl + "#online");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(theHtml == null) {
            return new ArrayList<>();
        }


        return mtgGoldfishIndexParser.getCardsFromPage(theHtml);
    }

    public void setMtgoGoldfishBaseUrl(String mtgoGoldfishBaseUrl) {
        this.mtgoGoldfishBaseUrl = mtgoGoldfishBaseUrl;
    }

    public void setMtgGoldfishIndexParser(MtgGoldfishIndexParser mtgGoldfishIndexParser) {
        this.mtgGoldfishIndexParser = mtgGoldfishIndexParser;
    }

    public void setJsoupCacheManager(JsoupCacheManager jsoupCacheManager) {
        this.jsoupCacheManager = jsoupCacheManager;
    }
}
