package com.cd.mtgoappraiser.model;

/**
 * Created by Cory on 8/10/2016.
 */
public class MtgGoldfishCard extends Card {
    private Double retailPrice;
    private Double buyPrice;
    private String link;

    public MtgGoldfishCard() {}

    public MtgGoldfishCard(MtgGoldfishCard aCard) {
        super(aCard);
        this.setRetailPrice(aCard.getRetailPrice());
        this.setBuyPrice(aCard.getBuyPrice());
        this.setLink(aCard.getLink());
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean equals(MtgGoldfishCard right) {
        return super.equals(right);
    }
}
