package com.cd.mtgoappraiser;

import com.cd.mtgoappraiser.config.AppraiserConfig;
import com.cd.mtgoappraiser.csv.AppraisedCsvParser;
import com.cd.mtgoappraiser.csv.AppraisedCsvProducer;
import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.csv.TimeSeriesAppraisalCsvProducer;
import com.cd.mtgoappraiser.http.JsoupCacheManager;
import com.cd.mtgoappraiser.http.mtgotraders.MtgoTradersHotListParser;
import com.cd.mtgoappraiser.model.AppraisedCard;
import com.cd.mtgoappraiser.model.Card;
import com.cd.mtgoappraiser.model.MarketCard;
import com.cd.mtgoappraiser.http.mtggoldfish.MtgGoldfishIndexRequestor;
import com.cd.mtgoappraiser.model.TimeSeriesCard;
import com.cd.mtgoappraiser.timeseries.TimeSeriesFileAppraiser;
import org.apache.commons.cli.*;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URL;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Cory on 7/16/2016.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    private static final String OPT_APPRAISE = "appraise";
    private static final String OPT_TIME_SERIES_APPRAISE = "timeSeriesAppraise";

    public static void main(String [] args) {
        System.out.println("Starting mtgoappraiser...");

        Options cliOptions = new Options();

        cliOptions.addOption(OPT_APPRAISE, "Appraise target collection.");
        cliOptions.addOption(OPT_TIME_SERIES_APPRAISE, "Appraise all appraised collections.");

        DefaultParser cliParser = new DefaultParser();
        CommandLine cli = null;

        try {
            cli = cliParser.parse(cliOptions, args);
            logger.info("CLI args=" + cli.toString());
        } catch (ParseException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

        //Setup spring context and load beans
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppraiserConfig.class);

        if(cli.hasOption(OPT_TIME_SERIES_APPRAISE)) {
            try {
                timeSeriesAppraiseCollection(applicationContext);
            } catch (NumberFormatException nfe) {
                logger.error(nfe.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        } else {
            appraiseCollection(applicationContext);
        }

        System.out.println("All done.");
        System.exit(0);
    }

    private static void timeSeriesAppraiseCollection(ApplicationContext applicationContext) throws IOException {
        AppraisedCsvParser appraisedCsvParser = (AppraisedCsvParser) applicationContext.getBean("appraisedCsvParser");
        TimeSeriesAppraisalCsvProducer timeSeriesAppraisalCsvProducer = (TimeSeriesAppraisalCsvProducer) applicationContext.getBean("timeSeriesAppraisalCsvProducer");
        TimeSeriesFileAppraiser timeSeriesFileAppraiser = (TimeSeriesFileAppraiser) applicationContext.getBean("timeSeriesFileAppraiser");

        List<URL> appraisedFiles = appraisedCsvParser.getAppraisedFiles();

        Map<Integer, TimeSeriesCard> cardHashToTimeSeriesCardMap = timeSeriesFileAppraiser.getCardToTimeSeriesMap(appraisedFiles);

        LocalDate[] minMaxDate = timeSeriesFileAppraiser.getMinMaxDate();

        timeSeriesAppraisalCsvProducer.printTimeSeriesCards(cardHashToTimeSeriesCardMap.values(), minMaxDate[0], minMaxDate[1]);
    }

    private static void appraiseCollection(ApplicationContext applicationContext) {
        MtgoCSVParser mtgoCsvParser = (MtgoCSVParser) applicationContext.getBean("mtgoCsvParser");
        MtgGoldfishIndexRequestor mtgGoldfishIndexRequestor = (MtgGoldfishIndexRequestor) applicationContext.getBean("mtgGoldfishIndexRequestor");
        AppraisedCsvProducer appraisedCsvProducer = (AppraisedCsvProducer) applicationContext.getBean("appraisedCsvProducer");
        MtgoTradersHotListParser mtgoTradersHotListParser = (MtgoTradersHotListParser) applicationContext.getBean("mtgoTradersHotListParser");
        URL mtgoCollectionUrl = (URL) applicationContext.getBean("mtgoCollectionUrl");
        //End spring setup

        List<Card> rawCards = mtgoCsvParser.getCards(mtgoCollectionUrl);

        List<String> indexUrls = mtgGoldfishIndexRequestor.getIndexUrls();

        //A little wonky. Simplest way I could figure out how to combine a list of lists from here - http://stackoverflow.com/questions/31706699/combine-stream-of-collections-into-one-collection-java-8
        List<MarketCard> mtgGoldfishRetailCards = indexUrls.stream()
                                                              .map(indexUrl -> mtgGoldfishIndexRequestor.getCardsFromPage(indexUrl))
                                                              .flatMap(Collection::stream)
                                                              .collect(Collectors.toList());

        //Remove duplicates since many of the indices from mtggoldfish will contain duplicates
        Set<MarketCard> mtgGoldfishRetailCardsSet = new HashSet<>(mtgGoldfishRetailCards);

        List<MarketCard> mtgoTradersBuylistCards = mtgoTradersHotListParser.getHotBuyListCards();

        //Create a list of appraised cards, basically involves cross referencing the cards from
        //the original CSV and slapping a quantity on them
        List<AppraisedCard> appraisedCards = mtgGoldfishRetailCardsSet.stream()
                .filter(mtgGoldfishMarketCard -> rawCards.contains(mtgGoldfishMarketCard))
                .map(mtgGoldfishMarketCard -> {
            AppraisedCard appraisedCard = new AppraisedCard(mtgGoldfishMarketCard);

            Card aRawCard = rawCards.get(rawCards.indexOf(mtgGoldfishMarketCard));

            appraisedCard.setQuantity(aRawCard.getQuantity());
            appraisedCard.setSet(aRawCard.getSet());

            int indexOfHotBuyCard = mtgoTradersBuylistCards.indexOf(mtgGoldfishMarketCard);

            if(indexOfHotBuyCard > -1) {
                appraisedCard.setMtgoTradersBuyPrice(mtgoTradersBuylistCards.get(indexOfHotBuyCard).getBuyPrice());
            }

            return appraisedCard;
        }).collect(Collectors.toCollection(ArrayList::new));

        //This will catch in-demand foils. Both of these streams should be refactored, they're bad
        //This also misses retail from MTGGoldFish, but they don't really provide retail for foils easily anyway
        appraisedCards.addAll(mtgoTradersBuylistCards.stream()
                .filter(mtgoTradersBuylistCard -> (mtgoTradersBuylistCard.isPremium() && rawCards.contains(mtgoTradersBuylistCard)))
                .map(mtgoTradersBuylistCard -> {
                    AppraisedCard appraisedCard = new AppraisedCard(mtgoTradersBuylistCard);

                    Card aRawCard = rawCards.get(rawCards.indexOf(mtgoTradersBuylistCard));

                    appraisedCard.setQuantity(aRawCard.getQuantity());
                    appraisedCard.setSet(aRawCard.getSet());
                    appraisedCard.setMtgoTradersBuyPrice(mtgoTradersBuylistCard.getBuyPrice());

                    return appraisedCard;
                }).collect(Collectors.toCollection(ArrayList::new)));

        appraisedCards.sort(Comparator.comparing(AppraisedCard::getName));
        appraisedCards.sort(Comparator.comparing(AppraisedCard::getMtgGoldfishRetailAggregate).reversed());
        appraisedCards.sort(Comparator.comparing(AppraisedCard::getMtgoTradersBuyPrice).reversed());

        appraisedCsvProducer.printAppraisedCards(appraisedCards);
    }
}
