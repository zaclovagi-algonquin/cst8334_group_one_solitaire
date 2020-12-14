package com.cst8334_group_one_solitaire.beans;

public class Hand {
    
    private Card card;
    private CardPile stackedCards;
    private CardPile inPile;
    
    public Hand(Card card, CardPile inPile) {
        this.card = card;
        this.inPile = inPile;
        stackedCards = null;
    }
    
    public Hand(CardPile stackedCards, CardPile inPile) {
        this.stackedCards = stackedCards;
        this.inPile = inPile;
        this.card = stackedCards.inspectTop();
    }
    
    public Card getCard() {
        return card;
    }

    public CardPile getInPile() {
        return inPile;
    }
    
    public boolean isStack() {
        return stackedCards != null;
    }
    
    public CardPile getStack() {
        return stackedCards;
    }
}
