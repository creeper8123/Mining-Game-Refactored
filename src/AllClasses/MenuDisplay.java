package AllClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

public class MenuDisplay<E> implements MouseListener, MouseWheelListener {
    public static final int STANDARD_TEXT_HEIGHT_PIXELS = 15;
    public JLabel menu;
    public int menuWidth = 175;
    public int menuHeight = 200;
    public Color backgroundColor = new Color(0, 0 ,0 , 127);
    public Color textColor = new Color(255, 255, 255, 255);
    public BufferedImage menuBackground = new BufferedImage(1, 1, 6);
    public E[] menuContents;
    public String menuTitle;
    public String[] menuDisplayContents;
    public String fullMenuDisplayContents;
    public int selectedItem = -1;
    public Font menuFont = new Font("Consolas", Font.PLAIN, 12);
    public Font buttonFont = new Font("Consolas", Font.PLAIN, 10);

    public MenuDisplay(int xPos, int yPos){
        this.menuBackground.setRGB(0, 0, backgroundColor.getRGB());
        this.menu = new JLabel();
        this.menu.setFocusable(false);
        this.menu.setBounds(xPos, yPos, 1, 1);

        this.menu.setForeground(textColor);
        this.menu.setIcon(new ImageIcon(ImageProcessing.resizeImage(this.menuBackground, this.menu.getWidth(), this.menu.getHeight())));
        this.menu.setHorizontalTextPosition(JLabel.CENTER);
        this.menu.setVerticalTextPosition(JLabel.CENTER);
        this.menu.setFont(menuFont);

        Game.layeredPane.add(menu);
        Game.layeredPane.setLayer(menu, ImageProcessing.HUD_LAYER);

        menu.addMouseListener(this);
        menu.addMouseWheelListener(this);
    }

    public void toggleVisibility(){
        setVisibility(!menu.isVisible());
    }

    public void setVisibility(boolean visibility){
        if(!menu.isVisible() && visibility){
            menu.addMouseListener(this);
            menu.addMouseWheelListener(this);
        }else if(menu.isVisible() && !visibility){
            menu.removeMouseListener(this);
            menu.removeMouseWheelListener(this);
        }
        menu.setVisible(visibility);
    }

    public void setMenuContents(E[] newMenuContents) {
        menuContents = newMenuContents;
        this.menuHeight = (menuContents.length + 1) * STANDARD_TEXT_HEIGHT_PIXELS;
        setMenuSize(menuWidth, menuHeight);
    }

    public void setMenuContents(E[] newMenuContents, int menuWidth) {
        this.menuWidth = menuWidth;
        menuContents = newMenuContents;
        this.menuHeight = (menuContents.length + 1) * STANDARD_TEXT_HEIGHT_PIXELS;
        setMenuSize(menuWidth, menuHeight);
    }

    public void setMenuContents(int index, E newMenuContent) {
        menuContents[index] = newMenuContent;
    }

    public E[] getMenuContents() {
        return menuContents;
    }

    public E getMenuContents(int index) {
        return menuContents[index];
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void configureMenuContents(){
        this.menuDisplayContents = new String[menuContents.length];
        for (int i = 0; i < this.menuDisplayContents.length; i++) {
            if(i == selectedItem){
                if(this.menuContents[i] == null){
                    this.menuDisplayContents[i] = "&gt[null]&gt";
                }else{
                    this.menuDisplayContents[i] = "&gt" + this.menuContents[i].toString() + "&lt";
                }
            }else{
                if(this.menuContents[i] == null){
                    this.menuDisplayContents[i] = "&#8194[null]&#8194";
                }else{
                    this.menuDisplayContents[i] = "&#8194" + this.menuContents[i].toString() + "&#8194";
                }
            }
        }
        this.fullMenuDisplayContents = "<html><body><u>" + menuTitle + "</u>";
        for (int i = 0; i < this.menuDisplayContents.length; i++) {
            this.fullMenuDisplayContents += "<br>" + menuDisplayContents[i];
        }
        this.fullMenuDisplayContents += "</body></html>";
        this.menu.setText(fullMenuDisplayContents);
    }

    public void setMenuPosition(int x, int y){
        menu.setLocation(x, y);
    }

    public void setMenuSize(int width, int height){
        this.menu.setSize(width, height);
        this.menu.setIcon(new ImageIcon(ImageProcessing.resizeImage(menuBackground, width, height)));
    }

    public void addButton(int buttonX, int buttonY, int buttonHeight, int buttonWidth, String title, Runnable codeToRun){
        JButton newButton = new JButton(title);
        newButton.setFocusable(false);
        newButton.setFont(buttonFont);
        newButton.setHorizontalTextPosition(JLabel.CENTER);
        newButton.setVerticalTextPosition(JLabel.CENTER);
        newButton.setForeground(textColor);
        newButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        newButton.setBackground(Color.BLACK);
        newButton.addActionListener(e -> codeToRun.run());
        menu.add(newButton);
    }

    public void deconstruct(boolean confirm){
        if(confirm){
            Game.layeredPane.remove(menu);
            Game.layeredPane.repaint();
            menu.removeMouseListener(this);
            menu.removeMouseWheelListener(this);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == 1){
            int newSelectedItem = (e.getY()/STANDARD_TEXT_HEIGHT_PIXELS) - 1;
            if(newSelectedItem == selectedItem){
                selectedItem = -1;
            }else if(selectedItem < menuContents.length){
                selectedItem = newSelectedItem;
            }
            configureMenuContents();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

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


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.selectedItem += e.getWheelRotation();
        if(this.selectedItem > menuContents.length - 1){
            this.selectedItem = -1;
        }else if(this.selectedItem < -1){
            this.selectedItem = menuContents.length - 1;
        }
        this.configureMenuContents();
    }
}
