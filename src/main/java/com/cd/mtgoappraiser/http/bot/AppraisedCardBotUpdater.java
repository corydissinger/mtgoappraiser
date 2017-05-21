package com.cd.mtgoappraiser.http.bot;

import com.cd.bot.model.domain.Card;
import com.cd.bot.model.domain.OwnedTradeableCard;
import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.wrapper.http.OwnedTradeableCardService;
import com.cd.mtgoappraiser.model.AppraisedCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cory on 5/20/2017.
 */
public class AppraisedCardBotUpdater {
    @Autowired
    private String accountName;

    @Autowired
    private OwnedTradeableCardService ownedTradeableCardService;

    public void updateApi(List<AppraisedCard> appraisedCards) {
        List<OwnedTradeableCard> cardsToPost = new ArrayList<>();

        for(int i = 0; i < appraisedCards.size(); i++) {
            AppraisedCard card = appraisedCards.get(i);
            OwnedTradeableCard ownedTradeableCard = new OwnedTradeableCard();

            Card modelCard = new Card(card.getName(), card.getSet(), card.isPremium());

            ownedTradeableCard.setQuantity(card.getQuantity());

            ownedTradeableCard.setCard(modelCard);

            cardsToPost.add(ownedTradeableCard);

            if (cardsToPost.size() % 100 == 0) {
                ownedTradeableCardService.addCards(cardsToPost, accountName);
                cardsToPost = new ArrayList<>();
            }
        }

        ownedTradeableCardService.addCards(cardsToPost, accountName);
    }
}
