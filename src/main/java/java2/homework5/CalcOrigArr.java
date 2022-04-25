package java2.homework5;

public class CalcOrigArr implements CheckSpeed {
    private final float[] workArr;

    public CalcOrigArr(float[] workArr) {
        this.workArr = workArr;
    }

    @Override
    public void run() {
        Runnable process = new ThreadCalculationArr(0, workArr);
        process.run();
    }

    @Override
    public String toString() {
        return "Расчет массива в один поток без распаралеливания";
    }
}
