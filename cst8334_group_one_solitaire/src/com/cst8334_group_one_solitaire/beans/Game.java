package com.cst8334_group_one_solitaire.beans;


import java.awt.Color;

import com.cst8334_group_one_solitaire.commands.CommandInvoker;
import com.cst8334_group_one_solitaire.commands.DrawCard;
import com.cst8334_group_one_solitaire.commands.FlipCard;
import com.cst8334_group_one_solitaire.commands.MoveCard;

public class Game {
    
    
    public static Board board;
    private final CommandInvoker commandInvoker;
    private static final Game INSTANCE = new Game();
    
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
        commandInvoker.restart();
    }
    
    public void select(int x, int y) {
        System.out.println("mouse clicked at: " + x + ", " + y);
        //deck check
        if (board.stockPile.isInArea(x, y)) {
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
            
            //talon check
        } else if (board.talon.isInArea(x, y)) {
            System.out.println("Talon pile clicked");
            if (!board.talon.isEmpty()) {
                checkForMove(board.talon);
            }
        } else {
            //tableau check
            for (int i = 0; i < 7; i ++) {
                if (board.tableau[i].isInArea(x, y)) {
                    System.out.println("Tableau[" + i + "] clicked");
                    if (!board.tableau[i].isEmpty()) {
                        if (board.tableau[i].inspectTop().isFaceUp()) {
                            System.out.println(board.tableau[i].inspectTop().toString());
                            if (checkForMove(board.tableau[i]))
                            return;
                        } else { 
                        	Card cardToFlip = board.tableau[i].inspectTop();
                        	commandInvoker.executeOperation(new FlipCard(this, cardToFlip));
                        	}
                    } else {
                        if (checkForMove(board.tableau[i]))
                        return;
                    }
                }
            }
            //foundation check
            for (int i = 0; i < board.foundations.length; i++) {
                if (board.foundations[i].isInArea(x, y)) {
                    //do foundation rules
                    System.out.println("Foundation[" + i + "] clicked");
                    return;
                }
            }
        }
    }
    
    private boolean checkForMove(CardPile fromPile) {
        if (!fromPile.isEmpty()) {
            Card tempCard = fromPile.inspectTop();
            System.out.println("Clicked on: " + tempCard.toString());
            
            //check foundations
            for (int i = 0; i < 4; i++) {
                CardPile toPile = board.foundations[i];
                if (toPile.isEmpty()) {
                    if (tempCard.getRank() == 0) { //is ace
                    	commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile));
                        return true;
                    }
                } else { //foundations aren't empty, check if card matches suit and rank
                    Card foundationTop = toPile.inspectTop();
                    if (tempCard.getSuit() == foundationTop.getSuit()) {
                        //suit matches, check rank
                        if (tempCard.getRank() - foundationTop.getRank() == 1) {
                        	commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile));
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
                                commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile));
                                return true;
                            }
                        }
                    } else { //pile is empty, only kings can move.
                        if (tempCard.getRank() == 12) {
                        	commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile));
                            return true;
                        }
                    }
                    
                }//end of piles !=
            } //end of tableau
           
        } //end of !fromPile.isEmpty()
        
        return false;
        
    }
    
    private static boolean checkCardColor(Card card1, Card card2) {
        Color color1;
        Color color2;
        if (card1.getSuit() == 1 || card1.getSuit() == 3) {
            color1 = Color.BLACK;
        } else {
            color1 = Color.RED;
        }
        if (card2.getSuit() == 1 || card2.getSuit() == 3) {
            color2 = Color.BLACK;
        } else {
            color2 = Color.RED;
        }

        return color1 != color2;
    }

	public void moveCard(CardPile fromPile, CardPile toPile) {
        
		Card card = fromPile.inspectTop();
        boolean retract = true;
        boolean expand = true;
        if (fromPile == board.talon || fromPile == board.stockPile) {
            retract = false;
        } if (toPile == board.stockPile || toPile == board.talon || 
                toPile == board.foundations[0] || toPile == board.foundations[1] || toPile == board.foundations[2] || toPile == board.foundations[3] ) {
            expand = false;
        }
        toPile.addCard(card, expand);
        fromPile.removeTop(retract);
	}
	
	
	public void drawFromDeck() {
		Card card = board.stockPile.removeTop(false);
        card.flip();
        board.talon.addCard(card, false);
	}
	
	public void addToDeck() {
		Card card = board.talon.removeTop(false);
        card.flip();
        board.stockPile.addCard(card, false);
	}
	
	public void flipCard(Card cardToFlip) {
		cardToFlip.flip();
	}
	

}
