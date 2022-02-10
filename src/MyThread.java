import java.util.concurrent.Semaphore;

/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */

public class MyThread extends Thread {

    static Parameters params;
    static SavedData data;
    static AllPopulation bestPopulation;
    Population population;

    //long time;
    static long maxTime;

    //static int maxIterations = 20000;

    static Semaphore threadContinueSem;
    static Semaphore threadGatherSem;

    static volatile boolean running;
    static volatile boolean merging;



    /**
     * Builder for MyThread
     * @param params - the parameters
     */
    public MyThread(Parameters params) {
        MyThread.params = params;
    }

    /**
     * The method that each thread will do
     */
    @Override
    public void run() {
        Individual ind = new Individual(params);
        ind.setFinalEval(Integer.MAX_VALUE);
        int iterations = 0;
        int bestIteration = 0;

        long initialTime = System.currentTimeMillis();
        long finalTime;
        long bestTime = 0;


        population = new Population(params);
        finalTime = System.currentTimeMillis() - initialTime;

        for (Individual individual : population.getList()) {
            if (ind.getFinalEval() > individual.getFinalEval()) {
                ind.updateInd(individual);
                bestTime = finalTime;
                bestIteration = iterations;
            }
        }

        while (running) {
            iterations++;
            if(merging) {
                writePopulation();
                try {
                    threadContinueSem.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            population.generateChilds();
            population.compare();

            finalTime = System.currentTimeMillis() - initialTime;

            for (Individual individual : population.getList()) {
                if (ind.getFinalEval() > individual.getFinalEval()) {
                    ind.updateInd(individual);
                    bestTime = finalTime;
                    bestIteration = iterations;
                }
            }
        }
        data.writeData(ind, bestTime, bestIteration);
    }

    /**
     * Method to save the best population for each thread
     */
    public void writePopulation(){
        bestPopulation.saveIndividuals(population.getList());
        this.threadGatherSem.release();
    }

    /**
     * Method to get the best population for each thread
     */
    public void getBestPopulation(){
        population.getList().clear();
        population.getList().addAll(bestPopulation.getBest());
    }


}
