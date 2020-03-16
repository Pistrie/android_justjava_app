package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 1;
    int price = 0;
    String priceMessage = "";
    TextView quantityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(Integer.toString(quantity));
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        Log.v("MainActivity", "Has whipped cream: " + hasWhippedCream);

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();
        Log.v("MainActivity", "Has chocolate: " + hasChocolate);

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        Log.v("MainActivity", "The price is " + price);

        try {
            if (getName().equals(null)) {
                return;
            }
            createOrderSummary(price, hasWhippedCream, hasChocolate);
            createEmail();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void increment(View view) {
        if  (quantity > 99) {
            Toast.makeText(this, "You can't have more than 100 cups of coffee",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            quantity += 1;
        }
        displayQuantity(quantity);
    }

    public void decrement(View view) {
        if (quantity < 2) {
            Toast.makeText(this, "You can't have less than 1 cup of coffee",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            quantity -= 1;
        }
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
//    private void displayMessage(String message) {
//        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
//        orderSummaryTextView.setText(message);
//        orderSummaryTextView.setVisibility(View.VISIBLE);
//    }

    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int basePrice = 5;
        if (hasChocolate) {
            basePrice += 1;
        }
        if (hasWhippedCream) {
            basePrice += 2;
        }
        return quantity * basePrice;
    }

    private String getName() {
        EditText text = (EditText) findViewById(R.id.name_edit_text);
        String name = text.getText().toString().trim().toLowerCase();
        if (!name.equals("")) {
            return name.substring(0,1).toUpperCase() + name.substring(1);
        } else {
            Toast.makeText(this, "You need to enter a name", Toast.LENGTH_SHORT)
                    .show();
        }
        return null;
    }

    private void createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate) {
        priceMessage = "Name: " + getName();
        if (addWhippedCream) {
            priceMessage += "\nWith whipped cream.";
        } else {
            priceMessage += "\nWithout whipped cream.";
        }
        if (addChocolate) {
            priceMessage += "\nWith chocolate.";
        } else {
            priceMessage += "\nWithout chocolate.";
        }
        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\nTotal: $" + price;
        priceMessage += "\n" + getString(R.string.thank_you);
    }

    private void createEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"sylvester.roos@xs4all.nl"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee order for " + getName());
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
