package com.cd.mtgoappraiser.config;

import com.cd.mtgoappraiser.appraiser.CollectionAppraiser;
import com.cd.mtgoappraiser.csv.CsvProducer;
import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.mtggoldfish.MtgGoldfishIndexParser;
import com.cd.mtgoappraiser.mtggoldfish.MtgGoldfishIndexRequestor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
    public CsvProducer csvProducer() {
        CsvProducer csvProducer = new CsvProducer();

        csvProducer.setOutputDirectory(environment.getRequiredProperty("output.file.path"));

        return csvProducer;
    }

    @Bean
    public CollectionAppraiser collectionAppraiser() {
        CollectionAppraiser collectionAppraiser = new CollectionAppraiser();

        collectionAppraiser.setCsvProducer(csvProducer());

        return collectionAppraiser;
    }

    @Bean
    public MtgGoldfishIndexRequestor mtgGoldfishIndexRequestor() {
        MtgGoldfishIndexRequestor mtgGoldfishIndexRequestor = new MtgGoldfishIndexRequestor();

        mtgGoldfishIndexRequestor.setCacheFolder(new File(environment.getRequiredProperty("cache.file.path")));
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

