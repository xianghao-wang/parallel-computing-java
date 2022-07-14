import java.util.Random;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ASum extends RecursiveAction {
    int[] A; //input array
    int LO, HI; // subrange
    int SUM; // return value

    public ASum(int[] A, int LO, int HI) {
        this.A = A;
        this.LO = LO;
        this.HI = HI;
    }

    @Override
    protected void compute() {
        SUM = 0;
        if (LO == HI) SUM = A[LO];
        else if (LO > HI) SUM = 0;
        else {
            int MID = (LO + HI) / 2;
            ASum L = new ASum(A, LO, MID);
            ASum R = new ASum(A, MID + 1, HI);
            L.fork();
            R.fork();
            L.join();
            R.join();

            SUM = add(L.SUM, R.SUM);
        }
    }

    public static int add(int a, int b) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return a + b;
    }

    public static void main(String[] args) {
        // Prepare random data
        Random random = new Random();
        int N = 30;
        int[] data = new int[N];
        for (int i = 0; i < N; i ++) data[i] = random.nextInt(15);

        // Parallel
        System.out.println("Running Parallel...");
        long start = System.currentTimeMillis();
        ASum sumTask = new ASum(data, 0, data.length - 1);
        ForkJoinTask.invokeAll(sumTask);
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("Result: " + sumTask.SUM + ", Time: " + duration + " ms");

        // Sequential
        System.out.println("Running Sequential...");
        start = System.currentTimeMillis();
        int sum = 0;
        for (int i = 0; i < N; i ++) sum = add(sum, data[i]);
        end = System.currentTimeMillis();
        duration = end - start;
        System.out.println("Result: " + sum + ", Time: " + duration + " ms");
    }

}