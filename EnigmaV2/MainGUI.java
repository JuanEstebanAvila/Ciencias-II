import java.util.Map;
import javax.swing.SwingUtilities;

public class MainGUI {

    // Configuraciones Históricas (Wehrmacht)
    private static final String wiringI = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
    private static final char notchI = 'Q';
    private static final String wiringII = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
    private static final char notchII = 'E';
    private static final String wiringIII = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
    private static final char notchIII = 'V';

    private static final String reflectorB = "YRUHQSLDPXNGOKMIEBFZCWVJAT";

    public static void main(String[] args) {
        String clavijeroVacio = "";

        Rotor rotorIzquierdo;
        Rotor rotorCentral;
        Rotor rotorDerecho;
        String refName = "B";

        Map<String, String[]> estado = MaquinaEnigma.cargarEstado();
        if (estado != null && estado.containsKey("ROTORES") && estado.containsKey("POSICIONES")) {
            String[] nombres = estado.get("ROTORES");
            String[] posiciones = estado.get("POSICIONES");
            
            rotorIzquierdo = crearRotor(nombres[0], posiciones[0].charAt(0));
            rotorCentral = crearRotor(nombres[1], posiciones[1].charAt(0));
            rotorDerecho = crearRotor(nombres[2], posiciones[2].charAt(0));
            
            if (estado.containsKey("REFLECTOR")) {
                refName = estado.get("REFLECTOR")[0];
            }
            System.out.println("Configuración cargada correctamente.");
        } else {
            // Inicializamos los rotores por defecto (Configuración M C A)
            rotorIzquierdo = crearRotor("I", 'M');
            rotorCentral = crearRotor("II", 'C');
            rotorDerecho = crearRotor("III", 'A');
            System.out.println("Iniciando con configuración por defecto.");
        }

        // Actualmente sólo soportamos Reflector B en este simulador básico
        MaquinaEnigma enigma = new MaquinaEnigma(rotorIzquierdo, rotorCentral, rotorDerecho, reflectorB, refName, clavijeroVacio);

        // Lanzar la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            EnigmaGUI gui = new EnigmaGUI(enigma);
            gui.setVisible(true);
        });
    }

    private static Rotor crearRotor(String nombre, char posicion) {
        switch (nombre) {
            case "I": return new Rotor("I", wiringI, notchI, posicion);
            case "II": return new Rotor("II", wiringII, notchII, posicion);
            case "III": return new Rotor("III", wiringIII, notchIII, posicion);
            default: return new Rotor(nombre, wiringI, notchI, posicion); // Fallback
        }
    }
}
