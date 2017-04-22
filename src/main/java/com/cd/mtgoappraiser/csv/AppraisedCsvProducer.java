package com.cd.mtgoappraiser.csv;

import com.cd.mtgoappraiser.model.AppraisedCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Cory on 8/14/2016.
 */
public class AppraisedCsvProducer extends AbstractCsvProducer {

    private String outputFile;
    private String mtgGoldfishBaseUrl;

    public void printAppraisedCards(List<AppraisedCard> appraisedCards) {
        CSVPrinter printer = null;
        try {
            printer = getCsvPrinter(outputFile);

            for(AppraisedCard appraisedCard : appraisedCards) {
                try {
                    printer.printRecord(appraisedCard.getName(),
                                        appraisedCard.getSet(),
                                        appraisedCard.getQuantity(),
                                        appraisedCard.isPremium() ? "Yes" : "No",
                                        appraisedCard.getMtgGoldfishRetailAggregate(),
                                        appraisedCard.getMtgoTradersBuyPrice(),
                                        appraisedCard.getLink() != null ? mtgGoldfishBaseUrl + appraisedCard.getLink() : "NA");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Created appraised collection here: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
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

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public void setMtgGoldfishBaseUrl(String mtgGoldfishBaseUrl) { this.mtgGoldfishBaseUrl = mtgGoldfishBaseUrl; }
}
