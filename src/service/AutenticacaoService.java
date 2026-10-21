
package service;

import model.Usuario;

public class AutenticacaoService {

    public Usuario autenticar(String email, String senha) {

        if (email == null || senha == null) {
            return null;
        }

        email = email.trim();

        if (email.equalsIgnoreCase("professor@escola.com")
                && senha.equals("123")) {

            return new Usuario(
                    "João Pereira",
                    "professor@escola.com",
                    "123",
                    "professor");
        }

        if (email.equalsIgnoreCase("aluno1@escola.com")
                && senha.equals("123")) {

            return new Usuario(
                    "Maria Silva",
                    "aluno1@escola.com",
                    "123",
                    "aluno");
        }

        if (email.equalsIgnoreCase("aluno2@escola.com")
                && senha.equals("123")) {

            return new Usuario(
                    "Pedro Santos",
                    "aluno2@escola.com",
                    "123",
                    "aluno");
        }

        return null;
    }
}
