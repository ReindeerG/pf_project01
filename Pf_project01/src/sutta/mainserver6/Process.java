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

/**
 * ���� Ŭ���̾�Ʈ�� �ൿ�� ���� ��ȣ�� �޾ƿ�
 * �׿� �°� ó�����ִ� Ŭ����
 */
public class Process implements Signal{
	private Client c;
	private ArrayList<Room> roomList;
	private List<Integer> roomPort;
	private List<Server> serverList;
	private Room r;
	private Server server;
	private ArrayList<User> userList;
	private MainServer main;
	private boolean isPlay = true;
	
	
	
	public Process(Client c, ArrayList<Room> roomList,List<Integer> roomPort, List<Server> serverList, ArrayList<User> userList, MainServer main) {
		this.c = c;
		this.roomList = roomList;
		Collections.sort(roomPort);
		this.roomPort = roomPort;
		this.serverList = serverList;
		this.userList = userList;
		this.main = main;
	}
	
	//�濡�� ���� �� �޼ҵ�
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
	
	//�濡 ������ �� �޼ҵ�
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
	
	//���ο� ���� ���鶧 �޼ҵ�
	public void newRoom(String name) throws ClassNotFoundException, IOException {
		if(roomPort.size() > 0) {
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
		else {
			//���� 1000�� �� ����� ���� ��
			c.out.writeObject(null);
			c.out.flush();
		}
	}
	//������ �����ߴ��� ����
	public void setIng(boolean ing) {
		r.setIng(ing);
	}
	//������ ������ �� �������� �˸�
	public void logout() {
		c.user.setLogin(false);
		isPlay = false;
	}
	
	public void process(){
		try {
			while(isPlay) {	//���� �ϱ� ������ �ݺ�
				int choose = c.in.readInt();
				switch(choose) {
				case JOIN:
					//�ش� �� ���� ����, ä�� ������ ����
					int index = c.in.readInt();
					if(index != -1) {
						joinRoom(index);
					}
					break;
					
				case ADDROOM:
					//���ο� �� �����
					String roomName = (String)c.in.readObject();
					if(roomName!=null) {
						newRoom(roomName);					
					}
					break;
					
				case QUICKJOIN:
					//���� �� ���� 
					Room target2 = null;
					for(Room r2 : roomList) {
						System.out.println(r2.getName()+"�� ���� ���� ����"+(r2.getCnt() < 4 && !r2.isIng()));
						if(r2.getCnt() < 4 && !r2.isIng()) {
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
				case EXITROOM:
					//�� ���� ����
					exitRoom();
					break;
				case GAMESTART:
					//���� ����
					setIng(true);
					break;
				case GAMEEND:
					//���� ����
					setIng(false);
					break;
				case LOGOUT:
					//�α׾ƿ�
					logout();
					break;
				}
				
			}
			
		}catch(Exception e) {
			if(r!=null) {
				exitRoom();
			}
			logout();
		}
	}
	
}
