
package service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodigoVerificacaoService {

    private final SecureRandom random = new SecureRandom();

    private byte[] hashCodigo;
    private Instant expiracao;
    private int tentativas;
    private boolean utilizado = true;

    public String gerarCodigo() {
        int numero = random.nextInt(1_000_000);
        String codigo = String.format("%06d", numero);

        hashCodigo = criarHash(codigo);
        expiracao = Instant.now().plus(Duration.ofMinutes(5));
        tentativas = 0;
        utilizado = false;

        return codigo;
    }

    public boolean verificarCodigo(String codigo) {

        if (utilizado || hashCodigo == null
                || codigo == null
                || Instant.now().isAfter(expiracao)
                || tentativas >= 5) {
            return false;
        }

        tentativas++;

        boolean correto = MessageDigest.isEqual(
                hashCodigo, criarHash(codigo));

        if (correto) {
            utilizado = true;
            hashCodigo = null;
        }

        return correto;
    }

    private byte[] criarHash(String codigo) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            return digest.digest(
                    codigo.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
