/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.metaagent;

import ica.messages.Message;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8036651
 * @author v8073331
 */
public class SocketAgent extends MetaAgent {

    /**
     * The portal which is running on the same end as this socket. This variable
     * also may point towards a Router (extension of portal)
     */
    protected Portal portal;

    /**
     * The class responsible for writing data to the socket and processing it.
     */
    protected final WriteWorker writeWorker;
    /**
     * The class responsible for reading data from the socket and processing it.
     */
    protected final ReadWorker readWorker;
    /**
     * The socket which is used to send and receive data.
     */
    protected Socket socket;

    /**
     * Thread pointer for {@link #writeWorker}.
     */
    private final Thread writeWorkerThread;

    /**
     * Thread pointer for {@link #readWorker}
     */
    private final Thread readWorkerThread;

    /**
     * Draws from the super class of MetaAgent, Constructor for the socket
     * agent.
     *
     * @param portal the portal which is on this side of the socket.
     * @param socket already existing socket connection which will be sending or
     * receiving data.
     * @author v8036651
     * @author v8073331
     */
    public SocketAgent(Portal portal, Socket socket) {
        super("Socket from: " + portal.getName());

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
    public void start() {
        readWorkerThread.start();
        writeWorkerThread.start();
    }

    /**
     * Adds message to the {@link WriteWorker#messageQueue messageQueue} of
     * {@link #writeWorker}
     *
     * @param agent the agent which is sending/forwarding the message
     * @param msg the message to be forwarded.
     * @author v8073331
     */
    @Override
    public void messageHandler(MetaAgent agent, Message msg) {
        writeWorker.queueMessage(msg);
    }

    /**
     * <p>
     * This function will shutdown the socket and the underlying threads.
     * </p>
     * <p>
     * BUG: For some reason the threads did not want to be stopped in clean
     * manner; the call to {@link ReadWorker#stop()} and
     * {@link WriteWorker#stop()} did not result in changing value of
     * {@link ReadWorker#running} and {@link WriteWorker#running} respectively.
     * This resulted in threads to not exit, but consume CPU resources. For this
     * reason for now the threads are interrupted to force crash them.
     * {@link Thread#interrupt() see here for more information}.
     * </p>
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

    /**
     * The parent Socket Agent.
     */
    private final SocketAgent agent;

    /**
     * Represents if the thread should continue to execute, or start
     * terminating.
     */
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
                    agent.writeWorker.running = false;
                    return;
                }

                String messageStr = new String(data);
                messageStr = messageStr.trim();
                if (messageStr.equals("#")) {

                    synchronized (agent.writeWorker) {
                        agent.writeWorker.notify();
                    }
                    continue;

                } else if (messageStr.startsWith("#")) {

                    synchronized (agent.writeWorker) {
                        agent.writeWorker.notify();
                    }
                    messageStr = messageStr.substring(1);

                } else if (messageStr.endsWith("#")) {

                    synchronized (agent.writeWorker) {
                        agent.writeWorker.notify();
                    }
                    messageStr = messageStr.substring(0, messageStr.length() - 1);

                }

                Message msg = Message.parseMessage(messageStr);
                agent.portal.messageHandler(agent, msg);

                agent.socket.getOutputStream().write("#".getBytes());
                agent.socket.getOutputStream().flush();

            }
        } catch (IOException ex) {
            Logger.getLogger(ReadWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets {@link #running} to false to initiate thread termination.
     */
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

    /**
     * The parent Socket Agent.
     */
    private final SocketAgent agent;
    /**
     * The queue of messages to be sent.
     */
    private final ArrayBlockingQueue<Message> messageQueue;
    /**
     * Represents if the thread should continue to execute, or start
     * terminating.
     */
    volatile boolean running;

    public WriteWorker(SocketAgent agent) {
        this.agent = agent;
        messageQueue = new ArrayBlockingQueue<>(100);
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            synchronized (agent.writeWorker) {
                try {
                    agent.socket.getOutputStream().write(messageQueue.take().toString().getBytes());
                    agent.socket.getOutputStream().flush();
                    agent.writeWorker.wait();
                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(WriteWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void queueMessage(Message msg) {
        messageQueue.add(msg);
    }

    /**
     * Sets {@link #running} to false to initiate thread termination.
     */
    public void stop() {
        running = false;
    }
}
