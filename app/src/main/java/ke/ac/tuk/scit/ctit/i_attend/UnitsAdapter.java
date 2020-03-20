package ke.ac.tuk.scit.ctit.i_attend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class UnitsAdapter extends RecyclerView.Adapter<UnitsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Unit> unitList;
    private CardView card;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,count;
        public ImageView thumbnail,overflow;

        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            card=(CardView)view.findViewById(R.id.card_view);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,SplashActivity.class));
                }
            });
        }
    }
    public UnitsAdapter(Context mContext,List<Unit> unitList){
        this.mContext=mContext;
        this.unitList=unitList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unit_card,parent,false);
                return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Unit unit=unitList.get(position);
        holder.title.setText(unit.getName());
        holder.count.setText(unit.getNumOfUnits()+ "  unit");


        Glide.with(mContext).load(unit.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.overflow);
            }
        });
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,SplashActivity.class));

            }
        });


    }

    private void showPopupMenu(ImageView view) {
        //inflating the menu
        PopupMenu popup= new PopupMenu(mContext,view);
        MenuInflater inflater= popup.getMenuInflater();
        inflater.inflate(R.menu.menu_unit,popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemCLickListener());
        popup.show();
    }

    @Override
    public int getItemCount() {
        return unitList.size();
    }

    private class MyMenuItemCLickListener extends Activity implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemCLickListener(){

        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.action_add_favorite:
                    mContext.startActivity(new Intent(mContext,SplashActivity.class));

                    return true;
                default:
            }
            return false;
        }
    }
}
