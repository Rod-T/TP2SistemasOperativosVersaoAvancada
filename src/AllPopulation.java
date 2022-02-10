import java.util.ArrayList;
import java.util.Collections;

public class AllPopulation {

    ArrayList<Individual> allPopulation;
    ArrayList<Individual> bestIndividuals;
    int MAX_SIZE = 75;

    /**
     * Builder for AllPopulation
     */
    public AllPopulation(){
        this.allPopulation = new ArrayList<>();
        bestIndividuals = new ArrayList<>();
    }

    /**
     * Saves the Individuals of each thread to the whole Population
     * @param inds - the array of individuals from each thread
     */
    public synchronized void saveIndividuals(ArrayList<Individual> inds){
        this.allPopulation.addAll(inds);
    }

    /**
     * Initializes the arraylists and numberOfThreads updated for each test cycle
     */
    public void initialize(){
        this.allPopulation.clear();
        this.bestIndividuals.clear();
    }

    /**
     * Sorts the whole population and writes the best 75 to the arraylist bestIndividuals
     */
    public synchronized void getBest75(){

        Collections.sort(allPopulation);

        for (int i = 0; i < MAX_SIZE; i++) {
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
