package gui;

import app.AppCore;
import lombok.Data;
import observer.Notification;
import observer.Subscriber;
import observer.enums.NotificationCode;
import querybuilder.Validator;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

@Data
public class MainFrame extends JFrame implements Subscriber {

    private static MainFrame instance = null;

    private AppCore appCore;
    private JTable jTable;
    private JScrollPane jsp;
    private JPanel bottomStatus;
    private JTextArea jTextArea;
    private JButton jButton;
    private JPanel panel;

    private MainFrame() {

    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
            instance.initialise();
        }
        return instance;
    }


    private void initialise() {
        this.setTitle("BP2021_Kosta_Lazar");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("SQL Worksheet"));

        jTextArea = new JTextArea(7, 1);
        jTextArea.setLineWrap(true);

        jButton = new JButton("Run SQL");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Upit -> validator
                Validator v = new Validator(jTextArea.getText().toLowerCase());
            }
        });
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(jButton);

        jTable = new JTable();
        jTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
        jTable.setFillsViewportHeight(true);

        panel.add(jTextArea);
        panel.add(box);
        panel.add(new JScrollPane(jTable));
        this.add(panel);
        //this.add(new JScrollPane(jTable), BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void setAppCore(AppCore appCore) {
        this.appCore = appCore;
        this.appCore.addSubscriber(this);
        this.jTable.setModel(appCore.getTableModel());
    }


    @Override
    public void update(Notification notification) {

        if (notification.getCode() == NotificationCode.RESOURCE_LOADED) {
            System.out.println(notification.getData());
        } else {
            jTable.setModel((TableModel) notification.getData());
        }

    }
}
