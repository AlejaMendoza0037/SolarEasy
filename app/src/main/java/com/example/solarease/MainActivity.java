package com.example.solarease;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText        latitudeEditText, longitudeEditText, areaEditText;
    private SeekBar         inclinacionSeekBar;
    private TextView        inclinacionTextView, resultadoTextViwe;
    private Button          calculateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtenemos las referencias a los elementos del diseño
        latitudeEditText        = findViewById(R.id.latitude_edittext);
        longitudeEditText       = findViewById(R.id.longitude_edittext);
        areaEditText            = findViewById(R.id.area_edittext);
        inclinacionSeekBar      = findViewById(R.id.inclination_seekbar);
        inclinacionTextView     = findViewById(R.id.inclination_textview);
        resultadoTextViwe       = findViewById(R.id.result_textview);
        calculateButton         = findViewById(R.id.calculate_button);

        //aca configuramos los listener para el seekbar de inclinacion
        inclinacionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {// cuando se camnia el valor de la barra de progreso, se actualiza el texto de inclinationTextviwe
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//cuando muevo el puntero se activa esta funcion
                //cuando arrastre
                inclinacionTextView.setText("Inclinacion paneles " + progress + " º ");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {// cuando inicio el arrastre de la barra

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {// cuando termino el arrastre de la barra

            }


        });
        //configuramos el lisener para el boton de calcular
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vamos a capturar el valor que coloquen en el area de latitud en la aplicacion
               /*double latitud      = Double.parseDouble(latitudeEditText.getText().toString());//toString= nos devuelve el valor de latitudEditText necesitamos pasarlo a un valor double  debemos de parcear
                double longitud     = Double.parseDouble(longitudeEditText.getText().toString());
                double area         = Double.parseDouble(areaEditText.getText().toString());
                int inclinacion     = inclinacionSeekBar.getProgress();*/

        //inicializamos las variables en cero para que cuando el usuario no pueda dejar campos en vacio
                double latitud      =0;
                double longitud     =0;
                double area         = 0;
                int inclinacion     = inclinacionSeekBar.getProgress();
                boolean error       = false;// si se encontro un campo vacio , si es asi el campo pararia a ser true


                switch (inclinacion){
                    case 0:
                        Toast.makeText(getApplicationContext(),   "Los paneles tienen muy poca inclinacion    ", Toast.LENGTH_LONG).show();

                        break;
                    case 90:
                        Toast.makeText(getApplicationContext(),   "Los paneles tienen demaciada inclinacion    ", Toast.LENGTH_LONG).show();
                        break;

                    default: Toast.makeText(getApplicationContext(),   "Todo esta bien : Ok     ", Toast.LENGTH_LONG).show();


                }










                /* if (validarCampo()){
                    //si error esta vacio, si hay un campo vacio
                    error= true;
                    isEMpty--el campo esta vacio

                }*/

               if (validarCampo( latitudeEditText.getText().toString(), "Latitud", false)){
                   latitud=Double.parseDouble(latitudeEditText.getText().toString());

               }else error=true;

                if (validarCampo( longitudeEditText.getText().toString(), "Longitud", false)){
                    longitud=Double.parseDouble(longitudeEditText.getText().toString());

                }else error=true;

                if (validarCampo( areaEditText.getText().toString(), "Area", true)){
                    area=Integer.parseInt(areaEditText.getText().toString());

                }else error=true;




                // si no hay nungun error o campo vacio
                //  error != true
                if ( !error ){

                    //aca mostramos por pantalla
                    double produccionEnergia = calcularProduccionEnergia(latitud, longitud, area, inclinacion);
                    //borra muchos numeros decimales
                    DecimalFormat decimalFormat= new DecimalFormat("0.00");
                    resultadoTextViwe.setText("Produccion de energia " + decimalFormat.format(produccionEnergia)+ "kWh");

                    resultadoTextViwe.setText("Producción de energía: " + produccionEnergia + " kWh");


                }else
                    resultadoTextViwe.setText("");


            }
        });
    }

        //solitamos parametros ... parametro que nos envie el texto del campo
    private boolean validarCampo( String dato,  String nombreCampo, boolean condicionMayorCero){
        if (dato.isEmpty()) {// isEmpty , el campo esta vacio
            // hacemos una alerta

                        // contexto del mensaje ----------------------------------------Lenggth -- mensaje largo--- .show es para mostrar el mensaje
            Toast.makeText(getApplicationContext(), nombreCampo + "  Debe tener un Valor", Toast.LENGTH_LONG).show();
            return false;
        //si en el area hay un dato que diga cero 0
        }else if (condicionMayorCero== true &&  Double.parseDouble(dato)== 0){
            Toast.makeText(getApplicationContext(), nombreCampo + "  Debe ser mayor a cero", Toast.LENGTH_LONG).show();
            return false;

        }


        else return true;
    }


        private double calcularProduccionEnergia(double latitud, double longitud, double area, int inclinacion) {

            // Convertir latitud, longitud e inclinación a radianes
            double latitudRad = Math.toRadians(latitud);
            double longitudRad = Math.toRadians(longitud);
            double inclinacionRad = Math.toRadians(inclinacion);

            // Obtener día del año actual
            //int diaDelAnio = LocalDate.now().getDayOfYear();

            //obtenemos dia del año actual
            Calendar calendar = Calendar.getInstance();
            int diaDelAnio = calendar.get(Calendar.DAY_OF_YEAR);


            // Calcular ángulo de incidencia de la radiación solar
            double anguloIncidencia = Math.acos(Math.sin(latitudRad) * Math.sin(inclinacionRad) + Math.cos(latitudRad) * Math.cos(inclinacionRad) * Math.cos(longitudRad));

            // Calcular radiación solar incidente
            double constanteSolar = 0.1367; // kWh/m²
            double radiacion = constanteSolar * Math.cos(anguloIncidencia) * (1 + 0.033 * Math.cos(Math.toRadians(360 * diaDelAnio / 365.0)));

            // Calcular producción de energía
            double areaPanel = area / 10000.0; // convertir a m²
            double eficienciaPanel = 0.16; // 16% de eficiencia
            double factorPerdidas = 0.9; // pérdida del 10%
            double produccionEnergia = areaPanel * radiacion * eficienciaPanel * factorPerdidas;

            return produccionEnergia;
        }

    private double calcularProduccionEnergia(double latitud, double longitud, int areaInt, int inclinacion) {
        // Redondear inclinación al entero más cercano
        double area = (double) Math.round(areaInt);

        // Llamar al otro método
        return calcularProduccionEnergia(latitud, longitud, area, inclinacion);


    }




}










    












