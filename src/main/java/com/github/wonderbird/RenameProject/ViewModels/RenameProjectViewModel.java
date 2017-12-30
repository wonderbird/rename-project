package com.github.wonderbird.RenameProject.ViewModels;

import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.Models.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RenameProjectViewModel implements ViewModel {
    private NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();

    private StringProperty from = new SimpleStringProperty("OriginalName");

    private StringProperty camelCaseFrom = new SimpleStringProperty("OriginalName");

    private StringProperty lowerCaseFrom = new SimpleStringProperty("originalname");

    private StringProperty upperCaseFrom = new SimpleStringProperty("ORIGINALNAME");

    private StringProperty spaceSeparatedFrom = new SimpleStringProperty("Original Name");

    private StringProperty dashSeparatedFrom = new SimpleStringProperty("original-name");

    private StringProperty to = new SimpleStringProperty("TargetName");

    private StringProperty camelCaseTo = new SimpleStringProperty("TargetName");

    private StringProperty lowerCaseTo = new SimpleStringProperty("targetname");

    private StringProperty upperCaseTo = new SimpleStringProperty("TARGETNAME");

    private StringProperty spaceSeparatedTo = new SimpleStringProperty("Target Name");

    private StringProperty dashSeparatedTo = new SimpleStringProperty("target-name");

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

    public RenameProjectViewModel() {
        from.addListener(property -> updateLowerCaseProperty(property, this::setLowerCaseFrom));
        from.addListener(property -> updateUpperCaseProperty(property, this::setUpperCaseFrom));
        from.addListener(property -> updateSpaceSeparatedProperty(property, this::setSpaceSeparatedFrom));
        from.addListener(property -> updateDashSeparatedProperty(property, this::setDashSeparatedFrom));

        to.addListener(property -> updateLowerCaseProperty(property, this::setLowerCaseTo));
        to.addListener(property -> updateUpperCaseProperty(property, this::setUpperCaseTo));
        to.addListener(property -> updateSpaceSeparatedProperty(property, this::setSpaceSeparatedTo));
        to.addListener(property -> updateDashSeparatedProperty(property, this::setDashSeparatedTo));
    }

    private void updateSpaceSeparatedProperty(Observable observable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) observable;
        String value = property.get();

        String spaceSeparatedValue = insertCharacterBetweenCamelCaseWords(" ", value);

        aSetterMethod.accept(spaceSeparatedValue);
    }

    private void updateDashSeparatedProperty(Observable observable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) observable;
        String value = property.get();

        String dashSeparatedValue = insertCharacterBetweenCamelCaseWords("-", value);
        String lowerCaseDashSeparatedValue = dashSeparatedValue.toLowerCase();

        aSetterMethod.accept(lowerCaseDashSeparatedValue);
    }

    private String insertCharacterBetweenCamelCaseWords(final String aSeparator, final String aText) {
        Pattern pattern = Pattern.compile("[a-z][A-Z]");
        Matcher matcher = pattern.matcher(aText);

        String result = aText;

        int numberOfInsertedCharacters = 0;
        final int separatorLength = aSeparator.length();

        while (matcher.find()) {
            int start = matcher.start();
            int splitIndex = start + 1 + numberOfInsertedCharacters;

            String left = result.substring(0, splitIndex);
            String right = result.substring(splitIndex);

            result = left + aSeparator + right;

            numberOfInsertedCharacters += separatorLength;
        }

        return result;
    }

    private void updateLowerCaseProperty(Observable observable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) observable;
        String value = property.get();

        String lowerCaseValue = value.toLowerCase();

        aSetterMethod.accept(lowerCaseValue);
    }

    private void updateUpperCaseProperty(Observable observable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) observable;
        String value = property.get();

        String upperCaseValue = value.toUpperCase();

        aSetterMethod.accept(upperCaseValue);
    }

    private void emitBrowseStartDirNotification()
    {
        publish(Notification.BROWSESTARTDIR.toString(), getStartDir());
    }

    private void emitQuitNotification() {
        notificationCenter.publish(Notification.QUIT.toString());
    }

    private void emitConfigurationForRenamingProject() {
        Configuration config = Configuration.getConfiguration();
        config.setFrom(getTo());
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

    public void setCamelCaseFrom(String aValue) {
        camelCaseFrom.set(aValue);
    }

    public StringProperty lowerCaseFromProperty() {
        return lowerCaseFrom;
    }

    String getLowerCaseFrom() {
        return lowerCaseFrom.get();
    }

    private void setLowerCaseFrom(String aValue) {
        lowerCaseFrom.set(aValue);
    }

    public StringProperty upperCaseFromProperty() {
        return upperCaseFrom;
    }

    String getUpperCaseFrom() {
        return upperCaseFrom.get();
    }

    private void setUpperCaseFrom(String aValue) {
        upperCaseFrom.set(aValue);
    }

    public StringProperty spaceSeparatedFromProperty() {
        return spaceSeparatedFrom;
    }

    String getSpaceSeparatedFrom() {
        return spaceSeparatedFrom.get();
    }

    private void setSpaceSeparatedFrom(String aValue) {
        spaceSeparatedFrom.set(aValue);
    }

    public StringProperty dashSeparatedFromProperty() {
        return dashSeparatedFrom;
    }

    String getDashSeparatedFrom() {
        return dashSeparatedFrom.get();
    }

    private void setDashSeparatedFrom(String aValue) {
        dashSeparatedFrom.set(aValue);
    }

    public StringProperty toProperty() {
        return to;
    }

    public String getTo() {
        return to.get();
    }

    public void setTo(String aValue) {
        to.set(aValue);
    }

    public StringProperty camelCaseToProperty() {
        return camelCaseTo;
    }

    public String getCamelCaseTo() {
        return camelCaseTo.get();
    }

    public void setCamelCaseTo(String aValue) {
        camelCaseTo.set(aValue);
    }

    public StringProperty lowerCaseToProperty() {
        return lowerCaseTo;
    }

    String getLowerCaseTo() {
        return lowerCaseTo.get();
    }

    private void setLowerCaseTo(String aValue) {
        lowerCaseTo.set(aValue);
    }

    public StringProperty upperCaseToProperty() {
        return upperCaseTo;
    }

    String getUpperCaseTo() {
        return upperCaseTo.get();
    }

    private void setUpperCaseTo(String aValue) {
        upperCaseTo.set(aValue);
    }

    public StringProperty spaceSeparatedToProperty() {
        return spaceSeparatedTo;
    }

    String getSpaceSeparatedTo() {
        return spaceSeparatedTo.get();
    }

    private void setSpaceSeparatedTo(String aValue) {
        spaceSeparatedTo.set(aValue);
    }

    public StringProperty dashSeparatedToProperty() {
        return dashSeparatedTo;
    }

    String getDashSeparatedTo() {
        return dashSeparatedTo.get();
    }

    private void setDashSeparatedTo(String aValue) {
        dashSeparatedTo.set(aValue);
    }

    public StringProperty startDirProperty() {
        return startDir;
    }

    private String getStartDir() {
        return startDir.get();
    }

    public void setStartDir(String aStartDir) {
        startDir.set(aStartDir);
    }
}
