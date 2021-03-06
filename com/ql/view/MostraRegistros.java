package com.ql.view;

import com.ql.controller.FuncionarioController;
import com.ql.controller.SetorController;
import com.ql.model.Funcionario;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MostraRegistros extends JInternalFrame {

    private JButton addRows;
    private JButton deleteButton;
    private JButton btRefresh;
    private JScrollPane jScrollPane1;
    private JTable tabela;
    private JLabel title;

    public static boolean isOpen = false;
    private final Funcionario f;
    FuncionarioController fc = new FuncionarioController();
    SetorController sc = new SetorController();

    public MostraRegistros() {
        this.f = new Funcionario();
        this.initComponents();
    }

    public MostraRegistros(int width, int height) {
        super("Document #",
                false, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable  

        this.f = new Funcionario();
        
        this.initComponents();
        setBounds((width - this.getSize().width) / 2, (height - this.getSize().height) / 2, 1000, 500);
        this.updateTableView();
    }

    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        tabela = new JTable();
        title = new JLabel();
        deleteButton = new JButton();
        addRows = new JButton();
        btRefresh = new JButton();

        

        addRows.setText("+");
        btRefresh.setText("Refresh");

        this.setTitle("Dados do Banco");
        this.tabela.setAutoCreateRowSorter(true);

        tabela.setModel(new DefaultTableModel(
                new Object[][]{
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String[]{
                    "id", "nome", "idade", "cpf"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabela);

        title.setFont(new Font("Tahoma", 0, 18)); // NOI18N
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setText("FUNCION??RIOS");

        deleteButton.setText("Deletar Registro");

        this.initListeners();

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        this.initLayout(layout);

        pack();
    }

    private void resetFuncionario() { // reseta os dados do funcionario(objeto que usamos para setar o funcionario selecionado)
        this.f.setCpf("");
        this.f.setId(null);
        this.f.setNome("");
        this.f.setIdade(null);
    }

    private void updateTableView() {
        List<Funcionario> lista = fc.listar(); // listando todos os funcionarios

        String[][] data = new String[lista.size()][5]; // setando um array bidirecional de acordo com os dados de funcionario

        for (int i = 0; i < data.length; i++) {
            String setor;
            try {
                setor = lista.get(i).getSetor().getNome(); // pega os setores da tabela

            } catch (NullPointerException e) {
                setor = "???"; // caso n??o haja setor, essa string ser?? mostrada
            }
            //setando os dados na array de acordo com seus ??ndices(em ordem, por conta da tabela)
            data[i] = new String[]{String.valueOf(lista.get(i).getId()),
                lista.get(i).getNome(), String.valueOf(lista.get(i).getIdade()), lista.get(i).getCpf(), setor};
        }

        tabela.setModel(new DefaultTableModel( // ordem da tabela
                data,
                new String[]{
                    "id", "nome", "idade", "cpf", "setor"
                }
        ) {
            boolean[] canEdit = new boolean[]{ // dados que podem ser editados na tabela(em ordem)
                false, true, true, true, true
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tabela.getSelectedColumn(); // sempre adiciona esse evento quando o modelo da tabela ?? atualizado
        tabela.getModel().addTableModelListener((TableModelEvent e) -> {
            TableModel model = tabela.getModel();
            this.f.setId(Integer.parseInt(model.getValueAt(e.getLastRow(), 0).toString()));
            this.f.setNome((String) model.getValueAt(e.getLastRow(), 1));
            this.f.setIdade(Integer.parseInt(model.getValueAt(e.getLastRow(), 2).toString()));
            this.f.setCpf((String) model.getValueAt(e.getLastRow(), 3));
            this.f.setSetor(sc.buscar(model.getValueAt(e.getLastRow(), 4).toString().toUpperCase()));
            this.fc.salvar(this.f);
            this.resetFuncionario();
            this.updateTableView();
        });
    }

    private void formWindowOpened(WindowEvent evt) {
        this.updateTableView();
    }

    private void deleteButtonActionPerformed(ActionEvent evt) {
        TableModel model = tabela.getModel();
        int row = tabela.getSelectedRow();
        Integer id = Integer.parseInt(model.getValueAt(row, 0).toString());
        String nome = (String) model.getValueAt(row, 1);
        Integer idade = Integer.parseInt(model.getValueAt(row, 2).toString());
        String cpf = (String) model.getValueAt(row, 3);

        this.f.setId(id);
        this.f.setNome(nome);
        this.f.setIdade(idade);
        this.f.setCpf(cpf);

        fc.deletar(this.f.getId());
        this.updateTableView();
        this.resetFuncionario();

    }

    private void addRowsActionPerformed(ActionEvent evt) {

        String nome = JOptionPane.showInputDialog("Digite o nome: ");
        Integer idade = Integer.parseInt(JOptionPane.showInputDialog("Digite a idade: "));
        String cpf = JOptionPane.showInputDialog("Digite o CPF: ");
        com.ql.model.Setor setor = sc.buscar(JOptionPane.showInputDialog("Digite o Setor: ").toUpperCase());

        this.f.setNome(nome);
        this.f.setIdade(idade);
        this.f.setCpf(cpf);
        this.f.setSetor(setor);
        this.fc.salvar(f);
        this.resetFuncionario();
        this.updateTableView();
    }

    private void btRefreshActionPerformed(ActionEvent evt) {
        this.updateTableView();

    }

    private void initListeners() {

        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                isOpen = false;
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        deleteButton.addActionListener(this::deleteButtonActionPerformed);

        addRows.addActionListener(this::addRowsActionPerformed);

        btRefresh.addActionListener(this::btRefreshActionPerformed);
    }

    private void initLayout(GroupLayout layout) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(111, 111, 111)
                                                .addComponent(title, GroupLayout.PREFERRED_SIZE, 470, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btRefresh)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(addRows)))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(title, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addRows)
                                        .addComponent(btRefresh))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 364, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

}
