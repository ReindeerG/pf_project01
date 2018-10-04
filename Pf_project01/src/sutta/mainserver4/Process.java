package sutta.mainserver4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;

public class Process {
	private ObjectInputStream in;
	private ArrayList<Room> roomList;
	private User user;
	
	public Process(ObjectInputStream in, ArrayList<Room> roomList, User user) {
		this.in = in;
		this.roomList = roomList;
		this.user = user;
	}
	
	public void removeRoom() {
//		���Ӽ����κ��� ���� ������ ���� �޾Ƽ� ������ ���� 0�̸� �� ����
	}
	
	public void joinRoom(int index) {
		if(roomList.get(index).getCnt()>=4) {
			System.out.println("����");
		}
		else {
			System.out.println((index+1)+"���� ���� �Ϸ�");
			roomList.get(index).plusCnt();
			roomList.get(index).addUser(user);
			System.out.println(roomList.get(index).getCnt()+"��");
			if(roomList.get(index).getCnt() == 4) {
				roomList.get(index).setIng(true);		
			}
//		inet = InetAddress.getByName("192.168.0.?");
//		g_socket = new Socket(inet,g_port);
//		c_socket = new Socket(inet,c_port);
//		�����ϰ� ���� ����(������ ���� ����)
		}
	}
	
	public void newRoom(String name) throws ClassNotFoundException, IOException {
		Room r = new Room(name);
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
			case Signal.JOIN:
				//�ش� �� ���� ����, ä�� ������ ����
				int index = in.readInt();
				if(index != -1) {
					joinRoom(index);
				}
				break;
				
			case Signal.ADDROOM:
				//���ο� �� �����
				String roomName = (String)in.readObject();
				if(roomName!=null) {
					newRoom(roomName);					
				}
				break;
				
			case Signal.QUICKJOIN:
//				System.out.println("���� ������ �� �ִ� �� ����");
				Room target2 = null;
				for(Room r2 : roomList) {
					if(r2.getCnt() < 4) {
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
