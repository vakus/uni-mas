/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8036651
 */
public class SocketAgent extends MetaAgent {

    protected Portal portal;
    protected final WriteWorker writeWorker;
    protected final ReadWorker readWorker;
    protected Socket socket;

    private final Thread writeWorkerThread;
    private final Thread readWorkerThread;

    /**
     * Draws from the super class of MetaAgent, Constructor for the socket
     * agent.
     *
     * @param portal
     * @param socket
     * @author v8036651
     * @author v8073331
     */
    public SocketAgent(Portal portal, Socket socket) {
        super("SOCKET-AGENT");

        this.portal = portal;
        this.socket = socket;
        writeWorker = new WriteWorker(this);
        readWorker = new ReadWorker(this);
        readWorkerThread = new Thread(readWorker);
        writeWorkerThread = new Thread(writeWorker);
    }

    /**
     * This function starts all threads which are used by SocketAgent
     */
    public void start(){
        readWorkerThread.start();
        writeWorkerThread.start();
    }
    
    /**
     * Overwrites the messageHandler method from the super class of MetaAgent,
     * this adds a message to the message queue.
     *
     * @param agent
     * @param msg
     * @author v8073331
     */
    @Override
    public void messageHandler(MetaAgent agent, Message msg) {
        writeWorker.queueMessage(msg);
    }

    /**
     * this function will shutdown the socket which should result in this thread
     * stopping
     *
     * @author v8073331
     */
    public void close() {
        /**
         * Stop the read thread and await its termination
         */
        if (!readWorkerThread.getState().equals(Thread.State.TERMINATED)) {
            readWorker.stop();
            readWorkerThread.interrupt();
            
            try {
                readWorkerThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Stop the write thread and await its termination
         */
        if (!writeWorkerThread.getState().equals(Thread.State.TERMINATED)) {
            writeWorker.stop();
            writeWorkerThread.interrupt();

            try {
                writeWorkerThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Close the output stream
         */
        try {
            socket.getOutputStream().flush();
            socket.getOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(SocketAgent.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * Close the input stream
         */
        try {
            socket.getInputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(SocketAgent.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * Close the socket itself
         */
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/**
 * This class is built to read data from a socket while using blocking. This
 * class will be running as its own thread.
 *
 * @author v8073331
 */
class ReadWorker implements Runnable {

    private final SocketAgent agent;
    volatile boolean running;

    ReadWorker(SocketAgent agent) {
        this.agent = agent;
        running = true;
    }

    @Override
    public void run() {

        try {
            InputStream in = agent.socket.getInputStream();
            while (running) {

                /**
                 * The maximum message limit is 65536 bytes (~65KB) I decided
                 * that 65KB is a reasonable size of message as it is relatively
                 * large, but doesn't result in consumption of huge amount of
                 * RAM.
                 */
                byte[] data = new byte[65536];

                /**
                 * We want to use .read() as it is blocking, thus reducing the
                 * CPU usage; until data is available this thread should be
                 * suspended.
                 */
                int readlen = in.read(data);

                if (readlen == -1) {
                    /**
                     * The connection was closed, there is no reason to keep
                     * this thread alive.
                     */
                    return;
                }

                String messageStr = new String(data);
                messageStr = messageStr.trim();
                if (messageStr.equals("#")) {

                    agent.writeWorker.busy = false;
                    continue;

                } else if (messageStr.startsWith("#")) {

                    agent.writeWorker.busy = false;
                    messageStr = messageStr.substring(1);

                } else if (messageStr.endsWith("#")) {

                    agent.writeWorker.busy = false;
                    messageStr = messageStr.substring(0, messageStr.length() - 1);

                }

                System.out.println(messageStr);
                Message msg = Message.parseMessage(messageStr);
                agent.portal.messageHandler(agent, msg);

                agent.socket.getOutputStream().write("#".getBytes());
                agent.socket.getOutputStream().flush();

            }
        } catch (IOException ex) {
            Logger.getLogger(ReadWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        running = false;
    }
}

/**
 * This class is built to send data to a socket while using blocking. This class
 * will be running as its own thread.
 *
 * @author v8073331
 */
class WriteWorker implements Runnable {

    private final SocketAgent agent;
    private final ArrayBlockingQueue<Message> messageQueue;
    volatile boolean busy;
    volatile boolean running;

    public WriteWorker(SocketAgent agent) {
        this.agent = agent;
        messageQueue = new ArrayBlockingQueue(20);
        busy = false;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            if (!busy) {
                try {
                    agent.socket.getOutputStream().write(messageQueue.take().toString().getBytes());
                    agent.socket.getOutputStream().flush();
                    busy = true;
                } catch (IOException ex) {
                    /**
                     * The connection is closed no reason to keep this thread
                     * alive.
                     */
                    return;
                } catch (InterruptedException ex) {
                    Logger.getLogger(WriteWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void queueMessage(Message msg) {
        messageQueue.add(msg);
    }

    public void stop() {
        running = false;
    }
}
