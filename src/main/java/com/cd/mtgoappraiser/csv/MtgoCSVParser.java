package com.cd.mtgoappraiser.csv;

import com.cd.mtgoappraiser.model.Card;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Cory on 7/16/2016.
 */
@Service("mtgoCsvParser")
public class MtgoCSVParser {

    private static final String HEADER_NAME     = "Card Name";
    private static final String HEADER_QUANTITY = "Quantity";
    private static final String HEADER_SET      = "Set";
    private static final String HEADER_PREMIUM  = "Premium";

    public List<Card> getCards(URL urlToCollection) {
        List<Card> cards = null;

        try {
            Iterable<CSVRecord> records = CSVParser.parse(urlToCollection, Charset.defaultCharset(), CSVFormat.RFC4180.withHeader());

            cards = StreamSupport.stream(records.spliterator(), false)
                                    .map(csvRecord -> {
                                        Card theCard = new Card();

                                        theCard.setName(csvRecord.get(HEADER_NAME));
                                        theCard.setQuantity(Integer.parseInt(csvRecord.get(HEADER_QUANTITY)));
                                        theCard.setSet(csvRecord.get(HEADER_SET));
                                        theCard.setPremium("Yes".equals(csvRecord.get(HEADER_PREMIUM)));

                                        return theCard;
                                    })
                                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cards;
    }

}
