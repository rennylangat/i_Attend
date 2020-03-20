package ke.ac.tuk.scit.ctit.i_attend;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    BarcodeReader barcodeReader;
    private DatabaseReference databaseReference;

    Date date= Calendar.getInstance().getTime();
    DateFormat dateFormat=new SimpleDateFormat("dd-MMMM-yyyy HH:mm");
    String strDate=dateFormat.format(date);
    private DocumentReference documentReference= FirebaseFirestore.getInstance().document("attendance/"+strDate);
    public Map<String,Object>data=new HashMap<String,Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
        String currentUser=FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        Toast.makeText(getApplicationContext(),currentUser,Toast.LENGTH_LONG).show();
        DocumentReference mDocumentReference=FirebaseFirestore.getInstance()
                .document("students/details/"+currentUser+"/personal_details/");

        mDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Details details1=new Details();
                String name=(String)documentSnapshot.get("studentName");
                String regno=(String)documentSnapshot.get("regNo");

                Log.d("name","done");
                Map<String,Object> data1=new HashMap<String,Object>();
                data.put("studentName",name);
                data.put("regNo",regno);
                documentReference.set(data);
                Toast.makeText(getApplicationContext(),"Added "+name+" "+regno,Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onScanned(Barcode barcode) {

        // playing barcode reader beep sound
        barcodeReader.playBeep();
        String details=barcode.rawValue.toString();
        String [] detail_array=details.split(",");

        databaseReference= FirebaseDatabase.getInstance().getReference("attendance");

        //using hashmap to add data

        data.put("date",detail_array[0]);
        data.put("unit code",detail_array[1]);


        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Attendance has been taken successfully!!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Attendance has not been taken !!",Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
