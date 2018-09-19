package operator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class ScanOperatorTest{
    @Test
    public void testReadFile(){
        String tableName = "Boats";
        String tableFolder = "Samples/samples/input/db/data/";
        File file = new File(tableFolder + tableName);

    }
}