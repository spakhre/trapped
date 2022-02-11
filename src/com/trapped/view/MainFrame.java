package com.trapped.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MainFrame extends JFrame{


    public JButton  startButton, settingButton, exitButton;
    public JPanel MenuBG_panel, menuPanel,aboutTextPanel, howTextPanel,mainTextPanel;
    public JTextArea textArea = new JTextArea();
    public JPanel[] GameBG_panel =new JPanel[10];
    public JLabel[] GameBG_label=new JLabel[10];

    public MainFrame() {
        // the tile of the window
        super("Trapped");
        setUpMainMenu();
    }

    public void setUpMainMenu() {
        Container con = getContentPane();
        // GUI setting up
        setFrameConfigs();
        setAllButtons();
        setAllPanels();
        createGameBG();

        // ---- LABELS ADDED TO PANELS ----
        JLabel themeLabel = new JLabel();
        ImageIcon ThemeIcon = new ImageIcon("resources/SwingArt/MainTheme1.png");
        themeLabel.setIcon(ThemeIcon);
        MenuBG_panel.add(themeLabel);


        menuPanel.add(startButton);
        menuPanel.add(settingButton);
        menuPanel.add(exitButton);

        con.add(MenuBG_panel);
        con.add(menuPanel);
        con.add(textArea);
        con.add(GameBG_panel[1]);

        setVisible(true);
    }


    private void setFrameConfigs() {
        setSize(500, 750);
        setResizable(false);        // enable the resize of the frame
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.black);
        setLayout(null);
        setVisible(true);
    }

    private void setAllPanels() {
        MenuBG_panel = createJPanel(-10, 0, 500, 750, true);
        menuPanel = createJPanel(150, 350, 100, 180, true);
        menuPanel.setBackground(Color.decode("#302a1e"));
        mainTextPanel = createJPanel(10, 550, 300, 150, false);
    }

    private void setAllButtons() {
        startButton = createJButton("Start", 100, 40, false, Color.lightGray, Color.decode("#302a1e"));
        settingButton = createJButton("Setting", 100, 40, false, Color.lightGray, Color.decode("#302a1e"));
        exitButton = createJButton("Exit", 100, 40, false, Color.lightGray, Color.black);
    }


    // display the MainMenu
    public void showMainMenu() {
        menuPanel.setVisible(true);
        aboutTextPanel.setVisible(false);
        howTextPanel.setVisible(false);
        mainTextPanel.setVisible(false);
    }

    // Create the game sense
    public void createGameScreen() {
        MenuBG_panel.setVisible(false);
        menuPanel.setVisible(false);
        textArea.setVisible(true);
        GameBG_panel[1].setVisible(true);
        createGameObj(140,250,200,200,"inspect","get","drop","resources/SwingArt/bed1.png");
    }

    // method to set the JButton
    private JButton createJButton(String title, int width, int height, boolean focusable, Color foreground, Color background) {
        JButton button = new JButton(title);
        button.setPreferredSize(new Dimension(width, height));
        button.setFocusable(focusable);
        button.setForeground(foreground);
        button.setBackground(background);
        return button;
    }

    // method to set the JPanel
    private JPanel createJPanel(int x, int y, int width, int height, boolean visible) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBackground(null);
        panel.setVisible(visible);
        return panel;
    }

    public void writeToTextArea(String s) {
            textArea.setText(s);
            textArea.setFont(new Font("Arial", Font.BOLD, 15));
            textArea.setBounds(10, 550, 300, 150);
            textArea.setBackground(Color.RED);
            textArea.setForeground(Color.white);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setEditable(false);
    }

    public void createGameBG(){
        GameBG_panel[1]=new JPanel();
        GameBG_panel[1]=createJPanel(10,40,480,500,false);
        GameBG_panel[1].setLayout(null);

        GameBG_label[1]=new JLabel();
        GameBG_label[1].setBounds(0,0,490,500);

        ImageIcon RoomBackground=new ImageIcon("resources/SwingArt/room1.png");
        GameBG_label[1].setIcon(RoomBackground);
    }

    public void createGameObj(int x,int y,int width,int height,
                              String inspect,String get, String drop,String fileName) {

        JPopupMenu popMenu=new JPopupMenu();
        JMenuItem menuItem[]=new JMenuItem[5];
        menuItem[1] = new JMenuItem(inspect);
        popMenu.add(menuItem[1]);

        menuItem[2] = new JMenuItem(get);
        popMenu.add(menuItem[2]);

        menuItem[3] = new JMenuItem(drop);
        popMenu.add(menuItem[3]);



        JLabel ObjLabel=new JLabel();
        ObjLabel.setBounds(x,y,width,height);
        ImageIcon objectIcon=new ImageIcon(fileName);
        ObjLabel.setIcon(objectIcon);
        ObjLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    popMenu.show(ObjLabel,e.getX(),e.getY());
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        GameBG_panel[1].add(ObjLabel);
        GameBG_panel[1].add(GameBG_label[1]);
    }


}