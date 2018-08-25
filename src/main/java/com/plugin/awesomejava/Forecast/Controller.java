package com.plugin.awesomejava.Forecast;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

	@FXML
	public Label labelWelcome;
	@FXML
	public ChoiceBox<String> choiceBox;

	@FXML
	public TextField textField;

	@FXML
	public Button convertButton;
	private static final String C_To_F_TEXT = "Celcius to Fahrenheit";
	private static final String F_To_C_TEXT = "Fahrenheit to Celcius";

	private boolean isC_To_F_Text=true;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		choiceBox.getItems().add(C_To_F_TEXT);
		choiceBox.getItems().add(F_To_C_TEXT);

		choiceBox.setValue(C_To_F_TEXT);
		choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals( isC_To_F_Text))
					isC_To_F_Text=true;
				else
					isC_To_F_Text=false;
			}
		});
		convertButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				convert();
			}
		});


	}

	private void convert() {
		String input = textField.getText();
		float enteredTemperature=0.0f;
		try{
			enteredTemperature=Float.parseFloat(input);
		}catch (Exception exception)
		{
			warnuser();
			return;
		}

		//float enteredTemperature=Float.parseFloat(input);
		float newTemperature=0.0f;
		if(isC_To_F_Text)
			newTemperature=(enteredTemperature*9/5) + 32;
		else
			newTemperature=(enteredTemperature-32)*5/9;
		display(newTemperature);
	}

	private void warnuser() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Result");
		alert.setHeaderText("Error Occured");
		alert.setContentText("User Entered Invalid Temperature");
		alert.show();
	}

	private void display(float newTemperature) {
		String unit=isC_To_F_Text?"°F" : "°C";
		System.out.println("The new Temperature :" + newTemperature + " " + unit);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Result");
		alert.setContentText("The new Temperature :" + newTemperature + " " + unit);
		alert.show();

	}
}
