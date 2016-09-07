package com.cd.mtgoappraiser.mtggoldfish;

import com.cd.mtgoappraiser.model.MtgGoldfishCard;
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

    private File cacheFolder;
    private String mtgoGoldfishBaseUrl;
    private MtgGoldfishIndexParser mtgGoldfishIndexParser;

    public List<String> getIndexUrls() {
        try {
            Document theHtml = loadFromCache(mtgoGoldfishBaseUrl + INDICES);
            return mtgGoldfishIndexParser.getIndexUrls(theHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<MtgGoldfishCard> getCardsFromPage(String pageUrl) {
        Document theHtml;

        try {
            theHtml = loadFromCache(mtgoGoldfishBaseUrl + pageUrl + "#online");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return mtgGoldfishIndexParser.getCardsFromPage(theHtml);
    }

    private Document loadFromCache(String pageUrl) throws Exception {
        String tempFileName = getTempFileName(pageUrl);

        final boolean shouldLoadFromCache;

        File formatCache = new File(cacheFolder + File.separator + tempFileName);

        if(formatCache.exists()) {
            BasicFileAttributes fileAttributes = Files.readAttributes(formatCache.toPath(), BasicFileAttributes.class);
            FileTime lastModified = fileAttributes.lastModifiedTime();
            Instant lastModifiedInstant = lastModified.toInstant();
            Instant oneDayAgo = Instant.now().minus(24, ChronoUnit.HOURS);

            shouldLoadFromCache = lastModifiedInstant.isAfter(oneDayAgo);
        } else {
            shouldLoadFromCache = false;
        }

        Document theHtml;

        if (shouldLoadFromCache) {
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

        if(theFormat == null) {
            throw new Exception("Could not parse URL, unable to get local temp file name.");
        } else if (theFormat.contains("#")) {
            theFormat = theFormat.substring(0, theFormat.indexOf("#"));
        }

        return theFormat;
    }

    public void setCacheFolder(File cacheFolder) {
        this.cacheFolder = cacheFolder;
    }

    public void setMtgoGoldfishBaseUrl(String mtgoGoldfishBaseUrl) {
        this.mtgoGoldfishBaseUrl = mtgoGoldfishBaseUrl;
    }

    public void setMtgGoldfishIndexParser(MtgGoldfishIndexParser mtgGoldfishIndexParser) {
        this.mtgGoldfishIndexParser = mtgGoldfishIndexParser;
    }

}
