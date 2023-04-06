package com.pnu.dev.pnufeedback.util;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;

import java.awt.Font;

public class ChartTooltipCustomizer implements JRChartCustomizer {

    @Override
    public void customize(JFreeChart chart, JRChart jasperChart) {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        Font font = new Font("Calibri", Font.BOLD, 10);

        plot.getDomainAxis().setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 2.25)
        );

        barRenderer.setBaseItemLabelFont(font);
        barRenderer.setBaseItemLabelsVisible(true);
        plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(1.0f);

        plot.getDomainAxis().setMaximumCategoryLabelLines(2);
        plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin() + 0.1);
        barRenderer.setItemMargin(0.0f);
    }
}
