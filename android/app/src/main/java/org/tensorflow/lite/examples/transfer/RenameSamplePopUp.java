package org.tensorflow.lite.examples.transfer;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class RenameSamplePopUp {
    boolean focus;

    EditText sampleNameText;
    Button buttonOkay;
    Button buttonCancel;

    int width;
    int height;

    LayoutInflater inflater;

    View popupView;

    DataModels dataModels;
    String sample_name;
    String new_sample_name;
    String file_path;

    public RenameSamplePopUp(DataModels model, String sampleName, String filePath)
    {
        dataModels = model;
        sample_name = sampleName;
        file_path = filePath;
    }

    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.rename_sample_pop_up, null);
        width = LinearLayout.LayoutParams.MATCH_PARENT;
        height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focus = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focus);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        sampleNameText = popupView.findViewById(R.id.editSampleName);
        sampleNameText.setText(sample_name);
        buttonOkay = popupView.findViewById(R.id.renameSampleOkButton);
        buttonCancel = popupView.findViewById(R.id.renameSampleCancelButton);

        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name = sampleNameText.getText().toString();
                dataModels.renameSampleByName(sample_name, new_name, file_path);
                popupWindow.dismiss();
                Toast.makeText(view.getContext(), "Renamed the sample " + sample_name + " to " + new_sample_name, Toast.LENGTH_SHORT).show();
            }
        });

         buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Toast.makeText(view.getContext(), "Returned to Sample Page", Toast.LENGTH_SHORT).show();
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
