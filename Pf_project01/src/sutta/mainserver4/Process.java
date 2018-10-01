package sutta.mainserver4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Process {
	private ObjectInputStream in;
	private ArrayList<Room> roomList;
	
	public Process(ObjectInputStream in, ArrayList<Room> roomList) {
		this.in = in;
		this.roomList = roomList;
	}
	
	public void joinRoom(int index) {
		if(index > roomList.size()) {
			System.out.println("�� ��� refresh�� �� �õ�");
		}
		else {
			System.out.println((index+1)+"���� ���� �Ϸ�");
			roomList.get(index).cnt++;
			if(roomList.get(index).cnt == 4) {
				roomList.get(index).ing = true;
			}			
		}
//		inet = InetAddress.getByName("192.168.0.?");
//		g_socket = new Socket(inet,g_port);
//		c_socket = new Socket(inet,c_port);
	}
	
	public void newRoom(String name) throws ClassNotFoundException, IOException {
		Room r = new Room();
		r.name = name;
		r.cnt = 0;
		r.ing = false;
		//�� ���� �޾Ƽ� list�� ����
		roomList.add(r);
		//�� �� ����
		joinRoom(roomList.indexOf(r));
	}
	
	public void process() throws Exception{
		while(true) {
			int choose = in.readInt();
//			System.out.println("choose = "+choose);
			switch(choose) {
			case MainServer.JOIN:
				//�ش� �� ���� ����, ä�� ������ ����
				int index = in.readInt();
				if(index != -1) {
					joinRoom(index);
				}
				break;
				
			case MainServer.ADDROOM:
				//���ο� �� �����
				newRoom((String)in.readObject());
				break;
				
			case MainServer.QUICKJOIN:
//				System.out.println("���� ������ �� �ִ� �� ����");
				Room target2 = null;
				for(Room r2 : roomList) {
					if(r2.cnt < 4) {
						target2 = r2;
						break;
					}
				}
				if(target2 != null) {
					joinRoom(roomList.indexOf(target2));
				}
				else {
					newRoom("����");
				}
				break;
			}
			
		}
	}
	
}
