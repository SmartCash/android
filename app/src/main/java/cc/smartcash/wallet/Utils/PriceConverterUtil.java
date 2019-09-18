package cc.smartcash.wallet.Utils;

import android.content.Context;

import java.math.BigDecimal;

public class PriceConverterUtil {

    private void calculateFromFiatToSmart(Context context, BigDecimal amountCrypto, BigDecimal amountFiat) {

        /*SmartCashApplication utils = new SmartCashApplication();

        BigDecimal amountConverted;
        Coin actualSelected = utils.getActualSelectedCoin(context);


        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).getName().equalsIgnoreCase(actualSelected.getName())) {
                actualSelected = coins.get(i);
                break;
            }
        }

        if (!txtAmount.getText().toString().isEmpty()) {

            BigDecimal amount = amountCrypto;

            BigDecimal finalValue = amount;

            if (actualSelected.getName().equals("SMART")) {
                amountConverted = utils.converterBigDecimal(finalValue, BigDecimal.valueOf(actualSelected.getValue()));
                //amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", "SMART"));
            } else {

                double currentPrice = actualSelected.getValue();
                double amountInTheField = Double.parseDouble(txtAmount.getText().toString());
                double ruleOfThree = amountInTheField / currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }

            //txtAmountConverted.setText(String.valueOf(amountConverted));

            BigDecimal amountWithFee = BigDecimal.valueOf(Double.parseDouble(txtAmountConverted.getText().toString())).add(BigDecimal.valueOf(0.001));
            sendButton.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.send_button, amountWithFee));

        }
*/
    }

    private void calculateFromSmartToFiat() {
/*
        BigDecimal amountConverted;
        Coin actualSelected = utils.getActualSelectedCoin(getContext());
        amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));

        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).getName().equalsIgnoreCase(actualSelected.getName())) {
                actualSelected = coins.get(i);
                break;
            }
        }

        if (!txtAmountConverted.getText().toString().isEmpty()) {

            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(txtAmountConverted.getText().toString()));

            BigDecimal finalValue = amount;

            if (actualSelected.getName().equals("SMART")) {
                amountConverted = utils.converterBigDecimal(finalValue, BigDecimal.valueOf(actualSelected.getValue()));
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", "SMART"));
            } else {

                double currentPrice = actualSelected.getValue();
                double amountInTheField = Double.parseDouble(txtAmountConverted.getText().toString());
                double ruleOfThree = amountInTheField * currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }

            txtAmount.setText(String.valueOf(amountConverted));

            BigDecimal amountWithFee = BigDecimal.valueOf(Double.parseDouble(txtAmountConverted.getText().toString())).add(BigDecimal.valueOf(0.001));


            sendButton.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.send_button, amountWithFee));

        }
        */

    }
}
