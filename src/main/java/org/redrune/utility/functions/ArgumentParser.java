package org.redrune.utility.functions;

import org.apache.commons.cli.*;
import org.redrune.game.GameFlags;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-25
 */
public class ArgumentParser {
	
	/**
	 * Parses the arguments from the jvm
	 */
	public static void parseArgs(String[] args) {
		Options options = new Options();
		Option hostOption = new Option("hostMode", "input", true, "game host mode");
		hostOption.setRequired(true);
		Option debugOption = new Option("debugMode", "input", true, "debug server mode");
		options.addOption(hostOption);
		options.addOption(debugOption);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);
			System.exit(1);
			return;
		}
		GameFlags.hostMode = Boolean.parseBoolean(cmd.getOptionValue("hostMode"));
		GameFlags.debugMode = Boolean.parseBoolean(cmd.getOptionValue("debugMode"));
	}
}
