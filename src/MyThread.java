/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */

public class MyThread extends Thread {

    static Parameters params;
    static SavedData data;

    static long time;
    static long maxTime;

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
        long finalResTime;
        long time;

        Population population = new Population(params);

        do{

            count ++;
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

            data.writeData(ind,finalResTime,count);
        } while (MyThread.time < maxTime && count < maxIterations);
    }
}
