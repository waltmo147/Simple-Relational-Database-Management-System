package com.sql.interpreter;

import util.Catalog;

/**
 * Example class for getting started with JSQLParser. Reads SQL statements from
 * a file and prints them to screen; then extracts SelectBody from each query
 * and also prints it to screen.
 * 
 * @author Lucja Kot
 */
public class App {
	public static void main(String[] args) {
        if (args != null && args.length == 2) {
            Catalog.getInstance().setInputPath(args[0]);
            Catalog.getInstance().setOutputPath(args[1]);
        }
        Handler.init();
        Handler.parseSql();
	}
}
