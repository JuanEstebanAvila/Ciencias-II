import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MainGUI {

    public static void main(String[] args) {
        final String[] cmdArgs = args;
        SwingUtilities.invokeLater(() -> {
            MaquinaEnigma enigma = inicializarMaquina(cmdArgs);
            EnigmaGUI gui = new EnigmaGUI(enigma);
            gui.setVisible(true);
        });
    }

    // Orden de carga: args de consola → diálogo → archivo guardado → por defecto
    private static MaquinaEnigma inicializarMaquina(String[] args) {
        if (args.length >= 2) {
            Map<String, String[]> config = MaquinaEnigma.cargarDesdeConfigCifrada(args[0], args[1]);
            if (config != null) {
                System.out.println("Configuración cargada desde parámetros de consola.");
                return MaquinaEnigma.crearDesdeConfigMap(config);
            }
            System.out.println("No se pudo descifrar la config de los parámetros.");
        }

        MaquinaEnigma enigmaDesdeDialogo = mostrarDialogoInicial();
        if (enigmaDesdeDialogo != null) return enigmaDesdeDialogo;

        Map<String, String[]> estado = MaquinaEnigma.cargarEstado();
        if (estado != null) {
            System.out.println("Configuración cargada desde archivo guardado.");
            return MaquinaEnigma.crearDesdeConfigMap(estado);
        }

        System.out.println("Iniciando con configuración por defecto (I-M, II-C, III-A).");
        return MaquinaEnigma.crearPorDefecto();
    }

    private static MaquinaEnigma mostrarDialogoInicial() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel(
                "<html>Si tienes una <b>configuración cifrada</b> de tu grupo, pégala aquí.<br>"
                + "Cancela para usar el estado guardado o la configuración por defecto.</html>"), gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridx = 0;
        panel.add(new JLabel("Clave pública del grupo:"), gbc);
        gbc.gridx = 1;
        JTextField txtDatoPublico = new JTextField("465", 20);
        panel.add(txtDatoPublico, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Configuración cifrada:"), gbc);
        gbc.gridx = 1;
        JTextArea txtCifrado = new JTextArea(4, 30);
        txtCifrado.setLineWrap(true);
        txtCifrado.setWrapStyleWord(false);
        txtCifrado.setFont(new Font("Monospaced", Font.PLAIN, 11));
        panel.add(new JScrollPane(txtCifrado), gbc);

        String[] opciones = {"Cargar configuración", "Cancelar"};
        int resp = JOptionPane.showOptionDialog(null, panel,
                "Cargar configuración inicial cifrada",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]);

        if (resp != 0) return null;

        String cifrado = txtCifrado.getText().trim();
        String clave   = txtDatoPublico.getText().trim();

        if (cifrado.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Faltan datos. Se usará la configuración guardada o la por defecto.",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Map<String, String[]> config = MaquinaEnigma.cargarDesdeConfigCifrada(cifrado, clave);
        if (config != null) {
            System.out.println("Configuración cifrada cargada desde el diálogo.");
            return MaquinaEnigma.crearDesdeConfigMap(config);
        }

        JOptionPane.showMessageDialog(null,
                "No se pudo descifrar. Verifica la clave pública y el texto cifrado.\n"
                + "Se usará la configuración guardada o la por defecto.",
                "Error al descifrar", JOptionPane.ERROR_MESSAGE);
        return null;
    }
}
