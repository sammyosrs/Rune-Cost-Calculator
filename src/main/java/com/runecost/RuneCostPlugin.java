package com.runecost;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import static java.lang.Integer.parseInt;

@Slf4j
@PluginDescriptor(
        name = "Rune Cost",
        description = "Calculates cost of runes from certain shops in Gielinor"
)
public class RuneCostPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    public RuneCostConfig config;

    @Inject
    private ClientToolbar clientToolbar;

    private RuneCostPanel panel;
    private NavigationButton navButton;

    @Provides
    RuneCostConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(RuneCostConfig.class);
    }

    private final ShopStorage[] shops = {
            new ShopStorage("Mage Arena", 0.001f,
                    new String[][]{
                            {"Chaos Rune", "90", "250"},
                            {"Chaos Rune Pack", "9950", "3"},
                            {"Death Rune", "180", "250"},
                            {"Law Rune", "240", "250"},
                            {"Nature Rune", "250", "180"}
                    }),
            new ShopStorage("Magic Guild", 0.001f,
                    new String[][]{
                            {"Chaos Rune", "90", "250"},
                            {"Chaos Rune Pack", "9950", "35"},
                            {"Mind Rune Pack", "40", "330"},
                            {"Law Rune", "240", "250"},
                            {"Soul Rune", "300", "250"},
                            {"Death Rune", "180", "250"},
                            {"Blood Rune", "400", "250"},
                            {"Nature Rune", "250", "180"}

                    }),
            new ShopStorage("Prifddinas", 0.001f,
                    new String[][]{
                            {"Chaos Rune", "90", "250"},
                            {"Chaos Rune Pack", "9950", "35"},
                            {"Mind Rune Pack", "40", "330"},
                            {"Cosmic Rune", "250", "50"},
                            {"Law Rune", "240", "250"},
                            {"Death Rune", "180", "250"},
                            {"Blood Rune", "400", "250"},
                            {"Cosmic Rune", "50", "250"},
                            {"Nature Rune", "180", "250"}
                    }),
            new ShopStorage("Wildy Shop", 0.001f,
                    new String[][]{
                            {"Chaos Rune", "90", "500"},
                            {"Chaos Rune Pack", "9950", "35"},
                            {"Mind Rune Pack", "40", "330"},
                            {"Law Rune", "240", "250"},
                            {"Death Rune", "180", "500"},
                            {"Blood Rune", "400", "500"},
                            {"Nature Rune", "250", "180"}

                    })
    };

    @Override
    protected void startUp() throws Exception {
        panel = new RuneCostPanel(this, config);
        log.info("Loading Image...");
        BufferedImage icon = ImageUtil.loadImageResource(getClass(), "icon.png");

        navButton = NavigationButton.builder()
                .tooltip("Rune Cost")
                .priority(5)
                .panel(panel)
                .icon(icon)
                .build();

        clientToolbar.addNavigation(navButton);

        panel.getConfirmButton().addActionListener(e -> calculateRuneCost());
        panel.getShopBox().addActionListener(e -> updateShopOptions());
    }

    @Override
    protected void shutDown() throws Exception{
        // panel.removeAll();
        clientToolbar.removeNavigation(navButton);
    }

    private void calculateRuneCost() {
        String shopName = (String) panel.getShopBox().getSelectedItem();
        ShopStorage selectedShop = getShopByName(shopName);

        if (selectedShop != null) {
            String selectedItem = (String) panel.getShopOptions().getSelectedItem();
            int itemIndex = getItemIndex(selectedShop, selectedItem);

            if (itemIndex != -1) {
                int itemCost = parseInt(selectedShop.shopItems[itemIndex][1]);
                int itemStock = parseInt(selectedShop.shopItems[itemIndex][2]);
                int buyLimit = parseInt(panel.getBuyPer().getText());
                int amountWanted = parseInt(panel.getTotalWant().getText());

                if (buyLimit > itemStock) {
                    panel.setResultInfo("You entered too high of a buy per world. The " + shopName + " only has " + itemStock + " in stock");
                    return;
                }

                int[] results = calculateItem(itemCost, itemStock, selectedShop.getShopCostPer(), amountWanted, buyLimit);

                if (results != null) {
                    DecimalFormat df = new DecimalFormat("###,###,###");
                    String totalCost = df.format(results[1]);
                    String perWorld = df.format(results[0]);
                    panel.setResultInfo("Results...\n" +
                            "~Total you will spend: " + totalCost + "\n" +
                            "~Cost Per Rune: " + totalCost / amountWanted + "\n"
                            "~GP Spent Per World: " + perWorld);
                } else {
                    panel.setResultInfo("You entered too high of a buy per world. The " + shopName + " only has " + itemStock + " in stock");
                }
            }
        }
    }

    private ShopStorage getShopByName(String shopName) {
        //Goes through all the shops to check for the correct shop
        for (ShopStorage shop : shops) {
            if (shop.getShopName().equals(shopName)) {
                return shop;
            }
        }
        return null;
    }

    private int getItemIndex(ShopStorage shop, String itemName) {
        for (int i = 0; i < shop.shopItems.length; i++) {
            if (shop.shopItems[i][0].equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    public int[] calculateItem(int itemCost, int itemStock, float costPer, int amountWanted, int buyPer) {
        int[] results = new int[2];
        float nextCost = 0;
        int currentCost = itemCost;

        int totalSpent = 0;

        if (buyPer > itemStock) {
            return null;
        }

        for (int i = 1; i < buyPer + 1; i++) {
            nextCost = currentCost * (1 + costPer * (i - 1));
            totalSpent += Math.floor(nextCost);
        }

        float amountAdd = totalSpent * 0.02f;
        totalSpent += Math.floor(amountAdd);

        int realTotal = (int) (((float) amountWanted / buyPer) * totalSpent);

        results[0] = totalSpent;
        results[1] = realTotal;

        return results;
    }

    private void updateShopOptions() {
        String shopName = (String) panel.getShopBox().getSelectedItem();
        ShopStorage selectedShop = getShopByName(shopName);

        if (selectedShop != null) {
            panel.setOptions(getColumn(selectedShop.shopItems, 0));
        }
    }

    public String[] getColumn(String[][] array, int index) {
        String[] column = new String[array.length];
        for (int i = 0; i < column.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }
}

