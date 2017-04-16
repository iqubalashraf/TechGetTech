package location.app.techgettech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final String MyPREFERENCES = "MyPrefs" ;
    public static final String EMAIL = "college.app.projectcollege.EMAIL";
    public static final String CUSTOMERNAME= "college.app.projectcollege.CUSTOMERNAME";
    TextView userName,techStatus;
    String email;
    String serverResponse;
    int check = 1;
    int acceptReject = 0;
    SharedPreferences sharedPreferences;
    Button finishJob,accpetJob,rejectJob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log.d("LIne", "Executed");
        userName = (TextView)findViewById(R.id.userName);
        Intent intent = getIntent();
        String name = intent.getStringExtra(CUSTOMERNAME);
        email = sharedPreferences.getString(SplashScreen.EMAIL, null);
        userName.setText("Hello " + name);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(SplashScreen.SERVER_IP_ADDRESS)
                .appendPath("techGet")
                .appendPath("getStatus.jsp")
                .appendQueryParameter("email", email);
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A", ":");
        GetStatus getStatus = new GetStatus();
        getStatus.execute(myUrl);
        Log.d("url:-", myUrl);

    }
    public class GetStatus extends AsyncTask<String, Void, Boolean> {
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
            Log.d("CustomerRegistartn:// ", "onPostExecution ");
            String status = (String)jsonObject.get("STATUS");
            int statusInt = Integer.parseInt(status);
            Log.d("CustomerRegistartn:// ", "onPostExecution ");
            techStatus = (TextView)findViewById(R.id.techStatus);
            if(statusInt ==0){
                techStatus.setText("You are now Busy");
            }
            if(statusInt == 1){
                techStatus.setText("You are now Available");
            }
            if(statusInt == 2){
                techStatus.setText("You are not Available");
            }
        }
    }
    public void viewMyOrder(View view){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(SplashScreen.SERVER_IP_ADDRESS)
                .appendPath("techGet")
                .appendPath("viewOrder.jsp")
                .appendQueryParameter("email", email);
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A",":");
        GetOrderDetails getOrderDetails = new GetOrderDetails();
        getOrderDetails.execute(myUrl);
        Log.d("url:-", myUrl);
    }
    public class GetOrderDetails extends AsyncTask<String, Void, Boolean> {
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
            Log.d("CustomerRegistartn:// ", "onPostExecution ");
            String status = (String)jsonObject.get("STATUS");
            if(status.equals("NO")){
                Toast.makeText(getApplicationContext(),"Oh..! You have no order still now",Toast.LENGTH_SHORT).show();
            }else if(status.equals("YES")){
                String name = (String)jsonObject.get("NAME");
                String phone = (String)jsonObject.get("PHONE");
                String address = (String)jsonObject.get("ADDRESS");
                String email = (String)jsonObject.get("EMAIL");
                String bstatus = (String)jsonObject.get("BSTATUS");
                int bstatusInt = Integer.parseInt(bstatus);
                String string = "Name:  "+name+"\nMobile No:  "+phone+"\nAddress: "+address+"Email: "+email;
                setContentView(R.layout.layout_order_details);
                finishJob = (Button)findViewById(R.id.finishJob);
                accpetJob = (Button)findViewById(R.id.acceptJob);
                rejectJob = (Button)findViewById(R.id.rejectJob);
                if(bstatusInt ==0){
                    finishJob.setVisibility(View.VISIBLE);
                    accpetJob.setVisibility(View.GONE);
                    rejectJob.setVisibility(View.GONE);
                }
                if(bstatusInt==2){
                    finishJob.setVisibility(View.GONE);
                    accpetJob.setVisibility(View.VISIBLE);
                    rejectJob.setVisibility(View.VISIBLE);
                }
                TextView userDetails = (TextView)findViewById(R.id.customerDetails);
                userDetails.setText(string);
            }else{
                Toast.makeText(getApplicationContext(),"Something went wrong. Try again",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void confirmStatus(View view){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(SplashScreen.SERVER_IP_ADDRESS)
                .appendPath("techGet")
                .appendPath("statusChange.jsp")
                .appendQueryParameter("email", email)
                .appendQueryParameter("status",Integer.toString(check));
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A",":");
        ConfirmStatus confirmStatus = new ConfirmStatus();
        confirmStatus.execute(myUrl);
        Log.d("url:-", myUrl);
    }
    public void makeMeAvailable(View view){
        check = 1;
    }
    public void makeMeBusy(View view){
        check = 0;
    }
    public void makeMeNotAvailable(View view){
        check = 2;
    }
    public void acceptJob(View view){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(SplashScreen.SERVER_IP_ADDRESS)
                .appendPath("techGet")
                .appendPath("processOrder.jsp")
                .appendQueryParameter("response", "accept")
                .appendQueryParameter("email", email);
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A",":");
        acceptReject = 0;
        ProcessOrder processOrder = new ProcessOrder();
        processOrder.execute(myUrl);
        Log.d("url:-", myUrl);
    }
    public void rejectJob(View view){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(SplashScreen.SERVER_IP_ADDRESS)
                .appendPath("techGet")
                .appendPath("processOrder.jsp")
                .appendQueryParameter("response","reject")
                .appendQueryParameter("email", email);
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A",":");
        acceptReject = 1;
        ProcessOrder processOrder = new ProcessOrder();
        processOrder.execute(myUrl);
        Log.d("url:-", myUrl);
    }
    public void finishJob(View view){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(SplashScreen.SERVER_IP_ADDRESS)
                .appendPath("techGet")
                .appendPath("processOrder.jsp")
                .appendQueryParameter("response","reject")
                .appendQueryParameter("email", email);
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A",":");
        acceptReject = 2;
        ProcessOrder processOrder = new ProcessOrder();
        processOrder.execute(myUrl);
        Log.d("url:-", myUrl);
    }
    public class ProcessOrder extends AsyncTask<String, Void, Boolean> {
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

            Log.d("CustomerRegistartn:// ", "onPostExecution ");
            if(acceptReject ==0){
                Toast.makeText(getApplicationContext(),"Order Accepted successfully",Toast.LENGTH_SHORT).show();
                //setContentView(R.layout.technician_registration_successful);
                finishJob = (Button)findViewById(R.id.finishJob);
                accpetJob = (Button)findViewById(R.id.acceptJob);
                rejectJob = (Button)findViewById(R.id.rejectJob);
                finishJob.setVisibility(View.VISIBLE);
                accpetJob.setVisibility(View.GONE);
                rejectJob.setVisibility(View.GONE);
            }
            if(acceptReject == 1){
                Toast.makeText(getApplicationContext(),"Order Rejected successfully",Toast.LENGTH_SHORT).show();

            }
            if(acceptReject == 2){
                Toast.makeText(getApplicationContext(),"Order Finish successfully",Toast.LENGTH_SHORT).show();

            }
        }
    }
    public class ConfirmStatus extends AsyncTask<String, Void, Boolean> {
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
            techStatus = (TextView)findViewById(R.id.techStatus);
            if(check ==0){
                techStatus.setText("You are now Busy");
            }
            if(check == 1){
                techStatus.setText("You are now Available");
            }
            if(check == 2){
                techStatus.setText("You are not Available");
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
