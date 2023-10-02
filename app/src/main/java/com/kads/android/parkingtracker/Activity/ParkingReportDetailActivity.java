package com.kads.android.parkingtracker.Activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kads.android.parkingtracker.Model.Receipt;
import com.kads.android.parkingtracker.R;

public class ParkingReportDetailActivity extends AppCompatActivity {

    private TextView txtViewEmail, txtViewCarPlate, txtViewCompany, txtViewColor, txtViewHours, txtViewDateTime, txtViewLot, txtViewSpot, txtViewPayment, txtViewAmount,txtViewIsPaid;
    Receipt receipt = new Receipt();
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    Button payBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_report_detail);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtViewEmail = findViewById(R.id.txtVEmail);
        txtViewCarPlate = findViewById(R.id.textViewCarPlate);
        txtViewCompany = findViewById(R.id.textViewCompany);
        txtViewColor = findViewById(R.id.textViewColor);
        txtViewHours = findViewById(R.id.textViewHours);
        txtViewDateTime = findViewById(R.id.textViewDate);
        txtViewLot = findViewById(R.id.textViewLot);
        txtViewSpot = findViewById(R.id.textViewSpot);
        txtViewPayment = findViewById(R.id.textViewPaymentMode);
        txtViewAmount = findViewById(R.id.textViewAmount);
        txtViewIsPaid = findViewById(R.id.is_paid_tv);
        payBtn = findViewById(R.id.payBtn);

        Intent intent = getIntent();
        receipt = (Receipt) intent.getSerializableExtra("receipt");

        txtViewEmail.setText(receipt.getEmail());
        txtViewCarPlate.setText(receipt.getCarNo());
        txtViewCompany.setText(receipt.getCarCompany());
        txtViewColor.setText(receipt.getCarColor());
        txtViewHours.setText(receipt.getNoOfHours());
        txtViewDateTime.setText(receipt.getDateTime());
        txtViewLot.setText(receipt.getLotNo());
        txtViewSpot.setText(receipt.getSpotNo());
        txtViewPayment.setText(receipt.getPaymentMethod());
        txtViewAmount.setText(receipt.getPaymentAmount());
        txtViewIsPaid.setText(receipt.getIsPaid());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receipt.getIsPaid().equals("Not Paid"))
            payBtn.setVisibility(View.VISIBLE);
        else
            payBtn.setVisibility(View.GONE);
        payBtn.setOnClickListener(view -> {

            openGooglePay();

        });
    }

    private void openGooglePay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(GOOGLE_PAY_PACKAGE_NAME);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Handle the case where the intent is null (Google Pay app not found).
                Toast.makeText(ParkingReportDetailActivity.this, "Google Pay app not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ParkingReportDetailActivity.this, "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }


    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
