package sutta.mainclient6;

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
			InetAddress inet = InetAddress.getByName("localhost");
			ObjectOutputStream out = null;
			ObjectInputStream in = null;

			Socket socket = new Socket(inet, 54890);
				
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
