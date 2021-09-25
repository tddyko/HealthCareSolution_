package com.gchc.ing.charting.interfaces.dataprovider;

import com.gchc.ing.charting.components.YAxis;
import com.gchc.ing.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
