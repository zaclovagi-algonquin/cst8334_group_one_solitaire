package com.cst8334_group_one_solitaire.beans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import com.cst8334_group_one_solitaire.commands.CommandInvoker;

public class Window extends JFrame {
    private static final long serialVersionUID = 1L;


    private class RestartButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Game.getInstance().restart();
            CommandInvoker.getInstance().restart();
            repaint();

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

        JButton restart = new JButton("Restart");
        restart.addActionListener(new RestartButtonListener());
        restart.setBackground(new Color(160, 82, 45)); //change color of button
        restart.setForeground(Color.WHITE); // text color
        add("South", restart);

        JButton undo = new JButton("Undo");
        undo.addActionListener(new UndoButtonListener());
        undo.setBackground(new Color(160, 82, 45)); //change color of button
        undo.setForeground(Color.WHITE); // text color
        add("North", undo);
                
        setVisible(true);
    }

}
