package com.cst8334_group_one_solitaire.beans;

import java.util.Stack;

public class Board {
    
    public CardPile foundations[];
    public CardPile talon;
    public CardPile stockPile;
    public CardPile tableau[];
    public final int pileCount;
    
    public Board(int pileCount) {
        this.pileCount = pileCount;
        initialization();   
    }
    
    public void initialization() {
        stockPile = new CardPile(1200, 55, Card.WIDTH, Card.HEIGHT);
        talon = new CardPile(1075, 55, Card.WIDTH, Card.HEIGHT);
        foundations = new CardPile[4];
        tableau = new CardPile[7];
        for (int i = 0; i < 4; i ++) {
            foundations[i] = new CardPile(15 + (Card.WIDTH + 55) * i, 55, Card.WIDTH, Card.HEIGHT);
        }
        for (int i = 0; i < 7; i ++) {
            tableau[i] = new CardPile(15 + (Card.WIDTH + 55) * i, Card.HEIGHT + 65, Card.WIDTH, 0);
        }
        Deck.initialization();
    }
    
    public void shuffle() {
        Stack<Integer> cards = Deck.shuffleDeck();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j <i+1; j++) {
                int nextCard = cards.pop();
                Card card = Deck.getCard(nextCard);
                tableau[i].addCard(card, true);
                System.out.println("Placing card at tableau " + i + ", " + card.toString());
            }
            tableau[i].inspectTop().flip();
        }
        while (!cards.isEmpty()) {
            stockPile.addCard(Deck.getCard(cards.pop()), false);
        }
    }
    
    public CardPile[] allPiles() {
        CardPile allPiles[] = new CardPile[13];
        allPiles[0] = stockPile;
        allPiles[1] = talon;
        allPiles[2] = foundations[0];
        allPiles[3] = foundations[1];
        allPiles[4] = foundations[2];
        allPiles[5] = foundations[3];
        allPiles[6] = tableau[0];
        allPiles[7] = tableau[1];
        allPiles[8] = tableau[2];
        allPiles[9] = tableau[3];
        allPiles[10] = tableau[4];
        allPiles[11] = tableau[5];
        allPiles[12] = tableau[6];
        return allPiles;
    }

}
