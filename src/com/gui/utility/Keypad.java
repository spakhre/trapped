package com.gui.utility;

import com.trapped.gui.GuiPanel;
import com.trapped.gui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Keypad extends JFrame {

    public String currentDigits = "";
    //
    public static final int FRAME_WIDTH = 360;
    public static final int FRAME_HEIGHT = 550;
    public static final int KP_HEIGHT = 425;
    public static final int DISPLAY_HEIGHT = 90;
//    public static final Dimension btnD = new Dimension(100, 100);

    public Keypad(){
        super("Locked Door: Keypad");
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);

        this.getContentPane().setBackground(Color.darkGray);

        JPanel basePanel = new JPanel();
        this.getContentPane().add(basePanel);
        basePanel.setLayout(new BorderLayout());
        basePanel.setBackground(Color.yellow);
        basePanel.add(new DisplayPanel(), BorderLayout.NORTH);
        basePanel.add(new KeypadPanel(), BorderLayout.SOUTH);

        this.setVisible(true);
    };

    public class DisplayPanel extends JPanel {
        public DisplayPanel(){
            this.setPreferredSize(new Dimension(FRAME_WIDTH, DISPLAY_HEIGHT));
            this.setBackground(Color.red);
        }
    }

    public class KeypadPanel extends JPanel {
        public KeypadPanel(){
            this.setPreferredSize(new Dimension(FRAME_WIDTH, KP_HEIGHT));
            this.setBackground(new Color(160, 160, 160));
            this.setLayout(new GridLayout(4,3, 10, 10));

            // Make buttons
            JButton one_btn = new JButton("1");
            JButton two_btn = new JButton("2");
            JButton three_btn = new JButton("3");
            JButton four_btn = new JButton("4");
            JButton five_btn = new JButton("5");
            JButton six_btn = new JButton("6");
            JButton seven_btn = new JButton("7");
            JButton eight_btn = new JButton("8");
            JButton nine_btn = new JButton("9");
            JButton zero_btn = new JButton("0");
            // Add action listeners
            one_btn.addActionListener(new KeypadListener("1"));
            two_btn.addActionListener(new KeypadListener("2"));
            three_btn.addActionListener(new KeypadListener("3"));
            four_btn.addActionListener(new KeypadListener("4"));
            five_btn.addActionListener(new KeypadListener("5"));
            six_btn.addActionListener(new KeypadListener("6"));
            seven_btn.addActionListener(new KeypadListener("7"));
            eight_btn.addActionListener(new KeypadListener("8"));
            nine_btn.addActionListener(new KeypadListener("9"));
            zero_btn.addActionListener(new KeypadListener("0"));
            // Add btns to panel
            this.add(one_btn);
            this.add(two_btn);
            this.add(three_btn);
            this.add(four_btn);
            this.add(five_btn);
            this.add(six_btn);
            this.add(seven_btn);
            this.add(eight_btn);
            this.add(nine_btn);
            this.add(zero_btn);
        }
    }

    // Associate number with button
    public class KeypadListener implements ActionListener {
        String value = "";

        public KeypadListener(String digit){
            value = digit;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentDigits += value;
            System.out.println(currentDigits);
        }
    }
}