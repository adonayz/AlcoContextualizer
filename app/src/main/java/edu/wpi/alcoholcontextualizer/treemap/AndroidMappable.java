package edu.wpi.alcoholcontextualizer.treemap;

import android.graphics.RectF;

import edu.umd.cs.treemap.Mappable;

public interface AndroidMappable extends Mappable {
    public RectF getBoundsRectF();

    public String getLabel();
}
