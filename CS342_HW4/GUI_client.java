
import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

/**
 * 
 * CS 342 Spring 2016
 * Shen Wang (swang224@uic.edu), Yunxiao Cui (ycui22@uic.edu), Yue Yu (yyu31@uic.edu)
 * 
 * client gui class handle the client actions
 * 
 */

public class GUI_client extends JFrame{
	private JTextField textField, textField_1;
	private JButton btnLogin, btnLogOff, btnSend; 
	private JTextArea textArea, textArea_1;
	private JLabel lblServerIp, lblSocket, lblTextArea, lblInput, lblUserList;
	
	public DefaultListModel<String> model = new DefaultListModel<>();
	//public DefaultListModel model;
	public JList list;
	public Client client;
	public Thread clientThread;
    static GUI_client frm;
    int port;
    String username;
    String serverAddr;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frm = new GUI_client();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Add the message to the text area
	 */
	public void addMessage(String msg){
		textArea.append(msg);
    }
	
	
	/**
	 * Create the application.
	 */
	@SuppressWarnings("unchecked")
	public GUI_client() {

		setBounds(100, 100, 700, 609);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		//
		// buttons
		//
	    btnLogin = new JButton("Login");
	    btnLogin.setBounds(352, 10, 105, 31);
		getContentPane().add(btnLogin);
		
		btnLogOff = new JButton("Logoff");
		btnLogOff.setBounds(352, 51, 105, 31);
		getContentPane().add(btnLogOff);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(454, 408, 122, 48);
		getContentPane().add(btnSend);
		
		//
		// labels
		//
		lblServerIp = new JLabel("Server IP");
		lblServerIp.setBounds(10, 10, 83, 31);
		getContentPane().add(lblServerIp);
		
		lblSocket = new JLabel("Socket");
		lblSocket.setBounds(10, 51, 83, 31);
		getContentPane().add(lblSocket);
		
		lblTextArea = new JLabel("Text Area");
		lblTextArea.setBounds(10, 96, 83, 31);
		getContentPane().add(lblTextArea);
		
		lblInput = new JLabel("Input");
		lblInput.setBounds(10, 380, 83, 31);
		getContentPane().add(lblInput);
		
		lblUserList = new JLabel("User List");
		lblUserList.setBounds(454, 96, 83, 31);
		
		//
		// text field
		//
		textField = new JTextField();
		textField.setText("127.0.0.1");
		textField.setBounds(103, 10, 180, 31);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setText("10007");
		textField_1.setColumns(10);
		textField_1.setBounds(103, 51, 180, 31);
		getContentPane().add(textField_1);
		
		//
		// text area
		//
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 137, 371, 233);
		getContentPane().add(textArea);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(10, 408, 371, 48);
		getContentPane().add(textArea_1);
		
		//
		//text list
		//
		model.addElement("All");
		list = new JList(model);	
		list.setSelectedIndex(0);
		list.setBounds(454, 140, 122, 233);
		getContentPane().add(list);
		getContentPane().add(lblUserList);
		
		setVisible(true);
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serverAddr = textField.getText(); 
				port = Integer.parseInt(textField_1.getText());

                //create new client
				try {
					client = new Client(frm);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            clientThread = new Thread(client);
	            clientThread.start();               
               
                //get user name
                while(true){
                	username=JOptionPane.showInputDialog(null, "please enter the user name", "Login", JOptionPane.QUESTION_MESSAGE);
                	if(username==null)
                		JOptionPane.showMessageDialog(null, "user can't be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                	if(username.equals("Server")==true){
                		JOptionPane.showMessageDialog(null, "user can't be Server", "ERROR", JOptionPane.ERROR_MESSAGE);
                		username=null;
                	}
                	if(username!=null)
                		break;
                }
                
                //client.send(new Message("test", "testUser", "testContent", "SERVER"));
                client.send(new Message("login", username, "", "SERVER"));
                //client.setUserName(username);
                // start the client process 
                //client.start();
                setTitle(username+"'s chat room");
                // become a client
                
                textField.setText("I'm client");
                
                // enable corresponding buttons
                textField.setEnabled(false);
                textField_1.setEnabled(false);
                btnLogin.setEnabled(false);
                btnLogOff.setEnabled(true);
			}
		});
		
		btnLogOff.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				
				client.send(new Message("signout", username, ".bye", "SERVER"));
        		btnLogin.setEnabled(true);
        		btnLogOff.setEnabled(false);
        		textArea.setText("");
        		textArea_1.setText("");
        		textField.setText("127.0.0.1");
        		textField.setEnabled(true);
        		textField.setEditable(true);
        		textField_1.setText("10007");
        		textField_1.setEnabled(true);
        		textField_1.setEditable(true);
        		
			}
		});
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = textArea_1.getText();
		        String target = list.getSelectedValue().toString();
		        
		        if(!msg.isEmpty() && !target.isEmpty()){
		        	textArea_1.setText("");
		            client.send(new Message("message", username, msg, target));
		        }
				/*
				textArea.append(username+": " + textArea_1.getText() + "\n");
                if (isserver)
                    server.dataout(textArea_1.getText());
                else
                    client.dataout(textArea_1.getText());
                */
                //textArea_1.setText("");
			}
		});
		
		
		
		/*
		if (this.server!=null)
		{
			System.out.println(this.server.getClients().size());
			for(ClientThread val : server.getClients())
			{
				System.out.println("*****");
				model.addElement(val.getName());
			}
		}*/	
	}			
}