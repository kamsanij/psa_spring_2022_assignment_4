package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);
        Random random = new Random();
        int threads = 2;
        while (threads <= 32) {
            int arrlength = 1000000;
            ParSort.grp = new ForkJoinPool(threads);
            System.out.println("Degree of parallelism: " + ParSort.grp.getParallelism());
            while (arrlength <= 8000000) {
                int[] array = new int[arrlength];
                System.out.println("Length of the Array: " + arrlength);
                ArrayList<Long> timeList = new ArrayList<>();
                for (int j = 5; j <= 200; j += 5) {
                    ParSort.cutoff = (arrlength/1000) * (j);
                    long time;
                    long startTime = System.currentTimeMillis();
                    for (int t = 0; t < 20; t++) {
                        for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                        ParSort.sort(array, 0, array.length);
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    timeList.add(time);
                    System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t20times Time:" + time + "ms");
                }
                try {
                    FileOutputStream fis = new FileOutputStream("./src/result_"+arrlength+"_"+threads+".csv");
                    OutputStreamWriter isr = new OutputStreamWriter(fis);
                    BufferedWriter bw = new BufferedWriter(isr);
                    int j = 1;
                    for (long i : timeList) {
                        String content = (double) j/2 + "," + (double) i / 10 + "\n";
                        j++;
                        bw.write(content);
                        bw.flush();
                    }
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                arrlength = arrlength*2;
            }
            threads = threads*2;
        }

    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
