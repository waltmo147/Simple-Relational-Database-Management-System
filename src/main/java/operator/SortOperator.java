package operator;

import net.sf.jsqlparser.statement.select.PlainSelect;
import util.Catalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortOperator extends Operator{
    private List<Tuple> tupleList;
    private final PlainSelect ps;


    public SortOperator(Operator operator, PlainSelect ps) {
        tupleList = new ArrayList<Tuple>();
        this.ps = ps;

        // initialize the list
        Tuple tuple = operator.getNextTuple();
        while(tuple != null) {
            tupleList.add(tuple);
            tuple = operator.getNextTuple();
        }

        Collections.sort(tupleList, new Comparator<Tuple>(){
            @SuppressWarnings("rawtypes")
            List<Integer> order = ps.getOrderByElements();
            @Override
            public int compare(Tuple tup1, Tuple tup2) {
                // TODO Auto-generated method stub
                // sort tuples from the order by language first.
                for(int i=0;i<order.size();i++){
                    String str = order.get(i).toString();
                    int index = man.get(str);
                    if(tup1.getData(index)>tup2.getData(index)) return 1;
                    else if(tup1.getData(index)<tup2.getData(index)) return -1;
                }
                // sort tuples by the order of the tuples.
                for(int i=0;i<man.size();i++){
                    if(tup1.getData(i)>tup2.getData(i)) return 1;
                    else if(tup1.getData(i)<tup2.getData(i)) return -1;
                }
                return 0;
            }

        });
        operator.reset();
    }


    class TupleComparator implements Comparator<Tuple> {

        List<Integer> order = ps.getOrderByElements();
        Catalog catalog = Catalog.getInstance();

        @Override
        public int compare(Tuple t1, Tuple t2) {
            // TODO Auto-generated method stub
            // sort tuples from the order by language first.
            for(int i = 0; i < order.size(); i++){
                String str = order.get(i).toString();

                int index = catalog.getIndexOfColumn(str);
                if (t1.getData(index)>t2.getData(index)) return 1;
                else if(tup1.getData(index)<tup2.getData(index)) return -1;
            }
            // sort tuples by the order of the tuples.
            for(int i=0;i<man.size();i++){
                if(tup1.getData(i)>tup2.getData(i)) return 1;
                else if(tup1.getData(i)<tup2.getData(i)) return -1;
            }
            return 0;
        }
    }
}
