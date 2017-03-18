package com.cd.mtgoappraiser.http.mtggoldfish;

import com.cd.mtgoappraiser.model.MarketCard;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 7/16/2016.
 */
public class MtgGoldfishIndexParser {

    public List<String> getIndexUrls(Document theHtml) throws Exception {
        List<Element> links = theHtml.select("li[role=presentation] > a");

        List<String> indexUrls = links.stream().map(link -> link.attr("href"))
                                               .filter(link -> link.contains("index")).collect(Collectors.toList());

        return indexUrls;
    }

    public List<MarketCard> getCardsFromPage(Document theHtml) {
        List<Element> cardsInFormat = theHtml.select("body > div.container-fluid.layout-container-fluid > div.index-price-table > div.index-price-table-online > table > tbody > tr");

        List<MarketCard> parsedCards = cardsInFormat.stream().map(rawCard -> {
            MarketCard parsedCard = new MarketCard();

            Element cardElement = rawCard.select("td.card > a").first();

            String setCode = rawCard.select("td").get(1).text();

            parsedCard.setName(cardElement.text());
            parsedCard.setSet(setCode);
            parsedCard.setLink(cardElement.attr("href"));
            parsedCard.setRetailPrice(Double.parseDouble(rawCard.select("td.text-right").first().text()));


            return parsedCard;
        }).collect((Collectors.toList()));

        return parsedCards;
    }

}
