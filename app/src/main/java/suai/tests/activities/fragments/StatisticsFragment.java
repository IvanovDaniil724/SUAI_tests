package suai.tests.activities.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.common.AlertDialogBuilder;
import suai.tests.common.AndroidElementsBuilder;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.commonAPI;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public class StatisticsFragment extends Fragment
{
    private Spinner StatisticsSubjectsSpinner, TeacherGroupsSpinner;

    private TableLayout MarksTableLayout, TeacherAverageGroupMarksTableLayout, TeacherGroupMarksTableLayout;
    private PieChart StatisticsPieChart; private BarChart StatisticsTeacherGroupMarksBarChart;

    private TextView StatisticsStudentAverageMark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        if (AccountFragment.role == 1) { root.findViewById(R.id.StatisticsStudentScrollView).setVisibility(View.VISIBLE); }
        else { root.findViewById(R.id.StatisticsTeacherScrollView).setVisibility(View.VISIBLE); }

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
        TeacherGroupsSpinner = root.findViewById(R.id.TeacherGroupsSpinner);
        MarksTableLayout = root.findViewById(R.id.StudentMarksTableLayout);
        TeacherAverageGroupMarksTableLayout = root.findViewById(R.id.TeacherAverageGroupMarksTableLayout);
        TeacherGroupMarksTableLayout = root.findViewById(R.id.TeacherGroupMarksTableLayout);

        StatisticsPieChart = root.findViewById(R.id.StatisticsStudentMarksPieChart);
        StatisticsTeacherGroupMarksBarChart = root.findViewById(R.id.StatisticsTeacherGroupMarksBarChart);

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

        AdapterView.OnItemSelectedListener itemSubjectSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String subject = (String) parent.getItemAtPosition(position);

                //root.findViewById(R.id.StatisticsTeacherGroupSpinnerLinearLayout).setVisibility(View.VISIBLE);
                if (AccountFragment.role == 1) { setStudentStatistics(root, subject); } else { setTeacherStatistics(root, context); }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        };
        StatisticsSubjectsSpinner.setOnItemSelectedListener(itemSubjectSelectedListener);
    }

    private void setStudentStatistics(View root, String subject)
    {
        MarksTableLayout.removeAllViews();

        commonAPI service = RetrofitConnection.commonAPI; Call<ItemsPOJO[]> call;
        call = service.getUserStatistics(AccountFragment.idUser, AccountFragment.role, subject);
        call.enqueue(new Callback<ItemsPOJO[]>()
        {
            @Override
            public void onResponse(@NonNull Call<ItemsPOJO[]> call, @NonNull Response<ItemsPOJO[]> response)
            {
                ItemsPOJO[] marks = response.body();

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
                else if (averageMark < 3) { colorID = android.R.color.holo_red_dark; }
                else if (averageMark < 4) { colorID = android.R.color.holo_red_light; }
                else if (averageMark < 5) { colorID = android.R.color.holo_green_dark; }
                else if (averageMark == 5) { colorID = android.R.color.holo_green_light; }
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

            @Override
            public void onFailure(@NonNull Call<ItemsPOJO[]> call, @NonNull Throwable t)
            {
                Log.e("retrofitError", t.getMessage());
            }
        });
    }

    private void setTeacherStatistics(View root, Context context)
    {
        TeacherAverageGroupMarksTableLayout.removeAllViews();

        commonAPI service = RetrofitConnection.commonAPI; Call<ItemsPOJO[]> call;
        call = service.getTeacherGroupsAverageMarks(AccountFragment.idUser, StatisticsSubjectsSpinner.getSelectedItem().toString());
        call.enqueue(new Callback<ItemsPOJO[]>()
        {
            @Override
            public void onResponse(@NonNull Call<ItemsPOJO[]> call, @NonNull Response<ItemsPOJO[]> response)
            {
                ItemsPOJO[] averageMarks = response.body();

                TeacherAverageGroupMarksTableLayout.setStretchAllColumns(true); TeacherAverageGroupMarksTableLayout.setShrinkAllColumns(true);
                TableRow tableRow; LinearLayout linearLayout; TextView cell; int colorID = 0, x = 0;

                tableRow = new TableRow(TeacherAverageGroupMarksTableLayout.getContext()); tableRow.setGravity(Gravity.CENTER);
                linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                cell = AndroidElementsBuilder.createTextView(linearLayout, "Группа");
                cell.setTextSize(20); cell.setTypeface(Typeface.DEFAULT_BOLD);
                cell.setTextColor(getResources().getColor(R.color.suai_secondary)); linearLayout.addView(cell);
                AndroidElementsBuilder.setTableBorders(cell, averageMarks.length, 2, 0, 0);

                cell = AndroidElementsBuilder.createTextView(linearLayout, "Средний балл");
                cell.setTextSize(20); cell.setTypeface(Typeface.DEFAULT_BOLD);
                cell.setTextColor(getResources().getColor(R.color.suai_secondary)); linearLayout.addView(cell);
                AndroidElementsBuilder.setTableBorders(cell, averageMarks.length, 2, 0, 1);

                tableRow.addView(linearLayout); TeacherAverageGroupMarksTableLayout.addView(tableRow);

                BarData data = new BarData();
                for (int i = 0; i < averageMarks.length; i++)
                {
                    tableRow = new TableRow(TeacherAverageGroupMarksTableLayout.getContext()); tableRow.setGravity(Gravity.CENTER);
                    linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                    String group = averageMarks[i].getItems()[0], averageMark = averageMarks[i].getItems()[1];
                    if (!averageMark.equals("0"))
                    {
                        ArrayList<BarEntry> barChartEntries = new ArrayList<>();
                        barChartEntries.add(new BarEntry(x, Float.parseFloat(averageMark)));
                        BarDataSet dataSet = new BarDataSet(barChartEntries, group);
                        if (Double.parseDouble(averageMark) == 0) { colorID = android.R.color.secondary_text_dark; }
                        else if (Double.parseDouble(averageMark) < 2.6) { colorID = android.R.color.holo_red_dark; }
                        else if (Double.parseDouble(averageMark) < 3.6) { colorID = android.R.color.holo_red_light; }
                        else if (Double.parseDouble(averageMark) < 4.6) { colorID = android.R.color.holo_green_dark; }
                        else if (Double.parseDouble(averageMark) <= 5) { colorID = android.R.color.holo_green_light; }
                        dataSet.setColor(getResources().getColor(colorID));
                        dataSet.setValueTextColor(getResources().getColor(R.color.suai_secondary));
                        dataSet.setValueTextSize(18); x++; data.addDataSet(dataSet);
                        StatisticsTeacherGroupMarksBarChart.setData(data);
                    }

                    cell = AndroidElementsBuilder.createTextView(linearLayout, group);
                    AndroidElementsBuilder.setTableBorders(cell, averageMarks.length, 2, i, 0);
                    cell.setTextColor(getResources().getColor(R.color.suai_primary));
                    cell.setTypeface(Typeface.DEFAULT_BOLD); linearLayout.addView(cell);

                    cell = AndroidElementsBuilder.createTextView(linearLayout, averageMark);
                    AndroidElementsBuilder.setTableBorders(cell, averageMarks.length, 2, i, 1);

                    if (Double.parseDouble(averageMark) == 0) { colorID = android.R.color.secondary_text_dark; }
                    else if (Double.parseDouble(averageMark) < 2.6) { colorID = android.R.color.holo_red_dark; }
                    else if (Double.parseDouble(averageMark) < 3.6) { colorID = android.R.color.holo_red_light; }
                    else if (Double.parseDouble(averageMark) < 4.6) { colorID = android.R.color.holo_green_dark; }
                    else if (Double.parseDouble(averageMark) <= 5) { colorID = android.R.color.holo_green_light; }
                    cell.setTextColor(getResources().getColor(colorID)); linearLayout.addView(cell);

                    tableRow.addView(linearLayout); TeacherAverageGroupMarksTableLayout.addView(tableRow);
                }

                if (x == 0) { StatisticsTeacherGroupMarksBarChart.setVisibility(View.GONE); }
                else
                {
                    StatisticsTeacherGroupMarksBarChart.setVisibility(View.VISIBLE);

                    Description chartDescription = new Description(); chartDescription.setTextSize(12);
                    chartDescription.setTextColor(getResources().getColor(R.color.suai_secondary));
                    chartDescription.setText("Гистограмма среднего балла по группам");
                    StatisticsTeacherGroupMarksBarChart.setDescription(chartDescription);

                    StatisticsTeacherGroupMarksBarChart.getXAxis().setEnabled(false);
                    StatisticsTeacherGroupMarksBarChart.getAxisRight().setEnabled(false);
                    StatisticsTeacherGroupMarksBarChart.getAxisLeft().setAxisMaximum((float) 5.1);
                    StatisticsTeacherGroupMarksBarChart.getAxisLeft().setAxisMinimum((float) 1);
                    StatisticsTeacherGroupMarksBarChart.getAxisLeft().setZeroLineColor(getResources().getColor(R.color.suai_primary));
                    StatisticsTeacherGroupMarksBarChart.getAxisLeft().setTextColor(getResources().getColor(R.color.suai_primary));
                    StatisticsTeacherGroupMarksBarChart.getXAxis().setGridColor(getResources().getColor(R.color.suai_primary));
                    StatisticsTeacherGroupMarksBarChart.getXAxis().setTextColor(getResources().getColor(R.color.suai_primary));

                    Legend legend = StatisticsTeacherGroupMarksBarChart.getLegend();
                    legend.setDrawInside(false); legend.setWordWrapEnabled(true);
                    legend.setTextColor(getResources().getColor(R.color.suai_primary));

                    StatisticsTeacherGroupMarksBarChart.animateXY(1000,1000);
                    StatisticsTeacherGroupMarksBarChart.invalidate();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ItemsPOJO[]> call, @NonNull Throwable t)
            {
                Log.e("retrofitError", t.getMessage());
            }
        });

        call = service.getTeacherGroups(AccountFragment.idUser, StatisticsSubjectsSpinner.getSelectedItem().toString());
        call.enqueue(new Callback<ItemsPOJO[]>()
        {
            @Override
            public void onResponse(@NonNull Call<ItemsPOJO[]> call, @NonNull Response<ItemsPOJO[]> response)
            {
                ItemsPOJO[] resp = response.body(); String[] groups = new String[resp.length];
                for (int i = 0; i < resp.length; i++) { groups[i] = resp[i].getItems()[0]; }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_style, groups);
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_style);
                TeacherGroupsSpinner.setAdapter(spinnerAdapter);

                AdapterView.OnItemSelectedListener itemGroupSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        TeacherGroupMarksTableLayout.removeAllViews();

                        commonAPI service = RetrofitConnection.commonAPI; Call<ItemsPOJO[]> call;
                        call = service.getGroupMarks(StatisticsSubjectsSpinner.getSelectedItem().toString(),
                                                     TeacherGroupsSpinner.getSelectedItem().toString());
                        call.enqueue(new Callback<ItemsPOJO[]>()
                        {
                            @Override
                            public void onResponse(@NonNull Call<ItemsPOJO[]> call, @NonNull Response<ItemsPOJO[]> response)
                            {
                                ItemsPOJO[] marks = response.body(); int colorID = 0;

                                if (marks.length > 1)
                                {
                                    TeacherGroupMarksTableLayout.setStretchAllColumns(true);
                                    //TeacherGroupMarksTableLayout.setShrinkAllColumns(true); int status, cellsIndex = 0;
                                    TableRow tableRow; LinearLayout linearLayout; ImageView cellImage = null; TextView cell;

                                    //root.findViewById(R.id.StatisticsTeacherGroupSpinnerLinearLayout).setVisibility(View.VISIBLE);

                                    Arrays.sort(marks[0].getItems(), Collections.reverseOrder());

                                    String[] students = marks[0].getItems(), labs = marks[1].getItems();
                                    //Arrays.sort(students, Collections.reverseOrder());

                                    //Arrays.sort(labs, Collections.reverseOrder());

                                    //String[] labs = marks[1].getItems();
                                    //List<String> list = Arrays.asList(marks[0].getItems());
                                    //List<String> reversed = Lists.reverse(list);
                                    //String[] students = (String[]) reversed.toArray();

                                    for (int i = 0; i < marks[1].getItems().length; i++)
                                    {
                                        //List<String> list = Arrays.asList(marks[0].getItems());
                                        //List<String> reversed = Lists.reverse(list);
                                        //String[] students = reversed.toArray();

                                        //if (labs.length > 0) { new AlertDialogBuilder(root.getContext()).alert("", labs[i]); }
                                        //new AlertDialogBuilder(root.getContext()).alert("", students[i]);
                                        //students[i] = marks[0].getItems()[i];
                                    }

                                    //for (int i = 0; students.length > i; i++)
                                    //{ new AlertDialogBuilder(root.getContext()).alert("", students[i]); }

                                    //String currentLab = marks[0].getItems()[0], currentStudent = marks[0].getItems()[1];
                                    //int testsCount = Integer.parseInt(marks[2].getItems()[3]),
                                    //    studentsCount = Integer.parseInt(marks[2].getItems()[4]);

                                    tableRow = new TableRow(TeacherGroupMarksTableLayout.getContext());
                                    tableRow.setGravity(Gravity.CENTER);
                                    linearLayout = new LinearLayout(tableRow.getContext());
                                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                                    cell = AndroidElementsBuilder.createTextView(linearLayout, "");
                                    LayerDrawable borders = (LayerDrawable) getResources().getDrawable(R.drawable.item_table_header_borders);
                                    cell.setBackground(borders); linearLayout.addView(cell);

                                    /*for (int i = 0; i < marks.length; i++)
                                    {
                                        if (!currentLab.equals(marks[i].getItems()[0]) || i == 0)
                                        {
                                            cell = AndroidElementsBuilder.createTextView(linearLayout, marks[i].getItems()[0]);
                                            AndroidElementsBuilder.setTableBorders(cell, studentsCount, testsCount, 0, cellsIndex);
                                            linearLayout.addView(cell); cellsIndex++; currentLab = marks[i].getItems()[0];
                                        }
                                    }*/

                                    //Arrays.sort(labs);
                                    for (int i = 0; i < labs.length; i++)
                                    {
                                        cell = AndroidElementsBuilder.createTextView(linearLayout, labs[i]);
                                        AndroidElementsBuilder.setTableBorders(cell,
                                                students.length + 1, labs.length + 1, 0, i + 1);
                                        linearLayout.addView(cell); //cellsIndex++; //currentLab = marks[i].getItems()[0];
                                    }
                                    tableRow.addView(linearLayout); TeacherGroupMarksTableLayout.addView(tableRow);

                                    for (int i = 0; i < students.length; i++)
                                    {
                                        tableRow = new TableRow(TeacherGroupMarksTableLayout.getContext());
                                        tableRow.setGravity(Gravity.CENTER);
                                        linearLayout = new LinearLayout(tableRow.getContext());
                                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                                        for (int j = 0; j < labs.length + 1; j++)
                                        {
                                            //new AlertDialogBuilder(root.getContext()).alert("", students[i]);
                                            //Arrays.sort(students);
                                            if (j == 0) { cell = AndroidElementsBuilder.createTextView(linearLayout, students[i]); }
                                            else
                                            {
                                                for (int ii = 2; ii < marks.length; ii++)
                                                {
                                                    //new AlertDialogBuilder(root.getContext()).alert("",
                                                    //    String.valueOf(marks[ii].getItems()[0].equals(labs[j - 1])) + " && " +
                                                    //            String.valueOf(marks[ii].getItems()[1].equals(students[i])));

                                                    //Arrays.sort(students);
                                                    new AlertDialogBuilder(root.getContext()).alert("",
                                                    String.valueOf(marks[ii].getItems()[0]) + " && " +
                                                             String.valueOf(marks[ii].getItems()[1]) + "\n\n" +
                                                            labs[j - 1] + " && " + students[i]);

                                                    //new AlertDialogBuilder(root.getContext()).alert("", students[i]);

                                                    if (marks[ii].getItems()[0].equals(labs[j - 1]) &&
                                                        marks[ii].getItems()[1].equals(students[i]))
                                                    {
                                                        cell = AndroidElementsBuilder.createTextView(linearLayout, marks[ii].getItems()[2]);
                                                        if (Double.parseDouble(marks[ii].getItems()[2]) < 2.6)
                                                            { colorID = android.R.color.holo_red_dark; }
                                                        else if (Double.parseDouble(marks[ii].getItems()[2]) < 3.6)
                                                            { colorID = android.R.color.holo_red_light; }
                                                        else if (Double.parseDouble(marks[ii].getItems()[2]) < 4.6)
                                                            { colorID = android.R.color.holo_green_dark; }
                                                        else if (Double.parseDouble(marks[ii].getItems()[2]) <= 5)
                                                            { colorID = android.R.color.holo_green_light; }
                                                    }
                                                    else
                                                    {
                                                        cell = AndroidElementsBuilder.createTextView(linearLayout, "-");
                                                        colorID = android.R.color.secondary_text_dark; cell.setTypeface(Typeface.DEFAULT_BOLD);
                                                        cell.setTextColor(getResources().getColor(R.color.suai_primary));

                                                    }
                                                    cell.setTextColor(getResources().getColor(colorID));
                                                }
                                            }

                                            AndroidElementsBuilder.setTableBorders(cell,
                                                    students.length + 1, labs.length + 1, i + 1, j);
                                            linearLayout.addView(cell);
                                        }

                                        tableRow.addView(linearLayout); TeacherGroupMarksTableLayout.addView(tableRow);
                                    }

                                    //TeacherGroupMarksTableLayout.getChildAt(1);

                                    /*for (int i = 0; i < marks.length; i++)
                                    {
                                        tableRow = new TableRow(TeacherGroupMarksTableLayout.getContext());
                                        tableRow.setGravity(Gravity.CENTER);
                                        linearLayout = new LinearLayout(tableRow.getContext());
                                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                                        for (int j = 0; j < marks.length; j++)
                                        {
                                            if (j == 0)
                                            {
                                                if (!currentStudent.equals(marks[i].getItems()[1]))
                                                {
                                                    currentStudent = marks[i].getItems()[1];
                                                }

                                                cell = AndroidElementsBuilder.createTextView(linearLayout, marks[i].getItems()[1]);
                                                AndroidElementsBuilder.setTableBorders(cell, studentsCount, testsCount, i + 1, j);
                                                linearLayout.addView(cell); //cellsIndex++;
                                                //tableRow.addView(linearLayout); TeacherGroupMarksTableLayout.addView(tableRow);
                                            }
                                            else
                                            {
                                                cell = AndroidElementsBuilder.createTextView(linearLayout, "");
                                                AndroidElementsBuilder.setTableBorders(cell, studentsCount, testsCount, i + 1, j);
                                                linearLayout.addView(cell); //cellsIndex++;
                                                //currentStudent = marks[i].getItems()[1];
                                            }*/



                                            /*if (!currentStudent.equals(marks[i].getItems()[1]))
                                            {

                                            }
                                            else
                                            {
                                                cell = AndroidElementsBuilder.createTextView(linearLayout, "");
                                                AndroidElementsBuilder.setTableBorders(cell, studentsCount, testsCount, i + 1, j);
                                                linearLayout.addView(cell); //cellsIndex++;
                                                //currentStudent = marks[i].getItems()[1];
                                            }*/
                                        //}

                                        //tableRow.addView(linearLayout); TeacherGroupMarksTableLayout.addView(tableRow);
                                    //}
                                }

                                /*for (int i = 0; i < marks.length; i++)
                                {
                                    tableRow = new TableRow(TeacherGroupMarksTableLayout.getContext()); tableRow.setGravity(Gravity.CENTER);
                                    linearLayout = new LinearLayout(tableRow.getContext());
                                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                                    if (i == 0)
                                    {
                                        cell = AndroidElementsBuilder.createTextView(linearLayout, "");
                                        LayerDrawable borders =
                                                (LayerDrawable) getResources().getDrawable(R.drawable.item_table_header_borders);
                                        cell.setBackground(borders); linearLayout.addView(cell); cellsIndex++;*/

                                        /*for (int j = 0; j < testsCount * studentsCount; j += studentsCount)
                                        {
                                            cell = AndroidElementsBuilder.createTextView(linearLayout, marks[j].getItems()[0]);
                                            AndroidElementsBuilder.setTableBorders(cell, studentsCount, testsCount, 0, cellsIndex);
                                            linearLayout.addView(cell); cellsIndex++;
                                        }*/

                                        //tableRow.addView(linearLayout); TeacherGroupMarksTableLayout.addView(tableRow);

                                        /*for (int j = 1; j < testsCount; j++)
                                        {
                                            if (j == 1)
                                            {
                                                cell = AndroidElementsBuilder.createTextView(linearLayout, marks[i].getItems()[1]);
                                                AndroidElementsBuilder.setTableBorders(cell, studentsCount, testsCount, i-1, cellsIndex);
                                                linearLayout.addView(cell); cellsIndex++;

                                                cell = AndroidElementsBuilder.createTextView(linearLayout, marks[j].getItems()[2]);
                                                AndroidElementsBuilder.setTableBorders(cell, studentsCount, testsCount, i-1, cellsIndex);
                                                linearLayout.addView(cell); cellsIndex++;
                                            }
                                            else
                                            {

                                            }
                                        }*/

                                        //continue;
                                    //}

                                    //tableRow.addView(linearLayout); TeacherGroupMarksTableLayout.addView(tableRow);
                                //}
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
                TeacherGroupsSpinner.setOnItemSelectedListener(itemGroupSelectedListener);
            }

            @Override
            public void onFailure(@NonNull Call<ItemsPOJO[]> call, @NonNull Throwable t)
            {
                Log.e("retrofitError", t.getMessage());
            }
        });
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