package ke.ac.tuk.scit.ctit.i_attend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class AttendanceResultActivity extends AppCompatActivity {
    private static final String TAG=AttendanceResultActivity.class.getSimpleName();

    //we will change this URL to match one from the lecturer's dashboard
    private static final String URL="http://192.168.0.26/android/get-data.php/";

    private TextView txtName,txtDuration,txtLecturer,txtUnit,txtDate,txtregNo,txtError;
    private ImageView imgAvi;
    private Button btn_attend;
    private ProgressBar progressBar;
    private AttendanceView attendanceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_result);

        //Toolbar toolbar=findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName=(TextView)findViewById(R.id.stdName);
        txtLecturer=(TextView)findViewById(R.id.lecturer);
        txtregNo=(TextView)findViewById(R.id.regno);
        txtDate=(TextView)findViewById(R.id.lesson_date);
        txtDuration=(TextView)findViewById(R.id.duration);
        txtUnit=(TextView)findViewById(R.id.unit_title);
        txtError=(TextView)findViewById(R.id.txt_error);
        imgAvi=(ImageView)findViewById(R.id.avi);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        btn_attend=(Button)findViewById(R.id.btn_attend);
        attendanceView=findViewById(R.id.layout_attend);

        String barcode= getIntent().getStringExtra("code");

        //In case of an empty QR code close activity
        if (TextUtils.isEmpty(barcode)){
            Toast.makeText(getApplicationContext(),"The QR code is empty",Toast.LENGTH_LONG).show();
            finish();
        }
        //else search for the code
        searchBarcode(barcode);
    }

    private void searchBarcode(String barcode) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET + URL + barcode, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Attendance response" + response.toString());
                        if (!response.has("error")) {
                            renderAttend(response);
                        } else {
                            showNoAttendance();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error:"+error.getMessage());
                showNoAttendance();
            }
        });
        MyQRCode.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    //render student details to card

    private void renderAttend(JSONObject response) {
        try {
            //convert json to student object
            Attend attend=new Gson().fromJson(response.toString(),Attend.class);
            if (attend != null){
                txtName.setText(attend.getStudentName());
                txtUnit.setText(attend.getUnitCode());
                txtDate.setText(attend.getDate());
                txtregNo.setText(attend.getRegNo());

                if (attend.isPresent()){
                    btn_attend.setText("PRESENT");
                    btn_attend.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                }else{
                    btn_attend.setText("ABSENT");
                    btn_attend.setTextColor(ContextCompat.getColor(this,R.color.btn_disabled));

                }
                attendanceView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }else{
                showNoAttendance();
            }


        }catch (JsonSyntaxException e){
            Log.e(TAG,"JSON Exception"+e.getMessage());
            showNoAttendance();
            Toast.makeText(getApplicationContext(),"Error occurred parsing QR Code",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            showNoAttendance();
            Toast.makeText(getApplicationContext(),"Error occurred parsing the QR Code",Toast.LENGTH_LONG).show();

        }
    }

    private void showNoAttendance() {
        txtError.setVisibility(View.VISIBLE);
        attendanceView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private class Attend {
        String studentName;
        String date;
        String unitCode;
        String regNo;

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }

        public String getRegNo() {
            return regNo;
        }

        public void setRegNo(String regNo) {
            this.regNo = regNo;
        }

        public boolean isPresent() {
            return isPresent;
        }

        public void setPresent(boolean present) {
            isPresent = present;
        }

        @SerializedName("present")
        boolean isPresent;


    }
}