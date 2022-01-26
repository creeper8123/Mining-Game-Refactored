package Helpers;

import UniqueIDs.ItemID;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.util.ArrayList;

/*
When adding a new UniqueIDs.ItemID, needs to update:

UniqueIDs.ItemID;                                                     //Used for every unique item
AllClasses.HoldableObject.generateTexture(itemID);                     //Only if object can be placed
AllClasses.HoldableObject.generateDisplayName(itemID);                 //Only if item is intended to be held.
AllClasses.HoldableObject.getCraftingRecipe(itemID);                   //Only if item can be crafted.
AllClasses.HoldableObject.whenUsed(int x, int y, AllClasses.MovingObject usedBy); //Only if the item has a use or can be placed (IE is not an intermediate crafting item)
AllClasses.InventoryManager.getStackSize(itemID);                      //Only if item is intended to be held.
AllClasses.TilePresets.getTilePreset();                                //For tiles only
AllClasses.TileGeneration.xyzGeneration();                             //For tiles only, and only if naturally occurring.
*/

public class HoldableObjectBuilder{
    final private static int nameLabelLength = 500;
    private static int lastTextureIndex = 0;


    static JFrame frame = new JFrame();
    public static void main(String[] args) {
        System.out.println("Loading Helpers.HoldableObjectBuilder...");

        //General Window Starter
        frame.setTitle("AllClasses.HoldableObject Builder");
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
        JLabel externalNameLabel = new JLabel("External Name (Leave blank to default to internal name) (omit level number):");
        externalNameLabel.setLocation(10, 60);
        externalNameLabel.setSize(nameLabelLength, 20);
        frame.getContentPane().add(externalNameLabel);
        JTextField externalNameTextArea = new JTextField("Lorem Ipsum");
        externalNameTextArea.setLocation(10, 80);
        externalNameTextArea.setSize(nameLabelLength, 20);
        externalNameTextArea.setToolTipText("Internal UniqueIDs.ItemID enum");
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
            public static final char VARIATION_NUMBER_REPLACEMENT = '#';
            public static final char LEVEL_NUMBER_REPLACEMENT = '@';
            String textureLocation = "missingTexture";
            boolean hasVariations = false;
            boolean allowRotations = false;
            boolean allowVerticalFlipping = false;
            boolean allowHorizontalFlipping = false;
            boolean hasNullPixels = false;

            public TextureManager(){

            }
        }
        ArrayList<TextureManager> textureManagerArrayList = new ArrayList<>();
        textureManagerArrayList.add(new TextureManager());
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
        JLabel fileLocationLabel = new JLabel("Texture File Location (Use " + TextureManager.VARIATION_NUMBER_REPLACEMENT + " for variant number, use " + TextureManager.LEVEL_NUMBER_REPLACEMENT + " for level number):");
        fileLocationLabel.setLocation(230, 160);
        fileLocationLabel.setSize(425, 20);
        frame.getContentPane().add(fileLocationLabel);
        JLabel fileLocationStartLabel = new JLabel("resources/textures/");
        fileLocationStartLabel.setLocation(230, 180);
        fileLocationStartLabel.setSize(120, 20);
        frame.getContentPane().add(fileLocationStartLabel);
        JTextField fileLocationTextField = new JTextField("missingTexture");
        fileLocationTextField.setLocation(345, 180);
        fileLocationTextField.setSize(200, 20);
        frame.getContentPane().add(fileLocationTextField);
        JLabel fileLocationEndLabel = new JLabel(".png");
        fileLocationEndLabel.setLocation(545, 180);
        fileLocationEndLabel.setSize(50, 20);
        frame.getContentPane().add(fileLocationEndLabel);
        JCheckBox hasVariationsCheckBox = new JCheckBox("Has Variations?");
        hasVariationsCheckBox.setLocation(300, 200);
        hasVariationsCheckBox.setSize(125, 20);
        frame.getContentPane().add(hasVariationsCheckBox);
        JCheckBox hasRotationsCheckBox = new JCheckBox("Allow Rotations?");
        hasRotationsCheckBox.setLocation(300, 220);
        hasRotationsCheckBox.setSize(125, 20);
        frame.getContentPane().add(hasRotationsCheckBox);
        JCheckBox hasVerticalFlippingCheckBox = new JCheckBox("Allow Vertical Flipping?");
        hasVerticalFlippingCheckBox.setLocation(300, 240);
        hasVerticalFlippingCheckBox.setSize(175, 20);
        frame.getContentPane().add(hasVerticalFlippingCheckBox);
        JCheckBox hasHorizontalFlippingCheckBox = new JCheckBox("Allow Horizontal Flipping?");
        hasHorizontalFlippingCheckBox.setLocation(300, 260);
        hasHorizontalFlippingCheckBox.setSize(175, 20);
        frame.getContentPane().add(hasHorizontalFlippingCheckBox);
        JCheckBox hasNullPixelsCheckBox = new JCheckBox("Has Null Pixels?");
        hasNullPixelsCheckBox.setLocation(300, 280);
        hasNullPixelsCheckBox.setSize(175, 20);
        frame.getContentPane().add(hasNullPixelsCheckBox);
        hasTextureCheckBox.addActionListener(e -> {
            boolean toggle = hasTextureCheckBox.isSelected();
            layerNameComboBox.setEnabled(toggle);
            addNewOverlayButton.setEnabled(toggle);
            removeLastOverlayButton.setEnabled(toggle);
            fileLocationLabel.setEnabled(toggle);
            fileLocationStartLabel.setEnabled(toggle);
            fileLocationTextField.setEnabled(toggle);
            fileLocationEndLabel.setEnabled(toggle);
            hasVariationsCheckBox.setEnabled(toggle);
            hasRotationsCheckBox.setEnabled(toggle);
            hasVerticalFlippingCheckBox.setEnabled(toggle);
            hasHorizontalFlippingCheckBox.setEnabled(toggle);
            hasNullPixelsCheckBox.setEnabled(toggle);
        });
        layerNameComboBox.addActionListener(e -> {
            //TODO: commit all texture options for the selected layer when "create object" button is clicked
            textureManagerArrayList.get(lastTextureIndex).textureLocation = fileLocationTextField.getText();
            textureManagerArrayList.get(lastTextureIndex).hasVariations = hasVariationsCheckBox.isSelected();
            textureManagerArrayList.get(lastTextureIndex).allowRotations = hasRotationsCheckBox.isSelected();
            textureManagerArrayList.get(lastTextureIndex).allowVerticalFlipping = hasVerticalFlippingCheckBox.isSelected();
            textureManagerArrayList.get(lastTextureIndex).allowHorizontalFlipping = hasHorizontalFlippingCheckBox.isSelected();
            textureManagerArrayList.get(lastTextureIndex).hasNullPixels = hasNullPixelsCheckBox.isSelected();

            TextureManager selectedTextureManager = textureManagerArrayList.get(layerNameComboBox.getSelectedIndex());
            fileLocationTextField.setText(selectedTextureManager.textureLocation);
            hasVariationsCheckBox.setSelected(selectedTextureManager.hasVariations);
            hasRotationsCheckBox.setSelected(selectedTextureManager.allowRotations);
            hasVerticalFlippingCheckBox.setSelected(selectedTextureManager.allowVerticalFlipping);
            hasHorizontalFlippingCheckBox.setSelected(selectedTextureManager.allowHorizontalFlipping);
            hasNullPixelsCheckBox.setSelected(selectedTextureManager.hasNullPixels);

            lastTextureIndex = layerNameComboBox.getSelectedIndex();
        });

        //Crafting Recipe
        JCheckBox hasCraftingRecipeCheckBox = new JCheckBox("Has Crafting Recipe?");
        hasCraftingRecipeCheckBox.setSize(200, 20);
        hasCraftingRecipeCheckBox.setLocation(700, 10);
        frame.getContentPane().add(hasCraftingRecipeCheckBox);
        JLabel craftingRecipeLabel = new JLabel("Ingredients (TILE_AIR for nothing):");
        craftingRecipeLabel.setSize(200, 20);
        craftingRecipeLabel.setLocation(700, 30);
        frame.getContentPane().add(craftingRecipeLabel);
        JLabel craftingRecipeQuantityLabel = new JLabel("Quantity:");
        craftingRecipeQuantityLabel.setSize(200, 20);
        craftingRecipeQuantityLabel.setLocation(910, 30);
        frame.getContentPane().add(craftingRecipeQuantityLabel);
        NumberFormatter craftingItemBounds = new NumberFormatter();
        craftingItemBounds.setMinimum(1);
        craftingItemBounds.setMaximum(Integer.MAX_VALUE);
        JComboBox<ItemID> firstCraftingItemComboBox = new JComboBox<>(ItemID.values());
        firstCraftingItemComboBox.setSize(200, 20);
        firstCraftingItemComboBox.setLocation(700, 50);
        frame.getContentPane().add(firstCraftingItemComboBox);
        JFormattedTextField firstCraftingItemQuantityTextField = new JFormattedTextField(craftingItemBounds);
        firstCraftingItemQuantityTextField.setValue(1);
        firstCraftingItemQuantityTextField.setLocation(910, 50);
        firstCraftingItemQuantityTextField.setSize(50, 20);
        frame.getContentPane().add(firstCraftingItemQuantityTextField);
        JComboBox<ItemID> secondCraftingItemComboBox = new JComboBox<>(ItemID.values());
        secondCraftingItemComboBox.setSize(200, 20);
        secondCraftingItemComboBox.setLocation(700, 70);
        frame.getContentPane().add(secondCraftingItemComboBox);
        JFormattedTextField secondCraftingItemQuantityTextField = new JFormattedTextField(craftingItemBounds);
        secondCraftingItemQuantityTextField.setValue(1);
        secondCraftingItemQuantityTextField.setLocation(910, 70);
        secondCraftingItemQuantityTextField.setSize(50, 20);
        frame.getContentPane().add(secondCraftingItemQuantityTextField);
        JComboBox<ItemID> thirdCraftingItemComboBox = new JComboBox<>(ItemID.values());
        thirdCraftingItemComboBox.setSize(200, 20);
        thirdCraftingItemComboBox.setLocation(700, 90);
        frame.getContentPane().add(thirdCraftingItemComboBox);
        JFormattedTextField thirdCraftingItemQuantityTextField = new JFormattedTextField(craftingItemBounds);
        thirdCraftingItemQuantityTextField.setValue(1);
        thirdCraftingItemQuantityTextField.setLocation(910, 90);
        thirdCraftingItemQuantityTextField.setSize(50, 20);
        frame.getContentPane().add(thirdCraftingItemQuantityTextField);
        JComboBox<ItemID> fourthCraftingItemComboBox = new JComboBox<>(ItemID.values());
        fourthCraftingItemComboBox.setSize(200, 20);
        fourthCraftingItemComboBox.setLocation(700, 110);
        frame.getContentPane().add(fourthCraftingItemComboBox);
        JFormattedTextField fourthCraftingItemQuantityTextField = new JFormattedTextField(craftingItemBounds);
        fourthCraftingItemQuantityTextField.setValue(1);
        fourthCraftingItemQuantityTextField.setLocation(910, 110);
        fourthCraftingItemQuantityTextField.setSize(50, 20);
        frame.getContentPane().add(fourthCraftingItemQuantityTextField);
        JComboBox<ItemID> fifthCraftingItemComboBox = new JComboBox<>(ItemID.values());
        fifthCraftingItemComboBox.setSize(200, 20);
        fifthCraftingItemComboBox.setLocation(700, 130);
        frame.getContentPane().add(fifthCraftingItemComboBox);
        JFormattedTextField fifthCraftingItemQuantityTextField = new JFormattedTextField(craftingItemBounds);
        fifthCraftingItemQuantityTextField.setValue(1);
        fifthCraftingItemQuantityTextField.setLocation(910, 130);
        fifthCraftingItemQuantityTextField.setSize(50, 20);
        frame.getContentPane().add(fifthCraftingItemQuantityTextField);

        frame.setVisible(true);
    }
}
