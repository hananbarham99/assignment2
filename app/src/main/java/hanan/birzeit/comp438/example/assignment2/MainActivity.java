package hanan.birzeit.comp438.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.*;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
private Spinner spinner;
 private EditText  result;
    private EditText  name;
    private EditText  height;
    private EditText  weight;
    private Button save;
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        result = findViewById(R.id.result);
        name = findViewById(R.id.name);
        height = findViewById(R.id.height);
        weight= findViewById(R.id.weight);
        save = findViewById(R.id.save);
        sharedPreferences= getSharedPreferences("NAME",MODE_PRIVATE) ;
        populateSpinner();
        Button Next = (Button) findViewById(R.id.Next);
        Button view = (Button) findViewById(R.id.veiw);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String h = height.getText().toString();
               String w = weight.getText().toString();
               String n = name.getText().toString();
               String s = spinner.getSelectedItem().toString();
               SharedPreferences.Editor editor= sharedPreferences.edit();
               editor.putString("height",h);
               editor.putString("weight",w);
               editor.putString("name",n);
               editor.putString("gender",s);
               editor.apply();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String hh = sharedPreferences.getString("height","");
                String ww = sharedPreferences.getString("weight","");
                String nn = sharedPreferences.getString("name","");
                String ss = sharedPreferences.getString("gender","");
                height.setText(hh  );
                weight.setText(ww);
                name.setText(nn);
                spinner.setSelected(true);
            }
        });

    }

    private void populateSpinner() {
        spinner.setOnItemSelectedListener(this);
        List<String> gender = new ArrayList<String>();
        gender.add("Male");
        gender.add("Female");
        ArrayAdapter<String > adapter = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item , gender);
        spinner.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
// On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void calcOnClick(View view) {

        String Height=height.getText().toString();
         float heightRes= Float.parseFloat(Height);
         heightRes = heightRes/100;
         heightRes= heightRes*heightRes;
        //result.setText(String.valueOf(heightRes));
       String Weight=weight.getText().toString();
       float WeightRes= Float.parseFloat(Weight);
       float Res= WeightRes/heightRes;
        result.setText(String.valueOf("your BMI is      "+Res));

    }



    }




