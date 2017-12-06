package com.github.wonderbird.RenameProject.ViewModels;

import com.github.wonderbird.RenameProject.Configuration;
import com.github.wonderbird.RenameProject.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class RenameProjectViewModel implements ViewModel {
    private NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();

    public StringProperty from = new SimpleStringProperty("OriginalName");

    public StringProperty to = new SimpleStringProperty("TargetName");

    private Command cancelCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() throws Exception {
            emitQuitNotification();
        }
    });

    private Command renameCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() throws Exception {
            emitConfigurationForRenamingProject();
        }
    });

    private void emitQuitNotification() {
        notificationCenter.publish(Notification.QUIT.toString());
    }

    private void emitConfigurationForRenamingProject() {
        Configuration config = Configuration.getConfiguration();
        config.setFrom(getFrom());
        config.setTo(getTo());

        notificationCenter.publish(Notification.RENAME.toString());
    }

    public Command getCancelCommand() {
        return cancelCommand;
    }

    public Command getRenameCommand() {
        return renameCommand;
    }

    public StringProperty fromProperty() {
        return from;
    }

    public String getFrom() {
        return from.get();
    }

    public void setFrom(String aFrom) {
        from.set(aFrom);
    }

    public StringProperty toProperty() {
        return to;
    }

    public String getTo() {
        return to.get();
    }

    public void setTo(String aTo) {
        to.set(aTo);
    }
}
