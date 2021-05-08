package suai.tests.activities.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.commonAPI;
import suai.tests.common.api.pojo.common.ItemsPOJO;
import suai.tests.common.api.testsAPI;

public class StatisticsFragment extends Fragment {

    private ItemsPOJO[] statistics;

    private TableLayout MarksTableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        root.findViewById(R.id.BackToAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).popBackStack();
            }
        });

        MarksTableLayout = root.findViewById(R.id.MarksTableLayout);

        commonAPI service = RetrofitConnection.commonAPI; Call<ItemsPOJO[]> call;
        call = service.getUserStatistics(AccountFragment.idUser, AccountFragment.role);
        call.enqueue(new Callback<ItemsPOJO[]>()
        {
            @Override
            public void onResponse(@NonNull Call<ItemsPOJO[]> call, @NonNull Response<ItemsPOJO[]> response)
            {
                ItemsPOJO[] resp = response.body();
                //String[] test = resp[0].getItems();

                if (AccountFragment.role == 1) { setStudentStatistics(root, resp); } else { setTeacherStatistics(root, resp); }
            }

            @Override
            public void onFailure(@NonNull Call<ItemsPOJO[]> call, @NonNull Throwable t)
            {
                Log.e("retrofitError", t.getMessage());
            }
        });



        return root;
    }

    private String[] exportData(ItemsPOJO[] resp, int exportIndex)
    {
        String[] items = new String[resp.length];

        for (int i = 0; i < resp.length; i++)
        {
            items[i] = resp[i].getItems()[exportIndex];
        }

        return items;
    }

    private void appendRowToTable(TableLayout tableLayout, String[] row)
    {

    }

    private TextView createTextView(LinearLayout container, String text)
    {
        TextView dataTextView = new TextView(container.getContext()); dataTextView.setPadding(4, 4, 4, 4);
        dataTextView.setGravity(Gravity.CENTER); dataTextView.setTextColor(getResources().getColor(R.color.black));
        dataTextView.setTextSize(14); dataTextView.setText(text); dataTextView.setSingleLine(false);

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

    private LayerDrawable setTableBorders(boolean left, boolean top, boolean right, boolean bottom)
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

    private TableRow createTableHeader(Context context, String[] headerData)
    {
        TableRow tableRow; Space space; LinearLayout linearLayout; TextView cell;

        tableRow = new TableRow(context); tableRow.setGravity(Gravity.CENTER);
        linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        /*space = new Space(linearLayout.getContext());
        space.setLayoutParams(
                new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT, 1f));*/
        //space.setBackground(setTableBorders(false, false, false, true));
        //linearLayout.addView(space);

        cell = createTextView(linearLayout, "");
        cell.setBackground(setTableBorders(false, false, false, true));
        linearLayout.addView(cell);

        for (int i = 0; i < headerData.length; i++)
        {
            cell = createTextView(linearLayout, headerData[i]);
            cell.setBackground(setTableBorders(true, false, false, true));
            linearLayout.addView(cell);
        }

        tableRow.addView(linearLayout);
        return tableRow;
    }

    private void setStudentStatistics(View root, ItemsPOJO[] resp)
    {
        //initializeStatisticsTable(root, resp, MarksTableLayout);

        String[] subjects, labs, marks, status;
        subjects = exportData(resp, 1);
        labs = exportData(resp, 0);
        marks = exportData(resp, 3);
        status = exportData(resp, 2);

        MarksTableLayout.setStretchAllColumns(true); MarksTableLayout.setShrinkAllColumns(true);

        TableRow tableRow; Space space; LinearLayout linearLayout; TextView cell;

        //tableRow = new TableRow(MarksTableLayout.getContext()); tableRow.setGravity(Gravity.CENTER);
        //linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        /*space = new Space(linearLayout.getContext());
        space.setLayoutParams(
                new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        linearLayout.addView(space);

        for (int i = 0; i < labs.length; i++)
        {
            linearLayout.addView(createTextView(linearLayout, labs[i]));
        }

        tableRow.addView(linearLayout); MarksTableLayout.addView(tableRow);*/

        MarksTableLayout.addView(createTableHeader(MarksTableLayout.getContext(), labs));

        String subject = resp[0].getItems()[1], currentSubject = resp[0].getItems()[1];
        tableRow = new TableRow(MarksTableLayout.getContext()); tableRow.setGravity(Gravity.CENTER);
        linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < resp.length; i++)
        {
            if (i == 0)
            {
                cell = createTextView(linearLayout, subject);
                if (subjects.length == 1) { cell.setBackground(setTableBorders(false, false, false, false)); }
                else { cell.setBackground(setTableBorders(false, false, false, true)); }
                linearLayout.addView(cell);
            }

            if (subject.equals(currentSubject))
            {
                if (resp[i].getItems()[3] == null)
                {
                    ImageView cellImage = createImageView(linearLayout,
                            R.drawable.ic_baseline_access_time_24, android.R.color.holo_orange_light);
                    if (subjects.length == 1)
                    {
                        cellImage.setBackground(setTableBorders(true, false, false, false));
                    }
                    else { cellImage.setBackground(setTableBorders(true, false, false, true)); }
                    linearLayout.addView(cellImage);
                }
                else
                {
                    cell = createTextView(linearLayout, resp[i].getItems()[3]);
                    if (subjects.length == 1) { cell.setBackground(setTableBorders(true, false, false, false)); }
                    else { cell.setBackground(setTableBorders(true, false, false, true)); }
                    linearLayout.addView(cell);
                }

                if (i == resp.length - 1 && currentSubject.equals(resp[i - 1].getItems()[1]))
                {
                    tableRow.addView(linearLayout); MarksTableLayout.addView(tableRow);
                }
            }
            else
            {
                tableRow.addView(linearLayout); MarksTableLayout.addView(tableRow);

                currentSubject = resp[i].getItems()[1];

                tableRow = new TableRow(MarksTableLayout.getContext()); tableRow.setGravity(Gravity.CENTER);
                linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                cell = createTextView(linearLayout, currentSubject);
                cell.setBackground(setTableBorders(false, true, true, false));
                linearLayout.addView(cell);
            }
        }
    }

    private void setTeacherStatistics(View root, ItemsPOJO[] resp)
    {

    }

    /*private void initializeStatisticsTable(View root, ItemsPOJO[] resp, TableLayout tableLayout)
    {
        for (int row = 0; row < resp.length; row++)
        {
            TableRow tableRow = new TableRow(tableLayout.getContext());
            LinearLayout linearLayout = new LinearLayout(tableRow.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            String[] items = resp[row].getItems();

            TextView dataTextView = new TextView(linearLayout.getContext());

            //dataTextView.setText(items[cell]); linearLayout.addView(dataTextView);

            for (int cell = 0; cell < resp[0].getItems().length; cell++)
            {
                String[] items = resp[row].getItems();

                TextView dataTextView = new TextView(linearLayout.getContext());
                dataTextView.setText(items[cell]); linearLayout.addView(dataTextView);
            }

            tableRow.addView(linearLayout); tableLayout.addView(tableRow);

            //TextView textView1 = new TextView(linearLayout.getContext());
            //TextView textView2 = new TextView(linearLayout.getContext());
            //textView1.setText(String.valueOf(i)); linearLayout.addView(textView1);
            //textView2.setText(String.valueOf(i) + " -> success"); linearLayout.addView(textView2);

            //tableRow.addView(linearLayout);
            //MarksTableLayout.addView(tableRow);
        }
    }*/
}