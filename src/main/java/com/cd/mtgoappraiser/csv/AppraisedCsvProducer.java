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
        FileWriter writer;

        try {
            File outputCsv = new File(outputFile);

            if(!outputCsv.exists()) {
                outputCsv.getParentFile().mkdirs();
                outputCsv.createNewFile();
            }

            writer = new FileWriter(outputCsv);
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.RFC4180.withHeader("Name", "Set", "Quantity", "RetailPrice", "SumPrice", "Link"));

            appraisedCards.stream().forEach(appraisedCard -> {
                try {
                    printer.printRecord(appraisedCard.getName(), appraisedCard.getSet(), appraisedCard.getQuantity(), appraisedCard.getRetailPrice(), appraisedCard.getSumPrice(), mtgGoldfishBaseUrl + appraisedCard.getLink());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public void setMtgGoldfishBaseUrl(String mtgGoldfishBaseUrl) { this.mtgGoldfishBaseUrl = mtgGoldfishBaseUrl; }
}
