package Perfomance;

import java.io.*;
import java.util.Scanner;

public class TestProcessIO {

  public static boolean isAlive(Process p) {
    try {
      p.exitValue();
      return false;
    }
    catch (IllegalThreadStateException e) {
      return true;
    }
  }

  static void normalTask() throws IOException{
    ProcessBuilder pb = new ProcessBuilder( "stdbuf","-o0","/home/daniel/a.out");
    Process p=pb.start();
    InputStream out = p.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(out));

    String line = null;
    System.out.println("Reading...");
    OutputStream os = p.getOutputStream();
    PrintWriter writer=new PrintWriter(os);
    Scanner lc = new Scanner(System.in);
    while ((line = br.readLine()) != null)
    {
      System.out.println("Line: " + line);
      String f=lc.nextLine();
      writer.write(f+"\n");
      writer.flush();
    }
  }

  static void runTerminal() throws IOException{
     ProcessBuilder builder = new ProcessBuilder("bash", "-i");

    builder.redirectErrorStream(true); // so we can ignore the error stream
    Process process = builder.start();
    InputStream out = process.getInputStream();
    OutputStream in = process.getOutputStream();

    byte[] buffer = new byte[4000];
    while (isAlive(process)) {
      int no = out.available();
      if (no > 0) {
        int n = out.read(buffer, 0, Math.min(no, buffer.length));
        System.out.println("Out: "+new String(buffer, 0, n));
      }

      int ni = System.in.available();
      if (ni > 0) {
        int n = System.in.read(buffer, 0, Math.min(ni, buffer.length));
        in.write(buffer, 0, n);
        in.flush();
      }

      try {
        Thread.sleep(10);
      }
      catch (InterruptedException e) {
      }
    }

    System.out.println(process.exitValue());
  }


  public static void main(String[] args) throws IOException {
    runTerminal();
  }

}