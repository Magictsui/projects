
import java.io.*;
import java.net.*;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * CS 342 Spring 2016
 * Shen Wang (swang224@uic.edu), Yunxiao Cui (ycui22@uic.edu), Yue Yu (yyu31@uic.edu)
 * 
 * client class
 * implement the Runnable
 */

public class Client implements Runnable{
    
    public int port;
    public String serverAddr;
    public Socket socket;
    public GUI_client ui;
    public ObjectInputStream In;
    public ObjectOutputStream Out;
    
    public Client(GUI_client frame) throws IOException{
        ui = frame; 
        this.serverAddr = ui.serverAddr; 
        this.port = ui.port;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
            
        Out = new ObjectOutputStream(socket.getOutputStream());
        Out.flush();
        In = new ObjectInputStream(socket.getInputStream());

    }

    @Override
    public void run() {
        boolean keepRunning = true;
        while(keepRunning){
            try {
                Message msg = (Message) In.readObject();
                System.out.println("Incoming : "+msg.toString());
                
                if(msg.type.equals("message")){
                    if(msg.recipient.equals(ui.username)){
                        ui.addMessage("["+msg.sender +" > Me] : " + msg.content + "\n");
                    }
                    else{
                        ui.addMessage("["+ msg.sender +" > "+ msg.recipient +"] : " + msg.content + "\n");
                    }
                    /*                        
                    if(!msg.content.equals(".bye") && !msg.sender.equals(ui.username)){
                        String msgTime = (new Date()).toString();
                        
                        try{
                            DefaultTableModel table = (DefaultTableModel) ui.historyFrame.jTable1.getModel();
                            table.addRow(new Object[]{msg.sender, msg.content, "Me", msgTime});
                        }
                        catch(Exception ex){}  
                    }
                    */
                }
                else if(msg.type.equals("login")){
                    if(msg.content.equals("TRUE")){
                        ui.addMessage("[SERVER > Me] : Login Successful\n");
                    }
                    else{
                        ui.addMessage("[SERVER > Me] : Login Failed\n");
                    }
                }
                else if(msg.type.equals("newuser")){
                	System.out.println("********1");
                	System.out.println(ui.username);
                	System.out.println(msg.content);
                    if(!msg.content.equals(ui.username)){
                    	System.out.println("********2");
                        boolean exists = false;
                        for(int i = 0; i < ui.model.getSize(); i++){
                            if(ui.model.getElementAt(i).equals(msg.content)){
                                exists = true; break;
                            }
                        }
                        if(!exists){ ui.model.addElement(msg.content); }
                    }
                }
                else if(msg.type.equals("signout")){
                	System.out.println("********3");
                	System.out.println(ui.username);
                	System.out.println(msg.content);
                    if(msg.content.equals(ui.username)){
                    	System.out.println("********4");
                    	System.out.println(ui.username);
                    	System.out.println(msg.content);
                        ui.addMessage("["+ msg.sender +" > Me] : Bye\n");                       
                        
                        for(int i = 1; i < ui.model.size(); i++){
                            ui.model.removeElementAt(i);
                        }
                        
                        ui.clientThread.stop();
                    }
                    else{
                        ui.model.removeElement(msg.content);
                        ui.addMessage("["+ msg.sender +" > All] : "+ msg.content +" has signed out\n");
                    }
                }
                //else{
                //    ui.addMessage("[SERVER > Me] : Unknown message type\n");
                //}
            }
            catch(Exception ex) {
                keepRunning = false;
                ui.addMessage("[Application > Me] : Connection Failure\n");
                
                
                for(int i = 1; i < ui.model.size(); i++){
                    ui.model.removeElementAt(i);
                }
                
                ui.clientThread.stop();
                
                System.out.println("Exception SocketClient run()");
                ex.printStackTrace();
            }
        }
    }
    
    public void send(Message msg){
        try {
            Out.writeObject(msg);
            Out.flush();
            System.out.println("Outgoing : "+msg.toString());
            /*
            if(msg.type.equals("message") && !msg.content.equals(".bye")){
                String msgTime = (new Date()).toString();
                try{
                    //hist.addMessage(msg, msgTime);               
                    DefaultTableModel table = (DefaultTableModel) ui.historyFrame.jTable1.getModel();
                    table.addRow(new Object[]{"Me", msg.content, msg.recipient, msgTime});
                }
                catch(Exception ex){}
            }
            */
        } 
        catch (IOException ex) {
            System.out.println("Exception SocketClient send()");
        }
    }
    
    public void closeThread(Thread t){
        t = null;
    }
}
