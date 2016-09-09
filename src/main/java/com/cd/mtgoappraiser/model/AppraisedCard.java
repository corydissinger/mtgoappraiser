package com.cd.mtgoappraiser.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Comparator;

/**
 * Created by Cory on 8/12/2016.
 */
public class AppraisedCard {
    private Double mtgGoldfishRetailAggregate;
    private Double mtgoTradersBuyPrice;
    private String link;
    private String name;
    private Integer quantity;
    private String set;
    private boolean isPremium;

    public AppraisedCard(MarketCard aCard) {
        this.setName(aCard.getName());
        this.setQuantity(aCard.getQuantity());
        this.setSet(aCard.getSet());
        this.setPremium(aCard.isPremium());
        this.setMtgGoldfishRetailAggregate(aCard.getRetailPrice());
        this.setMtgoTradersBuyPrice(aCard.getBuyPrice());
        this.setLink(aCard.getLink());
    }

    public Double getMtgGoldfishRetailAggregate() {
        return mtgGoldfishRetailAggregate;
    }

    public void setMtgGoldfishRetailAggregate(Double mtgGoldfishRetailAggregate) {
        this.mtgGoldfishRetailAggregate = mtgGoldfishRetailAggregate;
    }

    public Double getMtgoTradersBuyPrice() {
        return mtgoTradersBuyPrice;
    }

    public void setMtgoTradersBuyPrice(Double mtgoTradersBuyPrice) {
        this.mtgoTradersBuyPrice = mtgoTradersBuyPrice;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    public static class AppraisedCardComparator implements Comparator<AppraisedCard> {

        @Override
        public int compare(AppraisedCard left, AppraisedCard right) {
            if(left.getMtgGoldfishRetailAggregate() > right.getMtgGoldfishRetailAggregate()) {
                return -1;
            } else if (left.getMtgGoldfishRetailAggregate() < right.getMtgGoldfishRetailAggregate()) {
                return 1;
            } else {
                return left.getName().compareToIgnoreCase(right.getName());
            }
        }
    }
}
