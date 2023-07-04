package com.runecost;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import net.runelite.client.ui.ColorScheme;

public class Hover extends MouseAdapter {

    private String lblStr = "";

    public Hover( String lblStr ) {
        this.lblStr = lblStr;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JTextField area = (JTextField) e.getComponent();
        if(area.hasFocus())
            return;
        // area.setForeground(new Color(255, 127, 80));
        area.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        // area.setText( String.format( "<html><u>%s</u></html>", lblStr) );
        area.setText("");

    }

    @Override
    public void mouseExited(MouseEvent e) {
        JTextField area = (JTextField) e.getComponent();
        if(area.hasFocus())
            return;
        // area.setText( String.format( "<html><u>%s</u><</html>", lblStr ) );
        //area.setForeground( new Color(245, 245, 245) );
        area.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        area.setText(this.lblStr);
    }
}