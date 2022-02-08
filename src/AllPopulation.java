import java.util.ArrayList;
import java.util.Collections;

public class AllPopulation {

    ArrayList<Individual> allPopulation;
    ArrayList<Individual> bestIndividuals;
    int numberThreadsUpdated;
    int nbOfThreads;
    int MAX_SIZE = 75;

    /**
     * Builder for AllPopulation
     * @param nbOfThreads - the number of threads that are currently working
     */
    public AllPopulation(int nbOfThreads){
        this.allPopulation = new ArrayList<>();
        bestIndividuals = new ArrayList<>();
        this.numberThreadsUpdated = 0;
        this.nbOfThreads = nbOfThreads;
    }

    /**
     * Saves the Individuals of each thread to the whole Population
     * @param inds - the array of individuals from each thread
     */
    public synchronized void saveIndividuals(ArrayList<Individual> inds){
        this.allPopulation.addAll(inds);
        this.numberThreadsUpdated++;

        if(numberThreadsUpdated == nbOfThreads) {
            notify();
        }
    }

    /**
     * Initializes the arraylists and numberOfThreads updated for each test cycle
     */
    public void initialize(){
        this.numberThreadsUpdated = 0;
        this.allPopulation.clear();
        this.bestIndividuals.clear();
    }

    /**
     * Sorts the whole population and writes the best 75 to the arraylist bestIndividuals
     */
    public synchronized void getBest75(){
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Collections.sort(allPopulation);

        for(int i = 0 ; i < MAX_SIZE ; i++ ){
            this.bestIndividuals.add(allPopulation.get(i));
        }
    }

    /**
     * Returns the bestIndividuals arraylist
     * @return - the best individuals
     */
    public ArrayList<Individual> getBest(){
        return bestIndividuals;
    }
}
