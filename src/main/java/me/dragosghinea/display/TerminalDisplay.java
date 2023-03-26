package me.dragosghinea.display;

import me.dragosghinea.display.events.DisplayInputListener;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class TerminalDisplay implements Display{
    private JTextArea outputArea;
    private JFrame frame;
    private final List<DisplayInputListener> observers = new ArrayList<>();

    private final List<OutputReceived> waitingLine = new LinkedList<>();

    public TerminalDisplay(){
        generateMenu();
    }

    @Override
    public boolean triggerInput(String gotInfo, Properties otherSettings) {
        List.copyOf(observers).forEach(obv -> obv.inputInfo(gotInfo, otherSettings));
        return true;
    }

    @Override
    public boolean subscribeInput(DisplayInputListener inputListener) {
        observers.add(inputListener);
        return true;
    }

    @Override
    public boolean unsubscribeInput(DisplayInputListener inputListener) {
        return observers.remove(inputListener);
    }

    @Override
    public synchronized boolean outputInfo(String gotInfo, Properties otherSettings) {
        if(otherSettings==null || !otherSettings.getProperty("OutputType", "None").equals("Request")){
            outputArea.append(gotInfo);

            return true;
        }

        waitingLine.add(new OutputReceived(gotInfo, otherSettings));
        if(waitingLine.size() == 1){
            outputArea.append(gotInfo);
        }

        return true;
    }

    private void generateMenu(){
        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setMinimumSize(new Dimension(400, 500));
        frame.setTitle("Terminal | AuctionsSystem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));


        JTextField inputField = new JTextField();
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.WHITE);
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 12));


        JScrollPane scrollPane = new JScrollPane(outputArea);


        frame.add(scrollPane);
        frame.add(inputField, "South");

        frame.setVisible(true);


        inputField.requestFocus();


        inputField.addActionListener(e -> {
            String input = inputField.getText();
            inputField.setText("");

            synchronized (this) {
                if (!waitingLine.isEmpty()) {
                    OutputReceived outputReceived = waitingLine.get(0);
                    String passwordRegex = outputReceived.otherSettings.getProperty("Password", "None");
                    if(!passwordRegex.equals("None")){
                        if(!input.matches(passwordRegex)){
                            outputArea.append("*".repeat(input.length())+"\n");
                            outputArea.append(outputReceived.otherSettings.getProperty("PasswordErrorMessage", "Password doesn't have the correct format.\n"));
                            outputArea.append(outputReceived.gotInfo);
                            return;
                        }
                        else {
                            outputArea.append("*".repeat(input.length()) + "\n");
                        }
                        String salt = BCrypt.gensalt(12);
                        input = BCrypt.hashpw(input, salt);
                        triggerInput(input, outputReceived.otherSettings);
                    }
                    else{
                        outputArea.append(input + "\n");
                    }
                    waitingLine.remove(0);
                    if(!waitingLine.isEmpty()){
                        outputArea.append(waitingLine.get(0).gotInfo);
                    }
                    triggerInput(input, outputReceived.otherSettings);
                } else {
                    outputArea.append("> " + input + "\n");
                    triggerInput(input, null);
                }
            }
        });


        outputArea.setBorder(BorderFactory.createLoweredBevelBorder());
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(false);
    }

    @Override
    public void reset() {
        outputArea.setText("");
    }

    @Override
    public void close() {
        frame.dispose();
    }

    private record OutputReceived(String gotInfo, Properties otherSettings){}
}
