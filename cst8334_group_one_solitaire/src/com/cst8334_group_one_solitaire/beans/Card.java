package com.cst8334_group_one_solitaire.beans;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;

public class Card {

    //static variables for dimensions
    public final static int WIDTH = 64;
    public final static int HEIGHT = 96;

    //int values to hold static suit type
    public final static int HEART = 0;
    public final static int SPADE = 1;
    public final static int DIAMOND = 2;
    public final static int CLUB = 3;

    public JLabel graphic;
    public JLabel backGraphic;

    //values to hold instance rank, suit and faceUp
    private boolean faceUp;
    private final int rank;
    private final int suit;

    //constructor
    public Card(int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        faceUp = false;
    }

    public void draw(Graphics g, int x, int y) {

        if (isFaceUp()) {

            if (getSuit() == HEART) {
                g.drawImage(GameGraphics.hearts[rank], x, y, WIDTH, HEIGHT, null);
            } else if (getSuit() == SPADE) {
                g.drawImage(GameGraphics.spades[rank], x, y, WIDTH, HEIGHT, null);
            } else if (getSuit() == DIAMOND) {
                g.drawImage(GameGraphics.diamonds[rank], x, y, WIDTH, HEIGHT, null);
            } else if (getSuit() == CLUB) {
                g.drawImage(GameGraphics.clubs[rank], x, y, WIDTH, HEIGHT, null);
            }
        } else {
            g.drawImage(GameGraphics.cardBack, x, y, WIDTH, HEIGHT, null);
        }

    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public int getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }

    public void flip() {
        faceUp = !faceUp;
    }

    public Color getColor() {
        if (suit == HEART || suit == DIAMOND) {
            return Color.RED;
        } else {
            return Color.black;
        }
    }
    
    public String getName() {
        String cardName = "";
        String suitName = "";
        String rankName = "";
        if (rank != 0 && rank <= 9) {
            rankName = ""+ (rank+1);
        } else switch(rank) {
            case 10:
                rankName = "Jack";
                break;
            case 11:
                rankName = "Queen";
                break;
            case 12: 
                rankName = "King";
                break;
            case 0:
                rankName = "Ace";
                break;
        }
        switch(suit) {
            case 0:
                suitName = "Hearts";
                break;
            case 1:
                suitName = "Spades";
                break;
            case 2:
                suitName = "Diamonds";
                break;
            case 3:
                suitName = "Clubs";
                break;
        }
        cardName = rankName + " of " + suitName;
        return cardName;
    }

    @Override
    public String toString() {
        return "Card [faceUp=" + faceUp + ", rank=" + rank + ", suit=" + suit + "]";
    }


}
