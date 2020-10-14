package com.cst8334_group_one_solitaire.beans;

import java.util.EmptyStackException;
import java.util.Stack;

public class CardPile {
    //these values store the coordinates of the pile
    private GameSpace gameSpace;
    private Stack<Card> pile; //this is the stack of cards in the pile
    
    public CardPile(int x, int y, int xDim, int yDim){ 
        gameSpace = new GameSpace(x,y, xDim, yDim);
        pile = new Stack<Card>(); }
    
    public final Card inspectTop() {
        return (Card) pile.peek(); 
        }
    
    public boolean isEmpty() { 
        return pile.empty(); 
        }
    
    public Card removeTop(boolean retract) {
        try {
            if (retract) {
                gameSpace.recalculateBounds(pile.size()-1);
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
    
    public Stack<Card> pile(){
        return pile;
    }
    
    public GameSpace getGameSpace() {
        return gameSpace;
    }


}
