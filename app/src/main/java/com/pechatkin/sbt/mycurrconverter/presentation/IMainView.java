package com.pechatkin.sbt.mycurrconverter.presentation;

import java.util.List;

public interface IMainView {

    void updateSpinners(List<String> currencyNames);

    void updateConversionRate(String conversionRate, String currencyCodeFrom, String currencyCodeTo);

    void showResult(String result, String currencyNameTo);

    void showLoadErrorMessage();

    void showInputErrorMessage();
}
