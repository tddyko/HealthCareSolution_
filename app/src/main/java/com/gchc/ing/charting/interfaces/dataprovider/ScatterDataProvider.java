package com.gchc.ing.charting.interfaces.dataprovider;

import com.gchc.ing.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
