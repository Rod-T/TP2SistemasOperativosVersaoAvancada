import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */

public class Population {
    private final ArrayList<Individual> individuals;
    private final int maxPopulation = 75;

    /**
     * Returns the list of individuals to be used throughout the code
     * @return individuals
     */
    public ArrayList<Individual> getList() {
        return individuals;
    }

    /**
     * Builder for Population, receives parameters to pass down to each individual in the population
     * @param params - parameters to pass to each individual
     */
    public Population(Parameters params) {
        this.individuals = new ArrayList<>();
        for (int i = 0; i < maxPopulation; i++) {
            individuals.add(new Individual(params));
        }
        this.calculateCost();
    }

    /**
     * This function generates the childs for each individual making it a
     */
    public void generateChilds() {
        double aggProbability;
        ArrayList<Double> probability;
        ArrayList<Double> aggProbabilitiesList;
        ArrayList<Integer> index;
        ArrayList<Integer> individualsList;
        ArrayList<ArrayList<Integer>> coordinates;
        ArrayList<Individual> helper = new ArrayList<>();

        //Copia cada indivíduo para depois poder-mos fazer a mutação 3ps
        for (Individual individual : individuals) {

            coordinates = new ArrayList<>();
            aggProbability = 0;
            aggProbabilitiesList = new ArrayList<>();
            index = new ArrayList<>();

            //Cálculos para a formula do custo
            probability = calc2(individual);

            //Calcular a probabilidade agregada
            for (double x : probability) {
                aggProbability += x;
                aggProbabilitiesList.add(aggProbability);
            }

            //Calcular a probabilidade de cada placa
            calcPlateProb(index,aggProbabilitiesList);

            //Copiar as coordenadas da lista index para uma lista nova
            for (int number : index) {
                if (number == individual.getListOfIndex().size()) {
                    ArrayList<Integer> coords = new ArrayList<>();
                    coords.add(individual.getListOfIndex().get(number - 2));
                    coords.add(individual.getListOfIndex().get(number - 1));
                    coordinates.add(coords);
                }else{
                    if (number != 0) {
                        ArrayList<Integer> coords = new ArrayList<>();
                        coords.add(individual.getListOfIndex().get(number - 1) + 1);
                        coords.add(individual.getListOfIndex().get(number));
                        coordinates.add(coords);
                    } else {
                        ArrayList<Integer> coords = new ArrayList<>();
                        coords.add(0);
                        coords.add(individual.getListOfIndex().get(number));
                        coordinates.add(coords);
                    }
                }
            }

            //Fazer a mutação para cada indivíduo e de seguida adicionar o mesmo à arraylist helper para os guardar
            mutation3PS(individual,coordinates);
            helper.add(individual);
        }

        //Adiciona todos os novos indivíduos criados à lista
        individuals.addAll(helper);

        //Avaliar o custo de cada novo indíviduo
        this.calculateCost();
    }


    /**
     * Auxiliar method to calculate the probability of each plate
     * @param index - the ArrayList that contains the index number
     * @param aggProbabilitiesList - the ArrayList of the aggregate probability list
     */
    public void calcPlateProb(ArrayList<Integer> index, ArrayList<Double> aggProbabilitiesList){
        int count;

        for (int i=0; i < 3; i++) {
            count = 0;
            double rand = Math.random();
            for (double prob : aggProbabilitiesList) {
                if (rand <= prob) {
                    break;
                }
                count ++;
            }
            index.add(count);
        }
    }

    /**
     * This method calculates the cost based on a formula that was provided by the teacher
     */
    public void calculateCost() {

        //Somatório da formula que contém o w e maxLenght
        double totalWaste;

        //V da formula
        double plate;

        for (Individual i : individuals) {
            totalWaste = 0;
            plate = 0;

            for (int waste : i.getWasteList()) {
                totalWaste += Math.sqrt((double) waste/ (double) i.getParams().getMaxLenght());

                if (waste > 0) {
                    plate += (1.0/i.getNumberOfCuts());
                }
            }
            i.setEval((1.0/((double) i.getNumberOfCuts() + 1.0)*(totalWaste + plate)));
        }
    }

    //funcao para comparar qual o melhor individuo

    /**
     * This function compares each individual to each other to discover which ones are the best
     */
    public void compare() {
        int count = 0;
        ArrayList<Individual> bestIndividuals = new ArrayList<>();

        for (Individual ind : individuals) {
            for (int i=0 ; i<10 ; i++) {
                Random r = new Random();
                if (ind.getEval() > individuals.get(r.nextInt(individuals.size())).getEval()) {
                    ind.setP(ind.getP()-1);
                }
            }
        }

        Collections.sort(individuals);

        for (Individual item : individuals) {
            if (count < maxPopulation) {
                bestIndividuals.add(item);
            }
            count ++;
        }

        //Limpar a lista e adicionar os melhores individuos
        individuals.clear();
        individuals.addAll(bestIndividuals);
    }

    /**
     * Mutation 3ps is a swap3 method which swaps 3 variables, in our case it swaps each individuals' position
     * @param ind - the individual to mutate
     * @param coords - the list of coordinates
     */
    public void mutation3PS(Individual ind, ArrayList<ArrayList<Integer>> coords) {
        Random r = new Random();
        ArrayList<Integer> individualsList = ind.getPlateList();

        int p1 = r.nextInt(coords.get(0).get(1)+1 - coords.get(0).get(0)) + coords.get(0).get(0);
        int p2 = r.nextInt(coords.get(1).get(1)+1 - coords.get(1).get(0)) + coords.get(1).get(0);
        int p3 = r.nextInt(coords.get(2).get(1)+1 - coords.get(2).get(0)) + coords.get(2).get(0);
        int aux = individualsList.get(p1);

        individualsList.set(p1, individualsList.get(p2));
        individualsList.set(p2, individualsList.get(p3));
        individualsList.set(p3, aux);

        ind.setPlateList(individualsList);
    }

    /**
     * Auxiliar method to calculate the evaluation based on wastage for each Individual
     * @param ind - the individual
     * @return j
     */
    public double calc1(Individual ind){
        double j = 0;

        for (int waste : ind.getWasteList()) {
            if (waste != 0) {
                j += Math.sqrt(1.0/waste);
            }
        }
        return j;
    }

    /**
     * Auxiliar method to calculate the evaluation based on wastage for each individual
     * @param ind - the individual
     * @return probability
     */
    public ArrayList<Double> calc2(Individual ind) {
        ArrayList<Double> prob = new ArrayList<>();
        double j = calc1(ind);

        //se wj > 0 = 1 caso contrario = 0
        for (int desperdicio : ind.getWasteList()) {
            if (desperdicio == 0) {
                prob.add(0.0);
            } else {
                prob.add(Math.sqrt(1.0/desperdicio)/ j);
            }
        }

        return  prob;
    }

    /**
     * returns population in string format
     * @return the string
     */
    @Override
    public String toString() {
        return "Population{" +
                "individuals=" + individuals +
                ", maxPopulation=" + maxPopulation +
                '}';
    }
}
