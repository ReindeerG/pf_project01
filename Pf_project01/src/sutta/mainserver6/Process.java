package sutta.mainserver6;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sutta.gameserver2.Server;
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
	private Server server;
	private ArrayList<User> userList;
	private MainServer main;
	
	
	
	public Process(Client c, ArrayList<Room> roomList,List<Integer> roomPort, List<Server> serverList, ArrayList<User> userList, MainServer main) {
		this.c = c;
		this.roomList = roomList;
		Collections.sort(roomPort);
		this.roomPort = roomPort;
		this.serverList = serverList;
		this.userList = userList;
		this.main = main;
	}
	
	public void exitRoom(){
		r.minusCnt();
		System.out.println(c.user.getNickname()+"�� ���� �� = "+c.user.getMoney());
		server.removeUser(c.user);
		if(r.getCnt() == 0) {
			roomPort.add(r.getPort());
			roomList.remove(r);
			serverList.remove(server);
			server.serverClose();
		}
		server = null;
		r = null;
	}
	
	public void joinRoom(int index) {
		r = roomList.get(index);
		server = serverList.get(index);
		server.addUser(c.user);
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
		Server sv = new Server(port, r.getUserList(), main);
		server =sv;
		sv.setDaemon(true);
		sv.start();
		serverList.add(sv);
		r.setPort(port);
		roomList.add(r);
		//�� �� ����
		joinRoom(roomList.indexOf(r));
	}
	
	public void process(){
		boolean isPlay = true;
		try {
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
					//�� ����
					r.setIng(true);
					server.setInggame(true);
					break;
				case 5:
					//���� ����
					r.setIng(false);
					server.setInggame(false);
					break;
				case 6:
					//�α׾ƿ�
					c.user.setLogin(false);
					isPlay = false;
					break;
				}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			if(r!=null) {
				exitRoom();
			}
			c.user.setLogin(false);
			isPlay = false;
		}
	}
	
}
