
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
 * server gui class handle the server actions
 * 
 */

public class GUI_server extends JFrame{
	private JTextField textField, textField_1;
	private JButton btnSS; 
	private JTextArea textArea;
	private JLabel lblServerIp, lblSocket, lblTextArea, lblUserList;
	//private JList list;
	//DefaultListModel<String> model = new DefaultListModel<>();
    public Server server;
    Thread serverThread;
    static GUI_server frm;
    int port;
    String username;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frm = new GUI_server();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void RetryStart(int port){
        if(server != null){ server.stop(); }
        server = new Server(this, port);
    }

	public void addMessage(String msg){
		textArea.append(msg);
    }
	
	public String gettextField(){
		return textField.getText();
    }
	
	public String gettextField_1(){
		return textField_1.getText();
    }
	
	/**
	 * Create the application.
	 */
	@SuppressWarnings("unchecked")
	public GUI_server() {

		setBounds(100, 100, 700, 609);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		//
		// buttons
		//
		btnSS = new JButton("Start Server");
		btnSS.setBounds(496, 10, 144, 31);
		getContentPane().add(btnSS);
		/*
		btnCS = new JButton("Close Server");
		btnCS.setBounds(496, 51, 144, 31);
		getContentPane().add(btnCS);
		*/
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
		textArea.setBounds(10, 137, 630, 379);
		getContentPane().add(textArea);
		/*
		//
		//text list
		//
		list = new JList(model);
		model.addElement("All");
		list.setSelectedIndex(0);
		list.setBounds(454, 140, 122, 233);
		getContentPane().add(list);
		getContentPane().add(lblUserList);
		*/
		setVisible(true);
		
		btnSS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server = new Server(frm);
                textField.setText("I'm server");
                setTitle("Server");

                textField.setEnabled(false);
                textField_1.setEnabled(false);
                btnSS.setEnabled(false);
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
