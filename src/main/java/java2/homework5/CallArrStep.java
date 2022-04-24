package java2.homework5;

public class CallArrStep implements CheckSpeed {
    private final float[] workArr;
    private final int quantityStream;

    public CallArrStep(float[] workArr, int quantityStream) {
        this.workArr = workArr;
        this.quantityStream = quantityStream;
    }

    @Override
    public void run() {
        Thread[] thread = new Thread[quantityStream];
        for (int i = 0; i < quantityStream; i++) {
            thread[i] = new Thread(new ThreadCalculationArrStep(i, quantityStream, workArr));
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
        }

    }

    @Override
    public String toString() {
        return "Расчет массива в " + quantityStream + " потоках";
    }
}
