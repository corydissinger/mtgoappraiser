package com.cd.mtgoappraiser;

import com.cd.mtgoappraiser.csv.AppraisedCsvParser;
import com.cd.mtgoappraiser.csv.TimeSeriesAppraisalCsvProducer;
import com.cd.mtgoappraiser.model.TimeSeriesCard;
import com.cd.mtgoappraiser.timeseries.TimeSeriesFileAppraiser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by Cory on 5/4/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class TimeSeriesAppraisalIntegrationTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        @Scope("prototype")
        public TimeSeriesAppraisalCsvProducer timeSeriesAppraisalCsvProducer() {
            TimeSeriesAppraisalCsvProducer timeSeriesAppraisalCsvProducer = new TimeSeriesAppraisalCsvProducer();

            timeSeriesAppraisalCsvProducer.setOutputFileFolder(this.getClass().getResource("").getFile());

            return timeSeriesAppraisalCsvProducer;
        }

        @Bean
        @Scope("prototype")
        public AppraisedCsvParser appraisedCsvParser() {
            AppraisedCsvParser appraisedCsvParser = new AppraisedCsvParser();

            appraisedCsvParser.setOutputFileFolder(this.getClass().getResource("").getFile());
            appraisedCsvParser.setOutputFileName("appraisedcollection-%s.csv");

            return appraisedCsvParser;
        }

        @Bean
        public Double priceThreshold() {
            return new Double(1.00);
        }

        @Bean
        public TimeSeriesFileAppraiser timeSeriesFileAppraiser() {
            TimeSeriesFileAppraiser timeSeriesFileAppraiser = new TimeSeriesFileAppraiser();

            timeSeriesFileAppraiser.setAppraisedCsvParser(appraisedCsvParser());
            timeSeriesFileAppraiser.setPriceThreshold(priceThreshold());

            return timeSeriesFileAppraiser;
        }
    }

    @Autowired
    private TimeSeriesAppraisalCsvProducer timeSeriesAppraisalCsvProducer;

    @Autowired
    private AppraisedCsvParser appraisedCsvParser;

    @Autowired
    private TimeSeriesFileAppraiser timeSeriesFileAppraiser;

    @Test
    public void testParseCollection() throws Exception {
        List<URL> files = appraisedCsvParser.getAppraisedFiles();

        Map<Integer, TimeSeriesCard> cardMap = timeSeriesFileAppraiser.getCardToTimeSeriesMap(files);
        LocalDate[] minMaxDate = timeSeriesFileAppraiser.getMinMaxDate();

        timeSeriesAppraisalCsvProducer.printTimeSeriesCards(cardMap.values(), minMaxDate[0], minMaxDate[1]);

        URL appraisedFile = new URL("file:" + timeSeriesAppraisalCsvProducer.getOutputFile(minMaxDate[0].toString(), minMaxDate[1].toString()));

        assertTrue(new File(appraisedFile.toURI()).exists());
    }

}
