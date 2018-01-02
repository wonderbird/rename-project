package com.github.wonderbird.RenameProject.Logic;

import com.github.wonderbird.RenameProject.Models.Configuration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Apache Commons CLI implementation of a command line parser.
 *
 * @see <a href="https://stackoverflow.com/questions/367706/how-to-parse-command-line-arguments-in-java">Related StackOverflow article</a>
 */
public class ArgumentParserImpl implements ArgumentParser {
    @Override
    public void parse(final String[] aArgs) throws WrongUsageException {
        Options options = new Options();

        Option from = new Option("f", "from", true, "project name to replace");
        from.setRequired(true);
        options.addOption(from);

        Option to = new Option("t", "to", true, "replacement project name");
        to.setRequired(true);
        options.addOption(to);

        Option dir = new Option("d", "dir", true, "directory benath which to replace the project name");
        options.addOption(dir);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, aArgs);

            Configuration config = Configuration.getConfiguration();
            String fromValue = cmd.getOptionValue("from");
            String toValue = cmd.getOptionValue("to");
            config.addFromToPair(fromValue, toValue);

            String startDir = cmd.getOptionValue("dir");
            if (startDir == null) {
                startDir = ".";
            }
            config.setStartDir(startDir);
        } catch (ParseException aException) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter helpWriter = new PrintWriter(stringWriter);
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp(helpWriter, 80, "java RenameProject", null, options, 0, 3, null, true);

            String message = String.format("%s\n\n%s", aException.getLocalizedMessage(), stringWriter.toString());

            throw new WrongUsageException(message);
        }
    }
}
