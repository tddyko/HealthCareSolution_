package com.gchc.ing.charting.interfaces.dataprovider;

import com.gchc.ing.charting.data.SticData;

public interface SticDataProvider extends BarLineScatterCandleBubbleDataProvider {

    SticData getCandleData();
}
