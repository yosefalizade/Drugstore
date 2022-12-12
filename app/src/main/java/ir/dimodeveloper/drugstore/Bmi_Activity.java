package ir.dimodeveloper.drugstore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Bmi_Activity extends AppCompatActivity {

    Button btnResult;
    EditText edtName, edtvazn, edtqad;
    TextView txtResult;
    RadioButton rdoMan,rdoWomen;
    String name;
    int vazn;
    int qadCM;
    float natije;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_);

        btnResult =  findViewById(R.id.btn_Result);
        txtResult =  findViewById(R.id.txt_Result);
        edtName =  findViewById(R.id.edt_name);
        edtqad =  findViewById(R.id.edt_qad);
        edtvazn =  findViewById(R.id.edt_wazn);
        rdoMan =  findViewById(R.id.rdo_man);
        rdoWomen =  findViewById(R.id.rdo_women);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                name = edtName.getText().toString();

               String vaznr = edtvazn.getText().toString();
                String qadCMr= edtqad.getText().toString();

                if (TextUtils.isEmpty(vaznr) || TextUtils.isEmpty(qadCMr) || TextUtils.isEmpty(name)){
                    Toast.makeText(Bmi_Activity.this, "لطفا اطلاعات لازم را وارد کنید", Toast.LENGTH_LONG).show();

                    } else {

                    vazn = Integer.parseInt(edtvazn.getText().toString());
                    qadCM = Integer.parseInt(edtqad.getText().toString());

                    float qadM = (float) qadCM / 100;

                    qadM = (float) Math.pow(qadM, 2);

                    natije = vazn / qadM;

                    int vazneM = (int) (24 * qadM);
                    int vazneMonaseb = vazn - vazneM;
                    int vazneMonaseb2 = vazneM - vazn;

                    if (rdoWomen.isChecked()) {

                        natije = natije - 1;
                    }


                    if (natije < 16.5) {

                        txtResult.setText(name + " عزیز شما کمبود وزن شدید داری حدود " + vazneMonaseb2 + " کیلو کمبود وزن دارید");

                    } else if (natije < 18.4 && natije >= 16) {

                        txtResult.setText(name + " عزیز شما حدود " + vazneMonaseb2 + " کیلو کمبود وزن دارید");

                    } else if (natije < 25 && natije >= 18.4) {

                        txtResult.setText(name + " عزیز وزن و قدت باهم متناسبه ولی میتونه بهتر از این هم بشه ");


                    } else if (natije < 30 && natije >= 25) {

                        txtResult.setText(name + " عزیز شما حدود " + vazneMonaseb + "کیلو اضافه وزن دارید");

                    } else if (natije < 35 && natije >= 30) {

                        txtResult.setText(name + " عزیز شما دچار چاقی نوع 1 هستید و حدود " + vazneMonaseb + " کیلو اضافه وزن دارید");

                    } else if (natije < 40 && natije >= 35) {

                        txtResult.setText(name + " عزیز شما دچار چاقی نوع 2 هستید و حدود " + vazneMonaseb + " کیلو اضافه وزن دارید");

                    } else if (natije > 40) {

                        txtResult.setText(name + " عزیز شما دچار چاقی نوع 3 هستید و حدود " + vazneMonaseb + " کیلو اضافه وزن دارید ");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Bmi_Activity.this,MainActivity.class));
        super.onBackPressed();
    }
}



