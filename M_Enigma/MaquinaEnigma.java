public class MaquinaEnigma {
    private String clavijero;
    // El reflector es un mapa simétrico (ej: "YRUHQSLDPXNGOKMIEBFZCWVJAT")
    private String reflector;

    private Rotor[] rotores;

    /**
     * @param rotorIzquierdo El rotor en la posición más lenta (Rotor 3)
     * @param rotorCentral   El rotor en la posición media (Rotor 2)
     * @param rotorDerecho   El rotor en la posición más rápida (Rotor 1)
     * @param reflector      Configuración del reflector
     * @param clavijero      Configuración del clavijero
     */
    public MaquinaEnigma(Rotor rotorIzquierdo, Rotor rotorCentral, Rotor rotorDerecho, String reflector,
            String clavijero) {
        this.rotores = new Rotor[3];
        this.rotores[0] = rotorDerecho;
        this.rotores[1] = rotorCentral;
        this.rotores[2] = rotorIzquierdo;

        this.reflector = reflector;
        this.clavijero = clavijero;
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
        return indice;
    }
}
