import java.util.ArrayList;

/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */

public class Main {

    public static void main(String[] args) {
        /*String file = args[0];
        int threads = Integer.parseInt(args[1]);
        int maxTime = Integer.parseInt(args[2]);*/

        String file = "prob08_3.txt";
        int threads = 20;
        int maxTime = 2000;

        AllPopulation bestPopulation = new AllPopulation(threads);

        Parameters params = Parameters.readfile(file);
        MyThread.maxTime = maxTime;

        SavedData data = new SavedData(params);

        System.out.println("Problem = " + file + " Number threads = " + threads + " Time = " + 10);
        System.out.println(params);

        try {
            for (int i = 0; i < 10; i++) {
                data.initialize();
                MyThread.data = data;
                bestPopulation.initialize();
                MyThread.bestPopulation = bestPopulation;

                ArrayList<MyThread> myThreads = new ArrayList<>();

                for (int j = 0; j < threads; j++) {
                    myThreads.add(new MyThread(params));
                    myThreads.get(j).start();
                }

                Thread.sleep((long)(maxTime*0.5));

                for (int j = 0; j < threads; j++) {
                    myThreads.get(j).interrupt();
                }

                bestPopulation.getBest75();
                bestPopulation.allPopulation.clear();

                for (int j = 0; j < threads; j++) {
                    myThreads.get(j).getBestPopulation();
                }

                for (int j = 0; j < threads; j++) {
                    myThreads.get(j).setFlag(1);
                }

                Thread.sleep((long)(maxTime*0.5));

                for (int j = 0; j < threads; j++) {
                    myThreads.get(j).setFlag(2);
                }

                for (int j = 0; j < threads; j++) {
                    myThreads.get(j).join();
                }

                SavedData newData = data.get();
                System.out.println("------------------------------------- Test " + (i + 1) + " -----------------------------------");
                System.out.println(newData.bestIndividual.getPlateList());
                System.out.println("Eval = " + newData.bestIndividual.getFinalEval() + " Best Iteration = " + newData.bestIteration + " Best Time = " + newData.bestTime);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
