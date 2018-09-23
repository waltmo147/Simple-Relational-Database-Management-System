package operator;

import net.sf.jsqlparser.statement.select.PlainSelect;
import util.Catalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * SortOperator
 * created by Yufu Mo ym445
 */
public class SortOperator extends Operator{
    private List<Tuple> tupleList;
    private final PlainSelect plainSelect;
    private int currentIndex;


    public SortOperator(Operator operator, PlainSelect plainSelect) {
        tupleList = new ArrayList<Tuple>();
        this.plainSelect = plainSelect;

        // initialize the list
        Tuple tuple = operator.getNextTuple();
        while(tuple != null) {
            tupleList.add(tuple);
            tuple = operator.getNextTuple();
        }

        Collections.sort(tupleList, new TupleComparator());
        operator.reset();
    }

    /**
     * get the next tuple of the operator.
     */
    @Override
    public Tuple getNextTuple() {
        // TODO Auto-generated method stub
        Tuple tuple = null;
        if (currentIndex < tupleList.size()) {
            tuple = tupleList.get(currentIndex);
        }
        currentIndex++;
        return tuple;
    }

    /**
     * reset the operator.
     */
    @Override
    public void reset() {
        // TODO Auto-generated method stub
        currentIndex = 0;
    }

    /**
     * for debugging, get all the tuples at once and put them in a file.
     * @param index the index of the output file.
     */
    @Override
    public void dump(String s) {
        // TODO Auto-generated method stub
        Tuple tuple = null;
        BufferedWriter output = null;
        try{
            File file = new File(s + index);
            StringBuilder sb = new StringBuilder();
            output = new BufferedWriter(new FileWriter(file));
            while((tuple = nextTuple())!=null){
                sb.append(tuple.toString());
                sb.append("\n");
                System.out.println(tuple);
            }
            output.write(sb.toString());
            output.close();
        }catch(IOException e){
            System.out.println("An exception occurs!");
        }
        reset();
    }


    /**
     * comparator to sort tuples
     */
    class TupleComparator implements Comparator<Tuple> {

        List<Integer> order = plainSelect.getOrderByElements();
        Catalog catalog = Catalog.getInstance();

        @Override
        public int compare(Tuple t1, Tuple t2) {
            // TODO Auto-generated method stub
            // sort tuples from the order from sql query.
            for (int i = 0; i < order.size(); i++) {
                String str = order.get(i).toString();
                int index = catalog.getIndexOfColumn(str);
                if (t1.getDataAt(index) > t2.getDataAt(index)) {
                    return 1;
                }
                if (t1.getDataAt(index) < t2.getDataAt(index)) {
                    return -1;
                }
            }

            // for tie breaker
            // sort tuples by the order of columns.
            for (int i = 0; i < catalog.getCurrentSchema().size(); i++){
                if (t1.getDataAt(i) > t2.getDataAt(i)) {
                    return 1;
                }
                if (t1.getDataAt(i) < t2.getDataAt(i)) {
                    return -1;
                }
            }
            return 0;
        }
    }
}
