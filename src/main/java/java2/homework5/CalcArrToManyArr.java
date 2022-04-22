package java2.homework5;

import java.util.Arrays;

public class CalcArrToManyArr implements CheckSpeed{
    private final float [] workArr;
    private final int quantityStream;
    private final float [] [] newArr;

    public CalcArrToManyArr(float[] workArr, int quantityStream) {
        this.workArr = workArr;
        this.quantityStream = quantityStream;
        this.newArr = new float[quantityStream][];
    }

    @Override
    public void run()  {
        Thread [] thread =new Thread[quantityStream];
        int step = workArr.length/quantityStream;
        for (int i = 0; i < quantityStream; i++) {
            int sizeNewArr = (i + 1 == quantityStream ? workArr.length - i* step: step);
            newArr[i] = new float[sizeNewArr];
            System.arraycopy(workArr, i * step, newArr[i], 0, sizeNewArr);
            thread[i] =new Thread(new ThreadCalculationArr(i * step ,newArr[i]));
        }
        for (int i = 0; i < quantityStream; i++) {
            thread[i].start();
        }
        for (int i = 0; i < quantityStream; i++) {

            try {
                thread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.arraycopy(newArr[i],0 ,workArr , i * step, newArr[i].length);

        }

    }

    @Override
    public String toString() {
        return "Расчет массива с разбиение на " + quantityStream + " массивов каждый рассичтвается в своем потоке" ;
    }
}
