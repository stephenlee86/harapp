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

public class DeleteAllPopUp {
    boolean focus;

    EditText deleteText;
    Button buttonOkay;
    Button buttonCancel;

    int width;
    int height;

    LayoutInflater inflater;

    View popupView;

    DataModels dataModels;
    String file_path;
    String data_model_file_name;

    public DeleteAllPopUp(DataModels model, String filePath, String dataModelFileName)
    {
        dataModels = model;
        file_path = filePath;
        data_model_file_name = dataModelFileName;
    }

    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.delete_all_pop_up, null);
        width = LinearLayout.LayoutParams.MATCH_PARENT;
        height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focus = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focus);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        deleteText = popupView.findViewById(R.id.editDeleteAll);
        deleteText.setText("D");
        buttonOkay = popupView.findViewById(R.id.deleteAllOkButton);
        buttonCancel = popupView.findViewById(R.id.deleteAllCancelButton);

        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editText = deleteText.getText().toString();
                if (editText.equals("DELETE")) {
                    dataModels.deleteAll(file_path, data_model_file_name);
                    popupWindow.dismiss();
                    Toast.makeText(view.getContext(), "Deleted all samples", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(view.getContext(), "Please type the string ['DELETE'] to delete all saved samples !!!", Toast.LENGTH_SHORT).show();
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
