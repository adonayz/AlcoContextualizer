package edu.wpi.alcoholcontextualizer.treemap;

import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.umd.cs.treemap.MapItem;
import edu.umd.cs.treemap.Rect;

public class AndroidMapItem extends MapItem implements AndroidMappable, Comparable<AndroidMapItem> {
    private int weight;
    private String label;

    public AndroidMapItem(int weight, String label) {
        this.label = label;
        this.weight = weight;
        setSize(weight);
    }

    public String getLabel() {
        return label;
    }

    public int getWeight() {
        return weight;
    }


    /**
     * Return an Android RectF that is the size of the bounds rectangle
     */
    public RectF getBoundsRectF() {
        Rect bounds = getBounds();
        return new RectF(Double.valueOf(bounds.x).floatValue(),
                Double.valueOf(bounds.y).floatValue(),
                Double.valueOf(bounds.x).floatValue() + Double.valueOf(bounds.w).floatValue(),
                Double.valueOf(bounds.y).floatValue() + Double.valueOf(bounds.h).floatValue());
    }

    public static <T extends Comparable<? super T>> ArrayList<T> asReverseSortedList(
            Collection<T> collection) {
        ArrayList<T> arrayList = new ArrayList<T>(collection);
        Collections.sort(arrayList, Collections.reverseOrder());
        return arrayList;
    }

    @Override
    public int compareTo(AndroidMapItem otherItem) {
        return Double.compare(weight, otherItem.weight);
    }

    @Override
    public String toString() {
        return AndroidMapItem.class.getSimpleName() + "[label=" + label + ",weight=" + weight +
                ",bounds=" + getBounds().toString() +
                ",boundsRectF=" + getBoundsRectF().toString() + "]";
    }
}
