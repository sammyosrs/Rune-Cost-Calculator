package com.runecost;


import java.awt.*;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;


@Slf4j

public class RuneCostPanel extends PluginPanel{



    private static final Dimension PREFERRED_SIZE = new Dimension(PluginPanel.PANEL_WIDTH - 100, 30);
    private static final Dimension MINIMUM_SIZE = new Dimension(0, 30);

    private final Dimension RESULT_SIZE = new Dimension(PluginPanel.PANEL_WIDTH - 5, 100);
    String[] options = {"Select Shop"};
    String[] shops = {"Mage Arena", "Magic Guild", "Prifddinas", "Mage of Zamorak", "Baba Yaga", "Runic Emperium"};

    public JTextField getTotalWant() {
        return totalWant;
    }

    public void setResultInfo(String result){
        resultInfo.setText(result);
    }
    private final JTextField totalWant = new JTextField("Total Runes");

    public JTextField getBuyPer(){
        return buyPer;
    }
    private final JTextField buyPer = new JTextField("Buy Per World");


    private final JTextArea resultInfo = new JTextArea(" Waiting for Calculation...");


    public JComboBox<String> getShopOptions() {
        return shopOptions;
    }

    public void setOptions(String[] options) {
        shopOptions.removeAllItems();
        for(String s:options)
            shopOptions.addItem(s);
    }

    private final JComboBox<String> shopOptions = new JComboBox<String>(options);
    private final JComboBox<String> shopsBox = new JComboBox<String>(shops);

    public JButton getConfirmButton() {
        return confirmButton;
    }

    private final JButton confirmButton = new JButton("Submit");
    private GridBagConstraints c;
    @Inject
   public RuneCostPanel(final RuneCostPlugin plugin, RuneCostConfig config)
    {
        super(true);
        setBorder(new EmptyBorder(18, 10, 0, 10));
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 5, 0);


        totalWant.setPreferredSize(PREFERRED_SIZE);
        totalWant.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        totalWant.setBackground(ColorScheme.DARK_GRAY_COLOR);
        // totalWant.setOpaque(true);
        // totalWant.setMinimumSize(MINIMUM_SIZE);

        resultInfo.setForeground(ColorScheme.LIGHT_GRAY_COLOR);


        totalWant.addMouseListener(new Hover("Total Runes"));

        add(totalWant, c);
        c.gridy++;


        c.gridy = 0;
        c.gridx = 1;
        buyPer.setPreferredSize(PREFERRED_SIZE);
        buyPer.setForeground(ColorScheme.LIGHT_GRAY_COLOR);

        buyPer.addMouseListener(new Hover("Buy Per"));

        add(buyPer,c);

        c.gridx = 0;
        c.gridy++;
        shopOptions.setSize(400,400);
        add(shopOptions,c);
        c.gridy++;

        c.gridx = 1;
        c.gridy = 1;
        shopsBox.setSize(200,200);
        shopsBox.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        add(shopsBox, c);


        c.gridy++;

        c.gridx = 0;
        c.gridwidth = 2;
        confirmButton.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        add(confirmButton,c);
        c.gridy++;


        c.gridx = 0;
        c.gridwidth = 2;
        c.gridheight = 10;
        c.insets = new Insets(20, 0, 50, 0);

        resultInfo.setFont(resultInfo.getFont().deriveFont(16f));

        resultInfo.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        resultInfo.setFocusable(false);
        resultInfo.setEditable(false);
        resultInfo.setLineWrap(true);
        resultInfo.setWrapStyleWord(true);
        // resultInfo.setBackground(Color.lightGray);
        add(resultInfo,c);
        c.gridy++;
        log.info("Panel Ran");

    }


    private void calculatePrice()
    {
        //TODO: THIS
    }

    public JComboBox<String> getShopBox() {
        return shopsBox;
    }
}
