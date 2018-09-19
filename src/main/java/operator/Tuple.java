package operator;

public class Tuple {
    private String[] data; // string array to store data


    /**
     * Constructor for tuple
     * @param s
     */
    public Tuple(String s) {
        data = s.split(",");
    }

    /**
     * Overload constructor for tuple with empty input
     * @param length
     */
    public Tuple(int length) {
        data = new String[length];
    }


    /**
     * Overload constructor with data input
     * @param data
     */
    public Tuple(String[] data) {
        this.data = data;
    }

}
