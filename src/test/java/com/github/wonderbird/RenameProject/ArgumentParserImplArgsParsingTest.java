package com.github.wonderbird.RenameProject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sun.security.krb5.Config;

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
                {"frompattern", "toargument", new String[]{"--from", "frompattern", "--to", "toargument"}},
                {"otherfrompattern", "othertoargument", new String[]{"--to", "othertoargument", "--from", "otherfrompattern"}},
        });
    }

    @Parameterized.Parameter
    public String expectedFromPattern;

    @Parameterized.Parameter(1)
    public String expectedToArgument;

    @Parameterized.Parameter(2)
    public String[] args;

    @Test
    public void parse_GivenArguments_SetsExpectedConfigProperties() throws WrongUsageException {
        ArgumentParser parser = new ArgumentParserImpl();
        parser.parse(args);

        Configuration config = Configuration.getConfiguration();
        assertEquals(expectedFromPattern, config.getFrom());
        assertEquals(expectedToArgument, config.getTo());
    }
}