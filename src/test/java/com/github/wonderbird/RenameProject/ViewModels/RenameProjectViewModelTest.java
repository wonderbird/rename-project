package com.github.wonderbird.RenameProject.ViewModels;

import com.github.wonderbird.RenameProject.Models.Notification;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RenameProjectViewModelTest
{
   @Test
   public void browseCommand__ShouldEmitBrowseStartDirNotification()
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
      assertEquals("Start directory has not been passed to BROWSESTARTDIR notification", expectedStartDir, startDir.get());
   }
}
