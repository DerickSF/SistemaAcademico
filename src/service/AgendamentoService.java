
package service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.Agendamento;

public class AgendamentoService {

    private static final List<String> SALAS = List.of(
            "Sala 1",
            "Sala 2"
    );

    private static final List<String> HORARIOS = List.of(
            "19:00 - 19:40",
            "19:40 - 20:20",
            "20:20 - 21:00",
            "21:00 - 21:40",
            "21:40 - 22:20"
    );

    public List<String> listarSalas() {
        return new ArrayList<>(SALAS);
    }

    public List<String> listarHorarios() {
        return new ArrayList<>(HORARIOS);
    }

    public List<Agendamento> listarTodos() {
        return DadosService.listarAgendamentos();
    }

    public List<Agendamento> consultarData(LocalDate data) {

        List<Agendamento> resultado = new ArrayList<>();

        for (Agendamento a : listarTodos()) {
            if (a.getData().equals(data)) {
                resultado.add(a);
            }
        }

        resultado.sort(
                Comparator.comparing(Agendamento::getInicio));

        return resultado;
    }

    public boolean estaDisponivel(
            LocalDate data,
            String horario,
            String sala) {

        if (data == null
                || data.isBefore(LocalDate.now())
                || !HORARIOS.contains(horario)
                || !SALAS.contains(sala)) {

            return false;
        }

        LocalTime[] tempos = converterHorario(horario);

        for (Agendamento a : listarTodos()) {

            boolean mesmaData = a.getData().equals(data);
            boolean mesmaSala = a.getSala().equals(sala);

            boolean conflito =
                    tempos[0].isBefore(a.getFim())
                    && tempos[1].isAfter(a.getInicio());

            if (mesmaData && mesmaSala && conflito) {
                return false;
            }
        }

        return true;
    }

    public Agendamento agendar(
            LocalDate data,
            String horario,
            String sala,
            String professor,
            String disciplina) {

        if (data == null) {
            throw new IllegalArgumentException(
                    "Selecione uma data.");
        }

        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Não é permitido agendar em datas passadas.");
        }

        if (!HORARIOS.contains(horario)) {
            throw new IllegalArgumentException(
                    "Selecione um horário válido.");
        }

        if (!SALAS.contains(sala)) {
            throw new IllegalArgumentException(
                    "Selecione uma sala válida.");
        }

        if (disciplina == null || disciplina.isBlank()) {
            throw new IllegalArgumentException(
                    "Informe a disciplina.");
        }

        if (professor == null || professor.isBlank()) {
            throw new IllegalArgumentException(
                    "Professor não identificado.");
        }

        if (!estaDisponivel(data, horario, sala)) {
            throw new IllegalArgumentException(
                    "Esta sala já está ocupada nesse horário.");
        }

        LocalTime[] tempos = converterHorario(horario);

        Agendamento agendamento = new Agendamento(
                data,
                tempos[0],
                tempos[1],
                sala,
                professor,
                disciplina.trim());

        DadosService.adicionarAgendamento(agendamento);

        return agendamento;
    }

    public void cancelar(
            Agendamento agendamento,
            String professor) {

        if (agendamento == null) {
            throw new IllegalArgumentException(
                    "Selecione um agendamento.");
        }

        if (professor == null
                || !agendamento.getProfessor()
                        .equalsIgnoreCase(professor)) {

            throw new IllegalArgumentException(
                    "Só o professor responsável pode cancelar esta aula.");
        }

        if (!DadosService.removerAgendamento(agendamento)) {
            throw new IllegalArgumentException(
                    "O agendamento não foi encontrado.");
        }
    }

    private LocalTime[] converterHorario(String horario) {

        String[] partes = horario.split(" - ");

        return new LocalTime[] {
                LocalTime.parse(partes[0]),
                LocalTime.parse(partes[1])
        };
    }
}
