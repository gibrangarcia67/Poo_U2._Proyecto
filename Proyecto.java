/*

Proyecto de unidad 2: Programación Orientada a Objetos
Integrantes:
Joshua André Alvarado Tovar - 2330242
Luis Manuel Anaya Maldonado - 2330267
Gibran Emmanuel García Cervantes - 2330219

PROYECTO: Sistema gestor de estudiantes y calificaciones de unidades
 */

import javax.swing.*; // Librería de diseño
import javax.swing.border.TitledBorder; // Librería de diseño
import java.awt.*; // Librerías
import java.awt.event.*; // Librerías
import java.util.ArrayList; // Librerías de vectores
import javax.swing.text.AbstractDocument; // Para DocumentFilter
import javax.swing.text.AttributeSet; // Para DocumentFilter
import javax.swing.text.BadLocationException; // Para DocumentFilter
import javax.swing.text.DocumentFilter; // Para DocumentFilter

public class Proyecto {

    // --- Clases de Modelo ---
    static class Unidad {
        String nombre;
        double calificacion;

        Unidad(String nombre, double calificacion) {
            this.nombre = nombre;
            this.calificacion = calificacion;
        }

        @Override
        public String toString() {
            return nombre + " (" + String.format("%.2f", calificacion) + ")";
        }
    }

    static class Materia {
        String nombre;
        ArrayList<Unidad> unidades = new ArrayList<>();

        Materia(String nombre) {
            this.nombre = nombre;
        }

        void agregarUnidad(Unidad unidad) {
            unidades.add(unidad);
        }

        double calcularPromedio() {
            if (unidades.isEmpty()) return 0;
            double suma = 0;
            for (Unidad u : unidades) suma += u.calificacion;
            return suma / unidades.size();
        }
    }

    static class Estudiante {
        String nombre;
        String matricula;
        ArrayList<Materia> materias = new ArrayList<>();

        Estudiante(String nombre, String matricula) {
            this.nombre = nombre;
            this.matricula = matricula;
        }

        void agregarMateria(Materia materia) throws IllegalStateException {
            if (materias.size() >= 5) {
                throw new IllegalStateException("No se pueden agregar más de 5 materias por estudiante");
            }
            materias.add(materia);
        }

        void actualizarCalificacionUnidad(int indiceMateria, int indiceUnidad, double nuevaCalificacion) {
            if (indiceMateria < 0 || indiceMateria >= materias.size()) {
                throw new IllegalArgumentException("Índice de materia inválido");
            }
            if (indiceUnidad < 0 || indiceUnidad >= materias.get(indiceMateria).unidades.size()) {
                throw new IllegalArgumentException("Índice de unidad inválido");
            }
            if (nuevaCalificacion < 0 || nuevaCalificacion > 100) {
                throw new IllegalArgumentException("La calificación debe estar entre 0 y 100");
            }
            materias.get(indiceMateria).unidades.get(indiceUnidad).calificacion = nuevaCalificacion;
        }

        double calcularPromedioGeneral() {
            if (materias.isEmpty()) return 0;
            double sumaPromedios = 0;
            for (Materia m : materias) {
                sumaPromedios += m.calcularPromedio();
            }
            return sumaPromedios / materias.size();
        }

        String obtenerBeca() {
            double promedio = calcularPromedioGeneral();
            if (promedio >= 95) return "Beca Excelencia (100%)";
            else if (promedio >= 90) return "Beca Mérito Académico (50%)";
            else if (promedio >= 85) return "Beca Estímulo (30%)";
            else if (promedio < 70) return "Sin beca (Promedio reprobatorio)";
            else return "Sin beca (No cumple requisitos)";
        }
    }

    public static void main(String[] args) {
        // --- Establecer el Look and Feel "Nimbus" ---
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("Error al establecer el Look and Feel 'Nimbus': " + ex);
        }

        ArrayList<Estudiante> estudiantes = new ArrayList<>();

        JFrame frame = new JFrame("Gestor de estudiantes y promedios"); // Sistema gestor de estudiantes, dónde se utiliza nombre y matrícula
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 800);
        frame.setLocationRelativeTo(null); // Centrar la ventana
        frame.setLayout(new BorderLayout(15, 15));
        frame.getContentPane().setBackground(new Color(240, 240, 240)); // Fondo principal gris muy claro

        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(220, 220, 220)); // Gris claro para las pestañas
        tabbedPane.setForeground(new Color(44, 62, 80)); // Azul oscuro para el texto de las pestañas

        // --- Pestaña de Registro ---
        JPanel panelRegistro = new JPanel(new BorderLayout(10, 10));
        panelRegistro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelRegistro.setBackground(Color.WHITE); // Fondo blanco para el panel de registro

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE); // Fondo blanco
        GridBagConstraints configLayout = new GridBagConstraints();
        configLayout.insets = new Insets(8, 8, 8, 8); // Mayor espacio entre componentes
        configLayout.fill = GridBagConstraints.HORIZONTAL;

        // Título del panel de registro
        JLabel tituloRegistro = new JLabel("Registro de estudiantes", SwingConstants.CENTER);
        tituloRegistro.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloRegistro.setForeground(new Color(44, 62, 80)); // Azul oscuro
        configLayout.gridx = 0;
        configLayout.gridy = 0;
        configLayout.gridwidth = 2;
        panelFormulario.add(tituloRegistro, configLayout);

        // Campos de entrada para nombre y matrícula
        JTextField campoNombre = new JTextField(20);
        JTextField campoMatricula = new JTextField(20);

        // *** APLICAR DOCUMENTFILTERS PARA VALIDACIÓN DE ENTRADA ***
        // DocumentFilter para campoNombre (solo letras y espacios)
        ((AbstractDocument) campoNombre.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                // Permite letras (incluyendo ñ y tildes), y espacios
                if (string.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]+")) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    Toolkit.getDefaultToolkit().beep(); // Sonido de error para entrada no válida
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }
                // Permite letras (incluyendo ñ y tildes), y espacios
                if (text.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]+")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep(); // Sonido de error para entrada no válida
                }
            }
        });

        // DocumentFilter para campoMatricula (solo números)
        ((AbstractDocument) campoMatricula.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                // Solo permite dígitos
                if (string.matches("\\d+")) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    Toolkit.getDefaultToolkit().beep(); // Sonido de error para entrada no válida
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }
                // Solo permite dígitos
                if (text.matches("\\d+")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep(); // Sonido de error para entrada no válida
                }
            }
        });
        // *** FIN DE APLICACIÓN DE DOCUMENTFILTERS ***


        JLabel labelNombre = new JLabel("Nombre del Estudiante:");
        labelNombre.setForeground(new Color(52, 73, 94)); // Gris azulado oscuro
        configLayout.gridx = 0;
        configLayout.gridy = 1;
        configLayout.gridwidth = 1;
        panelFormulario.add(labelNombre, configLayout);
        configLayout.gridx = 1;
        panelFormulario.add(campoNombre, configLayout);

        JLabel labelMatricula = new JLabel("Matrícula:");
        labelMatricula.setForeground(new Color(52, 73, 94));
        configLayout.gridx = 0;
        configLayout.gridy = 2;
        panelFormulario.add(labelMatricula, configLayout);
        configLayout.gridx = 1;
        panelFormulario.add(campoMatricula, configLayout);

        // Panel para materias y unidades (dinámico)
        JPanel panelMaterias = new JPanel();
        panelMaterias.setLayout(new BoxLayout(panelMaterias, BoxLayout.Y_AXIS));
        panelMaterias.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)), // Borde gris claro
                "Materias registradas del estudiante",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(44, 62, 80))); // Título del borde azul oscuro
        panelMaterias.setBackground(new Color(248, 248, 248)); // Fondo gris muy claro

        // Array para almacenar los componentes dinámicos
        ArrayList<JPanel> panelesMaterias = new ArrayList<>();
        ArrayList<JTextField> camposNombreMateria = new ArrayList<>(); // Para el nombre de la materia
        ArrayList<ArrayList<JTextField>> camposUnidades = new ArrayList<>();
        ArrayList<ArrayList<JTextField>> camposCalifUnidades = new ArrayList<>();

        // Botón para agregar materia
        JButton botonAgregarMateria = new JButton("Agregar materia");
        botonAgregarMateria.setBackground(new Color(52, 152, 219)); // Azul
        botonAgregarMateria.setForeground(new Color(44, 62, 80)); // CAMBIO: Texto azul oscuro
        botonAgregarMateria.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botonAgregarMateria.setFocusPainted(false);
        botonAgregarMateria.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        botonAgregarMateria.addActionListener(e -> {
            if (panelesMaterias.size() >= 5) {
                JOptionPane.showMessageDialog(frame, "Máximo 5 materias por estudiante");
                return;
            }

            JPanel panelMateria = new JPanel();
            panelMateria.setLayout(new GridBagLayout());
            panelMateria.setBackground(new Color(250, 250, 250)); // Fondo casi blanco para cada materia
            panelMateria.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)), // Borde más suave
                    "Materia " + (panelesMaterias.size() + 1),
                    TitledBorder.LEFT, TitledBorder.TOP,
                    new Font("Segoe UI", Font.BOLD, 12),
                    new Color(52, 73, 94))); // Título de materia gris azulado

            GridBagConstraints configLayoutM = new GridBagConstraints();
            configLayoutM.insets = new Insets(5, 5, 5, 5);
            configLayoutM.fill = GridBagConstraints.HORIZONTAL;

            // Campo nombre materia
            configLayoutM.gridx = 0;
            configLayoutM.gridy = 0;
            configLayoutM.anchor = GridBagConstraints.WEST;
            JLabel lblNombreMateria = new JLabel("Materia:");
            lblNombreMateria.setForeground(new Color(52, 73, 94));
            panelMateria.add(lblNombreMateria, configLayoutM);
            configLayoutM.gridx = 1;
            JTextField campoMateria = new JTextField(15);
            // DocumentFilter para el nombre de la materia (AHORA PERMITE LETRAS, NÚMEROS Y ESPACIOS)
            ((AbstractDocument) campoMateria.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if (string == null) return;
                    // Permite letras (incluyendo ñ y tildes), números y espacios
                    if (string.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ\\s]+")) {
                        super.insertString(fb, offset, string, attr);
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text == null) {
                        super.replace(fb, offset, length, text, attrs);
                        return;
                    }
                    // Permite letras (incluyendo ñ y tildes), números y espacios
                    if (text.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ\\s]+")) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            });
            camposNombreMateria.add(campoMateria); // Guardar referencia al JTextField de nombre de materia
            panelMateria.add(campoMateria, configLayoutM);

            // Panel para unidades (dentro de cada materia)
            JPanel panelUnidades = new JPanel();
            panelUnidades.setLayout(new BoxLayout(panelUnidades, BoxLayout.Y_AXIS));
            panelUnidades.setBackground(new Color(252, 252, 252)); // Fondo aún más claro para unidades
            panelUnidades.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)), // Borde suave
                    "Unidades",
                    TitledBorder.LEFT, TitledBorder.TOP,
                    new Font("Segoe UI", Font.PLAIN, 12),
                    new Color(100, 100, 100))); // Título de unidades en gris

            ArrayList<JTextField> currentCamposUnidad = new ArrayList<>();
            ArrayList<JTextField> currentCamposCalifUnidad = new ArrayList<>();

            // Botón para agregar unidad
            JButton botonAgregarUnidad = new JButton("Agregar unidad"); // Botón para agregar una unidad, con su respectivo diseño qué después se reciclaría
            botonAgregarUnidad.setBackground(new Color(243, 156, 18)); // Naranja
            botonAgregarUnidad.setForeground(new Color(44, 62, 80)); // CAMBIO: Texto azul oscuro
            botonAgregarUnidad.setFont(new Font("Segoe UI", Font.BOLD, 12));
            botonAgregarUnidad.setFocusPainted(false);
            botonAgregarUnidad.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

            botonAgregarUnidad.addActionListener(ev -> {
                JPanel panelUnidad = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
                panelUnidad.setBackground(new Color(255, 255, 255)); // Fondo blanco para cada unidad

                JLabel lblUnidad = new JLabel("Unidad " + (currentCamposUnidad.size() + 1) + ":");
                lblUnidad.setForeground(new Color(70, 70, 70)); // Texto gris oscuro
                JTextField campoUnidad = new JTextField(10);
                // DocumentFilter para el nombre de la unidad (PERMITE LETRAS, NÚMEROS Y ESPACIOS)
                ((AbstractDocument) campoUnidad.getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                        if (string == null) return;
                        // Permite letras (incluyendo ñ y tildes), números y espacios
                        if (string.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ\\s]+")) {
                            super.insertString(fb, offset, string, attr);
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                        if (text == null) {
                            super.replace(fb, offset, length, text, attrs);
                            return;
                        }
                        // Permite letras (incluyendo ñ y tildes), números y espacios
                        if (text.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ\\s]+")) {
                            super.replace(fb, offset, length, text, attrs);
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                });

                JLabel lblCalif = new JLabel("Calificación:");
                lblCalif.setForeground(new Color(70, 70, 70));
                JTextField campoCalif = new JTextField(5);
                // DocumentFilter para la calificación (solo números)
                ((AbstractDocument) campoCalif.getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                        if (string == null) return;
                        if (string.matches("\\d*")) { // Permite números vacíos al inicio, luego solo dígitos
                            super.insertString(fb, offset, string, attr);
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                        if (text == null) {
                            super.replace(fb, offset, length, text, attrs);
                            return;
                        }
                        if (text.matches("\\d*")) {
                            super.replace(fb, offset, length, text, attrs);
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                });


                panelUnidad.add(lblUnidad);
                panelUnidad.add(campoUnidad);
                panelUnidad.add(lblCalif);
                panelUnidad.add(campoCalif);

                panelUnidades.add(panelUnidad);
                currentCamposUnidad.add(campoUnidad);
                currentCamposCalifUnidad.add(campoCalif);

                panelMateria.revalidate();
                panelMateria.repaint();
            });

            configLayoutM.gridx = 0;
            configLayoutM.gridy = 1;
            configLayoutM.gridwidth = 2;
            panelMateria.add(panelUnidades, configLayoutM);

            configLayoutM.gridx = 0;
            configLayoutM.gridy = 2;
            configLayoutM.gridwidth = 2;
            configLayoutM.anchor = GridBagConstraints.CENTER;
            panelMateria.add(botonAgregarUnidad, configLayoutM);

            panelesMaterias.add(panelMateria);
            camposNombreMateria.add(campoMateria); // Asegúrate de que el campo de materia se agrega aquí
            camposUnidades.add(currentCamposUnidad); // Agrega la lista de campos de nombres de unidad
            camposCalifUnidades.add(currentCamposCalifUnidad); // Agrega la lista de campos de calificaciones de unidad

            panelMaterias.add(panelMateria);
            panelMaterias.revalidate();
            panelMaterias.repaint();
        });

        // Configuración de componentes principales en panelRegistro
        configLayout.gridx = 0;
        configLayout.gridy = 3;
        configLayout.gridwidth = 2;
        configLayout.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(botonAgregarMateria, configLayout);

        panelRegistro.add(panelFormulario, BorderLayout.NORTH);
        panelRegistro.add(new JScrollPane(panelMaterias), BorderLayout.CENTER);

        // Botón de registro
        JButton botonRegistrar = new JButton("Registrar Estudiante");
        botonRegistrar.setBackground(new Color(46, 204, 113)); // Verde para acción positiva
        botonRegistrar.setForeground(new Color(44, 62, 80)); // CAMBIO: Texto azul oscuro
        botonRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

        JPanel panelBotonRegistro = new JPanel();
        panelBotonRegistro.setBackground(Color.WHITE);
        panelBotonRegistro.add(botonRegistrar);
        panelRegistro.add(panelBotonRegistro, BorderLayout.SOUTH);

        // --- Pestaña de Consulta/Actualización ---
        JPanel panelConsulta = new JPanel(new BorderLayout(10, 10));
        panelConsulta.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelConsulta.setBackground(Color.WHITE); // Fondo blanco

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                "Busqueda de estudiantes", // Se puede buscar un estudiante por medio de su matrícula
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(44, 62, 80)));
        panelBusqueda.setBackground(new Color(248, 248, 248)); // Gris muy claro

        JTextField campoBusqueda = new JTextField(20);
        // DocumentFilter para campoBusqueda (solo números)
        ((AbstractDocument) campoBusqueda.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d+")) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }
                if (text.matches("\\d+")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        campoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JButton botonBuscar = new JButton("Buscar por Matrícula");
        botonBuscar.setBackground(new Color(52, 152, 219)); // Azul
        botonBuscar.setForeground(new Color(44, 62, 80)); // CAMBIO: Texto azul oscuro
        botonBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonBuscar.setFocusPainted(false);
        botonBuscar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel labelMatriculaBusqueda = new JLabel("Matrícula:");
        labelMatriculaBusqueda.setForeground(new Color(52, 73, 94));
        panelBusqueda.add(labelMatriculaBusqueda);
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(botonBuscar);

        // Panel de resultados
        JTextArea salida = new JTextArea();
        salida.setEditable(false);
        salida.setFont(new Font("Monospaced", Font.PLAIN, 13));
        salida.setBackground(new Color(250, 250, 250)); // Fondo ligeramente diferente para el área de texto
        salida.setForeground(new Color(52, 73, 94)); // Texto gris azulado oscuro
        salida.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220))); // Borde sutil
        JScrollPane scroll = new JScrollPane(salida);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Siempre mostrar scroll

        // Panel de actualización
        JPanel panelActualizacion = new JPanel(new GridBagLayout());
        panelActualizacion.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                "Actualizaciones de calificaciones",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(44, 62, 80)));
        panelActualizacion.setBackground(new Color(248, 248, 248)); // Gris muy claro

        JComboBox<String> comboMaterias = new JComboBox<>();
        JComboBox<String> comboUnidades = new JComboBox<>();
        JTextField campoNuevaCalificacion = new JTextField(5);
        // DocumentFilter para campoNuevaCalificacion (solo números)
        ((AbstractDocument) campoNuevaCalificacion.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d*")) { // Permite números (y puede estar vacío)
                    super.insertString(fb, offset, string, attr);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }
                if (text.matches("\\d*")) { // Permite números (y puede estar vacío)
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        JButton botonActualizar = new JButton("Actualizar Calificación");
        botonActualizar.setBackground(new Color(243, 156, 18)); // Naranja
        botonActualizar.setForeground(new Color(44, 62, 80)); // CAMBIO: Texto azul oscuro
        botonActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonActualizar.setFocusPainted(false);
        botonActualizar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints configLayoutA = new GridBagConstraints();
        configLayoutA.insets = new Insets(8, 8, 8, 8);
        configLayoutA.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelMateriaActualizar = new JLabel("Materia:");
        labelMateriaActualizar.setForeground(new Color(52, 73, 94));
        configLayoutA.gridx = 0;
        configLayoutA.gridy = 0;
        panelActualizacion.add(labelMateriaActualizar, configLayoutA);
        configLayoutA.gridx = 1;
        panelActualizacion.add(comboMaterias, configLayoutA);

        JLabel labelUnidadActualizar = new JLabel("Unidad:");
        labelUnidadActualizar.setForeground(new Color(52, 73, 94));
        configLayoutA.gridx = 0;
        configLayoutA.gridy = 1;
        panelActualizacion.add(labelUnidadActualizar, configLayoutA);
        configLayoutA.gridx = 1;
        panelActualizacion.add(comboUnidades, configLayoutA);

        JLabel labelNuevaCalif = new JLabel("Nueva calificación (0-100):");
        labelNuevaCalif.setForeground(new Color(52, 73, 94));
        configLayoutA.gridx = 0;
        configLayoutA.gridy = 2;
        panelActualizacion.add(labelNuevaCalif, configLayoutA);
        configLayoutA.gridx = 1;
        panelActualizacion.add(campoNuevaCalificacion, configLayoutA);

        configLayoutA.gridx = 0;
        configLayoutA.gridy = 3;
        configLayoutA.gridwidth = 2;
        configLayoutA.anchor = GridBagConstraints.CENTER;
        panelActualizacion.add(botonActualizar, configLayoutA);

        // Panel de información del estudiante
        JPanel panelInfoEstudiante = new JPanel(new BorderLayout(10, 10));
        panelInfoEstudiante.add(scroll, BorderLayout.CENTER);
        panelInfoEstudiante.add(panelActualizacion, BorderLayout.SOUTH);

        // Ensamblar panel de consulta
        panelConsulta.add(panelBusqueda, BorderLayout.NORTH);
        panelConsulta.add(panelInfoEstudiante, BorderLayout.CENTER);

        // --- Pestaña de Listado General ---
        JPanel panelListado = new JPanel(new BorderLayout(10, 10));
        panelListado.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelListado.setBackground(Color.WHITE); // Fondo blanco, estándar para cualquier pestaña

        JTextArea listadoEstudiantes = new JTextArea();
        listadoEstudiantes.setEditable(false);
        listadoEstudiantes.setFont(new Font("Monospaced", Font.PLAIN, 13));
        listadoEstudiantes.setBackground(new Color(250, 250, 250));
        listadoEstudiantes.setForeground(new Color(52, 73, 94));
        listadoEstudiantes.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        JScrollPane scrollListado = new JScrollPane(listadoEstudiantes);
        scrollListado.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton botonListar = new JButton("Actualizar listado de estudiantes");
        botonListar.setBackground(new Color(243, 156, 18)); // Naranja (anteriormente Morado)
        botonListar.setForeground(new Color(44, 62, 80));
        botonListar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonListar.setFocusPainted(false);
        botonListar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel panelBotonListar = new JPanel();
        panelBotonListar.setBackground(Color.WHITE);
        panelBotonListar.add(botonListar);

        // *** CORRECCIÓN PARA EL ERROR EN panelListado.add(panelBotonListar, BorderLayout.SOUTH); ***
        JPanel contenidoListado = new JPanel(new BorderLayout()); // Nuevo panel para contener el scroll y el botón
        contenidoListado.setBackground(Color.WHITE); // Fondo blanco para este nuevo panel

        contenidoListado.add(scrollListado, BorderLayout.CENTER); // El JScrollPane va al centro
        contenidoListado.add(panelBotonListar, BorderLayout.SOUTH); // El panel del botón va al sur

        panelListado.add(contenidoListado, BorderLayout.CENTER); // Agregamos este nuevo panel contenedor al panelListado principal
        // *** FIN DE CORRECCIÓN ***


        // Agregar pestañas
        tabbedPane.addTab("Registro", panelRegistro);
        tabbedPane.addTab("Consultas y actualizaciones", panelConsulta);
        tabbedPane.addTab("Listado general", panelListado);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // --- Acciones de los botones ---

        // Acción para registrar estudiante
        botonRegistrar.addActionListener(e -> {
            try {
                String nombre = campoNombre.getText().trim();
                String matricula = campoMatricula.getText().trim();

                if (nombre.isEmpty() || matricula.isEmpty()) {
                    throw new IllegalArgumentException("Nombre y matrícula son obligatorios");
                }

                // Verificar si la matrícula ya existe
                for (Estudiante est : estudiantes) {
                    if (est.matricula.equals(matricula)) {
                        throw new IllegalArgumentException("La matrícula ya ha sido registrada");
                    }
                }

                Estudiante estudiante = new Estudiante(nombre, matricula);

                // Procesar materias y unidades
                for (int i = 0; i < panelesMaterias.size(); i++) {
                    JTextField campoMateria = camposNombreMateria.get(i);
                    String nombreMateria = campoMateria.getText().trim();

                    if (nombreMateria.isEmpty()) {
                        continue;
                    }

                    Materia materia = new Materia(nombreMateria);

                    // Obtener las listas de campos de unidad y calificación para la materia actual
                    ArrayList<JTextField> camposUnidadNombres = camposUnidades.get(i);
                    ArrayList<JTextField> camposUnidadCalificaciones = camposCalifUnidades.get(i);

                    boolean algunaUnidadAgregada = false;
                    for (int j = 0; j < camposUnidadNombres.size(); j++) {
                        String nombreUnidad = camposUnidadNombres.get(j).getText().trim();
                        String califText = camposUnidadCalificaciones.get(j).getText().trim();

                        if (!nombreUnidad.isEmpty() && !califText.isEmpty()) {
                            try {
                                double calif = Double.parseDouble(califText);
                                if (calif < 0 || calif > 100) {
                                    throw new IllegalArgumentException("Calificación para '" + nombreUnidad + "' debe estar entre 0 y 100.");
                                }
                                materia.agregarUnidad(new Unidad(nombreUnidad, calif));
                                algunaUnidadAgregada = true;
                            } catch (NumberFormatException ex) {
                                throw new IllegalArgumentException("Formato de calificación inválido en unidad '" + nombreUnidad + "' de " + nombreMateria);
                            }
                        } else if (!nombreUnidad.isEmpty() || !califText.isEmpty()) {
                            throw new IllegalArgumentException("Debe completar tanto el nombre como la calificación para cada unidad correspondiente.");
                        }
                    }

                    if (!algunaUnidadAgregada && !nombreMateria.isEmpty()) {
                        throw new IllegalArgumentException("La materia '" + nombreMateria + "' debe tener al menos una unidad con nombre y calificación.");
                    } else if (algunaUnidadAgregada) {
                        estudiante.agregarMateria(materia);
                    }
                }

                if (estudiante.materias.isEmpty()) {
                    throw new IllegalArgumentException("Debe registrar al menos una materia con sus unidades.");
                }

                estudiantes.add(estudiante);

                // Mostrar resultados en el área de salida
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("\n--- REGISTRO EXITOSO ---\n");
                mensaje.append("Estudiante: ").append(estudiante.nombre).append("\n");
                mensaje.append("Matrícula: ").append(estudiante.matricula).append("\n\n");

                for (Materia m : estudiante.materias) {
                    mensaje.append("Materia: ").append(m.nombre).append("\n");
                    for (Unidad u : m.unidades) {
                        mensaje.append("- ").append(u.nombre).append(": ").append(String.format("%.2f", u.calificacion)).append("\n");
                    }
                    mensaje.append("Promedio materia: ").append(String.format("%.2f", m.calcularPromedio())).append("\n\n");
                }

                mensaje.append("Promedio general: ").append(String.format("%.2f", estudiante.calcularPromedioGeneral())).append("\n");
                if (estudiante.calcularPromedioGeneral() < 70) {
                    mensaje.append("⚠ ALERTA: Promedio reprobatorio ⚠\n");
                }
                mensaje.append("Beca asignada: ").append(estudiante.obtenerBeca()).append("\n");
                mensaje.append("------------------------\n");

                salida.append(mensaje.toString());

                // Limpiar campos de registro
                campoNombre.setText("");
                campoMatricula.setText("");
                panelesMaterias.clear();
                camposNombreMateria.clear();
                camposUnidades.clear();
                camposCalifUnidades.clear();
                panelMaterias.removeAll();
                panelMaterias.revalidate();
                panelMaterias.repaint();

                JOptionPane.showMessageDialog(frame, "Estudiante registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción para buscar estudiante
        botonBuscar.addActionListener(e -> {
            try {
                String matricula = campoBusqueda.getText().trim();
                if (matricula.isEmpty()) {
                    throw new IllegalArgumentException("por favor, busque un estudiante.");
                }

                Estudiante encontrado = null;
                for (Estudiante est : estudiantes) {
                    if (est.matricula.equals(matricula)) {
                        encontrado = est;
                        break;
                    }
                }

                if (encontrado == null) {
                    salida.setText("\nNo se encontró ningún estudiante con la matrícula: " + matricula + "\n\n");
                    comboMaterias.removeAllItems();
                    comboUnidades.removeAllItems();
                    campoNuevaCalificacion.setText("");
                    return;
                }

                // Actualizar combos de materias
                comboMaterias.removeAllItems();
                // Si el estudiante tiene materias, agregarlas al comboMaterias
                if (!encontrado.materias.isEmpty()) {
                    for (Materia m : encontrado.materias) {
                        comboMaterias.addItem(m.nombre);
                    }
                    // Seleccionar la primera materia para que se dispare el listener y se carguen las unidades
                    comboMaterias.setSelectedIndex(0);
                } else {
                    // Si no hay materias, asegúrate de que ambos combos estén vacíos
                    comboMaterias.removeAllItems();
                    comboUnidades.removeAllItems();
                }

                // Limpiar campo de calificación
                campoNuevaCalificacion.setText("");

                // Mostrar información del estudiante en el área de salida
                StringBuilder info = new StringBuilder();
                info.append("\n--- INFORMACIÓN DEL ESTUDIANTE ---\n");
                info.append("Nombre: ").append(encontrado.nombre).append("\n");
                info.append("Matrícula: ").append(encontrado.matricula).append("\n\n");

                for (Materia m : encontrado.materias) {
                    info.append("Materia: ").append(m.nombre).append("\n");
                    for (Unidad u : m.unidades) {
                        info.append("- ").append(u.nombre).append(": ").append(String.format("%.2f", u.calificacion)).append("\n");
                    }
                    info.append("Promedio de materia: ").append(String.format("%.2f", m.calcularPromedio())).append("\n\n");
                }

                info.append("Promedio general: ").append(String.format("%.2f", encontrado.calcularPromedioGeneral())).append("\n");
                if (encontrado.calcularPromedioGeneral() < 70) {
                    info.append("⚠ ALERTA: Promedio reprobatorio ⚠\n");
                }
                info.append("Beca asignada: ").append(encontrado.obtenerBeca()).append("\n");
                info.append("--------------------------------\n");

                salida.setText(info.toString());
                salida.setCaretPosition(0); // Volver al inicio del texto

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error de búsqueda", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Listener para actualizar combo de unidades cuando cambia la materia seleccionada
        comboMaterias.addActionListener(e -> {
            // Se debe obtener el estudiante nuevamente ya que este listener puede dispararse en cualquier momento
            String matricula = campoBusqueda.getText().trim();
            if (matricula.isEmpty()) { // Si no hay matrícula en el campo de búsqueda, no hay estudiante para consultar
                comboUnidades.removeAllItems();
                return;
            }

            Estudiante encontrado = null;
            for (Estudiante est : estudiantes) {
                if (est.matricula.equals(matricula)) {
                    encontrado = est;
                    break;
                }
            }

            if (encontrado == null) { // Si no se encuentra el estudiante (ej. borrado o error)
                comboUnidades.removeAllItems();
                return;
            }

            int materiaIndex = comboMaterias.getSelectedIndex();
            // Asegurarse de que hay una materia seleccionada y que el índice es válido
            if (materiaIndex >= 0 && materiaIndex < encontrado.materias.size()) {
                comboUnidades.removeAllItems(); // Limpiar unidades anteriores
                Materia seleccionada = encontrado.materias.get(materiaIndex);
                if (seleccionada != null) { // doble chequeo, aunque el índice ya valida
                    for (Unidad u : seleccionada.unidades) {
                        comboUnidades.addItem(u.nombre);
                    }
                }
            } else { // Si no hay materia seleccionada (ej. después de un removeAllItems en comboMaterias)
                comboUnidades.removeAllItems();
            }

            // Seleccionar el primer elemento si existe para que sea visible
            if (comboUnidades.getItemCount() > 0) {
                comboUnidades.setSelectedIndex(0);
            } else {
                comboUnidades.setSelectedIndex(-1); // Si no hay unidades, asegúrate de que no haya nada seleccionado
            }

            campoNuevaCalificacion.setText(""); // Limpiar la calificación al cambiar de materia
        });

        // Acción para actualizar calificación
        botonActualizar.addActionListener(e -> {
            try {
                String matricula = campoBusqueda.getText().trim();
                if (matricula.isEmpty()) {
                    throw new IllegalArgumentException("Por favor, busque un estudiante.");
                }

                Estudiante encontrado = null;
                for (Estudiante est : estudiantes) {
                    if (est.matricula.equals(matricula)) {
                        encontrado = est;
                        break;
                    }
                }

                if (encontrado == null) {
                    throw new IllegalArgumentException("No hay estudiante seleccionado o la matrícula es incorrecta.");
                }

                int materiaIndex = comboMaterias.getSelectedIndex();
                if (materiaIndex == -1) {
                    throw new IllegalArgumentException("Seleccione una materia para actualizar.");
                }

                int unidadIndex = comboUnidades.getSelectedIndex();
                if (unidadIndex == -1) {
                    // Este es el error que estás viendo en la imagen.
                    // Si llegamos aquí, significa que comboUnidades no tiene elementos seleccionados.
                    throw new IllegalArgumentException("Seleccione una unidad para actualizar.");
                }

                String nuevaCalifText = campoNuevaCalificacion.getText().trim();
                if (nuevaCalifText.isEmpty()) {
                    throw new IllegalArgumentException("Ingrese una calificación para la unidad.");
                }

                double nuevaCalif = Double.parseDouble(nuevaCalifText);
                if (nuevaCalif < 0 || nuevaCalif > 100) {
                    throw new IllegalArgumentException("La calificación debe estar entre 0 y 100.");
                }

                encontrado.actualizarCalificacionUnidad(materiaIndex, unidadIndex, nuevaCalif);

                // Volver a mostrar la información completa del estudiante con la actualización
                StringBuilder mensajeActualizado = new StringBuilder();
                mensajeActualizado.append("\n--- CALIFICACIÓN ACTUALIZADA ---\n");
                mensajeActualizado.append("Estudiante: ").append(encontrado.nombre).append("\n");
                mensajeActualizado.append("Matrícula: ").append(encontrado.matricula).append("\n");
                mensajeActualizado.append("Materia: ").append(encontrado.materias.get(materiaIndex).nombre).append("\n");
                mensajeActualizado.append("Unidad: ").append(encontrado.materias.get(materiaIndex).unidades.get(unidadIndex).nombre).append("\n");
                mensajeActualizado.append("Nueva calificación: ").append(String.format("%.2f", nuevaCalif)).append("\n");
                mensajeActualizado.append("Promedio materia: ").append(String.format("%.2f", encontrado.materias.get(materiaIndex).calcularPromedio())).append("\n");
                mensajeActualizado.append("Promedio general: ").append(String.format("%.2f", encontrado.calcularPromedioGeneral())).append("\n");
                if (encontrado.calcularPromedioGeneral() < 70) {
                    mensajeActualizado.append("⚠ ALERTA: Promedio reprobatorio ⚠\n");
                }
                mensajeActualizado.append("Beca asignada: ").append(encontrado.obtenerBeca()).append("\n");
                mensajeActualizado.append("------------------------------\n");

                salida.append(mensajeActualizado.toString());
                salida.setCaretPosition(salida.getDocument().getLength());

                campoNuevaCalificacion.setText("");
                JOptionPane.showMessageDialog(frame, "Calificación actualizada exitosamente.", "Actualización Completada", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ingrese una calificación numérica válida.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error de Actualización", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción para listar todos los estudiantes
        botonListar.addActionListener(e -> {
            if (estudiantes.isEmpty()) {
                listadoEstudiantes.setText("No hay estudiantes registrados para mostrar.");
                return;
            }

            StringBuilder listado = new StringBuilder();
            listado.append("=== LISTADO GENERAL DE ESTUDIANTES ===\n\n");

            for (Estudiante est : estudiantes) {
                double promedio = est.calcularPromedioGeneral();
                String estado = promedio >= 70 ? "Aprobado" : "Reprobado";

                listado.append("Nombre: ").append(est.nombre).append("\n");
                listado.append("Matrícula: ").append(est.matricula).append("\n");
                listado.append("Materias registradas: ").append(est.materias.size()).append("\n");
                for (Materia m : est.materias) {
                    listado.append("   - Materia: ").append(m.nombre).append(" (Promedio: ").append(String.format("%.2f", m.calcularPromedio())).append(")\n");
                    for (Unidad u : m.unidades) {
                        listado.append("     > ").append(u.nombre).append(": ").append(String.format("%.2f", u.calificacion)).append("\n");
                    }
                }
                listado.append("Promedio general: ").append(String.format("%.2f", promedio)).append(" (").append(estado).append(")\n");
                listado.append("Beca: ").append(est.obtenerBeca()).append("\n");
                listado.append("------------------------------------------\n\n");
            }

            listado.append("Total de estudiantes registrados: ").append(estudiantes.size()).append("\n");
            listadoEstudiantes.setText(listado.toString());
            listadoEstudiantes.setCaretPosition(0); // Volver al inicio del texto
        });

        frame.setVisible(true);
    }
}
