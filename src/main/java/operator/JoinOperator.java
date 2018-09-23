package operator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.HashMap;
import java.util.Map;

public class JoinOperator extends Operator{
    private Operator op;
    private PlainSelect ps;
    private Map<String, Integer> schema;

    public JoinOperator(Operator opLeft, Operator opRight, PlainSelect ps){
        this.op = op;
        this.ps = ps;
        this.schema = new HashMap<>();
        schema.putAll(opLeft.getSchema());
        schema.putAll(opRight.getSchema());
    }

    public Tuple getNextTuple(){
        Tuple tuple = null;
        
        return tuple;
    }

    public void reset(){
    }

}