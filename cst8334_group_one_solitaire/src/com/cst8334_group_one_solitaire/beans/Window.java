package com.cst8334_group_one_solitaire.beans;

import com.cst8334_group_one_solitaire.commands.CommandInvoker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window extends JFrame {
    private static final long serialVersionUID = 1L;



    private static class CumulativeCheckboxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBoxMenuItem cb = (JCheckBoxMenuItem) e.getSource();
            Game.getInstance().scoreTracking(cb.isSelected());
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
            } else {
                JOptionPane.showMessageDialog(null, "No move to undo.");
            }
        }


    }

    private static class MouseKeeper extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            Game.getInstance().select(x, y);
        }

    }

    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(1366, (int) screenSize.getHeight() - 100);
        getContentPane().setBackground(new Color(0, 120, 0));
        setTitle("Solitaire Game Java - Zac, David, Theodore, Sebastien");
        setResizable(true);
        addMouseListener(new MouseKeeper());

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

//      Add menu to menuBar
        menuBar.add(gameMenu);

        restart.addActionListener(new RestartButtonListener());
        regular.addActionListener(new SwitchToRegular());
        vegas.addActionListener(new SwitchToVegas());
        exit.addActionListener(new CloseGame());
        cumulative.addActionListener(new CumulativeCheckboxListener());


        setJMenuBar(menuBar);
        setVisible(true);
    }

}
