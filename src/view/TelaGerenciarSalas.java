
package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;

import model.Agendamento;
import service.AgendamentoService;

public class TelaGerenciarSalas extends JFrame {

    private final AgendamentoService service =
            new AgendamentoService();

    private final JSpinner data;

    private final JComboBox<String> horario =
            new JComboBox<>();

    private final JTextArea resultado =
            new JTextArea(12, 35);

    public TelaGerenciarSalas() {

        setTitle("Gerenciar Salas");
        setSize(560, 440);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        SpinnerDateModel modelo = new SpinnerDateModel(
                new Date(),
                null,
                null,
                Calendar.DAY_OF_MONTH);

        data = new JSpinner(modelo);

        data.setEditor(
                new JSpinner.DateEditor(data, "dd/MM/yyyy"));

        for (String h : service.listarHorarios()) {
            horario.addItem(h);
        }

        resultado.setEditable(false);

        JPanel topo = new JPanel(
                new GridLayout(0, 2, 8, 8));

        topo.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 10, 15));

        topo.add(new JLabel("Data:"));
        topo.add(data);

        topo.add(new JLabel("Horário:"));
        topo.add(horario);

        JButton consultar =
                new JButton("Consultar disponibilidade");

        JButton fechar = new JButton("Fechar");

        topo.add(consultar);
        topo.add(fechar);

        setLayout(new BorderLayout());

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(resultado), BorderLayout.CENTER);

        consultar.addActionListener(e -> consultar());
        fechar.addActionListener(e -> dispose());

        consultar();
    }

    private void consultar() {

        Date selecionada = (Date) data.getValue();

        LocalDate dia = selecionada.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        String horarioSelecionado =
                (String) horario.getSelectedItem();

        StringBuilder texto =
                new StringBuilder("DISPONIBILIDADE DAS SALAS\n\n");

        for (String sala : service.listarSalas()) {

            boolean disponivel = service.estaDisponivel(
                    dia,
                    horarioSelecionado,
                    sala);

            texto.append(sala)
                    .append(": ")
                    .append(disponivel
                            ? "DISPONÍVEL"
                            : "OCUPADA")
                    .append("\n");

            for (Agendamento aula :
                    service.consultarData(dia)) {

                if (aula.getSala().equals(sala)
                        && aula.getInicio().toString()
                                .equals(horarioSelecionado.substring(0, 5))) {

                    texto.append("   ")
                            .append(aula.getInicio())
                            .append(" - ")
                            .append(aula.getFim())
                            .append(" | ")
                            .append(aula.getDisciplina())
                            .append("\n");
                }
            }

            texto.append("\n");
        }

        resultado.setText(texto.toString());
    }
}
