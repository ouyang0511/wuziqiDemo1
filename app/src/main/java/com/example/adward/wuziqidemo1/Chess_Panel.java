package com.example.adward.wuziqidemo1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adward on 2017/6/12.
 */

public class Chess_Panel extends View {
    private Paint mPaint;
    //棋盘宽度
    private int mPanelWidth;
    //行宽
    private float myLineHeight;
    //行数
    private int maxLine = 10;
    //白棋子
    private Bitmap myWhitePiece;
    //黑棋子
    private Bitmap myBlackPiece;
    //游戏结束
    private boolean isGemOver;
    private float radioPiecelineheight = 3 * 1.0f / 4;
    //判断谁先手
    private boolean isWhite;
    //存储棋子的位置
    private List<Point> mWhileArray = new ArrayList<>();
    private List<Point> mBlackArray = new ArrayList<>();

    private boolean mIsgameOver;
    private boolean mIswhiteWiner;
    private  int MAX_COUNT_IN_LINE = 5;

    public Chess_Panel(Context context) {
        super(context);
    }

    public Chess_Panel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        //设置棋子图片
        myWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.pice_w);
        myBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.pice_b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        myLineHeight = 1.0f * mPanelWidth / maxLine;
        //棋子大小占行宽的3/4
        int pieceWidth = (int) (myLineHeight * radioPiecelineheight);
        //以src为原图，创建新的图像，指定新图像的高宽以及是否可变。
        myWhitePiece = Bitmap.createScaledBitmap(myWhitePiece, pieceWidth, pieceWidth, false);
        myBlackPiece = Bitmap.createScaledBitmap(myBlackPiece, pieceWidth, pieceWidth, false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsgameOver) return false;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {   //判断触摸动作，ACTION_UP为单点触摸离开
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getVaLidPiont(x, y); //获取当前的坐标

            if (mWhileArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }

            if (isWhite) {
                mWhileArray.add(p);
            } else {
                mBlackArray.add(p);
            }
            invalidate();         //invalidate()是用来刷新View的，必须在UI线程中使用
            isWhite = !isWhite;
        }
        return true;

    }

    private Point getVaLidPiont(int x, int y) {
        return new Point((int) (x / myLineHeight), (int) (y / myLineHeight));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawBoard(canvas);
        DrawPeice(canvas);
        setBackgroundColor(0x44ff0000);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhileArray);
        boolean blackWin = checkFiveInLine(mBlackArray);
        if (whiteWin || blackWin) {
            mIsgameOver = true;
            String text = mIswhiteWiner ? "白棋胜利" : "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            boolean win = HorizonInFiveLine(x, y, points);
            if (win) return true;
            win = verticalInFiveLine(x, y, points);
            if (win) return true;
            win = leftSlashInFiveLine(x, y, points);
            if (win) return true;
            win = rightSlashInFiveLine(x, y, points);
            if (win) return true;
        }


        return false;
    }

    private boolean rightSlashInFiveLine(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }

        }
        if (count == MAX_COUNT_IN_LINE) return true;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    private boolean leftSlashInFiveLine(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;

        return false;
    }

    private boolean verticalInFiveLine(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;

            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    private boolean HorizonInFiveLine(int x, int y, List<Point> points) {
        int count = 1;
        //左横向是否有相同棋子

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    private void DrawPeice(Canvas canvas) {
        for (int i = 0; i < mWhileArray.size(); i++) {
            Point whilePoint = mWhileArray.get(i);
            canvas.drawBitmap(myWhitePiece,
                    (whilePoint.x + (1 - radioPiecelineheight) / 2) * myLineHeight,
                    (whilePoint.y + (1 - radioPiecelineheight) / 2) * myLineHeight, null);
        }
        for (int i = 0; i < mBlackArray.size(); i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(myBlackPiece,
                    (blackPoint.x + (1 - radioPiecelineheight) / 2) * myLineHeight,
                    (blackPoint.y + (1 - radioPiecelineheight) / 2) * myLineHeight, null);
        }


    }

    private void DrawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = myLineHeight;
        for (int i = 0; i < maxLine; i++) {
            int StarX = (int) (lineHeight / 2);
            int EndX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(StarX, y, EndX, y, mPaint);
            canvas.drawLine(y, StarX, y, EndX, mPaint);


        }
    }


    //设置画板尺寸
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);
        int heightsize = MeasureSpec.getSize(heightMeasureSpec);
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);
        int width = Math.min(widthsize, heightsize);
        if (widthmode == MeasureSpec.UNSPECIFIED) {
            width = heightMeasureSpec;
        } else if (heightmode == MeasureSpec.UNSPECIFIED) {
            width = widthMeasureSpec;
        }
        setMeasuredDimension(width, width);
    }
}
