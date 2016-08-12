package com.cd.mtgoappraiser;

import com.cd.mtgoappraiser.csv.MtgoCSVParser;
import com.cd.mtgoappraiser.model.Card;
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
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Cory on 8/10/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class MtgoCSVParserTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        @Scope("prototype")
        public MtgoCSVParser mtgoCsvParser() {
            MtgoCSVParser mtgoCsvParser = new MtgoCSVParser();
            return mtgoCsvParser;
        }
    }

    @Autowired
    private MtgoCSVParser mtgoCsvParser;

    @Test
    public void testParseCollection() throws Exception {
        URL urlToCollection = loadResource("testCollection.csv");

        List<Card> cards = mtgoCsvParser.getCards(urlToCollection);

        assertTrue(cards != null);
        assertTrue(cards.size() == 681);
    }

    private URL loadResource(String resourceName) throws IOException {
        return this.getClass().getResource(resourceName);
    }

}
