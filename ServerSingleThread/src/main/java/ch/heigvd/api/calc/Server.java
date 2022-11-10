package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - single threaded
 */
public class Server {

    private final static Logger LOG = Logger.getLogger(Server.class.getName());

    private final int LISTEN_PORT = 1111;

    /**
     * Main function to start the server
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        (new Server()).start();
    }

    /**
     * Start the server on a listening socket.
     */
    private void start() {
        LOG.info("Starting server...");

        ServerSocket serverSocket = null;

        LOG.log(Level.INFO, "Creating a server socket and binding it on any of the available interface and on port {0}",
                new Object[]{Integer.toString(LISTEN_PORT)});
        try {
            serverSocket = new ServerSocket(LISTEN_PORT);
            LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{serverSocket.getLocalSocketAddress()});
            LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(serverSocket.getLocalPort())});
            LOG.log(Level.INFO, "               is bound: {0}", new Object[]{serverSocket.isBound()});

            while (true) {
                LOG.log(Level.INFO, "Waiting (blocking) for a connection request on {0} : {1}",
                        new Object[]{serverSocket.getInetAddress(), Integer.toString(serverSocket.getLocalPort())});
                handleClient(serverSocket.accept());
            }
        }
        catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    /**
     * Handle a single client connection: receive commands and send back the result.
     *
     * @param clientSocket with the connection with the individual client.
     */
    private void handleClient(Socket clientSocket) {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        LOG.log(Level.INFO, "A client has arrived. We now have a client socket with following attributes:");
        LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{clientSocket.getLocalAddress()});
        LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(clientSocket.getLocalPort())});
        LOG.log(Level.INFO, "  Remote Socket address: {0}", new Object[]{clientSocket.getRemoteSocketAddress()});
        LOG.log(Level.INFO, "            Remote port: {0}", new Object[]{Integer.toString(clientSocket.getPort())});

        LOG.log(Level.INFO, "Getting a Reader and a Writer connected to the client socket...");
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
        }
        catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }

        LOG.log(Level.INFO, "Waiting for client input...");
        try {
            String line;
            if ((line = reader.readLine()) != null) {
                LOG.log(Level.INFO, line);
            }
            //throw new RuntimeException("REQUEST treatment not implement yet");
            String[] results = line.split(":");
            if (results.length == 2 && Objects.equals(results[0], "REQUEST")) {
                //TODO : REQUEST treatment
            }
            else {
                try {
                    writer.write("REFUSED");
                    writer.flush();
                    LOG.log(Level.INFO, "Refused client");
                }
                catch (Exception e) {
                    LOG.log(Level.SEVERE, e.toString(), e);
                }
            }
        }
        catch(Exception ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
            try {
                //TODO : send ERROR:problem instead of REFUSED
                writer.write("REFUSED");
                writer.flush();
                LOG.log(Level.INFO, "Refused client");
            }
            catch (Exception e) {
                LOG.log(Level.SEVERE, e.toString(), e);
            }
        }
        finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.toString(), ex);
            }
            try {
                if (reader != null) reader.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.toString(), ex);
            }
            try {
                if (!clientSocket.isClosed()) clientSocket.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.toString(), ex);
            }
        }
    }
}