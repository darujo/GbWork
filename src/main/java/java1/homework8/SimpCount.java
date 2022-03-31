package java1.homework8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpCount extends JFrame {
    private long value;
    private static final int WIDTH_WINDOW =500;
    private static final int HEIGHT_WINDOW =500;
    private static final int HEIGHT_BUTTON = 80;
    private static final int WIDTH_BUTTON = 80;
    private static final int HEIGHT_HEAD = 38;
    private SimpCount(){
        setBounds(500,500,WIDTH_WINDOW ,HEIGHT_WINDOW );
        setTitle("Счетчик");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        Font font = new Font("Arial", Font.BOLD, 32);

        JButton zeroButton = new JButton("Сброс");
        zeroButton.setFont(font);
        zeroButton.setBounds(0, HEIGHT_WINDOW - HEIGHT_BUTTON-HEIGHT_HEAD, WIDTH_WINDOW,HEIGHT_BUTTON);
        add(zeroButton);

        JLabel counterValueView = new JLabel();
        counterValueView.setFont(font);
        counterValueView.setHorizontalAlignment(SwingConstants.CENTER);
        counterValueView.setBounds(WIDTH_BUTTON
                              , (int) ((HEIGHT_WINDOW )/2 -0.75*HEIGHT_BUTTON)

                              , WIDTH_WINDOW - 2* WIDTH_BUTTON
                              ,HEIGHT_BUTTON);
        add(counterValueView);
        counterValueView.setText(String.valueOf(value));

        JButton decrement10Button = new JButton("<<");
        decrement10Button.setFont(font);
        decrement10Button.setBounds(0
                ,0
                ,WIDTH_WINDOW/2
                ,HEIGHT_BUTTON);
        add(decrement10Button);

        JButton decrementButton = new JButton("<");
        decrementButton.setFont(font);
        decrementButton.setBounds(0
                                 ,HEIGHT_BUTTON
                                 , WIDTH_BUTTON
                                 ,HEIGHT_WINDOW-2*HEIGHT_BUTTON-HEIGHT_HEAD
        );
        add(decrementButton);

        JButton increment10Button = new JButton(">>");
        increment10Button.setFont(font);
        increment10Button.setBounds(WIDTH_WINDOW/2
                                    ,0
                                    ,WIDTH_WINDOW/2
                                    ,HEIGHT_BUTTON);
        add(increment10Button);

        JButton incrementButton = new JButton(">");
        incrementButton.setFont(font);
        incrementButton.setBounds(WIDTH_WINDOW-WIDTH_BUTTON -15
                                 , HEIGHT_BUTTON
                                 , WIDTH_BUTTON
                                 ,HEIGHT_WINDOW-2*HEIGHT_BUTTON-HEIGHT_HEAD);
        add(incrementButton,BorderLayout.EAST);
        ActionListener listner = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (e.getSource() == increment10Button){
                    value *=11;
                }
                else if (e.getSource() == incrementButton){
                  value++;
                }
                else if (e.getSource() == decrement10Button){
                    value -=10;
                }
                else if (e.getSource() == decrementButton){
                    value--;
                }
                else
                {
                    value= 0;
                }
                counterValueView.setText(String.valueOf(value));
            }
        };

        decrementButton.addActionListener(listner);
        decrement10Button.addActionListener(listner);
        incrementButton.addActionListener(listner);
        increment10Button.addActionListener(listner);
        zeroButton.addActionListener(listner);

        setVisible(true);

    }
    public static void main(String[] args) {
        new SimpCount();
    }
}
