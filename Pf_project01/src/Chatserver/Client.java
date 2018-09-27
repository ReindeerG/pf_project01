package Chatserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {
	public static void main(String[] args) throws Exception {
		
		
		int port = 53892;
		InetAddress inet = InetAddress.getByName("192.168.0.14");
		Socket socket = new Socket(inet, port);
		
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		out.writeObject(new Message("���̵�", "/����޼���"));
		out.flush();
		
		Thread t1 = new Thread() {
			public void run() {
				while(true) {
					try {
							ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
							Message msg = (Message)in.readObject();
							String time = msg.getTime();
							String fromnick = msg.getNickname();
							String read = msg.getMsg();
							if(read.equals("/����޼���")) {
								System.out.println("["+time+"] ("+fromnick+"���� �����ϼ̽��ϴ�.)");
							}
							else {
								System.out.println("["+time+"] <"+fromnick+"> "+read);
							}
							
					} catch(Exception e ) {}
				}
			}
		};
		t1.setDaemon(true);
		t1.start();
		
		
		
		while(true) {
			String msg = JOptionPane.showInputDialog("");
			out.writeObject(new Message("���̵�", msg));
			out.flush();
		}
		
	}
}
