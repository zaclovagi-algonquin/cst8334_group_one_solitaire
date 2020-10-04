package com.cst8334_group_one_solitaire.beans;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Window extends JFrame{
    private static final long serialVersionUID = 1L;
    
    private class RestartButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Game.restart();
            repaint();
            
        }
    }
    
    /* undo button feature disabled in this build
    private class UndoButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
        
        
    } */
    
    private class MouseKeeper extends MouseAdapter{
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            Game.select(x, y);
        } 
        
    }
    
    public Window() {
        addWindowListener(new WindowAdapter(){  
            public void windowClosing(WindowEvent e) {  
                dispose();  
            }  
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(1366, (int)screenSize.getHeight()-100);
        getContentPane().setBackground(new Color(0,120,0));
        setTitle("Solitaire Game Java - Zac, David, Theodore, Sebastien");
        setResizable(true);
        addMouseListener(new MouseKeeper());
        Button restart = new Button("Restart");
        //Button undo = new Button("Undo");
        restart.addActionListener(new RestartButtonListener());
        restart.setBackground(new Color(160,82,45)); //change color of button
        //undo.addActionListener(new UndoButtonListener());
        //undo.setBackground(new Color(160,82,45)); //change color of button    undo button feature disabled in this build
        add("South", restart);
        //add("North", undo);
        setVisible(true);
    }
    
}
