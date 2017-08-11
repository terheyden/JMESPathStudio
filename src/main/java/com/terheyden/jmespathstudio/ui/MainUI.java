package com.terheyden.jmespathstudio.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.Window.Type;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainUI {

    private static final Logger log = LogManager.getLogger();

    // UI pieces:
    private JFrame frmJmespathStudio;
    private JSplitPane splitPane;
    private JScrollPane scrollPaneTop;
    private JScrollPane scrollPaneBot;
    private JTextArea textAreaTop;
    private JTextArea textAreaBot;
    private JTextField textFieldJmesPath;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    // Ask for window decorations provided by the look and feel.
                    JFrame.setDefaultLookAndFeelDecorated(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MainUI window = new MainUI();
                window.frmJmespathStudio.setResizable(true);
                window.frmJmespathStudio.setLocationRelativeTo(null);
                window.frmJmespathStudio.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public MainUI() {
        initialize();        // Init the Swing UI.
        initText();          // Add default example text.
        evaluate();          // Run the example and output result.
        startSwingUpdater(); // Live update the output based on keypresses.
    }

    SwingLiveUpdater live;

    private void startSwingUpdater() {
        live = new SwingLiveUpdater(500L, this::evaluate);
    }

    private void do_textAreaBot_keyTyped(KeyEvent e) {
        live.keyTyped(e);
    }

    private void do_textAreaTop_keyTyped(KeyEvent e) {
        live.keyTyped(e);
    }

    private void do_textFieldJmesPath_keyTyped(KeyEvent e) {
        live.keyTyped(e);
    }

    /**
     * Run JMESPath on the user's input and eval the results into the output pane.
     */
    private void evaluate() {
        try {

            String json = textAreaTop.getText();
            String jmes = textFieldJmesPath.getText();

            String resultJson = JMESUtil.searchS(json, jmes);

            textAreaBot.setText(resultJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initText() {

        textFieldJmesPath.setText("locations[?state == 'WA'].name | sort(@) | {WashingtonCities: join(', ', @)}");

        textAreaTop.setText(
            "{\n" +
                "  \"locations\": [\n" +
                "    {\"name\": \"Seattle\", \"state\": \"WA\"},\n" +
                "    {\"name\": \"New York\", \"state\": \"NY\"},\n" +
                "    {\"name\": \"Bellevue\", \"state\": \"WA\"},\n" +
                "    {\"name\": \"Olympia\", \"state\": \"WA\"}\n" +
                "  ]\n" +
                "}\n"
        );
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmJmespathStudio = new JFrame();
        frmJmespathStudio.setTitle("JMESPath Studio");
        frmJmespathStudio.setType(Type.UTILITY);
        frmJmespathStudio.setBounds(100, 100, 1024, 768);
        frmJmespathStudio.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        {
            splitPane = new JSplitPane();
            splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane.setDividerSize(5);
            splitPane.setResizeWeight(0.5);
            frmJmespathStudio.getContentPane().add(splitPane, BorderLayout.CENTER);
            {
                scrollPaneTop = new JScrollPane();
                splitPane.setLeftComponent(scrollPaneTop);
                {
                    textAreaTop = new JTextArea();
                    textAreaTop.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            do_textAreaTop_keyTyped(e);
                        }
                    });
                    textAreaTop.setFont(new Font("Menlo", Font.PLAIN, 12));
                    scrollPaneTop.setViewportView(textAreaTop);
                }
            }
            {
                scrollPaneBot = new JScrollPane();
                splitPane.setRightComponent(scrollPaneBot);
                {
                    textAreaBot = new JTextArea();
                    textAreaBot.setBackground(new Color(245, 245, 245));
                    textAreaBot.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            do_textAreaBot_keyTyped(e);
                        }
                    });
                    textAreaBot.setFont(new Font("Menlo", Font.PLAIN, 12));
                    scrollPaneBot.setViewportView(textAreaBot);
                }
            }
        }
        {
            textFieldJmesPath = new JTextField();
            frmJmespathStudio.getContentPane().add(textFieldJmesPath, BorderLayout.NORTH);
            textFieldJmesPath.setColumns(10);
            textFieldJmesPath.setFont(new Font("Menlo", Font.PLAIN, 12));
            textFieldJmesPath.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    do_textFieldJmesPath_keyTyped(e);
                }
            });
        }
    }
}
