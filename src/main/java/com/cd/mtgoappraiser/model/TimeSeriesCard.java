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

    public AppraisedCard getFirstCard() {
        List<LocalDate> sorted = getSortedDates();
        return dateToValueMap.get(sorted.get(0));
    }

    public AppraisedCard getLastCard() {
        List<LocalDate> sorted = getSortedDates();
        return dateToValueMap.get(sorted.get(sorted.size()-1));
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

        return current.getMtgGoldfishRetailAggregate() - oldest.getMtgGoldfishRetailAggregate();
    }

    public BigDecimal getChangeAsPercent() {
        final List<LocalDate> dates = getSortedDates();

        final AppraisedCard current = dateToValueMap.get(dates.get(dates.size()-1));
        final AppraisedCard oldest = dateToValueMap.get(dates.get(0));

        return BigDecimal.valueOf(current.getMtgGoldfishRetailAggregate() / oldest.getMtgGoldfishRetailAggregate()).setScale(2, BigDecimal.ROUND_DOWN);
    }

    public Double getLocalChangeRaw() {
        if(dateToValueMap.size() < 2) {
            return 0.0;
        }

        final List<LocalDate> dates = getSortedDates();

        final AppraisedCard current = dateToValueMap.get(dates.get(dates.size()-1));
        final AppraisedCard yesterday = dateToValueMap.get(dates.get(dates.size()-2));

        if(BigDecimal.ZERO.equals(current) || BigDecimal.ZERO.equals(yesterday)) {
            return 0.0;
        }

        return current.getMtgGoldfishRetailAggregate() - yesterday.getMtgGoldfishRetailAggregate();
    }

    public BigDecimal getLocalChangeAsPercent() {
        if(dateToValueMap.size() < 2) {
            return BigDecimal.ZERO;
        }

        final List<LocalDate> dates = getSortedDates();

        final AppraisedCard current = dateToValueMap.get(dates.get(dates.size()-1));
        final AppraisedCard yesterday = dateToValueMap.get(dates.get(dates.size()-2));

        if(BigDecimal.ZERO.equals(current) || BigDecimal.ZERO.equals(yesterday)) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(current.getMtgGoldfishRetailAggregate() / yesterday.getMtgGoldfishRetailAggregate()).setScale(2, BigDecimal.ROUND_DOWN);
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
