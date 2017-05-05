package com.cd.mtgoappraiser.csv;

import com.cd.mtgoappraiser.model.AppraisedCard;
import com.cd.mtgoappraiser.model.TimeSeriesCard;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 4/22/2017.
 */
public class TimeSeriesAppraisalCsvProducer extends AbstractCsvProducer {

    private static final Logger logger = Logger.getLogger(TimeSeriesAppraisalCsvProducer.class);

    private String outputFileFolder;

    public void setOutputFileFolder(String outputFileFolder) {
        this.outputFileFolder = outputFileFolder;
    }

    public String getOutputFile(String lowestDate, String highestDate) {
        return outputFileFolder + "timeSeriesFrom-" + lowestDate + "-to-" + highestDate + ".csv";
    }

    public void printTimeSeriesCards(Collection<TimeSeriesCard> timeSeriesCards, LocalDate minDate, LocalDate maxDate) {
        CSVPrinter printer = null;

        try {
            final String outputFile = getOutputFile(minDate.toString(), maxDate.toString());
            printer = getCsvPrinter(outputFile, Constants.TIME_SERIES_CARDS_CSV_HEADERS);

            List<Double> cardValuesGoldfish = new ArrayList<>();
            List<Double> cardValuesMtgoTraders = new ArrayList<>();
            List<Double> cardValuesSoldGoldfish = new ArrayList<>();
            List<Double> cardValuesSoldMtgoTraders = new ArrayList<>();

            for(TimeSeriesCard timeSeriesCard : timeSeriesCards) {
                AppraisedCard firstCard = timeSeriesCard.getFirstCard();
                AppraisedCard lastCard = timeSeriesCard.getLastCard();
                boolean isHeld = timeSeriesCard.isHeld();

                try {
                    printer.printRecord(firstCard.getName(),
                            firstCard.getSet(),
                            firstCard.getQuantity(),
                            lastCard.getQuantity(),
                            firstCard.isPremium() ? "Yes" : "No",
                            firstCard.getMtgGoldfishRetailAggregate(),
                            firstCard.getMtgoTradersBuyPrice(),
                            lastCard.getMtgGoldfishRetailAggregate(),
                            lastCard.getMtgoTradersBuyPrice(),
                            timeSeriesCard.getChangeAsPercent(),
                            timeSeriesCard.getLocalChangeAsPercent(),
                            timeSeriesCard.getChangeRaw(),
                            timeSeriesCard.getLocalChangeRaw(),
                            timeSeriesCard.getSortedDates().stream().map(date -> date.toString()).collect(Collectors.joining("; ")));

                    if(isHeld) {
                        cardValuesGoldfish.add(lastCard.getMtgGoldfishRetailAggregate() * lastCard.getQuantity());
                        cardValuesMtgoTraders.add(lastCard.getMtgoTradersBuyPrice() * lastCard.getQuantity());
                    } else {
                        cardValuesSoldGoldfish.add(lastCard.getMtgGoldfishRetailAggregate() * lastCard.getQuantity());
                        cardValuesSoldMtgoTraders.add(lastCard.getMtgoTradersBuyPrice() * lastCard.getQuantity());
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage());
                } catch (NumberFormatException nfe) {
                    logger.error(nfe.getMessage());
                    logger.error("NFE found with " + timeSeriesCard.toString() + " and appraised " + firstCard.toString());
                }
            }

            printer.printRecord("Total",
                    "HeldGoldFish->",
                    Double.toString(cardValuesGoldfish.stream().mapToDouble(value -> value).sum()),
                    "HeldMtgoTraders->",
                    Double.toString(cardValuesMtgoTraders.stream().mapToDouble(value -> value).sum()),
                    "SoldGoldFish->",
                    Double.toString(cardValuesSoldGoldfish.stream().mapToDouble(value -> value).sum()),
                    "SoldMtgoTraders->",
                    Double.toString(cardValuesSoldMtgoTraders.stream().mapToDouble(value -> value).sum()),
                    "",
                    "",
                    "",
                    "",
                    "");

            System.out.println("Created time series appraised collection here: " + outputFile);            
            
        } catch (IOException e) {
            logger.equals(e.getMessage());
        } finally {
            try {
                if(printer != null) {
                    printer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
