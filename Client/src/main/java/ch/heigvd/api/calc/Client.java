package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator client implementation
 */
public class Client {

    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    /**
     * Main function to run client
     *
     * @param args no args required
     */
    public static void main(String[] args) throws IOException {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        BufferedReader stdin = null;

        sendHttpRequest();


        /* TODO: Implement the client here, according to your specification
         *   The client has to do the following:
         *   - connect to the server
         *   - initialize the dialog with the server according to your specification
         *   - In a loop:
         *     - read the command from the user on stdin (already created)
         *     - send the command to the server
         *     - read the response line from the server (using BufferedReader.readLine)
         */

        stdin = new BufferedReader(new InputStreamReader(System.in));

    }

    public static void sendHttpRequest() throws IOException {
        Socket clientSocket = null;
        BufferedWriter out = null;
        BufferedReader in = null;
        int port = 1111;

        try {

            System.out.print("Entrer l'adresse ip du serveur: ");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String ip = bufferRead.readLine();

            System.out.print("Veuillez entrer votre calcul: ");
            String calcul = bufferRead.readLine();
            
            // Open Socket
            clientSocket = new Socket(ip, port);

            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            out.write(calcul);
            out.flush();

            LOG.log(Level.INFO, "*** Response sent by the server: ***");
            String line;
            while ((line = in.readLine()) != null) {
                LOG.log(Level.INFO, line);
            }

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.toString(), ex);
            }
            try {
                if (in != null) in.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.toString(), ex);
            }
            try {
                if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.toString(), ex);
            }
        }
    }
}