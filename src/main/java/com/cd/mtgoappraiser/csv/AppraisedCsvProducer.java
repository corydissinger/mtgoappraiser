package com.cd.mtgoappraiser.csv;

import com.cd.mtgoappraiser.model.AppraisedCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Cory on 8/14/2016.
 */
public class AppraisedCsvProducer {

    private String outputFile;
    private String mtgGoldfishBaseUrl;

    public void printAppraisedCards(List<AppraisedCard> appraisedCards) {
        FileWriter writer = null;
        CSVPrinter printer = null;

        try {
            File outputCsv = new File(outputFile);

            if(!outputCsv.exists()) {
                outputCsv.getParentFile().mkdirs();
                outputCsv.createNewFile();
            }

            writer = new FileWriter(outputCsv);
            printer = new CSVPrinter(writer, CSVFormat.EXCEL.withHeader("Name",
                                                                         "Set",
                                                                         "Quantity",
                                                                         "Premium",
                                                                         "MTGGoldfishRetailAggregate",
                                                                         "MTGOTradersHotBuyListPrice",
                                                                         "MTGGoldfishLink"));

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
                if(writer != null && printer != null) {
                    writer.flush();
                    writer.close();
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
