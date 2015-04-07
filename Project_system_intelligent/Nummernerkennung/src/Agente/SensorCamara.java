package Agente;

import Utilities.Mensaje;
import Utilities.Utilidades;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SensorCamara {

    private static String archivoEntrenamiento = "../DataSet/optdigits.tra";
    private static String archivoValidacion = "../DataSet/optdigits.tes";
    
    //private static String archivoEntrenamiento = "../DataSet/jpr_prueba.txt";
    //private static String archivoValidacion = "../DataSet/jpr_prueba.txt";
    
    private static boolean releerArchivo = true;

    private BufferedReader br;
    private boolean finArchivo = false;
    public enum TipoSensor {Entrenamiento, Validacion}
    private String rutaArchivo;
    
    public SensorCamara(TipoSensor tipo ) throws FileNotFoundException {
        if(tipo == TipoSensor.Entrenamiento)
            rutaArchivo = this.archivoEntrenamiento;
        else if (tipo == TipoSensor.Validacion)
            rutaArchivo = this.archivoValidacion;
        
        cargarArchivo();
    }

    private void cargarArchivo() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(new File(Utilidades.directorioActual(), this.rutaArchivo)));
    }

    public boolean finArchivo() {
        return finArchivo;
    }

    public Mensaje ObtenerMensaje() {
        Mensaje m = null;
        String msg = LeerLineaArchivo();
        if (msg != null) {
            m = new Mensaje(null, null);
            m.numero = Character.getNumericValue(msg.charAt(msg.length() - 1));
            m.mensaje = "";
            msg = msg.substring(0, msg.length() - 2);
            for (String s : msg.split(",")) {
                String str = Utilidades.intToBinaryString(Integer.parseInt(s));
                m.mensaje += str;
            }
        }
        return m;
    }

    private String LeerLineaArchivo() {

        String linea = null;
        if (!finArchivo) {
            try {
                linea = br.readLine();
            } catch (Exception e) {
                linea = null;
            }

            /*Para releer archivo*/
            if (linea == null && releerArchivo) {
                try {
                    cargarArchivo();
                    try {
                        linea = br.readLine();
                    } catch (Exception ex) {
                        linea = null;
                    }
                } catch (Exception e) {
                }
            }

            if (linea == null) {
                finArchivo = true;
            }
        }
        return linea;
    }
}
