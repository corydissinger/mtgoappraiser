package com.cd.mtgoappraiser.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Cory on 4/22/2017.
 */
public abstract class AbstractCsvProducer {
    protected CSVPrinter getCsvPrinter(String outputFile) throws IOException {
        return getCsvPrinter(outputFile, Constants.APPRAISED_CARDS_CSV_HEADERS);
    }

    protected CSVPrinter getCsvPrinter(String outputFile, String [] headers) throws IOException {
        CSVPrinter printer = null;
        FileWriter writer = null;
        File outputCsv = new File(outputFile);

        if(!outputCsv.exists()) {
            outputCsv.getParentFile().mkdirs();
            outputCsv.createNewFile();
        }

        writer = new FileWriter(outputCsv);
        printer = new CSVPrinter(writer, CSVFormat.EXCEL.withHeader(headers));

        return printer;
    }
}
