package com.example.administrador.avesdejardin;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    final Context contexto = this;
    private View miLayout;

    //variables para hacer llamada
//    private TextView tv_numero;
    private EditText tv_numero;
    private ImageButton b_telefono;

    //variables para enviar correo
//    private TextView tv_correo;
    private EditText tv_correo;
    private ImageButton b_correo;

    //Instancia de clase para asignar permisos para llamada y correo
    private Permisos permiso;

    //Calificacion usando RatingBar en Dialog
    private Button b_califica;
    private TextView tv_nota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guia_detalle);

        //Captura del layout "padre" para ubicar el elemento Snackbar
        miLayout = findViewById(R.id.guia_detalle);

//        tv_numero = (TextView) findViewById(R.id.tv_tel_guia);
        tv_numero = (EditText) findViewById(R.id.tv_tel_guia);
        b_telefono = (ImageButton) findViewById(R.id.b_llamar);

//        tv_correo = (TextView) findViewById(R.id.tv_mail_guia);
        tv_correo = (EditText) findViewById(R.id.tv_mail_guia);
        b_correo = (ImageButton) findViewById(R.id.b_mail);

        tv_nota = (TextView)findViewById(R.id.tv_nota);
        b_califica = (Button)findViewById(R.id.b_califica);

        permiso = new Permisos(miLayout,MainActivity.this);

        //invocar código para hacer llamada
        b_telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permiso.LanzarLlamada(tv_numero.getText().toString());
            }
        });

        //invocar código para enviar mail
        b_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permiso.Lanzar_Email(tv_correo.getText().toString());
            }
        });

        //abrir Dialogo para calificar al guía
        b_califica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomDialog(miLayout);
            }
        });
    }

    //método CALL_BACK que recupera los permisos de requestPermissions. debe ir en la
    //actividad principal.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
//        permissionCheck = ContextCompat.checkSelfPermission(miActividad, Manifest.permission.CALL_PHONE);
        if (requestCode == permiso.SOLICITAR_PERMISO_LLAMADAS) {
            // Si la solicitud es cancelada, grantResults estará vacío.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(miLayout, getString(R.string.permiso_si),
                        Snackbar.LENGTH_SHORT)
                        .show();
//                permissionCheck = ContextCompat.checkSelfPermission(miActividad, Manifest.permission.CALL_PHONE);
                // permiso concedido, ejecutar la acción.
                permiso.HacerLlamada();

            } else {

                Snackbar.make(miLayout, getString(R.string.permiso_no),
                        Snackbar.LENGTH_SHORT)
                        .show();
                // permiso negado, no se puede continuar
            }
        }
    }

    public void openCustomDialog(View view){
        // dialogo personalizado
        final Dialog dialog = new Dialog(contexto);
        //asignar un layout XML para el diálogo
        dialog.setContentView(R.layout.dialogo_calificar);

        // personalizar componentes del XML
        Button botCalificar = (Button) dialog.findViewById(R.id.b_aceptar);
        final RatingBar rbValoracion = (RatingBar) dialog.findViewById(R.id.rb_calificar);

        //Acción del botón, cerrar ventana al terminar
        botCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float stars = rbValoracion.getRating();
                tv_nota.setText(String.valueOf(stars));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}

