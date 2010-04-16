package com.compomics.mslims.gui.quantitation;

import org.apache.log4j.Logger;

import com.compomics.mslims.gui.QuantitationGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA. User: niklaas Date: 17-mrt-2009 Time: 15:44:08
 */
public class QuantitationTypeChooser extends JFrame {
    // Class specific log4j logger for QuantitationTypeChooser instances.
    private static Logger logger = Logger.getLogger(QuantitationTypeChooser.class);
    private JButton launchButton;
    private JButton launchButton1;
    private JPanel jpanContent;

    /**
     * The connection to ms_lims
     */
    private Connection iConn;
    /**
     * The database name
     */
    private String iDBName;


    public QuantitationTypeChooser(Connection aConn, String aDBName) {
        this.iConn = aConn;
        this.iDBName = aDBName;

        launchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuantitationGUI.setNotStandAlone();
                QuantitationGUI lQuantGui = new QuantitationGUI("Quantitation GUI", iConn, iDBName);
                setVisible(false);
                dispose();
            }
        });
        launchButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iTraqMs_limsStorageGui liTraq = new iTraqMs_limsStorageGui(iConn);
                setVisible(false);
                dispose();
            }
        });

        //create the jframe
        this.setTitle("Choose the quantitation import type");
        this.setContentPane(jpanContent);
        this.setSize(450, 300);
        this.setLocation(100, 100);
        this.setVisible(true);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your
     * code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jpanContent = new JPanel();
        jpanContent.setLayout(new GridBagLayout());
        final JLabel label1 = new JLabel();
        label1.setText("Choos your type of quantitation import method:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        label2.setText("Mascot distiller quantitation toolbox : ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 30;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(label2, gbc);
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(4);
        label3.setText("iTraq data from mascot dat files:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 30;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(label3, gbc);
        launchButton = new JButton();
        launchButton.setText("Launch");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(launchButton, gbc);
        launchButton1 = new JButton();
        launchButton1.setText("Launch");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        jpanContent.add(launchButton1, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanContent;
    }
}
