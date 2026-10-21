
package app;

import service.EnviarEmail;

public class TesteEmail {

    public static void main(String[] args) {

        try {
            EnviarEmail.enviar(
                "frutamimo189@gmail.com",
                "583921"
            );

            System.out.println("E-mail enviado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao enviar e-mail:");
            e.printStackTrace();
        }
    }
}
