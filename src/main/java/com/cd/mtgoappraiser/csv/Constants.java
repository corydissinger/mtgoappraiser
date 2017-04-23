package com.cd.mtgoappraiser.csv;

/**
 * Created by Cory on 3/21/2017.
 */
public class Constants {

    public static final String HEADER_NAME              = "Card Name";
    public static final String HEADER_QUANTITY          = "Quantity";
    public static final String HEADER_QUANTITY_BEGIN    = "QuantityBegin";
    public static final String HEADER_QUANTITY_END      = "QuantityEnd";
    public static final String HEADER_SET               = "Set";
    public static final String HEADER_PREMIUM           = "Premium";
    public static final String HEADER_MTGGOLDFISH_RETAIL_AGGREGATE  = "MTGGoldfishRetailAggregate";
    public static final String HEADER_MTGOTRADER_BUYLIST  = "MTGOTradersHotBuyListPrice";
    private static final String CHANGE_AS_PCT = "ChangeAsPercent";
    private static final String LOCAL_CHANGE_AS_PCT = "LocalChangeAsPercent";
    private static final String CHANGE_RAW = "ChangeRaw";
    private static final String LOCAL_CHANGE_RAW = "LocalChangeRaw";
    private static final String DATES_RANGE = "DatesRange";
    public static final String HEADER_MTGGOLDFISH_RETAIL_AGGREGATE_BEGIN  = "MTGGoldfishRetailAggregateBegin";
    public static final String HEADER_MTGOTRADER_BUYLIST_BEGIN  = "MTGOTradersHotBuyListPriceBegin";
    public static final String HEADER_MTGGOLDFISH_RETAIL_AGGREGATE_END  = "MTGGoldfishRetailAggregateEnd";
    public static final String HEADER_MTGOTRADER_BUYLIST_END  = "MTGOTradersHotBuyListPriceEnd";

    public static final String[] APPRAISED_CARDS_CSV_HEADERS = new String[] {HEADER_NAME,
            HEADER_SET,
            HEADER_QUANTITY,
            HEADER_PREMIUM,
            HEADER_MTGGOLDFISH_RETAIL_AGGREGATE,
            HEADER_MTGOTRADER_BUYLIST,
            "MTGGoldfishLink"};

    public static final String[] TIME_SERIES_CARDS_CSV_HEADERS = new String[] {HEADER_NAME,
            HEADER_SET,
            HEADER_QUANTITY_BEGIN,
            HEADER_QUANTITY_END,
            HEADER_PREMIUM,
            HEADER_MTGGOLDFISH_RETAIL_AGGREGATE_BEGIN,
            HEADER_MTGOTRADER_BUYLIST_BEGIN,
            HEADER_MTGGOLDFISH_RETAIL_AGGREGATE_END,
            HEADER_MTGOTRADER_BUYLIST_END,
            CHANGE_AS_PCT,
            LOCAL_CHANGE_AS_PCT,
            CHANGE_RAW,
            LOCAL_CHANGE_RAW,
            DATES_RANGE};
}

