package suai.tests.activities.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.common.AndroidElementsBuilder;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.commonAPI;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public class StatisticsFragment extends Fragment
{
    private ItemsPOJO[] statistics;

    private Spinner StatisticsSubjectsSpinner;

    private TableLayout MarksTableLayout;
    private PieChart StatisticsPieChart;

    private TextView StatisticsStudentAverageMark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        root.findViewById(R.id.BackToAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomNavigationView navBar = getActivity().findViewById(R.id.nav_view); navBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(root).popBackStack();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                BottomNavigationView navBar = getActivity().findViewById(R.id.nav_view); navBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(root).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        BottomNavigationView navBar = getActivity().findViewById(R.id.nav_view); navBar.setVisibility(View.GONE);

        StatisticsSubjectsSpinner = root.findViewById(R.id.StatisticsSpinner);
        MarksTableLayout = root.findViewById(R.id.StudentMarksTableLayout);
        StatisticsPieChart = root.findViewById(R.id.StatisticsStudentMarksPieChart);

        StatisticsStudentAverageMark = root.findViewById(R.id.StatisticsStudentAverageMark);

        commonAPI service = RetrofitConnection.commonAPI; Call<ItemsPOJO[]> call;

        call = service.getUserSubjects(AccountFragment.idUser, AccountFragment.role);
        call.enqueue(new Callback<ItemsPOJO[]>()
        {
            @Override
            public void onResponse(@NonNull Call<ItemsPOJO[]> call, @NonNull Response<ItemsPOJO[]> response)
            {
                ItemsPOJO[] subjects = response.body(); setUserSubjects(root.getContext(), root, subjects);
            }

            @Override
            public void onFailure(@NonNull Call<ItemsPOJO[]> call, @NonNull Throwable t)
            {
                Log.e("retrofitError", t.getMessage());
            }
        });

        return root;
    }

    private void setUserSubjects(Context context, View root, ItemsPOJO[] resp)
    {
        String[] subjects = new String[resp.length];
        for (int i = 0; i < resp.length; i++) { subjects[i] = resp[i].getItems()[1]; }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_style, subjects);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_style);
        StatisticsSubjectsSpinner.setAdapter(spinnerAdapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Получаем выбранный объект
                String subject = (String) parent.getItemAtPosition(position);

                MarksTableLayout.removeAllViews();

                commonAPI service = RetrofitConnection.commonAPI; Call<ItemsPOJO[]> call;
                call = service.getUserStatistics(AccountFragment.idUser, AccountFragment.role, subject);
                call.enqueue(new Callback<ItemsPOJO[]>()
                {
                    @Override
                    public void onResponse(@NonNull Call<ItemsPOJO[]> call, @NonNull Response<ItemsPOJO[]> response)
                    {
                        ItemsPOJO[] marks = response.body();
                        //String[] test = resp[0].getItems();

                        if (AccountFragment.role == 1) { setStudentStatistics(root, marks); } else { setTeacherStatistics(root, marks); }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ItemsPOJO[]> call, @NonNull Throwable t)
                    {
                        Log.e("retrofitError", t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        };
        StatisticsSubjectsSpinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void setStudentStatistics(View root, ItemsPOJO[] marks)
    {
        MarksTableLayout.setStretchAllColumns(true); MarksTableLayout.setShrinkAllColumns(true);
        TableRow tableRow; LinearLayout linearLayout; ImageView cellImage = null; TextView cell; int status, cellsIndex;

        String[] titles = { "Оценка / статус" };
        MarksTableLayout.addView(AndroidElementsBuilder.createTableHeader(MarksTableLayout.getContext(), titles));

        int inProgressCounter = 0, errorsCounter = 0, mark_2_counter = 0, mark_3_counter = 0, mark_4_counter = 0, mark_5_counter = 0;

        for (int i = 0; i < marks.length; i++)
        {
            tableRow = new TableRow(MarksTableLayout.getContext()); tableRow.setGravity(Gravity.CENTER);
            linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            cellsIndex = 0; status = Integer.parseInt(marks[i].getItems()[1]);
            cell = AndroidElementsBuilder.createTextView(linearLayout, marks[i].getItems()[0]); linearLayout.addView(cell);
            AndroidElementsBuilder.setTableBorders(cell, marks.length, 2, i, cellsIndex); cellsIndex++;

            if (status == 0)
            {
                cellImage = AndroidElementsBuilder.createImageView(linearLayout.getContext(),
                        R.drawable.ic_baseline_access_time_24, android.R.color.holo_orange_light);
                inProgressCounter++;
            }

            if (status == 2)
            {
                cellImage = AndroidElementsBuilder.createImageView(linearLayout.getContext(),
                        R.drawable.ic_baseline_error_24, android.R.color.holo_red_light);
                errorsCounter++;
            }

            if (status == 1)
            {
                cell = AndroidElementsBuilder.createTextView(linearLayout, marks[i].getItems()[2]);
                if (marks[i].getItems()[2].equals("2")) { mark_2_counter++; }
                if (marks[i].getItems()[2].equals("3")) { mark_3_counter++; }
                if (marks[i].getItems()[2].equals("4")) { mark_4_counter++; }
                if (marks[i].getItems()[2].equals("5")) { mark_5_counter++; }
            }

            if (status == 1)
            {
                AndroidElementsBuilder.setTableBorders(cell, marks.length, 2, i, cellsIndex);
                linearLayout.addView(cell);
            }
            else
            {
                AndroidElementsBuilder.setTableBorders(cellImage, marks.length, 2, i, cellsIndex);
                linearLayout.addView(cellImage);
            }

            tableRow.addView(linearLayout); MarksTableLayout.addView(tableRow);
        }

        int averageMark = ((2 * mark_2_counter) + (3 * mark_3_counter) +
                           (4 * mark_4_counter) + (5 * mark_5_counter)) /
                           (mark_2_counter + mark_3_counter + mark_4_counter + mark_5_counter);
        StatisticsStudentAverageMark.setText(String.valueOf(averageMark)); int colorID = android.R.color.secondary_text_dark;
        if (averageMark < 2) { StatisticsStudentAverageMark.setText("Нет принятых лабораторных работ"); }
        if (averageMark < 3) { colorID = android.R.color.holo_red_dark; }
        if (averageMark < 4) { colorID = android.R.color.holo_red_light; }
        if (averageMark < 5) { colorID = android.R.color.holo_green_dark; }
        if (averageMark == 5) { colorID = android.R.color.holo_green_light; }
        StatisticsStudentAverageMark.setTextColor(getResources().getColor(colorID));

        ArrayList<PieChartItem> pieChartDataArrayList = new ArrayList<PieChartItem>();
        if (inProgressCounter != 0) { pieChartDataArrayList.add(new PieChartItem("В прогрессе", inProgressCounter)); }
        if (errorsCounter != 0) { pieChartDataArrayList.add(new PieChartItem("Ошибки", errorsCounter)); }
        if (mark_2_counter != 0) { pieChartDataArrayList.add(new PieChartItem("Неудовлетворительно (2)", mark_2_counter)); }
        if (mark_3_counter != 0) { pieChartDataArrayList.add(new PieChartItem("Удовлетворительно (3)", mark_3_counter)); }
        if (mark_4_counter != 0) { pieChartDataArrayList.add(new PieChartItem("Хорошо (4)", mark_4_counter)); }
        if (mark_5_counter != 0) { pieChartDataArrayList.add(new PieChartItem("Отлично (5)", mark_5_counter)); }

        ArrayList<PieEntry> pieChartEntries = new ArrayList<PieEntry>();
        for (int i = 0; i < pieChartDataArrayList.size(); i++)
        {
            pieChartEntries.add(new PieEntry(pieChartDataArrayList.get(i).getMarksCount(),
                                             pieChartDataArrayList.get(i).getTitle()));
        }

        PieDataSet pieChartDataSet = new PieDataSet(pieChartEntries, "Количество оценок или статусов по данной дисциплине");
        pieChartDataSet.setColors(ColorTemplate.COLORFUL_COLORS); pieChartDataSet.setValueTextSize(16);
        pieChartDataSet.setValueTextColor(R.color.suai_primary);
        pieChartDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChartDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(pieChartDataSet);
        Description chartDescription = new Description(); chartDescription.setText("");
        StatisticsPieChart.setDescription(chartDescription); StatisticsPieChart.setData(data);

        Legend legend = StatisticsPieChart.getLegend();
        legend.setTextSize(12); legend.setDrawInside(false); legend.setWordWrapEnabled(true);
        legend.setTextColor(getResources().getColor(R.color.suai_secondary));
        legend.setXEntrySpace(128);


        StatisticsPieChart.setCenterText("Оценки по предмету \"" + StatisticsSubjectsSpinner.getSelectedItem().toString() + "\"");
        StatisticsPieChart.setEntryLabelColor(R.color.suai_primary);
        StatisticsPieChart.animateXY(1000,1000); StatisticsPieChart.invalidate();
    }

    private void setTeacherStatistics(View root, ItemsPOJO[] resp)
    {

    }

    private static class PieChartItem {
        String title; int marksCount;

        public PieChartItem(String title, int marksCount) { this.title = title; this.marksCount = marksCount; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public int getMarksCount() { return marksCount; }
        public void setMarksCount(int marksCount) { this.marksCount = marksCount; }
    }
}