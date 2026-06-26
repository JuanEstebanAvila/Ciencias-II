import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MaquinaEnigma {
    public static final String RUTA_ARCHIVO = "estado_enigma.txt";

    // Cableado histórico de cada rotor (Wehrmacht)
    static final String WIRING_I    = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
    static final String WIRING_II   = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
    static final String WIRING_III  = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
    static final char   NOTCH_I     = 'Q';
    static final char   NOTCH_II    = 'E';
    static final char   NOTCH_III   = 'V';
    static final String REFLECTOR_B = "YRUHQSLDPXNGOKMIEBFZCWVJAT";

    private String reflector;
    private String nombreReflector;
    private Rotor[] rotores; // [0]=Derecho, [1]=Central, [2]=Izquierdo

    public MaquinaEnigma(Rotor rotorIzquierdo, Rotor rotorCentral, Rotor rotorDerecho,
                         String reflector, String nombreReflector, String clavijero) {
        this.rotores = new Rotor[3];
        this.rotores[0] = rotorDerecho;
        this.rotores[1] = rotorCentral;
        this.rotores[2] = rotorIzquierdo;
        this.reflector = reflector;
        this.nombreReflector = nombreReflector;
    }

    public static void guardarEstado(MaquinaEnigma maquina) {
        try {
            String contenido = maquina.getConfiguracionActual().replace("|", "\n");
            Files.write(Paths.get(RUTA_ARCHIVO), contenido.getBytes());
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    public static Map<String, String[]> cargarEstado() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return null;

        Map<String, String[]> configMap = new HashMap<>();
        try {
            for (String linea : Files.readAllLines(Paths.get(RUTA_ARCHIVO))) {
                if (linea.contains("=")) {
                    String[] partes = linea.split("=", 2);
                    configMap.put(partes[0].trim(), partes[1].trim().split(","));
                }
            }
            return esConfigValida(configMap) ? configMap : null;
        } catch (IOException e) {
            return null;
        }
    }

    public static Rotor crearRotor(String nombre, char posicion) {
        switch (nombre) {
            case "I":   return new Rotor("I",   WIRING_I,   NOTCH_I,   posicion);
            case "II":  return new Rotor("II",  WIRING_II,  NOTCH_II,  posicion);
            case "III": return new Rotor("III", WIRING_III, NOTCH_III, posicion);
            default:    return new Rotor(nombre, WIRING_I,  NOTCH_I,   posicion);
        }
    }

    public static String getWiringReflector(String nombre) {
        return REFLECTOR_B;
    }

    public static MaquinaEnigma crearDesdeConfigMap(Map<String, String[]> config) {
        String[] nombres    = config.get("ROTORES");
        String[] posiciones = config.get("POSICIONES");
        String refName      = config.containsKey("REFLECTOR") ? config.get("REFLECTOR")[0] : "B";

        return new MaquinaEnigma(
                crearRotor(nombres[0], posiciones[0].charAt(0)),
                crearRotor(nombres[1], posiciones[1].charAt(0)),
                crearRotor(nombres[2], posiciones[2].charAt(0)),
                getWiringReflector(refName), refName, "");
    }

    public static MaquinaEnigma crearPorDefecto() {
        return new MaquinaEnigma(
                crearRotor("I",   'M'),
                crearRotor("II",  'C'),
                crearRotor("III", 'A'),
                REFLECTOR_B, "B", "");
    }

    // Descifra el Base64 del grupo con la clave pública. Devuelve null si la clave no coincide.
    public static Map<String, String[]> cargarDesdeConfigCifrada(String configCifrada, String datoPublico) {
        try {
            String configRaw = GestorCifradoSimple.descifrarConfiguracion(
                    configCifrada.trim(), datoPublico.trim());
            return parsearConfigRaw(configRaw);
        } catch (Exception e) {
            return null;
        }
    }

    private static Map<String, String[]> parsearConfigRaw(String configRaw) {
        Map<String, String[]> configMap = new HashMap<>();
        for (String parte : configRaw.split("\\|")) {
            if (parte.contains("=")) {
                String[] kv = parte.split("=", 2);
                configMap.put(kv[0].trim(), kv[1].trim().split(","));
            }
        }
        return esConfigValida(configMap) ? configMap : null;
    }

    private static boolean esConfigValida(Map<String, String[]> config) {
        return config.containsKey("ROTORES") && config.containsKey("POSICIONES")
                && config.get("ROTORES").length == 3 && config.get("POSICIONES").length == 3;
    }

    public void reconfigurar(Map<String, String[]> config) {
        String[] nombres    = config.get("ROTORES");
        String[] posiciones = config.get("POSICIONES");
        String refName      = config.containsKey("REFLECTOR") ? config.get("REFLECTOR")[0] : "B";

        this.rotores[2] = crearRotor(nombres[0], posiciones[0].charAt(0));
        this.rotores[1] = crearRotor(nombres[1], posiciones[1].charAt(0));
        this.rotores[0] = crearRotor(nombres[2], posiciones[2].charAt(0));
        this.nombreReflector = refName;
        this.reflector = getWiringReflector(refName);
    }

    // Aplica un string de config a la máquina. Lo usa el botón Limpiar para volver al inicio.
    public void reiniciarA(String configStr) {
        Map<String, String[]> config = parsearConfigRaw(configStr);
        if (config != null) reconfigurar(config);
    }

    private void rotarMecanismo() {
        boolean muescaRotor1 = rotores[0].estaEnMuesca();
        boolean muescaRotor2 = rotores[1].estaEnMuesca();

        rotores[0].avanzar();

        // Double stepping: si el central está en su muesca, él y el izquierdo avanzan juntos.
        if (muescaRotor2) {
            rotores[1].avanzar();
            rotores[2].avanzar();
        } else if (muescaRotor1) {
            rotores[1].avanzar();
        }
    }

    public char procesarCaracter(char letra) {
        int indice = letra - 'A';
        rotarMecanismo();
        // clavijero → 3 rotores ida → reflector → 3 rotores vuelta → clavijero
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

    private int pasarPorClavijero(int indice) {
        return indice; // sin clavijero por ahora
    }

    private int pasarPorReflector(int indice) {
        return this.reflector.charAt(indice) - 'A';
    }

    public Rotor[] getRotores() { return this.rotores; }

    public String getNombreReflector() { return this.nombreReflector; }

    // Formato de salida: "ROTORES=I,II,III|POSICIONES=M,C,A|REFLECTOR=B"
    public String getConfiguracionActual() {
        String rotoresStr    = rotores[2].getNombre() + "," + rotores[1].getNombre() + "," + rotores[0].getNombre();
        String posicionesStr = rotores[2].getLetraVisible() + "," + rotores[1].getLetraVisible() + "," + rotores[0].getLetraVisible();
        return "ROTORES=" + rotoresStr + "|POSICIONES=" + posicionesStr + "|REFLECTOR=" + this.nombreReflector;
    }
}
