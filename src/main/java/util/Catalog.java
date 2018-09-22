package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Catalog {
    private static Catalog instance = null;
    // store file location for different tables
    private Map<String, String> files = new HashMap<>();
    // store aliases map<alias, table name>
    private Map<String, String> aliases = new HashMap<>();
    // keep track of current schema map<alias, index>
    private Map<String, Integer> currentSchema = new HashMap<>();
    // store all the table-schema pairs
    private Map<String, Map<String, Integer>> schemas = new HashMap<>();


    /**
     * private constructor for singleton class
     */
    private Catalog() {
        try {
            FileReader file = new FileReader(Constants.SCHEMA_PATH);
            BufferedReader br = new BufferedReader(file);
            String s = br.readLine();
            while(s != null) {
                String[] str = s.split("\\s+");
                files.put(str[0], Constants.DATA_PATH + str[0]);
                Map<String, Integer> schema = new HashMap<>();

                for(int i = 1; i < str.length; ++i) {
                    String field = str[0];
                    schema.put(field + "." + str[i], i - 1);
                }
                schemas.put(str[0], schema);
                s = br.readLine();
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Files not found!");
        }

    }


    /**
     * @return singleton instance
     */
    public static Catalog getInstance() {
        if (instance == null){
            synchronized (Catalog.class){
                if (instance == null){
                    instance = new Catalog();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    /**
     * @return current schema
     */
    public Map<String, Integer> getCurrentSchema() {
        return currentSchema;
    }

    /**
     * move the current schema pointer
     * @param alias: change current schema to a new one
     */
    public void setCurrentSchema(String alias) {
        currentSchema = schemas.get(alias);
    }

    /**
     * move the current schema pointer
     * @param schema: change current schema to a new one
     */
    public void setCurrentSchema(Map<String, Integer> schema) {
        currentSchema = schema;
    }

    /**
     * return the file's path of certain table
     * @param table
     * @return path of data file
     */
    public String getDataPath(String table) {
        return files.get(table);
    }

    /**
     * to set aliases to deal with the occasional failure of sqlparser
     * @param str the str parsed by sqlparser
     */
    public void setAliases(String str) {
        String[] strs = str.split("\\s+");
        if (strs.length == 0 || strs.length == 1) {
            return;
        }

        if (strs.length == 2) {
            aliases.put(strs[1], strs[0]);
        }

        if (strs.length == 3) {
            aliases.put(strs[2], strs[0]);
        }
    }

    /**
     * getColumnNameFromAlias
     * @param alias
     * @return
     */
    public String getTableNameFromAlias(String alias) {
        return aliases.getOrDefault(alias, alias);
    }

    /**
     *
     * @param table
     * @return
     */
    public Map<String, Integer> getTableSchema(String table) {
        return schemas.get(table);
    }

    public int getIndexOfColumn(String colomn) {
        return currentSchema.get(colomn);
    }
}
