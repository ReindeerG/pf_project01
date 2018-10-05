package sutta.mainserver6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Gameserver.Server;
import sutta.mainserver6.MainServer.Client;
import sutta.useall.Room;
import sutta.useall.Signal;

public class Process {
	private Client c;
	private ArrayList<Room> roomList;
	private List<Integer> roomPort;
	private List<Server> serverList;
	
	
	public Process(Client c, ArrayList<Room> roomList,List<Integer> roomPort, List<Server> serverList) {
		this.c = c;
		this.roomList = roomList;
		Collections.sort(roomPort);
		this.roomPort = roomPort;
		this.serverList = serverList;
	}
	
	public void removeRoom() {
//		���Ӽ����κ��� ���� ������ ���� �޾Ƽ� ������ ���� 0�̸� �� ����
	}
	
	public void joinRoom(int index) {
		roomList.get(index).plusCnt();
		roomList.get(index).addUser(c.user);
		if(roomList.get(index).getCnt() == 4) {
		roomList.get(index).setIng(true);
		}
		try {
			c.out.writeObject(roomList.get(index));
			c.out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void newRoom(String name) throws ClassNotFoundException, IOException {
		Room r = new Room(name, roomPort.get(0));
		roomPort.remove(0);
		//�� ���� �޾Ƽ� port�ο�
		int port = r.getNumber();
		Server sv = new Server(port);
		serverList.add(sv);
		System.out.println("���Ӽ��� ����Ʈ�� ����");
		r.setPort(port);
		roomList.add(r);
		//�� �� ����
		joinRoom(roomList.indexOf(r));
	}
	
	public void process() throws Exception{
		while(true) {
			int choose = c.in.readInt();
//			System.out.println("choose = "+choose);
			switch(choose) {
			case Signal.JOIN:
				//�ش� �� ���� ����, ä�� ������ ����
				int index = c.in.readInt();
				if(index != -1) {
					joinRoom(index);
				}
				break;
				
			case Signal.ADDROOM:
				//���ο� �� �����
				String roomName = (String)c.in.readObject();
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
			case 3:
				//�� ���� ���� ��Ʈ�� �޾Ƽ� �� ��Ʈ�� �ش��ϴ� ���� ����(cnt��)�� ���ش�
				//��Ʈ�� �ٽ� roomPort����Ʈ�� �������ش� 
			}
			
		}
	}
	
}
