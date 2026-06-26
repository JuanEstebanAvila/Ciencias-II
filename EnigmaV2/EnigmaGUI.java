import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.Map;

public class EnigmaGUI extends JFrame {
    private MaquinaEnigma maquina;
    private JLabel[] rotoresLabels;
    private Map<Character, JLabel> lamparas;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private final String[] tecladoLayout = {
            "QWERTZUIO",
            "ASDFGHJK",
            "PYXCVBNML"
    };
    private Map<Character, JButton> botonesTeclado;
    private Map<Character, Boolean> teclasPresionadas = new HashMap<>();
    // Posición de los rotores al inicio. Se usa al exportar la config y al limpiar.
    private String configInicialStr;

    public EnigmaGUI(MaquinaEnigma maquina) {
        this.maquina = maquina;
        setTitle("Simulador de Máquina Enigma");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MaquinaEnigma.guardarEstado(maquina);
                System.exit(0);
            }
        });
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        // Panel superior: ventanilla de los rotores
        JPanel panelRotores = new JPanel();
        panelRotores.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Rotores (Izquierdo, Central, Derecho)"));
        rotoresLabels = new JLabel[3];
        for (int i = 2; i >= 0; i--) {
            rotoresLabels[i] = new JLabel("A", SwingConstants.CENTER);
            rotoresLabels[i].setFont(new Font("Monospaced", Font.BOLD, 36));
            rotoresLabels[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY, 3),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            rotoresLabels[i].setPreferredSize(new Dimension(60, 70));
            rotoresLabels[i].setOpaque(true);
            rotoresLabels[i].setBackground(Color.WHITE);
            panelRotores.add(rotoresLabels[i]);
            if (i > 0) panelRotores.add(Box.createRigidArea(new Dimension(30, 0)));
        }

        // Panel central: tablero de lámparas + teclado
        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelLamparas = new JPanel();
        panelLamparas.setLayout(new BoxLayout(panelLamparas, BoxLayout.Y_AXIS));
        panelLamparas.setBorder(BorderFactory.createTitledBorder("Tablero de Lámparas (Lightboard)"));
        lamparas = new HashMap<>();

        for (String filaStr : tecladoLayout) {
            JPanel filaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
            for (char c : filaStr.toCharArray()) {
                JLabel lampara = new JLabel(String.valueOf(c), SwingConstants.CENTER);
                lampara.setPreferredSize(new Dimension(45, 45));
                lampara.setOpaque(true);
                lampara.setBackground(new Color(40, 40, 40));
                lampara.setForeground(new Color(150, 150, 150));
                lampara.setFont(new Font("SansSerif", Font.BOLD, 18));
                lampara.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                lamparas.put(c, lampara);
                filaPanel.add(lampara);
            }
            panelLamparas.add(filaPanel);
        }

        JPanel panelTeclado = new JPanel();
        panelTeclado.setLayout(new BoxLayout(panelTeclado, BoxLayout.Y_AXIS));
        panelTeclado.setBorder(BorderFactory.createTitledBorder("Teclado"));

        botonesTeclado = new HashMap<>();
        for (String filaStr : tecladoLayout) {
            JPanel filaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
            for (char c : filaStr.toCharArray()) {
                JButton boton = new JButton(String.valueOf(c));
                boton.setPreferredSize(new Dimension(50, 50));
                boton.setFont(new Font("SansSerif", Font.BOLD, 16));
                boton.setFocusPainted(false);
                botonesTeclado.put(c, boton);

                boton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) { procesarLetra(c); }
                    @Override
                    public void mouseReleased(MouseEvent e) { apagarLamparas(); }
                });

                filaPanel.add(boton);
            }
            panelTeclado.add(filaPanel);
        }

        panelCentral.add(panelLamparas);
        panelCentral.add(panelTeclado);
        configurarKeyBindings();

        // Panel inferior: historial de texto
        JPanel panelTexto = new JPanel(new GridLayout(2, 1, 5, 5));
        panelTexto.setBorder(BorderFactory.createTitledBorder("Historial de Mensaje"));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Entrada: "), BorderLayout.WEST);
        inputArea = new JTextArea(2, 30);
        inputArea.setEditable(false);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Salida:  "), BorderLayout.WEST);
        outputArea = new JTextArea(2, 30);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        panelTexto.add(inputPanel);
        panelTexto.add(outputPanel);

        // Controles
        JPanel panelControles = new JPanel(new FlowLayout());
        JLabel lblClave       = new JLabel("Clave pública:");
        JTextField txtDatoPublico = new JTextField("465", 10);
        JButton btnExportarConfig = new JButton("Copiar Config Cifrada");
        JButton btnGuardar        = new JButton("Guardar Estado");
        JButton btnLimpiar        = new JButton("Limpiar");

        panelControles.add(lblClave);
        panelControles.add(txtDatoPublico);
        panelControles.add(btnExportarConfig);
        panelControles.add(btnGuardar);
        panelControles.add(btnLimpiar);

        // Exporta la config del INICIO (no la actual) para que quien la reciba pueda descifrar.
        btnExportarConfig.addActionListener(e -> {
            String clave = txtDatoPublico.getText();
            if (clave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa la clave pública (ej. 465) antes de copiar.");
                return;
            }
            String cifrado = GestorCifradoSimple.cifrarConfiguracion(configInicialStr, clave);

            JTextArea ta = new JTextArea(5, 30);
            ta.setText(cifrado);
            ta.setLineWrap(true);
            ta.setEditable(false);

            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(cifrado), null);
            JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                    "Configuración cifrada — copiada al portapapeles", JOptionPane.INFORMATION_MESSAGE);
        });

        btnGuardar.addActionListener(e -> {
            MaquinaEnigma.guardarEstado(maquina);
            JOptionPane.showMessageDialog(this, "Estado guardado en estado_enigma.txt");
        });

        // Vuelve al inicio de sesión y borra el historial. Para descifrar: limpiar y escribir el cifrado.
        btnLimpiar.addActionListener(e -> {
            maquina.reiniciarA(configInicialStr);
            inputArea.setText("");
            outputArea.setText("");
            apagarLamparas();
            actualizarRotoresVisuales();
        });

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelTexto, BorderLayout.CENTER);
        panelInferior.add(panelControles, BorderLayout.SOUTH);

        add(panelRotores, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        actualizarRotoresVisuales();

        configInicialStr = maquina.getConfiguracionActual();
    }

    private void procesarLetra(char letra) {
        inputArea.append(String.valueOf(letra));
        char resultado = maquina.procesarCaracter(letra);
        outputArea.append(String.valueOf(resultado));
        actualizarRotoresVisuales();
        encenderLampara(resultado);
    }

    private void actualizarRotoresVisuales() {
        Rotor[] rotores = maquina.getRotores();
        for (int i = 0; i < 3; i++) {
            if (rotores[i] != null)
                rotoresLabels[i].setText(String.valueOf(rotores[i].getLetraVisible()));
        }
    }

    private void encenderLampara(char letra) {
        JLabel lampara = lamparas.get(letra);
        if (lampara != null) {
            lampara.setBackground(Color.YELLOW);
            lampara.setForeground(Color.BLACK);
        }
    }

    private void apagarLamparas() {
        for (JLabel lampara : lamparas.values()) {
            lampara.setBackground(new Color(40, 40, 40));
            lampara.setForeground(new Color(150, 150, 150));
        }
    }

    private void configurarKeyBindings() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        for (char c = 'A'; c <= 'Z'; c++) {
            final char letra = c;
            String keyStrokePress   = "pressed_"  + letra;
            String keyStrokeRelease = "released_" + letra;

            im.put(KeyStroke.getKeyStroke(Character.toLowerCase(letra)), keyStrokePress);
            im.put(KeyStroke.getKeyStroke(letra), keyStrokePress);

            // teclasPresionadas evita que mantener la tecla disparare la letra varias veces
            am.put(keyStrokePress, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (teclasPresionadas.getOrDefault(letra, false)) return;
                    teclasPresionadas.put(letra, true);
                    JButton btn = botonesTeclado.get(letra);
                    if (btn != null) btn.getModel().setPressed(true);
                    procesarLetra(letra);
                }
            });

            im.put(KeyStroke.getKeyStroke("released " + Character.toLowerCase(letra)), keyStrokeRelease);
            im.put(KeyStroke.getKeyStroke("released " + letra), keyStrokeRelease);

            am.put(keyStrokeRelease, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    teclasPresionadas.put(letra, false);
                    JButton btn = botonesTeclado.get(letra);
                    if (btn != null) btn.getModel().setPressed(false);
                    apagarLamparas();
                }
            });
        }
    }
}
