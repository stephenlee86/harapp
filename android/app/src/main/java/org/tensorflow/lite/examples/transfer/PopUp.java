package org.tensorflow.lite.examples.transfer;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PopUp {
    boolean focus;

    Button buttonOkay;

    int width;
    int height;

    LayoutInflater inflater;

    View popupView;

    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.pop_up, null);
        width = LinearLayout.LayoutParams.MATCH_PARENT;
        height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focus = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focus);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        TextView test2 = popupView.findViewById(R.id.DataReleased);
        test2.setText(R.string.data_released);
        buttonOkay = popupView.findViewById(R.id.okayButton);

         buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Toast.makeText(view.getContext(), "Returned to Main Page", Toast.LENGTH_SHORT).show();
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}
