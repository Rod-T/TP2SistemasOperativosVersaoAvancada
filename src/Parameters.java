import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/** @author - Rodrigo Tavares
 * @docente - Aníbal Ponte
 * */
public class Parameters {

    private int m;
    private int maxLenght;
    private int[] lenghts;
    private int[] limits;

    /**
     * Builder for Parameters
     */
    public Parameters() {
        this.m = 0;
        this.maxLenght = 0;
    }

    /**
     * readFile - reads the parameters from the problem file which has a specific structure ence why we can read like this
     * @param fileName - name of the file to read
     * @return params - a new instance of Parameters that contains all the parameters on the file
     */
    public static Parameters readfile(String fileName) {
        Parameters params = new Parameters();

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            params.m = scanner.nextInt();
            params.maxLenght = scanner.nextInt();
            params.lenghts = new int[params.m];
            params.limits = new int[params.m];

            for (int i = 0; i < params.m; i++) {
                params.lenghts[i] = scanner.nextInt();
            }

            for (int i = 0; i < params.m; i++) {
                params.limits[i] = scanner.nextInt();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * Returns this.m
     * @return m
     */
    public int getM() {
        return m;
    }

    /**
     * Returns this.maxLenght
     * @return maxLenght
     */
    public int getMaxLenght() {
        return maxLenght;
    }

    /**
     * Returns array lenghts
     * @return lenghts
     */
    public int[] getLenghts() {
        return lenghts;
    }

    /**
     * Returns limits array
     * @return limits
     */
    public int[] getLimits() {
        return limits;
    }

    /**
     * toString method
     * @return string
     */
    @Override
    public String toString() {
        return "Parameters{" +
                "m=" + m +
                ", maxLenght=" + maxLenght +
                ", lenghts=" + Arrays.toString(lenghts) +
                ", limits=" + Arrays.toString(limits) +
                '}';
    }
}