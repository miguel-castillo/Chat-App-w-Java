import java.io.*;
import java.net.*;
import java.util.*;

class Thread_Receive extends Thread
{
    ObjectInput oInputStream;
    Thread_Receive(ObjectInput oInputStream)
    {
        this.oInputStream = oInputStream;
    }
    public void run()
    {
        for (int i = 0; i < 5; i++)
        {
            String s;
            try
            {
                s = (String)oInputStream.readObject();
                System.out.println("Received from server: " + s);
            }
            catch (Exception e)
            { }
        }
    }
}

class Thread_Send extends Thread
{
    ObjectOutput oOutputStream;
    Thread_Send(ObjectOutput oOutputStream)
    {
        this.oOutputStream = oOutputStream;
    }
    public void run()
    {
        Scanner kb = new Scanner(System.in);
        for (int i = 0; i < 5; i++)
        {
            String s;
            try
            {
                String ins = kb.nextLine();
                oOutputStream.writeObject(ins);
            }
            catch (Exception e)
            { }
        }
    }
}


public class Socket_Client
{
    public static void main(String[] args)
    {
        try
        {
            Socket clientSocket = new Socket("130.182.4.92", 8069);

            OutputStream outStream = clientSocket.getOutputStream();
            ObjectOutput oOutputStream = new ObjectOutputStream(outStream);
            Thread_Send ts = new Thread_Send(oOutputStream);
            ts.start();

            InputStream inStream = clientSocket.getInputStream();
            ObjectInput oInputStream = new ObjectInputStream(inStream);
            Thread_Receive tr = new Thread_Receive(oInputStream);
            tr.start();

            ts.join();
            tr.join();

            oOutputStream.flush();
            oOutputStream.close();
        }
        catch (Exception e)
        { }
    }
}