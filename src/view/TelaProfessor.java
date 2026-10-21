
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

import model.Usuario;
import service.NotaService;

public class TelaProfessor extends JFrame {

    private final Usuario usuario;

    public TelaProfessor(Usuario usuario) {

        this.usuario = usuario;

        setTitle("Área do Professor");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout(15, 20));
        principal.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 25, 35));
        principal.setBackground(new Color(246, 247, 249));

        JLabel titulo = new JLabel(
                "ÁREA DO PROFESSOR",
                SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel nome = new JLabel(
                "Professor: " + usuario.getNome());

        JPanel topo = new JPanel(new GridLayout(2, 1, 5, 15));
        topo.setOpaque(false);
        topo.add(titulo);
        topo.add(nome);

        JPanel botoes = new JPanel(new GridLayout(4, 1, 0, 12));
        botoes.setOpaque(false);

        JButton calendario =
                new JButton("Calendário e Agendamentos");

        JButton salas = new JButton("Gerenciar Salas");
        JButton notas = new JButton("Gerenciar Notas");
        JButton sair = new JButton("Sair");

        botoes.add(calendario);
        botoes.add(salas);
        botoes.add(notas);
        botoes.add(sair);

        principal.add(topo, BorderLayout.NORTH);
        principal.add(botoes, BorderLayout.CENTER);

        setContentPane(principal);

        calendario.addActionListener(e ->
                new TelaCalendario(usuario).setVisible(true));

        salas.addActionListener(e ->
                new TelaGerenciarSalas().setVisible(true));

        notas.addActionListener(e -> gerenciarNotas());

        sair.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            dispose();
        });
    }

    private void gerenciarNotas() {

        JTextField email = new JTextField();
        JTextField disciplina = new JTextField();
        JTextField valor = new JTextField();

        Object[] campos = {
                "E-mail do aluno:", email,
                "Disciplina:", disciplina,
                "Nota (0 a 10):", valor
        };

        int opcao = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Cadastrar nota",
                JOptionPane.OK_CANCEL_OPTION);

        if (opcao != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            new NotaService().cadastrar(
                    email.getText().trim(),
                    disciplina.getText().trim(),
                    Double.parseDouble(
                            valor.getText().replace(',', '.')));

            JOptionPane.showMessageDialog(
                    this,
                    "Nota salva com sucesso.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
