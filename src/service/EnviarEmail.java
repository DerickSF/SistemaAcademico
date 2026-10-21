
package service;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EnviarEmail {

    public static void enviar(String destinatario, String codigo)
            throws Exception {

        String remetente = System.getenv("GMAIL_USUARIO");
        String senhaApp = System.getenv("GMAIL_SENHA_APP");

        if (remetente == null || senhaApp == null
                || remetente.isBlank() || senhaApp.isBlank()) {
            throw new IllegalStateException(
                "Configure as variáveis do Gmail.");
        }

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.checkserveridentity", "true");

        Session session = Session.getInstance(
            props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication
                        getPasswordAuthentication() {

                    return new PasswordAuthentication(
                        remetente, senhaApp);
                }
            }
        );

        MimeMessage mensagem = new MimeMessage(session);

        mensagem.setFrom(new InternetAddress(remetente));

        mensagem.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(destinatario)
        );

        mensagem.setSubject(
            "Código de verificação - Sistema Acadêmico",
            "UTF-8"
        );

        mensagem.setText(
            "Olá!\n\n" +
            "Seu código de verificação é: " + codigo +
            "\n\nEste código expira em 5 minutos." +
            "\n\nSe você não solicitou o acesso, ignore.",
            "UTF-8"
        );

        Transport.send(mensagem);
    }
}
