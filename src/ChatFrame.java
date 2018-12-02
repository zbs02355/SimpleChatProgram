import javax.swing.*;

import java.awt.Panel;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ChatFrame extends JFrame implements ActionListener{
	//界面的控件
	JTextField tField;
	JTextArea tArea;
	JScrollPane sPane;
	JButton send;
	JPanel panel;
	//连接需要的一些参数
	int port;  //描述当前端口
	String sText = ""; //发送的内容
	String current_ID;
	Date date; 
	ServerSocket serverSocket;
	Socket mySocket;
	BufferedReader is ; //输入流
	PrintWriter os ; //输出流
	String line; //输入流的内容
	//构造方法
	public ChatFrame(String ID, String remoteID, String IP, int port, boolean isServer) {
		super(ID);
		current_ID = ID;
		this.port = port;
		tArea = new JTextArea();
		tArea.setEditable(false);
		sPane = new JScrollPane(tArea);
		this.setSize(400, 300);
		this.setResizable(false);
		this.getContentPane().add(sPane, "Center");
		panel = new JPanel();
		this.getContentPane().add(panel, "South");
		send = new JButton("发送");
		tField = new JTextField(25);
		tField.requestFocus(); //文本框获得焦点
		panel.add(tField);
		panel.add(send);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		//为显示区域和发送按钮添加监听事件
		tField.addActionListener(this);
		send.addActionListener(this);
		try {
			//设置显示风格
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch (Exception e) {
			System.out.println("UI Errors!");
		}
		//判断当前是服务器还是客户端
		if(isServer) {
			try {
				serverSocket = null;
				try {
					serverSocket = new ServerSocket(port);
				} catch (Exception e) {
					System.out.println("服务器端异常！" + e);
				}
				mySocket = null;
				try {
					mySocket = serverSocket.accept();
				} catch (Exception e) {
					System.out.println("服务器端异常！" + e);
				}
				is = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
				os = new PrintWriter(mySocket.getOutputStream());
				
			} catch (Exception e) {
				System.out.println("服务器端异常！" + e);
			}
		}
		else {
			try {
				mySocket = new Socket(IP, port);
				//输入输出流
				is = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
				os = new PrintWriter(mySocket.getOutputStream());
			} catch (Exception e) {
				System.out.println("客户端异常！" + e);
			}
		}
		//读取两者之间发送的内容
		while(true) {
			try {
				line = is.readLine();
				date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String current_time = format.format(date);
				sText += current_time + " " + remoteID + "say:" + line + "\n";
				tArea.setText(sText);
			} catch (Exception e) {
				System.out.println("接收信息异常！" + e);
			}
		}
	}
	public void actionPerformed(ActionEvent event) {
		date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String current_time = format.format(date);
		sText += current_time + " " + current_ID + "say:" + tField.getText() + "\n";
		tArea.setText(sText);
		os.println(tField.getText());
		os.flush();
		tField.setText("");
		tField.requestFocus();
	}
}
