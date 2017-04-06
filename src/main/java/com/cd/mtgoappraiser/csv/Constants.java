package com.cd.mtgoappraiser.csv;

/**
 * Created by Cory on 3/21/2017.
 */
public class Constants {

    public static final String HEADER_NAME     = "Card Name";
    public static final String HEADER_QUANTITY = "Quantity";
    public static final String HEADER_SET      = "Set";
    public static final String HEADER_PREMIUM  = "Premium";
    public static final String HEADER_MTGGOLDFISH_RETAIL_AGGREGATE  = "MTGGoldfishRetailAggregate";
    public static final String HEADER_MTGOTRADER_BUYLIST  = "MTGOTradersHotBuyListPrice";

    public static final String[] APPRAISED_CARDS_CSV_HEADERS = new String[] {HEADER_NAME,
            HEADER_SET,
            HEADER_QUANTITY,
            HEADER_PREMIUM,
            HEADER_MTGGOLDFISH_RETAIL_AGGREGATE,
            HEADER_MTGOTRADER_BUYLIST,
            "MTGGoldfishLink"};

}
