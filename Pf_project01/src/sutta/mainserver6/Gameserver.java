package sutta.mainserver6;

import java.net.ServerSocket;
import java.net.Socket;

public class Gameserver {
	
	private ServerSocket g_server;
	private Socket[] socket = new Socket[4];
	private int cnt = 0;
	
	public void waitPlayer() throws Exception{
		if(cnt<4) {
			socket[cnt] = g_server.accept();
			cnt++;			
		}
		System.out.println(cnt+"�� ���� ��");
	}
	
	
	public Gameserver(int port){
		try {
			g_server = new ServerSocket(port);
			System.out.println(port+"��Ʈ�� ���Ӽ��� ����");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
