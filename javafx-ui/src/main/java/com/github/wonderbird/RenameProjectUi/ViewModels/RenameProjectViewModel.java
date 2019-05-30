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

public class RenameProjectViewModel implements ViewModel
{
   private final NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();

   private final StringProperty from = new SimpleStringProperty("OriginalName");

   private final StringProperty originalFrom = new SimpleStringProperty("OriginalName");

   private final StringProperty camelCaseFrom = new SimpleStringProperty("OriginalName");

   private final StringProperty firstLowerThenCamelCaseFrom = new SimpleStringProperty("originalName");

   private final StringProperty lowerCaseFrom = new SimpleStringProperty("originalname");

   private final StringProperty upperCaseFrom = new SimpleStringProperty("ORIGINALNAME");

   private final StringProperty spaceSeparatedFrom = new SimpleStringProperty("Original Name");

   private final StringProperty dashSeparatedFrom = new SimpleStringProperty("original-name");

   private final StringProperty to = new SimpleStringProperty("TargetName");

   private final StringProperty originalTo = new SimpleStringProperty("TargetName");

   private final StringProperty camelCaseTo = new SimpleStringProperty("TargetName");

   private final StringProperty firstLowerThenCamelCaseTo = new SimpleStringProperty("targetName");

   private final StringProperty lowerCaseTo = new SimpleStringProperty("targetname");

   private final StringProperty upperCaseTo = new SimpleStringProperty("TARGETNAME");

   private final StringProperty spaceSeparatedTo = new SimpleStringProperty("Target Name");

   private final StringProperty dashSeparatedTo = new SimpleStringProperty("target-name");

   private final BooleanProperty enableOriginalReplacement = new SimpleBooleanProperty(true);

   private final BooleanProperty enableCamelCaseReplacement = new SimpleBooleanProperty(true);

   private final BooleanProperty enableFirstLowerThenCamelCaseReplacement = new SimpleBooleanProperty(true);

   private final BooleanProperty enableLowerCaseReplacement = new SimpleBooleanProperty(true);

   private final BooleanProperty enableUpperCaseReplacement = new SimpleBooleanProperty(true);

   private final BooleanProperty enableSpaceSeparatedReplacement = new SimpleBooleanProperty(true);

   private final BooleanProperty enableDashSeparatedReplacement = new SimpleBooleanProperty(true);

   private final StringProperty startDir = new SimpleStringProperty(".");

   private final Command browseCommand = new DelegateCommand(() -> new Action()
   {
      @Override
      protected void action()
      {
         emitBrowseStartDirNotification();
      }
   });

   private final Command cancelCommand = new DelegateCommand(() -> new Action()
   {
      @Override
      protected void action()
      {
         emitQuitNotification();
      }
   });

   private final Command renameCommand = new DelegateCommand(() -> new Action()
   {
      @Override
      protected void action()
      {
         emitConfigurationForRenamingProject();
      }
   });

   public RenameProjectViewModel()
   {
      from.addListener(property -> copyProperty(property, this::setOriginalFrom));
      from.addListener(property -> updateCamelCaseProperty(property, this::setCamelCaseFrom));
      from.addListener(
         property -> updateFirstLowerThenCamelCaseProperty(property, this::setFirstLowerThenCamelCaseFrom));
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

   private void copyProperty(final Observable aObservable, final Consumer<String> aSetterMethod)
   {
      final StringProperty property = (StringProperty)aObservable;
      final String value = property.get();
      aSetterMethod.accept(value);
   }

   private void updateCamelCaseProperty(final Observable aObservable, final Consumer<String> aSetterMethod)
   {
      final StringProperty property = (StringProperty)aObservable;
      final String value = property.get();

      final String camelCaseValue = toCamelCase(value);

      aSetterMethod.accept(camelCaseValue);
   }

   private String toCamelCase(final String value)
   {
      final List<String> words = Arrays.asList(value.split("[ -]"));
      final List<String> capitalizedWords = words.stream()
                                                 .filter(word -> !word.isEmpty())
                                                 .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                                                 .collect(Collectors.toList());
      return StringUtils.join(capitalizedWords, "");
   }

   private void updateFirstLowerThenCamelCaseProperty(final Observable aObservable,
                                                      final Consumer<String> aSetterMethod)
   {
      final StringProperty property = (StringProperty)aObservable;
      final String value = property.get();

      final String camelCaseValue = toCamelCase(value);

      if(camelCaseValue.length() > 0)
      {
         final String firstLowerThenCamelCaseValue =
            camelCaseValue.substring(0, 1).toLowerCase() + camelCaseValue.substring(1);
         aSetterMethod.accept(firstLowerThenCamelCaseValue);
      }
   }

   private void updateSpaceSeparatedProperty(final Observable aObservable, final Consumer<String> aSetterMethod)
   {
      final StringProperty property = (StringProperty)aObservable;
      final String value = property.get();

      final String dashesReplacedBySpaceValue = value.replace("-", " ");
      final String multiSpaceReplacedBySingleSpaceValue = dashesReplacedBySpaceValue.replaceAll(" +", " ");
      final String spaceSeparatedValue =
         insertCharacterBetweenCamelCaseWords(" ", multiSpaceReplacedBySingleSpaceValue);

      aSetterMethod.accept(spaceSeparatedValue);
   }

   private void updateDashSeparatedProperty(final Observable aObservable, final Consumer<String> aSetterMethod)
   {
      final StringProperty property = (StringProperty)aObservable;
      final String value = property.get();

      final String spacesReplacedByDashValue = value.replace(" ", "-");
      final String dashSeparatedValue = insertCharacterBetweenCamelCaseWords("-", spacesReplacedByDashValue);
      final String multiDashReplacedBySingleDashValue = dashSeparatedValue.replaceAll("-+", "-");
      final String lowerCaseDashSeparatedValue = multiDashReplacedBySingleDashValue.toLowerCase();

      aSetterMethod.accept(lowerCaseDashSeparatedValue);
   }

   private String insertCharacterBetweenCamelCaseWords(final String aSeparator, final String aText)
   {
      final Pattern pattern = Pattern.compile("[a-z][A-Z]");
      final Matcher matcher = pattern.matcher(aText);

      String result = aText;

      int numberOfInsertedCharacters = 0;
      final int separatorLength = aSeparator.length();

      while(matcher.find())
      {
         final int start = matcher.start();
         final int splitIndex = start + 1 + numberOfInsertedCharacters;

         final String left = result.substring(0, splitIndex);
         final String right = result.substring(splitIndex);

         result = left + aSeparator + right;

         numberOfInsertedCharacters += separatorLength;
      }

      return result;
   }

   private void updateLowerCaseProperty(final Observable aObservable, final Consumer<String> aSetterMethod)
   {
      final StringProperty property = (StringProperty)aObservable;
      final String value = property.get();

      final String lowerCaseValue = value.toLowerCase();

      aSetterMethod.accept(lowerCaseValue);
   }

   private void updateUpperCaseProperty(final Observable aObservable, final Consumer<String> aSetterMethod)
   {
      final StringProperty property = (StringProperty)aObservable;
      final String value = property.get();

      final String upperCaseValue = value.toUpperCase();

      aSetterMethod.accept(upperCaseValue);
   }

   private void emitBrowseStartDirNotification()
   {
      publish(Notification.BROWSESTARTDIR.toString(), getStartDir());
   }

   private void emitQuitNotification()
   {
      notificationCenter.publish(Notification.QUIT.toString());
   }

   private void emitConfigurationForRenamingProject()
   {
      final Configuration config = Configuration.getConfiguration();
      config.setStartDir(getStartDir());

      if(getEnableOriginalReplacement())
      {
         config.addFromToPair(getFrom(), getTo());
      }
      if(getEnableCamelCaseReplacement())
      {
         config.addFromToPair(getCamelCaseFrom(), getCamelCaseTo());
      }
      if(getEnableFirstLowerThenCamelCaseReplacement())
      {
         config.addFromToPair(getFirstLowerThenCamelCaseFrom(), getFirstLowerThenCamelCaseTo());
      }
      if(getEnableLowerCaseReplacement())
      {
         config.addFromToPair(getLowerCaseFrom(), getLowerCaseTo());
      }
      if(getEnableUpperCaseReplacement())
      {
         config.addFromToPair(getUpperCaseFrom(), getUpperCaseTo());
      }
      if(getEnableDashSeparatedReplacement())
      {
         config.addFromToPair(getDashSeparatedFrom(), getDashSeparatedTo());
      }
      if(getEnableSpaceSeparatedReplacement())
      {
         config.addFromToPair(getSpaceSeparatedFrom(), getSpaceSeparatedTo());
      }

      notificationCenter.publish(Notification.RENAME.toString());
   }

   public Command getBrowseCommand()
   {
      return browseCommand;
   }

   public Command getCancelCommand()
   {
      return cancelCommand;
   }

   public Command getRenameCommand()
   {
      return renameCommand;
   }

   public StringProperty fromProperty()
   {
      return from;
   }

   public String getFrom()
   {
      return from.get();
   }

   public void setFrom(final String aFrom)
   {
      from.set(aFrom);
   }

   public StringProperty originalFromProperty()
   {
      return originalFrom;
   }

   public String getOriginalFrom()
   {
      return originalFrom.get();
   }

   public void setOriginalFrom(final String aValue)
   {
      originalFrom.set(aValue);
   }

   public StringProperty camelCaseFromProperty()
   {
      return camelCaseFrom;
   }

   public String getCamelCaseFrom()
   {
      return camelCaseFrom.get();
   }

   private void setCamelCaseFrom(final String aValue)
   {
      camelCaseFrom.set(aValue);
   }

   public StringProperty lowerCaseFromProperty()
   {
      return lowerCaseFrom;
   }

   String getLowerCaseFrom()
   {
      return lowerCaseFrom.get();
   }

   private void setLowerCaseFrom(final String aValue)
   {
      lowerCaseFrom.set(aValue);
   }

   public StringProperty upperCaseFromProperty()
   {
      return upperCaseFrom;
   }

   String getUpperCaseFrom()
   {
      return upperCaseFrom.get();
   }

   private void setUpperCaseFrom(final String aValue)
   {
      upperCaseFrom.set(aValue);
   }

   public StringProperty spaceSeparatedFromProperty()
   {
      return spaceSeparatedFrom;
   }

   String getSpaceSeparatedFrom()
   {
      return spaceSeparatedFrom.get();
   }

   private void setSpaceSeparatedFrom(final String aValue)
   {
      spaceSeparatedFrom.set(aValue);
   }

   public StringProperty dashSeparatedFromProperty()
   {
      return dashSeparatedFrom;
   }

   String getDashSeparatedFrom()
   {
      return dashSeparatedFrom.get();
   }

   private void setDashSeparatedFrom(final String aValue)
   {
      dashSeparatedFrom.set(aValue);
   }

   public StringProperty toProperty()
   {
      return to;
   }

   public String getTo()
   {
      return to.get();
   }

   public void setTo(final String aValue)
   {
      to.set(aValue);
   }

   public StringProperty originalToProperty()
   {
      return originalTo;
   }

   public String getOriginalTo()
   {
      return originalTo.get();
   }

   public void setOriginalTo(final String aValue)
   {
      originalTo.set(aValue);
   }

   public StringProperty camelCaseToProperty()
   {
      return camelCaseTo;
   }

   public String getCamelCaseTo()
   {
      return camelCaseTo.get();
   }

   private void setCamelCaseTo(final String aValue)
   {
      camelCaseTo.set(aValue);
   }

   public StringProperty firstLowerThenCamelCaseFromProperty()
   {
      return firstLowerThenCamelCaseFrom;
   }

   public String getFirstLowerThenCamelCaseFrom()
   {
      return firstLowerThenCamelCaseFrom.get();
   }

   public void setFirstLowerThenCamelCaseFrom(final String aValue)
   {
      firstLowerThenCamelCaseFrom.set(aValue);
   }

   public StringProperty firstLowerThenCamelCaseToProperty()
   {
      return firstLowerThenCamelCaseTo;
   }

   public String getFirstLowerThenCamelCaseTo()
   {
      return firstLowerThenCamelCaseTo.get();
   }

   public void setFirstLowerThenCamelCaseTo(final String aValue)
   {
      firstLowerThenCamelCaseTo.set(aValue);
   }

   public StringProperty lowerCaseToProperty()
   {
      return lowerCaseTo;
   }

   String getLowerCaseTo()
   {
      return lowerCaseTo.get();
   }

   private void setLowerCaseTo(final String aValue)
   {
      lowerCaseTo.set(aValue);
   }

   public StringProperty upperCaseToProperty()
   {
      return upperCaseTo;
   }

   String getUpperCaseTo()
   {
      return upperCaseTo.get();
   }

   private void setUpperCaseTo(final String aValue)
   {
      upperCaseTo.set(aValue);
   }

   public StringProperty spaceSeparatedToProperty()
   {
      return spaceSeparatedTo;
   }

   String getSpaceSeparatedTo()
   {
      return spaceSeparatedTo.get();
   }

   private void setSpaceSeparatedTo(final String aValue)
   {
      spaceSeparatedTo.set(aValue);
   }

   public StringProperty dashSeparatedToProperty()
   {
      return dashSeparatedTo;
   }

   String getDashSeparatedTo()
   {
      return dashSeparatedTo.get();
   }

   private void setDashSeparatedTo(final String aValue)
   {
      dashSeparatedTo.set(aValue);
   }

   public BooleanProperty enableOriginalReplacementProperty()
   {
      return enableOriginalReplacement;
   }

   boolean getEnableOriginalReplacement()
   {
      return enableOriginalReplacement.get();
   }

   void setEnableOriginalReplacement(final boolean aValue)
   {
      enableOriginalReplacement.set(aValue);
   }

   public BooleanProperty enableCamelCaseReplacementProperty()
   {
      return enableCamelCaseReplacement;
   }

   boolean getEnableCamelCaseReplacement()
   {
      return enableCamelCaseReplacement.get();
   }

   void setEnableCamelCaseReplacement(final boolean aValue)
   {
      enableCamelCaseReplacement.set(aValue);
   }

   public BooleanProperty enableFirstLowerThenCamelCaseReplacementProperty()
   {
      return enableFirstLowerThenCamelCaseReplacement;
   }

   boolean getEnableFirstLowerThenCamelCaseReplacement()
   {
      return enableFirstLowerThenCamelCaseReplacement.get();
   }

   void setEnableFirstLowerThenCamelCaseReplacement(final boolean aValue)
   {
      enableFirstLowerThenCamelCaseReplacement.set(aValue);
   }

   public BooleanProperty enableLowerCaseReplacementProperty()
   {
      return enableLowerCaseReplacement;
   }

   boolean getEnableLowerCaseReplacement()
   {
      return enableLowerCaseReplacement.get();
   }

   public void setEnableLowerCaseReplacement(final boolean aValue)
   {
      enableLowerCaseReplacement.set(aValue);
   }

   public BooleanProperty enableUpperCaseReplacementProperty()
   {
      return enableUpperCaseReplacement;
   }

   boolean getEnableUpperCaseReplacement()
   {
      return enableUpperCaseReplacement.get();
   }

   void setEnableUpperCaseReplacement(final boolean aValue)
   {
      enableUpperCaseReplacement.set(aValue);
   }

   public BooleanProperty enableSpaceSeparatedReplacementProperty()
   {
      return enableSpaceSeparatedReplacement;
   }

   boolean getEnableSpaceSeparatedReplacement()
   {
      return enableSpaceSeparatedReplacement.get();
   }

   public void setEnableSpaceSeparatedReplacement(final boolean aValue)
   {
      enableSpaceSeparatedReplacement.set(aValue);
   }

   public BooleanProperty enableDashSeparatedReplacementProperty()
   {
      return enableDashSeparatedReplacement;
   }

   boolean getEnableDashSeparatedReplacement()
   {
      return enableDashSeparatedReplacement.get();
   }

   void setEnableDashSeparatedReplacement(final boolean aValue)
   {
      enableDashSeparatedReplacement.set(aValue);
   }

   public StringProperty startDirProperty()
   {
      return startDir;
   }

   private String getStartDir()
   {
      return startDir.get();
   }

   public void setStartDir(final String aStartDir)
   {
      startDir.set(aStartDir);
   }
}
