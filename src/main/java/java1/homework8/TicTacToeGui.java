package java1.homework8;

import homework4.TicTacToe;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class TicTacToeGui {
    private static final String FRAME_TITLE = "Крестики-Нолики";
    private static final String START_GAME_MSG = "Игра началачь!";
    JFrame frame;
    JLabel gameStateLabel;
    JPanel panel;
    JPanel panelSetting;
    private  JLabel[][] gameField = new JLabel[TicTacToe.getSizeMap()][TicTacToe.getSizeMap()];
    char actionNextSet= TicTacToe.CHAR_X;
    boolean player1Computer= false;
    boolean player2Computer= true;

    public TicTacToeGui() {
        initFrame();
    }

    private void initFrame() {
        frame = new JFrame();
        frame.setTitle(FRAME_TITLE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 400);

        createSettingPanel();

        createGamePanel();
        frame.setVisible(true);

    }

    private void createSettingPanel() {
        panelSetting = new JPanel(new GridBagLayout());
        GridBagConstraints gridConst = new GridBagConstraints();
        DecimalFormat number = new DecimalFormat("#0");

        JLabel sizeMapLabel = new JLabel("Размер поля : ");
        sizeMapLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gridConst.weightx = 0.5;
        gridConst.fill = GridBagConstraints.HORIZONTAL;
        gridConst.gridx = 1;
        gridConst.gridy = 1;
        panelSetting.add(sizeMapLabel,gridConst);

        final JFormattedTextField sizeMap = new JFormattedTextField(
                new NumberFormatter (number) {
                });
        sizeMap.setValue(TicTacToe.getSizeMap());
        gridConst.gridx = 2;
        gridConst.gridy = 1;
        panelSetting.add(sizeMap,gridConst);

        JLabel sizeWinLabel = new JLabel("Размер выигрыша : ");

        sizeWinLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gridConst.gridx = 1;
        gridConst.gridy = 2;
        panelSetting.add(sizeWinLabel,gridConst);


        final JFormattedTextField sizeWin = new JFormattedTextField(
                new NumberFormatter (number) {
                });
        sizeWin.setValue(TicTacToe.quantityWin);
        gridConst.gridx = 2;
        gridConst.gridy = 2;
        panelSetting.add(sizeWin,gridConst);

        JButton restartGameButton = new JButton("Начать заново");
        restartGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Number sizeMapObj = ( Number ) sizeMap.getValue();
                Number sizeWinObj = ( Number ) sizeWin.getValue();
                if ( sizeMapObj!= null && sizeWinObj != null) {
                    startNewGame(sizeMapObj.intValue(),sizeWinObj.intValue());
                }
                else {
                    gameStateLabel.setText("Неверные значения размеров");
                }

            }
        });
        frame.add(restartGameButton, BorderLayout.SOUTH);

        JButton player1Button = new JButton("Игрок 1 :Человек");
        player1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1Computer = !player1Computer;
                player1Button.setText( (player1Computer) ? "Игрок 1: Компьютер" : "Игрок 1: Человек");
                computerTurn();
            }
        });
        gridConst.gridx = 1;
        gridConst.gridy = 4;
        panelSetting.add(player1Button, gridConst);

        JButton player2Button = new JButton("Игрок 2 : Компьютер");
        player2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2Computer = !player2Computer;
                player2Button.setText( (player2Computer) ? "Игрок 2: Компьютер" : "Игрок 2: Человек");
                computerTurn();
            }
        });
        gridConst.fill = GridBagConstraints.HORIZONTAL;
        gridConst.weightx = 0.5;
        gridConst.gridx = 2;
        gridConst.gridy = 4;
        panelSetting.add(player2Button, gridConst);

        gameStateLabel = new JLabel(START_GAME_MSG);
        gameStateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gridConst.weightx = 0.0;
        gridConst.gridx = 1;
        gridConst.gridy = 5;
        gridConst.gridwidth = 2;
        panelSetting.add(gameStateLabel,gridConst);

        frame.add(panelSetting, BorderLayout.NORTH);

    }

    private void createGamePanel() {
        panel = new JPanel(new GridLayout(TicTacToe.getSizeMap() + 1,TicTacToe.getSizeMap() + 1));

        Font font = new Font("Arial", Font.BOLD, 32);
        for (int i = 0; i <= TicTacToe.getSizeMap(); i++) {
            for (int j = 0; j <= TicTacToe.getSizeMap(); j++) {
                JLabel cell = new JLabel(
                        String.valueOf(TicTacToe.CHAR_EMPTY)
                );

                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setFont(font);
                cell.setBorder(BorderFactory.createLineBorder(Color.black));
                if (i==0 && j==0){
                    cell.setText("Y\\X");
                    cell.setEnabled(false);

                }
                else if (i==0){
                    cell.setText(String.valueOf(j));
                    cell.setEnabled(false);
                }
                else if (j==0){
                    cell.setText(String.valueOf(i));
                    cell.setEnabled(false);
                }
                else {

                    final int y = i;
                    final int x = j;
                    cell.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            String player;

                            if( cell.isEnabled()
                                &&(
                                    !player1Computer && actionNextSet == TicTacToe.CHAR_X
                                 || !player2Computer && actionNextSet == TicTacToe.CHAR_O)) {
                                 turnMap(cell);
                                 player = (actionNextSet == TicTacToe.CHAR_O ) ? "Игрок 1": "Игрок 2";
                                printTurn(player,y,x);
                                 TicTacToe.setMap(y, x, actionNextSet, player);
                                 if (checkWin(player)){
                                    return;
                                 }
                            }

                            computerTurn();


                        }

                    });
                    gameField[i-1][j-1]=cell;
                }
                panel.add(cell);

            }


        }
        frame.add(panel,BorderLayout.CENTER);
        frame.repaint();
        frame.revalidate();
    }

    private void computerTurn() {
        String player;
        if ((player2Computer && actionNextSet == TicTacToe.CHAR_O)
           || (player1Computer && actionNextSet == TicTacToe.CHAR_X))
        {
            player = (actionNextSet==TicTacToe.CHAR_O) ? "Компьютер 1" :"Компьютер 2";

            turnMap(player,TicTacToe.computerTurn(getNextChar()));
            checkWin( player);
        }
        if (player1Computer && player2Computer){
            while(true){
                player = (actionNextSet==TicTacToe.CHAR_O) ? "Компьютер 1" :"Компьютер 2";
                turnMap(player,TicTacToe.computerTurn(getNextChar()));

                if(checkWin( player)){
                    return;
                }
            }
        }
    }

    private boolean checkWin(String text) {
        if (TicTacToe.isWinAndPrint(actionNextSet,text)){
            setEnable(false);
            setTextGameStateLabel ( "Выиграл " + text);
            return  true;

        }
        if (TicTacToe.isNotEmpty()){
            setEnable(false);
            setTextGameStateLabel ( "Ничья");
            return true;
        }
        return false;
    }

    private char getNextChar(){
        if (actionNextSet == TicTacToe.CHAR_X){
            return TicTacToe.CHAR_O;
        }
        else {
            return  TicTacToe.CHAR_X;
        }
    }
    private void turnMap(String player,int []cell){
        turnMap(gameField[cell[0]][cell[1]]);
        printTurn( player ,   cell[0]+ 1,  cell[1]+1 );
    }
    private void printTurn (String player,int y, int x){
        gameStateLabel.setText("Предыдущий ход " + player  + " : Y "+ y + " X " + x );
    }
    private void turnMap(JLabel cell) {
        actionNextSet = getNextChar();
        cell.setEnabled(false);
        cell.setText(String.valueOf(actionNextSet));
    }

    public static void main(String[] args) {
        TicTacToeGui ticTacToeGui =new TicTacToeGui();
        ticTacToeGui.startNewGame();


    }
    private void startNewGame (int sizeMap,int sizeWin) {
        if (sizeMap !=TicTacToe.getSizeMap()){
            TicTacToe.setSizeMap (sizeMap);
            frame.remove(panel);
            frame.repaint();
            gameField = new JLabel[TicTacToe.getSizeMap()][TicTacToe.getSizeMap()];
            createGamePanel();
        }
        TicTacToe.quantityWin = sizeWin;
        startNewGame();

    }
        private void startNewGame (){
        actionNextSet= TicTacToe.CHAR_X;
        setEnable(true);
        initMap();
        TicTacToe.startNewGame();
        computerTurn();

    }
    private void initMap(){
        for (int i = 0; i < TicTacToe.getSizeMap(); i++) {
            for (int j = 0; j < TicTacToe.getSizeMap(); j++) {
                gameField[i][j].setText(String.valueOf(TicTacToe.CHAR_EMPTY));
            }
        }
    }

    private void setEnable(boolean flag){
        for (int i = 0; i < TicTacToe.getSizeMap(); i++) {
            for (int j = 0; j < TicTacToe.getSizeMap(); j++) {
                gameField[i][j].setEnabled(flag);
            }
        }
    }

    public void setTextGameStateLabel(String text) {
        this.gameStateLabel.setText(text);
    }
}
