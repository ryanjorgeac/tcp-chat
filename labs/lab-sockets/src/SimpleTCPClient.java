import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SimpleTCPClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private volatile boolean running = true;

    public void start(String serverIp, int serverPort) throws IOException {
        // Cria socket de comunicacao com o servidor e obtem canais de entrada e saida
        System.out.println("[C1] Conectando com servidor " + serverIp + ":" + serverPort);
        socket = new Socket(serverIp, serverPort);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        System.out.println("[C2] Conexão estabelecida, eu sou o cliente: " + socket.getLocalSocketAddress());
        
        // Ask for client identification
        Scanner identificationScanner = new Scanner(System.in);
        System.out.print("Digite seu nome ou identificação: ");
        String clientId = identificationScanner.nextLine();
        System.out.println("Digite 'sair' para encerrar o chat.");
        System.out.println("==============================");
        
        output.writeUTF(clientId);

        Thread receiveThread = new Thread(() -> {
            try {
                while (running && !socket.isClosed()) {
                    String message = input.readUTF();
                    System.out.println(message);
                }
            } catch (IOException e) {
                if (running) {
                    System.out.println("Conexão com servidor perdida.");
                }
                running = false;
            }
        });
        receiveThread.setDaemon(true);
        receiveThread.start();
        
        // Thread principal para enviar mensagens
        Scanner scanner = new Scanner(System.in);
        while (running && !socket.isClosed()) {
            String msg = scanner.nextLine();

            if (msg.equalsIgnoreCase("sair")) {
                running = false;
                output.writeUTF(msg);
                break;
            } else if ()
            
            try {
                output.writeUTF(msg);
            } catch (IOException e) {
                System.out.println("Erro ao enviar mensagem: " + e.getMessage());
                running = false;
                break;
            }
        }
        scanner.close();
        identificationScanner.close();
    }
 
    public void stop() {
        running = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverIp = "127.0.0.1"; // Changed to localhost for local testing
        int serverPort = 6666;
        try {
            // Cria e roda cliente
            SimpleTCPClient client = new SimpleTCPClient();
            client.start(serverIp, serverPort);
            
            // Finaliza cliente
            client.stop();
            System.out.println("Cliente desconectado.");
        } catch (IOException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        }
    }
}
