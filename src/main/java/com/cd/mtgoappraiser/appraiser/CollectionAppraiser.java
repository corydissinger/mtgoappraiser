package com.cd.mtgoappraiser.appraiser;

import com.cd.mtgoappraiser.csv.CsvProducer;
import com.cd.mtgoappraiser.model.AppraisedCard;
import com.cd.mtgoappraiser.model.Card;
import com.cd.mtgoappraiser.model.MtgGoldfishCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Cory on 8/14/2016.
 */
@Service("collectionAppraiser")
public class CollectionAppraiser {

    @Autowired
    private CsvProducer csvProducer;

    public void appraiseCollection(List<Card> unappraisedCards, List<MtgGoldfishCard> cardsInMarket) {

    }

    public void setCsvProducer(CsvProducer csvProducer) {
        this.csvProducer = csvProducer;
    }

}
