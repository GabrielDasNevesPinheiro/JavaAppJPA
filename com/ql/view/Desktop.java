package com.ql.view;

import com.ql.controller.factory.ConnectionFactory;
import java.awt.*;
import java.awt.event.*;
import javax.persistence.EntityManager;
import javax.swing.*;

public class Desktop extends JFrame {

    private JMenuItem cadastroMenu;
    private JDesktopPane desktop;
    private JMenuItem registrosMenu;
    private JMenuBar menu;
    private JMenu menuFile;
    
    private static final EntityManager manager;

    //starts before the window appears
    static {
        manager = new ConnectionFactory().getConnection().createEntityManager();
    }

    //construct
    public Desktop() {
        this.initComponents();
    }

    // initial config
    private void initComponents() {

        this.desktop = new JDesktopPane();
        this.menu = new JMenuBar();
        this.menuFile = new JMenu();
        this.registrosMenu = new JMenuItem();
        this.cadastroMenu = new JMenuItem();

        this.setExtendedState(Frame.MAXIMIZED_BOTH);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(new Rectangle(600, 600, 1280, 600));

        this.menuFile.setText("Ações");
        this.cadastroMenu.setText("Cadastrar");
        this.registrosMenu.setText("Registros");

        this.menuFile.add(registrosMenu);
        this.menuFile.add(cadastroMenu);
        this.menu.add(menuFile);
        this.setJMenuBar(menu);

        this.initListeners();

        GroupLayout layout = new GroupLayout(getContentPane());
        GroupLayout desktopLayout = new GroupLayout(desktop);
        this.getContentPane().setLayout(layout);
        this.initLayout(layout, desktopLayout);

        this.pack();
    }

    private void cadastroMenuActionPerformed(ActionEvent evt) {

        int x = this.getSize().width;
        int y = this.getSize().height;

        new Thread() {

            @Override
            public void run() {
                JInternalFrame frame = new Registro(x, y);
                frame.setResizable(false);

                if (!Registro.isOpen) {

                    desktop.add(frame);
                    frame.show();
                    Registro.isOpen = true;

                }
            }

        }.start();

    }

    private void registrosMenuActionPerformed(ActionEvent evt) {
        int x = this.getSize().width;
        int y = this.getSize().height;

        new Thread() {

            @Override
            public void run() {
                JInternalFrame frame = new MostraRegistros(x, y);
                frame.setResizable(false);

                if (!MostraRegistros.isOpen) {

                    desktop.add(frame);
                    frame.show();
                    MostraRegistros.isOpen = true;

                }
            }

        }.start();
    }

    public static void main(String args[]) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Falha ao setar look and feel");
        }

        EventQueue.invokeLater(() -> {
            new Desktop().setVisible(true);
        });
    }

    private void initListeners() {

        cadastroMenu.addActionListener(this::cadastroMenuActionPerformed);

        registrosMenu.addActionListener(this::registrosMenuActionPerformed);

    }

    private void initLayout(GroupLayout layout, GroupLayout desktopLayout) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(desktop, GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(desktop)
        );
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
                desktopLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 880, Short.MAX_VALUE)
        );
        desktopLayout.setVerticalGroup(
                desktopLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 555, Short.MAX_VALUE)
        );
    }

}
