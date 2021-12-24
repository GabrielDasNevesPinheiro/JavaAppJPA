package com.ql.view;

import com.ql.factory.ConnectionFactory;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Desktop extends JFrame {
    
    private JMenuItem cadastroMenu;
    private JDesktopPane desktop;
    private JMenuItem registrosMenu;
    private JMenuBar menu;
    private JMenu menuFile;
    
    //starts before the window appears
    static {
        new ConnectionFactory().getConnection();
    }

    //construct
    public Desktop() {
        initComponents();
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    // initial config
    private void initComponents() {

        desktop = new JDesktopPane();
        menu = new JMenuBar();
        menuFile = new JMenu();
        registrosMenu = new JMenuItem();
        cadastroMenu = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(new Rectangle(600, 600, 1280, 600));

        GroupLayout desktopLayout = new GroupLayout(desktop);
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
                desktopLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 880, Short.MAX_VALUE)
        );
        desktopLayout.setVerticalGroup(
                desktopLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 555, Short.MAX_VALUE)
        );

        menuFile.setText("Ações");

        registrosMenu.setText("Registros");
        menuFile.add(registrosMenu);

        cadastroMenu.setText("Cadastrar");
        
        this.initListeners();

        menuFile.add(cadastroMenu);

        menu.add(menuFile);

        setJMenuBar(menu);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        this.initLayout(layout);

        pack();
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
        
        cadastroMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cadastroMenuActionPerformed(evt);
            }
        });

        registrosMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                registrosMenuActionPerformed(evt);
            }
        });
        
    }

    private void initLayout(GroupLayout layout) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(desktop, GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(desktop)
        );
    }
    
}
