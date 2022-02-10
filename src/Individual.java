import java.util.ArrayList;
import java.util.Collections;

/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */

public class Individual implements Comparable<Individual>{

    // Private variables for each individual
    private int cuts;
    private int finalEval;
    private double eval;

    // Private Lists
    private final ArrayList<Integer> indexList;
    private ArrayList<Integer> plateList;
    private final ArrayList<Integer> wasteList;
    private final ArrayList<Integer> cutsList;

    // The parameters
    private final Parameters params;

    /**
     * Builder for Individual
     * @param params - the parameters to associate with the individual
     */
    public Individual(Parameters params) {
        this.indexList = new ArrayList<>();
        this.plateList = new ArrayList<>();
        this.wasteList = new ArrayList<>();
        this.cutsList = new ArrayList<>();

        this.params = params;

        this.cuts = 0;
        this.eval = 0;
        this.finalEval = 0;

        this.createPlateList();
        this.createWasteList();
    }

    /**
     * Method to create the list of plates and then shuffle them so each individual can be different
     */
    public void createPlateList() {

        for(int i = 0; i < params.getM(); i++){
            for(int j = 0; j < params.getLimits()[i]; j++){
                plateList.add(params.getLenghts()[i]);
            }
        }

        Collections.shuffle(plateList);
    }

    /**
     * Method to cut the pieces and to calculate the wastage after
     */
    public void createWasteList() {
        int sum = 0;
        int count = 0;

        for (int ignored : plateList) {
            cutsList.add(0);
        }

        //Cálculo para preencher os cortes
        for (int item : plateList) {
            if (count - 1 == -1) {
                sum = item;
            } else {
                sum = sum + item;
                if (sum >= params.getMaxLenght()) {
                    if (sum == params.getMaxLenght()) {
                        cutsList.set(count, 1);
                        indexList.add(count);
                        wasteList.add(0);
                        sum = 0;
                    } else {
                        sum = sum - item;
                        cutsList.set(count-1, 1);
                        indexList.add(count-1);
                        wasteList.add(params.getMaxLenght() - sum);
                        sum = plateList.get(count);
                    }
                }
            }
            count++;
        }

        //Preenchimento do final do array pois senão chegava ao último e ignorava
        if (cutsList.get(cutsList.size()-1) == 0) {
            int cuts = getNumberOfCuts();
            count = 0;
            sum = 0;
            int count1 = 0;
            for (int item : cutsList) {
                count ++;
                if(item == 1) {
                    count1++;
                } else if (count1 == cuts) {
                    count1++;
                }
                if(count1 > cuts) {
                    sum = sum + plateList.get(count-1);
                } if (count == cutsList.size()) {
                    cutsList.set(count-1, 1);
                    indexList.add(count-1);
                }
            }
            wasteList.add(params.getMaxLenght() - sum);
        }

        //Cálculo da avaliação final
        int soma = 0;
        for (int i : wasteList) {
            soma += i;
        }
        finalEval = this.getWasteList().size() + soma;
    }

    /**
     * Gets the number of cuts
     * @return number of cuts
     */
    public int getNumberOfCuts() {
        cuts = 0;

        for (int i: cutsList) {
            if (i == 1) {
                cuts++;
            }
        }

        return cuts;
    }

    /**
     * Get parameters
     * @return params
     */
    public Parameters getParams() {
        return params;
    }

    /**
     * get the wastage list
     * @return wasteList
     */
    public ArrayList<Integer> getWasteList() {
        return wasteList;
    }

    /**
     * get list of Plates
     * @return plateList
     */
    public ArrayList<Integer> getPlateList() {
        return plateList;
    }

    /**
     * get list of indexes
     * @return indexList
     */
    public ArrayList<Integer> getListOfIndex() {
        return indexList;
    }

    /**
     * get final evaluation
     * @return finalEval
     */
    public int getFinalEval() {
        return finalEval;
    }

    /**
     * sets final evaluation
     * @param finalEval - final Evaluation
     */
    public void setFinalEval(int finalEval) {
        this.finalEval = finalEval;
    }

    /**
     * gets evaluation
     * @return eval
     */
    public double getEval() {
        return this.eval;
    }

    /**
     * sets evaluation
     * @param e - evaluation
     */
    public void setEval(Double e) {
        this.eval = e;
    }

    /**
     * Method to create a new list of plates
     * @param plateList - list of plates
     */
    public void setPlateList(ArrayList<Integer> plateList) {
        this.plateList = plateList;
        this.cutsList.clear();
        this.wasteList.clear();
        this.indexList.clear();
        this.createWasteList();
    }

    /**
     * Method to compare each individual to each other
     * @param o - individual to compare to
     * @return - the evaluation
     */
    @Override
    public int compareTo(Individual o) {
        return (int) (this.getEval()*1000000000 - o.getEval()*1000000000);
    }

    public void updateInd(Individual ind){
        this.indexList.clear();
        this.indexList.addAll(ind.indexList);

        this.plateList.clear();
        this.plateList.addAll(ind.plateList);

        this.wasteList.clear();
        this.wasteList.addAll(ind.wasteList);

        this.cutsList.clear();
        this.cutsList.addAll(ind.cutsList);

        this.cuts = ind.cuts;
        this.eval = ind.eval;
        this.finalEval = ind.finalEval;
    }

}
