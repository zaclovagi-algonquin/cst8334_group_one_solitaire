package com.cst8334_group_one_solitaire.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class Deck {
    
    public static ArrayList<Card> DECK;
    
    public static void initialization() {
        DECK = new ArrayList<Card>();
        //Create cards by suit
        for (int i = 0; i < 4; i++) 
        {
            for (int j = 0; j < 13; j++) 
            {
                Card card = new Card(i, j);
                DECK.add(card);
                System.out.println(card.toString());
            }//for loop for values
        }
    }
    
    public static Stack<Integer> shuffleDeck(){
       Stack<Integer> shuffledDeck = new Stack<Integer>();
        for (int i = 0; i < 52; i++) {
            shuffledDeck.add(i);
        }
        System.out.println("Shuffling deck...");
        Collections.shuffle(shuffledDeck);
        System.out.println(Arrays.toString(shuffledDeck.toArray()));
        
        return shuffledDeck;
    }
    
    public static Card getCard(int index) {
        return DECK.get(index);
    }

}
