package com.github.wonderbird.RenameProject;

import com.github.wonderbird.RenameProject.Logic.ArgumentParser;
import com.github.wonderbird.RenameProject.Logic.RenameProjectManager;
import com.github.wonderbird.RenameProject.Logic.WrongUsageException;
import com.github.wonderbird.RenameProject.Models.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MainTest {
    private String[] args;

    private ArgumentParser parser;

    private RenameProjectManager renameProjectManager;

    private UiManager uiManager;

    private final String exceptionMessage = "Exception thrown by unit test, expected to show up on console during test";

    @Before
    public void before() {
        final String fromPattern = "Main";
        final String toArgument = "Main";
        args = new String[]{"--from", fromPattern, "--to", toArgument};

        parser = mock(ArgumentParser.class);
        Main.setArgumentParser(parser);

        renameProjectManager = mock(RenameProjectManager.class);
        Main.setRenameProjectManager(renameProjectManager);

        uiManager = mock(UiManager.class);
        Main.setUiManager(uiManager);
    }

    @Test
    public void main_ArgumentsGiven_InvokesArgumentParserWithAllArguments() throws WrongUsageException {
        Main.main(args);

        verify(parser).parse(args);
    }

    @Test
    public void main_ArgumentParserThrowsUsageException_ShowsUi() throws WrongUsageException {
        parser = mock(ArgumentParser.class);
        doThrow(new WrongUsageException("USAGE: renameProject ... (" + exceptionMessage + ")")).when(parser).parse(any(String[].class));
        Main.setArgumentParser(parser);

        Main.main(args);

        verify(uiManager).runUi(args);
    }

    @Test
    public void main_RenameProjectManagerThrowsException_HandlesException() throws IOException {
        renameProjectManager = mock(RenameProjectManager.class);
        doThrow(new IOException(exceptionMessage)).when(renameProjectManager).renameProject();
        Main.setRenameProjectManager(renameProjectManager);
        
        Main.main(args);
    }
}
