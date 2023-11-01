package com.example.pagetrade;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.help5g.uddoktapaysdk.UddoktaPay;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    String cartItemKey, bookName, price, mail, buyerMail, buyerName,status,taka;
    String transaction="Payment for PDF";
    Button pay;
    TextView priceTv, mailTv, statTv;
    EditText buyerTv, bName;
    WebView payWebView;
    LinearLayout uiLayout, webLayout;
    LottieAnimationView lottie;
    Uri uri;
    // Constants for payment
    private static final String API_KEY = "982d381360a69d419689740d9f2e26ce36fb7a50";
    private static final String CHECKOUT_URL = "https://sandbox.uddoktapay.com/api/checkout-v2";
    private static final String VERIFY_PAYMENT_URL = "https://sandbox.uddoktapay.com/api/verify-payment";
    private static final String REDIRECT_URL = "https://uddoktapay.com";
    private static final String CANCEL_URL = "https://uddoktapay.com";
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    // Instance variables to store payment information
    private String storedFullName;
    private String storedEmail;
    private String storedAmount;
    private String storedInvoiceId;
    private String storedPaymentMethod;
    private String storedSenderNumber;
    private String storedTransactionId;
    private String storedDate;
    private String storedFee;
    private String storedChargedAmount;

    private String storedMetaKey1;
    private String storedMetaValue1;

    private String storedMetaKey2;
    private String storedMetaValue2;

    private String storedMetaKey3;
    private String storedMetaValue3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cartItemKey = getIntent().getStringExtra("cartItemKey");
        bookName = getIntent().getStringExtra("bookName");
        price = getIntent().getStringExtra("price");
        mail = getIntent().getStringExtra("mail");
        if (cartItemKey == null || bookName == null || price == null || mail == null) {
            Toast.makeText(this, "Invalid data received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        payWebView= findViewById(R.id.payWebView);
        uiLayout= findViewById(R.id.uiLayout);
        webLayout= findViewById(R.id.webLayout);
        lottie= findViewById(R.id.lottie);
        priceTv= findViewById(R.id.priceTv);
        pay= findViewById(R.id.pay);
        mailTv= findViewById(R.id.sellerMail);
        buyerTv= findViewById(R.id.buyerMail);
        statTv= findViewById(R.id.stat);
        bName= findViewById(R.id.buyerName);

        String curr_price = extractNumericValue(price);
        priceTv.setText(curr_price);
        mailTv.setText(mail);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uiLayout.setVisibility(View.GONE);
                webLayout.setVisibility(View.VISIBLE);
                buyerMail = buyerTv.getText().toString();
                buyerName = bName.getText().toString();
                taka = priceTv.getText().toString();

                // Set your metadata values in the map
                Map<String, String> metadataMap = new HashMap<>();
                metadataMap.put("CustomMetaData1", "Meta Value 1");
                metadataMap.put("CustomMetaData2", "Meta Value 2");
                metadataMap.put("CustomMetaData3", "Meta Value 3");

                UddoktaPay.PaymentCallback paymentCallback = new UddoktaPay.PaymentCallback() {
                    @Override
                    public void onPaymentStatus(String status, String fullName, String email, String amount, String invoiceId,
                                                String paymentMethod, String senderNumber, String transactionId,
                                                String date, Map<String, String> metadataValues, String fee,String chargeAmount) {
                        // Callback method triggered when the payment status is received from the payment gateway.
                        // It provides information about the payment transaction.
                        storedFullName = bName.getText().toString();
                        storedEmail = buyerTv.getText().toString();
                        storedAmount = priceTv.getText().toString();
                        storedInvoiceId = invoiceId;
                        storedPaymentMethod = paymentMethod;
                        storedSenderNumber = senderNumber;
                        storedTransactionId = transactionId;
                        storedDate = date;
                        storedFee = fee;
                        storedChargedAmount = chargeAmount;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Clear previous metadata values to avoid duplication
                                storedMetaKey1 = null;
                                storedMetaValue1 = null;
                                storedMetaKey2 = null;
                                storedMetaValue2 = null;
                                storedMetaKey3 = null;
                                storedMetaValue3 = null;

                                // Iterate through the metadata map and store the key-value pairs
                                for (Map.Entry<String, String> entry : metadataValues.entrySet()) {
                                    String metadataKey = entry.getKey();
                                    String metadataValue = entry.getValue();

                                    if ("CustomMetaData1".equals(metadataKey)) {
                                        storedMetaKey1 = metadataKey;
                                        storedMetaValue1 = metadataValue;
                                    } else if ("CustomMetaData2".equals(metadataKey)) {
                                        storedMetaKey2 = metadataKey;
                                        storedMetaValue2 = metadataValue;
                                    } else if ("CustomMetaData3".equals(metadataKey)) {
                                        storedMetaKey3 = metadataKey;
                                        storedMetaValue3 = metadataValue;
                                    }
                                }

                                // Update UI based on payment status
                                if ("COMPLETED".equals(status)) {
                                    uiLayout.setVisibility(View.VISIBLE);
                                    webLayout.setVisibility(View.GONE);
                                    statTv.setText("PAYMENT SUCCESSFUL TO");
                                    buyerTv.setText(buyerMail);
                                    bName.setText(buyerName);
                                } else if ("PENDING".equals(status)) {
                                    uiLayout.setVisibility(View.VISIBLE);
                                    webLayout.setVisibility(View.GONE);
                                    statTv.setText("PAYMENT PENDING TO");
                                    buyerTv.setText(buyerMail);
                                    bName.setText(buyerName);
                                } else if ("ERROR".equals(status)) {
                                    uiLayout.setVisibility(View.VISIBLE);
                                    webLayout.setVisibility(View.GONE);
                                    statTv.setText("PAYMENT ERROR");
                                    buyerTv.setText(buyerMail);
                                    bName.setText(buyerName);
                                }
                            }
                        });
                    }
                };

                UddoktaPay uddoktapay = new UddoktaPay(payWebView, paymentCallback);
                uddoktapay.loadPaymentForm(API_KEY, buyerName, buyerMail, taka, CHECKOUT_URL, VERIFY_PAYMENT_URL, REDIRECT_URL, CANCEL_URL, metadataMap);
            }
        });
    }
    private String extractNumericValue(String input) {
        StringBuilder numericValue = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                numericValue.append(c);
            }
        }
        return numericValue.toString();
    }
}