package com.example.administrador.avesdejardin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by COMERCIO on 13/03/2017.
 */

public class Permisos extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static final Integer SOLICITAR_PERMISO_LLAMADAS = 0;
    private int permissionCheck;
    private View miLayout;
    private Activity miActividad;
    private String telefono;
    private String[] lista_correos;
    Snackbar miSnackBar;

    Permisos(){
        //constructor vacío por defecto.
    }

    Permisos(View layoutPadre, Activity actividadPadre){
        setView(layoutPadre);
        setActividad(actividadPadre);

        //definir un objeto snackBar para personalizarlo
        miSnackBar = Snackbar.make(miLayout,"", Snackbar.LENGTH_INDEFINITE);
        View view = miSnackBar.getView();
        // cambiar color del texto del botón de acción
        miSnackBar.setActionTextColor(Color.YELLOW);

    }

    public void setTelefono(String var_telefono){
        telefono = var_telefono;
    }

    public void setCorreo(String varCorreo){
        lista_correos = varCorreo.split(",");
    }

    public void setView(View varLayout){
        miLayout = varLayout;
    }

    public void setActividad(Activity varActividad){
        miActividad = varActividad;
    }

//Validar los permisos, volver a solicitarlos y tratar de lanzar la actividad de la llamada
    public void LanzarLlamada(String num_tel) {
        setTelefono(num_tel);
        permissionCheck = ContextCompat.checkSelfPermission(miActividad, Manifest.permission.CALL_PHONE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
        {
            //no hay permiso, mostrar un dialogo explicativo
            if (ActivityCompat.shouldShowRequestPermissionRationale(miActividad,
                    Manifest.permission.CALL_PHONE)) {

                // Elemento Sanckbar coon opcion de boton <aceptar> para
                // explicar la necesidad del permiso, en caso que haya sido negado.
                miSnackBar.setText(miActividad.getString(R.string.confirmar_permiso));
                miSnackBar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(miActividad,
                                new String[]{Manifest.permission.CALL_PHONE},
                                SOLICITAR_PERMISO_LLAMADAS);
                    }
                });
                miSnackBar.show();
            } else {
                // ir directo a la solicitud de permisos.
                ActivityCompat.requestPermissions(miActividad,
                        new String[]{Manifest.permission.CALL_PHONE},
                        SOLICITAR_PERMISO_LLAMADAS);

                // SOLICITAR_PERMISO_LLAMADAS es una constante
                // de tipo entero (int) declarada al princicipio, guarda el resultado de la
                // petición de permisos y lo entrega a onRequestPermissionsResult.
            }
        } else {
            //si ya está habilitado el permiso, se ejecuta la acción de llamar.
            HacerLlamada();
        }
    }

    public void HacerLlamada() {
        //Acción que lleva al marcador telefónico.
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + telefono));
        miActividad.startActivity(i);
    }

    public void Lanzar_Email(String var_correo) {
        //Obtener los campos de correo:
        String text_asunto;
        String text_mensaje;

        setCorreo(var_correo);
        text_asunto = miActividad.getString(R.string.asunto_mail);
        text_mensaje = miActividad.getString(R.string.cuerpo_mail);
        //generar la acción y definir los extras.
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // sólo apps de tipo correo manejan este tipo de datos
        intent.putExtra(Intent.EXTRA_EMAIL, lista_correos);
        intent.putExtra(Intent.EXTRA_SUBJECT, text_asunto);
        intent.putExtra(Intent.EXTRA_TEXT, text_mensaje);
        //preguntar si existe una app para ejecutar la acción
        if (intent.resolveActivity(miActividad.getPackageManager()) != null) {
            miActividad.startActivity(intent);
        } else {
            miSnackBar.make(miLayout, miActividad.getString(R.string.error_mail),
                    Snackbar.LENGTH_SHORT)
                    .show();

        }

    }

}
