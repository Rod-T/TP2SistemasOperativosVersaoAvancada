/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */

public class MyThread extends Thread {

    static Parameters params;
    static SavedData data;
    static AllPopulation bestPopulation;
    Population population;

    static long time;
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
        int count = 0;

        long initialTime = System.currentTimeMillis();
        long finalTime;
        long resTime;
        long finalResTime = 0;
        long time;

        population = new Population(params);

        while (flag == 0 || flag == 1) {

            while(!isInterrupted()){
                count++;
                resTime = System.currentTimeMillis();

                population.generateChilds();
                population.compare();

                finalTime = System.currentTimeMillis();
                time = finalTime;

                MyThread.time = finalTime - initialTime;
                finalResTime = time - resTime;

                for (Individual individual : population.getList()) {

                    if (ind.getFinalEval() > individual.getFinalEval()) {
                        ind = individual;
                    }

                }

            }
            writePopulation();
            if(flag == 2)
                break;

            while(flag == 0);

        }

        data.writeData(ind, finalResTime, count);
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
