package com.cd.mtgoappraiser.csv;

import com.cd.mtgoappraiser.http.JsoupCacheManager;
import com.cd.mtgoappraiser.model.AppraisedCard;
import com.cd.mtgoappraiser.model.TimeSeriesCard;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Created by Cory on 4/22/2017.
 */
public class TimeSeriesAppraisalCsvProducer extends AbstractCsvProducer {

    private static final Logger logger = Logger.getLogger(TimeSeriesAppraisalCsvProducer.class);

    private String outputFilePath;

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    private String getOutputFile(String lowestDate, String highestDate) {
        return outputFilePath + "timeSeriesFrom-" + lowestDate + "-to-" + highestDate;
    }

    public void printTimeSeriesCards(Collection<TimeSeriesCard> timeSeriesCards, LocalDate minDate, LocalDate maxDate) {
        CSVPrinter printer = null;

        try {
            final String outputFile = getOutputFile(minDate.toString(), maxDate.toString());
            printer = getCsvPrinter(outputFile, Constants.TIME_SERIES_CARDS_CSV_HEADERS);

            for(TimeSeriesCard timeSeriesCard : timeSeriesCards) {
                AppraisedCard appraisedCard = timeSeriesCard.getCard();

                try {
                    printer.printRecord(appraisedCard.getName(),
                            appraisedCard.getSet(),
                            appraisedCard.getQuantity(),
                            appraisedCard.isPremium() ? "Yes" : "No",
                            appraisedCard.getMtgGoldfishRetailAggregate(),
                            appraisedCard.getMtgoTradersBuyPrice(),
                            timeSeriesCard.getChangeAsPercent(),
                            timeSeriesCard.getLocalChangeAsPercent(),
                            timeSeriesCard.getChangeRaw(),
                            timeSeriesCard.getLocalChangeRaw(),
                            timeSeriesCard.getSortedDates().stream().map(date -> date.toString()).reduce("", String::concat));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
