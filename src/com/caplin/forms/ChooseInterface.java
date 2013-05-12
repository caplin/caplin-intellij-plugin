package com.caplin.forms;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ChooseInterface extends JDialog implements KeyListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList interfacelist;
    private JTextField searchField;
    private ArrayList<String> interfaces;
    public ChooseInterface(ArrayList<String> interfaces) {
        this.interfaces = interfaces;
        interfacelist.setListData(this.interfaces.toArray());

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        searchField.addKeyListener(this);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        interfacelist.getSelectedValue();
        interfacelist.getSelectionModel();
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        ChooseInterface dialog = new ChooseInterface(com.caplin.util.FileScanner.getInterfaces(null));
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void filterList() {
        String currentValue = searchField.getText().trim().toLowerCase();
        String regex = ".*";
        for (int i = 0; i < currentValue.length(); i++){
            char c = currentValue.charAt(i);
            regex = regex + c + ".*";
        }

        ArrayList<String> matches = new ArrayList<String>();

        for (int i = 0, l = interfaces.size(); i < l; i++) {
            if (interfaces.get(i).toLowerCase().matches(regex)) {
                matches.add(interfaces.get(i));
            }
        }

        interfacelist.setListData(matches.toArray());
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        filterList();
    }
}
