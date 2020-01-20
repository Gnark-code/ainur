package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.Chart;
import org.jetbrains.annotations.NotNull;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Oscilloscope {
    private final List<Chart> charts;

    public Oscilloscope() {
        this.charts = new ArrayList<>();
    }

    public void addChart(final String title, final String xTitle, final String yTitle, final double[] data) {
        this.charts.add(Chart.builder()
                .title(title)
                .xTitle(xTitle)
                .yTitle(yTitle)
                .data(data)
                .build());
    }


    public void displayAll() {
        this.charts.stream()
                .map(this::mapChart)
                .collect(Collectors.toList())
                .forEach(chart -> new SwingWrapper<>(chart).displayChart());
    }

    @NotNull
    private XYChart mapChart(final Chart chart) {
        final double[] xData = new double[chart.getData().length];
        for (int i = 0; i < chart.getData().length; i++) {
            xData[i] = i;
        }

        final XYChart c = new XYChart(1200, 800);
        c.setTitle(chart.getTitle());
        c.setXAxisTitle(chart.getXTitle());
        c.setYAxisTitle(chart.getYTitle());
        c.addSeries(chart.getTitle(), xData, chart.getData());
        return c;
    }

}
