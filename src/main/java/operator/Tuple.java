package operator;

public class Tuple {
    private long[] data; // string array to store data


    /**
     * Constructor for tuple
     * @param s
     */
    public Tuple(String s) {
        String[] sData = s.split(",");
        data = new long[sData.length];
        for(int i=0; i<sData.length; ++i){
            data[i] = Long.parseLong(sData[i]);
        }
    }

    /**
     * Overload constructor for tuple with empty input
     * @param length
     */
    public Tuple(int length) {
        data = new long[length];
    }


    /**
     * Overload constructor with data input
     * @param data
     */
    public Tuple(long[] data) {
        this.data = data;
    }

    /**
     * Get data by index
     * @param index
     * @return long
     */
    public long getDataAt(int index){
        return data[index];
    }

}
