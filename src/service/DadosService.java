
package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import model.Agendamento;
import model.Nota;

public final class DadosService {

    private static final Path ARQUIVO =
            Paths.get("dados-academicos.dat");

    private static List<Agendamento> agendamentos =
            new ArrayList<>();

    private static List<Nota> notas =
            new ArrayList<>();

    static {
        carregar();
    }

    private DadosService() {
    }

    @SuppressWarnings("unchecked")
    private static void carregar() {

        if (!Files.exists(ARQUIVO)) {
            return;
        }

        try (ObjectInputStream in =
                new ObjectInputStream(Files.newInputStream(ARQUIVO))) {

            agendamentos =
                    (List<Agendamento>) in.readObject();

            notas = (List<Nota>) in.readObject();

        } catch (Exception e) {
            System.err.println(
                    "Não foi possível carregar os dados: "
                    + e.getMessage());
        }
    }

    private static void salvar() {

        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        Files.newOutputStream(ARQUIVO))) {

            out.writeObject(agendamentos);
            out.writeObject(notas);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Erro ao salvar os dados: "
                    + e.getMessage(), e);
        }
    }

    public static synchronized List<Agendamento>
            listarAgendamentos() {

        return new ArrayList<>(agendamentos);
    }

    public static synchronized void adicionarAgendamento(
            Agendamento agendamento) {

        agendamentos.add(agendamento);
        salvar();
    }

    public static synchronized boolean removerAgendamento(
            Agendamento agendamento) {

        boolean removido = agendamentos.remove(agendamento);

        if (removido) {
            salvar();
        }

        return removido;
    }

    public static synchronized List<Nota> listarNotas() {

        return new ArrayList<>(notas);
    }

    public static synchronized void adicionarNota(Nota nota) {

        notas.removeIf(n ->
                n.getEmailAluno().equalsIgnoreCase(
                        nota.getEmailAluno())
                && n.getDisciplina().equalsIgnoreCase(
                        nota.getDisciplina()));

        notas.add(nota);
        salvar();
    }
}
