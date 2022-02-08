/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */

public class MyThread extends Thread {

    static Parameters params;
    static SavedData data;
    static AllPopulation bestPopulation;
    Population population;

    long time;
    static long maxTime;

    volatile int flag = 0;

    static int maxIterations = 20000;

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
        for (Individual individual : population.getList()) {
            if (ind.getFinalEval() > individual.getFinalEval()) {
                ind.updateInd(individual);
                bestTime = this.time;
                bestIteration = iterations;
            }
        }

        while (flag == 0 || flag == 1) {

            while(!isInterrupted()){
                iterations++;

                population.generateChilds();
                population.compare();

                finalTime = System.currentTimeMillis();
                this.time = finalTime - initialTime;



                for (Individual individual : population.getList()) {

                    if (ind.getFinalEval() > individual.getFinalEval()) {
                        ind = individual;
                        bestTime = this.time;
                        bestIteration = iterations;
                    }

                }

            }
            writePopulation();
            if(flag == 2)
                break;

            while(flag == 0);

        }

        data.writeData(ind, bestTime, bestIteration);
    }

    /**
     * Method to save the best population for each thread
     */
    public void writePopulation(){
        bestPopulation.saveIndividuals(population.getList());
    }

    /**
     * Method to get the best population for each thread
     */
    public void getBestPopulation(){
        population.getList().clear();
        population.getList().addAll(bestPopulation.getBest());
    }

    /**
     * Auxiliary method to set the flag to 1 to exit the while loop
     * @param flag - the flag
     */
    public void setFlag(int flag){
        this.flag = flag;
    }

}
