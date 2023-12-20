package netpulse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import static javax.print.attribute.standard.MediaSizeName.C;

public class client extends JFrame {
    private JTextField websiteTextField;
    private JTextArea resultTextArea;
    private JButton calculateButton;

    public client() {
        setTitle("NetPulse - Network Analysis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a background panel with an image
        JPanel backgroundPanel;
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image image = new ImageIcon("C:/Users/mithu/Downloads/laptop.jpg").getImage();
                        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        // Create a translucent panel for the content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        // Create a panel for the input components
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.setOpaque(false);

        JLabel websiteLabel = new JLabel("Website URL:");
        websiteLabel.setForeground(Color.WHITE);
        websiteLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inputPanel.add(websiteLabel);

        websiteTextField = new JTextField(40);
        inputPanel.add(websiteTextField);

        contentPanel.add(inputPanel, BorderLayout.NORTH);

        // Create a panel for the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 16));
        calculateButton.setBackground(Color.WHITE);
        calculateButton.addActionListener(new CalculateButtonListener());
        buttonPanel.add(calculateButton);

        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        // Create a panel for the result text area
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setOpaque(false);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setFont(new Font("Monospaced", Font.BOLD, 20));
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setBackground(new Color(0, 0, 0, 0));
        resultTextArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        scrollPane.setOpaque(false);

        resultPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(resultPanel, BorderLayout.SOUTH);
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String website = websiteTextField.getText();
            if (website.isEmpty()) {
                JOptionPane.showMessageDialog(client.this, "Please enter a website URL.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            calculateButton.setEnabled(false);
            resultTextArea.setText("Calculating...");

            new Thread(() -> {
                try {
                    // Connect to the server
                    Socket socket = new Socket("localhost", 12345);
                    System.out.println("Connected to server");

                    // Create a BufferedReader to read input from the server
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    // Create a PrintWriter to send output to the server
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                    // Send the website name to the server
                    writer.println(website);

                    // Receive the result from the server
                    StringBuilder resultBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        resultBuilder.append(line).append("\n");
                        if (line.startsWith("Number of Data Packets:")) {
                            String[] parts = line.split(":");
                            int dataPackets = Integer.parseInt(parts[1].trim());
                            
                        }
                    }
                    String result = resultBuilder.toString();

                    SwingUtilities.invokeLater(() -> {
                        resultTextArea.setText(result);
                        calculateButton.setEnabled(true);
                    });

                    // Close the connections
                    writer.close();
                    reader.close();
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

    private int measureDataPackets(String websiteName, int initialDataPackets) {
        int dataPackets = initialDataPackets;
        try {
            Process process = Runtime.getRuntime().exec("ping -c 1 " + websiteName);
            process.waitFor();
            if (process.exitValue() == 0) {
                dataPackets++; // Increment data packets when ping is successful
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return dataPackets;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            client netPulseUI = new client();
            netPulseUI.setVisible(true);
        });
    }
}