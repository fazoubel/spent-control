package com.azoubel.expensecontrol.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.azoubel.expensecontrol.R;
import com.azoubel.expensecontrol.model.Address;
import com.azoubel.expensecontrol.ui.UserPickerActivity;

public class AddressView extends ConstraintLayout{

    public static final int PICK_USER_ADDRESS = 3;

    private EditText streetET;
    private EditText numberET;
    private EditText neighborhoodET;
    private EditText cityET;
    private EditText stateET;
    private EditText countryET;
    private EditText zipET;
    private EditText complementET;
    private EditText referenceET;
    private EditText apartmentET;
    private EditText blockET;
    private Button findAddressBT;

    private Activity intentStarter;

    public AddressView(Context context) {
        super(context);
        init();
    }

    public AddressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.view_address, this);
        streetET = findViewById(R.id.street);
        numberET = findViewById(R.id.number);
        neighborhoodET = findViewById(R.id.neighborhood);
        cityET = findViewById(R.id.city);
        stateET = findViewById(R.id.state);
        countryET = findViewById(R.id.country);
        zipET = findViewById(R.id.zip);
        complementET = findViewById(R.id.complement);
        referenceET = findViewById(R.id.reference);
        apartmentET = findViewById(R.id.apartment);
        blockET = findViewById(R.id.block);
        findAddressBT = findViewById(R.id.findAddress);

        findAddressBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            if(intentStarter != null) {
                Intent userPickerIntent = new Intent(intentStarter, UserPickerActivity.class);
                intentStarter.startActivityForResult(userPickerIntent, PICK_USER_ADDRESS);
            }
            }
        });
    }

    public Address getAddress() {

        final String street = streetET.getText().toString();
        final String numberString = numberET.getText().toString();
        final String neighborhood = neighborhoodET.getText().toString();

        if(TextUtils.isEmpty(street) || TextUtils.isEmpty(numberString) || TextUtils.isEmpty(neighborhood)) {
            return null;
        }

        String city = cityET.getText().toString();
        String state = stateET.getText().toString();
        String country = countryET.getText().toString();
        String zip = zipET.getText().toString();
        String complement = complementET.getText().toString();
        String reference = referenceET.getText().toString();
        String apartmentString = apartmentET.getText().toString();
        String block = blockET.getText().toString();

        Address address = new Address();
        address.setStreet(street);
        address.setNumber(Integer.parseInt(numberString));
        address.setNeighborhood(neighborhood);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setZipCode(zip);
        address.setComplement(complement);
        address.setReference(reference);
        if(!TextUtils.isEmpty(apartmentString)){
            address.setApartment(Integer.parseInt(apartmentString));
            address.setApartmentBlock(block);
        }

        return address;
    }

    public void fillAddress(Address address) {
        if(address != null) {
            streetET.setText(address.getStreet());
            numberET.setText("" + address.getNumber());
            neighborhoodET.setText(address.getNeighborhood());
            cityET.setText(address.getCity());
            stateET.setText(address.getState());
            countryET.setText(address.getCountry());
            zipET.setText(address.getZipCode());
            complementET.setText(address.getComplement());
            referenceET.setText(address.getReference());
            apartmentET.setText("" + address.getApartment());
            blockET.setText(address.getApartmentBlock());
        }
    }

    public void setIntentStarter(Activity intentStarter) {
        this.intentStarter = intentStarter;
    }
}
