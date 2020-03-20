package ke.ac.tuk.scit.ctit.i_attend;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private UnitsAdapter adapter;
    private List<Unit> unitList;
    private ImageView img;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        initCollapsingToolbar();

        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog(v);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);



        unitList = new ArrayList<>();
        adapter = new UnitsAdapter(this, unitList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareUnits();
        img=(ImageView)findViewById(R.id.backdrop);

        try {
            Glide.with(this).load(R.drawable.thumb4).into((img));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void logoutDialog(View v) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to log out?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert=builder.create();
            alert.show();
    }


    /**
     * This method will have to be updated to get courses from Firebase Database
     */

    private void prepareUnits() {
        int [] covers=new int[]{
                R.drawable.thumbnail2,
                R.drawable.thumbnail3
        };
        Unit a=new Unit("2019 SUMMER SEM- SOFTWARE ENG.-401",1,covers[1]);
        unitList.add(a);

        a=new Unit("2019 SUMMER SEM- NETWORK ADMIN",1,covers[1]);
        unitList.add(a);

        a=new Unit("2019 SUMMER SEM- DATABASE ADMIN",1,covers[1]);
        unitList.add(a);

        a=new Unit("2019 SUMMER SEM- COMMON UNIT",1,covers[1]);
        unitList.add(a);

        a=new Unit("2019 SUMMER SEM- IBL 3203",1,covers[1]);
        unitList.add(a);

        adapter.notifyDataSetChanged();
    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.appbaer);
        appBarLayout.setExpanded(true);

        //hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange=-1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset==0){
                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    isShow=true;
                }else if(isShow){
                    collapsingToolbarLayout.setTitle("");
                    isShow=false;
                }
            }
        });
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount=spanCount;
            this.spacing=spacing;
            this.includeEdge=includeEdge;
        }
        @Override
        public void getItemOffsets (Rect outRect, View view,RecyclerView parent,RecyclerView.State state){
            //item position
            int position =parent.getChildAdapterPosition(view);
            //item column
            int column=position %spanCount;
            if (includeEdge){
                outRect.left=spacing-column* spacing/spanCount;
                outRect.right=(column+1)*spacing/spanCount;

                if (position <spanCount){
                    outRect.top=spacing;
                }
                outRect.bottom=spacing;
            }else{
                outRect.left=column*spacing/spanCount;
                outRect.right=spacing-(column +1)*spacing /spanCount;
                if (position>=spanCount){
                    outRect.top=spacing;
                }
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r=getResources();

        return Math.round(TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics()));

    }

}

