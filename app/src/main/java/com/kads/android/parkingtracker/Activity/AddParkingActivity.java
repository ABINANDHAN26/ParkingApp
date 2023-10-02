package com.kads.android.parkingtracker.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kads.android.parkingtracker.Model.User;
import com.kads.android.parkingtracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class AddParkingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView email, carPlate, amountTv, dateTime;
    private Spinner carCompany, carColor, spotNumber, lotNumber,paymentMode,paid;
    private EditText parkingEtNoHours, parkingEtPriceHour;
    private Button submit,payBtn;
    private FirebaseAuth mAuth;

    private int amountValue = 0, priceHour = 0;

    private String strEmail = "", strCarPlate = "",
            strAmount = "", strDateTime = "", strCarCompany = "",
            strCarColor = "", strSpotNumber = "",
            strLotNumber = "", strParkingNoOfHours = "", strPriceHr = "",isPaid = "",strPaymentMode="";

//    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    String amount="1";
    String name = "Aakash R";
    String upiId = "aakash03122004@oksbi";
    String transactionNote = "pay test";
    String status;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        email = findViewById(R.id.parkingTxtEmailData);
        carPlate = findViewById(R.id.parkingTxtCarNoData);
        amountTv = findViewById(R.id.parkingTxtAmountData);
        dateTime = findViewById(R.id.parkingTxtDateData);
        paymentMode = findViewById(R.id.parkingSpinPaymentMode);
        carCompany = findViewById(R.id.parkingSpinCarCompany);
        carColor = findViewById(R.id.parkingSpinCarColor);
        spotNumber = findViewById(R.id.parkingSpinSpot);
        lotNumber = findViewById(R.id.parkingSpinLot);
        submit = findViewById(R.id.parkingBtnSubmit);
        payBtn = findViewById(R.id.parkingBtnPay);
        paid  = findViewById(R.id.paid);

        payBtn.setOnClickListener(this);
        submit.setOnClickListener(this);


        parkingEtNoHours = findViewById(R.id.parkingEtNoHours);
        parkingEtPriceHour = findViewById(R.id.parkingEtPriceHours);

        DateFormat dateFormatDate = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss", Locale.CANADA);
        Calendar cal = Calendar.getInstance();
        final String date = dateFormatDate.format(cal.getTime());
        final String time = dateFormatTime.format(cal.getTime());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userId = user.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User").child(userId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                Log.d("ProfileActivity", user1.getEmail());

                email.setText(user1.getEmail());
                carPlate.setText(user1.getCar_number());
                dateTime.setText(date + " " + time);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().equalsIgnoreCase("")) {
                    int value = Integer.valueOf(s.toString());
                    if (priceHour != 0) {
                        amountValue = value * priceHour;
                        amountTv.setText(String.valueOf(amountValue));
                    } else {
                        amountValue = value;
                        amountTv.setText(String.valueOf(value));
                    }
                }
            }
        };

        parkingEtNoHours.addTextChangedListener(textWatcher);
        parkingEtPriceHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().equalsIgnoreCase("")) {
                    int value = Integer.valueOf(s.toString());
                    if (amountValue != 0) {
                        amountValue = value * amountValue;
                        amountTv.setText(String.valueOf(amountValue));
                    } else {
                        amountTv.setText(String.valueOf(amountValue));
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.parkingBtnSubmit:
                //Save data to database
                extractData();
                if (checkData()) {
                    //save to db
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userId = user.getUid();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("carColor", strCarColor);
                    map.put("carCompany", strCarCompany);
                    map.put("carNo", strCarPlate);
                    map.put("dateTime", strDateTime);
                    map.put("email", strEmail);
                    map.put("lotNo", strLotNumber);
                    map.put("noOfHours", strParkingNoOfHours);
                    map.put("paymentMethod", strPaymentMode);
                    map.put("priceHour", strPriceHr);
                    map.put("spotNo", strSpotNumber);
                    map.put("paymentAmount",strAmount);
                    map.put("isPaid",isPaid);
                    myRef.child("parkingReceipt").child(userId).push().setValue(map);
                    Toast.makeText(AddParkingActivity.this, "Parking reciept successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddParkingActivity.this, ParkingReceiptActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Incomplete form", Toast.LENGTH_SHORT).show();
                    break;
                }
                break;

            case R.id.parkingBtnPay:
//                amount = strAmount;
//                uri = getUpiPaymentUri(name, upiId, transactionNote, amount);
//                payWithGPay();
                openGooglePay();
                break;
            default:

                break;
        }
    }

    private void openGooglePay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(GOOGLE_PAY_PACKAGE_NAME);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Handle the case where the intent is null (Google Pay app not found).
                Toast.makeText(AddParkingActivity.this, "Google Pay app not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddParkingActivity.this, "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }

    public String generateTransactionReference() {
        // Get the current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();

        // Generate a random UUID and remove hyphens to get a random string
        String randomString = UUID.randomUUID().toString().replaceAll("-", "");

        // Combine timestamp and random string to create the TR value
        String transactionReference = timestamp + randomString;

        // Make sure the TR value is unique (you can add additional logic for uniqueness)

        return transactionReference;
    }


    public ActivityResultLauncher<Intent> paymentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                String status = data.getStringExtra("Status");
                if (status != null && status.toLowerCase().equals("success")) {
                    Toast.makeText(AddParkingActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddParkingActivity.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });
    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            paymentLauncher.launch(intent);
        } else {
            Toast.makeText(AddParkingActivity.this, "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == resultCode) && status.equals("success")) {
            Toast.makeText(AddParkingActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddParkingActivity.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
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

    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }


    boolean checkData() {
        boolean isDataTrue = true;

        if (parkingEtNoHours.getText().toString().equals("")) {
            isDataTrue = false;
        }

        return isDataTrue;
    }


    void extractData() {
        strEmail = email.getText().toString();
        strCarPlate = carPlate.getText().toString();
        strCarCompany = carCompany.getSelectedItem().toString();
        strCarColor = carColor.getSelectedItem().toString();
        strParkingNoOfHours = parkingEtNoHours.getText().toString();
        strAmount = amountTv.getText().toString();
        strPriceHr = parkingEtPriceHour.getText().toString();
        strDateTime = dateTime.getText().toString();
        strLotNumber = lotNumber.getSelectedItem().toString();
        strSpotNumber = spotNumber.getSelectedItem().toString();
        isPaid = paid.getSelectedItem().toString();
        strPaymentMode = paymentMode.getSelectedItem().toString();
    }
}
