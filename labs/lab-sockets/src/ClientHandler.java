import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

class ClientHandler extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    private String clientName;
    private volatile boolean running = true;

    public ClientHandler(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {
            clientName = in.readUTF();
            
            MultiTCPServer.addClient(clientName, this);
            
            while (running && !clientSocket.isClosed()) {
                String data = in.readUTF();
                if (data.equalsIgnoreCase("sair")) {
                    running = false;
                    break;
                }
                System.out.println(clientName + ": " + data);
                MultiTCPServer.broadcastMessage(clientName + ": " + data, this);
            }
        } catch (EOFException e) {
            System.out.println("Cliente '" + clientName + "' desconectou inesperadamente.");
        } catch (IOException e) {
            System.out.println("Erro com cliente '" + clientName + "': " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    public void sendMessage(String message) {
        try {
            synchronized (out) {
                out.writeUTF(message);
            }
        } catch (IOException e) {
            System.out.println("Erro ao enviar mensagem para '" + clientName + "': " + e.getMessage());
            cleanup();
        }
    }

    private void cleanup() {
        running = false;
        try {
            if (clientName != null) {
                MultiTCPServer.removeClient(clientName);
            }
            clientSocket.close();
        } catch (IOException e) {
            // Ignora erros no fechamento
        }
        System.out.println("Conexão com '" + clientName + "' encerrada.");
    }
}