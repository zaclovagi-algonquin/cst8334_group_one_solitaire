package com.cst8334_group_one_solitaire.beans;

import com.cst8334_group_one_solitaire.commands.CommandInvoker;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javafx.scene.input.*;

public class Window extends JFrame {
    private static final long serialVersionUID = 1L;


    private static class CumulativeCheckboxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBoxMenuItem cb = (JCheckBoxMenuItem) e.getSource();
            Game.getInstance().scoreTracking(cb.isSelected());
        }
    }
    
    private static class ManualMoveCheckboxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBoxMenuItem cb = (JCheckBoxMenuItem) e.getSource();
            Game.getInstance().manualMove(cb.isSelected());
        }
    }

    private class RestartButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Game.getInstance().startGame();
            CommandInvoker.getInstance().restart();
            repaint();

        }
    }

    private static class CloseGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private static class SwitchToVegas implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Game.getInstance().gameModeTest("vegas");
            Game.getInstance().startGame();
        }
    }

    private static class SwitchToRegular implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Game.getInstance().gameModeTest("regular");
            Game.getInstance().startGame();
        }
    }

    private static class UndoButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (CommandInvoker.getInstance().undoOperation()) {
                System.out.println("Move undone");
                Game.getInstance().setLastMove("Undo");
            } else {
                JOptionPane.showMessageDialog(null, "No move to undo.");
            }
        }


    }
    
    private static class CustomKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_SPACE) {
                Game.getInstance().dropCardFromStack();
            }
            
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_SHIFT) {
                Game.getInstance().setShiftKey(false);
                System.out.println("shift=false");
            }
            
        }
    }

    private static class MouseKeeper extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isShiftDown()) {
                Game.getInstance().setShiftKey(true);
            } else {
                Game.getInstance().setShiftKey(false);
            }
            int x = e.getX();
            int y = e.getY();
            Game.getInstance().select(x, y);
        }

    }
    
    private static class MouseMotion extends MouseInputAdapter {
        public void mouseMoved(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            Game.getInstance().setMouse(x, y);
        }
    }
    
    private static class Help implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, Game.getInstance().getHelpDialog() + "\n\nTableau's stack cards in alternating colour and decreasing ranks (King -> Ace)"
                    + "\nFoundations stack cards in matching suit and increasing ranks(Ace -> King)"
                    + "\nGet all the cards into the foundations!"
                    +"\n\nManual Move can be turned on/off in Game options"
                    +"\nManual Move = " + Game.getInstance().manualMove());
        }
    }

    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(1366, (int) screenSize.getHeight() - 100);
        setLocationRelativeTo(null);
        //setMinimumSize(new Dimension(1366, (int) screenSize.getHeight() - 100));
        getContentPane().setBackground(new Color(0, 120, 0));
        setTitle("Solitaire Game Java - Zac, David, Theodore, Sebastien");
        setResizable(true);
        addMouseListener(new MouseKeeper());
        addMouseMotionListener(new MouseMotion());
        addKeyListener(new CustomKeyListener());
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        JButton undo = new JButton("Undo");
        undo.addActionListener(new UndoButtonListener());
        undo.setBackground(new Color(160, 82, 45)); //change color of button
        undo.setForeground(Color.WHITE); // text color
        add("South", undo);

//      Make Menu bar and menus
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu gameModeMenu = new JMenu("Gamemode");
//      Make menu items
        JMenuItem restart, exit;
        JCheckBoxMenuItem cumulative;
        restart = new JMenuItem("Restart");
        exit = new JMenuItem("Exit");
        JMenuItem regular, vegas;
        regular = new JMenuItem("Regular");
        vegas = new JMenuItem("Vegas");
        cumulative = new JCheckBoxMenuItem("Cumulative scoring");
//      Add items to menu
        gameMenu.add(restart);
        gameModeMenu.add(regular);
        gameModeMenu.add(vegas);
        gameMenu.add(gameModeMenu);
        gameModeMenu.add(cumulative);
        gameMenu.add(exit);
        
        JCheckBoxMenuItem manualMove = new JCheckBoxMenuItem("Manual Card Move");
        gameMenu.add(manualMove);

//      Add menu to menuBar
        menuBar.add(gameMenu);

        restart.addActionListener(new RestartButtonListener());
        regular.addActionListener(new SwitchToRegular());
        vegas.addActionListener(new SwitchToVegas());
        exit.addActionListener(new CloseGame());
        cumulative.addActionListener(new CumulativeCheckboxListener());
        manualMove.addActionListener(new ManualMoveCheckboxListener());
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem howToPlay= new JMenuItem("How To Play");
        helpMenu.add(howToPlay);
        howToPlay.addActionListener(new Help());
        
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        setVisible(true);
    }

}
