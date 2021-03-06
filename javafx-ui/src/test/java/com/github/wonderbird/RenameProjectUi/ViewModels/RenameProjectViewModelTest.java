package com.github.wonderbird.RenameProjectUi.ViewModels;

import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.Models.Notification;
import com.github.wonderbird.RenameProject.Models.RenameFromToPair;
import de.saxsys.mvvmfx.MvvmFX;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class RenameProjectViewModelTest
{
   @Before
   public void before()
   {
      Configuration.getConfiguration().reset();
   }

   @Test
   public void browseCommand__EmitsBrowseStartDirNotification()
   {
      final String expectedStartDir = "expectedStartDir";

      final AtomicBoolean browseStartDirNotificationFired = new AtomicBoolean(false);
      final AtomicReference<String> startDir = new AtomicReference<>("");

      final RenameProjectViewModel viewModel = new RenameProjectViewModel();
      viewModel.setStartDir(expectedStartDir);

      viewModel.subscribe(Notification.BROWSESTARTDIR.toString(), (key, payload) -> {
         assertEquals("Too many parameters for BROWSESTARTDIR notification", 1, payload.length);

         browseStartDirNotificationFired.set(true);
         startDir.set((String)payload[0]);
      });

      viewModel.getBrowseCommand().execute();

      assertTrue("BROWSESTARTDIR notification should be emitted", browseStartDirNotificationFired.get());
      assertEquals("Start directory has not been passed to BROWSESTARTDIR notification", expectedStartDir,
                   startDir.get());
   }

   @Test
   public void renameCommand_TwoFromToPairsAreIdentical_StripsOneOfTheIdenticalPairsFromConfiguration()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      final String expectedFrom = "CamelCaseFrom";
      final String expectedTo = "CamelCaseTo";

      viewModel.setFrom(expectedFrom);
      viewModel.setTo(expectedTo);
      viewModel.setEnableOriginalReplacement(true);
      viewModel.setEnableCamelCaseReplacement(true);

      viewModel.setEnableFirstLowerThenCamelCaseReplacement(false);
      viewModel.setEnableLowerCaseReplacement(false);
      viewModel.setEnableUpperCaseReplacement(false);
      viewModel.setEnableSpaceSeparatedReplacement(false);
      viewModel.setEnableDashSeparatedReplacement(false);

      final AtomicBoolean renameNotificationFired = new AtomicBoolean(false);
      MvvmFX.getNotificationCenter()
            .subscribe(Notification.RENAME.toString(), (key, payload) -> renameNotificationFired.set(true));

      viewModel.getRenameCommand().execute();

      assertTrue("RENAME notification should be emitted", renameNotificationFired.get());
      assertEquals("Invalid number of from/to pairs in emitted configuration", 1,
                   Configuration.getConfiguration().getFromToPairs().size());

      final RenameFromToPair pair = Configuration.getConfiguration().getFromToPairs().get(0);
      assertEquals("'from' value is wrong in emitted configuration", expectedFrom, pair.getFrom());
      assertEquals("'to' value is wrong in emitted configuration", expectedTo, pair.getTo());
   }

   @Test
   public void renameCommand_NoCheckBoxTicked_EmitsConfigurationWithOneFromToPair()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setEnableOriginalReplacement(false);
      viewModel.setEnableCamelCaseReplacement(false);
      viewModel.setEnableFirstLowerThenCamelCaseReplacement(false);
      viewModel.setEnableLowerCaseReplacement(false);
      viewModel.setEnableUpperCaseReplacement(false);
      viewModel.setEnableSpaceSeparatedReplacement(false);
      viewModel.setEnableDashSeparatedReplacement(false);

      final AtomicBoolean renameNotificationFired = new AtomicBoolean(false);
      MvvmFX.getNotificationCenter()
            .subscribe(Notification.RENAME.toString(), (key, payload) -> renameNotificationFired.set(true));

      viewModel.getRenameCommand().execute();

      assertTrue("RENAME notification should be emitted", renameNotificationFired.get());
      assertEquals("Invalid number of from/to pairs in emitted configuration", 0,
                   Configuration.getConfiguration().getFromToPairs().size());
   }

   @Test
   public void renameCommand_AllCheckBoxesTicked_EmitsConfigurationWithCorrectFromToPairs()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setFrom("Ensure all From-Values are unique");
      viewModel.setEnableOriginalReplacement(true);
      viewModel.setEnableCamelCaseReplacement(true);
      viewModel.setEnableFirstLowerThenCamelCaseReplacement(true);
      viewModel.setEnableLowerCaseReplacement(true);
      viewModel.setEnableUpperCaseReplacement(true);
      viewModel.setEnableSpaceSeparatedReplacement(true);
      viewModel.setEnableDashSeparatedReplacement(true);

      final AtomicBoolean renameNotificationFired = new AtomicBoolean(false);
      MvvmFX.getNotificationCenter()
            .subscribe(Notification.RENAME.toString(), (key, payload) -> renameNotificationFired.set(true));

      viewModel.getRenameCommand().execute();

      final List<RenameFromToPair> fromToPairs = Configuration.getConfiguration().getFromToPairs();
      assertTrue("RENAME notification should be emitted", renameNotificationFired.get());
      assertEquals("Invalid number of from/to pairs in emitted configuration", 7, fromToPairs.size());
      for(final RenameFromToPair pair : fromToPairs)
      {
         assertNotNull("'from' value is null in emitted configuration", pair.getFrom());
         assertFalse("'from' value is empty in emitted configuration", pair.getFrom().isEmpty());
         assertNotNull("'to' value is null in emitted configuration", pair.getTo());
         assertFalse("'to' value is empty in emitted configuration", pair.getTo().isEmpty());
      }
   }

   @Test
   public void setFrom__UpdatesOriginalFrom()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      final String expectedValue = "OrIgInAl  --  VaLuE";
      viewModel.setFrom(expectedValue);

      assertEquals("originalFrom should be updated correctly", expectedValue, viewModel.getOriginalFrom());
   }

   @Test
   public void setTo__UpdatesOriginalTo()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      final String expectedValue = "OrIgInAl  --  VaLuE";
      viewModel.setTo(expectedValue);

      assertEquals("originalTo should be updated correctly", expectedValue, viewModel.getOriginalTo());
   }

   @Test
   public void setFrom_NewFromIsCamelCaseTestPattern_UpdatesLowerCaseFrom()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setFrom("CamelCaseTestPattern");

      assertEquals("lowerCaseFrom should be updated correctly", "camelcasetestpattern", viewModel.getLowerCaseFrom());
   }

   @Test
   public void setTo_NewToIsCamelCaseTestPattern_UpdatesLowerCaseTo()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setTo("CamelCaseTestPattern");

      assertEquals("lowerCaseTo should be updated correctly", "camelcasetestpattern", viewModel.getLowerCaseTo());
   }

   @Test
   public void setFrom_NewFromIsCamelCaseTestPattern_UpdatesUpperCaseFrom()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setFrom("CamelCaseTestPattern");

      assertEquals("upperCaseFrom should be updated correctly", "CAMELCASETESTPATTERN", viewModel.getUpperCaseFrom());
   }

   @Test
   public void setTo_NewToIsCamelCaseTestPattern_UpdatesUpperCaseTo()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setTo("CamelCaseTestPattern");

      assertEquals("upperCaseTo should be updated correctly", "CAMELCASETESTPATTERN", viewModel.getUpperCaseTo());
   }

   @Test
   public void setFrom_NewFromIsCamelCaseTestPatternWithDash_UpdatesSpaceSeparatedFrom()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setFrom("CamelCaseTest-Pattern---ExAm");

      assertEquals("spaceSeparatedFrom should be updated correctly", "Camel Case Test Pattern Ex Am",
                   viewModel.getSpaceSeparatedFrom());
   }

   @Test
   public void setTo_NewToIsCamelCaseTestPatternWithDash_UpdatesSpaceSeparatedTo()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setTo("CamelCaseTest-Pattern---ExAm");

      assertEquals("spaceSeparatedTo should be updated correctly", "Camel Case Test Pattern Ex Am",
                   viewModel.getSpaceSeparatedTo());
   }

   @Test
   public void setFrom_NewFromIsCamelCaseTestPatternWithSpaces_UpdatesDashSeparatedFrom()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setFrom("CamelCase   Test PatternExAm");

      assertEquals("dashSeparatedFrom should be updated correctly", "camel-case-test-pattern-ex-am",
                   viewModel.getDashSeparatedFrom());
   }

   @Test
   public void setTo_NewToIsCamelCaseTestPatternWithSpaces_UpdatesDashSeparatedTo()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setTo("CamelCase   Test PatternExAm");

      assertEquals("dashSeparatedTo should be updated correctly", "camel-case-test-pattern-ex-am",
                   viewModel.getDashSeparatedTo());
   }

   @Test
   public void setFrom_NewFromIsSpaceSeparatedTestPattern_UpdatesCamelCaseFrom()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setFrom("Space Separated Test Pattern Ex Am");

      assertEquals("camelCaseFrom should be updated correctly", "SpaceSeparatedTestPatternExAm",
                   viewModel.getCamelCaseFrom());
   }

   @Test
   public void setFrom_NewToIsSpaceAndDashSeparatedTestPattern_UpdatesCamelCaseFrom()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setFrom("Space Separated test-pattern Ex--Am");

      assertEquals("camelCaseTo should be updated correctly", "SpaceSeparatedTestPatternExAm",
                   viewModel.getCamelCaseFrom());
   }

   @Test
   public void setTo_NewToIsSpaceAndDashSeparatedTestPattern_UpdatesCamelCaseTo()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setTo("Space Separated test-pattern Ex--Am");

      assertEquals("camelCaseTo should be updated correctly", "SpaceSeparatedTestPatternExAm",
                   viewModel.getCamelCaseTo());
   }

   @Test
   public void setFrom_NewToIsSpaceAndDashSeparatedTestPattern_UpdatesFirstLowerThenCamelCaseFrom()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setFrom("Space Separated test-pattern Ex--Am");

      assertEquals("firstLowerThenCamelCaseFrom should be updated correctly", "spaceSeparatedTestPatternExAm",
                   viewModel.getFirstLowerThenCamelCaseFrom());
   }

   @Test
   public void setTo_NewToIsSpaceAndDashSeparatedTestPattern_UpdatesFirstLowerThenCamelCaseTo()
   {
      final RenameProjectViewModel viewModel = new RenameProjectViewModel();

      viewModel.setTo("Space Separated test-pattern Ex--Am");

      assertEquals("firstLowerThenCamelCaseTo should be updated correctly", "spaceSeparatedTestPatternExAm",
                   viewModel.getFirstLowerThenCamelCaseTo());
   }
}
