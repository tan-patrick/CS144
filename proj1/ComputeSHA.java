import java.security.*;
import java.io.*;

class ComputeSHA {

	public static void main(String[] args){
		
		//gets file name
		String filename = args[0];

		try{

			//open file and read bytes from file into filebytes
			File file = new File(filename);
			FileInputStream input = null;

			input = new FileInputStream(file);
			int filelength = (int)file.length();
			byte filebytes[] = new byte[filelength];
			//convert byte array filebytes into String filestring
			input.read(filebytes);
			String filestring = new String(filebytes);

			//use MessageDigest to compute SHA-1
			java.security.MessageDigest digest = null;
			digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.reset();

			digest.update(filestring.getBytes("UTF-8"));
			byte[] bytes = digest.digest();

			//Convert byte array of SHA-1 hash to hexadecimal for output
		    StringBuilder builder = new StringBuilder(bytes.length * 2);
		    for(byte b: bytes)
		       builder.append(String.format("%02x", b & 0xff));

		    //print hexadecimal string to output
		    System.out.println(builder.toString() + "a;sdlfj");
			}
		catch(Exception e){
			System.out.println("Error.");
		}
	}

}