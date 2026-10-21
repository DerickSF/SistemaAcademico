package model;

import java.io.Serializable;

public class Nota implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String emailAluno;
    private final String disciplina;
    private final double valor;

    public Nota(String emailAluno, String disciplina, double valor) {

        if (emailAluno == null || emailAluno.isBlank()
                || disciplina == null || disciplina.isBlank()) {
            throw new IllegalArgumentException(
                    "Aluno e disciplina são obrigatórios.");
        }

        if (!Double.isFinite(valor) || valor < 0 || valor > 10) {
            throw new IllegalArgumentException(
                    "A nota deve estar entre 0 e 10.");
        }

        this.emailAluno = emailAluno;
        this.disciplina = disciplina;
        this.valor = valor;
    }

    public String getEmailAluno() {
        return emailAluno;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public double getValor() {
        return valor;
    }
}
