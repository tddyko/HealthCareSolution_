package com.gchc.ing.charting.interfaces.dataprovider;

import com.gchc.ing.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
