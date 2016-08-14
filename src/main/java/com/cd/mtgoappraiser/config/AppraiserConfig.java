package com.cd.mtgoappraiser.config;

import com.cd.mtgoappraiser.appraiser.CollectionAppraiser;
import com.cd.mtgoappraiser.csv.CsvProducer;
import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.mtggoldfish.MtgGoldfishIndexParser;
import com.cd.mtgoappraiser.mtggoldfish.MtgGoldfishIndexRequestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.mtgoappraiser" })
@PropertySource(value = { "classpath:application.properties" })
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
}

