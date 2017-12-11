package com.github.wonderbird.RenameProject;

import com.github.wonderbird.RenameProject.Logic.RenameProjectManager;
import com.github.wonderbird.RenameProject.Models.Notification;
import com.github.wonderbird.RenameProject.ViewModels.RenameProjectViewModel;
import com.github.wonderbird.RenameProject.Views.RenameProjectView;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UiManagerImpl extends Application implements UiManager {
    private RenameProjectManager renameProjectManager;

    private Logger logger = LoggerFactory.getLogger(UiManagerImpl.class);

    @Override
    public void runUi(final String[] aArgs) {
        launch(aArgs);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewTuple<RenameProjectView, RenameProjectViewModel> viewTuple = FluentViewLoader.fxmlView(RenameProjectView.class).load();
        Parent root = viewTuple.getView();
        primaryStage.setTitle("Rename Project");
        primaryStage.setScene(new Scene(root));

        NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
        notificationCenter.subscribe(Notification.QUIT.toString(), (key, payload) -> primaryStage.close());
        notificationCenter.subscribe(Notification.RENAME.toString(), (key, payload) -> {
            try {
                renameProjectManager.renameProject();
            } catch (IOException aException) {
                logger.error(aException.toString());
            } finally {
                primaryStage.close();
            }
        });

        primaryStage.show();
    }
}
