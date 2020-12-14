package com.cst8334_group_one_solitaire.beans;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.util.Enumeration;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    Font stringFont = new Font("SansSerif", Font.PLAIN, 25);
    private final boolean debug = false;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void paintComponent(Graphics g) {
        g.setFont(stringFont);
        super.paintComponent(g);
        if(!Game.board.foundationFull()) {
        	g.drawString("You have " + (52 - Game.board.getFoundationCount()) + " cards to put in the foundation", /*375*/ (this.getWidth()/3)+50, 35);
        }else {
        	g.drawString("CONGRATULATIONS, you've won!!", 375, 35);

        }
        g.drawString(Game.getInstance().getGameModeString(), this.getWidth()-160, 35);
        g.drawString("Score: " + Game.getInstance().getScore(), 20, 35);
        if (Game.getInstance().getLastMove() != "") {
            g.drawString("Your last move was: " + Game.getInstance().getLastMove(), 5, this.getHeight()- 25); 
        }
        
        
        for (int i = 0; i < 13; i++) {
            g.setColor(Color.black);
            CardPile cardPile = Game.board.allPiles()[i];
            if (cardPile.isEmpty()) {
                g.drawRect(cardPile.getGameSpace().x, Game.board.allPiles()[i].getGameSpace().y,
                        Card.WIDTH, Card.HEIGHT);
            } else {
                if (i == 0) { //deck
                    Card card = cardPile.inspectTop();
                    card.draw(g, cardPile.getGameSpace().x, cardPile.getGameSpace().y);
                } else if (i == 1) { //talon
                    Card card = cardPile.inspectTop();
                    card.draw(g, cardPile.getGameSpace().x, cardPile.getGameSpace().y);
                } else if (i > 1 && i < 6) { //foundations
                    Card card = cardPile.inspectTop();
                    card.draw(g, cardPile.getGameSpace().x, cardPile.getGameSpace().y);
                } else if (i > 5) {
                    int tableY = cardPile.getGameSpace().y;

                    for (Enumeration<Card> e = cardPile.pile().elements(); e.hasMoreElements(); ) {
                        Card card = (Card) e.nextElement();
                        card.draw(g, cardPile.getGameSpace().x, tableY);
                        tableY += 40;

                    }
                }
            }
        }
        if (debug) {
            for (int i = 0; i < Game.board.pileCount; i++) {
                CardPile pile = Game.board.allPiles()[i];
                g.setColor(Color.red);
                g.drawRect(pile.getGameSpace().x, pile.getGameSpace().y, pile.getGameSpace().width(), pile.getGameSpace().height());
            }
        }
        
        //draw card in hand
      if (Game.getInstance().getHand() != null) {
          if (Game.getInstance().getHand().isStack()) {
              CardPile stack = Game.getInstance().getHand().getStack();
              int occurences = 0;
              for (int i = stack.size()-1; i > -1; i--) {
                  
                  Card card = stack.getCard(i);
                  Graphics2D g2 = (Graphics2D)g;
                  float opacity = 0.5f;
                  g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                  g2.drawImage(GameGraphics.getCardFaceImage(card.getSuit(), card.getRank()), 
                          Game.getInstance().getMouseX()-40, Game.getInstance().getMouseY()-40+(40*occurences), Card.WIDTH, Card.HEIGHT, null);
                  occurences++;
              }
              
          }else {
              Card card = Game.getInstance().getHand().getCard();
              Graphics2D g2 = (Graphics2D)g;
              float opacity = 0.5f;
              g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
              g2.drawImage(GameGraphics.getCardFaceImage(card.getSuit(), card.getRank()), 
                      Game.getInstance().getMouseX()-40, Game.getInstance().getMouseY()-40, Card.WIDTH, Card.HEIGHT, null);
          }
          GameSpace pileGameSpace = Game.getInstance().getHand().getInPile().getGameSpace();
          Graphics2D g2 = (Graphics2D)g;
          g2.setStroke(new BasicStroke(3));
          g2.setColor(Color.BLACK);
          int height = Card.HEIGHT+6;
          if (Game.getInstance().getHand().getInPile().getType("tableau")) {
              height = (Game.getInstance().getHand().getInPile().size()*40)+58;
          }
          g2.drawRect(pileGameSpace.x-3, pileGameSpace.y-3, pileGameSpace.width()+6, height);
          
              
      }
        

        //System.out.println("painting");
        repaint();
        setLayout(new GridLayout());

    }

}
