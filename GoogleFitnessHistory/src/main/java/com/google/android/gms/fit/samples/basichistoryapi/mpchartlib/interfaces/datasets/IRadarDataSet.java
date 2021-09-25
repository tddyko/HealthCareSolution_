package com.google.android.gms.fit.samples.basichistoryapi.mpchartlib.interfaces.datasets;

import com.google.android.gms.fit.samples.basichistoryapi.mpchartlib.data.RadarChartEntry;

/**
 * Created by Philipp Jahoda on 03/11/15.
 */
public interface IRadarDataSet extends ILineRadarDataSet<RadarChartEntry> {

    /// flag indicating whether highlight circle should be drawn or not
    boolean isDrawHighlightCircleEnabled();

    /// Sets whether highlight circle should be drawn or not
    void setDrawHighlightCircleEnabled(boolean enabled);

    int getHighlightCircleFillColor();

    /// The stroke color for highlight circle.
    /// If Utils.COLOR_NONE, the color of the dataset is taken.
    int getHighlightCircleStrokeColor();

    int getHighlightCircleStrokeAlpha();

    float getHighlightCircleInnerRadius();

    float getHighlightCircleOuterRadius();

    float getHighlightCircleStrokeWidth();

}
