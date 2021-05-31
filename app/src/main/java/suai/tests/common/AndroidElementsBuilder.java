package suai.tests.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import suai.tests.R;

public class AndroidElementsBuilder extends AppCompatActivity
{
    private static Context context;
    public static Context getContext() { return context; }
    public static void setContext(Context _context) { context = _context; }

    private static Resources getElementsBuilderResources() { return getContext().getResources(); }

    public static TextView createTextView(LinearLayout container, String text)
    {
        TextView dataTextView = new TextView(container.getContext()); dataTextView.setPadding(4, 4, 4, 4);
        dataTextView.setGravity(Gravity.CENTER); dataTextView.setTextColor(getElementsBuilderResources().getColor(R.color.black));
        dataTextView.setTextSize(18); dataTextView.setText(text); dataTextView.setSingleLine(false);

        dataTextView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));

        return dataTextView;
    }

    public static ImageView createImageView(Context context, int resourceID, int tintID)
    {
        ImageView dataImageView = new ImageView(context);
        dataImageView.setImageResource(resourceID);
        dataImageView.setColorFilter(ContextCompat.getColor(context, tintID), PorterDuff.Mode.SRC_IN);
        dataImageView.setPadding(4, 4, 4, 4);
        dataImageView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        return dataImageView;
    }

    public static LayerDrawable setCellBorders(boolean left, boolean top, boolean right, boolean bottom)
    {
        LayerDrawable borders = (LayerDrawable) getElementsBuilderResources().getDrawable(R.drawable.item_table_borders);
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

    public static void setTableBorders(View view, int rowsNumber, int cellsNumber, int rowIndex, int cellIndex)
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

    public static TableRow createTableHeader(Context context, String[] headerData)
    {
        TableRow tableRow; LinearLayout linearLayout; TextView cell;

        tableRow = new TableRow(context); tableRow.setGravity(Gravity.CENTER);
        linearLayout = new LinearLayout(tableRow.getContext()); linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        cell = createTextView(linearLayout, "");
        LayerDrawable borders = (LayerDrawable) getElementsBuilderResources().getDrawable(R.drawable.item_table_header_borders);
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
}
