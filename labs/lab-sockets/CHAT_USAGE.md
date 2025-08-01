# TCP Multi-Client Chat Application

## Overview
This is a TCP-based multi-client chat application that allows real-time communication between a server and multiple clients simultaneously. All participants can communicate with each other in a shared chat room.

## Features
- **Multi-client chat room**: Multiple clients can join the same chat and communicate with each other
- **Server participation**: The server can also send messages to all connected clients
- **Client identification**: Each client must provide a name or ID when connecting
- **Real-time messaging**: Messages are broadcast to all participants immediately
- **Join/leave notifications**: Automatic notifications when users join or leave the chat
- **Graceful disconnection**: Type 'sair' to disconnect cleanly

## How to Use

### Starting the Server
1. Open a terminal
2. Navigate to the source directory:
   ```bash
   cd "labs/lab-sockets/src"
   ```
3. Compile the files (if not already done):
   ```bash
   javac *.java
   ```
4. Start the server:
   ```bash
   java MultiTCPServer
   ```
5. Once clients connect, the server can type messages that will be broadcast to all connected clients
6. Type 'sair' to shut down the server

### Starting the Client
1. Open a new terminal
2. Navigate to the source directory:
   ```bash
   cd "labs/lab-sockets/src"
   ```
3. Start the client:
   ```bash
   java SimpleTCPClient
   ```
4. When prompted, enter your name or identification
5. You can start typing messages immediately

### Chat Commands
- When connecting, provide your name/ID when asked
- Type any message and press Enter to send it to all participants
- Type `sair` to disconnect and exit the chat
- Server messages appear as "`Servidor`: `message`"
- Client messages appear as "`ClientName`: `message`"
- Join/leave notifications are automatically sent to all participants

## Example Usage (Multi-Client Chat)

### Server Terminal:
```
=== SERVIDOR DE CHAT TCP ===
Servidor iniciado na porta 6666
Aguardando clientes se conectarem...
Digite mensagens para enviar para todos os clientes conectados.
Digite 'sair' para encerrar o servidor.
==============================
Aguardando conexao no endereco: /127.0.0.1:6666
Cliente 'João' conectado. Total de clientes: 1
Aguardando conexao no endereco: /127.0.0.1:6666
Cliente 'Maria' conectado. Total de clientes: 2
João: Olá pessoal!
Maria: Oi João!
Você: Bem-vindos ao chat!
João: Obrigado servidor!
```

### Client 1 Terminal (João):
```
[C1] Conectando com servidor 127.0.0.1:6666
[C2] Conexão estabelecida, eu sou o cliente: /127.0.0.1:45678
Digite seu nome ou identificação: João
Chat started!
Digite 'sair' para encerrar o chat.
==============================
Servidor: Maria entrou no chat!
Você: Olá pessoal!
Maria: Oi João!
Servidor: Bem-vindos ao chat!
Você: Obrigado servidor!
```

### Client 2 Terminal (Maria):
```
[C1] Conectando com servidor 127.0.0.1:6666
[C2] Conexão estabelecida, eu sou o cliente: /127.0.0.1:45679
Digite seu nome ou identificação: Maria
Chat started!
Digite 'sair' para encerrar o chat.
==============================
João: Olá pessoal!
Você: Oi João!
Servidor: Bem-vindos ao chat!
João: Obrigado servidor!
```

## Technical Details
- **Server**: `MultiTCPServer.java` - Manages multiple client connections and broadcasts messages
- **Client Handler**: `ClientHandler.java` - Handles individual client communication and message broadcasting
- **Client**: `SimpleTCPClient.java` - Connects to server and manages chat interface
- **Protocol**: TCP/IP using DataInputStream and DataOutputStream
- **Port**: 6666 (configurable in the code)
- **Threading**: Each client connection runs in a separate thread
- **Message Broadcasting**: All messages are sent to all connected clients simultaneously
- **Synchronization**: Thread-safe client management and message broadcasting
