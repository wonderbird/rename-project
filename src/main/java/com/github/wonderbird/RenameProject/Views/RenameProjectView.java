package com.github.wonderbird.RenameProject.Views;

import com.github.wonderbird.RenameProject.Models.Notification;
import com.github.wonderbird.RenameProject.ViewModels.RenameProjectViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RenameProjectView implements FxmlView<RenameProjectViewModel>, Initializable {
    @FXML
    private TextField fromTextField;

    @FXML
    private TextField camelCaseFromTextField;

    @FXML
    private TextField lowerCaseFromTextField;

    @FXML
    private TextField upperCaseFromTextField;

    @FXML
    private TextField spaceSeparatedFromTextField;

    @FXML
    private TextField dashSeparatedFromTextField;

    @FXML
    private TextField toTextField;

    @FXML
    private TextField startDirTextField;

    @InjectViewModel
    private RenameProjectViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fromTextField.textProperty().bindBidirectional(viewModel.fromProperty());
        camelCaseFromTextField.textProperty().bindBidirectional(viewModel.camelCaseFromProperty());
        lowerCaseFromTextField.textProperty().bindBidirectional(viewModel.lowerCaseFromProperty());
        upperCaseFromTextField.textProperty().bindBidirectional(viewModel.upperCaseFromProperty());
        spaceSeparatedFromTextField.textProperty().bindBidirectional(viewModel.spaceSeparatedFromProperty());
        dashSeparatedFromTextField.textProperty().bindBidirectional(viewModel.dashSeparatedFromProperty());
        toTextField.textProperty().bindBidirectional(viewModel.toProperty());
        startDirTextField.textProperty().bindBidirectional(viewModel.startDirProperty());

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
