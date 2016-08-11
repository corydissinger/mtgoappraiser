package com.cd.mtgoappraiser;

import com.cd.mtgoappraiser.config.AppraiserConfig;
import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.mtggoldfish.MtgGoldfishIndexParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Cory on 7/16/2016.
 */
public class Main {
    public static void main(String [] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppraiserConfig.class);

        MtgoCSVParser mtgoCsvParser = (MtgoCSVParser) applicationContext.getBean("mtgoCsvParser");
        MtgGoldfishIndexParser mtgGoldfishIndexParser = (MtgGoldfishIndexParser) applicationContext.getBean("mtgoCsvParser");

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
