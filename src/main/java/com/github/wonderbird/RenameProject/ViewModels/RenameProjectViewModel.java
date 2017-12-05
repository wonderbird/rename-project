package com.github.wonderbird.RenameProject.ViewModels;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class RenameProjectViewModel implements ViewModel {
    public StringProperty from = new SimpleStringProperty("ProjectToBeRenamed");

    public StringProperty to = new SimpleStringProperty("TargetName");

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
