package com.cesarandres.ps2link.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cesarandres.ps2link.R;

class ProgressBarDivided extends View {
	private int mDivisions;

    private Paint paint = null;
    private int fillColor = Color.parseColor("#2D6EB9");
    private int emptyColor = Color.parseColor("#233952");
    private int separatorColor = Color.parseColor("#FFFFFF");
    private RectF rectFill = null;
    private RectF rectEmpty = null;
    private List<RectF> separators = null;
	
    public ProgressBarDivided(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProgressBarDivided,
                0, 0);

           try {
               this.setDivisions(a.getInteger(R.styleable.ProgressBarDivided_divisions, 10));
           } finally {
               a.recycle();
           }
           init();
    }

    private void init(){
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.separators = new ArrayList<RectF>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // Try for a width based on our minimum
       int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
       int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

       // Whatever the width ends up being, ask for a height that would let the pie
       // get as big as it can
       int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
       int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);

       setMeasuredDimension(w, h);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();

        int spaceFilled = (int)(5000 * width / 10000);
        this.rectFill = new RectF(0, 0, spaceFilled, height);
        this.rectEmpty = new RectF(spaceFilled, 0, width, height);

        int spaceBetween = (int)(width / 100);
        int widthPart = (int)(width / this.mDivisions - (int)(0.9 * spaceBetween));
        int startX = widthPart;
        for (int i=0; i<this.mDivisions - 1; i++)
        {
            this.separators.add( new RectF(startX, 0, startX + spaceBetween, height) );
            startX += spaceBetween + widthPart;
        }


        // Foreground
        this.paint.setColor(this.fillColor);
        canvas.drawRect(this.rectFill, this.paint);

        // Background
        this.paint.setColor(this.emptyColor);
        canvas.drawRect(this.rectEmpty, this.paint);

        // Separator
        this.paint.setColor(this.separatorColor);
        for (RectF separator : this.separators)
        {
            canvas.drawRect(separator, this.paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	return super.onTouchEvent(event);
    }
    
	public int getDivisions() {
		return mDivisions;
	}

	public void setDivisions(int divisions) {
		this.mDivisions = divisions;
		invalidate();
		requestLayout();
	}
}