package edu.wpi.alcoholcontextualizer.treemap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.umd.cs.treemap.AbstractMapLayout;
import edu.umd.cs.treemap.Mappable;
import edu.umd.cs.treemap.Rect;
import edu.umd.cs.treemap.SquarifiedLayout;
import edu.umd.cs.treemap.TreeModel;
import edu.wpi.alcoholcontextualizer.database.DatabaseHandler;

import static android.R.attr.value;

public class MapLayoutView extends View {

    private static final String FAMILY = "Family";
    private static final String FRIENDS = "Friends";
    private static final String COWORKER = "Coworker";

    private Mappable[] previousMappableItems;
    private Mappable[] currentMappableItems;
    private Paint mRectBorderPaint;
    private Paint mTextPaint;

    private AndroidMapItem currentRootItem;
    private AndroidMapItem previousRootItem;

    private int viewWidth = 0;
    private int viewHeight = 0;

    private List<AndroidMapItem> rectangles = new ArrayList<>();

    public MapLayoutView(Context context, AttributeSet attributeSet) {
        super(context);
    }

    public MapLayoutView(Context context, TreeModel model) {
        super(context);

        currentRootItem = (AndroidMapItem) model.getMapItem();
        previousRootItem = (AndroidMapItem) model.getMapItem();

        currentMappableItems = model.getTreeItems();//getItems();

        setupMappableItemsArray();

        // Set up the Paint for the rectangle border
        mRectBorderPaint = new Paint();
        mRectBorderPaint.setColor(Color.BLACK);
        mRectBorderPaint.setStyle(Paint.Style.STROKE); // outline the rectangle
        mRectBorderPaint.setStrokeWidth(5); // single-pixel outline

        // Set up the Paint for the text label
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(20);

    }

    private void setupMappableItemsArray() {
        rectangles.clear();
        // Draw all the rectangles and their labels
        if (currentMappableItems != null) {
            for (Mappable mappableItem : currentMappableItems) {
                AndroidMapItem item = (AndroidMapItem) mappableItem;
                rectangles.add(item);
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        // Lay out the placement of the rectangles within the area available to this view
        setupMapLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRectangle(0, canvas, currentRootItem.getBoundsRectF(), true);
        drawText(canvas, currentRootItem.getLabel(), currentRootItem.getBoundsRectF(), true);

        // Draw all the rectangles and their labels
        if (currentMappableItems != null) {
            ArrayList<Integer> weightList = new ArrayList<>();
            ArrayList<AndroidMapItem> androidMapItemArrayList = new ArrayList<>();
            for (Mappable mappableItem : currentMappableItems) {
                AndroidMapItem item = (AndroidMapItem) mappableItem;
                weightList.add(item.getWeight());
                androidMapItemArrayList.add(item);
            }

            Collections.sort(weightList);
            Collections.sort(androidMapItemArrayList);

//            for (Integer i: weightList){
//                Log.d("***************", String.valueOf(i));
//            }

            double fraction = 0.1;
            for (AndroidMapItem item : androidMapItemArrayList) {

                int color = darken(Color.GREEN, fraction);
                fraction += 0.1;
                System.out.println(String.valueOf(fraction));
                drawRectangle(color, canvas, item.getBoundsRectF(), false);
                drawText(canvas, item.getLabel() + " [" + item.getWeight() + "]", item.getBoundsRectF(), false);
            }

//            for (Mappable mappableItem : currentMappableItems) {
//                AndroidMapItem item = (AndroidMapItem) mappableItem;
////                int color = getNextColor((float) weightList.get(0) / item.getWeight());
//                int color = darken(Color.RED, (double) item.getWeight() / weightList.get(weightList.size() - 1));
////                Log.d("*******************", item.getLabel() + " " + item.getWeight());
//                drawRectangle(color, canvas, item.getBoundsRectF(), false);
//                drawText(canvas, item.getLabel() + " [" + item.getWeight() + "]", item.getBoundsRectF(), false);
//            }
        }
    }

    private int getNextColor(double fraction) {
        float[] hsv = new float[3];
        int color = Color.BLUE;
        Color.colorToHSV(color, hsv);
        hsv[2] *= value; // value component
        color = Color.HSVToColor(hsv);
        return color;
    }

    public static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int darken(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

    private static int darkenColor(int color, double fraction) {
        return (int) Math.max(color - (color * fraction), 0);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }

    private void drawRectangle(int color, Canvas canvas, RectF rectF, boolean root) {
        Paint rectBackgroundPaint = new Paint();

        if (root) {
            rectBackgroundPaint.setColor(Color.rgb(192, 192, 192));
            rectBackgroundPaint.setStyle(Paint.Style.FILL);

            Paint rectBorderPaint = new Paint();
            rectBorderPaint.setColor(Color.BLACK);
            rectBorderPaint.setStyle(Paint.Style.STROKE); // outline the rectangle
            rectBorderPaint.setStrokeWidth(20);

            // Draw the rectangle's background
            canvas.drawRect(rectF, rectBackgroundPaint);
            // Draw the rectangle's border
            canvas.drawRect(rectF, rectBorderPaint);
        } else {
            Random rnd = new Random();
//            rectBackgroundPaint.setColor(Colors.COLORFUL_COLORS[rnd.nextInt(27)]);
            rectBackgroundPaint.setColor(color);
            rectBackgroundPaint.setStyle(Paint.Style.FILL);

            // Draw the rectangle's background
            canvas.drawRect(rectF, rectBackgroundPaint);
            // Draw the rectangle's border
            canvas.drawRect(rectF, mRectBorderPaint);
        }


    }

    private void drawText(Canvas canvas, String text, RectF rectF, boolean root) {
        if (root) {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            float textSize = rectF.width() / 7;
            textPaint.setTextSize(textSize);
            textPaint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText(text, rectF.centerX(), rectF.top + textSize / 2 + rectF.height() / 2, textPaint);
        } else {
            // Don't draw text for small rectangles
            if (rectF.width() > 30) {
                float textSize = Math.max(Math.max(rectF.width() / 20, rectF.height() / 20), 12);
                mTextPaint.setTextSize(textSize);
                mTextPaint.setTextAlign(Paint.Align.CENTER);

                canvas.drawText(text, rectF.centerX(), rectF.top + textSize / 2 + rectF.height() / 2, mTextPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        boolean mappableItemsChanged = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (currentRootItem.getBoundsRectF().contains(touchX, touchY) && currentMappableItems != null) {
                    if (previousMappableItems != null && currentMappableItems != previousMappableItems) {
                        currentMappableItems = previousMappableItems;
                        currentRootItem = previousRootItem;
                        mappableItemsChanged = true;
                    }
                    break;
                }

                for (AndroidMapItem rect : rectangles) {
                    if (rect.getBoundsRectF().contains(touchX, touchY)) {
                        switch (rect.getLabel()) {
                            case FRIENDS:
                                udpateTreeModel(FRIENDS);
                                mappableItemsChanged = true;
                                break;

                            case FAMILY:
                                udpateTreeModel(FAMILY);
                                mappableItemsChanged = true;
                                break;

                            case COWORKER:
                                udpateTreeModel(COWORKER);
                                mappableItemsChanged = true;
                                break;

                            default:
                                System.out.println("default");
                                break;
                        }
                    }
                }
                break;
        }

        if (mappableItemsChanged) {
            setupMappableItemsArray();
            setupMapLayout();
            invalidate();
        }
        return true;
    }

    private void udpateTreeModel(String groupName) {
        TreeModel model = new DatabaseHandler(this.getContext()).getTreeModel(groupName);
        currentRootItem = (AndroidMapItem) model.getMapItem();
        previousMappableItems = currentMappableItems;
        currentMappableItems = model.getTreeItems();
    }

    private void setupMapLayout() {
        currentRootItem.setBounds(0, 0, viewWidth, 200);

        if (currentMappableItems != null) {
            AbstractMapLayout mapLayout = new SquarifiedLayout();
            mapLayout.layout(currentMappableItems, new Rect(0, 200, viewWidth, viewHeight - 200));
        }
    }
}

