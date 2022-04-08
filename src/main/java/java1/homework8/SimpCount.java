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
//        setLayout(null);

        Font font = new Font("Arial", Font.BOLD, 32);

        JButton zeroButton = new JButton("Сброс");
        zeroButton.setFont(font);
        add(zeroButton,BorderLayout.SOUTH);

        JLabel counterValueView = new JLabel();
        counterValueView.setFont(font);
        counterValueView.setHorizontalAlignment(SwingConstants.CENTER);
        add(counterValueView,BorderLayout.CENTER);
        counterValueView.setText(String.valueOf(value));

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1,2));
        this.add (northPanel,BorderLayout.NORTH);
        JButton decrement10Button = new JButton("<<");
        decrement10Button.setFont(font);
        northPanel.add(decrement10Button,BorderLayout.NORTH);

        JButton decrementButton = new JButton("<");
        decrementButton.setFont(font);
        add(decrementButton,BorderLayout.WEST);

        JButton increment10Button = new JButton(">>");
        increment10Button.setFont(font);
        northPanel.add(increment10Button,BorderLayout.EAST);

        JButton incrementButton = new JButton(">");
        incrementButton.setFont(font);
        add(incrementButton,BorderLayout.EAST);

        ActionListener listner = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (e.getSource() == increment10Button){
                    value +=10;
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
