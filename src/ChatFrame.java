import javax.swing.*;

import java.awt.Panel;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ChatFrame extends JFrame implements ActionListener{
	//����Ŀؼ�
	JTextField tField;
	JTextArea tArea;
	JScrollPane sPane;
	JButton send;
	JPanel panel;
	//������Ҫ��һЩ����
	int port;  //������ǰ�˿�
	String sText = ""; //���͵�����
	String current_ID;
	Date date; 
	ServerSocket serverSocket;
	Socket mySocket;
	BufferedReader is ; //������
	PrintWriter os ; //�����
	String line; //������������
	//���췽��
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
		send = new JButton("����");
		tField = new JTextField(25);
		tField.requestFocus(); //�ı����ý���
		panel.add(tField);
		panel.add(send);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		//Ϊ��ʾ����ͷ��Ͱ�ť��Ӽ����¼�
		tField.addActionListener(this);
		send.addActionListener(this);
		try {
			//������ʾ���
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch (Exception e) {
			System.out.println("UI Errors!");
		}
		//�жϵ�ǰ�Ƿ��������ǿͻ���
		if(isServer) {
			try {
				serverSocket = null;
				try {
					serverSocket = new ServerSocket(port);
				} catch (Exception e) {
					System.out.println("���������쳣��" + e);
				}
				mySocket = null;
				try {
					mySocket = serverSocket.accept();
				} catch (Exception e) {
					System.out.println("���������쳣��" + e);
				}
				is = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
				os = new PrintWriter(mySocket.getOutputStream());
				
			} catch (Exception e) {
				System.out.println("���������쳣��" + e);
			}
		}
		else {
			try {
				mySocket = new Socket(IP, port);
				//���������
				is = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
				os = new PrintWriter(mySocket.getOutputStream());
			} catch (Exception e) {
				System.out.println("�ͻ����쳣��" + e);
			}
		}
		//��ȡ����֮�䷢�͵�����
		while(true) {
			try {
				line = is.readLine();
				date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String current_time = format.format(date);
				sText += current_time + " " + remoteID + "say:" + line + "\n";
				tArea.setText(sText);
			} catch (Exception e) {
				System.out.println("������Ϣ�쳣��" + e);
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
