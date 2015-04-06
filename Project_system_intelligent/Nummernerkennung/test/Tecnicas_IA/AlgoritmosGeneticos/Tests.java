package Tecnicas_IA.AlgoritmosGeneticos;

import Utilities.Utilidades;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class Tests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testIntToBinaryString()
    {
        assertEquals("00100",Utilidades.intToBinaryString(4));
        assertEquals("00011",Utilidades.intToBinaryString(3));
        assertEquals("10000",Utilidades.intToBinaryString(16));
        assertEquals("10010",Utilidades.intToBinaryString(18));
        assertEquals("11111",Utilidades.intToBinaryString(31));
    }
    
    @Test
    public void testConstructor() {

        BitSet condicion = new BitSet(16);
        BitSet accion = new BitSet(3);
        int fitness = 10;

        List<Integer> comodines = new ArrayList();
        comodines.add(1);
        comodines.add(3);

        Regla r = new Regla(condicion, accion, fitness, comodines);

        assertEquals(condicion, r.getCondicion());
        assertEquals(accion, r.getAccion());
        assertEquals(fitness, r.getFitness());
        assertArrayEquals(comodines.toArray(), r.getComodines().toArray());

    }

    @Test
    public void testConstructorExcepciones() {

        BitSet condicion = null;
        BitSet accion = null;
        int fitness = 0;
        List<Integer> comodines = null;

        Regla r;
        boolean excepcionEjecutada = false;

        /*Prueba todos null*/
        excepcionEjecutada = false;
        try {
            r = new Regla(condicion, accion, fitness, comodines);
        } catch (Exception e) {
            excepcionEjecutada = true;
            assertEquals(e.getClass(), NullPointerException.class);
        }
        assertTrue(excepcionEjecutada);

        /*Prueba accion null*/
        condicion = new BitSet(5);
        excepcionEjecutada = false;
        try {
            r = new Regla(condicion, accion, fitness, comodines);
        } catch (Exception e) {
            excepcionEjecutada = true;
            assertEquals(e.getClass(), NullPointerException.class);
        }
        assertTrue(excepcionEjecutada);

        /*Prueba longitud condicion*/
        condicion = new BitSet(0);
        excepcionEjecutada = false;
        try {
            r = new Regla(condicion, accion, fitness, comodines);
        } catch (Exception e) {
            excepcionEjecutada = true;
            assertEquals(e.getClass(), NullPointerException.class);
        }
        assertTrue(excepcionEjecutada);

        /*Prueba longitud accion*/
        condicion = new BitSet(2);
        accion = new BitSet(0);
        excepcionEjecutada = false;
        try {
            r = new Regla(condicion, accion, fitness, comodines);
        } catch (Exception e) {
            excepcionEjecutada = true;
            assertEquals(e.getClass(), NullPointerException.class);
        }
        assertTrue(excepcionEjecutada);
    }

    @Test
    public void testDebugString() {
        BitSet condicion = new BitSet();
        BitSet accion = new BitSet();
        int fitness = 10;

        List<Integer> comodines = new ArrayList();
        comodines.add(0);
        comodines.add(0);
        comodines.add(7);

        condicion.set(0);
        condicion.set(2);
        condicion.set(7);
        accion.set(0);
        accion.set(2);

        Regla r = new Regla(condicion, accion, fitness, comodines);
        //System.out.println(r.toDebugString());
        assertEquals("C: 00000000000000000000000000000000000000000000000000000000#000010#  A: 0000000000000000000000000000000000000000000000000000000000000101  F: 10  #: 2", r.toDebugString());
    }

    @Test
    public void testDebugString2() {
        BitSet input2 = new BitSet();
        input2.set(0);
        input2.set(4);
        input2.set(63);

    }

    @Test
    public void testComparador() {
        BitSet condicion = new BitSet();
        BitSet accion = new BitSet();
        int fitness = 10;

        List<Integer> comodines = new ArrayList();
        comodines.add(2);
        comodines.add(7);

        condicion.set(0);
        condicion.set(2);
        condicion.set(7);
        accion.set(0);
        accion.set(2);

        Regla r = new Regla(condicion, accion, fitness, comodines);
        //System.out.println(r.toDebugString());

        BitSet input1 = new BitSet();
        input1.set(0);
        input1.set(7);
        //System.out.println(Regla.toDebugString(input1));
        assertTrue(r.verificarActivacion(input1));

        BitSet input2 = new BitSet();
        input2.set(0);
        input2.set(63);
        //System.out.println(Regla.toDebugString(input2));
        assertFalse(r.verificarActivacion(input2));

        BitSet input3 = new BitSet();
        input3.set(1);
        input3.set(5);
        //System.out.println(Regla.toDebugString(input3));
        assertFalse(r.verificarActivacion(input3));
    }

    @Test
    public void testConvertirStringEnBitset() {
        String s = "100001101";//1000001001100";
        BitSet c = Regla.crearBitsetDesdeString(s);
        String sC = Regla.toDebugString(c);

//        System.out.println(s);
//        System.out.println(sC);
//        System.out.println(c);
        assertTrue(sC.contains(s));
    }

    @Test
    public void testRecombinacionReglas() {

        Regla r1 = Regla.crearReglaAleatoria(4);
        Regla r2 = Regla.crearReglaAleatoria(4);

        Regla[] vr = LCS.recombinarReglas(r1, r2, 4);

/*
        System.out.println(Regla.toDebugString(r1.getCondicion()));
        System.out.println(Regla.toDebugString(r2.getCondicion()));
        System.out.println(Regla.toDebugString(vr[0].getCondicion()));
        System.out.println(Regla.toDebugString(vr[1].getCondicion()));
        System.out.println("");
        System.out.println(r1.toDebugString());
        System.out.println(r2.toDebugString());
        System.out.println(vr[0].toDebugString());
        System.out.println(vr[1].toDebugString());
*/

    }
    
    @Test
    public void testMutacion()
    {
        int longitudRegla = 1024;
        Regla r1 = Regla.crearReglaAleatoria(longitudRegla);
        Regla rn = LCS.mutarRegla(r1, longitudRegla);
        System.out.println(r1.toDebugString());
        System.out.println(rn.toDebugString());
    }
    
    @Test
    public void testSeleccionPorRuleta()
    {
        int tamaño = 8;
        List<Regla> listaReglas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Regla r1 = Regla.crearReglaAleatoria(tamaño);
            listaReglas.add(r1);
        }
        List<Regla> reglasRuleta = LCS.seleccionarReglasPorRuleta(listaReglas, 2);
        
        for (Regla r : reglasRuleta) {
            System.out.println(r.toDebugString());
        }
        
        
        
    }

}
