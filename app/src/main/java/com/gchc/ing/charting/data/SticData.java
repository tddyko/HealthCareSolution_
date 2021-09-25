package com.gchc.ing.charting.data;

import com.gchc.ing.charting.interfaces.datasets.ISticDataSet;

import java.util.List;

public class SticData extends BarLineScatterCandleBubbleData<ISticDataSet> {

    public SticData() {
        super();
    }

    public SticData(List<ISticDataSet> dataSets) {
        super(dataSets);
    }

    public SticData(ISticDataSet... dataSets) {
        super(dataSets);
    }
}
