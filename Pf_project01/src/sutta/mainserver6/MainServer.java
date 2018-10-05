package sutta.mainserver6;


import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Gameserver.Server;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;


/**
 *���� ���� Ŭ����
 */
public class MainServer extends Thread{
	public static final int JOIN = 0;
	public static final int ADDROOM = 1;
	public static final int QUICKJOIN = 2;
	
	/**
	 * Ŭ���̾�Ʈ �����
	 * �������� �����
	 */
	private ArrayList<User> user_list = new ArrayList<>();
	private ArrayList<Client> list = new ArrayList<>();
	private List<Paint> paint = new ArrayList<>();
	private List<Server> serverList = new ArrayList<>();
	private ServerSocket m_server;
	private ServerSocket w_server;
	private ArrayList<Room> roomList = new ArrayList<>();
	private ArrayList<String[]> roomList2 = new ArrayList<>();
	private List<Integer> roomPort = new ArrayList<>();	
	private File f;
	
	
	/**
	 * ������(Client)�� ���Ѵ�� �޾�
	 * �����(list)�� �����ϴ� ���� �Ѵ�
	 */
	private MainServer() {
		for(int i = 53001 ; i <= 54000; i++) {
			roomPort.add(i);
		}
		f = new File("UserFile","user.txt");
		if(f.exists()) {
			user_list = BackUpManager.getUserInfo(f);
		}
		else {
			user_list = new ArrayList<>();
		}
		
		try {
			m_server = new ServerSocket(54890);
//			����ؼ� �� ����� �ѷ��ִ� ���� ������ ���� 
			this.setDaemon(true);
			this.start();
			while(true) {
				Socket socket = m_server.accept();
				
				//������ ������ ����
				Client c = new Client();
				c.socket = socket;
				c.out = new ObjectOutputStream(socket.getOutputStream());
				c.in = new ObjectInputStream(socket.getInputStream());
				c.setDaemon(true);
				c.start();
				list.add(c);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * �� ����� ������ ��ü���� �ѷ��ִ� �޼ҵ�
	 * @throws IOException
	 */
	public void broadCast(){
		try {
			roomList2.clear();
			for(int i = 0; i<roomList.size(); i++) {
				Room t = roomList.get(i);
				String[] str = {(i+1)+"����",t.getName(),t.getCnt()+"/4"};
				roomList2.add(str);
			}
			for(int i=0; i<paint.size();i++) {
				paint.get(i).send();
			}
		}catch(Exception e) {
			e.getMessage();
		}
	}
	
	class Paint extends Thread{
		Socket w_socket;
		ObjectOutputStream w_out;
		ObjectInputStream w_in;
		
		public void send() {
			try {
				ArrayList<String[]> target = (ArrayList<String[]>) roomList2.clone();
				w_out.writeObject(target);
				w_out.flush();					
			}catch(Exception e) {
				e.getMessage();
			}
		}
		
		public void run() {
			while(true) {
				try {
					broadCast();
					//2�ʿ� �� �� �� - ���� ���� ���� 
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ����ؼ� �� ����� �ѷ��ִ� ���� �ϴ� �޼ҵ�(������)
	 * MainServer run()
	 */
	public void run() {
		try {
			w_server = new ServerSocket(54891);
			while(true) {
				Paint pa = new Paint();
				pa.w_socket = w_server.accept();
				pa.w_out = new ObjectOutputStream(pa.w_socket.getOutputStream());
				pa.w_in = new ObjectInputStream(pa.w_socket.getInputStream());
				pa.setDaemon(true);
				pa.start();
				paint.add(pa);
			}			
		} catch (IOException e) {
		}
	}

	
	
	/**
	 * ������ ���� ���� Ŭ������ �����带 ���
	 * �������� ���̵�� ��й�ȣ�� Ȯ���ϰ� ȸ�� ������ �������ش� 
	 */
	class Client extends Thread{
		Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;
		User user;
		boolean on = false;			//�������� �α��� ����
		
		/**
		 * model(����)�� ArrayList<Room>�� ���·� Ŭ���̾�Ʈ�� �����ϴ� �޼ҵ�
		 * @throws IOException
		 */
		public void send() {
			try {
				if(on == true) {
					ArrayList<String[]> target = (ArrayList<String[]>) roomList2.clone();
//					for(int i = 0 ; i < target.size();i++) {
//						System.out.println("target.get("+i+").cnt = "+target.get(i).getCnt());
//					}
//					System.out.println("send = "+target.hashCode()+" / "+target);
					out.writeObject(target);
					out.flush();	
				}				
			}catch(Exception e) {
				e.getMessage();
			}
		}
		
		/**
		 * ȸ������ �� ���̵� ��й�ȣ�� ��ġ�ϴ��� Ȯ���ϴ� �޼ҵ� 
		 */
		public void loginProc(){
			try {
				boolean isMember = false;
				//�α����� �Ϸ�� �� ���� ���� 
				while(!isMember) {
					//� ��ư�� �������Ŀ� ���� �ٸ���
					//0  ȸ�� ����
					//1 �α���
					int num = in.readInt();
					switch(num) {
					case Signal.ADDMEMBER:
						//ȸ������ ���Ե� ������ �޾ƿ´�(u1)
						User u1 = (User)in.readObject();
						//�̹� ���ԵǾ��ִ� ���̵� Ȥ�� �г������� Ȯ��
						boolean isUser = false;
						if(user_list.size()>0) {
							//ȸ������ 0�� �ƴ� �� ȸ�� ����
							for(int i = 0; i < user_list.size(); i++) {
								User target = user_list.get(i);
								//���� ���̵� Ȥ�� �г����� �����ϸ� ȸ������ ���� �ߴ�
								if(target.getId().equals(u1.getId())||target.getNickname().equals(u1.getNickname())) {
									isUser = true;
									break;
								}
							}
							//ȸ����Ͽ� ���� ��� ȸ�� ���� ����
							if(!isUser) {
								u1.setMoney(10000);//�ʱ� �� ���� (���Ƿ� ���� ���� ����)
								user_list.add(u1);
								BackUpManager.backUpUserInfo(f, user_list);

							}
							out.writeBoolean(!isUser);
							out.flush();
						}
						else {
							//ȸ���� ������ ����
							user_list.add(u1);
							BackUpManager.backUpUserInfo(f, user_list);
							out.writeBoolean(true);
							out.flush();
						}
						break;
					case Signal.LOGIN:
						//�α���
						User u2 = (User)in.readObject();
						
						//ȸ�� ��Ͽ��� ��ġ�ϴ� ���� �޾ƿ� �α��� ����
						for(int i = 0; i<user_list.size(); i++) {
							User user = user_list.get(i);
							if(user.getId().equals(u2.getId()) && user.getPw().equals(u2.getPw())) {
								isMember = true;
								u2 = user_list.get(i);
								break;
							}
						}
						//ȸ���� �� ���� ��Ų��  
						if(isMember) {
							user = u2;
						}
						out.writeBoolean(isMember);
						out.flush();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//Client run() : Ŭ���̾�Ʈ�κ��� �̺�Ʈ�� �޾ƿ� ��Ȳ�� �´� ó���� ���ش�
		public void run() {
			try {
				loginProc();
				//�� ����� 1ȸ(���۽� �ѷ��ش�)
				on = true;
				out.writeObject(user);
				out.flush();
				Process p  = new Process(this, roomList,roomPort, serverList);
				p.process();
			}catch(Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		MainServer m =new MainServer();
	}
}
