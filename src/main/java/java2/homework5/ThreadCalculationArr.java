package java2.homework5;

public class ThreadCalculationArr implements Runnable{
    private final int  startElem;
    private final float [] workArr;

    public ThreadCalculationArr(int startElem, float[] workArr) {
        this.startElem = startElem;
        this.workArr = workArr;
    }

    @Override
    public void run() {
        for (int i = 0; i < workArr.length; i++) {
            float elemOrigArr = i +startElem;
            workArr[i] = (float)(workArr[i] * Math.sin(0.2f + elemOrigArr / 5) * Math.cos(0.2f + elemOrigArr / 5) * Math.cos(0.4f + elemOrigArr / 2));
        }

    }
}
