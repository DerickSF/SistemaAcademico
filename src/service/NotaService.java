
package service;

import java.util.ArrayList;
import java.util.List;

import model.Nota;

public class NotaService {

    public void cadastrar(
            String emailAluno,
            String disciplina,
            double valor) {

        Nota nota = new Nota(
                emailAluno.trim(),
                disciplina.trim(),
                valor);

        DadosService.adicionarNota(nota);
    }

    public List<Nota> consultarAluno(String email) {

        List<Nota> resultado = new ArrayList<>();

        for (Nota nota : DadosService.listarNotas()) {
            if (nota.getEmailAluno().equalsIgnoreCase(email)) {
                resultado.add(nota);
            }
        }

        return resultado;
    }

    public List<Nota> listarTodas() {
        return DadosService.listarNotas();
    }
}
