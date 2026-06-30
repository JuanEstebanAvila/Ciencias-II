package Grafos.ui;

import javax.swing.*;

import Grafos.algoritmos.ArbolRecubrimiento.*;
import Grafos.algoritmos.CaminosCortos.*;
import Grafos.algoritmos.Coloreado.*;
import Grafos.algoritmos.EnvolventeConexo.*;
import Grafos.algoritmos.FlujoMaximo.*;
import Grafos.core.*;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class InterfazGrafo extends JFrame {
    private GrafoUniversal miGrafo;
    private JPanel panelLienzo;

    // Estados de visualizaciÃ³n
    private List<Arista> aristasResaltadas = new ArrayList<>();
    private List<Nodo> envolventeResaltada = new ArrayList<>();
    private boolean mostrarFlujo = false;
    private boolean usarLetras = false;

    // Paleta de colores para el coloreado de grafos
    private static final Color[] paleta = {
            new Color(255, 99, 71), // Tomato
            new Color(50, 205, 50), // Lime Green
            new Color(255, 215, 0), // Gold
            new Color(138, 43, 226), // Blue Violet
            new Color(255, 20, 147), // Deep Pink
            new Color(0, 206, 209), // Dark Turquoise
            new Color(255, 69, 0), // Orange Red
            new Color(154, 205, 50), // Yellow Green
            new Color(0, 128, 128), // Teal
            new Color(238, 130, 238) // Violet
    };

    public InterfazGrafo(GrafoUniversal grafo) {
        this.miGrafo = grafo;
        setTitle("Laboratorio Visual de Grafos - CIENCIAS II");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------------------------------------------------------
        // 1. PANEL SUPERIOR: CONTROLES DE EDICIÃ“N RÃPIDA
        // ---------------------------------------------------------
        JPanel panelControles = new JPanel();
        panelControles.setBackground(new Color(240, 240, 240));

        JButton btnAnadirNodo = new JButton("Anadir Nodo");
        JButton btnAnadirArista = new JButton("Anadir Arista");
        JButton btnEliminarNodo = new JButton("Eliminar Nodo");
        JButton btnEliminarArista = new JButton("Eliminar Arista");

        panelControles.add(btnAnadirNodo);
        panelControles.add(btnAnadirArista);
        panelControles.add(btnEliminarNodo);
        panelControles.add(btnEliminarArista);
        add(panelControles, BorderLayout.NORTH);

        // ---------------------------------------------------------
        // 2. PANEL CENTRAL: EL LIENZO DE DIBUJO
        // ---------------------------------------------------------
        panelLienzo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Paso A: Dibujar la Envolvente Conexa (si existe)
                if (envolventeResaltada.size() >= 3) {
                    int[] xPoints = new int[envolventeResaltada.size()];
                    int[] yPoints = new int[envolventeResaltada.size()];
                    for (int i = 0; i < envolventeResaltada.size(); i++) {
                        xPoints[i] = (int) envolventeResaltada.get(i).x;
                        yPoints[i] = (int) envolventeResaltada.get(i).y;
                    }
                    // Relleno translÃºcido
                    g2d.setColor(new Color(0, 191, 255, 50));
                    g2d.fillPolygon(xPoints, yPoints, envolventeResaltada.size());
                    // Contorno azul
                    g2d.setColor(new Color(0, 100, 255));
                    g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawPolygon(xPoints, yPoints, envolventeResaltada.size());
                }

                // Paso B: Dibujar las LÃ­neas (Aristas) primero
                for (Arista a : miGrafo.obtenerAristas()) {
                    Nodo o = miGrafo.obtenerNodo(a.origen);
                    Nodo d = miGrafo.obtenerNodo(a.destino);
                    if (o != null && d != null) {
                        boolean esResaltada = false;
                        for (Arista r : aristasResaltadas) {
                            if ((r.origen == a.origen && r.destino == a.destino) ||
                                    (r.origen == a.destino && r.destino == a.origen)) {
                                esResaltada = true;
                                break;
                            }
                        }

                        if (esResaltada) {
                            g2d.setColor(new Color(255, 69, 0)); // Naranja-rojo grueso
                            g2d.setStroke(new BasicStroke(4));
                        } else {
                            g2d.setColor(Color.GRAY);
                            g2d.setStroke(new BasicStroke(2));
                        }

                        g2d.drawLine((int) o.x, (int) o.y, (int) d.x, (int) d.y);

                        // Peso o flujo
                        g2d.setColor(Color.RED);
                        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                        String label = mostrarFlujo ? String.format("%.1f/%.1f", a.flujo, a.capacidad)
                                : String.format("%.1f", a.peso);
                        g2d.drawString(label, (int) (o.x + d.x) / 2, (int) (o.y + d.y) / 2 - 5);
                    }
                }

                // Paso C: Dibujar los CÃ­rculos (Nodos)
                int radio = 15;
                for (Nodo n : miGrafo.obtenerNodos()) {
                    if (n != null) {
                        if (n.color != -1) {
                            g2d.setColor(paleta[n.color % paleta.length]);
                        } else {
                            g2d.setColor(new Color(70, 130, 180)); // default azul acero
                        }
                        g2d.fillOval((int) n.x - radio, (int) n.y - radio, radio * 2, radio * 2);

                        // Contorno del nodo
                        g2d.setColor(Color.DARK_GRAY);
                        g2d.setStroke(new BasicStroke(1.5f));
                        g2d.drawOval((int) n.x - radio, (int) n.y - radio, radio * 2, radio * 2);

                        // Dibujar etiqueta (nÃºmero o letra)
                        g2d.setColor(Color.WHITE);
                        g2d.setFont(new Font("Arial", Font.BOLD, 12));
                        String txt = usarLetras ? obtenerLetra(n.id) : String.valueOf(n.id);
                        int labelOffset = txt.length() > 1 ? -8 : -4;
                        g2d.drawString(txt, (int) n.x + labelOffset, (int) n.y + 4);
                    }
                }
            }
        };
        panelLienzo.setBackground(Color.WHITE);
        add(panelLienzo, BorderLayout.CENTER);

        // ---------------------------------------------------------
        // 3. PANEL LATERAL: SELECCIÃ“N DE ALGORITMOS Y ESTRUCTURAS
        // ---------------------------------------------------------
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelLateral.setPreferredSize(new Dimension(340, 600));

        // Bloque 1: ConfiguraciÃ³n de etiquetas
        JPanel panelConfig = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelConfig.setBorder(BorderFactory.createTitledBorder("ConfiguraciÃ³n"));
        JCheckBox chkLetras = new JCheckBox("Usar letras en Nodos (A, B, C...)");
        chkLetras.addActionListener(e -> {
            usarLetras = chkLetras.isSelected();
            panelLienzo.repaint();
        });
        panelConfig.add(chkLetras);
        
        JCheckBox chkDirigido = new JCheckBox("Grafo Dirigido", miGrafo.isDirigido());
        chkDirigido.addActionListener(e -> {
            miGrafo.setDirigido(chkDirigido.isSelected());
            panelLienzo.repaint();
        });
        panelConfig.add(chkDirigido);
        panelLateral.add(panelConfig);

        // Bloque 2: Representaciones de Estructura
        JPanel panelRep = new JPanel(new GridBagLayout());
        panelRep.setBorder(BorderFactory.createTitledBorder("Estructura del Grafo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JComboBox<String> cmbRepresentaciones = new JComboBox<>(new String[] {
                "Lista de Adyacencia", "Matriz de Adyacencia", "Matriz de Incidencia"
        });
        panelRep.add(cmbRepresentaciones, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton btnVerRep = new JButton("Ver Estructura");
        panelRep.add(btnVerRep, gbc);
        panelLateral.add(panelRep);

        // Bloque 3: EjecuciÃ³n de Algoritmos
        JPanel panelAlg = new JPanel(new GridBagLayout());
        panelAlg.setBorder(BorderFactory.createTitledBorder("EjecuciÃ³n de Algoritmos"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelAlg.add(new JLabel("CategorÃ­a:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cmbCategorias = new JComboBox<>(new String[] {
                "Caminos MÃ¡s Cortos", "Ãrbol de Recubrimiento MÃ­nimo", "Flujo MÃ¡ximo", "Envolvente Conexo",
                "Coloreado de Grafos"
        });
        panelAlg.add(cmbCategorias, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelAlg.add(new JLabel("Algoritmo:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cmbAlgoritmos = new JComboBox<>();
        panelAlg.add(cmbAlgoritmos, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelAlg.add(new JLabel("Origen / Fuente:"), gbc);
        gbc.gridx = 1;
        JTextField txtOrigen = new JTextField("0", 5);
        panelAlg.add(txtOrigen, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelAlg.add(new JLabel("Destino / Sumidero:"), gbc);
        gbc.gridx = 1;
        JTextField txtDestino = new JTextField("1", 5);
        panelAlg.add(txtDestino, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton btnEjecutar = new JButton("Ejecutar Algoritmo");
        panelAlg.add(btnEjecutar, gbc);

        gbc.gridy = 5;
        JButton btnLimpiar = new JButton("Limpiar Resultados");
        panelAlg.add(btnLimpiar, gbc);

        panelLateral.add(panelAlg);

        // Bloque 4: Consola de Resultados
        JPanel panelRes = new JPanel(new BorderLayout());
        panelRes.setBorder(BorderFactory.createTitledBorder("Resultados y Matrices"));
        JTextArea txtAreaResultados = new JTextArea(12, 28);
        txtAreaResultados.setEditable(false);
        txtAreaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollRes = new JScrollPane(txtAreaResultados);
        panelRes.add(scrollRes, BorderLayout.CENTER);
        panelLateral.add(panelRes);

        add(panelLateral, BorderLayout.EAST);

        // ---------------------------------------------------------
        // 4. LÃ“GICA DE EVENTOS Y ENLACE DE COMPONENTES
        // ---------------------------------------------------------

        // Evento: Cambio de CategorÃ­a de Algoritmo
        cmbCategorias.addActionListener(e -> {
            cmbAlgoritmos.removeAllItems();
            String cat = (String) cmbCategorias.getSelectedItem();
            if ("Caminos MÃ¡s Cortos".equals(cat)) {
                cmbAlgoritmos.addItem("Dijkstra");
                cmbAlgoritmos.addItem("Bellman-Ford");
                cmbAlgoritmos.addItem("Floyd-Warshall");
            } else if ("Ãrbol de Recubrimiento MÃ­nimo".equals(cat)) {
                cmbAlgoritmos.addItem("Kruskal");
                cmbAlgoritmos.addItem("Prim");
            } else if ("Flujo MÃ¡ximo".equals(cat)) {
                cmbAlgoritmos.addItem("Ford-Fulkerson");
                cmbAlgoritmos.addItem("Edmonds-Karp");
                cmbAlgoritmos.addItem("Dinic");
            } else if ("Envolvente Conexo".equals(cat)) {
                cmbAlgoritmos.addItem("Regalo (Jarvis)");
                cmbAlgoritmos.addItem("Graham Scan");
                cmbAlgoritmos.addItem("QuickHull");
                cmbAlgoritmos.addItem("MonÃ³tona de Andrew");
            } else if ("Coloreado de Grafos".equals(cat)) {
                cmbAlgoritmos.addItem("Voraz");
                cmbAlgoritmos.addItem("DSatur");
            }
        });
        cmbCategorias.setSelectedIndex(0); // Forzar carga inicial

        // Evento: Clic en el lienzo para Anadir nodos
        panelLienzo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                miGrafo.agregarNodo(new Nodo(miGrafo.getCantidadNodos(), e.getX(), e.getY()));
                panelLienzo.repaint();
            }
        });

        // Evento: BotÃ³n Anadir Nodo
        btnAnadirNodo.addActionListener(e -> {
            miGrafo.agregarNodo(
                    new Nodo(miGrafo.getCantidadNodos(), 50 + Math.random() * (panelLienzo.getWidth() - 100),
                            50 + Math.random() * (panelLienzo.getHeight() - 100)));
            panelLienzo.repaint();
        });

        // Evento: BotÃ³n Anadir Arista
        btnAnadirArista.addActionListener(e -> {
            try {
                String origenStr = JOptionPane.showInputDialog(this, "ID del Nodo Origen:");
                String destinoStr = JOptionPane.showInputDialog(this, "ID del Nodo Destino:");
                String pesoStr = JOptionPane.showInputDialog(this, "Peso de la Arista:");
                String capStr = JOptionPane.showInputDialog(this, "Capacidad (para Flujo MÃ¡ximo, Opcional):", "10.0");

                if (origenStr != null && destinoStr != null && pesoStr != null) {
                    int origen = Integer.parseInt(origenStr.trim());
                    int destino = Integer.parseInt(destinoStr.trim());
                    double peso = Double.parseDouble(pesoStr.trim());
                    double cap = 10.0;
                    if (capStr != null && !capStr.trim().isEmpty()) {
                        cap = Double.parseDouble(capStr.trim());
                    }

                    miGrafo.agregarArista(new Arista(origen, destino, peso, cap));
                    panelLienzo.repaint();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados.");
            }
        });

        // Evento: Botón Eliminar Nodo
        btnEliminarNodo.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "ID del Nodo a Eliminar:");
                if (idStr != null && !idStr.trim().isEmpty()) {
                    int id = Integer.parseInt(idStr.trim());
                    miGrafo.eliminarNodo(id);
                    panelLienzo.repaint();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        });

        // Evento: Botón Eliminar Arista
        btnEliminarArista.addActionListener(e -> {
            try {
                String origenStr = JOptionPane.showInputDialog(this, "ID del Nodo Origen:");
                String destinoStr = JOptionPane.showInputDialog(this, "ID del Nodo Destino:");
                if (origenStr != null && destinoStr != null && !origenStr.trim().isEmpty() && !destinoStr.trim().isEmpty()) {
                    int origen = Integer.parseInt(origenStr.trim());
                    int destino = Integer.parseInt(destinoStr.trim());
                    miGrafo.eliminarArista(origen, destino);
                    panelLienzo.repaint();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.");
            }
        });

        // Evento: Mostrar Estructuras de Datos
        btnVerRep.addActionListener(e -> {
            String rep = (String) cmbRepresentaciones.getSelectedItem();
            if ("Lista de Adyacencia".equals(rep)) {
                txtAreaResultados.setText(generarListaAdyacenciaString());
            } else if ("Matriz de Adyacencia".equals(rep)) {
                txtAreaResultados.setText(generarMatrizAdyacenciaString());
            } else if ("Matriz de Incidencia".equals(rep)) {
                txtAreaResultados.setText(generarMatrizIncidenciaString());
            }
        });

        // Evento: EjecuciÃ³n de Algoritmos
        btnEjecutar.addActionListener(e -> {
            try {
                // Reiniciar resultados visuales anteriores
                aristasResaltadas.clear();
                envolventeResaltada.clear();
                mostrarFlujo = false;
                for (Nodo n : miGrafo.obtenerNodos()) {
                    if (n != null)
                        n.color = -1;
                }

                String cat = (String) cmbCategorias.getSelectedItem();
                String alg = (String) cmbAlgoritmos.getSelectedItem();

                if ("Caminos MÃ¡s Cortos".equals(cat)) {
                    int origen = Integer.parseInt(txtOrigen.getText().trim());
                    int destino = Integer.parseInt(txtDestino.getText().trim());

                    if ("Dijkstra".equals(alg)) {
                        List<Arista> res = Dijkstra.ejecutar(miGrafo, origen, destino);
                        if (res.isEmpty()) {
                            txtAreaResultados.setText("No se encontrÃ³ camino entre " + origen + " y " + destino);
                        } else {
                            aristasResaltadas.addAll(res);
                            double costo = 0;
                            for (Arista a : res)
                                costo += a.peso;
                            txtAreaResultados.setText("Camino Dijkstra:\nCosto total: " + costo);
                        }
                    } else if ("Bellman-Ford".equals(alg)) {
                        try {
                            List<Arista> res = BellmanFord.ejecutar(miGrafo, origen, destino);
                            if (res.isEmpty()) {
                                txtAreaResultados.setText("No se encontrÃ³ camino entre " + origen + " y " + destino);
                            } else {
                                aristasResaltadas.addAll(res);
                                double costo = 0;
                                for (Arista a : res)
                                    costo += a.peso;
                                txtAreaResultados.setText("Camino Bellman-Ford:\nCosto total: " + costo);
                            }
                        } catch (Exception ex) {
                            txtAreaResultados.setText("Error:\n" + ex.getMessage());
                        }
                    } else if ("Floyd-Warshall".equals(alg)) {
                        double[][] matriz = FloydWarshall.ejecutar(miGrafo);
                        StringBuilder sb = new StringBuilder("Floyd-Warshall (Distancias):\n");
                        int n = miGrafo.getCantidadNodos();
                        for (int i = 0; i < n; i++) {
                            if (miGrafo.obtenerNodos().get(i) == null)
                                continue;
                            String lblI = usarLetras ? obtenerLetra(i) : String.valueOf(i);
                            sb.append(lblI).append(": ");
                            for (int j = 0; j < n; j++) {
                                if (miGrafo.obtenerNodos().get(j) == null)
                                    continue;
                                if (matriz[i][j] == Double.POSITIVE_INFINITY) {
                                    sb.append(" âˆž ");
                                } else {
                                    sb.append(String.format(" %.1f ", matriz[i][j]));
                                }
                            }
                            sb.append("\n");
                        }
                        txtAreaResultados.setText(sb.toString());
                        // Resaltar camino si origen/destino son vÃ¡lidos
                        List<Arista> res = Dijkstra.ejecutar(miGrafo, origen, destino);
                        aristasResaltadas.addAll(res);
                    }
                } else if ("Ãrbol de Recubrimiento MÃ­nimo".equals(cat)) {
                    if ("Kruskal".equals(alg)) {
                        List<Arista> res = Kruskal.ejecutar(miGrafo);
                        aristasResaltadas.addAll(res);
                        double costo = 0;
                        for (Arista a : res)
                            costo += a.peso;
                        txtAreaResultados.setText("MST Kruskal:\nPeso total: " + costo);
                    } else if ("Prim".equals(alg)) {
                        List<Arista> res = Prim.ejecutar(miGrafo);
                        aristasResaltadas.addAll(res);
                        double costo = 0;
                        for (Arista a : res)
                            costo += a.peso;
                        txtAreaResultados.setText("MST Prim:\nPeso total: " + costo);
                    }
                } else if ("Flujo MÃ¡ximo".equals(cat)) {
                    int fuente = Integer.parseInt(txtOrigen.getText().trim());
                    int sumidero = Integer.parseInt(txtDestino.getText().trim());
                    mostrarFlujo = true;
                    double flow = 0;
                    if ("Ford-Fulkerson".equals(alg)) {
                        flow = FordFulkerson.ejecutar(miGrafo, fuente, sumidero);
                        txtAreaResultados.setText("Ford-Fulkerson Max Flow:\nFlujo total: " + flow);
                    } else if ("Edmonds-Karp".equals(alg)) {
                        flow = EdmondsKarp.ejecutar(miGrafo, fuente, sumidero);
                        txtAreaResultados.setText("Edmonds-Karp Max Flow:\nFlujo total: " + flow);
                    } else if ("Dinic".equals(alg)) {
                        flow = Dinic.ejecutar(miGrafo, fuente, sumidero);
                        txtAreaResultados.setText("Dinic Max Flow:\nFlujo total: " + flow);
                    }
                } else if ("Envolvente Conexo".equals(cat)) {
                    List<Nodo> hull = null;
                    if ("Regalo (Jarvis)".equals(alg)) {
                        hull = JarvisMarch.ejecutar(miGrafo);
                        txtAreaResultados.setText("Envolvente calculada con Jarvis March (Regalo).");
                    } else if ("Graham Scan".equals(alg)) {
                        hull = GrahamScan.ejecutar(miGrafo);
                        txtAreaResultados.setText("Envolvente calculada con Graham Scan.");
                    } else if ("QuickHull".equals(alg)) {
                        hull = QuickHull.ejecutar(miGrafo);
                        txtAreaResultados.setText("Envolvente calculada con QuickHull.");
                    } else if ("MonÃ³tona de Andrew".equals(alg)) {
                        hull = MonotonaAndrew.ejecutar(miGrafo);
                        txtAreaResultados.setText("Envolvente calculada con MonÃ³tona de Andrew.");
                    }
                    if (hull != null && hull.size() >= 3) {
                        envolventeResaltada.addAll(hull);
                    } else {
                        txtAreaResultados.append("\n(Se necesitan al menos 3 nodos no colineales).");
                    }
                } else if ("Coloreado de Grafos".equals(cat)) {
                    if ("Voraz".equals(alg)) {
                        ColoreadoVoraz.ejecutar(miGrafo);
                        txtAreaResultados.setText("Coloreado Voraz aplicado.");
                    } else if ("DSatur".equals(alg)) {
                        ColoreadoExacto.ejecutar(miGrafo);
                        txtAreaResultados.setText("Coloreado DSatur aplicado.");
                    }
                    java.util.Set<Integer> uniqueColors = new java.util.HashSet<>();
                    for (Nodo n : miGrafo.obtenerNodos()) {
                        if (n != null && n.color != -1) {
                            uniqueColors.add(n.color);
                        }
                    }
                    txtAreaResultados.append("\nColores Ãºnicos utilizados: " + uniqueColors.size());
                }

                panelLienzo.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error de ParÃ¡metros: " + ex.getMessage());
            }
        });

        // Evento: Limpiar Resultados
        btnLimpiar.addActionListener(e -> {
            aristasResaltadas.clear();
            envolventeResaltada.clear();
            mostrarFlujo = false;
            for (Nodo n : miGrafo.obtenerNodos()) {
                if (n != null)
                    n.color = -1;
            }
            txtAreaResultados.setText("");
            panelLienzo.repaint();
        });
    }

    // ---------------------------------------------------------
    // 5. MÃ‰TODOS DE FORMATEO DE ESTRUCTURAS
    // ---------------------------------------------------------

    private String generarListaAdyacenciaString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTA DE ADYACENCIA ===\n");
        for (Nodo n : miGrafo.obtenerNodos()) {
            if (n == null)
                continue;
            String label = usarLetras ? obtenerLetra(n.id) : String.valueOf(n.id);
            sb.append(label).append(":");
            for (Arista a : miGrafo.obtenerAristas()) {
                if (a.origen == n.id) {
                    String destLabel = usarLetras ? obtenerLetra(a.destino) : String.valueOf(a.destino);
                    sb.append(" -> ").append(destLabel)
                            .append(" (p: ").append(String.format("%.1f", a.peso))
                            .append(", c: ").append(String.format("%.1f", a.capacidad)).append(")");
                } else if (!miGrafo.isDirigido() && a.destino == n.id) {
                    String destLabel = usarLetras ? obtenerLetra(a.origen) : String.valueOf(a.origen);
                    sb.append(" -> ").append(destLabel)
                            .append(" (p: ").append(String.format("%.1f", a.peso))
                            .append(", c: ").append(String.format("%.1f", a.capacidad)).append(")");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String generarMatrizAdyacenciaString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MATRIZ DE ADYACENCIA ===\n");
        List<Nodo> validNodos = new ArrayList<>();
        for (Nodo n : miGrafo.obtenerNodos()) {
            if (n != null)
                validNodos.add(n);
        }
        int size = validNodos.size();
        if (size == 0)
            return sb.append("(Grafo vacÃ­o)").toString();

        sb.append(String.format("%-6s", ""));
        for (Nodo n : validNodos) {
            String label = usarLetras ? obtenerLetra(n.id) : String.valueOf(n.id);
            sb.append(String.format("%-8s", label));
        }
        sb.append("\n");

        for (Nodo u : validNodos) {
            String labelU = usarLetras ? obtenerLetra(u.id) : String.valueOf(u.id);
            sb.append(String.format("%-6s", labelU));
            for (Nodo v : validNodos) {
                double peso = Double.POSITIVE_INFINITY;
                for (Arista a : miGrafo.obtenerAristas()) {
                    if ((a.origen == u.id && a.destino == v.id) || (!miGrafo.isDirigido() && a.destino == u.id && a.origen == v.id)) {
                        peso = a.peso;
                        break;
                    }
                }
                if (peso == Double.POSITIVE_INFINITY) {
                    sb.append(String.format("%-8s", "âˆž"));
                } else {
                    sb.append(String.format("%-8.1f", peso));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String generarMatrizIncidenciaString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MATRIZ DE INCIDENCIA ===\n");
        List<Nodo> validNodos = new ArrayList<>();
        for (Nodo n : miGrafo.obtenerNodos()) {
            if (n != null)
                validNodos.add(n);
        }
        int numNodos = validNodos.size();
        int numAristas = miGrafo.obtenerAristas().size();
        if (numNodos == 0)
            return sb.append("(Grafo vacÃ­o)").toString();
        if (numAristas == 0)
            return sb.append("(Sin aristas)").toString();

        sb.append(String.format("%-6s", ""));
        for (int j = 0; j < numAristas; j++) {
            sb.append(String.format("%-8s", "e" + j));
        }
        sb.append("\n");

        for (Nodo u : validNodos) {
            String labelU = usarLetras ? obtenerLetra(u.id) : String.valueOf(u.id);
            sb.append(String.format("%-6s", labelU));

            for (int j = 0; j < numAristas; j++) {
                Arista a = miGrafo.obtenerAristas().get(j);
                int val = 0;
                if (a.origen == u.id && a.destino == u.id) {
                    val = 2; // Loop
                } else if (a.origen == u.id) {
                    val = miGrafo.isDirigido() ? -1 : 1; // Outgoing
                } else if (a.destino == u.id) {
                    val = 1; // Incoming
                }
                sb.append(String.format("%-8d", val));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // MÃ©todo auxiliar para obtener la etiqueta de letra segÃºn el ID
    public static String obtenerLetra(int id) {
        StringBuilder sb = new StringBuilder();
        int temp = id;
        do {
            sb.insert(0, (char) ('A' + (temp % 26)));
            temp = (temp / 26) - 1;
        } while (temp >= 0);
        return sb.toString();
    }
}