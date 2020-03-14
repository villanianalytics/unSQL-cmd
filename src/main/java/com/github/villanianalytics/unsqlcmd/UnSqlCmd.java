package com.github.villanianalytics.unsqlcmd;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.github.villanianalytics.unsql.UnSql;
import com.github.villanianalytics.unsql.exception.UnSqlException;
import com.github.villanianalytics.unsql.model.Result;

public class UnSqlCmd {

	private static final String FILE = "file";
	private static final String QUERY = "query";

	public static void main(String[] args) {
		Options options = getOptions();
		printHelp(options);
		printUsage(options);
		
		final CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption(FILE) && cmd.hasOption(QUERY)) {
				String filePath = cmd.getOptionValue(FILE);
				String query = cmd.getOptionValue(QUERY);
				
				String raw = readFile(filePath);
				
				UnSql util = new UnSql();
				List<Result> results = util.runQuery(raw, query);
				
				System.out.println("Results:");
				for (Result result : results) {
					System.out.println("Result " + result.getResults());
				}
				
			}
		} catch (ParseException e) {
			System.out.println("Parse Exception " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException " + e.getMessage());
		} catch (UnSqlException e) {
			System.out.println("UnSqlException " + e.getMessage());
		}
	}

	private static Options getOptions() {
		Options options = new Options();

		Option optFile = Option.builder(FILE).hasArg(true).longOpt("Json and Sql file").desc("File").required().build();
		options.addOption(optFile);

		Option optQuery = Option.builder(QUERY).hasArg(true).longOpt("SQL query").desc("Query").required()
				.build();
		options.addOption(optQuery);

		return options;
	}

	private static void printUsage(final Options options) {
		final HelpFormatter formatter = new HelpFormatter();
		final String syntax = "Main";
		System.out.println("\n=====");
		System.out.println("USAGE");
		System.out.println("=====");
		
		final PrintWriter pw = new PrintWriter(System.out);
		formatter.printUsage(pw, 80, syntax, options);
		pw.flush();
	}

	private static void printHelp(final Options options) {
		final HelpFormatter formatter = new HelpFormatter();
		final String syntax = "Main";
		final String usageHeader = "UNSQL CMD";
		final String usageFooter = "";
		
		System.out.println("\n====");
		System.out.println("HELP");
		System.out.println("====");
		formatter.printHelp(syntax, usageHeader, options, usageFooter);
	}

	private static String readFile(String filePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}
}
