package com.gchc.ing.charting.interfaces.dataprovider;

import com.gchc.ing.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
