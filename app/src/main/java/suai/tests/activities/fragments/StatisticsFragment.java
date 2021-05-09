package suai.tests.activities.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.activities.MainActivity;
import suai.tests.common.AlertDialogBuilder;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.commonAPI;
import suai.tests.common.api.pojo.common.ItemsPOJO;
import suai.tests.common.api.testsAPI;

public class StatisticsFragment extends Fragment
{
    private ItemsPOJO[] statistics;

    private Spinner StatisticsSubjectsSpinner;

    private TableLayout MarksTableLayout;

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

        StatisticsSubjectsSpinner = root.findViewById(R.id.StatisticsSubjectsSpinner);
        MarksTableLayout = root.findViewById(R.id.MarksTableLayout);

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

    private TextView createTextView(LinearLayout container, String text)
    {
        TextView dataTextView = new TextView(container.getContext()); dataTextView.setPadding(4, 4, 4, 4);
        dataTextView.setGravity(Gravity.CENTER); dataTextView.setTextColor(getResources().getColor(R.color.black));
        dataTextView.setTextSize(18); dataTextView.setText(text); dataTextView.setSingleLine(false);

        dataTextView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 128, 1f));

        return dataTextView;
    }

    private ImageView createImageView(LinearLayout container, int resourceID, int tintID)
    {
        ImageView dataImageView = new ImageView(container.getContext());
        dataImageView.setImageResource(resourceID);
        dataImageView.setColorFilter(ContextCompat.getColor(container.getContext(), tintID), PorterDuff.Mode.SRC_IN);
        dataImageView.setPadding(4, 4, 4, 4);
        dataImageView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 128, 1f));

        return dataImageView;
    }

    private LayerDrawable setCellBorders(boolean left, boolean top, boolean right, boolean bottom)
    {
        LayerDrawable borders = (LayerDrawable) getResources().getDrawable(R.drawable.item_table_borders);
        Drawable borderTop =  borders.findDrawableByLayerId(R.id.itemTableBorderTop),
                 borderBottom =  borders.findDrawableByLayerId(R.id.itemTableBorderBottom),
                 borderLeft =  borders.findDrawableByLayerId(R.id.itemTableBorderLeft),
                 borderRight =  borders.findDrawableByLayerId(R.id.itemTableBorderRight);

        if (!top) { borderTop.setAlpha(0); }
        if (!bottom) { borderBottom.setAlpha(0); }
        if (!left) { borderLeft.setAlpha(0); }
        if (!right) { borderRight.setAlpha(0); }

        return borders;
    }

    public void setTableBorders(View view, int rowsNumber, int cellsNumber, int rowIndex, int cellIndex)
    {
        if (rowIndex == 0)
        {
            if (rowsNumber == 1)
            {
                if (cellsNumber == 1) { view.setBackground(setCellBorders(false, false, false, false)); }
                else
                {
                    if (cellIndex == 0) { view.setBackground(setCellBorders(false, false, false, false)); }
                    else { view.setBackground(setCellBorders(true, false, false, false)); }
                }
            }
            else
            {
                if (cellsNumber == 1) { view.setBackground(setCellBorders(false, false, false, false)); }
                else
                {
                    if (cellIndex == 0) { view.setBackground(setCellBorders(false, false, false, true)); }
                    else { view.setBackground(setCellBorders(true, false, false, true)); }
                }
            }
        }
        else
        {
            if (rowIndex + 1 == rowsNumber)
            {
                if (cellsNumber == 1) { view.setBackground(setCellBorders(false, false, false, false)); }
                else
                {
                    if (cellIndex == 0) { view.setBackground(setCellBorders(false, false, false, false)); }
                    else { view.setBackground(setCellBorders(true, false, false, false)); }
                }
            }
            else
            {
                if (cellIndex == 0) { view.setBackground(setCellBorders(false, false, false, true)); }
                else { view.setBackground(setCellBorders(true, false, false, true)); }
            }
        }
    }

    private TableRow createTableHeader(Context context, String[] headerData)
    {
        TableRow tableRow; LinearLayout linearLayout; TextView cell;

        tableRow = new TableRow(context); tableRow.setGravity(Gravity.CENTER);
        linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        cell = createTextView(linearLayout, "");
        LayerDrawable borders = (LayerDrawable) getResources().getDrawable(R.drawable.item_table_header_borders);
        cell.setBackground(borders); linearLayout.addView(cell);

        for (int i = 0; i < headerData.length; i++)
        {
            cell = createTextView(linearLayout, headerData[i]);
            cell.setBackground(setCellBorders(true, false, false, true));
            linearLayout.addView(cell);
        }

        tableRow.addView(linearLayout);
        return tableRow;
    }

    private void setStudentStatistics(View root, ItemsPOJO[] marks)
    {
        MarksTableLayout.setStretchAllColumns(true); MarksTableLayout.setShrinkAllColumns(true);
        TableRow tableRow; LinearLayout linearLayout; ImageView cellImage = null; TextView cell; int status, cellsIndex;

        String[] titles = { "Оценка / статус" };
        MarksTableLayout.addView(createTableHeader(MarksTableLayout.getContext(), titles));

        for (int i = 0; i < marks.length; i++)
        {
            tableRow = new TableRow(MarksTableLayout.getContext()); tableRow.setGravity(Gravity.CENTER);
            linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            cellsIndex = 0; status = Integer.parseInt(marks[i].getItems()[1]);
            cell = createTextView(linearLayout, marks[i].getItems()[0]); linearLayout.addView(cell);
            setTableBorders(cell, marks.length, 2, i, cellsIndex); cellsIndex++;

            if (status == 0)
            {
                cellImage = createImageView(linearLayout,
                        R.drawable.ic_baseline_access_time_24, android.R.color.holo_orange_light);
            }

            if (status == 2)
            {
                cellImage = createImageView(linearLayout,
                        R.drawable.ic_baseline_error_24, android.R.color.holo_red_light);
            }

            if (status == 1) { cell = createTextView(linearLayout, marks[i].getItems()[2]); }

            if (status == 1) { setTableBorders(cell, marks.length, 2, i, cellsIndex); linearLayout.addView(cell); }
            else { setTableBorders(cellImage, marks.length, 2, i, cellsIndex); linearLayout.addView(cellImage); }

            tableRow.addView(linearLayout); MarksTableLayout.addView(tableRow);
        }
    }

    private void setTeacherStatistics(View root, ItemsPOJO[] resp)
    {

    }

    class CustomShapeDrawable extends ShapeDrawable {
        private final Paint fillpaint;

        public CustomShapeDrawable(Shape s, int fill) {
            super(s);
            fillpaint = new Paint(this.getPaint());
            fillpaint.setColor(fill);
        }

        @Override
        protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
            shape.draw(canvas, fillpaint);
        }
    }
}