package com.ql.view;

import com.ql.controller.FuncionarioController;
import com.ql.controller.SetorController;
import com.ql.model.Funcionario;

import org.netbeans.lib.awtextra.*;
import javax.swing.JOptionPane;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Registro extends JInternalFrame {
    
    // attr
    public static boolean isOpen = false;
    SetorController sc = new SetorController();
    
    // form components
    private JTextField cpfField;
    private JTextField idadeField;
    private JButton btConfirma;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JTextField nomeField;
    private JPanel painel;
    private JComboBox<String> setores;
    
    //construct
    public Registro(int width, int height) {
        super("Document #",
          false, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable   
        initComponents();
        
        this.setBounds((width - this.getSize().width) / 2, (height - this.getSize().height) / 2 , 500, 400);
    }
    
    private void listSetores() {
        java.util.List<com.ql.model.Setor> list = sc.listar();
        String[] array = new String[sc.listar().size()];
        
        for (int i = 0; i < array.length; i++) {
            array[i] = new String(list.get(i).getNome());
        }
        setores.setModel(new DefaultComboBoxModel<>(array));
    }
    
    private void initComponents() {
        painel = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        nomeField = new JTextField();
        idadeField = new JTextField();
        label3 = new JLabel();
        label4 = new JLabel();
        cpfField = new JTextField();
        btConfirma = new JButton();
        setores = new JComboBox<>();
        label5 = new JLabel();
        setTitle("Registrar funcionário");

        
        this.initListeners();
        
        
        painel.setLayout(new AbsoluteLayout());

        label1.setFont(new Font("Tahoma", 0, 24));
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setText("Registrar");
        painel.add(label1, new AbsoluteConstraints(0, 0, 400, 44));

        label2.setFont(new Font("Tahoma", 0, 14));
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setText("Nome");
        painel.add(label2, new AbsoluteConstraints(20, 110, 60, 20));

        nomeField.setColumns(2);
        nomeField.setBorder(BorderFactory.createEtchedBorder());
        
        painel.add(nomeField, new AbsoluteConstraints(80, 110, 240, 20));

        idadeField.setColumns(2);
        idadeField.setBorder(BorderFactory.createEtchedBorder());
        
        painel.add(idadeField, new AbsoluteConstraints(80, 150, 240, 20));

        label3.setFont(new Font("Tahoma", 0, 14));
        label3.setHorizontalAlignment(SwingConstants.CENTER);
        label3.setText("Idade");
        painel.add(label3, new AbsoluteConstraints(20, 150, 60, 20));

        label4.setFont(new Font("Tahoma", 0, 14));
        label4.setHorizontalAlignment(SwingConstants.CENTER);
        label4.setText("Setor");
        painel.add(label4, new AbsoluteConstraints(20, 220, 60, 20));

        cpfField.setColumns(2);
        cpfField.setBorder(BorderFactory.createEtchedBorder());
        
        painel.add(cpfField, new AbsoluteConstraints(80, 190, 240, 20));

        btConfirma.setText("Confirmar");
        btConfirma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btConfirmaActionPerformed(evt);
            }
        });
        painel.add(btConfirma, new AbsoluteConstraints(310, 270, -1, -1));

        setores.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        
        painel.add(setores, new AbsoluteConstraints(80, 220, 110, 30));

        label5.setFont(new Font("Tahoma", 0, 14));
        label5.setHorizontalAlignment(SwingConstants.CENTER);
        label5.setText("CPF");
        painel.add(label5, new AbsoluteConstraints(20, 190, 60, 20));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        this.initLayout(layout);

        pack();
    }

    private void btConfirmaActionPerformed(ActionEvent evt) {
        Funcionario f = new Funcionario();
        
        f.setNome(nomeField.getText());
        f.setIdade(Integer.parseInt(idadeField.getText()));
        f.setCpf(cpfField.getText());
        com.ql.model.Setor setor = sc.buscar(setores.getSelectedItem().toString());
        f.setSetor(setor);
        
        FuncionarioController fc = new FuncionarioController();
        
        fc.salvar(f);
        
        JOptionPane.showMessageDialog(this, "Dados inseridos!");
        Registro.isOpen = false;
        this.dispose();
    }


    private void painelComponentAdded(ContainerEvent evt) {
        this.listSetores();
    }

    private void initLayout(GroupLayout layout) {
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(painel, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(painel, GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void initListeners() {
        painel.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent evt) {
                painelComponentAdded(evt);
            }
        });
        
        this.addInternalFrameListener(new InternalFrameAdapter(){
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
               Registro.isOpen = false;  
            }
        });
        
    }
    
}
