package com.example.tong.jiaowuxitong.view.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by TONG on 2017/1/19.
 */
public class MyChart extends View {

    private PathEffect pathEffect;
    private Point xEndpoint;
    private Point yEndpoint;
    private Point origin;
    private Paint paint;
    private ArrayList<String> xPoints;
    private ArrayList<Integer> yPoints;
    //    private Arra
    private int h, w;
    private int xStep;
    private int yStep;
    private int maxItem = 10;
    private float degreeDemo = 67.9f;
    private float maxDegree = 100;
    private boolean drawDegree = true;


    public MyChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }


    private void init() {
        dashPath = new Path();
        pathEffect = new DashPathEffect(new float[]{10, 5}, 2);
        xPoints = new ArrayList<>();
        yPoints = new ArrayList<>();
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        xPoints.add("10");
        xPoints.add("20");
        xPoints.add("30");
        xPoints.add("40");
        xPoints.add("50");
        xPoints.add("60");
        xPoints.add("70");
        xPoints.add("80");
        xPoints.add("90");
        xPoints.add("100");
        maxItem = xPoints.size();
        yPoints.add(12);
        yPoints.add(22);
        yPoints.add(42);
        yPoints.add(72);
        yPoints.add(32);
        yPoints.add(92);
        yPoints.add(02);
        yPoints.add(52);
        yPoints.add(42);
        yPoints.add(82);
        for (int i = 0; i < yPoints.size(); i++) total += yPoints.get(i);
    }


    private int total;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        drawCoordinate(canvas, paint);

        dividCoordinate(canvas, paint);

        drawTitle(canvas, paint);

        drawChart(canvas, paint);

    }

    private void drawTitle(Canvas canvas, Paint paint) {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(20);
        canvas.drawText(yTitle, origin.x + textXMargin, 2 * textYMargin, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(xTitle, w - textXMargin, origin.y - textYMargin, paint);
    }

    private String demoText = "demo";
    private int charWidth;
    private int charHeight;
    private Path dashPath;
    private float scale = 1;

    private void drawChart(Canvas canvas, Paint paint) {
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setPathEffect(pathEffect);
        for (int i = 0; i < yPoints.size(); i++) {
            int position = (int) (yPoints.get(i) / 10);
            int l = origin.x + position * xStep;
            int t = (int) ((1 - yPoints.get(i) * 1.f / total) * (charHeight * scale) + topMargin);
            int r = origin.x + (position + 1) * xStep;
            int b = origin.y - 2;
            Rect rect = new Rect(l, t, r, b);
            paint.setColor(Color.BLUE);

            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rect, paint);

//            canvas.drawText(demoText, origin.x + position * xStep + xStep / 2,(1 - degrees.get(i) / maxDegree) * charHeight+topMargin-topMargin/3, paint);

            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
             paint.setStrokeWidth(2);
            dashPath.moveTo(origin.x, t);
            dashPath.lineTo(r, t);
            canvas.drawPath(dashPath, paint);
        }

        paint.setPathEffect(null);

    }


    private float sideWid = 3.5f;
    private int textXMargin = 10;
    private int textYMargin = 20;
    private float textSize = 18f;

    private void dividCoordinate(Canvas canvas, Paint paint) {

        paint.setTextSize(textSize);
        for (int i = 0; i < maxItem; i++) {

            canvas.drawLine(origin.x, origin.y - (i * yStep), origin.x + sideWid, origin.y - (i * yStep), paint);

            canvas.drawLine(origin.x + (i * xStep), origin.y, origin.x + (i * xStep), origin.y - sideWid, paint);

            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(xPoints.get(i), origin.x + i * xStep, origin.y + textYMargin, paint);

//            paint.setTextAlign(Paint.Align.RIGHT);
//            canvas.drawText(String.valueOf(((int) (maxDegree * i / maxItem)))
//                    , origin.x - textXMargin, origin.y - i * yStep, paint);

        }


    }

    private int charTextViewSize = 50;
    private String xTitle = "xtitle";
    private String yTitle = "ytitle";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawCoordinate(Canvas canvas, Paint paint) {
        paint.setAntiAlias(true);
        canvas.drawLine(origin.x, origin.y, origin.x, 0, paint);//y
        canvas.drawLine(origin.x, origin.y, w, origin.y, paint);//x
//        canvas.save();

        Path path1 = new Path();
        path1.moveTo(w - 20, origin.y + 9);
        path1.lineTo(w - 20, origin.y - 9);
        path1.lineTo(w + 1, origin.y);
        path1.close();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path1, paint);
        path1.moveTo(origin.x - 9, 20);
        path1.lineTo(origin.x + 9, 20);
        path1.lineTo(origin.x, -1);
        path1.close();
        canvas.drawPath(path1, paint);
    }

    private int topMargin = 100;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        w = right - left;
        h = bottom - top;
        origin = new Point(0 + charTextViewSize, bottom - top - charTextViewSize);
//        xEndpoint = new Point(0,right-left);
//        yEndpoint = new Point(0,bottom)
        charWidth = right - left - charTextViewSize - textYMargin;
        xStep = (charWidth) / maxItem;
        charHeight = bottom - top - charTextViewSize - topMargin;
        yStep = (charHeight) / maxItem;
    }
}
