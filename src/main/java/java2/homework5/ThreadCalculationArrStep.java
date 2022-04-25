package java2.homework5;

public class ThreadCalculationArrStep implements Runnable {
    private final int startElem;
    private final int step;
    private final float[] workArr;

    public ThreadCalculationArrStep(int startElem, int step, float[] workArr) {
        this.startElem = startElem;
        this.step = step;
        this.workArr = workArr;
    }

    @Override
    public void run() {
        for (int i = startElem; i < workArr.length; i = i + step) {
            workArr[i] = (float) (workArr[i] * Math.sin(0.2f + ((float) i / 5)) * Math.cos(0.2f + (float) i / 5) * Math.cos(0.4f + (float) i / 2));
        }

    }
}
