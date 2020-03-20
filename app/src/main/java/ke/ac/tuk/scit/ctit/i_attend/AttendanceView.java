package ke.ac.tuk.scit.ctit.i_attend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class AttendanceView extends LinearLayout {

    private Bitmap bm;
    private Canvas cv;
    private Paint eraser;
    private int holesBottomMargin=70;
    private int holeRadius=40;

    public AttendanceView(Context context){
        super(context);
        Init();
    }
    public AttendanceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public AttendanceView(Context context, AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    private void Init() {
        eraser=new Paint();
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraser.setAntiAlias(true);

    }
    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh){
        if (w != oldw || h!= oldh){
            bm=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
            cv=new Canvas();
        }
        super.onSizeChanged(w,h,oldw,oldh);
    }
    @Override
    protected void onDraw(Canvas canvas){
        int w=getWidth();
        int h=getHeight();

        bm.eraseColor(Color.TRANSPARENT);

        //setting up the bg color
        cv.drawColor(Color.WHITE);

        //drawing footer square with Confirm Button

        Paint paint=new Paint();
        paint.setARGB(255,250,250,250);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        cv.drawRect(0,h,w,h-pxFromDp(getContext(),holesBottomMargin),paint);

        //add punch holes to the card by erasure
        cv.drawCircle(0,0,holeRadius,eraser);//top left hole
        cv.drawCircle(w/2,0,holeRadius,eraser);//top-middle hole
        cv.drawCircle(w,0,holeRadius,eraser);
        cv.drawCircle(0,h-pxFromDp(getContext(),holesBottomMargin),holeRadius,eraser);
        cv.drawCircle(w,h-pxFromDp(getContext(),holesBottomMargin),holeRadius,eraser);

        canvas.drawBitmap(bm,0,0,null);

        //drawing dashed lines at the bottom
        Path mpPath=new Path();
        mpPath.moveTo(holeRadius,h-pxFromDp(getContext(),holesBottomMargin ));
        mpPath.quadTo(w-holeRadius,h-pxFromDp(getContext(),
                holesBottomMargin),w-holeRadius,h-pxFromDp(getContext(),holesBottomMargin));

        //the dashed line
        Paint dashed=new Paint();
        dashed.setARGB(255,200,200,200);
        dashed.setStyle(Paint.Style.STROKE);
        dashed.setPathEffect(new DashPathEffect(new float[]{10,5},0));
        canvas.drawPath(mpPath,dashed);

        super.onDraw(canvas);
    }

    static float pxFromDp(final Context context, final float dp) {

        return dp * context.getResources().getDisplayMetrics().density;
    }
}

