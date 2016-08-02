package com.facci.conversorja;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityJA extends AppCompatActivity {

    final String[] datos = new String[] {"DÓLAR","EURO","PESO MEXICANO"};

    private Spinner MonedaActualSP;
    private Spinner MonedaCambioSP;
    private EditText ValorCambioET;
    private TextView ResultadoTV;

    final private double factorDolarEuro = 0.87;
    final private double factorPesoDolar = 0.54;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_j);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,datos);

        MonedaActualSP = (Spinner) findViewById(R.id.MonedaActualSP);

        MonedaActualSP.setAdapter(adaptador);

        MonedaCambioSP = (Spinner) findViewById(R.id.MonedaCambioSP);

        SharedPreferences  preferencias = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

        String tmpMonedaActual = preferencias.getString("MonedaActual","");
        String tmpMonedaCambio = preferencias.getString("MonedaCambio","");

        if (tmpMonedaActual.equals("")){
            int indice = adaptador.getPosition(tmpMonedaActual);
            MonedaActualSP.setSelection(indice);

        }
        if(tmpMonedaCambio.equals(""));
            int indice = adaptador.getPosition("tmpMonedaCambio");
            MonedaCambioSP.setSelection(indice);

    }

    public void clickConvertir(View v){
        MonedaActualSP = (Spinner) findViewById(R.id.MonedaActualSP);
        MonedaCambioSP = (Spinner) findViewById(R.id.MonedaCambioSP);
        ValorCambioET  = (EditText) findViewById(R.id.ValorCambioET);
        ResultadoTV    = (TextView)   findViewById(R.id.ResultadoTV);

        String MonedaActual = MonedaActualSP.getSelectedItem().toString();
        String MonedaCambio = MonedaCambioSP.getSelectedItem().toString();

        double  ValorCambio = Double.parseDouble(ValorCambioET.getText().toString());

        double    Resultado = ProcesarConversión(MonedaActual,MonedaCambio,ValorCambio);

        if (Resultado>0){
            ResultadoTV.setText(String.format("por %5.2f %s, Usted Recibirá %5.2f %s",ValorCambio,MonedaActual,Resultado,MonedaCambio));
            ValorCambioET.setText("");
            SharedPreferences preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();

            editor.putString("MonedaActual",MonedaActual);
            editor.putString("MonedaCambio",MonedaCambio);

            editor.commit();
        }else{
            ResultadoTV.setText(String.format("Usted Recibirá"));
            Toast.makeText(MainActivityJA.this, "Las opciones elegidas no tiene un factor de conversión", Toast.LENGTH_SHORT).show();

        }

    }

    private double ProcesarConversión(String MonedaActual,String MonedaCambio,double ValorCambio){

        double resultadoConversión =0;
        switch (MonedaActual){
            case"DÓLAR":
                if (MonedaCambio.equals("EURO"))
                    resultadoConversión = ValorCambio = factorDolarEuro;

                if (MonedaCambio.equals("PESOMEXICANO"))
                    resultadoConversión = ValorCambio / factorPesoDolar;

                break;
            case "EURO":
                if (MonedaCambio.equals("DÓLAR"))
                    resultadoConversión = ValorCambio / factorDolarEuro;

                break;
            case "PESO MEXICANO":
                if (MonedaCambio.equals("DÓLAR")){
                    resultadoConversión = ValorCambio * factorPesoDolar;
                }
                break;
        }

        return resultadoConversión;
    }
}
