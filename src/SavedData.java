/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */

public class SavedData {

    long bestIteration;
    long bestTime;
    Parameters params;
    Individual bestIndividual;

    /**
     * Builder for the saved data
     * @param params - the parameters for the data to pass to each individual
     */
    public SavedData(Parameters params) {
        this.params = params;
        bestIndividual = new Individual(params);
        bestIndividual.setFinalEval(Integer.MAX_VALUE);
    }

    /**
     * Method to save to the shared memory, this has to be synchronized so the threads won't corrupt the data trying to write at the same time
     * @param bestIndividual - the best individual
     * @param bestTime - the best time to find the solution
     * @param bestIteration - the best iteration to find the solution
     */
    public synchronized void writeData(Individual bestIndividual, long bestTime, long bestIteration) {
        if (this.bestIndividual.getFinalEval() > bestIndividual.getFinalEval()) {
            this.bestIndividual = bestIndividual;
            this.bestTime = bestTime;
            this.bestIteration = bestIteration;
        }
    }

    /**
     * get the saved data
     * @return savedData
     */
    public synchronized SavedData get() {
        return this;
    }
}
