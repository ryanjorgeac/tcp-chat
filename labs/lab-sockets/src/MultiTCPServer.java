import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class MultiTCPServer {
    private static HashMap<String, ClientHandler> clients = new HashMap<>();
    
    public static void main(String args[]) {
        try {
            int serverPort = 6666; // the server port
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("=== SERVIDOR DE CHAT TCP ===");
            System.out.println("Servidor iniciado na porta " + serverPort);
            System.out.println("Aguardando clientes se conectarem...");
            System.out.println("Digite mensagens para enviar para todos os clientes conectados.");
            System.out.println("Digite 'sair' para encerrar o servidor.");
            System.out.println("==============================");

            // Thread para input do servidor
            Thread serverInputThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String serverMessage = scanner.nextLine();
                    if (serverMessage.equalsIgnoreCase("sair")) {
                        System.out.println("Encerrando servidor...");
                        System.exit(0);
                    }
                    broadcastMessage("Servidor: " + serverMessage, null);
                    System.out.println("Você: " + serverMessage);
                }
            });
            serverInputThread.setDaemon(true);
            serverInputThread.start();
            System.out.println("Aguardando conexao no endereco: " + InetAddress.getLocalHost() + ":" + serverPort);

            while (serverSocket.isBound()) {
                
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }
    
    // Método para adicionar cliente
    public static synchronized void addClient(String clientName, ClientHandler handler) {
        clients.put(clientName, handler);
        System.out.println("Cliente '" + clientName + "' conectado. Total de clientes: " + clients.size());
        broadcastMessage("Servidor: " + clientName + " entrou no chat!", handler);
    }
    
    // Método para remover cliente
    public static synchronized void removeClient(String clientName) {
        clients.remove(clientName);
        System.out.println("Cliente '" + clientName + "' desconectado. Total de clientes: " + clients.size());
        broadcastMessage("Servidor: " + clientName + " saiu do chat!", null);
    }
    
    // Método para enviar mensagem para todos os clientes
    public static synchronized void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients.values()) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}