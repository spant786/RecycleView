package com.example.tablerecycleview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.tablerecycleview.Module.User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    final List<User> user = new ArrayList<>();
    Spinner country;
    AutoCompleteTextView image;
    String[] images={"sagar","male","female"};
    EditText name, dob, phone, email;
    RadioGroup gender;
    Button submit,click;
    RadioButton male, female, other;
    String[] countr = {"--Select Country--","Nepal", "United Kingdom", "Switzerland", "USA", "Canada", "France", "New zealand"};
    String uname, udob, uphone, uemail, ugender, ucountry,uimage;
    CalendarView calendarView;
    Calendar calendar = Calendar.getInstance();

/* ----- Date Picker for Date of Birth -----*/
    DatePickerDialog.OnDateSetListener mydatepicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR,i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH,i2);
            String mydateFormat = "dd-MM-y";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mydateFormat, Locale.getDefault());
            dob.setText(simpleDateFormat.format(calendar.getTime()));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        country = findViewById(R.id.Spinner);
        name = findViewById(R.id.Name);
        dob = findViewById(R.id.DoB);
        phone = findViewById(R.id.Phone);
        email = findViewById(R.id.Email);
        gender = findViewById(R.id.RG);
        male = findViewById(R.id.RBMale);
        female = findViewById(R.id.RBFemale);
        other = findViewById(R.id.RBOther);
        submit = findViewById(R.id.btnSubmit);
        click = findViewById(R.id.click);
        image = findViewById(R.id.image);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter(this, R.layout.images,images);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.countryname, countr);
        image.setAdapter(stringArrayAdapter);
        image.setThreshold(1);
        country.setAdapter(adapter);
        gender.setOnCheckedChangeListener(this);
        submit.setOnClickListener(this);
        dob.setOnClickListener(this);
        click.setOnClickListener(this);


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.RBMale) {
            ugender = "Male";
            Toast.makeText(this, "Male", Toast.LENGTH_SHORT).show();
        }
        if (i == R.id.RBFemale) {
            ugender = "Female";
            Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show();
        }
        if (i == R.id.RBOther) {
            ugender = "Other";
            Toast.makeText(this, "Others", Toast.LENGTH_SHORT).show();
        }
    }

    private void setaValue() {
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ucountry = adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        setaValue();
        uname = name.getText().toString();
        uemail = email.getText().toString();
        udob = dob.getText().toString();
        uphone = phone.getText().toString();
        uimage = image.getText().toString();
        String uri = "@drawable/"+uimage;
        int resID = getResources().getIdentifier(uri, null, getPackageName());

        if (validate()) {
            if(view.getId() == R.id.btnSubmit){
                user.add(new User(uname,udob,uemail,ugender,uphone,ucountry,resID));

                Toast.makeText(this, "User has been added successfully", Toast.LENGTH_SHORT).show();
            }
        }
        if(view.getId() == R.id.DoB){
            new DatePickerDialog(this,mydatepicker,
                    calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

        }
        if (view.getId() == R.id.click){
            if(!user.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, userlist.class);
                intent.putExtra("allusers", (Serializable) user);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "User Data not available", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private boolean validate() {
        if (TextUtils.isEmpty(uname)) {
            name.setError("Enter Name");
            return false;

        }
        if (TextUtils.isEmpty(uemail)) {
            email.setError("Enter Email");
            return false;

        }

        if (TextUtils.isEmpty(udob)) {
            dob.setError("Enter DOB");
            return false;

        }
        if (TextUtils.isEmpty(uphone)) {
            phone.setError("Enter Phone");
            return false;

        }
        if (TextUtils.isEmpty(uimage)) {
            image.setError("Enter image");
            return false;

        }
        if (TextUtils.isEmpty(ucountry)) {
            Toast.makeText(this, "Select Country", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (ucountry.equals("--Select Country--")) {
            Toast.makeText(this, "Select Country", Toast.LENGTH_SHORT).show();
            return false;

        }
        if(!TextUtils.isDigitsOnly(uphone)){
            phone.setError("Invalid Phone");
            return false;

        }
        if(TextUtils.isEmpty(ugender)){
            Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
            return false;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(uemail).matches()){
            email.setError("Invalid Email");
            return false;
        }

        return true;


    }
}

