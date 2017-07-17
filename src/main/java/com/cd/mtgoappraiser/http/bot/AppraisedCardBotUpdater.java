package com.cd.mtgoappraiser.http.bot;

import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import com.cd.bot.model.domain.trade.Card;
import com.cd.bot.model.domain.trade.OwnedTradeableCard;
import com.cd.bot.model.domain.repository.OwnedTradeableCardRepository;
import com.cd.mtgoappraiser.model.AppraisedCard;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Cory on 5/20/2017.
 */
public class AppraisedCardBotUpdater {
    @Autowired
    private String accountName;

    private OwnedTradeableCardRepository ownedTradeableCardRepository;

    private PlayerBotRepository playerBotRepository;

    public void setOwnedTradeableCardRepository(OwnedTradeableCardRepository ownedTradeableCardRepository) {
        this.ownedTradeableCardRepository = ownedTradeableCardRepository;
    }

    public void setPlayerBotRepository(PlayerBotRepository playerBotRepository) {
        this.playerBotRepository = playerBotRepository;
    }

    public void updateApi(List<AppraisedCard> appraisedCards) {
        PlayerBot playerBot = playerBotRepository.findByName(accountName);

        for(AppraisedCard card : appraisedCards) {
            OwnedTradeableCard ownedTradeableCard = new OwnedTradeableCard();

            Card modelCard = new Card(card.getName(), card.getSet(), card.isPremium());

            ownedTradeableCard.setQuantity(card.getQuantity());
            ownedTradeableCard.setCard(modelCard);
            ownedTradeableCard.setPlayerBot(playerBot);

            ownedTradeableCardRepository.save(ownedTradeableCard);
        }
    }

}
