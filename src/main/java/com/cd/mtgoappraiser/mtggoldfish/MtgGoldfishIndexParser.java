package com.cd.mtgoappraiser.mtggoldfish;

import com.cd.mtgoappraiser.model.MtgGoldfishCard;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

    public List<MtgGoldfishCard> getCardsFromPage(Document theHtml) {
        List<Element> cardsInFormat = theHtml.select("body > div.container-fluid.layout-container-fluid > div.index-price-table > div.index-price-table-online > table > tbody > tr");

        List<MtgGoldfishCard> parsedCards = cardsInFormat.stream().map(rawCard -> {
            MtgGoldfishCard parsedCard = new MtgGoldfishCard();

            parsedCard.setName(rawCard.select("td.card > a").text());
            parsedCard.setPrice(Double.parseDouble(rawCard.select("td.text-right").first().text()));

            return parsedCard;
        }).collect((Collectors.toCollection(ArrayList::new)));

        return parsedCards;
    }

}
