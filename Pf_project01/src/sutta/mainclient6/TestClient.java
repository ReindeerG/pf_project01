package sutta.mainclient6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.UIManager;

import sutta.useall.Signal;

/**
 * Ŭ���̾�Ʈ â
 * 
 */
//�� ��� ������Ʈ ��Ȳ ���� ���� ����Ǵ��� Ȯ���ϱ� 

public class TestClient {
	

	public static void main(String[] args) {
		try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception e) {}
		try {
			File f = new File("Address","address.txt");
			String ipadd = null;
			int port = 0;
			if(f.exists()) {
				BufferedReader fin = new BufferedReader(new FileReader(f));
				
				ipadd = fin.readLine();
				port = Integer.parseInt(fin.readLine());
				if(ipadd == null || port == 0 || port == -1) {
					System.exit(0);
				}
			}
			else {
				System.exit(0);
			}
			
			InetAddress inet = InetAddress.getByName(ipadd);
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
			

			Socket socket = new Socket(inet, port);
				
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			Login login  = new Login(out,in);
		 	login.setVisible(true);
			
			while(login.isLogin() != Signal.SUCCESSLOGIN) {}
			MainWindow m = new MainWindow(socket, out, in);
			m.setVisible(true);
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
