package fr.univ.orleans.pnt.controller;

import fr.univ.orleans.pnt.model.Calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CalculatorController {

    @FXML
    private Label historyDisplay;

    @FXML
    private TextField display;

    private final Calculator model = new Calculator();

    private double firstNumber = 0;
    private String currentOperator = "";
    private String secondNumberText = "";
    private boolean justCalculated = false;

    @FXML
    private void handleNumber(ActionEvent event) {
        String digit = ((Button) event.getSource()).getText();

        if (justCalculated) {
            historyDisplay.setText("");
            display.setText(digit);
            justCalculated = false;
            return;
        }

        if (currentOperator.isEmpty()) {
           if (display.getText().equals("0") || display.getText().equals("Error")) {
                display.setText(digit);
            } else {
                display.setText(display.getText() + digit);
            }
        } else {
            secondNumberText += digit;
            display.setText(formatNumber(firstNumber) + " " + currentOperator + " " + secondNumberText);
        }
    }

    @FXML
    private void handleOperator(ActionEvent event) {
        if (display.getText().equals("Error")) {
            display.setText("0");
        }

        String operator = ((Button) event.getSource()).getText();

        if (currentOperator.isEmpty() || justCalculated) {
            firstNumber = Double.parseDouble(display.getText());
            justCalculated = false;
        }

        currentOperator = operator;
        secondNumberText = "";

        display.setText(formatNumber(firstNumber) + " " + currentOperator + " ");
    }

    @FXML
    private void handleEquals(ActionEvent event) {
        if (currentOperator.isEmpty() || secondNumberText.isEmpty()) {
            return;
        }

        double secondNumber = Double.parseDouble(secondNumberText);

        historyDisplay.setText(formatNumber(firstNumber) + " " + currentOperator + " " + formatNumber(secondNumber) + " =");

        try {
            double result = model.calculate(firstNumber, secondNumber, currentOperator);
            display.setText(formatNumber(result));
        } catch (ArithmeticException e) {
            display.setText("Error");
        }

        currentOperator = "";
        secondNumberText = "";
        justCalculated = true;
    }

    @FXML
    private void handleClear(ActionEvent event) {
        historyDisplay.setText("");
        display.setText("0");
        firstNumber = 0;
        currentOperator = "";
        secondNumberText = "";
        justCalculated = false;
    }

    @FXML
    private void handleBackspace(ActionEvent event) {
        if (justCalculated || display.getText().equals("Error")) {
            return;
        }

        if (!currentOperator.isEmpty()) {
            if (!secondNumberText.isEmpty()) {
                secondNumberText = secondNumberText.substring(0, secondNumberText.length() - 1);
                display.setText(formatNumber(firstNumber) + " " + currentOperator + " " + secondNumberText);
            } else {
                currentOperator = "";
                display.setText(formatNumber(firstNumber));
            }
        } else {
            String currentText = display.getText();
            if (currentText.length() > 1 && !(currentText.length() == 2 && currentText.startsWith("-"))) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            } else {
                display.setText("0");
            }
        }
    }

    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number);
        }
        return String.valueOf(number);
    }
}