package com.cd.mtgoappraiser.csv;

import com.cd.mtgoappraiser.model.AppraisedCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Cory on 8/14/2016.
 */
@Service("csvProducer")
public class CsvProducer {

    private String outputDirectory;

    public void printAppraisedCards(List<AppraisedCard> appraisedCards) {
        FileWriter writer;

        try {
            writer = new FileWriter(new File(outputDirectory));
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.RFC4180);

            appraisedCards.stream().forEach(appraisedCard -> {
                try {
                    printer.printRecord(appraisedCard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }



}
