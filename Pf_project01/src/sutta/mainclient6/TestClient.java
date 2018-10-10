package sutta.mainclient6;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import sutta.useall.User;

/**
 * Ŭ���̾�Ʈ â
 * 
 */
//�� ��� ������Ʈ ��Ȳ ���� ���� ����Ǵ��� Ȯ���ϱ� 

public class TestClient {

	public static void main(String[] args) {
		try {

			InetAddress inet = InetAddress.getByName("localhost");
			ObjectOutputStream out = null;
			ObjectInputStream in = null;

			Socket socket = new Socket(inet, 54890);
				
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			Login login  = new Login(out,in);
		 	login.setVisible(true);
			
			while(!login.isLogin()) {}
			MainWindow m = new MainWindow(socket, out, in);
			m.setVisible(true);
				
			
//			socket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
