package com.cst8334_group_one_solitaire.beans;

import java.util.EmptyStackException;
import java.util.Stack;

public class CardPile {
    //these values store the coordinates of the pile
    private final GameSpace gameSpace;
    private final Stack<Card> pile; //this is the stack of cards in the pile
    private String type;

    public CardPile(int x, int y, int xDim, int yDim) {
        gameSpace = new GameSpace(x, y, xDim, yDim);
        pile = new Stack<Card>();
    }

    public final Card inspectTop() {
        return (Card) pile.peek();
    }

    public boolean isEmpty() {
        return pile.empty();
    }

    public Card removeTop(boolean retract) {
        try {
            if (retract) {
                gameSpace.recalculateBounds(pile.size() - 1);
            }
            return (Card) pile.pop();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    public boolean isInArea(int x, int y) {
        return gameSpace.isInArea(x, y);
    }

    public void addCard(Card card, boolean expand) {
        pile.push(card);
        if (expand) {
            gameSpace.recalculateBounds(pile.size());
        }

    }

    public int size() {
        return pile.size();
    }


    public Card getCard(int index) {
        return pile.get(index);
    }

    public Stack<Card> pile() {
        return pile;
    }

    public GameSpace getGameSpace() {
        return gameSpace;
    }

    public int indexOfBottomFaceUp() {

        for(int i = 0; i < pile.size(); i++) {
            if(pile.get(i).isFaceUp()) {
                System.out.println("indexOfBottomFaceUp = " + i);
                return i;
            }
        }
        System.out.println("indexOfBottomFaceUp = " + -1);
        return -1;
    }

    // TODO: Not for final version, This check only applies to tableau's.
    public boolean canReceiveCard(Card card) {
        if(isEmpty()) {
            //only a king ranked card can be received in an empty slot
            return card.getRank() == 12; //12 represents the king rank
        }else if (inspectTop().isFaceUp()){
            //only accept a card that is of opposite color and 1 LESS than topmost card
            Card topCard = inspectTop();
            return card.getColor() != topCard.getColor() && card.getRank() == topCard.getRank() - 1;
        }
        return false;
    }
    public CardPile setType(String type) {
        this.type = type;
        return this;
    }
    public String getType() {
        return type;
    }
    
    public boolean getType(String string) {
        if (type == string) {
            return true;
        } else return false;
    }
}
