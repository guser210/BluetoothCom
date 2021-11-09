package com.example.bluetoothcom.controls;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Size;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bluetoothcom.R;

/**
 * TODO: document your custom view class.
 */
public class ControlOdometerV2_1 extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;


    private Point mCenter = new Point(0,0);
    private Size mSize = new Size(0,0);
    private int mColor = 0;
    private int mRadius = 0;
    private int mEdgeOffset = 20;
    private int mGraduationCircleColor = 0; //
    private int mGraduationCircleWidth = 0;
    private int mGraduationCircleEdgeOffset = 0;


    private int mGraduations= 0; //"13"
    private int mGraduationDegrees= 0; //"270"
    private float mBaseAngle= 0; //"90"

    private float mFontWidth = 0;
    private int mFontSize= 0; //"16dp"
    private int mFontColor= 0; //"@color/white"
    private int mFontOffset = 0;

    private int mScaleColorMajor= 0; //"@color/white"
    private int mScaleColorMinor= 0; //"@color/gray_400"
    private int mScaleWidthMajor = 0;

    private int mScaleLengthMajor= 0; //"100"
    private int mScaleLengthMinor= 0; //"80"
    private int mScaleWidthMinor = 0;

    private int mGraduationRedLine = 0;
    private int mGraduationRedLineColor = 0;

    private float mCurrentAngle = 90;

    private ConstraintLayout mNeedleGroup;


    public void setFontColor(int color ){
        this.mFontColor = color;
    }

    @Override
    public void setBackgroundColor(int color) {
        //super.setBackgroundColor(color);
        mColor = color;
    }
    public void setRingColor(int color) {
        mGraduationCircleColor = color;
    }
    public void setScaleColor(int color) {
        mScaleColorMinor = color;
        mScaleColorMajor = color;
    }
    public void setNeedleGroup(ConstraintLayout group){
        this.mNeedleGroup = group;
    }
    public ControlOdometerV2_1(Context context) {
        super(context);
        init(null, 0);
    }

    public ControlOdometerV2_1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ControlOdometerV2_1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSize = new Size(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

        mCenter.set(mSize.getWidth()/2, mSize.getHeight()/2);

        mRadius = Math.min(mSize.getWidth() - mEdgeOffset, mSize.getHeight() - mEdgeOffset) / 2;

    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ControlOdometerV2_1, defStyle, 0);

        //TODO: properties
        mColor = a.getColor(R.styleable.ControlOdometerV2_1_odometerBackgroundColor, mColor);
        mEdgeOffset = a.getInteger(R.styleable.ControlOdometerV2_1_odometerEdgeOffset, mEdgeOffset);
        mGraduationCircleColor = a.getColor(R.styleable.ControlOdometerV2_1_odometerGraduationCircleColor,mGraduationCircleColor);
        mGraduationCircleWidth = a.getInteger(R.styleable.ControlOdometerV2_1_odometerGraduationCircleWidth,mGraduationCircleWidth);
        mGraduationCircleEdgeOffset = a.getInteger(R.styleable.ControlOdometerV2_1_odometerGraduationCircleEdgeOffset, mGraduationCircleEdgeOffset);

        mGraduations = a.getInteger(R.styleable.ControlOdometerV2_1_odometerGraduations,mGraduations);
        mGraduationDegrees = a.getInteger(R.styleable.ControlOdometerV2_1_odometerGraduationDegrees,mGraduationDegrees);
        mBaseAngle = a.getFloat(R.styleable.ControlOdometerV2_1_odometerBaseAngle, mBaseAngle);

        mFontWidth = (float)a.getDimension(R.styleable.ControlOdometerV2_1_odometerFontWidth,mFontWidth);
        mFontSize = (int) a.getDimension(R.styleable.ControlOdometerV2_1_odometerFontSize,mFontSize);
        mFontColor = a.getColor(R.styleable.ControlOdometerV2_1_odometerFontColor,mFontColor);
        mFontOffset = (int) a.getDimension(R.styleable.ControlOdometerV2_1_odometerFontOffset,mFontOffset);

        mScaleLengthMajor = (int) a.getDimension(R.styleable.ControlOdometerV2_1_odometerScaleLengthMajor,mScaleLengthMajor);
        mScaleColorMajor = a.getColor(R.styleable.ControlOdometerV2_1_odometerScaleColorMajor,mScaleColorMajor);
        mScaleWidthMajor = (int) a.getDimension(R.styleable.ControlOdometerV2_1_odometerScaleWidthMajor, mScaleWidthMajor);

        mScaleLengthMinor = (int) a.getDimension(R.styleable.ControlOdometerV2_1_odometerScaleLengthMinor,mScaleLengthMinor);
        mScaleWidthMinor = (int) a.getDimension(R.styleable.ControlOdometerV2_1_odometerScaleWidthMinor, mScaleWidthMinor);
        mScaleColorMinor = a.getColor(R.styleable.ControlOdometerV2_1_odometerScaleColorMinor , mScaleColorMinor);

        mGraduationRedLine = a.getInteger(R.styleable.ControlOdometerV2_1_odometerGraduationRedLine,mGraduationRedLine);
        mGraduationRedLineColor = a.getColor(R.styleable.ControlOdometerV2_1_odometerGraduationRedLineColor,mGraduationRedLineColor);

        //TODO: end properties

        mExampleString = a.getString(
                R.styleable.ControlOdometerV2_1_exampleString);
        mExampleColor = a.getColor(
                R.styleable.ControlOdometerV2_1_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.ControlOdometerV2_1_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.ControlOdometerV2_1_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.ControlOdometerV2_1_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements(); //TODO: uncomment this code before live
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBackground(canvas, mColor);
        setGraduationCircle(canvas, mGraduationCircleColor,mGraduationCircleWidth, mGraduationCircleEdgeOffset);


//        mGraduations
//        mGraduationDegrees
//        mBaseAngle
//        mFontSize
//        mFontColor
//
//        mScaleColorMajor
//        mScaleColorMinor
//
//        mScaleLengthMajor
//        mScaleLengthMinor

        setScale(canvas, mGraduations,mBaseAngle,  mGraduationDegrees,mScaleLengthMajor, mScaleWidthMajor, mScaleColorMajor,
                mScaleLengthMinor, mScaleWidthMinor, mScaleColorMinor);

        if( 1==1)
            return;
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }


    private void setBackground(Canvas canvas, int color){
         Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

         paint.setColor(color);

         paint.setStrokeCap(Paint.Cap.ROUND);
         paint.setStyle(Paint.Style.FILL_AND_STROKE);

         canvas.drawCircle(mCenter.x, mCenter.y, mRadius,paint);
    }

    private void setGraduationCircle(Canvas canvas, int color, int width, int edgeBuffer){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(color);

        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius - (edgeBuffer + (width*0.5f)),paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenter.x, mCenter.y, 5 ,paint);

    }

    private void setScale(Canvas canvas, int graduations,float baseAngle, int graduationDegrees,int scaleLengthMajor,int scaleWidthMajor, int scaleColorMajor,
                          int scaleLengthMinor, int scaleWidthMinor, int scaleColorMinor){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(scaleColorMajor);

        paint.setStrokeWidth(scaleWidthMajor);
        float cx = 0.0f, cy = 0.0f;
        int degIncrements = graduationDegrees / graduations;
        int graduationCounter = 0;
        for( int deg = (int)baseAngle; deg <= baseAngle + graduationDegrees; deg += degIncrements){
            cx = (float) Math.cos(Math.toRadians(deg));
            cy = (float) Math.sin(Math.toRadians(deg));

            if( graduationCounter >= mGraduationRedLine )
                paint.setColor(mGraduationRedLineColor);

            canvas.drawLine((mRadius - (mGraduationCircleEdgeOffset + scaleLengthMajor)) * cx + mCenter.x ,
                    (mRadius - (mGraduationCircleEdgeOffset + scaleLengthMajor)) * cy + mCenter.y,
                    (mRadius - (mGraduationCircleEdgeOffset + 5)) * cx + mCenter.x,
                    (mRadius - (mGraduationCircleEdgeOffset + 5)) * cy + mCenter.y,
                    paint);
            graduationCounter++;
        }

        paint.setStrokeWidth(scaleWidthMinor);
        paint.setColor(scaleColorMinor);

        graduationCounter = 0;
        for( int deg = ((int)baseAngle + (degIncrements /2)); deg < baseAngle + graduationDegrees; deg += degIncrements){
            cx = (float) Math.cos(Math.toRadians(deg));
            cy = (float) Math.sin(Math.toRadians(deg));

            if( graduationCounter >= mGraduationRedLine )
                paint.setColor(mGraduationRedLineColor);

            canvas.drawLine((mRadius - (mGraduationCircleEdgeOffset + scaleLengthMinor)) * cx + mCenter.x ,
                    (mRadius - (mGraduationCircleEdgeOffset + scaleLengthMinor)) * cy + mCenter.y,
                    (mRadius - (mGraduationCircleEdgeOffset + mGraduationCircleWidth)) * cx + mCenter.x,
                    (mRadius - (mGraduationCircleEdgeOffset + mGraduationCircleWidth)) * cy + mCenter.y,
                    paint);

            graduationCounter++;
        }


        ;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);

        paint.setStrokeWidth(mFontWidth);

        paint.setTextSize(mFontSize);
        paint.setColor(mFontColor);


        int numbers = 0;
        graduationCounter = 0;
        for( int deg = (int)baseAngle; deg <= baseAngle + graduationDegrees; deg += degIncrements){
            cx = (float) Math.cos(Math.toRadians(deg));
            cy = (float) Math.sin(Math.toRadians(deg));

            if( graduationCounter >= mGraduationRedLine )
                paint.setColor(mGraduationRedLineColor);

            if( (numbers % 2) == 1 || numbers == 0)
                canvas.drawText("" + numbers,
                        ((mRadius - (mGraduationCircleEdgeOffset + scaleLengthMajor + mFontOffset  )) * cx) + (mCenter.x - mFontSize/2),
                        ((mRadius - (mGraduationCircleEdgeOffset + scaleLengthMajor + mFontOffset)) * cy) + (mCenter.y + mFontSize/2) ,
                        paint);

            numbers++;
            graduationCounter++;
        }


    }
    private RotateAnimation mNeedleAnimation;
    private ValueAnimator mNeedleValueAnimator;
    public void setValue(int value){
        float toDegrees = (mGraduationDegrees /  mGraduations ) * ((float)value * 0.001f);// * (float)value ;

        if( mNeedleValueAnimator != null)
            mNeedleValueAnimator.cancel();

        toDegrees += mBaseAngle;

        mNeedleValueAnimator =  ValueAnimator.ofFloat((float) mNeedleGroup.getRotation(), (float) toDegrees);

        mNeedleValueAnimator.setDuration( 400);

        mNeedleValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if( mNeedleValueAnimator != animation){

//                   // animation = null;
                    return;}

                mNeedleGroup.setRotation( (float)animation.getAnimatedValue());
            }

        });

        mNeedleValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
        mNeedleValueAnimator.start();



    }
    public void setValueWorks(int value){
        int duration = 50;

        float toDegrees = (mGraduationDegrees /  mGraduations ) * ((float)value * 0.001f);// * (float)value ;


        if( mNeedleAnimation != null && mNeedleAnimation.hasStarted())
            mNeedleAnimation.cancel();


        toDegrees += mBaseAngle;
        if( toDegrees > mCurrentAngle)
            duration = (int) (( toDegrees -  mCurrentAngle) * 10);
        else
            duration = (int) ((  mCurrentAngle - toDegrees) * 10);






        mNeedleAnimation = new RotateAnimation( mCurrentAngle, toDegrees, Animation.RELATIVE_TO_PARENT,0.500f, Animation.RELATIVE_TO_PARENT, 0.500f);



        float finalToDegrees = toDegrees;
        int finalDuration = duration;
        mNeedleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCurrentAngle = finalToDegrees;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setAnimationListener(null);

               // System.out.println("Set Duration = " + String.valueOf(finalDuration) + " Actual= " + String.valueOf(animation.getDuration()));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

     //   mCurrentAngle = toDegrees;

        mNeedleAnimation.setDuration(duration);
        mNeedleAnimation.setInterpolator(new LinearInterpolator( ));

        mNeedleAnimation.setFillAfter(true);
        mNeedleGroup.startAnimation(mNeedleAnimation);



    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view"s example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view"s example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view"s example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view"s example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }


}