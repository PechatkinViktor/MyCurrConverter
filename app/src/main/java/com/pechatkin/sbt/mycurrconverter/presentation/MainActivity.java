package com.pechatkin.sbt.mycurrconverter.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pechatkin.sbt.mycurrconverter.R;
import com.pechatkin.sbt.mycurrconverter.utils.ResourceWrapper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainView {

    private static final int SECOND_ITEM = 1;

    private Spinner mFromSpinner;
    private Spinner mToSpinner;
    private TextView mConversionRateTextView;
    private EditText mAmountToConvertEditText;
    private Button mConvertButton;
    private TextView mResultTextView;

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void updateSpinners(List<String> currencyNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                currencyNames
        );
        mFromSpinner.setAdapter(adapter);
        mToSpinner.setAdapter(adapter);
        mToSpinner.setSelection(SECOND_ITEM);
    }

    @Override
    public void updateConversionRate(String conversionRate, String currencyCodeFrom, String currencyCodeTo) {
        String resultString = String.format(getString(R.string.conversion_rate), conversionRate, currencyCodeFrom, currencyCodeTo);
        mConversionRateTextView.setText(resultString);
    }

    @Override
    public void showResult(String result, String currencyNameTo) {
        String resultString = String.format(getString(R.string.conversion_result), result, currencyNameTo);
        mResultTextView.setText(resultString);
    }

    @Override
    public void showLoadErrorMessage() {
        Toast.makeText(this, getString(R.string.load_error_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInputErrorMessage() {
        Toast.makeText(this, getString(R.string.input_error_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMainPresenter.detachView();
    }

    private void init() {

        mFromSpinner = findViewById(R.id.spinner_from);
        mToSpinner = findViewById(R.id.spinner_to);
        mConversionRateTextView = findViewById(R.id.text_view_conversion_rate);
        mAmountToConvertEditText = findViewById(R.id.edit_text_amount);
        mConvertButton = findViewById(R.id.button_convert);
        mResultTextView = findViewById(R.id.text_view_result);

        ResourceWrapper resourceWrapper = new ResourceWrapper(this.getResources());
        mMainPresenter = new MainPresenter(this, resourceWrapper);
        mMainPresenter.loadCurrencies();

        mFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMainPresenter.setFromCurrencyPosition(position);
                calculateResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMainPresenter.setToCurrencyPosition(position);
                calculateResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mConvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });
    }

    private void calculateResult() {
        String amountToConvert = mAmountToConvertEditText.getText().toString();
        if (!amountToConvert.isEmpty()) mMainPresenter.calculateResult(amountToConvert);
    }
}
