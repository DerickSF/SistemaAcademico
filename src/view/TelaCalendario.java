
package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import model.Agendamento;
import model.Usuario;
import service.AgendamentoService;

public class TelaCalendario extends JFrame {

    private final Usuario usuario;

    private final AgendamentoService service =
            new AgendamentoService();

    private final JComboBox<String> horario = new JComboBox<>();
    private final JComboBox<String> sala = new JComboBox<>();

    private final JTextField disciplina =
            new JTextField("Matemática", 14);

    private final JTextArea lista = new JTextArea(9, 42);

    private final JSpinner data;

    public TelaCalendario(Usuario usuario) {

        this.usuario = usuario;

        setTitle("Calendário e Agendamentos");
        setSize(720, 570);
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

        for (String s : service.listarSalas()) {
            sala.addItem(s);
        }

        lista.setEditable(false);

        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel(
                new GridLayout(0, 2, 8, 8));

        formulario.add(new JLabel("Data:"));
        formulario.add(data);

        formulario.add(new JLabel("Horário:"));
        formulario.add(horario);

        formulario.add(new JLabel("Sala:"));
        formulario.add(sala);

        if (usuario.isProfessor()) {
            formulario.add(new JLabel("Disciplina:"));
            formulario.add(disciplina);
        }

        JButton consultar = new JButton("Consultar data");
        JButton confirmar = new JButton("Confirmar agendamento");
        JButton cancelar = new JButton("Cancelar aula selecionada");
        JButton voltar = new JButton("Voltar");

        JPanel botoes = new JPanel(
                new FlowLayout(FlowLayout.LEFT));

        botoes.add(consultar);

        if (usuario.isProfessor()) {
            botoes.add(confirmar);
            botoes.add(cancelar);
        }

        botoes.add(voltar);

        principal.add(formulario, BorderLayout.NORTH);
        principal.add(new JScrollPane(lista), BorderLayout.CENTER);
        principal.add(botoes, BorderLayout.SOUTH);

        setContentPane(principal);

        consultar.addActionListener(e -> atualizarLista());
        confirmar.addActionListener(e -> agendar());
        cancelar.addActionListener(e -> cancelar());
        voltar.addActionListener(e -> dispose());

        atualizarLista();
    }

    private LocalDate dataSelecionada() {

        Date selecionada = (Date) data.getValue();

        return selecionada.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void atualizarLista() {

        StringBuilder texto =
                new StringBuilder("AULAS AGENDADAS\n\n");

        List<Agendamento> aulas =
                service.consultarData(dataSelecionada());

        for (Agendamento aula : aulas) {
            texto.append(aula).append("\n");
        }

        if (aulas.isEmpty()) {
            texto.append("Nenhuma aula nesta data.");
        }

        lista.setText(texto.toString());
    }

    private void agendar() {

        try {
            Agendamento aula = service.agendar(
                    dataSelecionada(),
                    (String) horario.getSelectedItem(),
                    (String) sala.getSelectedItem(),
                    usuario.getEmail(),
                    disciplina.getText());

            JOptionPane.showMessageDialog(
                    this,
                    "Agendamento realizado com sucesso!\n\n"
                    + "Data: "
                    + aula.getData().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + "\nHorário: "
                    + aula.getInicio() + " - " + aula.getFim()
                    + "\nSala: " + aula.getSala()
                    + "\nDisciplina: " + aula.getDisciplina(),
                    "Agendamento concluído",
                    JOptionPane.INFORMATION_MESSAGE);

            atualizarLista();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Não foi possível agendar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {

        List<Agendamento> aulas =
                service.consultarData(dataSelecionada());

        if (aulas.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não existem aulas nesta data.");
            return;
        }

        Object selecionado = JOptionPane.showInputDialog(
                this,
                "Selecione a aula que deseja cancelar:",
                "Cancelar agendamento",
                JOptionPane.QUESTION_MESSAGE,
                null,
                aulas.toArray(),
                aulas.get(0));

        if (!(selecionado instanceof Agendamento)) {
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente cancelar esta aula?",
                "Confirmar cancelamento",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            service.cancelar(
                    (Agendamento) selecionado,
                    usuario.getEmail());

            JOptionPane.showMessageDialog(
                    this,
                    "Agendamento cancelado com sucesso.");

            atualizarLista();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
