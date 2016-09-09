package com.cd.mtgoappraiser.http;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Cory on 9/8/2016.
 */
public class JsoupCacheManager {

    private File cacheFolder;

    public Document loadFromCache(String pageUrl) throws Exception {
        return loadFromCache(pageUrl, null);
    }

    public Document loadFromCache(String pageUrl, String suggestedTempName) throws Exception {
        final String tempFileName;

        if(suggestedTempName == null) {
            tempFileName = getTempFileName(pageUrl);
        } else {
            tempFileName = suggestedTempName;
        }

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
            //Sleep so that our requesting doesn't look as robotic...
            System.out.println("Need to request: " + pageUrl);
            System.out.println("Sleeping for a ~second so we appear less bot like.");
            Thread.sleep(ThreadLocalRandom.current().nextInt(700, 1301));
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

}
