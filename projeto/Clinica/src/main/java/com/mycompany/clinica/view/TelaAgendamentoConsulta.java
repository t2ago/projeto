package com.mycompany.clinica.view;

import com.mycompany.clinica.controle.ConsultaControle;
import com.mycompany.clinica.controle.MedicoControle;
import com.mycompany.clinica.controle.PacienteControle;
import com.mycompany.clinica.dao.ConsultaDAO;
import com.mycompany.clinica.modelo.Consulta;
import com.mycompany.clinica.modelo.Medico;
import com.mycompany.clinica.modelo.Paciente;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class TelaAgendamentoConsulta extends javax.swing.JFrame {

    private ConsultaControle consultaControle = new ConsultaControle();
    private PacienteControle pacienteControle = new PacienteControle();
    private MedicoControle medicoControle = new MedicoControle();
    private ArrayList<Paciente> listaPacientes;
    private ArrayList<Medico> listaMedicos;
    private DefaultTableModel tabelaModelo;

    private void atualizarTabela() {
        tabelaModelo.setRowCount(0);

        ArrayList<Consulta> lista = consultaControle.listarConsultas();

        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Consulta p : lista) {
            String nomeMedico = buscarNomeMedicoPorId(p.getIdMedico());
            String nomePaciente = buscarNomePacientePorId(p.getIdPaciente());
            String dataFormatada = p.getData().format(formatoData);

            tabelaModelo.addRow(new Object[]{
                p.getId(),
                nomePaciente,
                nomeMedico,
                dataFormatada,
                p.getHora(),
                p.getObservacao()
            });
        }
    }

    private void buscarDinamicamente() {
        String texto = txtBuscar.getText().trim();
        tabelaModelo.setRowCount(0);
        LocalDate data = tentarParseData(texto);
        String nomePaciente = (data == null) ? texto : "";

        ArrayList<Consulta> lista = consultaControle.buscarPorNomeOuData(nomePaciente, data);

        for (Consulta p : lista) {
            tabelaModelo.addRow(new Object[]{
                p.getId(),
                buscarNomePacientePorId(p.getIdPaciente()),
                buscarNomeMedicoPorId(p.getIdMedico()),
                p.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                p.getHora().toString(),
                p.getObservacao()
            });
        }
    }

    private LocalDate tentarParseData(String texto) {
        List<String> formatos = Arrays.asList("dd/MM/yyyy", "yyyy-MM-dd");

        for (String formato : formatos) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
                return LocalDate.parse(texto, formatter);
            } catch (Exception e) {
                // Continua tentando o próximo formato
            }
        }

        return null;
    }

    private void preencherCampos(int linha) {
        ArrayList<Consulta> lista = consultaControle.listarConsultas();
        Consulta p = lista.get(linha);

        String nomePaciente = buscarNomePacientePorId(p.getIdPaciente());
        String nomeMedico = buscarNomeMedicoPorId(p.getIdMedico());
        String dataFormatada = p.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        txtPaciente.setText(nomePaciente);
        txtMedico.setText(nomeMedico);
        txtData.setText(dataFormatada);
        txtHora.setText(p.getHora().toString());
        txtObs.setText(p.getObservacao());
    }

    private void limparCampos() {
        txtPaciente.setText("");
        txtMedico.setText("");
        txtData.setText("");
        txtHora.setText("");
        txtObs.setText("");
        txtBuscar.setText("");
        tbListar.clearSelection();
    }

    public TelaAgendamentoConsulta() {
        initComponents();
        tabelaModelo = (DefaultTableModel) tbListar.getModel();
        carregarListas();
        listarConsultas();

        txtPaciente.addKeyListener(new KeyAdapter() {
            private String sugestao = "";

            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtPaciente.getText().trim();

                if (texto.length() > 0) {

                    ArrayList<Paciente> pacientes = pacienteControle.buscarPorNome(texto);

                    if (!pacientes.isEmpty()) {

                        sugestao = pacientes.get(0).getNome();

                        if (sugestao.toLowerCase().startsWith(texto.toLowerCase())) {
                            txtPaciente.setText(sugestao);
                            txtPaciente.setSelectionStart(texto.length());
                            txtPaciente.setSelectionEnd(sugestao.length());
                        }
                    }
                }
            }
        });

        txtMedico.addKeyListener(new KeyAdapter() {
            private String sugestao = "";

            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtMedico.getText().trim();

                if (texto.length() > 0) {
                    ArrayList<Medico> medicos = medicoControle.buscarPorNome(texto);

                    if (!medicos.isEmpty()) {
                        sugestao = medicos.get(0).getNome();

                        if (sugestao.toLowerCase().startsWith(texto.toLowerCase())) {
                            txtMedico.setText(sugestao);
                            txtMedico.setSelectionStart(texto.length());
                            txtMedico.setSelectionEnd(sugestao.length());
                        }
                    }
                }
            }
        });

        tbListar.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                    int linha = tbListar.getSelectedRow();
                    if (linha != -1) {
                        preencherCampos(linha);
                    }
                }
            }
        });

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarDinamicamente();
            }
        });
    }

    private String buscarNomeMedicoPorId(int idMedico) {
        ArrayList<Medico> medicos = medicoControle.listarMedicos();
        for (Medico m : medicos) {
            if (m.getId() == idMedico) {
                return m.getNome();
            }
        }
        return null;
    }

    private String buscarNomePacientePorId(int idPaciente) {
        ArrayList<Paciente> pacientes = pacienteControle.listarPacientes();
        for (Paciente p : pacientes) {
            if (p.getId() == idPaciente) {
                return p.getNome();
            }
        }
        return null;
    }

    private int buscarIdPacientePorNome(String nome) {
        for (Paciente p : listaPacientes) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p.getId();
            }
        }
        return -1;
    }

    private int buscarIdMedicoPorNome(String nome) {
        for (Medico p : listaMedicos) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p.getId();
            }
        }
        return -1;
    }

    private Paciente buscarPacientePorIdLocal(int id) {
        for (Paciente p : listaPacientes) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private Medico buscarMedicoPorIdLocal(int id) {
        for (Medico p : listaMedicos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private void carregarListas() {
        try {
            listaPacientes = pacienteControle.listarPacientes();
            listaMedicos = medicoControle.listarMedicos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar listas de pacientes ou médicos.");
            ex.printStackTrace();
        }
    }

    private void listarConsultas() {
        try {
            tabelaModelo.setRowCount(0);
            ArrayList<Consulta> consultas = new ConsultaDAO().listarTodos();

            DateTimeFormatter formatarData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Consulta p : consultas) {
                Paciente paciente = buscarPacientePorIdLocal(p.getIdPaciente());
                Medico medico = buscarMedicoPorIdLocal(p.getIdMedico());

                String nomePaciente = (paciente != null) ? paciente.getNome() : "Paciente não encontrado";
                String nomeMedico = (medico != null) ? medico.getNome() : "Médico não encontrado";
                String dataFormatada = (p.getData() != null) ? p.getData().format(formatarData) : "Data inválida";

                tabelaModelo.addRow(new Object[]{
                    p.getId(),
                    nomePaciente,
                    nomeMedico,
                    dataFormatada,
                    p.getHora(),
                    p.getObservacao()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar consultas.");
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbListar = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtPaciente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btAtualizar = new javax.swing.JButton();
        btRemover = new javax.swing.JButton();
        btLimpar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btAgendar = new javax.swing.JButton();
        txtMedico = new javax.swing.JTextField();
        txtHora = new javax.swing.JTextField();
        txtData = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtObs = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        btVoltar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agendamento de Consulta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        tbListar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "PACIENTE", "MÉDICO", "DATA", "HORA", "OBS"
            }
        ));
        jScrollPane1.setViewportView(tbListar);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Paciente:");

        txtPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPacienteActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Médico:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Data:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Hora:");

        btAtualizar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btAtualizar.setText("ATUALIZAR");
        btAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAtualizarActionPerformed(evt);
            }
        });

        btRemover.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btRemover.setText("REMOVER");
        btRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoverActionPerformed(evt);
            }
        });

        btLimpar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btLimpar.setText("LIMPAR");
        btLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimparActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Buscar:");

        btAgendar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btAgendar.setText("AGENDAR");
        btAgendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAgendarActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Observação:");

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        btVoltar.setText("↩");
        btVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btVoltarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPaciente)
                                    .addComponent(txtMedico))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtData, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                    .addComponent(txtHora))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btVoltar))
                            .addComponent(txtObs)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(jLabel5)
                        .addGap(7, 7, 7)
                        .addComponent(txtBuscar)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(btAgendar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btAtualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRemover)
                .addGap(18, 18, 18)
                .addComponent(btLimpar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btVoltar))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtObs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAgendar)
                    .addComponent(btAtualizar)
                    .addComponent(btRemover)
                    .addComponent(btLimpar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAtualizarActionPerformed
        int linha = tbListar.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(null, "Selecione uma consulta na tabela para alterar.");
            return;
        }

        String novaDataStr = txtData.getText();
        String novaHoraStr = txtHora.getText();
        String novaObs = txtObs.getText();
        int novoIdMedico = buscarIdMedicoPorNome(txtMedico.getText());
        int novoIdPaciente = buscarIdPacientePorNome(txtPaciente.getText());

        if (novaDataStr.isEmpty() || novaHoraStr.isEmpty() || novaObs.isEmpty() || novoIdMedico == -1 || novoIdPaciente == -1) {
            if (novoIdMedico == -1) {
                JOptionPane.showMessageDialog(null, "Médico não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (novoIdPaciente == -1) {
                JOptionPane.showMessageDialog(null, "Paciente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        try {
            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate novaData;
            try {
                novaData = LocalDate.parse(novaDataStr, formatoData);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Data inválida. Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalTime novaHora;
            try {
                novaHora = LocalTime.parse(novaHoraStr);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Hora inválida. Use o formato HH:mm.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<Consulta> lista = consultaControle.listarConsultas();
            Consulta p = lista.get(linha);

            p.setData(novaData);
            p.setHora(novaHora);
            p.setObservacao(novaObs);
            p.setIdMedico(novoIdMedico);
            p.setIdPaciente(novoIdPaciente);

            if (consultaControle.medicoOcupado(novoIdMedico, novaData, novaHora, p.getId())) {
                JOptionPane.showMessageDialog(this, "Este médico já possui uma consulta agendada neste horário!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean erro = consultaControle.atualizarConsulta(p);

            if (erro) {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar a consulta no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                tabelaModelo.setValueAt(novaData, linha, 1);
                tabelaModelo.setValueAt(novaHora, linha, 2);
                tabelaModelo.setValueAt(txtMedico.getText(), linha, 3);
                tabelaModelo.setValueAt(txtPaciente.getText(), linha, 4);
                tabelaModelo.setValueAt(novaObs, linha, 5);

                atualizarTabela();
                limparCampos();
                JOptionPane.showMessageDialog(null, "Consulta atualizada com sucesso!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro.");
        }
    }//GEN-LAST:event_btAtualizarActionPerformed

    private void btRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoverActionPerformed
        int linha = tbListar.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(null, "Selecione uma consulta para remover.");
            return;
        }

        int idConsulta = (int) tbListar.getValueAt(linha, 0);

        int confirmacao = JOptionPane.showConfirmDialog(null, "Deseja realmente remover esta consulta?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean erro = consultaControle.deletarConsulta(idConsulta);

        if (erro) {
            JOptionPane.showMessageDialog(null, "Erro ao remover a consulta.");
        } else {
            listarConsultas();
            limparCampos();
            JOptionPane.showMessageDialog(null, "Consulta removida com sucesso.");
        }
    }//GEN-LAST:event_btRemoverActionPerformed

    private void btAgendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAgendarActionPerformed
        try {
            String nomePaciente = txtPaciente.getText().trim();
            String nomeMedico = txtMedico.getText().trim();

            int idPaciente = buscarIdPacientePorNome(nomePaciente);
            int idMedico = buscarIdMedicoPorNome(nomeMedico);

            if (idPaciente == -1) {
                JOptionPane.showMessageDialog(this, "Paciente não encontrado!");
                return;
            }

            if (idMedico == -1) {
                JOptionPane.showMessageDialog(this, "Médico não encontrado!");
                return;
            }

            DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

            LocalDate data;
            LocalTime hora;
            try {
                data = LocalDate.parse(txtData.getText().trim(), formatterData);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                hora = LocalTime.parse(txtHora.getText().trim(), formatterHora);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Hora inválida. Use o formato HH:mm.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String obs = txtObs.getText().trim();

            Consulta p = new Consulta();
            p.setIdPaciente(idPaciente);
            p.setIdMedico(idMedico);
            p.setData(data);
            p.setHora(hora);
            p.setObservacao(obs);

            ConsultaDAO dao = new ConsultaDAO();

            if (dao.consultaIgual(p)) {
                JOptionPane.showMessageDialog(this, "Esta consulta já está agendada com as mesmas informações!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dao.medicoOcupado(idMedico, data, hora)) {
                JOptionPane.showMessageDialog(this, "Este médico já possui uma consulta agendada neste horário!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new ConsultaDAO().inserir(p);
            JOptionPane.showMessageDialog(this, "Consulta agendada com sucesso!");
            limparCampos();
            listarConsultas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao agendar.");
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btAgendarActionPerformed

    private void txtPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPacienteActionPerformed

    }//GEN-LAST:event_txtPacienteActionPerformed

    private void btVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btVoltarActionPerformed
        new TelaInicial().setVisible(true);
        dispose();
    }//GEN-LAST:event_btVoltarActionPerformed

    private void btLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimparActionPerformed
        limparCampos();
        atualizarTabela();
    }//GEN-LAST:event_btLimparActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed

    }//GEN-LAST:event_txtBuscarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaAgendamentoConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaAgendamentoConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaAgendamentoConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaAgendamentoConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaAgendamentoConsulta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAgendar;
    private javax.swing.JButton btAtualizar;
    private javax.swing.JButton btLimpar;
    private javax.swing.JButton btRemover;
    private javax.swing.JButton btVoltar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbListar;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextField txtMedico;
    private javax.swing.JTextField txtObs;
    private javax.swing.JTextField txtPaciente;
    // End of variables declaration//GEN-END:variables
}
