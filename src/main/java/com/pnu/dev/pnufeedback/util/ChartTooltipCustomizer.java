package com.pnu.dev.pnufeedback.util;

import java.awt.Color;
import java.util.Map;
import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRChart;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;

import java.awt.Font;
import org.jfree.data.category.CategoryDataset;

public class ChartTooltipCustomizer extends JRAbstractChartCustomizer {

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

        setStakeholderColors(plot);
    }

    private void setStakeholderColors(CategoryPlot plot) {
        Map<String, String> colorMap = (Map<String, String>) getParameterValue("COLOR_MAP");
        CategoryDataset dataset = plot.getDataset();

        for (int i = 0; i < dataset.getRowCount(); i++) {
            String stakeholderCategory = dataset.getRowKey(i).toString();
            String stakeholderColor = colorMap.get(stakeholderCategory);

            if (stakeholderColor != null) {
                plot.getRenderer().setSeriesPaint(i, Color.decode(stakeholderColor));
            }
        }
    }

}
