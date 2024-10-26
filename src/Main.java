import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Resaltador de Código PSeInt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.add(scrollPane);

        // Crear barra de menú con la opción de abrir archivo
        JMenuBar menuBar = new JMenuBar();
        JMenu archivoMenu = new JMenu("Archivo");
        JMenuItem abrirItem = new JMenuItem("Abrir Archivo");

        abrirItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para seleccionar y cargar un archivo
                File archivo = seleccionarArchivo();
                if (archivo != null) {
                    cargarArchivo(archivo, textPane);
                }
            }
        });

        archivoMenu.add(abrirItem);
        menuBar.add(archivoMenu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    // Método para mostrar el JFileChooser y seleccionar un archivo
    private static File seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un archivo de código PSeInt");
        int seleccion = fileChooser.showOpenDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    // Método para cargar y procesar el contenido del archivo seleccionado
    private static void cargarArchivo(File archivo, JTextPane textPane) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            PseintLexer lexer = new PseintLexer(reader);
            StyledDocument doc = textPane.getStyledDocument();
            doc.remove(0, doc.getLength());  // Limpia el contenido anterior

            // Estilos para diferentes tipos de tokens
            StyleContext sc = new StyleContext();
            Style palabraReservada = sc.addStyle("PalabraReservada", null);
            StyleConstants.setForeground(palabraReservada, Color.BLUE);

            Style numero = sc.addStyle("Numero", null);
            StyleConstants.setForeground(numero, Color.ORANGE);

            Style comentario = sc.addStyle("Comentario", null);
            StyleConstants.setForeground(comentario, Color.GRAY);
            StyleConstants.setItalic(comentario, true);

            Style simbolo = sc.addStyle("Simbolo", null);
            StyleConstants.setForeground(simbolo, Color.MAGENTA);

            Style textoPorDefecto = sc.addStyle("TextoPorDefecto", null);
            StyleConstants.setForeground(textoPorDefecto, Color.BLACK);

            // Procesar cada token del archivo
            int token;
            while ((token = lexer.yylex()) != -1) {
                String texto = lexer.yytext();
                System.out.println("Token: " + token + ", Texto: '" + texto + "'");  // Depuración

                if (token == PseintLexer.SALTO_LINEA) {
                    doc.insertString(doc.getLength(), "\n", null);
                } else {
                    Style estilo = obtenerEstilo(token, palabraReservada, numero, simbolo, textoPorDefecto);
                    doc.insertString(doc.getLength(), texto + " ", estilo);
                }
            }

            textPane.repaint();  // Refrescar la interfaz gráfica
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para obtener el estilo adecuado según el tipo de token
    private static Style obtenerEstilo(int token, Style palabraReservada, Style numero, Style simbolo, Style textoPorDefecto) {
        switch (token) {
            case PseintLexer.PALABRA_RESERVADA:
                return palabraReservada;
            case PseintLexer.NUMERO:
                return numero;
            case PseintLexer.SIMBOLO:
                System.out.println("Aplicando estilo de símbolo");  // Depuración
                return simbolo;
            default:
                return textoPorDefecto;
        }
    }
}
