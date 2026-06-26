import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MaquinaEnigma {
    public static final String RUTA_ARCHIVO = "estado_enigma.txt";

    public static void guardarEstado(MaquinaEnigma maquina) {
        try {
            String contenido = maquina.getConfiguracionActual().replace("|", "\n");
            Files.write(Paths.get(RUTA_ARCHIVO), contenido.getBytes());
        } catch (IOException e) {
            System.err.println("Error al guardar la configuración: " + e.getMessage());
        }
    }

    public static Map<String, String[]> cargarEstado() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return null;

        Map<String, String[]> configMap = new HashMap<>();
        try {
            for (String linea : Files.readAllLines(Paths.get(RUTA_ARCHIVO))) {
                if (linea.contains("=")) {
                    String[] partes = linea.split("=");
                    configMap.put(partes[0].trim(), partes[1].trim().split(","));
                }
            }
            return configMap;
        } catch (IOException e) {
            return null;
        }
    }
    private String reflector;
    private String nombreReflector;

    private Rotor[] rotores;

    /**
     * @param rotorIzquierdo El rotor en la posición más lenta (Rotor 3)
     * @param rotorCentral   El rotor en la posición media (Rotor 2)
     * @param rotorDerecho   El rotor en la posición más rápida (Rotor 1)
     * @param reflector      Configuración del reflector
     * @param nombreReflector Nombre del reflector (ej. "B")
     * @param clavijero      Configuración del clavijero
     */
    public MaquinaEnigma(Rotor rotorIzquierdo, Rotor rotorCentral, Rotor rotorDerecho, String reflector,
            String nombreReflector, String clavijero) {
        this.rotores = new Rotor[3];
        this.rotores[0] = rotorDerecho;
        this.rotores[1] = rotorCentral;
        this.rotores[2] = rotorIzquierdo;

        this.reflector = reflector;
        this.nombreReflector = nombreReflector;
    }

    private void rotarMecanismo() {

        boolean muescaRotor1 = rotores[0].estaEnMuesca();
        boolean muescaRotor2 = rotores[1].estaEnMuesca();

        rotores[0].avanzar();

        if (muescaRotor2) {
            rotores[1].avanzar();
            rotores[2].avanzar();
        }

        else if (muescaRotor1) {
            rotores[1].avanzar();
        }
    }

    /**
     * @param letra Letra de entrada pulsada en el teclado.
     * @return Letra de salida iluminada en el tablero.
     */
    public char procesarCaracter(char letra) {

        int indice = letra - 'A';

        rotarMecanismo();

        indice = pasarPorClavijero(indice);

        indice = rotores[0].cifrarIda(indice);
        indice = rotores[1].cifrarIda(indice);
        indice = rotores[2].cifrarIda(indice);

        indice = pasarPorReflector(indice);

        indice = rotores[2].cifrarVuelta(indice);
        indice = rotores[1].cifrarVuelta(indice);
        indice = rotores[0].cifrarVuelta(indice);

        indice = pasarPorClavijero(indice);

        return (char) (indice + 'A');
    }

    /**
     * @param indice Señal de entrada (0-25)
     * @return Señal de salida intercambiada (0-25)
     */
    private int pasarPorClavijero(int indice) {
        return indice;
    }

    /**
     * @param indice Señal de entrada desde el Rotor 3 (0-25)
     * @return Señal de salida rebotada hacia el Rotor 3 (0-25)
     */
    private int pasarPorReflector(int indice) {
        // En el reflector, el carácter en el índice se sustituye por su par
        return this.reflector.charAt(indice) - 'A';
    }

    /**
     * @return Arreglo de los 3 rotores (0: Derecho, 1: Central, 2: Izquierdo).
     */
    public Rotor[] getRotores() {
        return this.rotores;
    }

    public String getNombreReflector() {
        return this.nombreReflector;
    }

    /**
     * Obtiene la configuración actual en formato de texto.
     * Ejemplo: "ROTORES=I,II,III|POSICIONES=M,C,A|REFLECTOR=B"
     */
    public String getConfiguracionActual() {
        String rotoresStr = rotores[2].getNombre() + "," + rotores[1].getNombre() + "," + rotores[0].getNombre();
        String posicionesStr = rotores[2].getLetraVisible() + "," + rotores[1].getLetraVisible() + "," + rotores[0].getLetraVisible();
        return "ROTORES=" + rotoresStr + "|POSICIONES=" + posicionesStr + "|REFLECTOR=" + this.nombreReflector;
    }
}
