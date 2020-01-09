/**
 * This is the Driver class and is used to run the program in a specific way.
 */
package ica.main;

import ica.messages.Message;
import ica.messages.MessageType;
import ica.metaagent.MetaAgent;
import ica.metaagent.Portal;
import ica.metaagent.Router;
import ica.metaagent.SocketAgent;
import ica.metaagent.User;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8073331
 */
public class DevMain {

    private static Router router;
    private static Thread routerThread;
    private static ArrayList<Portal> portals;
    private static ArrayList<User> users;
    private static ArrayList<SocketAgent> socketAgents;
    private static Scanner keyb;

    public static void main(String[] args) {
        keyb = new Scanner(System.in);

        portals = new ArrayList<>();
        users = new ArrayList<>();
        socketAgents = new ArrayList<>();

        while (true) {
            createMenu();
            String line = keyb.nextLine();
            if (line.equalsIgnoreCase("1")) {
                if (router == null) {
                    //create new router
                    System.out.print("Enter router name: ");
                    String routername = keyb.nextLine();
                    try {
                        router = new Router(routername);
                        routerThread = new Thread(router);
                        routerThread.start();
                    } catch (IOException ex) {
                        Logger.getLogger(DevMain.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("Unable to start router");
                    }
                } else {
                    //disconnect our router
                }
            } else if (line.equalsIgnoreCase("2")) {
                //create new portal
                System.out.print("Enter portal name: ");
                String portalname = keyb.nextLine();
                Portal portal = new Portal(portalname);
                portals.add(portal);
            } else if (line.equalsIgnoreCase("3") && !portals.isEmpty()) {
                //create new User Agent
                System.out.println("Enter User Agent name: ");
                String username = keyb.nextLine();
                Portal portal = choosePortal();
                User user = new User(username, portal);
                users.add(user);

                if (askYN("Do you want to auto register user to portal?", true)) {
                    //register the user to portal
                    portal.messageHandler(user, new Message(
                            user.getName(),
                            "GLOBAL",
                            MessageType.ADD_METAAGENT,
                            ""));
                } else {
                    System.out.println("User not registered to portal.");
                    System.out.println("To register user manually you must send"
                            + " new message from the user you wish to register "
                            + "with message type ADD_METAAGENT source the name "
                            + "valid name of agent, and recipient 'GLOBAL'. The"
                            + " content of the message is ignored");
                }

            } else if (line.equalsIgnoreCase("4") && !portals.isEmpty()) {
                //connect portal to router
                Portal portal = choosePortal();
                String ip = getIP();

                try {
                    Socket s = new Socket(ip, 42069);
                    SocketAgent a = new SocketAgent(portal, s);
                    a.start();

                    socketAgents.add(a);
                    if (askYN("Do you want to auto register portal to router", true)) {
                        a.messageHandler(portal, new Message(portal.getName(),
                                "GLOBAL", MessageType.ADD_PORTAL, ""));
                    } else {
                        System.out.println("Portal not registered to router.");
                        System.out.println("To register portal manuall you must"
                                + " send new message from the portal you wish"
                                + " to register with message type ADD_PORTAL,"
                                + " source must be valid name of the portal and"
                                + " recipient must be 'GLOBAL'. The content of"
                                + " the message is ignored");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(DevMain.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Could not create socket connection");
                }
            } else if (line.equalsIgnoreCase("5") && router != null) {
                //connect router to router

                String ip = getIP();
                try {
                    Socket s = new Socket(ip, 42069);
                    SocketAgent sa = new SocketAgent(router, s);
                    sa.start();

                    if (askYN("Do you want to auto request router addresses", true)) {
                        sa.messageHandler(sa, new Message(router.getName(), "GLOBAL", MessageType.REQUEST_ROUTER_ADDRESSES, ""));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(DevMain.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (line.equalsIgnoreCase("6") && (router != null || !portals.isEmpty())) {
                //send message
                System.out.print("Sender: ");
                String sender = keyb.nextLine();
                System.out.print("Reciever: ");
                String reciever = keyb.nextLine();
                MessageType type = chooseMessageType();
                String content = "";
                System.out.println("Enter your message. Enter empty line to send");
                while (true) {
                    String nextl = keyb.nextLine();
                    if (nextl.isEmpty()) {
                        break;
                    }
                    content += nextl;
                }
                System.out.println("Choose node to send the message to: ");
                MetaAgent forward = chooseNode();
                System.out.println("Choose node to send message from: ");
                MetaAgent from = chooseNode();

                forward.messageHandler(from, new Message(sender, reciever, type, content));

            } else if (line.equalsIgnoreCase("7") && !portals.isEmpty()) {
                //disconnect portal from router

                Portal portal = choosePortal();
                SocketAgent agent = getSocketFromPortal(portal);

                agent.messageHandler(portal, new Message(portal.getName(), "GLOBAL", MessageType.REMOVE_PORTAL, ""));
                agent.close();
                socketAgents.remove(agent);

            } else if (line.equalsIgnoreCase("8") && !users.isEmpty()) {
                //disconnect user agent
                User user = chooseUserAgent();
                MetaAgent forward = chooseNode();
                forward.messageHandler(user, new Message(user.getName(), "GLOBAL", MessageType.REMOVE_METAAGENT, ""));
            } else if (line.equalsIgnoreCase("9")) {
                //disconnect socket agent
                System.out.println("Not implemented.");
            } else if (line.equalsIgnoreCase("0")) {
                //KILL AND EXIT
                System.exit(0);
            } else {
                System.out.println("Unrecognised input. Try again.");
            }
        }
    }

    public static void createMenu() {
        System.out.println("What do you want to do?");
        if (router == null) {
            System.out.println("[1]\tCreate new Router");
        } else {
            System.out.println("[1]\tDisconnect Router");
        }
        System.out.println("[2]\tCreate new Portal");
        if (portals.isEmpty()) {
            System.out.println("[-]\t!Must have portal first!");
            System.out.println("[-]\t!Must have portal first!");
        } else {
            System.out.println("[3]\tCreate new User Agent");
            System.out.println("[4]\tConnect Portal to Router");
        }
        if (router != null) {
            System.out.println("[5]\tConnect Router to Router");
        } else {
            System.out.println("[-]\t!Must have local router first!");
        }
        if (router == null && portals.isEmpty()) {
            System.out.println("[-]\t!Must have local router or portal first");
        } else {
            System.out.println("[6]\tSend message");
        }
        if (portals.isEmpty()) {
            System.out.println("[-]\t!Must have portal connected to router first!");
        } else {
            System.out.println("[7]\tDisconnect Portal from Router");
        }
        if (users.isEmpty()) {
            System.out.println("[-]\t!Must have User Agent first!");
        } else {
            System.out.println("[8]\tDisconnect User Agent");
        }
        System.out.println("[9]\tKill Socket Agent");
        System.out.println("[0]\tKILL AND EXIT");
    }

    public static Portal choosePortal() {
        if (portals.isEmpty()) {
            System.out.println("There is no portals to be choosen!");
            return null;
        } else if (portals.size() == 1) {
            System.out.println("Automatically choosing only portal available (" + portals.get(0).getName() + ")");
            return portals.get(0);
        } else {
            System.out.println("Choose portal to use: ");
            for (int x = 0; x < portals.size(); x++) {
                System.out.println("[" + x + "] " + portals.get(x).getName());
            }
            while (true) {
                System.out.print("Portal number: ");
                while (!keyb.hasNextInt()) {
                    System.out.println("Invalid input.");
                    keyb.nextLine();
                    System.out.print("Portal number: ");
                }
                int choice = keyb.nextInt();
                keyb.nextLine();
                if (choice > portals.size() || choice < 0) {
                    System.out.println("Invalid number.");
                    continue;
                }
                return portals.get(choice);
            }
        }
    }

    public static User chooseUserAgent() {
        if (users.isEmpty()) {
            System.out.println("There is no user agents to be choosen!");
            return null;
        } else if (users.size() == 1) {
            System.out.println("Automatically choosing only user available (" + users.get(0) + ")");
            return users.get(0);
        } else {
            System.out.println("Choose user to use: ");
            for (int x = 0; x < users.size(); x++) {
                System.out.println("[" + x + "] " + users.get(x).getName());
            }
            while (true) {
                System.out.println("User number: ");
                while (!keyb.hasNextInt()) {
                    System.out.println("Invalid input.");
                    keyb.nextLine();
                    System.out.println("User number: ");
                }
                int choice = keyb.nextInt();
                keyb.nextLine();
                if (choice > users.size() || choice < 0) {
                    System.out.println("Invalid number.");
                    continue;
                }
                return users.get(choice);
            }
        }
    }

    public static boolean askYN(String question, boolean def) {
        while (true) {
            System.out.print(question + (def ? " [Y/n]" : " [y/N]") + ": ");
            String resp = keyb.nextLine();
            if (resp.equalsIgnoreCase("y")) {
                return true;
            } else if (resp.equalsIgnoreCase("n")) {
                return false;
            } else if (resp.isEmpty()) {
                return def;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    public static String getIP() {
        System.out.println("Enter IP or leave blank for 127.0.0.1");
        String ip = keyb.nextLine();
        return (ip.isEmpty()) ? "127.0.0.1" : ip;
    }

    public static MessageType chooseMessageType() {
        while (true) {
            System.out.println("Choose message type:");
            System.out.println("[0] USER_MSG");
            System.out.println("[1] ADD_METAAGENT");
            System.out.println("[2] REMOVE_METAAGENT");
            System.out.println("[3] ERROR");
            System.out.println("[4] ADD_PORTAL");
            System.out.println("[5] REMOVE_PORTAL");
            System.out.println("[6] LOAD_TABLE");
            System.out.println("[7] ADD_ROUTER");
            System.out.println("[8] LOAD_ADDRESSES");
            System.out.println("[9] REQUEST_ROUTER_ADDRESSES");

            while (!keyb.hasNextInt()) {
                keyb.nextLine();
            }

            int choice = keyb.nextInt();
            keyb.nextLine();
            switch (choice) {
                case 0:
                    return MessageType.USER_MSG;
                case 1:
                    return MessageType.ADD_METAAGENT;
                case 2:
                    return MessageType.REMOVE_METAAGENT;
                case 3:
                    return MessageType.ERROR;
                case 4:
                    return MessageType.ADD_PORTAL;
                case 5:
                    return MessageType.REMOVE_PORTAL;
                case 6:
                    return MessageType.LOAD_TABLE;
                case 7:
                    return MessageType.ADD_ROUTER;
                case 8:
                    return MessageType.LOAD_ADDRESSES;
                case 9:
                    return MessageType.REQUEST_ROUTER_ADDRESSES;
            }
        }
    }

    public static MetaAgent chooseNode() {
        while (true) {
            System.out.println("Choose node: ");
            if (router != null) {
                System.out.println("[0] " + router.getName());
            }
            for (int x = 1; x < portals.size() + 1; x++) {
                System.out.println("[" + x + "] " + portals.get(x - 1).getName());
            }
            for (int x = portals.size() + 1; x < users.size() + portals.size() + 1; x++) {
                System.out.println("[" + x + "] " + users.get(x - 1 - portals.size()).getName());
            }
            for (int x = portals.size() + users.size() + 1; x < socketAgents.size() + users.size() + portals.size() + 1; x++) {
                System.out.println("[" + x + "] " + socketAgents.get(x - 1 - portals.size() - users.size()).getName());
            }

            while (!keyb.hasNextInt()) {
                keyb.nextLine();
            }
            int choice = keyb.nextInt();
            keyb.nextLine();
            if (choice == 0 && router != null) {
                return router;
            } else if (choice < portals.size() + 1) {
                return portals.get(choice - 1);
            } else if (choice < users.size() + portals.size() + 1) {
                return users.get(choice - 1 - portals.size());
            } else if (choice < socketAgents.size() + users.size() + portals.size() + 1) {
                return socketAgents.get(choice - users.size() - portals.size() - 1);
            }
        }
    }

    public static SocketAgent getSocketFromPortal(Portal portal) {
        for (SocketAgent sa : socketAgents) {
            if (sa.getName().contains(portal.getName())) {
                return sa;
            }
        }
        return null;
    }
}
