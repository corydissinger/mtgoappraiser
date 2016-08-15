package com.cd.mtgoappraiser;

import com.cd.mtgoappraiser.appraiser.CollectionAppraiser;
import com.cd.mtgoappraiser.config.AppraiserConfig;
import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.model.Card;
import com.cd.mtgoappraiser.model.MtgGoldfishCard;
import com.cd.mtgoappraiser.mtggoldfish.MtgGoldfishIndexParser;
import com.cd.mtgoappraiser.mtggoldfish.MtgGoldfishIndexRequestor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;
import java.util.List;

/**
 * Created by Cory on 7/16/2016.
 */
public class Main {
    public static void main(String [] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppraiserConfig.class);

        MtgoCSVParser mtgoCsvParser = (MtgoCSVParser) applicationContext.getBean("mtgoCsvParser");
        MtgGoldfishIndexRequestor mtgGoldfishIndexRequestor = (MtgGoldfishIndexRequestor) applicationContext.getBean("mtgGoldfishIndexRequestor");
        CollectionAppraiser collectionAppraiser = (CollectionAppraiser) applicationContext.getBean("collectionAppraiser");
        URL mtgoCollectionUrl = (URL) applicationContext.getBean("mtgoCollectionUrl");

        List<Card> rawCards = mtgoCsvParser.getCards(mtgoCollectionUrl);

        List<String> indexUrls = mtgGoldfishIndexRequestor.getIndexUrls();

        indexUrls.stream().forEach(indexUrl -> {
            List<MtgGoldfishCard> marketCards = mtgGoldfishIndexRequestor.getCardsFromPage(indexUrl);

            marketCards.stream().forEach(marketCard -> {
                System.out.println(marketCard.toString());
            });
        });

        System.exit(0);
        /*
        Function<String, List<Card>> urlToCards = new Function<String, List<Card>>() {
            public List<Card> apply(String url) {
                return mtgGoldfishIndexParser.getCardsFromPage(url);
            }
        };

        try {
            List<Card> allCardsAndPrices = mtgGoldfishIndexParser.getIndexUrls()
                    .stream()
                    .map(indexUrl -> urlToCards.apply(indexUrl))
                    .collect(Collectors.toList());
        } catch(Exception e) {
            e.printStackTrace();
        }*/
    }
}
