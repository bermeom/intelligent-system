/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import Agente.Agente;
import static Utilities.Utilities.generatorBitSetRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Juan
 */
public class Erkennung_Nummern {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        Agente a = new Agente();
        a.iniciar();
        
    }
    
    public static void main2(String[] args) {//10000000
        byte []bytes= new byte[20];
        Random r=new Random((long) (Math.random()*(new GregorianCalendar()).getTimeInMillis()));
        System.out.println(Arrays.toString(bytes));
        r.nextBytes(bytes);
        byte b=(int)1;
        System.out.println(Arrays.toString(bytes));
        System.out.println((33&(1<<5)));
        System.out.println(((33>>5)&1));
        System.out.println(r.nextLong());
        System.out.println(r.nextLong());
        System.out.println("----------------------------------");
        BitSet bs=generatorBitSetRandom(9);
        List<Integer> list=Utilities.Utilities.generatorListIntegerRandom(9);
        BitSet bs1=Utilities.Utilities.convertBitsetToListInteger(list);
        System.out.println(bs);
        System.out.println(list);
        System.out.println(bs1);
        Utilities.Utilities.printBitset(bs1, list.size()*4);
        Utilities.Utilities.printBitset(bs, 9);
        String routeBase="\\home\\angel\\Documentos\\git\\Sistemas inteligentes\\system-intelligent\\Project_system_intelligent\\DataSet";
        routeBase="/home/angel/Documentos/git/Sistemas inteligentes/system-intelligent/Project_system_intelligent/DataSet";
        Utilities.Utilities.readFile(routeBase+"/optdigits.tra");
        
    }
    
}
