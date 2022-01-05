package Helpers;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
When adding a new ItemID, needs to update:
HoldableObject.generateTexture(itemID);                     //Only if object can be placed
HoldableObject.generateDisplayName(itemID);                 //Only if item is intended to be held.
HoldableObject.getCraftingRecipe(itemID);                   //Only if item can be crafted.
HoldableObject.whenUsed(int x, int y, MovingObject usedBy); //Only if the item has a use or can be placed (IE is not an intermediate crafting item)
InventoryManager.getStackSize(itemID);                      //Only if item is intended to be held.
TilePresets.getTilePreset();                                //For tiles only
TileGeneration.xyzGeneration();                             //For tiles only, and only if naturally occurring.
*/

public class HoldableObjectBuilder {
    final private static int nameLabelLength = 500;


    static JFrame frame = new JFrame();
    public static void main(String[] args) {
        System.out.println("Loading HoldableObject Builder...");

        //General Window Starter
        frame.setTitle("HoldableObject Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(0, 0);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setLayout(null);

        //Internal name text fields
        JLabel internalNameLabel = new JLabel("Internal Name (Exclude the \"XXX_\" prefix, and the \"_L#\" postfix):");
        internalNameLabel.setLocation(10, 10);
        internalNameLabel.setSize(nameLabelLength, 20);
        frame.getContentPane().add(internalNameLabel);
        JTextField internalNameTextArea = new JTextField("LOREM_IPSUM");
        internalNameTextArea.setLocation(10, 30);
        internalNameTextArea.setSize(nameLabelLength, 20);
        frame.getContentPane().add(internalNameTextArea);

        //External/Display name text fields
        JLabel externalNameLabel = new JLabel("External Name (Leave blank to default to internal name):");
        externalNameLabel.setLocation(10, 60);
        externalNameLabel.setSize(nameLabelLength, 20);
        frame.getContentPane().add(externalNameLabel);
        JTextField externalNameTextArea = new JTextField("Lorem Ipsum");
        externalNameTextArea.setLocation(10, 80);
        externalNameTextArea.setSize(nameLabelLength, 20);
        externalNameTextArea.setToolTipText("Internal ItemID enum");
        frame.getContentPane().add(externalNameTextArea);

        //Max Stack Size
        JLabel stackLabel = new JLabel("Max Stack Size:");
        stackLabel.setLocation(nameLabelLength + 20, 10);
        stackLabel.setSize(100, 20);
        frame.getContentPane().add(stackLabel);
        NumberFormatter maxStackBounds = new NumberFormatter();
        maxStackBounds.setMinimum(1);
        maxStackBounds.setMaximum(Integer.MAX_VALUE);
        JFormattedTextField stackTextArea = new JFormattedTextField(maxStackBounds);
        stackTextArea.setValue(100);
        stackTextArea.setLocation(nameLabelLength + 20, 30);
        stackTextArea.setSize(100, 20);
        stackTextArea.setToolTipText("Stack Size");
        frame.getContentPane().add(stackTextArea);

        //Levels
        JCheckBox hasLevelsCheckBox = new JCheckBox("Object has Levels?");
        hasLevelsCheckBox.setLocation(nameLabelLength + 20, 60);
        hasLevelsCheckBox.setSize(150, 20);
        frame.getContentPane().add(hasLevelsCheckBox);
        JLabel maxLevelLabel = new JLabel("Max Level:");
        maxLevelLabel.setLocation(nameLabelLength + 20, 80);
        maxLevelLabel.setSize(80, 20);
        maxLevelLabel.setEnabled(false);
        frame.getContentPane().add(maxLevelLabel);
        NumberFormatter maxLevelBounds = new NumberFormatter();
        maxLevelBounds.setMinimum(1);
        maxLevelBounds.setMaximum(Integer.MAX_VALUE);
        JFormattedTextField maxLevelTextField = new JFormattedTextField(maxLevelBounds);
        maxLevelTextField.setValue(1);
        maxLevelTextField.setLocation(nameLabelLength + 10 + maxLevelLabel.getWidth(), 80);
        maxLevelTextField.setSize(60, 20);
        maxLevelTextField.setEnabled(false);
        frame.getContentPane().add(maxLevelTextField);
        hasLevelsCheckBox.addChangeListener(e -> {
            if(hasLevelsCheckBox.isSelected()){
                maxLevelLabel.setEnabled(true);
                maxLevelTextField.setEnabled(true);
            }else{
                maxLevelLabel.setEnabled(false);
                maxLevelTextField.setEnabled(false);
            }
        });

        //Object Type
        ButtonGroup itemTypeButtonGroup = new ButtonGroup();
        JLabel objectTypeLabel = new JLabel("Object Type:");
        objectTypeLabel.setLocation(10, 110);
        objectTypeLabel.setSize(175, 20);
        frame.getContentPane().add(objectTypeLabel);
        JRadioButton tileRadioButton = new JRadioButton("TILE");
        tileRadioButton.setLocation(10, 130);
        tileRadioButton.setSize(175, 20);
        frame.getContentPane().add(tileRadioButton);
        JRadioButton itemRadioButton = new JRadioButton("ITEM");
        itemRadioButton.setLocation(10, 150);
        itemRadioButton.setSize(175, 20);
        frame.getContentPane().add(itemRadioButton);
        JRadioButton otherRadioButton = new JRadioButton("Other (Please Specify)");
        otherRadioButton.setLocation(10, 170);
        otherRadioButton.setSize(175, 20);
        frame.getContentPane().add(otherRadioButton);
        JTextField otherTypeTextField = new JTextField("LOREM_IPSUM");
        otherTypeTextField.setLocation(10, 190);
        otherTypeTextField.setSize(175, 20);
        otherTypeTextField.setEnabled(false);
        frame.getContentPane().add(otherTypeTextField);
        itemTypeButtonGroup.add(tileRadioButton);
        itemTypeButtonGroup.add(itemRadioButton);
        itemTypeButtonGroup.add(otherRadioButton);
        tileRadioButton.setSelected(true);
        tileRadioButton.addActionListener(e -> otherTypeTextField.setEnabled(otherRadioButton.isSelected()));
        itemRadioButton.addActionListener(e -> otherTypeTextField.setEnabled(otherRadioButton.isSelected()));
        otherRadioButton.addActionListener(e -> otherTypeTextField.setEnabled(otherRadioButton.isSelected()));

        //Textures
        class TextureManager{
            public final char VARIATION_NUMBER_REPLACEMENT = '#';
            public final char LEVEL_NUMBER_REPLACEMENT = '@';
            String textureLocation;
            boolean hasVariations;
            boolean allowRotations;
            boolean allowVerticalFlipping;
            boolean allowHorizontalFlipping;
            boolean hasNullPixels;

            public TextureManager(){

            }

            public TextureManager(String location, boolean hasVariations, boolean allowRotations, boolean allowVerticalFlipping, boolean allowHorizontalFlipping, boolean hasNullPixels){

            }
        }
        ArrayList<TextureManager> textureManagerArrayList = new ArrayList<>();
        JCheckBox hasTextureCheckBox = new JCheckBox("Has Texture?");
        hasTextureCheckBox.setLocation(200, 110);
        hasTextureCheckBox.setSize(125, 20);
        hasTextureCheckBox.setSelected(true);
        frame.getContentPane().add(hasTextureCheckBox);
        JComboBox<String> layerNameComboBox = new JComboBox<>();
        layerNameComboBox.addItem("Base Layer");
        layerNameComboBox.setLocation(350, 110);
        layerNameComboBox.setSize(200, 20);
        frame.getContentPane().add(layerNameComboBox);
        JButton addNewOverlayButton = new JButton("Add");
        addNewOverlayButton.setLocation(575, 110);
        addNewOverlayButton.setSize(100, 20);
        frame.getContentPane().add(addNewOverlayButton);
        JButton removeLastOverlayButton = new JButton("Remove");
        removeLastOverlayButton.setLocation(575, 130);
        removeLastOverlayButton.setSize(100, 20);
        frame.getContentPane().add(removeLastOverlayButton);
        addNewOverlayButton.addActionListener(e -> {
            layerNameComboBox.addItem("Overlay Layer #" + layerNameComboBox.getItemCount());
            textureManagerArrayList.add(new TextureManager());
        });
        removeLastOverlayButton.addActionListener(e -> {
            if(layerNameComboBox.getItemCount() > 1){
                layerNameComboBox.removeItemAt(layerNameComboBox.getItemCount()-1);
                textureManagerArrayList.remove(textureManagerArrayList.size()-1);
            }

        });





        frame.setVisible(true);
    }
}
