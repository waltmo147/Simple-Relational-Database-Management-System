package operator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class ScanOperator extends Operator{
    private Operator op;
    private File file;
    private RandomAccessFile readerPointer;

    public ScanOperator(File file){
        this.op = null;
        this.file = file;
        initReaderPointer();
    }

    public ScanOperator(Operator op, PlainSelect ps, File file){
        this.op = op;
        this.file = file;
        initReaderPointer();
    }

    private void initReaderPointer(){
        try{
            this.readerPointer = new RandomAccessFile(this.file, "r");
        }catch(FileNotFoundException e){
            System.out.printf("Cannot find file %s!\n", this.file.getName());
        }
    }

    public Tuple getNextTuple(){
        Tuple tuple = null;
        if(this.op != null){
            tuple = this.op.getNextTuple();
        }
        try{
            String s = readerPointer.readLine();
            if(s.length() == 0) return tuple;
            tuple = new Tuple(s);
        }catch(IOException e){
            System.out.print("Met error when read line");
        }
        return tuple;
    }

    public void reset(){
    }

}