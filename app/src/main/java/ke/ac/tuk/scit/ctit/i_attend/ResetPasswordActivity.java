package ke.ac.tuk.scit.ctit.i_attend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail=(EditText)findViewById(R.id.emailReset);
        btnReset=(Button)findViewById(R.id.resetButton);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        auth=FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Enter your registered email",Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this,"We have sent the reset password link to your email",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                        }else {
                            Toast.makeText(getApplicationContext(),"There was an error sending your password reeset link. Please check your email address",Toast.LENGTH_LONG).show();
                            inputEmail.setText("");
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

    }
}
