package com.github.wonderbird.RenameProjectUi.ViewModels;

import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.Models.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class RenameProjectViewModel implements ViewModel {
    private NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();

    private StringProperty from = new SimpleStringProperty("OriginalName");

    private StringProperty originalFrom = new SimpleStringProperty("OriginalName");

    private StringProperty camelCaseFrom = new SimpleStringProperty("OriginalName");

    private StringProperty firstLowerThenCamelCaseFrom = new SimpleStringProperty("originalName");

    private StringProperty lowerCaseFrom = new SimpleStringProperty("originalname");

    private StringProperty upperCaseFrom = new SimpleStringProperty("ORIGINALNAME");

    private StringProperty spaceSeparatedFrom = new SimpleStringProperty("Original Name");

    private StringProperty dashSeparatedFrom = new SimpleStringProperty("original-name");

    private StringProperty to = new SimpleStringProperty("TargetName");

    private StringProperty originalTo = new SimpleStringProperty("TargetName");

    private StringProperty camelCaseTo = new SimpleStringProperty("TargetName");

    private StringProperty firstLowerThenCamelCaseTo = new SimpleStringProperty("targetName");

    private StringProperty lowerCaseTo = new SimpleStringProperty("targetname");

    private StringProperty upperCaseTo = new SimpleStringProperty("TARGETNAME");

    private StringProperty spaceSeparatedTo = new SimpleStringProperty("Target Name");

    private StringProperty dashSeparatedTo = new SimpleStringProperty("target-name");

    private BooleanProperty enableOriginalReplacement = new SimpleBooleanProperty(true);

    private BooleanProperty enableCamelCaseReplacement = new SimpleBooleanProperty(true);

    private BooleanProperty enableFirstLowerThenCamelCaseReplacement = new SimpleBooleanProperty(true);

    private BooleanProperty enableLowerCaseReplacement = new SimpleBooleanProperty(true);

    private BooleanProperty enableUpperCaseReplacement = new SimpleBooleanProperty(true);

    private BooleanProperty enableSpaceSeparatedReplacement = new SimpleBooleanProperty(true);

    private BooleanProperty enableDashSeparatedReplacement = new SimpleBooleanProperty(true);

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
        from.addListener(property -> copyProperty(property, this::setOriginalFrom));
        from.addListener(property -> updateCamelCaseProperty(property, this::setCamelCaseFrom));
        from.addListener(property -> updateFirstLowerThenCamelCaseProperty(property, this::setFirstLowerThenCamelCaseFrom));
        from.addListener(property -> updateLowerCaseProperty(property, this::setLowerCaseFrom));
        from.addListener(property -> updateUpperCaseProperty(property, this::setUpperCaseFrom));
        from.addListener(property -> updateSpaceSeparatedProperty(property, this::setSpaceSeparatedFrom));
        from.addListener(property -> updateDashSeparatedProperty(property, this::setDashSeparatedFrom));

        to.addListener(property -> copyProperty(property, this::setOriginalTo));
        to.addListener(property -> updateCamelCaseProperty(property, this::setCamelCaseTo));
        to.addListener(property -> updateFirstLowerThenCamelCaseProperty(property, this::setFirstLowerThenCamelCaseTo));
        to.addListener(property -> updateLowerCaseProperty(property, this::setLowerCaseTo));
        to.addListener(property -> updateUpperCaseProperty(property, this::setUpperCaseTo));
        to.addListener(property -> updateSpaceSeparatedProperty(property, this::setSpaceSeparatedTo));
        to.addListener(property -> updateDashSeparatedProperty(property, this::setDashSeparatedTo));
    }

    private void copyProperty(Observable aObservable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) aObservable;
        String value = property.get();
        aSetterMethod.accept(value);
    }

    private void updateCamelCaseProperty(Observable aObservable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) aObservable;
        String value = property.get();

        String camelCaseValue = toCamelCase(value);

        aSetterMethod.accept(camelCaseValue);
    }

    private String toCamelCase(String value) {
        List<String> words = Arrays.asList(value.split("[ -]"));
        List<String> capitalizedWords = words.stream().filter(word -> !word.isEmpty()).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.toList());
        return StringUtils.join(capitalizedWords, "");
    }

    private void updateFirstLowerThenCamelCaseProperty(Observable aObservable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) aObservable;
        String value = property.get();

        String camelCaseValue = toCamelCase(value);

        if (camelCaseValue.length() > 0) {
            String firstLowerThenCamelCaseValue = camelCaseValue.substring(0, 1).toLowerCase() + camelCaseValue.substring(1);
            aSetterMethod.accept(firstLowerThenCamelCaseValue);
        }
    }

    private void updateSpaceSeparatedProperty(Observable aObservable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) aObservable;
        String value = property.get();

        String dashesReplacedBySpaceValue = value.replace("-", " ");
        String multiSpaceReplacedBySingleSpaceValue = dashesReplacedBySpaceValue.replaceAll(" +", " ");
        String spaceSeparatedValue = insertCharacterBetweenCamelCaseWords(" ", multiSpaceReplacedBySingleSpaceValue);

        aSetterMethod.accept(spaceSeparatedValue);
    }

    private void updateDashSeparatedProperty(Observable aObservable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) aObservable;
        String value = property.get();

        String spacesReplacedByDashValue = value.replace(" ", "-");
        String dashSeparatedValue = insertCharacterBetweenCamelCaseWords("-", spacesReplacedByDashValue);
        String multiDashReplacedBySingleDashValue = dashSeparatedValue.replaceAll("-+", "-");
        String lowerCaseDashSeparatedValue = multiDashReplacedBySingleDashValue.toLowerCase();

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

    private void updateLowerCaseProperty(Observable aObservable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) aObservable;
        String value = property.get();

        String lowerCaseValue = value.toLowerCase();

        aSetterMethod.accept(lowerCaseValue);
    }

    private void updateUpperCaseProperty(Observable aObservable, Consumer<String> aSetterMethod) {
        StringProperty property = (StringProperty) aObservable;
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
        config.setStartDir(getStartDir());

        if (getEnableOriginalReplacement()) {
            config.addFromToPair(getFrom(), getTo());
        }
        if (getEnableCamelCaseReplacement()) {
            config.addFromToPair(getCamelCaseFrom(), getCamelCaseTo());
        }
        if (getEnableFirstLowerThenCamelCaseReplacement()) {
            config.addFromToPair(getFirstLowerThenCamelCaseFrom(), getFirstLowerThenCamelCaseTo());
        }
        if (getEnableLowerCaseReplacement()) {
            config.addFromToPair(getLowerCaseFrom(), getLowerCaseTo());
        }
        if (getEnableUpperCaseReplacement()) {
            config.addFromToPair(getUpperCaseFrom(), getUpperCaseTo());
        }
        if (getEnableDashSeparatedReplacement()) {
            config.addFromToPair(getDashSeparatedFrom(), getDashSeparatedTo());
        }
        if (getEnableSpaceSeparatedReplacement()) {
            config.addFromToPair(getSpaceSeparatedFrom(), getSpaceSeparatedTo());
        }

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

    public StringProperty originalFromProperty() {
        return originalFrom;
    }

    public String getOriginalFrom() {
        return originalFrom.get();
    }

    public void setOriginalFrom(String aValue) {
        originalFrom.set(aValue);
    }

    public StringProperty camelCaseFromProperty() {
        return camelCaseFrom;
    }

    public String getCamelCaseFrom() {
        return camelCaseFrom.get();
    }

    private void setCamelCaseFrom(String aValue) {
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

    public StringProperty originalToProperty() {
        return originalTo;
    }

    public String getOriginalTo() {
        return originalTo.get();
    }

    public void setOriginalTo(String aValue) {
        originalTo.set(aValue);
    }

    public StringProperty camelCaseToProperty() {
        return camelCaseTo;
    }

    public String getCamelCaseTo() {
        return camelCaseTo.get();
    }

    private void setCamelCaseTo(String aValue) {
        camelCaseTo.set(aValue);
    }

    public StringProperty firstLowerThenCamelCaseFromProperty() {
        return firstLowerThenCamelCaseFrom;
    }

    public String getFirstLowerThenCamelCaseFrom() {
        return firstLowerThenCamelCaseFrom.get();
    }

    public void setFirstLowerThenCamelCaseFrom(String aValue) {
        firstLowerThenCamelCaseFrom.set(aValue);
    }

    public StringProperty firstLowerThenCamelCaseToProperty() {
        return firstLowerThenCamelCaseTo;
    }

    public String getFirstLowerThenCamelCaseTo() {
        return firstLowerThenCamelCaseTo.get();
    }

    public void setFirstLowerThenCamelCaseTo(String aValue) {
        this.firstLowerThenCamelCaseTo.set(aValue);
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

    public BooleanProperty enableOriginalReplacementProperty() {
        return enableOriginalReplacement;
    }

    boolean getEnableOriginalReplacement() {
        return enableOriginalReplacement.get();
    }

    void setEnableOriginalReplacement(boolean aValue) {
        enableOriginalReplacement.set(aValue);
    }

    public BooleanProperty enableCamelCaseReplacementProperty() {
        return enableCamelCaseReplacement;
    }

    boolean getEnableCamelCaseReplacement() {
        return enableCamelCaseReplacement.get();
    }

    void setEnableCamelCaseReplacement(boolean aValue) {
        enableCamelCaseReplacement.set(aValue);
    }

    public BooleanProperty enableFirstLowerThenCamelCaseReplacementProperty() {
        return enableFirstLowerThenCamelCaseReplacement;
    }

    boolean getEnableFirstLowerThenCamelCaseReplacement() {
        return enableFirstLowerThenCamelCaseReplacement.get();
    }

    void setEnableFirstLowerThenCamelCaseReplacement(boolean aValue) {
        enableFirstLowerThenCamelCaseReplacement.set(aValue);
    }

    public BooleanProperty enableLowerCaseReplacementProperty() {
        return enableLowerCaseReplacement;
    }

    boolean getEnableLowerCaseReplacement() {
        return enableLowerCaseReplacement.get();
    }

    public void setEnableLowerCaseReplacement(boolean aValue) {
        enableLowerCaseReplacement.set(aValue);
    }

    public BooleanProperty enableUpperCaseReplacementProperty() {
        return enableUpperCaseReplacement;
    }

    boolean getEnableUpperCaseReplacement() {
        return enableUpperCaseReplacement.get();
    }

    void setEnableUpperCaseReplacement(boolean aValue) {
        enableUpperCaseReplacement.set(aValue);
    }

    public BooleanProperty enableSpaceSeparatedReplacementProperty() {
        return enableSpaceSeparatedReplacement;
    }

    boolean getEnableSpaceSeparatedReplacement() {
        return enableSpaceSeparatedReplacement.get();
    }

    public void setEnableSpaceSeparatedReplacement(boolean aValue) {
        enableSpaceSeparatedReplacement.set(aValue);
    }

    public BooleanProperty enableDashSeparatedReplacementProperty() {
        return enableDashSeparatedReplacement;
    }

    boolean getEnableDashSeparatedReplacement() {
        return enableDashSeparatedReplacement.get();
    }

    void setEnableDashSeparatedReplacement(boolean aValue) {
        enableDashSeparatedReplacement.set(aValue);
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
