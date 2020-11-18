package com.cst8334_group_one_solitaire.beans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Enumeration;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private final boolean debug = false;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!Game.board.foundationFull()) {
        	g.drawString("You have " + (52 - Game.board.getFoundationCount()) + " cards to put in the foundation", 100, 35);
        }else {
        	g.drawString("CONGRATULATIONS, you've won!!", 100, 35);

        }

        g.drawString("Score: " + Game.getInstance().getScore(), 25, 35);
        
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

        //System.out.println("painting");
        repaint();
        setLayout(new GridLayout());

    }

}
