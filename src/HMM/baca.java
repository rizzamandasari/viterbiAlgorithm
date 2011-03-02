/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package HMM;

/**
 *
 * @author reiza
 */
import java.io.*;

public class baca {

    public static void main(String[]args ) throws IOException
    {
        FileInputStream finput = null;
        
        //membuka file
        try{
            finput = new FileInputStream("src/HMM/Sequence.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(finput));
            String line = null;
            String dna = "";


        while( (line = br.readLine()) != null ) {
            dna += line.replaceAll("[\n0-9 ]", "");
            
        }
            System.out.println(dna);

        }
        catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
            return;
        }
        //membaca data dari dalam file
        /*try{
            while ((data = finput.read()) != -1){
            //dikonversi menjadi char
                //System.out.print((char) data);
             
                System.out.print(data);
                }
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return;
        }
        */

        try{
            finput.close();
        }
        catch(IOException ioe){}
        }
    }
