
package model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nome;
    private final String email;
    private final String senha;
    private final String tipo;

    public Usuario(String nome, String email, String senha, String tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isProfessor() {
        return "professor".equalsIgnoreCase(tipo);
    }

    public boolean isAluno() {
        return "aluno".equalsIgnoreCase(tipo);
    }
}
