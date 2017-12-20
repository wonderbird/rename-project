package com.github.wonderbird.RenameProject.ViewModels;

import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.Models.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.swing.*;


public class RenameProjectViewModel implements ViewModel {
    private NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();

    private StringProperty from = new SimpleStringProperty("OriginalName");

    private StringProperty to = new SimpleStringProperty("TargetName");

    private StringProperty camelCaseFrom = new SimpleStringProperty("OriginalName");

    private StringProperty lowerCaseFrom = new SimpleStringProperty("originalname");

    private StringProperty upperCaseFrom = new SimpleStringProperty("ORIGINALNAME");

    private StringProperty spaceSeparatedFrom = new SimpleStringProperty("Original Name");

    private StringProperty dashSeparatedFrom = new SimpleStringProperty("original-name");

    private StringProperty startDir = new SimpleStringProperty(".");

    private Command browseCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            emitBrowseStartDirNotification();
        }
    });

    private Command cancelCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action()
        {
            emitQuitNotification();
        }
    });

    private Command renameCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action()
        {
            emitConfigurationForRenamingProject();
        }
    });

    private void emitBrowseStartDirNotification()
    {
        publish(Notification.BROWSESTARTDIR.toString(), getStartDir());
    }

    private void emitQuitNotification() {
        notificationCenter.publish(Notification.QUIT.toString());
    }

    private void emitConfigurationForRenamingProject() {
        Configuration config = Configuration.getConfiguration();
        config.setFrom(getFrom());
        config.setTo(getTo());
        config.setStartDir(getStartDir());

        notificationCenter.publish(Notification.RENAME.toString());
    }

    public Command getBrowseCommand() {
        return browseCommand;
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

    public StringProperty camelCaseFromProperty() {
        return camelCaseFrom;
    }

    public String getCamelCaseFrom() {
        return camelCaseFrom.get();
    }

    public void setCamelCaseFrom(String aFrom) {
        camelCaseFrom.set(aFrom);
    }

    public StringProperty lowerCaseFromProperty() {
        return lowerCaseFrom;
    }

    public String getLowerCaseFrom() {
        return lowerCaseFrom.get();
    }

    public void setLowerCaseFrom(String aFrom) {
        lowerCaseFrom.set(aFrom);
    }

    public StringProperty upperCaseFromProperty() {
        return upperCaseFrom;
    }

    public String getUpperCaseFrom() {
        return upperCaseFrom.get();
    }

    public void setUpperCaseFrom(String aFrom) {
        upperCaseFrom.set(aFrom);
    }

    public StringProperty spaceSeparatedFromProperty() {
        return spaceSeparatedFrom;
    }

    public String getSpaceSeparatedFrom() {
        return spaceSeparatedFrom.get();
    }

    public void setSpaceSeparatedFrom(String aFrom) {
        spaceSeparatedFrom.set(aFrom);
    }

    public StringProperty dashSeparatedFromProperty() {
        return dashSeparatedFrom;
    }

    public String getDashSeparatedFrom() {
        return dashSeparatedFrom.get();
    }

    public void setDashSeparatedFrom(String aFrom) {
        dashSeparatedFrom.set(aFrom);
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

    public StringProperty startDirProperty() {
        return startDir;
    }

    public String getStartDir() {
        return startDir.get();
    }

    public void setStartDir(String aStartDir) {
        startDir.set(aStartDir);
    }
}
