package com.cd.mtgoappraiser.http;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
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

    private static final Logger logger = Logger.getLogger(JsoupCacheManager.class);

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
            logger.info("Need to request: " + pageUrl);
            logger.info("Sleeping for a ~second so we appear less bot like.");
            Thread.sleep(ThreadLocalRandom.current().nextInt(700, 1301));

            Connection.Response resp;

            try {
                resp = Jsoup.connect(pageUrl).header("Accept-Encoding", "gzip, deflate")
                        .maxBodySize(0)
                        .timeout(600000).execute();
            } catch (HttpStatusException e) {
                logger.error("Failed to load HTML for " + pageUrl);
                logger.error(e.getMessage());
                return null;
            }

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(formatCache));

            try {
                out.write(resp.bodyAsBytes());
            } catch (IOException ioe) {
                logger.error("Could not cache result for " + pageUrl);
                logger.error(ioe.getMessage());
            } finally {
                if(out != null) {
                    out.close();
                }
            }

            theHtml = resp.parse();
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
