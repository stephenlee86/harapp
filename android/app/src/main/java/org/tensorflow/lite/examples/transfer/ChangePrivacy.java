package org.tensorflow.lite.examples.transfer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class ChangePrivacy extends AppCompatActivity {

    Button savePrivacyChanges;
    Button returnToMain;

    private Switch AgeSw, GenderSw, HeightSw, WeightSw;
    String AgeStr, GenderStr, HeightStr, WeightStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_privacy);
        savePrivacyChanges = (Button)findViewById(R.id.saveBtn);
        returnToMain = (Button)findViewById(R.id.returnBtn);

        AgeSw = (Switch)findViewById(R.id.AgeSwitch);
        GenderSw = (Switch)findViewById(R.id.GenderSwitch);
        HeightSw = (Switch)findViewById(R.id.HeightSwitch);
        WeightSw = (Switch)findViewById(R.id.WeightSwitch);

        SharedPreferences settings = getSharedPreferences("DataPreferences", 0);
        AgeStr = settings.getString("AgePreference", "DefaultValueIfNotExists");
        GenderStr = settings.getString("GenderPreference", "DefaultValueIfNotExists");
        HeightStr = settings.getString("HeightPreference", "DefaultValueIfNotExists");
        WeightStr = settings.getString("WeightPreference", "DefaultValueIfNotExists");

        if(!AgeStr.equals("DefaultValueIfNotExists"))
        {
            if (AgeStr.equals("ON"))
            {
                AgeSw.setChecked(true);
            } else
            {
                AgeSw.setChecked(false);
            }
        }

        if(!GenderStr.equals("DefaultValueIfNotExists"))
        {
            if (GenderStr.equals("ON"))
            {
                GenderSw.setChecked(true);
            } else
            {
                GenderSw.setChecked(false);
            }
        }

        if(!HeightStr.equals("DefaultValueIfNotExists"))
        {
            if (HeightStr.equals("ON"))
            {
                HeightSw.setChecked(true);
            } else
            {
                HeightSw.setChecked(false);
            }
        }

        if(!WeightSw.equals("DefaultValueIfNotExists"))
        {
            if (WeightStr.equals("ON"))
            {
                WeightSw.setChecked(true);
            } else
            {
                WeightSw.setChecked(false);
            }
        }


        savePrivacyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AgeSw.isChecked())
                    AgeStr = AgeSw.getTextOn().toString();
                else
                    AgeStr = AgeSw.getTextOff().toString();
                if (GenderSw.isChecked())
                    GenderStr = GenderSw.getTextOn().toString();
                else
                    GenderStr = GenderSw.getTextOff().toString();
                if (HeightSw.isChecked())
                    HeightStr = HeightSw.getTextOn().toString();
                else
                    HeightStr = HeightSw.getTextOff().toString();
                if (WeightSw.isChecked())
                    WeightStr = WeightSw.getTextOn().toString();
                else
                    WeightStr = WeightSw.getTextOff().toString();
                Toast.makeText(getApplicationContext(),
                        "Age -  " + AgeStr + " \n"
                        + "Gender - " + GenderStr + "\n "
                        + "Height -  " + HeightStr + " \n"
                        + "Weight - " + WeightStr,Toast.LENGTH_SHORT).show();
            }
        });


        returnToMain.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("DataPreferences", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("AgePreference", AgeStr);
                editor.putString("GenderPreference", GenderStr);
                editor.putString("HeightPreference", HeightStr);
                editor.putString("WeightPreference", WeightStr);
                editor.apply();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

    }



}
