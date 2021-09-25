package com.gchc.ing.charting.interfaces.dataprovider;

import com.gchc.ing.charting.components.YAxis.AxisDependency;
import com.gchc.ing.charting.data.BarLineScatterCandleBubbleData;
import com.gchc.ing.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
