package com.caplin.forms;

import com.caplin.filter.FilterChain;
import com.caplin.filter.filters.IncludesAllCharactersInOrder;
import com.caplin.filter.filters.WeightByCharacterGrouping;
import com.caplin.listener.SelectionListener;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ChooseInterface extends JDialog implements KeyListener {
    private final FilterChain filterChain;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList interfacelist;
    private JTextField searchField;
    private ArrayList<String> interfaces;
    private SelectionListener listener;

    public ChooseInterface(ArrayList<String> interfaces, SelectionListener listener) {
        this.listener = listener;
        this.interfaces = interfaces;
        interfacelist.setListData(this.interfaces.toArray());
        setListIndex(0);

        this.filterChain = new FilterChain();
        this.filterChain.addFilter(new IncludesAllCharactersInOrder());
        this.filterChain.addFilter(new WeightByCharacterGrouping());

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
        Object selectedValue = interfacelist.getSelectedValue();
        if (selectedValue != null) {
            listener.onSelected((String) selectedValue);
        };
        dispose();
    }

    private void onCancel() {
        dispose();
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void filterList() {
        String searchFor = searchField.getText();
        ArrayList<String> filteredAndSortedList = this.filterChain.run(searchFor, interfaces);
        interfacelist.setListData(filteredAndSortedList.toArray());
        setListIndex(0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 40) {
            // Down arrow
            moveList(1);
        }
        else if (e.getKeyCode() == 38) {
            // Up arrow
            moveList(-1);
        } else {
            filterList();
        }

    }

    private void setListIndex(int index) {
        int size = interfacelist.getModel().getSize();
        if (size > 0 && index < size) {
            interfacelist.setSelectedIndex(index);
            interfacelist.ensureIndexIsVisible(interfacelist.getSelectedIndex());
        };
    }

    private void moveList(int i) {
        int newindex = interfacelist.getSelectedIndex() + i;
        setListIndex(newindex);
    }
}
