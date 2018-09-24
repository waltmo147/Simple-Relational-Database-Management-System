package com.sql.interpreter;

import java.io.FileReader;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import util.Catalog;

/**
 * Example class for getting started with JSQLParser. Reads SQL statements from
 * a file and prints them to screen; then extracts SelectBody from each query
 * and also prints it to screen.
 * 
 * @author Lucja Kot
 */
public class App {

	private static final String queriesFile = "Samples/samples/input/queries.sql";

	public static void main(String[] args) {

        Handler handler = new Handler();
        if (args != null || args.length != 0) {
            Catalog.getInstance().setInputPath(args[0]);
            Catalog.getInstance().setOutputPath(args[1]);
            handler.init();
        }
		try {
			// try
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				System.out.println("Select body is " + select.getSelectBody());
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
//		try {
//			// try
//			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
//			Statement statement;
//			while ((statement = parser.Statement()) != null) {
//				System.out.println("Read statement: " + statement);
//				Select select = (Select) statement;
//				System.out.println("Select body is " + select.getSelectBody());
//			}
//		} catch (Exception e) {
//			System.err.println("Exception occurred during parsing");
//			e.printStackTrace();
//		}


	}
}
