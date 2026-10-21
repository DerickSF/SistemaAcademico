
package view;

import javax.swing.*;
import java.awt.*;
import service.CodigoVerificacaoService;

public class TelaVerificacao extends JDialog {

    private final CodigoVerificacaoService servico;
    private boolean confirmado = false;

    public TelaVerificacao(JFrame janelaPai,
                           CodigoVerificacaoService servico) {
        super(janelaPai, "Verificação por e-mail", true);
        this.servico = servico;

        setSize(400, 230);
        setLocationRelativeTo(janelaPai);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
            "Digite o código recebido no e-mail",
            SwingConstants.CENTER
        );

        JTextField campoCodigo = new JTextField(10);
        JButton botaoConfirmar = new JButton("Confirmar código");

        JPanel centro = new JPanel(new FlowLayout());
        centro.add(campoCodigo);

        add(titulo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botaoConfirmar, BorderLayout.SOUTH);

        botaoConfirmar.addActionListener(e -> {
            String codigo = campoCodigo.getText().trim();

            if (servico.verificarCodigo(codigo)) {
                confirmado = true;
                JOptionPane.showMessageDialog(
                    this, "Código confirmado!"
                );
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Código inválido, expirado ou já utilizado."
                );
            }
        });
    }

    public boolean foiConfirmado() {
        return confirmado;
    }
}
