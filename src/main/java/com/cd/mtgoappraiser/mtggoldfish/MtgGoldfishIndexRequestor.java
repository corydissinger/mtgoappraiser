package com.cd.mtgoappraiser.mtggoldfish;

import com.cd.mtgoappraiser.model.CSVCard;
import com.cd.mtgoappraiser.model.MtgGoldfishCard;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.util.List;

/**
 * Created by Cory on 8/10/2016.
 */
public class MtgGoldfishIndexRequestor {

    private static final String INDICES = "indices";

    private File cacheFolder;
    private String mtgoGoldfishBaseUrl;
    private MtgGoldfishIndexParser mtgGoldfishIndexParser;

    public List<String> getIndexUrls() throws Exception {
        Document theHtml = loadFromCache(mtgoGoldfishBaseUrl + INDICES);
        return mtgGoldfishIndexParser.getIndexUrls(theHtml);
    }

    public List<MtgGoldfishCard> getCardsFromPage(String pageUrl) {
        Document theHtml;

        try {
            theHtml = loadFromCache(pageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return mtgGoldfishIndexParser.getCardsFromPage(theHtml);
    }

    private Document loadFromCache(String pageUrl) throws Exception {
        String tempFileName = getTempFileName(pageUrl);

        File formatCache = new File(cacheFolder + tempFileName);

        Document theHtml;

        if (formatCache.exists()) {
            theHtml = Jsoup.parse(FileUtils.readFileToString(formatCache));
        } else {
            theHtml = Jsoup.connect(pageUrl).get();
            FileUtils.write(formatCache, theHtml.toString());
        }

        return theHtml;
    }

    private String getTempFileName(String pageUrl) throws Exception {
        String[] pageParts = pageUrl.split("/");

        String theFormat = pageParts[pageParts.length - 1];

        if(!theFormat.contains("#") || INDICES.equals(theFormat)) {
            throw new Exception("Could not parse URL, unable to get local temp file name.");
        }

        theFormat = theFormat.substring(0, theFormat.indexOf("#") - 1);

        LocalDate date = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("mdmdyyyy");
        String todayString = date.toString(fmt);

        return theFormat + todayString;
    }

    public File getCacheFolder() {
        return cacheFolder;
    }

    public void setCacheFolder(File cacheFolder) {
        this.cacheFolder = cacheFolder;
    }

    public String getMtgoGoldfishBaseUrl() {
        return mtgoGoldfishBaseUrl;
    }

    public void setMtgoGoldfishBaseUrl(String mtgoGoldfishBaseUrl) {
        this.mtgoGoldfishBaseUrl = mtgoGoldfishBaseUrl;
    }

    public MtgGoldfishIndexParser getMtgGoldfishIndexParser() {
        return mtgGoldfishIndexParser;
    }

    public void setMtgGoldfishIndexParser(MtgGoldfishIndexParser mtgGoldfishIndexParser) {
        this.mtgGoldfishIndexParser = mtgGoldfishIndexParser;
    }

}
