package Utilities;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Utilidades {

    public static void imprimirStackTrace() {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        String msg = null;
        for (StackTraceElement s : stack) {
            msg += s.getClassName() + ":" + s.getMethodName() + ":" + String.valueOf(s.getLineNumber()) + "\r\n";
        }
        System.out.println(msg);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String directorioActual() {
        return System.getProperty("user.dir");
    }

    public static String intToBinaryString(int i) {
        String str = Integer.toBinaryString(i);
        return String.format("%1$5s", str).replace(' ', '0');
    }

    public static int intAleatorio(int min, int max) {
        XSRandom rand = new XSRandom();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static Integer[] vectorEnterosAleatorio(int cantidadMaxima, int min, int max) {
        XSRandom rand = new XSRandom();
        int cantidad = rand.nextInt(cantidadMaxima + 1);
        Integer[] vector = new Integer[cantidad];
        for (int i = 0; i < cantidad; i++) {
            vector[i] = rand.nextInt((max - min) + 1) + min;
        }
        return vector;
    }

    public static int convertirBitSetEnInt(BitSet b) {
        int value = 0;
        for (int i = 0; i < b.length(); ++i) {
            value += b.get(i) ? (1 << i) : 0;
        }
        return value;
    }

    public static Integer[] vectorEnterosUnicosAleatorios(int cantidad, int min, int max) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = min; i < max; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        Integer[] vector = new Integer[cantidad];
        if (cantidad > max) {
            cantidad = max;
        }
        for (int i = 0; i < cantidad; i++) {
            vector[i] = list.get(i);
        }
        return vector;
    }

    public static List<Integer> copiarListaDeEnteros(List<Integer> l) {
        List<Integer> lc = new ArrayList();
        if (l != null) {
            for (Integer i : l) {
                lc.add(i);
            }
        }
        return lc;
    }
}
