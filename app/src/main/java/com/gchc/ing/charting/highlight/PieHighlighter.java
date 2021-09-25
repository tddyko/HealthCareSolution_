package com.gchc.ing.charting.highlight;

import com.gchc.ing.charting.charts.PieChart;
import com.gchc.ing.charting.data.CEntry;
import com.gchc.ing.charting.interfaces.datasets.IPieDataSet;

/**
 * Created by philipp on 12/06/16.
 */
public class PieHighlighter extends PieRadarHighlighter<PieChart> {

    public PieHighlighter(PieChart chart) {
        super(chart);
    }

    @Override
    protected Highlight getClosestHighlight(int index, float x, float y) {

        IPieDataSet set = mChart.getData().getDataSet();

        final CEntry entry = set.getEntryForIndex(index);

        return new Highlight(index, entry.getY(), x, y, 0, set.getAxisDependency());
    }
}
