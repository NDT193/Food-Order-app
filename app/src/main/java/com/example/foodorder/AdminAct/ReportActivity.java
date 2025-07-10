package com.example.foodorder.AdminAct;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodorder.Domain.FoodBill;
import com.example.foodorder.Domain.GeneralBill;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityReportBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {
    ActivityReportBinding binding;
    private List<GeneralBill> allBillsList = new ArrayList<>();

    private List<FoodBill> allFoodBillsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadBillsAndShowChart();
        loadFoodBillsAndShowBestFoodChart();
        setVariable();
    }

    private void setVariable() {
        binding.reportBackBtn.setOnClickListener(v -> finish());
    }

    private void loadFoodBillsAndShowBestFoodChart() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FoodBill");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allFoodBillsList.clear();
                for (DataSnapshot foodNameSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot : foodNameSnapshot.getChildren()) {
                        FoodBill foodBill = dataSnapshot.getValue(FoodBill.class);
                        if (foodBill != null) {
                            allFoodBillsList.add(foodBill);
                        }
                    }
                }
                showBestFoodChart();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void loadBillsAndShowChart() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GeneralBill");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allBillsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GeneralBill bill = dataSnapshot.getValue(GeneralBill.class);
                    if (bill != null) {
                        allBillsList.add(bill);
                    }
                }
                showRevenueChart();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void showRevenueChart() {
        Map<String, Long> revenueByDate = new HashMap<>();
        for (GeneralBill bill : allBillsList) {
            String date = bill.getDate();
            long price = (long) bill.getPrice();
            revenueByDate.put(date, revenueByDate.getOrDefault(date, 0L) + price);
        }

        List<String> dates = new ArrayList<>(revenueByDate.keySet());
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            entries.add(new BarEntry(i, revenueByDate.get(dates.get(i))));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Revenue by Day");
        dataSet.setValueTextSize(18f);
        BarData barData = new BarData(dataSet);

        BarChart barChart = findViewById(R.id.totalByDayChart);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dates));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getDescription().setText("Total Revenue per Day");
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void showBestFoodChart() {
        Map<String, Integer> foodCount = new HashMap<>();
        for (FoodBill foodBill : allFoodBillsList) {
            String foodName = foodBill.getFoodTittle();
            int quantity = foodBill.getQuantity();
            if (foodName == null) foodName = "Unknown";
            foodCount.put(foodName, foodCount.getOrDefault(foodName, 0) + quantity);
        }

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : foodCount.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Best-selling Foods");
        dataSet.setValueTextSize(16f);

        // Set different colors for each food
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#F44336")); // Red
        colors.add(Color.parseColor("#2196F3")); // Blue
        colors.add(Color.parseColor("#4CAF50")); // Green
        colors.add(Color.parseColor("#FFEB3B")); // Yellow
        colors.add(Color.parseColor("#9C27B0")); // Purple
        colors.add(Color.parseColor("#FF9800")); // Orange
        // Add more colors if needed

        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);

        PieChart pieChart = findViewById(R.id.bestFoodChart);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setText("Quantity Sold per Food");
        pieChart.setEntryLabelTextSize(12f);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}