package ke.ac.tuk.scit.ctit.i_attend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail,loginPassword;
    private Button loginButton;
    private TextView resetPassButton;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }

        loginEmail=(EditText)findViewById(R.id.email);
        loginPassword=(EditText)findViewById(R.id.password);
        loginButton=(Button)findViewById(R.id.loginButton);
        resetPassButton=(TextView)findViewById(R.id.resetPassButton);

        auth=FirebaseAuth.getInstance();
        resetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= loginEmail.getText().toString();
                final String password=loginPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Please input your school-given email address",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Please input your password",Toast.LENGTH_LONG).show();
                }
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            if (password.length()<6){
                                loginPassword.setError("Password too short. Enter a minimum of 6 characters!");
                            }else{
                                Toast.makeText(getApplicationContext(),"Authentication failed check your email or password",Toast.LENGTH_LONG).show();

                            }
                        }else {
                            Intent start=new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(start);
                            finish();
                        }
                    }
                });

            }
        });


    }
}
