package com.cst8334_group_one_solitaire.beans;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameGraphics {
    
    //image containers for the cards
    static public Image hearts[];
    static public Image spades[];
    static public Image clubs[];
    static public Image diamonds[];
    static public Image cardBack;
    
    private static JFrame window;
    private static JPanel gamePanel;
    
    public static void loadGraphics(Game game) {
      //load cardBack image
        URL url = game.getClass().getResource("/card_back.png");
        if (url == null) {
            System.out.println("Could not find image");
        }
        else {
            cardBack = new ImageIcon(game.getClass().getResource("/card_back.png")).getImage();
            cardBack = cardBack.getScaledInstance(Card.WIDTH, Card.HEIGHT, java.awt.Image.SCALE_SMOOTH);
          //set window icon to card back
            
        }
        //load all other images
        String resHearts[] = {"/heart/heart_a.png", "/heart/heart_2.png", "/heart/heart_3.png", "/heart/heart_4.png", 
                "/heart/heart_5.png", "/heart/heart_6.png", "/heart/heart_7.png", "/heart/heart_8.png", "/heart/heart_9.png", 
                "/heart/heart_10.png", "/heart/heart_j.png", "/heart/heart_q.png", "/heart/heart_k.png"};
        
        String resSpades[] = {"/spade/spade_a.png", "/spade/spade_2.png", "/spade/spade_3.png", "/spade/spade_4.png", 
                "/spade/spade_5.png", "/spade/spade_6.png", "/spade/spade_7.png", "/spade/spade_8.png", "/spade/spade_9.png", 
                "/spade/spade_10.png", "/spade/spade_j.png", "/spade/spade_q.png", "/spade/spade_k.png"};
        
        String resClubs[] = {"/club/club_a.png", "/club/club_2.png", "/club/club_3.png", "/club/club_4.png", 
                "/club/club_5.png", "/club/club_6.png", "/club/club_7.png", "/club/club_8.png", "/club/club_9.png", 
                "/club/club_10.png", "/club/club_j.png", "/club/club_q.png", "/club/club_k.png"};
        
        String resDiamonds[] = {"/diamond/diamond_a.png", "/diamond/diamond_2.png", "/diamond/diamond_3.png", "/diamond/diamond_4.png", 
                "/diamond/diamond_5.png", "/diamond/diamond_6.png", "/diamond/diamond_7.png", "/diamond/diamond_8.png", "/diamond/diamond_9.png", 
                "/diamond/diamond_10.png", "/diamond/diamond_j.png", "/diamond/diamond_q.png", "/diamond/diamond_k.png"};
        
        hearts = new Image[13];
        clubs = new Image[13];
        spades = new Image[13];
        diamonds = new Image[13];
        
        for (int i = 0; i < 4; i++) 
        {
            for (int j = 0; j < resHearts.length; j++) 
            {
                if (i == 0) 
                { //resHearts
                    URL urltemp = game.getClass().getResource(resHearts[j]);
                    if (urltemp == null) 
                    {
                        System.out.println("Could not find image: " + resHearts[j]);
                    } else {
                        Image image = new ImageIcon(urltemp).getImage();
                        image = image.getScaledInstance(Card.WIDTH, Card.HEIGHT, java.awt.Image.SCALE_SMOOTH);
                        hearts[j] = image;
                    }
                } else if (i == 1) 
                { //resSpades
                    URL urltemp = game.getClass().getResource(resSpades[j]);
                    if (urltemp == null) 
                    {
                        System.out.println("Could not find image: " + resSpades[j]);
                    } else {
                        Image image = new ImageIcon(urltemp).getImage();
                        image = image.getScaledInstance(Card.WIDTH, Card.HEIGHT, java.awt.Image.SCALE_SMOOTH);
                        spades[j] = image;
                    }
                } else if (i == 2) 
                { //resClubs
                    URL urltemp = game.getClass().getResource(resClubs[j]);
                    if (urltemp == null) 
                    {
                        System.out.println("Could not find image: " + resClubs[j]);
                    } else {
                        Image image = new ImageIcon(urltemp).getImage();
                        image = image.getScaledInstance(Card.WIDTH, Card.HEIGHT, java.awt.Image.SCALE_SMOOTH);
                        clubs[j] = image;
                    }
                } else if (i == 3) 
                { //resDiamonds
                    URL urltemp = game.getClass().getResource(resDiamonds[j]);
                    if (urltemp == null) 
                    {
                        System.out.println("Could not find image: " + resDiamonds[j]);
                    } else {
                        Image image = new ImageIcon(urltemp).getImage();
                        image = image.getScaledInstance(Card.WIDTH, Card.HEIGHT, java.awt.Image.SCALE_SMOOTH);
                        diamonds[j] = image;
                    }
                }
            }//for loop for values
        } 
    }
    
    public static void initializeWindow() {
        window = new Window();
        window.setIconImage(cardBack);
        gamePanel = new GamePanel();
        gamePanel.setLayout(null);
        gamePanel.setBackground(new Color(0,120,0));
        window.add(gamePanel);
        window.setVisible(true);
    }
    
    public static void redraw() {
        
        gamePanel.repaint();
        window.repaint();
        window.validate();
    }
    
    public static JPanel getPanel() {
        return gamePanel;
    }
}
