package com.cd.mtgoappraiser.config;

import com.cd.bot.model.domain.repository.OwnedTradeableCardRepository;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import com.cd.mtgoappraiser.csv.AppraisedCsvParser;
import com.cd.mtgoappraiser.csv.AppraisedCsvProducer;
import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.csv.TimeSeriesAppraisalCsvProducer;
import com.cd.mtgoappraiser.http.JsoupCacheManager;
import com.cd.mtgoappraiser.http.bot.AppraisedCardBotUpdater;
import com.cd.mtgoappraiser.http.mtggoldfish.MtgGoldfishIndexParser;
import com.cd.mtgoappraiser.http.mtggoldfish.MtgGoldfishIndexRequestor;
import com.cd.mtgoappraiser.http.mtgotraders.MtgoTradersHotListParser;
import com.cd.mtgoappraiser.timeseries.TimeSeriesFileAppraiser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.cd.bot.model.domain"} )
@EnableAutoConfiguration(exclude={WebMvcAutoConfiguration.class})
@ComponentScan({ "com.cd.mtgoappraiser"})
@PropertySources({
    @PropertySource(value = { "classpath:appraiser-application.properties" }),
    @PropertySource(value = "file:./appraiser-application.properties", ignoreResourceNotFound = true )
})
public class AppraiserConfig {

    @Autowired
    private Environment environment;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JsoupCacheManager jsoupCacheManager() {
        JsoupCacheManager jsoupCacheManager = new JsoupCacheManager();

        jsoupCacheManager.setCacheFolder(new File(environment.getRequiredProperty("cache.file.path")));

        return jsoupCacheManager;
    }

    @Bean
    public MtgoTradersHotListParser mtgoTradersHotListParser() {
        MtgoTradersHotListParser mtgoTradersHotListParser = new MtgoTradersHotListParser();

        mtgoTradersHotListParser.setMtgoTradersBaseUrl(environment.getRequiredProperty("mtgotraders.base.url"));
        mtgoTradersHotListParser.setJsoupCacheManager(jsoupCacheManager());
        mtgoTradersHotListParser.setObjectMapper(objectMapper());

        return mtgoTradersHotListParser;
    }

    @Bean
    public AppraisedCsvProducer appraisedCsvProducer() {
        AppraisedCsvProducer appraisedCsvProducer = new AppraisedCsvProducer();

        appraisedCsvProducer.setOutputFile(outputFileFormatted());
        appraisedCsvProducer.setMtgGoldfishBaseUrl(environment.getRequiredProperty("mtggoldfish.base.url"));

        return appraisedCsvProducer;
    }

    @Bean
    public TimeSeriesFileAppraiser timeSeriesFileAppraiser() {
        TimeSeriesFileAppraiser timeSeriesFileAppraiser = new TimeSeriesFileAppraiser();

        timeSeriesFileAppraiser.setPriceThreshold(priceThreshold());
        timeSeriesFileAppraiser.setAppraisedCsvParser(appraisedCsvParser());

        return timeSeriesFileAppraiser;
    }

    @Bean
    public TimeSeriesAppraisalCsvProducer timeSeriesAppraisalCsvProducer() {
        TimeSeriesAppraisalCsvProducer timeSeriesAppraisalCsvProducer = new TimeSeriesAppraisalCsvProducer();

        timeSeriesAppraisalCsvProducer.setOutputFileFolder(environment.getRequiredProperty("output.file.path"));

        return timeSeriesAppraisalCsvProducer;
    }    

    @Bean
    public String outputFileFormatted() {
        final String rawOutputFile = environment.getRequiredProperty("output.file.path") + environment.getRequiredProperty("output.file.name");
        return String.format(rawOutputFile, LocalDate.now().toString());
    }

    @Bean
    public AppraisedCsvParser appraisedCsvParser() {
        AppraisedCsvParser appraisedCsvParser = new AppraisedCsvParser();

        appraisedCsvParser.setOutputFileFolder(environment.getRequiredProperty("output.file.path"));
        appraisedCsvParser.setOutputFileName(environment.getRequiredProperty("output.file.name"));

        return appraisedCsvParser;
    }    

    @Bean
    public MtgGoldfishIndexRequestor mtgGoldfishIndexRequestor() {
        MtgGoldfishIndexRequestor mtgGoldfishIndexRequestor = new MtgGoldfishIndexRequestor();

        mtgGoldfishIndexRequestor.setJsoupCacheManager(jsoupCacheManager());
        mtgGoldfishIndexRequestor.setMtgoGoldfishBaseUrl(environment.getRequiredProperty("mtggoldfish.base.url"));
        mtgGoldfishIndexRequestor.setMtgGoldfishIndexParser(mtgGoldfishIndexParser());

        return mtgGoldfishIndexRequestor;
    }

    @Bean
    public MtgGoldfishIndexParser mtgGoldfishIndexParser() {
        MtgGoldfishIndexParser parser = new MtgGoldfishIndexParser();

        return parser;
    }

    @Bean
    public MtgoCSVParser mtgoCsvParser() {
        MtgoCSVParser parser = new MtgoCSVParser();
        return parser;
    }

    @Bean
    public URL mtgoCollectionUrl() {
        File collectionFile = new File(environment.getRequiredProperty("mtgo.collection.file.path"));

        try {
            URL[] shouldBeOne = FileUtils.toURLs(new File[] { collectionFile });

            if(shouldBeOne.length == 1) {
                return shouldBeOne[0];
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Bean
    public Double priceThreshold() {
        return Double.parseDouble(environment.getRequiredProperty("price.threshold"));
    }

    @Bean
    public String accountName() {
        return environment.getRequiredProperty("account.name");
    }

    @Bean
    public AppraisedCardBotUpdater appraisedCardBotUpdater() {
        AppraisedCardBotUpdater appraisedCardBotUpdater = new AppraisedCardBotUpdater();

        appraisedCardBotUpdater.setOwnedTradeableCardRepository(ownedTradeableCardRepository);
        appraisedCardBotUpdater.setPlayerBotRepository(playerBotRepository);

        return appraisedCardBotUpdater;
    }

    @Autowired
    PlayerBotRepository playerBotRepository;

    @Autowired
    OwnedTradeableCardRepository ownedTradeableCardRepository;

}


