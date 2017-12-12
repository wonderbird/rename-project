package com.github.wonderbird.RenameProject.Logic;

import com.github.wonderbird.RenameProject.Logic.ArgumentParser;
import com.github.wonderbird.RenameProject.Logic.ArgumentParserImpl;
import com.github.wonderbird.RenameProject.Logic.WrongUsageException;
import com.github.wonderbird.RenameProject.Models.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ArgumentParserImplArgsParsingTest {
    /**
     * Expected values and args array for the test cases.
     *
     * The format of the elements in this list can be derived from the sequence
     * of @Parameterized.Parameter members defined below.
     */
    @Parameterized.Parameters(name = "{index}: parse(\"--from {0}\")")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"frompattern", "toargument", "./src", new String[]{"--from", "frompattern", "--to", "toargument", "--dir", "./src"}},
                {"otherfrompattern", "othertoargument", ".", new String[]{"--to", "othertoargument", "--from", "otherfrompattern"}},
        });
    }

    @Parameterized.Parameter
    public String expectedFromPattern;

    @Parameterized.Parameter(1)
    public String expectedToArgument;

    @Parameterized.Parameter(2)
    public String expectedStartDir;

    @Parameterized.Parameter(3)
    public String[] args;

    @Test
    public void parse_GivenArguments_SetsExpectedConfigProperties() throws WrongUsageException {
        ArgumentParser parser = new ArgumentParserImpl();
        parser.parse(args);

        Configuration config = Configuration.getConfiguration();
        assertEquals(expectedFromPattern, config.getFrom());
        assertEquals(expectedToArgument, config.getTo());
        assertEquals(expectedStartDir, config.getStartDir());
    }
}