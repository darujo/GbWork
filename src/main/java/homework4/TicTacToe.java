package homework4;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private static final int SIZE = 5;
    private static final int qntyWin = 4;
    private static final char CHAR_EMPTY = '•';
    private static final char CHAR_X     = 'X';
    private static final char CHAR_O     = 'O';
    private static final int COMP_SENSATIV =2;
    public  static final Scanner scan = new Scanner(System.in);
    public  static final Random rand = new Random();

    private  static char [][] map;
    private  static int  [][] mapRes;

    public static void main(String[] args) {
        initMap();
        PrintMap();
        while (true){
            humenTurn(CHAR_O);
            PrintMap();
            if (isWin(CHAR_O)){
                System.out.println("Вы выиграли");
                break;
            }
            if (isNotEmpty()){
                System.out.println("Ничья");
                break;
            }
            computerTurn(CHAR_X);
            PrintMap();
            if (isWin(CHAR_X)){
                System.out.println("Вы выиграли");
                break;
            }
            if (isNotEmpty()){
                System.out.println("Ничья");
                break;
            }
        }


    }

    private static void initMap(){
        mapRes = new int [SIZE][SIZE];
        map = new char [SIZE][SIZE];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = CHAR_EMPTY;
            }
        }
    }

    private static void PrintMap(){
        /*   System.out.print("Y X ");
        for (int j = 0; j < SIZE; j++) {
            System.out.print((j + 1) + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + "   ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(mapRes[i][j] + " ");
            }
            System.out.println();
        }
*/
        System.out.print("Y X ");
        for (int j = 0; j < SIZE; j++) {
            System.out.print((j + 1) + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + "   ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static boolean is_valid (int x, int y){
        return is_valid (x, y,true);
    }

    private static boolean is_valid (int x, int y,boolean putMes){
        if (x > 0 && x <= SIZE && y > 0 && y <= SIZE){
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

    private static void humenTurn (char setChar) {
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

    }

    private static void computerTurn (char setChar) {
        System.out.println("Ход компьютера:");
        int x=0,y=0,maxValue = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (maxValue < mapRes[i][j]){
                    maxValue = mapRes[i][j];
                    x = i;
                    y = j;
                }

            }
        }
        if (maxValue > COMP_SENSATIV){
            map[x][y] = setChar;
        }
        else while (true){
            x =rand.nextInt(5);
            y =rand.nextInt(5);
            if (is_valid(x+ 1,y+1,false)){
                map[x][y] = setChar;
                break;
            }
        }
    }

//    private static boolean isWin (char checkChar) {
//        return isWin(checkChar,0,CHAR_EMPTY);
//    }

    private static int charLine;
    private static int oldI, oldJ;

    private static boolean isWin (char checkChar
//            ,int dotSize,char setChar
    ){
        mapResEmpty();
        setFieldMasRes (-1,-1);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (checkField (i,j,checkChar)){
                    return true;
                }
            }
            setFieldMasRes (-1,-1);
        }
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                if (checkField (i,j,checkChar)){
                    return true;
                }
            }
            setFieldMasRes(-1,-1);
        }
        for (int i = 0; i < SIZE - qntyWin + 1; i++) {
            for (int j = 0; j < SIZE - i; j++) {
                if (checkField (j+ i,j,checkChar)){
                    return true;
                }

            }
            setFieldMasRes(-1,-1);
            if (i != 0){
                for (int j = 0; j < SIZE - i; j++) {
                    if (checkField (j,j+ i,checkChar)){
                        return true;
                    }

                }
                setFieldMasRes(-1,-1);
            }

            for (int j = 0; j < SIZE - i; j++) {
                if (checkField (j + i,SIZE - 1 - j,checkChar)){
                    return true;
                }
            }
            setFieldMasRes(-1,-1);
            if (i != 0){
                for (int j = 0; j < SIZE - i; j++) {
                    if (checkField (j,SIZE - 1 -j - i,checkChar)){
                        return true;
                    }
                }
                setFieldMasRes(-1,-1);
            }
        }
        return false;
    }

    private static boolean isNotEmpty (){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == CHAR_EMPTY){
                    return false;
                }
            }
        }
        return true;
    }
    private static boolean checkField (int i,int j,char checkChar){
        if (oldI == -1 || oldJ == -1){
            oldJ = j;
            oldI = i;
        }

        if (map[i][j]==checkChar){
            if (qntyWin   == ++charLine) {
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
                valWeight = (0+k) * k;
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
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                mapRes[i][j] = 0;
            }

        }
    }
}
