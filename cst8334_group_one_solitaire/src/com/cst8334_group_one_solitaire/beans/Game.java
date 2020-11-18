package com.cst8334_group_one_solitaire.beans;


import com.cst8334_group_one_solitaire.commands.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Game {


    public static Board board;
    private final CommandInvoker commandInvoker;
    private static final Game INSTANCE = new Game();
    private int score = 0;

    private Game() {
        commandInvoker = CommandInvoker.getInstance();
        GameGraphics.loadGraphics(this);
        startGame();

    }

    public static Game getInstance() {
        return INSTANCE;
    }

    private void startGame() {
        board = new Board(13); //manually declaring pile count since it is still hard coded
        GameGraphics.initializeWindow();
        board.shuffle();
        // GameGraphics.drawCards();
        GameGraphics.getPanel().repaint();
    }

    public void restart() {
        board = new Board(13);
        board.shuffle();
        GameGraphics.getPanel().repaint();
        score = 0;
    }

    public void select(int x, int y) {
        System.out.println("mouse clicked at: " + x + ", " + y);
        //deck check
        if (board.stockPile.isInArea(x, y)) {
            stockPileClicked();
            //talon check
        } else if (board.talon.isInArea(x, y)) {
            talonClicked();
        } else {
            //tableau check
            for (int i = 0; i < 7; i++) {
                if (board.tableau[i].isInArea(x, y)) {
                    tableauClicked(i);
                }
            }
            //removed foundation check
        }
        
        if(board.foundationFull()) {
        	System.out.println("Game over! You won!!");
        }
    }

    private void tableauClicked(int i) {
        System.out.println("Tableau[" + i + "] clicked");

        if (!board.tableau[i].isEmpty()) {
            if (board.tableau[i].inspectTop().isFaceUp()) {
                System.out.println(board.tableau[i].inspectTop().toString());
                if (checkForMove(board.tableau[i], "tableau"))
                    return;
            } else {
                Card cardToFlip = board.tableau[i].inspectTop();
                commandInvoker.executeOperation(new FlipCard(this, cardToFlip, 5));
            }
        } else {
            if (checkForMove(board.tableau[i], "tableau"))
                return;
        }
    }

    private void talonClicked() {
        System.out.println("Talon pile clicked");
        if (!board.talon.isEmpty()) {
            if (checkForMove(board.talon, "talon"))
                return;
        }
    }

    private void stockPileClicked() {
        System.out.println("Deck pile clicked");
        if (board.stockPile.isEmpty()) {
            if (!board.talon.isEmpty()) {
                while (!board.talon.isEmpty()) {
                    addToDeck();

                }
            }
        } else {
            commandInvoker.executeOperation(new DrawCard(this));
        }
    }

    private boolean checkForMove(CardPile fromPile, String from) {
        if (!fromPile.isEmpty()) { // if the from pile is not empty
            Card tempCard = fromPile.inspectTop();
            System.out.println("Clicked on: " + tempCard.toString());

            //check foundations
            for (int i = 0; i < 4; i++) {
                CardPile toPile = board.foundations[i];
                if (toPile.isEmpty()) {
                    if (tempCard.getRank() == 0) { // is ace
                        commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 10));
                        return true;
                    }
                } else { //foundations aren't empty, check if card matches suit and rank
                    Card foundationTop = toPile.inspectTop();
                    if (tempCard.getSuit() == foundationTop.getSuit()) {
                        //suit matches, check rank
                        if (tempCard.getRank() - foundationTop.getRank() == 1) {
                            commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 10));
                            return true;
                        }
                    }
                }
            }//end of foundations

            //check tableau
            for (int i = 0; i < 7; i++) {
                CardPile toPile = board.tableau[i];
                if (fromPile != toPile) {
                    if (!toPile.isEmpty()) {
                        Card tableauTop = toPile.inspectTop();
                        if (checkCardColor(tempCard, tableauTop)) { //color is opposite
                            if (tableauTop.getRank() - tempCard.getRank() == 1) {
                                if (fromPile == board.talon) { // if card from deck add 5 points
                                    commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 5));
                                } else { // card from other row add 3 points
                                    commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 3));
                                }


                                return true;
                            }
                        }
                    } else { //pile is empty, only kings can move.
                        System.out.println("Else if");
                        if (tempCard.getRank() == 12) {
                            if (fromPile == board.talon) { // if card from deck add 5 points
                                commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 5));
                            } else { // card from other row add 3 points
                                commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 3));
                            }
                            return true;
                        }
                    }

                }//end of piles !=
                if (fromPile.pile().indexOf(tempCard) != fromPile.indexOfBottomFaceUp() && from != "talon") {
                	//prevent a card from the talon going through this block

                    if (toPile.canReceiveCard(fromPile.pile().get(fromPile.indexOfBottomFaceUp()))) {
                        commandInvoker.executeOperation(new MoveStackOfCards(this, fromPile, toPile, 5));
                        return true;

                    }
                }
            } //end of tableau
        } //end of !fromPile.isEmpty()

        return false;

    }

    /**
     * Check if card colors are opposite from one another
     *
     * @param card1 First card to check
     * @param card2 Second card to check
     * @return true or false
     */
    private static boolean checkCardColor(Card card1, Card card2) {
        return card1.getColor() != card2.getColor();
    }

    /**
     * Move a card from one pile to another
     *
     * @param fromPile Pile the card is coming from
     * @param toPile   Pile the card is moving to
     */
    public void moveCard(CardPile fromPile, CardPile toPile) {

        Card card = fromPile.inspectTop();
        boolean retract = true;
        boolean expand = true;
        if (fromPile == board.talon || fromPile == board.stockPile) {
            retract = false;
        }
        if (toPile == board.stockPile || toPile == board.talon ||
                toPile == board.foundations[0] || toPile == board.foundations[1] || toPile == board.foundations[2] || toPile == board.foundations[3]) {
            expand = false;
        }
        toPile.addCard(card, expand);
        fromPile.removeTop(retract);

    }

    /**
     * Used to move a stack of cards to another tableau
     * @param fromPile The tableau cards are coming from
     * @param toPile The tableau the cards are going to
     */
    public void moveStack(CardPile fromPile, CardPile toPile) {
        CardPile moveable = new CardPile(0,0,0,0);

        while (!fromPile.isEmpty()) {
            if (!fromPile.inspectTop().isFaceUp()) {
                break;
            }
            moveable.addCard(fromPile.pile().pop(), false);
        }
        while(!moveable.isEmpty()) {
            toPile.addCard(moveable.pile().pop(), true);
        }
    }

    /**
     * Draw a card from stock pile
     */
    public void drawFromDeck() {
        Card card = board.stockPile.removeTop(false);
        card.flip();
        board.talon.addCard(card, false);
    }

    /**
     * Add a card to stock pile
     */
    public void addToDeck() {
        Card card = board.talon.removeTop(false);
        card.flip();
        board.stockPile.addCard(card, false);
    }

    /**
     * Flips a card
     *
     * @param cardToFlip The card to flip
     */
    public void flipCard(Card cardToFlip) {
        cardToFlip.flip();
    }

    /**
     * Sets the score by adding newScore to the value of score
     *
     * @param newScore score to be added
     */
    public void setScore(int newScore) {
        score += newScore;
        System.out.println("Score = " + score);
    }

    /**
     * Method to get the score variable
     *
     * @return int score
     */
    public int getScore() {
        return score;
    }

}
