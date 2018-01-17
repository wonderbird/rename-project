package com.github.wonderbird.RenameProject.Views;

import com.github.wonderbird.RenameProject.Models.Notification;
import com.github.wonderbird.RenameProject.ViewModels.RenameProjectViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RenameProjectView implements FxmlView<RenameProjectViewModel>, Initializable {
    @FXML
    private TextField startDirTextField;

    @FXML
    private TextField fromTextField;

    @FXML
    private Label originalFromLabel;

    @FXML
    private Label camelCaseFromLabel;

    @FXML
    private Label firstLowerThenCamelCaseFromLabel;

    @FXML
    private Label lowerCaseFromLabel;

    @FXML
    private Label upperCaseFromLabel;

    @FXML
    private Label spaceSeparatedFromLabel;

    @FXML
    private Label dashSeparatedFromLabel;

    @FXML
    private TextField toTextField;

    @FXML
    private Label originalToLabel;

    @FXML
    private Label camelCaseToLabel;

    @FXML
    private Label firstLowerThenCamelCaseToLabel;

    @FXML
    private Label lowerCaseToLabel;

    @FXML
    private Label upperCaseToLabel;

    @FXML
    private Label spaceSeparatedToLabel;

    @FXML
    private Label dashSeparatedToLabel;

    @FXML
    private CheckBox enableOriginalReplacementCheckBox;

    @FXML
    private CheckBox enableCamelCaseReplacementCheckBox;

    @FXML
    private CheckBox enableFirstLowerThenCamelCaseReplacementCheckBox;

    @FXML
    private CheckBox enableLowerCaseReplacementCheckBox;

    @FXML
    private CheckBox enableUpperCaseReplacementCheckBox;

    @FXML
    private CheckBox enableSpaceSeparatedReplacementCheckBox;

    @FXML
    private CheckBox enableDashSeparatedReplacementCheckBox;

    @InjectViewModel
    private RenameProjectViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startDirTextField.textProperty().bindBidirectional(viewModel.startDirProperty());

        fromTextField.textProperty().bindBidirectional(viewModel.fromProperty());
        originalFromLabel.textProperty().bindBidirectional(viewModel.originalFromProperty());
        firstLowerThenCamelCaseFromLabel.textProperty().bindBidirectional(viewModel.firstLowerThenCamelCaseFromProperty());
        camelCaseFromLabel.textProperty().bindBidirectional(viewModel.camelCaseFromProperty());
        lowerCaseFromLabel.textProperty().bindBidirectional(viewModel.lowerCaseFromProperty());
        upperCaseFromLabel.textProperty().bindBidirectional(viewModel.upperCaseFromProperty());
        spaceSeparatedFromLabel.textProperty().bindBidirectional(viewModel.spaceSeparatedFromProperty());
        dashSeparatedFromLabel.textProperty().bindBidirectional(viewModel.dashSeparatedFromProperty());

        toTextField.textProperty().bindBidirectional(viewModel.toProperty());
        originalToLabel.textProperty().bindBidirectional(viewModel.originalToProperty());
        firstLowerThenCamelCaseToLabel.textProperty().bindBidirectional(viewModel.firstLowerThenCamelCaseToProperty());
        camelCaseToLabel.textProperty().bindBidirectional(viewModel.camelCaseToProperty());
        lowerCaseToLabel.textProperty().bindBidirectional(viewModel.lowerCaseToProperty());
        upperCaseToLabel.textProperty().bindBidirectional(viewModel.upperCaseToProperty());
        spaceSeparatedToLabel.textProperty().bindBidirectional(viewModel.spaceSeparatedToProperty());
        dashSeparatedToLabel.textProperty().bindBidirectional(viewModel.dashSeparatedToProperty());

        enableOriginalReplacementCheckBox.selectedProperty().bindBidirectional(viewModel.enableOriginalReplacementProperty());
        enableCamelCaseReplacementCheckBox.selectedProperty().bindBidirectional(viewModel.enableCamelCaseReplacementProperty());
        enableFirstLowerThenCamelCaseReplacementCheckBox.selectedProperty().bindBidirectional(viewModel.enableFirstLowerThenCamelCaseReplacementProperty());
        enableLowerCaseReplacementCheckBox.selectedProperty().bindBidirectional(viewModel.enableLowerCaseReplacementProperty());
        enableUpperCaseReplacementCheckBox.selectedProperty().bindBidirectional(viewModel.enableUpperCaseReplacementProperty());
        enableSpaceSeparatedReplacementCheckBox.selectedProperty().bindBidirectional(viewModel.enableSpaceSeparatedReplacementProperty());
        enableDashSeparatedReplacementCheckBox.selectedProperty().bindBidirectional(viewModel.enableDashSeparatedReplacementProperty());

        viewModel.subscribe(Notification.BROWSESTARTDIR.toString(), (key, payload) -> browseStartDir((String)payload[0]));
    }

    private void browseStartDir(String initialDir) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(initialDir));
        File file = directoryChooser.showDialog(fromTextField.getScene().getWindow());
        if (file != null) {
            viewModel.setStartDir(file.getPath());
        }
    }

    @FXML
    public void browseAction() {
        viewModel.getBrowseCommand().execute();
    }

    @FXML
    public void cancelAction() {
        viewModel.getCancelCommand().execute();
    }

    @FXML
    public void renameAction() {
        viewModel.getRenameCommand().execute();
    }
}
