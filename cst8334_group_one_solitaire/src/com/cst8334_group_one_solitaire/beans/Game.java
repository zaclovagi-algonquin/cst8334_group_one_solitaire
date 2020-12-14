package com.cst8334_group_one_solitaire.beans;


import com.cst8334_group_one_solitaire.commands.*;
import com.cst8334_group_one_solitaire.database.ScoreManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

public class Game {


    public static Board board;
    private final CommandInvoker commandInvoker;
    private static final Game INSTANCE = new Game();
    private int score;
    private int gameMode = 0;
    private boolean trackScore;
    private final String gameSession;
    private String lastMove;
    
    private int mouseX;
    private int mouseY;
    private boolean shiftKey;
    private boolean controlKey;
    private Hand hand;
    private int clickY;

    private boolean manualMove = false;
    
    public static final String autoHowTo = "Automove: Click a card! If there is a valid move, the game will move the card or stack automatically!";
    public static final String manualHowTo = "Manual Move: Cards will follow the mouse. Clicking a stack will have it follow the mouse."
            + "\nPress SHIFT and click to pick bottom of stack only\n"
            + "Press SPACE when carrying a stack to drop the top card back down";
    
    private Game() {
        mouseX = 0;
        mouseY = 0;
        clickY=0;
        gameSession = UUID.randomUUID().toString();
        System.out.println("Session ID: " + gameSession);
        commandInvoker = CommandInvoker.getInstance();
        GameGraphics.loadGraphics(this);
        GameGraphics.initializeWindow();
        startGame();
    }

    public static Game getInstance() {
        return INSTANCE;
    }

    public void startGame() {
        lastMove = "";
        hand= null;
        shiftKey = false;
        board = new Board(13); //manually declaring pile count since it is still hard coded
        board.shuffle();
        GameGraphics.getPanel().repaint();
        if (gameMode == 1) {
            if (trackScore) {
                try {
                    score = ScoreManager.fetchScore();
                } catch (SQLException e) {
                    System.err.println(e);
                }
            } else {
                score = -52;
            }

        } else {
            score = 0;
        }
    }


    public void select(int x, int y) {
        System.out.println("mouse clicked at: " + x + ", " + y);
        y-=40;
        clickY=y;
        if (manualMove) {
            manualCardMove(x, y);
        }else { //auto move
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
        }
        
        
        if(board.foundationFull()) {
        	System.out.println("Game over! You won!!");
        	if (trackScore) {
                try {
                    ScoreManager.insertScore();
                } catch (SQLException e) {
                    System.err.println(e);
                }
            }
        }
    }

    private void tableauClicked(int i) {
        System.out.println("Tableau[" + i + "] clicked");
        
        if (manualMove == false) {
            //auto move
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
        } else {
            //manual move
            if (hand == null) {
                //no card
                if (!board.tableau[i].isEmpty()) {
                    if (board.tableau[i].inspectTop().isFaceUp()) {
                        //card is face up, grab card.
                        if (shiftKey) {
                            hand = new Hand(board.tableau[i].inspectTop(), board.tableau[i]);
                        } else {
                            //check for stack move
                            Card tempCard = board.tableau[i].inspectTop();
                            if (board.tableau[i].pile().indexOf(tempCard) != board.tableau[i].indexOfBottomFaceUp() && board.tableau[i].getType() != "talon") 
                            {
                                CardPile movable = new CardPile(0,0,0,0);
                                Stack<Card> tempPile = (Stack<Card>) board.tableau[i].pile().clone();
                                while(!tempPile.isEmpty()) {
                                    if (!tempPile.peek().isFaceUp()) {
                                        break;
                                    }
                                    movable.addCard(tempPile.pop(), false);
                                }
                                hand = new Hand(movable, board.tableau[i]);
                                return;
                            }else {
                                hand = new Hand(board.tableau[i].inspectTop(), board.tableau[i]);
                                return;  
                            }
                        }
                        
                    } else {
                        //card is face down, flip card
                        Card cardToFlip = board.tableau[i].inspectTop();
                        commandInvoker.executeOperation(new FlipCard(this, cardToFlip, 5));
                    }
                }
                
            } else { //card(s) in hand
                if (!hand.isStack()) 
                { //hand is one card
                    if (board.tableau[i].canReceiveCard(hand.getCard())) {
                        if (hand.getInPile() == board.talon) { // if card from deck add 5 points
                            commandInvoker.executeOperation(new MoveCard(this, hand.getInPile(), board.tableau[i], 5));
                            hand = null;
                        } else { // card from other row add 3 points
                            commandInvoker.executeOperation(new MoveCard(this, hand.getInPile(), board.tableau[i], 3));
                            hand = null;
                        }
                    }
                } 
                else {
                    //hand is a stack
                    if (board.tableau[i].canReceiveCard(hand.getStack().inspectTop())) {
                        commandInvoker.executeOperation(new MoveStackOfCards(this, hand.getStack(), hand.getInPile(), board.tableau[i], 5));
                        hand = null;
                    }
                }
                
            }
        }


    }//end of method

    private void talonClicked() {
        System.out.println("Talon pile clicked");
        if (manualMove == false) {
            //auto move
            if (!board.talon.isEmpty()) {
                if (checkForMove(board.talon, "talon"))
                    return;
            } 
        } else {
            //manual move
            if (!board.talon.isEmpty()) {
                if (hand == null) {
                    hand = new Hand(board.talon.inspectTop(), board.talon);
                }
            }
        }
        
    }

    private void stockPileClicked() {
        System.out.println("Deck pile clicked");
        if (board.stockPile.isEmpty()) {
            if (!board.talon.isEmpty()) {
                while (!board.talon.isEmpty()) {
                    addToDeck();

                }
                setLastMove("Return Talon to Stockpile");
            }
        } else {
            commandInvoker.executeOperation(new DrawCard(this));
            setLastMove("Drew card from Stockpile");
        }
    }
    
    private void foundationClicked(int i) { //only being used for manual move
        if (!board.foundations[i].isEmpty()) {
            if (hand == null) {
                //grab card from foundation if not ace or 2
                if (board.foundations[i].inspectTop().getRank() > 1) {
                    hand = new Hand(board.foundations[i].inspectTop(), board.foundations[i]);
                }
            } else if (!hand.isStack()) 
            {
                Card foundationTop = board.foundations[i].inspectTop();
                if (hand.getCard().getSuit() == foundationTop.getSuit()) {
                    //suit matches, check rank
                    if (hand.getCard().getRank() - foundationTop.getRank() == 1) {
                        if (gameMode == 1) {
                            commandInvoker.executeOperation(new MoveCard(this, hand.getInPile(), board.foundations[i], 5));
                            hand = null;
                        } else {
                            commandInvoker.executeOperation(new MoveCard(this, hand.getInPile(), board.foundations[i], 10));
                            hand = null;
                        }
                    }
                }
            }
        } else {
            if (hand != null) {
                if (!hand.isStack()) {
                    if (hand.getCard().getRank() == 0) {
                        if (gameMode == 1) {
                            commandInvoker.executeOperation(new MoveCard(this, hand.getInPile(),board.foundations[i], 5));
                            hand = null;
                        } else {
                            commandInvoker.executeOperation(new MoveCard(this, hand.getInPile(),board.foundations[i], 10));
                            hand = null;
                        }
                    } 
                }
                
            }
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
                        if (gameMode == 1) {
                            commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 5));
                        } else {
                            commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 10));
                        }
                        return true;
                    }
                } else { //foundations aren't empty, check if card matches suit and rank
                    Card foundationTop = toPile.inspectTop();
                    if (tempCard.getSuit() == foundationTop.getSuit()) {
                        //suit matches, check rank
                        if (tempCard.getRank() - foundationTop.getRank() == 1) {
                            if (gameMode == 1) {
                                commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 5));
                            } else {
                                commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 10));
                            }
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
                        if (toPile.canReceiveCard(fromPile.inspectTop())) {
                            if (fromPile == board.talon) { // if card from deck add 5 points
                                commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 5));
                            } else { // card from other row add 3 points
                                commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 3));
                            }


                            return true;
                        }
                        /*
                        if (checkCardColor(tempCard, tableauTop)) 
                        { //color is opposite
                            if (tableauTop.getRank() - tempCard.getRank() == 1) {
                                if (fromPile == board.talon) { // if card from deck add 5 points
                                    commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 5));
                                } else { // card from other row add 3 points
                                    commandInvoker.executeOperation(new MoveCard(this, fromPile, toPile, 3));
                                }


                                return true;
                            }
                        }*/
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
    
    public void manualCardMove(int x, int y) {
        CardPile clickedPile = null;
        
        //check if a pile was clicked on
        for (int i = 0; i < board.allPiles().length; i++) {
            if (board.allPiles()[i].isInArea(x, y)) {
                clickedPile = board.allPiles()[i];
                break;
            }
        }
        
        if (clickedPile == null) {
            //click was on nothing. 
            return;
        } 
        else if (hand != null && clickedPile == hand.getInPile()) {
            hand = null;
        }
        else if (clickedPile.getType("stockpile")) {
            //stockpile was clicked
            if (hand == null) {
                stockPileClicked();
            }
        } 
        else if (clickedPile.getType("talon")) {
            //talon was clicked
            talonClicked();
            
        } 
        else if (clickedPile.getType("tableau")) {
            //a tableau was clicked
            int index = 0;
            for (int i = 0; i < board.tableau.length; i++) {
                if (clickedPile == board.tableau[i]) {
                    index = i;
                    break;
                }
            }
            tableauClicked(index);
            return;
            
        } 
        else if (clickedPile.getType("foundation")) {
            //a foundation was clicked
            int index = 0;
            for (int i = 0; i < board.foundations.length; i++) {
                if (clickedPile == board.foundations[i]) {
                    index = i;
                    break;
                }
            }
            foundationClicked(index);
        } 
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
        setLastMove(card.getName() + " from " + getPileName(fromPile) + " to " + getPileName(toPile));

    }

    /**
     * Used to move a stack of cards to another tableau
     * @param fromPile The tableau cards are coming from
     * @param toPile The tableau the cards are going to
     */
    public void moveStack(CardPile fromPile, CardPile toPile) {
        CardPile movable = new CardPile(0,0,0,0);
        List<String> cardNames = new ArrayList<String>();
        setLastMove("");
        while (!fromPile.isEmpty()) {
            if (!fromPile.inspectTop().isFaceUp()) {
                break;
            }
            movable.addCard(fromPile.pile().pop(), false);
            cardNames.add(movable.inspectTop().getName());
            System.out.println(movable.pile().size());
        }
        while(!movable.isEmpty()) {
            toPile.addCard(movable.pile().pop(), true);
        }
        for (int i = 0; i < cardNames.size(); i ++) {
            if (i != 0) {
                lastMove += ", ";
                
            }
            lastMove += cardNames.get(i);
        }
        lastMove += " from "  +getPileName(fromPile) + " to " + getPileName(toPile);
    }
    
    public void moveStack(CardPile fromPile, CardPile toPile, CardPile moveStack) {
        setLastMove("");
        List<String> cardNames = new ArrayList<String>();
        CardPile movable = new CardPile(0,0,0,0);
        for (int i = 0; i < moveStack.size(); i++) {
            Card card = fromPile.pile().pop();
            cardNames.add(card.getName());
            movable.addCard(card, false);
            
        }
        while (!movable.isEmpty()) {
            toPile.addCard(movable.pile().pop(), true);
        }
        
        for (int i = 0; i < cardNames.size(); i ++) {
            if (i != 0) {
                lastMove += ", ";
                
            }
            lastMove += cardNames.get(i);
        }
        lastMove += " from "  +getPileName(fromPile) + " to " + getPileName(toPile);
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
        setLastMove("Flipped over the " + cardToFlip.getName());
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

    public String getGameSession() {return gameSession;}

    // if vegas return true
    // TODO: Write a proper method to select a game mode
    public void gameModeTest(String mode) {
        if (mode.toLowerCase().equals("vegas")) {
            gameMode = 1;
        } else {
            gameMode = 0;
        }
    }
    
    public String getGameModeString() {
        if (gameMode == 1) {
            return "Vegas Rules";
        } else return "Normal Rules";
    }
    
    public void setLastMove(String string) {
        lastMove = string;
    }
    public String getLastMove() {
        return lastMove;
    }
    
    public void scoreTracking(boolean track) {
        trackScore = track;
    }
    
    public void manualMove(boolean manual) {
        manualMove = manual;
        if (manualMove == false) {
            hand = null;
        }
    }
    
    public boolean manualMove() {
        return manualMove;
    }
    
    public String getPileName(CardPile pile) {
        String pileName = "";
        
        if (pile == board.stockPile) {
            pileName = "Stockpile";
        } else if (pile == board.talon) {
            pileName = "Talon";
        } 
        else {
            for (int i = 0; i < board.foundations.length; i++) {
                if (pile == board.foundations[i]) {
                    pileName = "Foundation " + (i+1);
                    break;
                }
            }
            for (int i = 0; i < board.tableau.length; i++) {
                if (pile == board.tableau[i]) {
                    pileName = "Tableau " + (i+1);
                    break;
                }
            }
        }
        
        return pileName;
    }

    public void setMouse(int x, int y) {
        mouseX = x;
        mouseY = y;
    }
    
    public int getMouseX() {
        return mouseX;
    }
    
    public int getMouseY() {
        return mouseY;
    }
    
    public Hand getHand() {
        return hand;
        
    }
    public void emptyHand() {
        hand = null;
    }
    
    public void setShiftKey(boolean pressed) {
        shiftKey = pressed;
    }
    public boolean isShiftKeyPressed() {
        return shiftKey;
    }
    
    public void setControlKey(boolean pressed) {
        controlKey = pressed;
    }
    public boolean isControlKeyPressed() {
        return controlKey;
    }
    
    public void dropCardFromStack() {
        if (manualMove) {
            if (hand == null) {
                return;
            }
            if (hand.isStack()) {
                hand.getStack().removeTop(false);
                if (hand.getStack().isEmpty()) {
                    hand = null;
                } else if (hand.getStack().size() == 1) {
                    hand = new Hand(hand.getStack().getCard(0), hand.getInPile());
                }
            } else if (hand.getCard() != null) {
                hand = null;
            }
        }
    }

    public String getHelpDialog() {
        if (manualMove) {
            return manualHowTo;
            
        } else return autoHowTo;
    }

}
