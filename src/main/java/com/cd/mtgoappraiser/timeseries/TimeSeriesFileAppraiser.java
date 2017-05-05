package com.cd.mtgoappraiser.timeseries;

import com.cd.mtgoappraiser.csv.AppraisedCsvParser;
import com.cd.mtgoappraiser.model.AppraisedCard;
import com.cd.mtgoappraiser.model.TimeSeriesCard;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cory on 5/4/2017.
 */
public class TimeSeriesFileAppraiser {

    public TimeSeriesFileAppraiser() {
        minMaxDate = new LocalDate[2];
    }

    @Autowired
    private AppraisedCsvParser appraisedCsvParser;

    @Autowired
    private Double priceThreshold;

    private LocalDate[] minMaxDate;

    public void setAppraisedCsvParser(AppraisedCsvParser appraisedCsvParser) {
        this.appraisedCsvParser = appraisedCsvParser;
    }

    public void setPriceThreshold(Double priceThreshold) {
        this.priceThreshold = priceThreshold;
    }

    public Map<Integer, TimeSeriesCard> getCardToTimeSeriesMap(List<URL> appraisedFiles) {
        Map<Integer, TimeSeriesCard> cardHashToTimeSeriesCardMap = new HashMap<>();

        appraisedFiles.stream().forEach(url -> {
            List<AppraisedCard> cards = appraisedCsvParser.getCards(url);
            final String urlAsString = url.toString();

            LocalDate currentCollectionDate = com.cd.mtgoappraiser.util.DateUtils.getDate(urlAsString);
            //Happens after this for 3/8... hmmms

            if(minMaxDate[0] == null || minMaxDate[0].compareTo(currentCollectionDate) > 0) {
                minMaxDate[0] = currentCollectionDate;
            }

            if(minMaxDate[1] == null || minMaxDate[1].compareTo(currentCollectionDate) < 0) {
                minMaxDate[1] = currentCollectionDate;
            }

            cards.stream().filter(card -> card != null).forEach(appraisedCard -> {
                if(priceThreshold.compareTo(appraisedCard.getMtgoTradersBuyPrice()) > 0 && priceThreshold.compareTo(appraisedCard.getMtgGoldfishRetailAggregate()) > 0) {
                    return;
                }

                Integer cardHash = appraisedCard.hashCode();

                TimeSeriesCard existingCard = cardHashToTimeSeriesCardMap.get(cardHash);

                if(existingCard == null) {
                    existingCard = new TimeSeriesCard();
                    cardHashToTimeSeriesCardMap.put(cardHash, existingCard);
                }

                existingCard.putDateAndCard(currentCollectionDate, appraisedCard);
            });
        });

        return cardHashToTimeSeriesCardMap;
    }

    public LocalDate[] getMinMaxDate() {
        return minMaxDate;
    }
}
