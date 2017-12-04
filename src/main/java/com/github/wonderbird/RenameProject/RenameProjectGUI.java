package com.github.wonderbird.RenameProject;

import javax.swing.*;
import java.awt.*;

class RenameProjectGUI extends JFrame {
    private JLabel fromLabel;
    private JTextField fromTextField;
    private JLabel toLabel;
    private JTextField toTextField;
    private JButton cancelButton;
    private JButton renameButton;

    RenameProjectGUI() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rename Project");

        fromLabel = new JLabel("From:");
        fromTextField = new JTextField();
        toLabel = new JLabel("To:");
        toTextField = new JTextField();
        cancelButton = new JButton("Cancel");
        renameButton = new JButton("Rename");

        renameButton.addActionListener((aEvent) -> onRename());
        cancelButton.addActionListener((aEvent) -> onCancel());

        GridLayout layout = new GridLayout(0, 2);
        setLayout(layout);
        add(fromLabel);
        add(fromTextField);
        add(toLabel);
        add(toTextField);
        add(cancelButton);
        add(renameButton);

        pack();
    }

    private void onRename() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
