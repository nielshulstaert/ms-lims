/**
 * Created by IntelliJ IDEA.
 * User: Lennart
 * Date: 12-feb-2004
 * Time: 11:51:35
 */
package com.compomics.mslims.gui.dialogs;

import com.compomics.mslims.db.accessors.Instrument;
import com.compomics.mslims.gui.interfaces.Informable;
import org.apache.log4j.Logger;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * CVS information:
 *
 * $Revision: 1.2 $
 * $Date: 2008/11/28 16:07:18 $
 */

/**
 * This class shows a dialog from which an instrument can be selected.
 *
 * @author Lennart Martens
 * @version $Id: InstrumentSelectionDialog.java,v 1.2 2008/11/28 16:07:18 kenny Exp $
 */
public class InstrumentSelectionDialog extends JDialog {
    // Class specific log4j logger for InstrumentSelectionDialog instances.
    private static Logger logger = Logger.getLogger(InstrumentSelectionDialog.class);

    /**
     * The instruments from which the user can choose.
     */
    private Instrument[] iInstruments = null;

    /**
     * The parent.
     */
    private JFrame iParent = null;

    private JComboBox cmbInstruments = null;
    private JLabel lblDescription = null;
    private JButton btnSelect = null;
    private JButton btnCancel = null;
    /**
     * This checkbox identifies whether the data is processed by Mascot Distiller. As then a specific
     * SpectrumStorageEngine is used.
     */
    private JCheckBox chkMascotDistiller = null;

    /**
     * This constructor takes the parent of this dialog and the array of Instruments to present the user with.
     *
     * @param aInstruments Instrument[] with the instruments to present the user with.
     */
    public InstrumentSelectionDialog(JFrame aParent, Instrument[] aInstruments) {
        super(aParent, "Instrument selection dialog", true);
        this.iParent = aParent;
        this.iInstruments = aInstruments;
        this.constructScreen();
        this.setLocation(150, 150);
        this.pack();
    }

    /**
     * This method constructs and lays out the GUI.
     */
    private void constructScreen() {
        // Init the components.
        lblDescription = new JLabel();
        Font original = lblDescription.getFont();
        lblDescription.setFont(new Font(original.getName(), Font.ITALIC, original.getSize()));
        lblDescription.setForeground(Color.blue);
        lblDescription.setHorizontalAlignment(SwingConstants.LEFT);
        cmbInstruments = new JComboBox(iInstruments);
        cmbInstruments.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Instrument selected = (Instrument) e.getItem();
                    stateChangedInstrument(selected);
                }
            }
        });
        cmbInstruments.setMaximumSize(cmbInstruments.getPreferredSize());
        stateChangedInstrument((Instrument) cmbInstruments.getSelectedItem());

        // Now the lay-out.
        JPanel jpanCombo = new JPanel();
        jpanCombo.setLayout(new BoxLayout(jpanCombo, BoxLayout.X_AXIS));
        jpanCombo.add(Box.createHorizontalGlue());
        jpanCombo.add(cmbInstruments);
        jpanCombo.add(Box.createHorizontalGlue());

        JPanel jpanLabel = new JPanel();
        jpanLabel.setLayout(new BoxLayout(jpanLabel, BoxLayout.X_AXIS));
        jpanLabel.add(Box.createHorizontalStrut(5));
        jpanLabel.add(lblDescription);
        jpanLabel.add(Box.createHorizontalGlue());

        JPanel jpanCenter = new JPanel();
        jpanCenter.setLayout(new BoxLayout(jpanCenter, BoxLayout.Y_AXIS));
        jpanCenter.setBorder(BorderFactory.createTitledBorder("Instrument selection"));
        jpanCenter.add(Box.createVerticalStrut(10));
        jpanCenter.add(jpanCombo);
        jpanCenter.add(Box.createVerticalStrut(15));
        jpanCenter.add(jpanLabel);
        jpanCenter.add(Box.createVerticalGlue());

        JPanel jpanMain = new JPanel(new BorderLayout());
        jpanMain.add(jpanCenter, BorderLayout.CENTER);
        jpanMain.add(this.getButtonPanel(), BorderLayout.SOUTH);
        this.getContentPane().add(jpanMain);
    }

    /**
     * This method constructs and lays out the button panel.
     *
     * @return JPanel with the buttons.
     */
    private JPanel getButtonPanel() {
        // Init the buttons.

        // Checkbox indicating that the spectrum files where generated by Mascot Distiller.
        chkMascotDistiller = new JCheckBox("Mascot Distiller");
        chkMascotDistiller.setSelected(true);

        btnSelect = new JButton("Select");
        btnSelect.setMnemonic(KeyEvent.VK_S);
        btnSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectTriggered();
            }
        });
        btnSelect.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been pressed.
             */
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selectTriggered();
                }
            }
        });

        btnCancel = new JButton("Cancel");
        btnCancel.setMnemonic(KeyEvent.VK_C);
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelTriggered();
            }
        });
        btnCancel.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been pressed.
             */
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cancelTriggered();
                }
            }
        });
        // Lay out the buttons.
        JPanel jpanButtons = new JPanel();
        jpanButtons.setLayout(new BoxLayout(jpanButtons, BoxLayout.X_AXIS));
        jpanButtons.add(Box.createHorizontalStrut(10));
        jpanButtons.add(chkMascotDistiller);
        jpanButtons.add(Box.createHorizontalGlue());
        jpanButtons.add(btnSelect);
        jpanButtons.add(Box.createHorizontalStrut(5));
        jpanButtons.add(btnCancel);
        jpanButtons.add(Box.createHorizontalStrut(10));

        JPanel jpanAll = new JPanel();
        jpanAll.setLayout(new BoxLayout(jpanAll, BoxLayout.Y_AXIS));
        jpanAll.add(Box.createVerticalStrut(10));
        jpanAll.add(jpanButtons);

        return jpanAll;
    }

    /**
     * This method is called when the user changes the selection in the Instrument selection combobox. It will update
     * the lblDescription label with the description of the currently selected Instrument.
     *
     * @param aSelected Instrument that is currently selected.
     */
    private void stateChangedInstrument(Instrument aSelected) {
        String description = aSelected.getDescription();
        lblDescription.setText(description);
    }

    /**
     * This method is called whenever the user clicks 'select'.
     */
    private void selectTriggered() {
        if (iParent instanceof Informable) {
            // Inform the parent on the selected instrument.
            ((Informable) iParent).inform(cmbInstruments.getSelectedItem());
            // Inform the parent on the usage of MascotDistiller.
            ((Informable) iParent).inform(chkMascotDistiller.isSelected());
        }
        this.close();
    }

    /**
     * This method is called whenever the user clicks 'cancel'.
     */
    private void cancelTriggered() {
        if (iParent instanceof Informable) {
            ((Informable) iParent).inform(null);
        }
        this.close();
    }

    /**
     * This method takes care of closing down the dialog.
     */
    private void close() {
        this.setVisible(false);
        this.dispose();
    }
}
