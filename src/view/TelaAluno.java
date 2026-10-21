
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.Nota;
import model.Usuario;
import service.NotaService;

public class TelaAluno extends JFrame {

    private final Usuario usuario;

    public TelaAluno(Usuario usuario) {

        this.usuario = usuario;

        setTitle("Área do Aluno");
        setSize(480, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout(20, 20));
        principal.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 25, 35));
        principal.setBackground(new Color(246, 247, 249));

        JLabel titulo = new JLabel(
                "ÁREA DO ALUNO",
                SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel nome = new JLabel(
                "Aluno: " + usuario.getNome());

        JPanel topo = new JPanel(new GridLayout(2, 1, 5, 15));
        topo.setOpaque(false);
        topo.add(titulo);
        topo.add(nome);

        JPanel botoes = new JPanel(new GridLayout(3, 1, 0, 12));
        botoes.setOpaque(false);

        JButton calendario = new JButton("Calendário e Horários");
        JButton notas = new JButton("Minhas Notas");
        JButton sair = new JButton("Sair");

        botoes.add(calendario);
        botoes.add(notas);
        botoes.add(sair);

        principal.add(topo, BorderLayout.NORTH);
        principal.add(botoes, BorderLayout.CENTER);

        setContentPane(principal);

        calendario.addActionListener(e ->
                new TelaCalendario(usuario).setVisible(true));

        notas.addActionListener(e -> mostrarNotas());

        sair.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            dispose();
        });
    }

    private void mostrarNotas() {

        List<Nota> notas =
                new NotaService().consultarAluno(usuario.getEmail());

        StringBuilder texto = new StringBuilder();

        for (Nota nota : notas) {
            texto.append(nota.getDisciplina())
                    .append(": ")
                    .append(nota.getValor())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(
                this,
                texto.length() == 0
                        ? "Nenhuma nota cadastrada."
                        : texto.toString(),
                "Minhas Notas",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
