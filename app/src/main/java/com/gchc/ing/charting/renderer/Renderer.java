
package com.gchc.ing.charting.renderer;


import com.gchc.ing.charting.utils.ViewPortHandler;
import com.gchc.ing.util.ChartTimeUtil;

/**
 * Abstract baseclass of all Renderers.
 * 
 * @author Philipp Jahoda
 */
public abstract class Renderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }

    public void setTimeClass(ChartTimeUtil timeClass) {
    }
}
