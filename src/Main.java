import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */

public class Main {

    public static void main(String[] args) {
        /*String file = args[0];
        int threads = Integer.parseInt(args[1]);
        int maxTime = Integer.parseInt(args[2]);
        int percentage = Integer.parseInt(args[3]);
        double timeToSleep = (double)percentage / 100;
        int percIterations = 100 / percentage;*/

        String file = "prob08_3.txt";
        int threads = 4;
        int maxTime = 40000;
        int percentage = 25;
        double percentageTimeToSleep = (double) percentage / 100;
        int percentageIterations = 100 / percentage;

        AllPopulation bestPopulation = new AllPopulation();

        Parameters params = Parameters.readfile(file);
        MyThread.maxTime = maxTime;

        SavedData data = new SavedData(params);

        System.out.println("Problem = " + file + " Number threads = " + threads + " Time = " + maxTime/1000);
        System.out.println(params);

        Semaphore threadContinueSem;
        Semaphore threadGatherSem;

        threadContinueSem = new Semaphore(0);
        threadGatherSem = new Semaphore(0);


        for (int i = 0; i < 10; i++) {
            data.initialize();
            MyThread.data = data;
            bestPopulation.initialize();
            MyThread.bestPopulation = bestPopulation;

            MyThread.threadContinueSem = threadContinueSem;
            MyThread.threadGatherSem = threadGatherSem;

            ArrayList<MyThread> myThreads = new ArrayList<>();

            for (int j = 0; j < threads; j++) {
                myThreads.add(new MyThread(params));
                myThreads.get(j).start();
            }

            int numberParts = 100 / percentage;
            if (100 % percentage != 0)
                numberParts++;
            long timeExec = 0;
            //System.out.println("numberParts ="+numberParts);
            //long initTime = System.currentTimeMillis();

            MyThread.running = true;
            MyThread.merging = false;
            for (int k=0; k < numberParts; k++) {
                // last part
                if (k == (numberParts-1)) {
                    MyThread.merging = false;
                    timeExec = (long) ( maxTime -(numberParts-1)*percentageTimeToSleep*maxTime);
                    try {
                        Thread.sleep((long) (timeExec));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("Total time = "+ (System.currentTimeMillis()-initTime)/1000.0);

                    MyThread.running = false;
                    for (int j = 0; j < threads; j++) {
                        try {
                            myThreads.get(j).join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    try {
                        Thread.sleep((long) (maxTime * percentageTimeToSleep));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    MyThread.merging = true;

                    // Espera que todas as threads façam merging
                    for (int j = 0; j < threads; j++) {
                        try {
                            MyThread.threadGatherSem.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    bestPopulation.getBest75();

                    MyThread.merging = false;

                    // Sinaliza que todas as threads terminaram a copia
                    for (int j = 0; j < threads; j++) {
                        MyThread.threadContinueSem.release();
                    }
                    bestPopulation.allPopulation.clear();

                }
            }
            SavedData newData = data.get();
            System.out.println("------------------------------------- Test " + (i + 1) + " -----------------------------------");
            System.out.println(newData.bestIndividual.getPlateList());
            System.out.println("Eval = " + newData.bestIndividual.getFinalEval() + " Best Iteration = " + newData.bestIteration +
                    " Best Time = " + (newData.bestTime / 1000.0));
        }

    }
}
