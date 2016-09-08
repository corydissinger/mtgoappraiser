package com.cd.mtgoappraiser.http.mtgotraders;

import com.cd.mtgoappraiser.http.JsoupCacheManager;
import com.cd.mtgoappraiser.http.mtgotraders.json.MtgoTradersCard;
import com.cd.mtgoappraiser.model.MarketCard;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 9/8/2016.
 */
public class MtgoTradersHotListParser {
    public static final String HOTLIST_PATH = "hotlist/data/get.php?order=asc&limit=10000&offset=0";

    private String mtgoTradersBaseUrl;
    private JsoupCacheManager jsoupCacheManager;
    private ObjectMapper objectMapper;

    public List<MarketCard> getHotBuyListCards() {
        Document theHtml = null;

        try {
            theHtml = jsoupCacheManager.loadFromCache(mtgoTradersBaseUrl + HOTLIST_PATH, "mtgotradersbuylist");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Element stupidJsonBody = theHtml.getElementsByTag("body").first();

        List<MtgoTradersCard> allCards = null;

        try {
            allCards = objectMapper.readValue(stupidJsonBody.text(), new TypeReference<List<MtgoTradersCard>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allCards.stream().map(jsonCard -> {
            MarketCard marketCard = new MarketCard();

            marketCard.setBuyPrice(jsonCard.getPrice());
            marketCard.setName(jsonCard.getName());
            marketCard.setPremium("Prem".equals(jsonCard.getVerison()));
            marketCard.setSet(jsonCard.getSetshort());

            return marketCard;
        }).collect(Collectors.toList());
    }

    public void setMtgoTradersBaseUrl(String mtgoTradersBaseUrl) {
        this.mtgoTradersBaseUrl = mtgoTradersBaseUrl;
    }

    public void setJsoupCacheManager(JsoupCacheManager jsoupCacheManager) {
        this.jsoupCacheManager = jsoupCacheManager;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
