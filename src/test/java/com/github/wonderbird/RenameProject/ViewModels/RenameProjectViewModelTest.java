package com.github.wonderbird.RenameProject.ViewModels;

import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.Models.Notification;
import com.github.wonderbird.RenameProject.Models.RenameFromToPair;
import de.saxsys.mvvmfx.MvvmFX;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class RenameProjectViewModelTest {
    @Before
    public void before() {
        Configuration.getConfiguration().reset();
    }

    @Test
    public void browseCommand__EmitsBrowseStartDirNotification() {
        final String expectedStartDir = "expectedStartDir";

        final AtomicBoolean browseStartDirNotificationFired = new AtomicBoolean(false);
        final AtomicReference<String> startDir = new AtomicReference<>("");

        final RenameProjectViewModel viewModel = new RenameProjectViewModel();
        viewModel.setStartDir(expectedStartDir);

        viewModel.subscribe(Notification.BROWSESTARTDIR.toString(), (key, payload) -> {
            assertEquals("Too many parameters for BROWSESTARTDIR notification", 1, payload.length);

            browseStartDirNotificationFired.set(true);
            startDir.set((String) payload[0]);
        });

        viewModel.getBrowseCommand().execute();

        assertTrue("BROWSESTARTDIR notification should be emitted", browseStartDirNotificationFired.get());
        assertEquals("Start directory has not been passed to BROWSESTARTDIR notification", expectedStartDir, startDir.get());
    }

    @Test
    public void renameCommand_NoCheckBoxTicked_EmitsConfigurationWithOneFromToPair() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setEnableCamelCaseReplacement(false);
        viewModel.setEnableLowerCaseReplacement(false);
        viewModel.setEnableUpperCaseReplacement(false);
        viewModel.setEnableSpaceSeparatedReplacement(false);
        viewModel.setEnableDashSeparatedReplacement(false);

        final AtomicBoolean renameNotificationFired = new AtomicBoolean(false);
        MvvmFX.getNotificationCenter().subscribe(Notification.RENAME.toString(), (key, payload) -> renameNotificationFired.set(true));

        viewModel.getRenameCommand().execute();

        assertTrue("RENAME notification should be emitted", renameNotificationFired.get());
        assertEquals("Invalid number of from/to pairs in emitted configuration", 1, Configuration.getConfiguration().getFromToPairs().size());
    }

    @Test
    public void renameCommand_AllCheckBoxesTicked_EmitsConfigurationWithCorrectFromToPairs() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setFrom("UnitTestFrom");
        viewModel.setEnableCamelCaseReplacement(true);
        viewModel.setEnableLowerCaseReplacement(true);
        viewModel.setEnableUpperCaseReplacement(true);
        viewModel.setEnableSpaceSeparatedReplacement(true);
        viewModel.setEnableDashSeparatedReplacement(true);

        final AtomicBoolean renameNotificationFired = new AtomicBoolean(false);
        MvvmFX.getNotificationCenter().subscribe(Notification.RENAME.toString(), (key, payload) -> renameNotificationFired.set(true));

        viewModel.getRenameCommand().execute();

        List<RenameFromToPair> fromToPairs = Configuration.getConfiguration().getFromToPairs();
        assertTrue("RENAME notification should be emitted", renameNotificationFired.get());
        assertEquals("Invalid number of from/to pairs in emitted configuration", 6, fromToPairs.size());
        for (RenameFromToPair pair : fromToPairs) {
            assertNotNull("'from' value is null in emitted configuration", pair.getFrom());
            assertFalse("'from' value is empty in emitted configuration", pair.getFrom().isEmpty());
            assertNotNull("'to' value is null in emitted configuration", pair.getTo());
            assertFalse("'to' value is empty in emitted configuration", pair.getTo().isEmpty());
        }
    }

    @Test
    public void setFrom_NewFromIsCamelCaseTestPattern_UpdatesLowerCaseFrom() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setFrom("CamelCaseTestPattern");

        assertEquals("lowerCaseFrom should be updated correctly", "camelcasetestpattern", viewModel.getLowerCaseFrom());
    }

    @Test
    public void setTo_NewToIsCamelCaseTestPattern_UpdatesLowerCaseTo() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setTo("CamelCaseTestPattern");

        assertEquals("lowerCaseTo should be updated correctly", "camelcasetestpattern", viewModel.getLowerCaseTo());
    }

    @Test
    public void setFrom_NewFromIsCamelCaseTestPattern_UpdatesUpperCaseFrom() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setFrom("CamelCaseTestPattern");

        assertEquals("upperCaseFrom should be updated correctly", "CAMELCASETESTPATTERN", viewModel.getUpperCaseFrom());
    }

    @Test
    public void setTo_NewToIsCamelCaseTestPattern_UpdatesUpperCaseTo() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setTo("CamelCaseTestPattern");

        assertEquals("upperCaseTo should be updated correctly", "CAMELCASETESTPATTERN", viewModel.getUpperCaseTo());
    }

    @Test
    public void setFrom_NewFromIsCamelCaseTestPattern_UpdatesSpaceSeparatedFrom() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setFrom("CamelCaseTestPatternExAm");

        assertEquals("spaceSeparatedFrom should be updated correctly", "Camel Case Test Pattern Ex Am", viewModel.getSpaceSeparatedFrom());
    }

    @Test
    public void setTo_NewToIsCamelCaseTestPattern_UpdatesSpaceSeparatedTo() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setTo("CamelCaseTestPatternExAm");

        assertEquals("spaceSeparatedTo should be updated correctly", "Camel Case Test Pattern Ex Am", viewModel.getSpaceSeparatedTo());
    }

    @Test
    public void setFrom_NewFromIsCamelCaseTestPattern_UpdatesDashSeparatedFrom() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setFrom("CamelCaseTestPatternExAm");

        assertEquals("dashSeparatedFrom should be updated correctly", "camel-case-test-pattern-ex-am", viewModel.getDashSeparatedFrom());
    }

    @Test
    public void setTo_NewToIsCamelCaseTestPattern_UpdatesDashSeparatedTo() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setTo("CamelCaseTestPatternExAm");

        assertEquals("dashSeparatedTo should be updated correctly", "camel-case-test-pattern-ex-am", viewModel.getDashSeparatedTo());
    }

    @Test
    public void setFrom_NewFromIsSpaceSeparatedTestPattern_UpdatesCamelCaseFrom() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setFrom("Space Separated Test Pattern Ex Am");

        assertEquals("camleCaseFrom should be updated correctly", "SpaceSeparatedTestPatternExAm", viewModel.getCamelCaseFrom());
    }

    @Test
    public void setFrom_NewToIsSpaceAndDashSeparatedTestPattern_UpdatesCamelCaseFrom() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setFrom("Space Separated test-pattern Ex--Am");

        assertEquals("camleCaseTo should be updated correctly", "SpaceSeparatedTestPatternExAm", viewModel.getCamelCaseFrom());
    }

    @Test
    public void setTo_NewToIsSpaceAndDashSeparatedTestPattern_UpdatesCamelCaseTo() {
        RenameProjectViewModel viewModel = new RenameProjectViewModel();

        viewModel.setTo("Space Separated test-pattern Ex--Am");

        assertEquals("camleCaseTo should be updated correctly", "SpaceSeparatedTestPatternExAm", viewModel.getCamelCaseTo());
    }
}
