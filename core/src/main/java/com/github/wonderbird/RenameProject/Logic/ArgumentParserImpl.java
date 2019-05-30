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
 * @see <a href="https://stackoverflow.com/questions/367706/how-to-parse-command-line-arguments-in-java">Related
 * StackOverflow article</a>
 */
public class ArgumentParserImpl implements ArgumentParser
{
   @Override
   public void parse(final String[] aArgs) throws WrongUsageException
   {
      final Options options = new Options();

      final Option from = new Option("f", "from", true, "project name to replace");
      from.setRequired(true);
      options.addOption(from);

      final Option to = new Option("t", "to", true, "replacement project name");
      to.setRequired(true);
      options.addOption(to);

      final Option dir = new Option("d", "dir", true, "directory benath which to replace the project name");
      options.addOption(dir);

      final CommandLineParser parser = new DefaultParser();

      try
      {
         final CommandLine cmd = parser.parse(options, aArgs);

         final Configuration config = Configuration.getConfiguration();
         final String fromValue = cmd.getOptionValue("from");
         final String toValue = cmd.getOptionValue("to");
         config.addFromToPair(fromValue, toValue);

         String startDir = cmd.getOptionValue("dir");
         if(startDir == null)
         {
            startDir = ".";
         }
         config.setStartDir(startDir);
      }
      catch(final ParseException aException)
      {
         final StringWriter stringWriter = new StringWriter();
         final PrintWriter helpWriter = new PrintWriter(stringWriter);
         final HelpFormatter helpFormatter = new HelpFormatter();
         helpFormatter.printHelp(helpWriter, 80, "java RenameProject", null, options, 0, 3, null, true);

         final String message = String.format("%s\n\n%s", aException.getLocalizedMessage(), stringWriter.toString());

         throw new WrongUsageException(message);
      }
   }
}
