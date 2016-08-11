package com.cd.mtgoappraiser.mtggoldfish;

import com.cd.mtgoappraiser.model.CSVCard;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 7/16/2016.
 */
public class MtgGoldfishIndexParser {

    public List<String> getIndexUrls(Document theHtml) throws Exception {
        List<Element> links = theHtml.select("body > div.container-fluid.layout-container-fluid > table > tbody > tr > td > a");

        List<String> indexUrls = links.stream().map(link -> link.attr("href")).collect(Collectors.toList());

        return indexUrls;
    }

    public List<CSVCard> getCardsFromPage(Document theHtml) {
        List<CSVCard> parsedCards = new ArrayList<>();

        List<Element> cardsInFormat = theHtml.select("body > div.container-fluid.layout-container-fluid > div.index-price-table > div.index-price-table-online > table > tbody > tr:nth-child(1)");

        return parsedCards;
    }

}
