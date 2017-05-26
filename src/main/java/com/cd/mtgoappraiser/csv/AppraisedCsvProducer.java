package com.cd.mtgoappraiser.csv;

import com.cd.mtgoappraiser.model.AppraisedCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cory on 8/14/2016.
 */
public class AppraisedCsvProducer extends AbstractCsvProducer {

    private String outputFile;
    private String mtgGoldfishBaseUrl;

    public void printAppraisedCards(List<AppraisedCard> appraisedCards) {
        List<Double> goldfishRetail = new ArrayList<>();
        List<Double> mtgotradersBuylist = new ArrayList<>();
        CSVPrinter printer = null;
        try {
            printer = getCsvPrinter(outputFile);

            for(AppraisedCard appraisedCard : appraisedCards) {
                mtgotradersBuylist.add(appraisedCard.getMtgoTradersBuyPrice());
                goldfishRetail.add(appraisedCard.getMtgGoldfishRetailAggregate());

                printer.printRecord(appraisedCard.getName(),
                                    appraisedCard.getSet(),
                                    appraisedCard.getQuantity(),
                                    appraisedCard.isPremium() ? "Yes" : "No",
                                    appraisedCard.getMtgGoldfishRetailAggregate(),
                                    appraisedCard.getMtgoTradersBuyPrice(),
                                    appraisedCard.getLink() != null ? mtgGoldfishBaseUrl + appraisedCard.getLink() : "NA");
            }

            printer.printRecord("TOTALS ----->",
                    null,
                    null,
                    null,
                    goldfishRetail.stream().mapToDouble(a -> new Double(a)).sum(),
                    mtgotradersBuylist.stream().mapToDouble(a -> new Double(a)).sum(),
                    null);

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
