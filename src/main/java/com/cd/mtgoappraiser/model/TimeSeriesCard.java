package com.cd.mtgoappraiser.model;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cory on 3/31/2017.
 */
public class TimeSeriesCard {

    private AppraisedCard theCard;

    public TimeSeriesCard() {
        dateToValueMap = new HashMap<>();
    }

    private HashMap<LocalDate, AppraisedCard> dateToValueMap;

    public AppraisedCard getCard() {
        return theCard;
    }

    public void putDateAndCard(LocalDate aDate, AppraisedCard aCard) {
        theCard = aCard;
        dateToValueMap.put(aDate, aCard);
    }

    public List<LocalDate> getSortedDates() {
        final List<LocalDate> dates = CollectionUtils.arrayToList(dateToValueMap.keySet().toArray());
        Collections.sort(dates);
        return dates;
    }

    public Double getChangeRaw() {
        final List<LocalDate> dates = getSortedDates();

        final AppraisedCard current = dateToValueMap.get(dates.get(dates.size()-1));
        final AppraisedCard oldest = dateToValueMap.get(dates.get(0));

        return current.getMtgoTradersBuyPrice() - oldest.getMtgoTradersBuyPrice();
    }

    public BigDecimal getChangeAsPercent() {
        final List<LocalDate> dates = getSortedDates();

        final AppraisedCard current = dateToValueMap.get(dates.get(dates.size()-1));
        final AppraisedCard oldest = dateToValueMap.get(dates.get(0));

        return BigDecimal.valueOf(current.getMtgoTradersBuyPrice() / oldest.getMtgoTradersBuyPrice());
    }

    public Double getLocalChangeRaw() {
        final List<LocalDate> dates = getSortedDates();

        final AppraisedCard current = dateToValueMap.get(dates.get(dates.size()-1));
        final AppraisedCard yesterday = dateToValueMap.get(dates.get(dates.size()-2));

        return current.getMtgoTradersBuyPrice() - yesterday.getMtgoTradersBuyPrice();
    }

    public BigDecimal getLocalChangeAsPercent() {
        final List<LocalDate> dates = getSortedDates();

        final AppraisedCard current = dateToValueMap.get(dates.get(dates.size()-1));
        final AppraisedCard yesterday = dateToValueMap.get(dates.get(dates.size()-2));

        return new BigDecimal(current.getMtgoTradersBuyPrice() / yesterday.getMtgoTradersBuyPrice());
    }

    public boolean isHeld() {
        List<LocalDate> dates = getSortedDates();
        LocalDate current = dates.get(dates.size()-1);
        return current.isEqual(LocalDate.now());
    }

    public boolean isNew() {
        return dateToValueMap.size() == 1;
    }
}
