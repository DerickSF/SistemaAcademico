
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import model.Usuario;
import service.AutenticacaoService;
import service.CodigoVerificacaoService;
import service.EnviarEmail;

public class TelaLogin extends JFrame {

    private final JTextField email = new JTextField(22);
    private final JPasswordField senha = new JPasswordField(22);
    private final JButton entrar = new JButton("Entrar");

    public TelaLogin() {

        setTitle("Sistema Acadêmico - Login");
        setSize(510, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(246, 247, 249));
        painel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 8, 10, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel(
                "SISTEMA ACADÊMICO",
                SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 27));
        titulo.setForeground(new Color(16, 39, 73));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        painel.add(titulo, c);

        c.gridwidth = 1;
        c.gridy = 1;
        c.gridx = 0;
        painel.add(new JLabel("E-mail:"), c);

        c.gridx = 1;
        painel.add(email, c);

        c.gridy = 2;
        c.gridx = 0;
        painel.add(new JLabel("Senha:"), c);

        c.gridx = 1;
        painel.add(senha, c);

        entrar.setBackground(new Color(39, 132, 222));
        entrar.setForeground(Color.WHITE);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        painel.add(entrar, c);

        JLabel ajuda = new JLabel(
                "<html>"
                + "Professor: professor@escola.com / 123<br>"
                + "Aluno 1: aluno1@escola.com / 123<br>"
                + "Aluno 2: aluno2@escola.com / 123"
                + "</html>");

        c.gridy = 4;
        painel.add(ajuda, c);

        setContentPane(painel);

        entrar.addActionListener(e -> login());
        senha.addActionListener(e -> login());

        getRootPane().setDefaultButton(entrar);
    }

    private void login() {

        Usuario usuario = new AutenticacaoService().autenticar(
                email.getText(),
                new String(senha.getPassword()));

        if (usuario == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "E-mail ou senha incorretos.",
                    "Falha no login",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        CodigoVerificacaoService servico =
                new CodigoVerificacaoService();

        String codigo = servico.gerarCodigo();

        // SOMENTE PARA TESTES:
        // Envia para o Gmail configurado no terminal.
        // Em produção, usar o e-mail cadastrado do usuário.
        String destinatario = System.getenv("GMAIL_USUARIO");

        if (destinatario == null || destinatario.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gmail não configurado no terminal.",
                    "Erro de configuração",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        entrar.setEnabled(false);

        new SwingWorker<Void, Void>() {

            private Exception erro;

            @Override
            protected Void doInBackground() {
                try {
                    EnviarEmail.enviar(destinatario, codigo);
                } catch (Exception e) {
                    erro = e;
                }
                return null;
            }

            @Override
            protected void done() {

                entrar.setEnabled(true);

                if (erro != null) {
                    JOptionPane.showMessageDialog(
                            TelaLogin.this,
                            "Não foi possível enviar o código:\n"
                            + erro.getMessage(),
                            "Erro no envio",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(
                        TelaLogin.this,
                        "Enviamos um código de 6 dígitos para o e-mail de teste."
                );

                TelaVerificacao verificacao =
                        new TelaVerificacao(TelaLogin.this, servico);

                verificacao.setVisible(true);

                if (!verificacao.foiConfirmado()) {
                    return;
                }

                if (usuario.isProfessor()) {
                    new TelaProfessor(usuario).setVisible(true);
                } else {
                    new TelaAluno(usuario).setVisible(true);
                }

                dispose();
            }

        }.execute();
    }
}
