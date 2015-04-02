/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

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
    
    public static Random random() {
       return new Random((long) (Math.random()*(new GregorianCalendar()).getTimeInMillis()));
    }
    
    public static Boolean cast_A_ByteBit(byte b) {
        if (b==0){
            return false;
        }
        return true;
    }
    
    public static BitSet  generatorBitSetRandom(int nbits){
        int size=nbits/8;
        //If n is not a multiple of 8
        if((float)size<(float)nbits/8){
            size++;
        }
        byte []bytes= new byte[size];
        Random r=random();
        r.nextBytes(bytes);
        BitSet bitSet=new BitSet(nbits);
        
        for (int i=0,k=0;i<size&&k<nbits;i++){
            for (int j=0;j<8&&k<nbits;j++,k++){
                bitSet.set(k,cast_A_ByteBit((byte)((bytes[i]>>j)&1)));
            }
        }
        return bitSet;
    }
    
    public static List<Integer> generatorListIntegerRandom(int nbits) {
        String arry=generatorBitSetRandom(nbits)+"";
        List<Integer> list_integer=new ArrayList<Integer>();
        for (String s : arry.substring(1, arry.length()-1).split(",")) {
	    list_integer.add(Integer.parseInt(s.trim()));
	}
        return list_integer;
    }
    public static BitSet convertBitsetToListInteger(List<Integer> list) {
        int nbits=list.size()*4;
        BitSet bitSet=new BitSet(nbits);
        
        for (int i=0,k=0;i<list.size()&&k<nbits;i++){
            for (int j=0;j<4&&k<nbits;j++,k++){
                bitSet.set(k,cast_A_ByteBit((byte)((list.get(i)>>j)&1)));
            }
        }
        return bitSet;
    }
    
    public static void printBitset(BitSet bs,int nbits) {
        for (int i=nbits;i>=0;){
            for (int j=0; j<4 && i>=0;j++,i--){
                System.out.print((bs.get(i)?1:0));
            }
            System.out.print(" ");
        }
    }
    
    public static void main(String[] args) {//10000000
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
        List<Integer> list=generatorListIntegerRandom(9);
        BitSet bs1=convertBitsetToListInteger(list);
        System.out.println(bs);
        System.out.println(list);
        System.out.println(bs1);
        printBitset(bs1, list.size()*4);
        
    }
    
}
