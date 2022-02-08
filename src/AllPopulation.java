import java.util.ArrayList;
import java.util.Collections;

public class AllPopulation {

    ArrayList<Individual> allPopulation;
    ArrayList<Individual> bestIndividuals;
    int numberThreadsUpdated;
    int threads;
    int MAX_SIZE = 75;


    public AllPopulation(int threads){
        this.allPopulation = new ArrayList<>();
        bestIndividuals = new ArrayList<>();
        this.numberThreadsUpdated = 0;
        this.threads = threads;
    }

    public synchronized void saveIndividuals(ArrayList<Individual> inds){
        this.allPopulation.addAll(inds);
        this.numberThreadsUpdated++;
        if(numberThreadsUpdated == threads) {
            notify();
            System.out.println("notify()");
        }
    }

    public void initialize(){
        this.numberThreadsUpdated = 0;
        this.allPopulation.clear();
        this.bestIndividuals.clear();
    }

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

    public ArrayList<Individual> getBest(){
        return bestIndividuals;
    }
}
