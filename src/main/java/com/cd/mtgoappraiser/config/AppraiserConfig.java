package com.cd.mtgoappraiser.config;

import com.cd.mtgoappraiser.csv.AppraisedCsvParser;
import com.cd.mtgoappraiser.csv.AppraisedCsvProducer;
import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.http.JsoupCacheManager;
import com.cd.mtgoappraiser.http.mtggoldfish.MtgGoldfishIndexParser;
import com.cd.mtgoappraiser.http.mtggoldfish.MtgGoldfishIndexRequestor;
import com.cd.mtgoappraiser.http.mtgotraders.MtgoTradersHotListParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.mtgoappraiser" })
@PropertySources({
    @PropertySource(value = { "classpath:application.properties" }),
    @PropertySource(value = "file:./application.properties", ignoreResourceNotFound = true )
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
}

