/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

/**
 *
 * @author angel
 */
public class Utilities {
    
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
        String stringBits="";
        for (int i=0;i<nbits;){
            for (int j=0; j<4 && i<nbits;j++,i++){
                stringBits=stringBits+(bs.get(i)?1:0);
            }
           stringBits+=" ";
        }
        System.out.println((new StringBuilder(stringBits).reverse().toString().trim()));
    }
    
    public static List<String> readFile(String route) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File(route);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
             String linea;
            while((linea=br.readLine())!=null)
              System.out.println(linea.trim());
        
        }catch(Exception e){
           e.printStackTrace();
        }finally{
            try{                    
               if( null != fr ){   
                  fr.close();     
               }                  
            }catch (Exception e){ 
               e.printStackTrace();
            }
        }   
        
        return null;
    }
    
    
}
