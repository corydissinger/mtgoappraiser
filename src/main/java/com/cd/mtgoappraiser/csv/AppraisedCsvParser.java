package com.cd.mtgoappraiser.csv;

import com.cd.mtgoappraiser.model.AppraisedCard;
import com.cd.mtgoappraiser.model.Card;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.cd.mtgoappraiser.csv.Constants.*;

/**
 * Created by Cory on 7/16/2016.
 */
public class AppraisedCsvParser {

    private static final Logger logger = Logger.getLogger(AppraisedCsvParser.class);

    private String outputFileFolder;
    private String outputFileName;

    public List<AppraisedCard> getCards(URL urlToCollection) {
        List<AppraisedCard> cards = null;

        try {
            Iterable<CSVRecord> records = CSVParser.parse(urlToCollection, Charset.defaultCharset(), CSVFormat.RFC4180.withHeader(Constants.APPRAISED_CARDS_CSV_HEADERS));

            cards = StreamSupport.stream(records.spliterator(), false)
                                    .map(csvRecord -> {
                                        AppraisedCard theCard = new AppraisedCard();

                                        String quantity = csvRecord.get(HEADER_QUANTITY);

                                        if (quantity.isEmpty() || !StringUtils.isNumeric(quantity))
                                            return null;

                                        try {
                                            theCard.setName(csvRecord.get(HEADER_NAME));
                                            theCard.setQuantity(Integer.parseInt(quantity));
                                            theCard.setSet(csvRecord.get(HEADER_SET));
                                            theCard.setPremium("Yes".equals(csvRecord.get(HEADER_PREMIUM)));
                                            theCard.setMtgoTradersBuyPrice(Double.parseDouble(csvRecord.get(HEADER_MTGOTRADER_BUYLIST)));
                                            theCard.setMtgGoldfishRetailAggregate(Double.parseDouble(csvRecord.get(HEADER_MTGGOLDFISH_RETAIL_AGGREGATE)));
                                        } catch (NumberFormatException nfe) {
                                            logger.error("Blew up on this: " + csvRecord.toString());
                                        }

                                        return theCard;
                                    })
                                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cards;
    }

    public List<URL> getAppraisedFiles() throws IOException {
        File appraisedFileFolder = new File(outputFileFolder);

        final FilenameFilter filter = (dir, name) -> {
            if(name.startsWith(outputFileName.substring(0, outputFileName.indexOf("-")))) {
                return true;
            }

            return false;
        };

        URL[] appraisedFileUrls = FileUtils.toURLs(appraisedFileFolder.listFiles(filter));

        return Arrays.asList(appraisedFileUrls);
    }

    public String getOutputFileFolder() {
        return outputFileFolder;
    }

    public void setOutputFileFolder(String outputFileFolder) {
        this.outputFileFolder = outputFileFolder;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
}
