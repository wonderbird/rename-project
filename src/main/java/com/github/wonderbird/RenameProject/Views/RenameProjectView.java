package com.github.wonderbird.RenameProject.Views;

import com.github.wonderbird.RenameProject.ViewModels.RenameProjectViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RenameProjectView implements FxmlView<RenameProjectViewModel>, Initializable {
    @FXML
    private TextField fromTextField;

    @FXML
    private TextField toTextField;

    @InjectViewModel
    private RenameProjectViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fromTextField.textProperty().bind(viewModel.fromProperty());
        toTextField.textProperty().bind(viewModel.toProperty());
    }
}
