import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class MenuDisplay<E> implements MouseListener {
    public JLabel menu;
    public Color backgroundColor = new Color(0, 0 ,0 , 127);
    public Color textColor = new Color(255, 255, 255, 255);
    public BufferedImage menuBackground = new BufferedImage(1, 1, 6);
    public E[] menuContents;
    public String menuTitle;
    public String[] menuDisplayContents;
    public String fullMenuDisplayContents;

    public MenuDisplay(){
        menuBackground.setRGB(0, 0, backgroundColor.getRGB());

        this.menu = new JLabel();

        Rectangle menuSize = new Rectangle(100, 500, 200, 200);

        this.menu.setBounds(menuSize);

        this.menu.setForeground(textColor);
        this.menu.setIcon(new ImageIcon(ImageProcessing.resizeImage(menuBackground, 50, 50)));
        this.menu.setHorizontalTextPosition(JLabel.CENTER);
        this.menu.setFont(new Font("Consolas", Font.PLAIN, 12));

        Game.layeredPane.add(menu);
        Game.layeredPane.setLayer(menu, ImageProcessing.MENU_LAYER);



    }

    public void toggleVisibility(){
        setVisibility(!menu.isVisible());
    }

    public void setVisibility(boolean aFlag){
        if(aFlag){
            menu.addMouseListener(this);
        }else{
            menu.removeMouseListener(this);
        }
        menu.setVisible(aFlag);
    }

    public void setPosition(int x, int y){
        menu.setLocation(x, y);
    }

    public void setMenuContents(E[] newMenuContents) {
        menuContents = newMenuContents;
    }

    public void setMenuContents(int index, E newMenuContent) {
        menuContents[index] = newMenuContent;
    }

    public Object[] getMenuContents() {
        return menuContents;
    }

    public Object getMenuContents(int index) {
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
            this.menuDisplayContents[i] = this.menuContents[i].toString();
        }
        this.fullMenuDisplayContents = "<html><body><u>" + menuTitle + "</u>";
        for (String menuDisplayContent : this.menuDisplayContents) {
            this.fullMenuDisplayContents += "<br>" + menuDisplayContent;
        }
        this.fullMenuDisplayContents += "</body></html>";
        this.menu.setText(fullMenuDisplayContents);
    }

    public void setMenuSize(int width, int height){
        this.menu.setSize(width, height);
        this.menu.setIcon(new ImageIcon(ImageProcessing.resizeImage(menuBackground, width, height)));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Menu Clicked");
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
}
