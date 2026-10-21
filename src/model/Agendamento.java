
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Agendamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LocalDate data;
    private final LocalTime inicio;
    private final LocalTime fim;
    private final String sala;
    private final String professor;
    private final String disciplina;

    public Agendamento(
            LocalDate data,
            LocalTime inicio,
            LocalTime fim,
            String sala,
            String professor,
            String disciplina) {

        this.data = data;
        this.inicio = inicio;
        this.fim = fim;
        this.sala = sala;
        this.professor = professor;
        this.disciplina = disciplina;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

    public String getSala() {
        return sala;
    }

    public String getProfessor() {
        return professor;
    }

    public String getDisciplina() {
        return disciplina;
    }

    @Override
    public String toString() {
        return data + " | "
                + inicio + "-" + fim + " | "
                + sala + " | "
                + disciplina + " | "
                + professor;
    }
}
