package operator;

import org.junit.Test;
import java.io.File;

public class ScanOperatorTest{

    @Test
    public void testReadFile(){
        String tableName = "Boats";
        String tableFolder = "Samples/samples/input/db/data/";
        File file = new File(tableFolder + tableName);

        Operator op = new ScanOperator(file);
        while(op.getNextTuple()!=null){
            System.out.println("read successfully");
        }

    }
}