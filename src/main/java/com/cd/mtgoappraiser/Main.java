package com.cd.mtgoappraiser;

import com.cd.mtgoappraiser.config.AppraiserConfig;
import com.cd.mtgoappraiser.csv.CsvProducer;
import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.model.AppraisedCard;
import com.cd.mtgoappraiser.model.Card;
import com.cd.mtgoappraiser.model.MtgGoldfishCard;
import com.cd.mtgoappraiser.mtggoldfish.MtgGoldfishIndexRequestor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 7/16/2016.
 */
public class Main {
    public static void main(String [] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppraiserConfig.class);

        MtgoCSVParser mtgoCsvParser = (MtgoCSVParser) applicationContext.getBean("mtgoCsvParser");
        MtgGoldfishIndexRequestor mtgGoldfishIndexRequestor = (MtgGoldfishIndexRequestor) applicationContext.getBean("mtgGoldfishIndexRequestor");
        CsvProducer csvProducer = (CsvProducer) applicationContext.getBean("csvProducer");
        URL mtgoCollectionUrl = (URL) applicationContext.getBean("mtgoCollectionUrl");

        List<Card> rawCards = mtgoCsvParser.getCards(mtgoCollectionUrl);

        List<String> indexUrls = mtgGoldfishIndexRequestor.getIndexUrls();

        List<AppraisedCard> appraisedCards = new ArrayList<>();

        indexUrls.stream().forEach(indexUrl -> {
            List<MtgGoldfishCard> marketCards = mtgGoldfishIndexRequestor.getCardsFromPage(indexUrl);

            appraisedCards.addAll(marketCards.stream()
                    .filter(marketCard -> rawCards.contains(marketCard))
                    .map(marketCard -> {
                marketCard.setQuantity(rawCards.get(rawCards.indexOf(marketCard)).getQuantity());

                AppraisedCard appraisedCard = new AppraisedCard(marketCard);

                appraisedCard.setSumPrice(appraisedCard.getRetailPrice() * appraisedCard.getQuantity());

                return appraisedCard;
            }).collect(Collectors.toCollection(ArrayList::new)));
        });

        csvProducer.printAppraisedCards(appraisedCards);

        System.exit(0);
    }
}
