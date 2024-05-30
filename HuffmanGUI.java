import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class HuffmanGUI extends JFrame {
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTextField decodeTextField;
    private JButton encodeButton;
    private JButton decodeButton;
    private JTable frequencyTable;
    private HuffmanTree huffmanTree;
    private HuffmanNode huffmanRoot;
    private Map<Character, String> huffmanCodes;

    public HuffmanGUI() {
        setTitle("Codificación de Huffman");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        addComponents();
    }

    private void initComponents() {
        inputTextArea = new JTextArea();
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);

        decodeTextField = new JTextField(20); // Cuadro de texto para decodificar

        encodeButton = new JButton("Codificar");
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encodeText();
            }
        });

        decodeButton = new JButton("Decodificar");
        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decodeText();
            }
        });

        frequencyTable = new JTable();
        frequencyTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Letra", "Frecuencia", "Costo en Bits"}
        ));
    }

    private void addComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Ingrese un texto:"));
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(encodeButton);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Texto codificado/decodificado:"));
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);

        JPanel decodePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel adicional para decodificar
        decodePanel.setBorder(BorderFactory.createTitledBorder("Decodificar texto:"));
        decodePanel.add(new JLabel("Texto a decodificar:"));
        decodePanel.add(decodeTextField);
        decodePanel.add(decodeButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Tabla de Frecuencias y Costo:"));
        JScrollPane tableScrollPane = new JScrollPane(frequencyTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        mainPanel.add(decodePanel, BorderLayout.SOUTH); // Añadir el panel de decodificación al sur
                mainPanel.add(tablePanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private void encodeText() {
        String inputText = inputTextArea.getText();
        if (!inputText.isEmpty()) {
            huffmanTree = new HuffmanTree();
            Map<Character, Integer> frequencies = calculateFrequencies(inputText);
            huffmanRoot = huffmanTree.buildHuffmanTree(frequencies);
            huffmanCodes = new HashMap<>();
            huffmanTree.generateHuffmanCodes(huffmanRoot, "", huffmanCodes);
            String encodedText = huffmanTree.encode(inputText, huffmanCodes);
            int bitsCost = encodedText.length(); // Longitud total del texto codificado
            updateFrequencyTable(frequencies, bitsCost);
            outputTextArea.setText(encodedText);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un texto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decodeText() {
        String encodedText = decodeTextField.getText(); // Obtener el texto a decodificar del cuadro de texto
        if (!encodedText.isEmpty() && huffmanRoot != null) {
            String decodedText = huffmanTree.decode(encodedText, huffmanRoot);
            outputTextArea.setText(decodedText); // Mostrar el texto decodificado en el área de salida
        } else {
            JOptionPane.showMessageDialog(this, "No hay texto para decodificar o el árbol de Huffman no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Map<Character, Integer> calculateFrequencies(String text) {
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }
        return frequencies;
    }

    private void updateFrequencyTable(Map<Character, Integer> frequencies, int totalBitsCost) {
        DefaultTableModel model = (DefaultTableModel) frequencyTable.getModel();
        model.setRowCount(0); // Limpiar la tabla antes de actualizar
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            char letter = entry.getKey();
            int frequency = entry.getValue();
            model.addRow(new Object[]{letter, frequency, totalBitsCost});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HuffmanGUI().setVisible(true);
            }
        });
    }
}

       

