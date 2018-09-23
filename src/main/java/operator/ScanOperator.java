package operator;

import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;

import model.Tuple;
import net.sf.jsqlparser.statement.select.PlainSelect;
import util.Catalog;

public class ScanOperator extends Operator{
    private Operator op;
    private File file;
    private RandomAccessFile readerPointer;
    private Map<String, Integer> schema;

    public ScanOperator(PlainSelect plainSelect, int tableIndex){
        this.op = null;
        
        String item;
        if(tableIndex == 0){
            item = plainSelect.getFromItem().toString();
        }
        else{
            item = plainSelect.getJoins().get(tableIndex-1).toString();
        }

        String[] strs = item.split("\\s+");
        if(strs.length < 0){
            this.file = null;
            return;
        }
        String tableName = strs[0];
        String aliasName = strs[strs.length - 1];
        this.file = new File(Catalog.getInstance().getDataPath(tableName));
        initReaderPointer();

        String fromItem = plainSelect.getFromItem().toString();
        Catalog.getInstance().setAliases(fromItem);
        Catalog.getInstance().updateCurrentSchema(aliasName);

        this.schema = Catalog.getInstance().getCurrentSchema();

    }

    public ScanOperator(Operator op, PlainSelect plainSelect, File file){
        this.op = op;
        this.file = file;
        initReaderPointer();
        
        String fromItem = plainSelect.getFromItem().toString();
        Catalog.getInstance().setAliases(fromItem);
        Catalog.getInstance().updateCurrentSchema(getAlias(fromItem));

        this.schema = Catalog.getInstance().getCurrentSchema();

    }

    /**
     * get the next tuple of the operator.
     */
    @Override
    public Tuple getNextTuple(){
        Tuple tuple = null;
        if(this.op != null){
            tuple = this.op.getNextTuple();
        }
        try{
            String s = readerPointer.readLine();
            if(s == null) return tuple;
            tuple = new Tuple(s);
        }catch(IOException e){
            System.out.print("Met error when read line");
        }
        return tuple;
    }

    /**
     * reset the operator.
     */
    @Override
    public void reset(){
        try{
			readerPointer.seek(0);
		}catch(IOException e){
			System.out.println("Files not found.");
		}
    }

    /**
     * get the next tuple of the operator.
     */
    @Override
    public void dump(int i){
    }

    /**
     * get the schema
     */
    @Override
    public Map<String, Integer> getSchema(){
        return this.schema;
    }

    private void initReaderPointer(){
        try{
            this.readerPointer = new RandomAccessFile(this.file, "r");
        }catch(FileNotFoundException e){
            System.out.printf("Cannot find file %s!\n", this.file.getName());
        }
    }

    private String getAlias(String fromItem){
        String[] strs = fromItem.split("\\s+");
        if (strs.length == 0) {
            return "";
        }
        else{
            return strs[strs.length - 1];
        }
    }
}