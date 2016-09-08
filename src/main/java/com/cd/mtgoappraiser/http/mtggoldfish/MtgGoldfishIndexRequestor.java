package com.cd.mtgoappraiser.http.mtggoldfish;

import com.cd.mtgoappraiser.http.JsoupCacheManager;
import com.cd.mtgoappraiser.model.MarketCard;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/**
 * Created by Cory on 8/10/2016.
 */
public class MtgGoldfishIndexRequestor {

    private static final String INDICES = "indices";

    private String mtgoGoldfishBaseUrl;
    private MtgGoldfishIndexParser mtgGoldfishIndexParser;
    private JsoupCacheManager jsoupCacheManager;

    public List<String> getIndexUrls() {
        try {
            Document theHtml = jsoupCacheManager.loadFromCache(mtgoGoldfishBaseUrl + INDICES);
            return mtgGoldfishIndexParser.getIndexUrls(theHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<MarketCard> getCardsFromPage(String pageUrl) {
        Document theHtml;

        try {
            theHtml = jsoupCacheManager.loadFromCache(mtgoGoldfishBaseUrl + pageUrl + "#online");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
