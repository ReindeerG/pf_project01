package sutta.mainserver6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sutta.gameserver.Server;
import sutta.mainserver6.MainServer.Client;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;

public class Process {
	private Client c;
	private ArrayList<Room> roomList;
	private List<Integer> roomPort;
	private List<Server> serverList;
	private Room r;
	
	
	public Process(Client c, ArrayList<Room> roomList,List<Integer> roomPort, List<Server> serverList) {
		this.c = c;
		this.roomList = roomList;
		Collections.sort(roomPort);
		this.roomPort = roomPort;
		this.serverList = serverList;
	}
	
	public void exitRoom() throws Exception {
		Room target = r;
		c.user = (User) c.in.readObject();
		System.out.println("c.user = "+c.user);
		target.removeUser(c.user);
		target.minusCnt();
		System.out.println("���� �� �ο� = "+target.getCnt());
		if(target.getCnt() == 0) {
			roomPort.add(target.getPort());
			int index = roomList.indexOf(target);
			roomList.remove(target);
			serverList.get(index).serverClose();
			serverList.remove(index);
		}
	}
	
	public void joinRoom(int index) {
		r = roomList.get(index);
		if(r.isIng()) {
			try {
				c.out.writeObject(null);
				c.out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			r.plusCnt();
			r.addUser(c.user);
			try {
				c.out.writeObject(r);
				c.out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
	}
	
	public void newRoom(String name) throws ClassNotFoundException, IOException {
		r = new Room(name, roomPort.get(0));
		roomPort.remove(0);
		//�� ���� �޾Ƽ� port�ο�
		int port = r.getPort();
		Server sv = new Server(port);
		sv.setDaemon(true);
		sv.start();
		serverList.add(sv);
		r.setPort(port);
		roomList.add(r);
		//�� �� ����
		joinRoom(roomList.indexOf(r));
	}
	
	public void process() throws Exception{
		boolean isPlay = true;
		while(isPlay) {
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
				System.out.println("�� ������");
				exitRoom();
				break;
			case 4:
				boolean isPlaying = c.in.readBoolean();
				r.setIng(isPlaying);
				break;
				//������ ���۵Ǹ� �� ���¸� ���������� ����
				//������ ������ �� ���¸� ��������� ���� 
				//room.isIng();
				//room.setIng(����);
			case 5:
				//�α׾ƿ�
				c.user.setLogin(false);
				c.interrupt();
				isPlay = false;
			}
			
		}
	}
	
}
