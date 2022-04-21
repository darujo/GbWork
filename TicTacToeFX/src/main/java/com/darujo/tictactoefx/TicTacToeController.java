package com.darujo.tictactoefx;

import javafx.beans.binding.NumberExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;


public class TicTacToeController {
    @FXML
    public Button restartGameButton;

    @FXML
    public Button player1Button;

    @FXML
    public TextField sizeMap;

    @FXML
    public TextField sizeWin;

    @FXML
    public Button player2Button;

    @FXML
    public BorderPane gamePanel;

    @FXML
    public Label gameStateLabel;

    private static final String START_GAME_MSG = "Игра началачь!";
    char actionNextSet= TicTacToe.CHAR_X;
    boolean player1Computer= false;
    boolean player2Computer= true;
    private  Button[][] gameField = new Button[TicTacToe.getSizeMap()+1][TicTacToe.getSizeMap()+1];


    public void createGamePanel() {
        GridPane panel = new GridPane () ;
        panel.setMaxSize(0,0);
        panel.setPrefSize(TicTacToe.getSizeMap(),TicTacToe.getSizeMap());
        panel.setMaxSize(1.7976931348623157E308,1.7976931348623157E308);
        for (int i = 0; i <= TicTacToe.getSizeMap(); i++) {
            for (int j = 0; j <= TicTacToe.getSizeMap(); j++) {
                Button cell = new Button(
                        String.valueOf(TicTacToe.CHAR_EMPTY)
                );

                if (i==0 && j==0){
                    cell.setText("Y\\X");
                    cell.setDisable(true);

                }
                else {
                    if (i==0){
                        cell.setText(String.valueOf(j));
                        cell.setDisable(true);
                    }
                    else if (j==0){
                        cell.setText(String.valueOf(i));
                        cell.setDisable(true);
                    }
                    else {

                        final int y = i;
                        final int x = j;
                        cell.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                String player;

                                if( !cell.isDisable()
                                    &&(!player1Computer && actionNextSet == TicTacToe.CHAR_X
                                        || !player2Computer && actionNextSet == TicTacToe.CHAR_O))
                                {
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


                    }

                }
                gameField[i][j]=cell;
                cell.setMinSize(0,0);
                cell.setMaxSize(1000,1000);
                cell.setPrefSize(1000,1000);
                panel.add(cell,i ,j );
            }
        }
        setSizeFontButton();

        gamePanel.setCenter( panel);
    }

    private void setSizeFontButton() {
        double sizeFont = Math.min(gamePanel.getWidth(),gamePanel.getHeight());
        if (sizeFont == 0) sizeFont = 400; // размер поумолчанию
        sizeFont = sizeFont/ (3.5* (TicTacToe.getSizeMap() +1));
        javafx.scene.text.Font font = new javafx.scene.text.Font("Arial", sizeFont);
        for (int i = 0; i <= TicTacToe.getSizeMap(); i++) {
            for (int j = 0; j <= TicTacToe.getSizeMap(); j++) {
                gameField[i][j].setFont(font);

            }
        }
    }

    private void computerTurn() {
        String player;
        if  (TicTacToe.isNotEmpty()){
            return;
        }
        if ((player2Computer && actionNextSet == TicTacToe.CHAR_O)
                || (player1Computer && actionNextSet == TicTacToe.CHAR_X))
        {
            player = (actionNextSet==TicTacToe.CHAR_O) ? "Компьютер 1" :"Компьютер 2";

            turnMap(player,TicTacToe.computerTurn(getNextChar()));
            if(checkWin( player)){
                return;
            }
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
        turnMap(gameField[cell[0]+1][cell[1]+1]);
        printTurn( player ,   cell[0]+ 1,  cell[1]+1 );
    }
    private void printTurn (String player,int y, int x){
        gameStateLabel.setText("Предыдущий ход " + player  + " : Y "+ y + " X " + x );
    }
    private void turnMap(Button cell) {
        actionNextSet = getNextChar();
        cell.setDisable(true);
        cell.setText(String.valueOf(actionNextSet));
    }

//    public static void main(String[] args) {
//        TicTacToeGui ticTacToeGui =new TicTacToeGui();
//        ticTacToeGui.startNewGame();
//
//
//    }
    public void restartGame (){
        try {
            Number sizeMapObj = Integer.valueOf(sizeMap.getText());
            Number sizeWinObj = Integer.valueOf(sizeWin.getText());
            startNewGame(sizeMapObj.intValue(),sizeWinObj.intValue());

        }
        catch (NumberFormatException e) {
            gameStateLabel.setText("Неверные значения размеров");

        }

    }
    private void startNewGame (int sizeMap,int sizeWin) {
        if (sizeMap !=TicTacToe.getSizeMap()){
            TicTacToe.setSizeMap (sizeMap);
//             gamePanel.getChildren().clear();
//            frame.remove(panel);
//            frame.repaint();
            gameField = new Button[TicTacToe.getSizeMap()+1][TicTacToe.getSizeMap()+1];
            createGamePanel();
        }else {
            setSizeFontButton();
        }
        TicTacToe.quantityWin = sizeWin;
        startNewGame();

    }
    public void startNewGame (){
        gameStateLabel.setText(START_GAME_MSG);
        actionNextSet= TicTacToe.CHAR_X;
        setEnable(true);
        initMap();
        TicTacToe.startNewGame();
        computerTurn();
        sizeMap.setText(String.valueOf(TicTacToe.getSizeMap()));
        sizeWin.setText(String.valueOf(TicTacToe.quantityWin));
    }
    private void initMap(){
        for (int i = 0; i < TicTacToe.getSizeMap(); i++) {
            for (int j = 0; j < TicTacToe.getSizeMap(); j++) {
                gameField[i+1][j+1].setText(String.valueOf(TicTacToe.CHAR_EMPTY));
            }
        }
    }

    private void setEnable(boolean flag){
        for (int i = 0; i < TicTacToe.getSizeMap(); i++) {
            for (int j = 0; j < TicTacToe.getSizeMap(); j++) {
                gameField[i+1][j+1].setDisable(!flag);
            }
        }
    }

    public void setTextGameStateLabel(String text) {
        this.gameStateLabel.setText(text);
    }

    public void changePlayer1(ActionEvent actionEvent) {
        player1Computer = !player1Computer;
        player1Button.setText( (player1Computer) ? "Игрок 1: Компьютер" : "Игрок 1: Человек");
        computerTurn();
    }
    public void changePlayer2(ActionEvent actionEvent) {
        player2Computer = !player2Computer;
        player2Button.setText( (player2Computer) ? "Игрок 2: Компьютер" : "Игрок 2: Человек");
        computerTurn();
    }
}