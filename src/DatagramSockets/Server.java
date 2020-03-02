// Fig. 21.5: Server.java
// Set up a Server that will receive packets from a
// client and send packets to a client.
package DatagramSockets;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {
   private JTextArea display;

   private DatagramPacket sendPacket, receivePacket;
   private DatagramSocket socket;
   
   private ArrayList<InetAddress> hostsIp = new ArrayList<InetAddress>();
   private ArrayList<Integer> hostsPort = new ArrayList<Integer>();

   public Server()
   {
      super( "Server" );

      display = new JTextArea();
      getContentPane().add( new JScrollPane( display),
                            BorderLayout.CENTER );
      setSize( 400, 300 );
      show();

      try {
         socket = new DatagramSocket( 5000 );
      }
      catch( SocketException se ) {
         se.printStackTrace();
         System.exit( 1 );
      }
   }

   public void waitForPackets()
   {
      while ( true ) {
         try {
            // set up packet
            byte data[] = new byte[ 100 ];
            receivePacket =
               new DatagramPacket( data, data.length );

            // wait for packet
            socket.receive( receivePacket );
 
            // process packet
            display.append( "\nPacket received:" +
               "\nFrom host: " + receivePacket.getAddress() +
               "\nHost port: " + receivePacket.getPort() +
               "\nLength: " + receivePacket.getLength() +
               "\nContaining:\n\t" +
               new String( receivePacket.getData(), 0,
                           receivePacket.getLength() ) );

            // echo information from packet back to client
            display.append( "\n\nEcho data to client...");
            if (!hostsIp.contains(receivePacket.getAddress())){
            	hostsIp.add(receivePacket.getAddress());
                hostsPort.add(receivePacket.getPort());
            }        
            for (int i = 0 ; i < hostsIp.size() ; i++) {
            	if (hostsIp.get(i) != receivePacket.getAddress()) {
            		sendPacket =
                            new DatagramPacket( receivePacket.getData(),
                                                receivePacket.getLength(),
                                                hostsIp.get(i),
                                                hostsPort.get(i)
                                                 );
                        socket.send( sendPacket );
                        display.append( "Packet sent\n" );
            	}
            }
            display.setCaretPosition(
               display.getText().length() );
         }
         catch( IOException io ) {
            display.append( io.toString() + "\n" );
            io.printStackTrace();
         }
      }
   }

   public static void main( String args[] )
   {
      Server app = new Server();

      app.addWindowListener(
         new WindowAdapter() {
            public void windowClosing( WindowEvent e )
            {
               System.exit( 0 );
            }
         }
      );

      app.waitForPackets();
   }
}

/**************************************************************************
 * (C) Copyright 1999 by Deitel & Associates, Inc. and Prentice Hall.     *
 * All Rights Reserved.                                                   *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/