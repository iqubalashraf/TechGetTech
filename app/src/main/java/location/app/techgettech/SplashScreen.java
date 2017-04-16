package location.app.techgettech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

/**
 * Created by ashrafiqubal on 16/05/16.
 */
public class SplashScreen extends AppCompatActivity {
    Spinner customerDropDownMenu;
    SharedPreferences sharedpreferences;
    public static final String SERVER_IP_ADDRESS = "192.168.43.47:8080";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String CUSTOMERNAME= "college.app.projectcollege.CUSTOMERNAME";
    public static final String ADDRESS= "college.app.projectcollege.ADDRESS";
    public static final String MOBILENO= "college.app.projectcollege.MOBILENO";
    public static final String PINCODE= "college.app.projectcollege.PINCODE";
    public static final String LATITUDE = "college.app.projectcollege.customer.LATITUDE";
    public static final String LONGITUDE = "college.app.projectcollege.customer.LONGITUDE";
    public static final String ISNEWUSER = "college.app.projectcollege.ISNEWUSER";
    public static final String EMAIL = "college.app.projectcollege.EMAIL";
    public static final String PASSWORD = "college.app.projectcollege.PASSWORD";
    public static final String SPECIALITY = "college.app.projectcollege.SPECIALITY";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    double latitude=25.6181719;
    double longitude=85.1726037;
    private Location mLastLocation=null;
    EditText name,email, password,mobile, address, city, state, pincode,priceTech;
    String nameString,emailString,passwordString,mobileString,addressString,pincodeString,priceTechString;
    String serverResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (EditText)findViewById(R.id.customerName);
        email = (EditText)findViewById(R.id.customerEmail);
        password = (EditText)findViewById(R.id.customerPassword);
        mobile = (EditText)findViewById(R.id.customerContactNo);
        priceTech = (EditText)findViewById(R.id.techPrice);
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        boolean isNewUser = sharedpreferences.getBoolean(ISNEWUSER,true);
        //isNewUser = true;
        if(!isNewUser){
            String customerName = sharedpreferences.getString(CUSTOMERNAME,"Unable to fetch!");
            String emailString = sharedpreferences.getString(EMAIL,"Unable to fetch");
            Intent mainActivityForCustomer = new Intent(SplashScreen.this,MainActivity.class);
            mainActivityForCustomer.putExtra(CUSTOMERNAME,customerName);
            mainActivityForCustomer.putExtra(EMAIL,emailString);
            startActivity(mainActivityForCustomer);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void onClickWelcomeScreen(View view){
        setContentView(R.layout.featurs_screen);
    }
    public void onCallLoginScreen (View view){
        setContentView(R.layout.login_registration);
    }
    public void onCallWelcomeScreen(View view){
        setContentView(R.layout.welcome_screen);
    }
    public void loginActivity (View view){
        setContentView(R.layout.layout_login);
    }
    public void singupActivity(View view){
        setContentView(R.layout.layout_registration);
        customerDropDownMenu = (Spinner) findViewById(R.id.customerDropDownMenu);
        customerDropDownMenu.setVisibility(View.VISIBLE);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.customerDropDownList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        customerDropDownMenu.setAdapter(adapter);
    }
    public void startMainActivity(View view){
        name = (EditText)findViewById(R.id.customerName);
        email = (EditText)findViewById(R.id.customerEmail);
        password = (EditText)findViewById(R.id.customerPassword);
        mobile = (EditText)findViewById(R.id.customerContactNo);
        priceTech = (EditText)findViewById(R.id.techPrice);
        address = (EditText)findViewById(R.id.addressLineOne);
        city = (EditText)findViewById(R.id.cityName);
        state = (EditText)findViewById(R.id.stateName);
        pincode = (EditText)findViewById(R.id.customerPinCode);
        nameString = name.getText().toString();
        if(nameString.length()<4){
            Toast.makeText(getApplicationContext(), "Name length should be greter then four word", Toast.LENGTH_SHORT).show();
            return;
        }
        mobileString = mobile.getText().toString();
        if(mobileString.length()!=10){
            Toast.makeText(getApplicationContext(),"Mobile No should be of 10 Digit",Toast.LENGTH_SHORT).show();
            return;
        }
        String addressLineOne = address.getText().toString();
        if(addressLineOne.length()<1){
            Toast.makeText(getApplicationContext(),"Address is in-sufficient",Toast.LENGTH_SHORT).show();
            return;
        }
        String City = city.getText().toString();
        if(City.length()<3){
            Toast.makeText(getApplicationContext(),"Invalid City",Toast.LENGTH_SHORT).show();
            return;
        }
        String State = state.getText().toString();
        if(State.length()<3){
            Toast.makeText(getApplicationContext(),"Invalid State",Toast.LENGTH_SHORT).show();
            return;
        }
        addressString = addressLineOne+", "+City+"\n"+State;
        pincodeString = pincode.getText().toString();
        if(pincodeString.length()!=6){
            Toast.makeText(getApplicationContext(),"Invalid PINCODE",Toast.LENGTH_SHORT).show();
            return;
        }
        String passwordString  = password.getText().toString();
        if(passwordString.length()<8){
            Toast.makeText(getApplicationContext(),"Password Should be minimum of 8 digit",Toast.LENGTH_SHORT).show();
            return;
        }
        String emailString = email.getText().toString();
        if(!emailString.contains("@")){
            Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
            return;
        }
        String speciality = customerDropDownMenu.getSelectedItem().toString();
        String priceString = priceTech.getText().toString();
        if(priceString == null){
            Toast.makeText(getApplicationContext(),"Price cannot be EMPTY",Toast.LENGTH_SHORT).show();
            return;
        }
        int priceInt = Integer.parseInt(priceString);
        if(priceInt<9){
            Toast.makeText(getApplicationContext(),"Price cannot be less then RS 10",Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(CUSTOMERNAME, nameString);
        editor.putString(ADDRESS, addressString);
        editor.putString(MOBILENO, mobileString);
        editor.putString(PINCODE, pincodeString);
        editor.putBoolean(ISNEWUSER, false);
        editor.putString(LATITUDE, Double.toString(latitude));
        editor.putString(LONGITUDE,Double.toString(longitude));
        editor.putString(EMAIL, emailString);
        editor.putString(PASSWORD, passwordString);
        editor.putString(SPECIALITY,speciality);
        editor.putInt("price", priceInt);
        editor.commit();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(SERVER_IP_ADDRESS)
                    .appendPath("techGet")
                    .appendPath("technicianRegistration.jsp")
                    .appendQueryParameter("name", nameString)
                    .appendQueryParameter("phone",mobileString)
                    .appendQueryParameter("latitude",Double.toString(latitude))
                    .appendQueryParameter("longitude",Double.toString(longitude))
                    .appendQueryParameter("pincode",pincodeString)
                    .appendQueryParameter("address",addressLineOne)
                    .appendQueryParameter("city",City)
                    .appendQueryParameter("state",State)
                    .appendQueryParameter("skill",speciality)
                    .appendQueryParameter("email",emailString)
                    .appendQueryParameter("price",priceString)
                    .appendQueryParameter("password",passwordString);
            String myUrl = builder.build().toString();
            myUrl = myUrl.replace("%3A",":");
            TechnicianRegistration technicianRegistration = new TechnicianRegistration();
            technicianRegistration.execute(myUrl);
            Log.d("url:-", myUrl);
        }
    public class TechnicianRegistration extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {
                String str;
                HttpClient myClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                HttpResponse myResponse = myClient.execute(get);
                BufferedReader br = new BufferedReader(new InputStreamReader(myResponse.getEntity().getContent()));
                while ((str = br.readLine()) != null) {
                    //serverResponse1 = str;
                    Log.d("CustomerRegistration: ", str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            prepared = true;
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            /*int check = Integer.parseInt(serverResponse1);
            if (check == 1) {
                setContentView(R.layout.technician_registration_successful);
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong, Try again later", Toast.LENGTH_LONG).show();
            }*/
            Log.d("CustomerRegistartn:// ", "onPostExecution ");
            setContentView(R.layout.technician_registration_successful);
        }
    }
    public void mainActivityTechnician(View view){
        Intent mainActivityForCustomer = new Intent(SplashScreen.this,MainActivity.class);
        mainActivityForCustomer.putExtra(CUSTOMERNAME,nameString);
        mainActivityForCustomer.putExtra(EMAIL,emailString);
        startActivity(mainActivityForCustomer);
        finish();
    }
    public void loginNow(View view){
        email = (EditText)findViewById(R.id.techEmail);
        password = (EditText)findViewById(R.id.techPassword);
        emailString = email.getText().toString();
        if(!emailString.contains("@")){
            Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
            return;
        }
        passwordString = password.getText().toString();
        if(passwordString.length()<8){
            Toast.makeText(getApplicationContext(),"Password Should be minimum of 8 digit",Toast.LENGTH_SHORT).show();
            return;
        }
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(SERVER_IP_ADDRESS)
                .appendPath("techGet")
                .appendPath("technicianLogin.jsp")
                .appendQueryParameter("email",emailString)
                .appendQueryParameter("password", passwordString);
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A",":");
        TechnicianLogin technicianLogin = new TechnicianLogin();
        technicianLogin.execute(myUrl);
        Log.d("url:-", myUrl);
    }
    public class TechnicianLogin extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {
                String str;
                HttpClient myClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                HttpResponse myResponse = myClient.execute(get);
                BufferedReader br = new BufferedReader(new InputStreamReader(myResponse.getEntity().getContent()));
                while ((str = br.readLine()) != null) {
                    serverResponse = str;
                    Log.d("CustomerRegistration: ", str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            prepared = true;
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Object obj = JSONValue.parse(serverResponse);
            JSONObject jsonObject = (JSONObject)obj;
            String name = (String)jsonObject.get("NAME");
            Log.d("NAME",name);
            if (name.equals("NO")) {
                Toast.makeText(getApplicationContext(), "USERNAME PASSWORD mismatch", Toast.LENGTH_LONG).show();
            } else {
                Intent mainActivityForCustomer = new Intent(SplashScreen.this,MainActivity.class);
                mainActivityForCustomer.putExtra(CUSTOMERNAME,name);
                mainActivityForCustomer.putExtra(EMAIL,emailString);
                startActivity(mainActivityForCustomer);
                finish();
                Toast.makeText(getApplicationContext(), "LOGIN Successfully", Toast.LENGTH_LONG).show();
            }
            Log.d("CustomerRegistartn:// ", "onPostExecution ");
            //setContentView(R.layout.technician_registration_successful);
        }
    }
        //setContentView(R.layout.customer_registration_successful);
    public void fetchLocation(View view){

          mLastLocation = LocationServices.FusedLocationApi
                   .getLastLocation(mGoogleApiClient);
           latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        address = (EditText)findViewById(R.id.addressLineOne);
        city = (EditText)findViewById(R.id.cityName);
        state = (EditText)findViewById(R.id.stateName);
        pincode = (EditText)findViewById(R.id.customerPinCode);
        if (mLastLocation != null) {
            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
            try{
                List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
                //Toast.makeText(getApplicationContext(),""+,Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),addresses.get(0).getAddressLine(0).toString(),Toast.LENGTH_SHORT).show();
                address.setText(addresses.get(0).getAddressLine(0).toString());
                city.setText(addresses.get(0).getLocality());
                state.setText(addresses.get(0).getAdminArea());
                pincode.setText(addresses.get(0).getPostalCode());
            }catch (IOException e){

            }
        } else {
           Toast.makeText(getApplicationContext(),"Unable to fetch location",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }
    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

}