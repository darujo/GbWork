package com.darujo.tictactoefx;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private static       int sizeMap = 5;
    public  static       int  quantityWin = 4;
    public  static final char CHAR_EMPTY = '•';
    public  static final char CHAR_X     = 'X';
    public  static final char CHAR_O     = 'O';
    private static final int  COMP_SENSITIVE =2;
    public  static final Scanner scan = new Scanner(System.in);
    public  static final Random rand = new Random();

    private  static char [][] map;
    private  static int  [][] mapRes;

    public static void main(String[] args) {
        while (true){
            humanTurn(CHAR_O);
            if (isWinAndPrint(CHAR_O,"Вы выиграли")){
                break;
            }
            if (isNotEmpty()){
                break;
            }
            computerTurn(CHAR_X);

            if (isWinAndPrint(CHAR_X,"Компьютер выиграл")){
                break;
            }
            if (isNotEmpty()){
                break;
            }
        }


    }
    public static  void startNewGame(){
        initMap();
        PrintMap();

    }

    public static void setSizeMap(int sizeMap) {
        TicTacToe.sizeMap = sizeMap;
        initMap();
    }

    public static int getSizeMap() {
        return sizeMap;
    }

    private static void initMap(){
        mapRes = new int [sizeMap][sizeMap];
        map = new char [sizeMap][sizeMap];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = CHAR_EMPTY;
            }
        }
    }

    private static void PrintMap(){

        System.out.print("Y X ");
        for (int j = 0; j < sizeMap; j++) {
            System.out.print((j + 1) + " ");
        }
        System.out.println();
        for (int i = 0; i < sizeMap; i++) {
            System.out.print((i + 1) + "   ");
            for (int j = 0; j < sizeMap; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static boolean is_valid (int x, int y){
        return is_valid (x, y,true);
    }

    private static boolean is_valid (int x, int y,boolean putMes){
        if (x > 0 && x <= sizeMap && y > 0 && y <= sizeMap){
            if  (map[x-1][y-1] == CHAR_EMPTY){
                return true;
            }
            else{
                if(putMes){
                    System.out.println("Поле уже занято");
                }

            }
        }
        else{
            System.out.println("Координаты выходят за пределы поля");
        }
        return false ;
    }

    private static void humanTurn(char setChar) {
        int x,y;
        while (true){
            System.out.println("Введите координаты в формате X Y");
            if (scan.hasNextInt()) {
                y = scan.nextInt();
            }
            else {
                System.out.println("Не правильно введено введена координата X");
                scan.nextLine();
                continue;
            }
            if (scan.hasNextInt()) {
                x = scan.nextInt();
            }
            else{
                System.out.println("Не правильно введено введена координата Y");
                scan.nextLine();
                continue;
            }
            scan.nextLine();

            if (is_valid(x,y)){
                map[x-1][y-1] = setChar;
                break;
            }

        }
        System.out.println("Ход игрока:");
        PrintMap();

    }

    public static void setMap(int i,int j ,char  charSet, String player){
        System.out.println("Ход " + player);
        map[i-1][j-1]= charSet;
        PrintMap();

    }

    public static int[] computerTurn (char setChar) {
        int[] cell= new int [2];
        if (setChar == CHAR_O){
            System.out.println("Ход Компьютера 1:");

        }
        else{
            System.out.println("Ход Компьютера 2:");

        }

        int x=0,y=0,maxValue = 0;

        for (int i = 0; i < sizeMap; i++) {
            for (int j = 0; j < sizeMap; j++) {
                if (maxValue < mapRes[i][j]){
                    maxValue = mapRes[i][j];
                    x = i;
                    y = j;
                }

            }
        }
        if (maxValue > COMP_SENSITIVE){
            map[x][y] = setChar;
            cell[0]= x;
            cell[1]= y;
        }
        else while (true){
            x =rand.nextInt(sizeMap);
            y =rand.nextInt(sizeMap);
            if (is_valid(x+ 1,y+1,false)){
                map[x][y] = setChar;
                cell[0]= x;
                cell[1]= y;
                break;
            }
        }
        PrintMap();
        return cell;
    }

    private static int charLine;
    private static int oldI, oldJ;
    public static boolean isWinAndPrint (char checkChar,String text){
        boolean result = isWin (checkChar);
        if (result){
            System.out.println("Выиграл " + text);

        }
        return result;
    }
    private static boolean isWin (char checkChar
//            ,int dotSize,char setChar
    ){
        mapResEmpty();
        setFieldMasRes (-1,-1);
        for (int i = 0; i < sizeMap; i++) {
            for (int j = 0; j < sizeMap; j++) {
                if (checkField (i,j,checkChar)){
                    return true;
                }
            }
            setFieldMasRes (-1,-1);
        }
        for (int j = 0; j < sizeMap; j++) {
            for (int i = 0; i < sizeMap; i++) {
                if (checkField (i,j,checkChar)){
                    return true;
                }
            }
            setFieldMasRes(-1,-1);
        }
        for (int i = 0; i < sizeMap - quantityWin + 1; i++) {
            for (int j = 0; j < sizeMap - i; j++) {
                if (checkField (j+ i,j,checkChar)){
                    return true;
                }

            }
            setFieldMasRes(-1,-1);
            if (i != 0){
                for (int j = 0; j < sizeMap - i; j++) {
                    if (checkField (j,j+ i,checkChar)){
                        return true;
                    }

                }
                setFieldMasRes(-1,-1);
            }

            for (int j = 0; j < sizeMap - i; j++) {
                if (checkField (j + i, sizeMap - 1 - j,checkChar)){
                    return true;
                }
            }
            setFieldMasRes(-1,-1);
            if (i != 0){
                for (int j = 0; j < sizeMap - i; j++) {
                    if (checkField (j, sizeMap - 1 -j - i,checkChar)){
                        return true;
                    }
                }
                setFieldMasRes(-1,-1);
            }
        }
        return false;

    }

    public static boolean isNotEmpty (){
        for (int i = 0; i < sizeMap; i++) {
            for (int j = 0; j < sizeMap; j++) {
                if (map[i][j] == CHAR_EMPTY){
                    return false;
                }
            }
        }
        System.out.println("Ничья");
       return true;
    }
    private static boolean checkField (int i,int j,char checkChar){
        if (oldI == -1 || oldJ == -1){
            oldJ = j;
            oldI = i;
        }

        if (map[i][j]==checkChar){
            if (quantityWin == ++charLine) {
                return true;
            }
        }
        else {
            if (charLine != 0){
                setFieldMasRes(i,j);

            }
            oldJ = j;
            oldI = i;

        }
        return false;
    }

    private static void setFieldMasRes (int i,int j){
//       увеличим вес символов подряд
        int valWeight = 0;
        if (charLine != 0){
            for (int k = 1; k <= charLine; k++) {
                valWeight = (k) * k;
            }
        }

        if (charLine != 0 && oldI >=0 && oldJ >=0){
            if (map[oldI][oldJ] ==CHAR_EMPTY){
                mapRes[oldI][oldJ] = mapRes[oldI][oldJ] + valWeight;
            }
       }


        if (i  >= 0 && j  >= 0 ){
            if (map[i][j] ==CHAR_EMPTY && charLine != 0){
                mapRes[i][j] = mapRes[i][j] + valWeight;
            }
        }
        else{
            oldJ = j;
            oldI = i;
        }
        charLine = 0;


    }
    private static void mapResEmpty(){
        for (int i = 0; i < sizeMap; i++) {
            for (int j = 0; j < sizeMap; j++) {
                mapRes[i][j] = 0;
            }

        }
    }
}
