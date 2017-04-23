package com.cd.mtgoappraiser;

import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.model.AppraisedCard;
import com.cd.mtgoappraiser.model.Card;
import com.cd.mtgoappraiser.model.TimeSeriesCard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Cory on 4/5/2017.
 */
public class TimeSerriesCardTest {

    @Test
    public void testChangeRaw() throws Exception {
        TimeSeriesCard tsc = getTestCard();

        assertTrue(tsc.getChangeRaw().equals(new Double(10*11.11) - new Double(11.11)));
    }

    @Test
    public void testChangeAsPercent() throws Exception {
        TimeSeriesCard tsc = getTestCard();

        assertTrue(tsc.getChangeAsPercent().equals(BigDecimal.valueOf(new Double(10*11.11) / new Double(11.11)).setScale(2, BigDecimal.ROUND_DOWN)));
    }

    @Test
    public void testLocalChangeRaw() throws Exception {
        TimeSeriesCard tsc = getTestCard();

        assertTrue(tsc.getLocalChangeRaw().equals(new Double(10*11.11) - new Double(9*11.11)));
    }

    @Test
    public void testLocalChangeAsPercent() throws Exception {
        TimeSeriesCard tsc = getTestCard();

        assertTrue(tsc.getLocalChangeAsPercent().equals(BigDecimal.valueOf(new Double(10*11.1) / new Double(9*11.1)).setScale(2, BigDecimal.ROUND_DOWN)));
    }

    @Test
    public void testLocalChangeOneValue() throws Exception {
        TimeSeriesCard tsc = new TimeSeriesCard();
        AppraisedCard cardForOneDay = new AppraisedCard();

        cardForOneDay.setMtgGoldfishRetailAggregate(1.11);

        tsc.putDateAndCard(LocalDate.now(), cardForOneDay);

        assertTrue(BigDecimal.ZERO.equals(tsc.getLocalChangeAsPercent()));
    }

    public TimeSeriesCard getTestCard() {
        TimeSeriesCard tsc = new TimeSeriesCard();

        for(int i = 1; i < 11; i++) {
            LocalDate myDate = LocalDate.of(2017, 3, i+2);

            AppraisedCard myCard = new AppraisedCard();
            myCard.setMtgoTradersBuyPrice(new Double(i*11.11));
            myCard.setMtgGoldfishRetailAggregate(new Double(i*11.11));

            tsc.putDateAndCard(myDate, myCard);
        }

        return tsc;
    }
}
