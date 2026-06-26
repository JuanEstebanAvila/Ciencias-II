import java.util.Base64;
import java.nio.charset.StandardCharsets;

// XOR byte a byte con la clave, luego Base64 para que el resultado sea texto copiable.
// No es cifrado serio, solo evita que la config se lea a simple vista.
public class GestorCifradoSimple {

    public static String cifrarConfiguracion(String datos, String clave) {
        byte[] txtBytes   = datos.getBytes(StandardCharsets.UTF_8);
        byte[] claveBytes = clave.getBytes(StandardCharsets.UTF_8);
        byte[] resultado  = new byte[txtBytes.length];

        for (int i = 0; i < txtBytes.length; i++) {
            resultado[i] = (byte) (txtBytes[i] ^ claveBytes[i % claveBytes.length]);
        }

        return Base64.getEncoder().encodeToString(resultado);
    }

    // XOR es simétrico: la misma operación con la misma clave devuelve el original.
    public static String descifrarConfiguracion(String datosBase64, String clave) {
        byte[] cifradoBytes = Base64.getDecoder().decode(datosBase64);
        byte[] claveBytes   = clave.getBytes(StandardCharsets.UTF_8);
        byte[] resultado    = new byte[cifradoBytes.length];

        for (int i = 0; i < cifradoBytes.length; i++) {
            resultado[i] = (byte) (cifradoBytes[i] ^ claveBytes[i % claveBytes.length]);
        }

        return new String(resultado, StandardCharsets.UTF_8);
    }
}
