package controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;

public class StatsWindowController {

    @FXML
    private Label titleLabel;

    @FXML
    private PieChart genderPieChart;

    @FXML
    private Polygon hommeTriangle;

    @FXML
    private Label hommeLabel;

    @FXML
    private Polygon femmeTriangle;

    @FXML
    private Label femmeLabel;

    public void initializeStats(int hommeCount, int femmeCount) {
        int total = hommeCount + femmeCount;

        // Create pie chart data with exact names
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Homme", hommeCount),
                new PieChart.Data("Femme", femmeCount)
        );

        // Set data to pie chart
        genderPieChart.setData(pieChartData);

        // Show percentages in pie chart slices with a space before %
        for (PieChart.Data data : genderPieChart.getData()) {
            double percentage = (data.getPieValue() / total) * 100;
            data.nameProperty().set(data.getName() + " (" + String.format("%.0f", percentage) + " %)"); // Whole numbers with space
        }

        // Set custom colors for the pie chart (ensure correct application)
        for (PieChart.Data data : genderPieChart.getData()) {
            if ("Homme".equals(data.getName())) {
                data.getNode().setStyle("-fx-pie-color: #FF8C00;"); // Orange for Homme
            } else if ("Femme".equals(data.getName())) {
                data.getNode().setStyle("-fx-pie-color: #EAB700;"); // Yellow for Femme
            }
        }
    }
}