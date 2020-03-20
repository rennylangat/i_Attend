package ke.ac.tuk.scit.ctit.i_attend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private Button btnscan;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No toolbar theme method
        //transparentToolbar();

        setContentView(R.layout.activity_splash);
        btnscan=(Button)findViewById(R.id.btn_scan);
        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(SplashActivity.this,ScanActivity.class);
                startActivity(inte);
            }
        });
    }

    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT>=19 && Build.VERSION.SDK_INT<21){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,true);
        }
        if (Build.VERSION.SDK_INT>=19){
            getWindow().getDecorView().setSystemUiVisibility
                    (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT>=21){
            setWindowFlag(this,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
    }

    private void setWindowFlag(Activity activity,final int bits,boolean on) {
        Window win=activity.getWindow();
        WindowManager.LayoutParams winParams=win.getAttributes();
        if (on){
            winParams.flags |=bits;
        }else{
            winParams.flags &=bits;
        }
        win.setAttributes(winParams);
    }
}
