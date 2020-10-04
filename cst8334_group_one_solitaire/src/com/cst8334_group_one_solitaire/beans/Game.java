package com.cst8334_group_one_solitaire.beans;

import java.awt.Color;

public class Game {
    
    
    public static Board board;
    
    public Game() {
        GameGraphics.loadGraphics(this);
        startGame();
    }
    
    private static void startGame() {
        board = new Board(13); //manually declaring pile count since it is still hard coded
        GameGraphics.initializeWindow();
        board.shuffle();
       // GameGraphics.drawCards();
       GameGraphics.getPanel().repaint();
    }
    
    public static void restart() {
        board = new Board(13);
        board.shuffle();
        GameGraphics.getPanel().repaint();
    }
    
    public static void select(int x, int y) {
        System.out.println("mouse clicked at: " + x + ", " + y);
        //deck check
        if (board.stockPile.isInArea(x, y)) {
            if (board.stockPile.isEmpty()) {
                if (!board.talon.isEmpty()) {
                    while (!board.talon.isEmpty()) {
                        Card card = board.talon.removeTop(false);
                        card.flip();
                        board.stockPile.addCard(card, false);
                       // GameGraphics.redraw();
                    }
                }
            } else {
                Card card = board.stockPile.removeTop(false);
                card.flip();
                board.talon.addCard(card, false);
                //GameGraphics.redraw();
            }
            System.out.println("Deck pile clicked");
            return;
            
            //talon check
        } else if (board.talon.isInArea(x, y)) {
            if (!board.talon.isEmpty()) {
                checkForMove(board.talon);
            }
            System.out.println("Talon pile clicked");
            return;
        } else {
            //tableau check
            for (int i = 0; i < board.tableau.length; i ++) {
                if (board.tableau[i].isInArea(x, y)) {
                    System.out.println("Tableau[" + i + "] clicked");
                    if (!board.tableau[i].isEmpty()) {
                        if (board.tableau[i].inspectTop().isFaceUp()) {
                            System.out.println(board.tableau[i].inspectTop().toString());
                            checkForMove(board.tableau[i]);
                        } else { board.tableau[i].inspectTop().flip();}
                    } else {
                        checkForMove(board.tableau[i]);
                    }
                    
                    
                    
                    return;
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
    
    private static void checkForMove(CardPile fromPile) {
        Card tempCard = fromPile.inspectTop();
        
        //check if ace
        if (tempCard.getRank() == 0) {
            for (int i = 0; i < 4; i++) {
                if (board.foundations[i].isEmpty()) {
                    moveCard(fromPile, board.foundations[i]);
                    return;
                    
                }
            }
        }
        //check if card can go in foundations
        for (int i = 0; i < 4; i++) {
            if (!board.foundations[i].isEmpty()) {
                Card foundationCard = board.foundations[i].inspectTop();
                if (tempCard.getSuit() == foundationCard.getSuit()) {
                    if (tempCard.getRank() - foundationCard.getRank() == 1) {
                        moveCard(fromPile, board.foundations[i]);
                        return;
                    }
                }
            }
            
        }
        
        //basic move to tableau spot
        for (int i = 0; i < 7; i++) 
        {
            if (board.tableau[i] == fromPile) {
                return;
            } 
            if (!board.tableau[i].isEmpty()) 
            {
                Card tableauCard = board.tableau[i].inspectTop();
                if (checkCardColor(tempCard, tableauCard)) {
                    
                    if (tableauCard.getRank() - tempCard.getRank() == 1) {
                        System.out.println("different colour");
                        System.out.println("1 less than rank");
                        moveCard(fromPile, board.tableau[i]);
                       return;
                    }
                } 
            } 
            
        }
      //check move king to empty tableau
        for (int i = 0; i < 7; i++) 
        {
            if (board.tableau[i] == fromPile) {
                return;
            } if (board.tableau[i].isEmpty()) 
            {
                if (tempCard.getRank() == 12) {
                    moveCard(fromPile, board.tableau[i]);
                    return;
                }
            } 
            
        }
        
    }
    
    private static void moveCard(CardPile fromPile, CardPile toPile) {
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
    
    private static boolean checkCardColor(Card card1, Card card2) {
        Color color1;
        Color color2;
        if (card1.getSuit() == 1 || card1.getSuit()==3) {
            color1 = Color.BLACK;
        } else color1 = Color.RED;
        if (card2.getSuit() == 1 || card2.getSuit() == 3) {
            color2 = Color.BLACK;
        } else color2 = Color.RED;
        
        if (color1 != color2) {
            return true;
        } else return false;
    }

}
