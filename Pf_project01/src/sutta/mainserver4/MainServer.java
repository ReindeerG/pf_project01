package sutta.mainserver4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;


/**
 *���� ���� Ŭ����
 */
public class MainServer  extends Thread{
	public static final int JOIN = 0;
	public static final int ADDROOM = 1;
	public static final int QUICKJOIN = 2;
	
	/**
	 * Ŭ���̾�Ʈ �����
	 * �������� �����
	 */
	private ArrayList<Client> list = new ArrayList<>();
	private ServerSocket m_server;
	private ArrayList<Room> roomList = new ArrayList<>();
	private ArrayList<String> roomList2 = new ArrayList<>();

	/**
	 * ȸ�� ���� �����
	 */
	private ArrayList<User> user_list = new ArrayList<>();
	
	
	
	
	/**
	 * ������(Client)�� ���Ѵ�� �޾�
	 * �����(list)�� �����ϴ� ���� �Ѵ�
	 */
	private MainServer() {
		
		try {
			m_server = new ServerSocket(53890);
			//����ؼ� �� ����� �ѷ��ִ� ������ ����
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
				roomList2.add((i+1)+"����        "+t.getName()+"                "+t.getCnt()+"/4");
			}
			System.out.println("list.size = "+list.size());
			for(int i=0; i<list.size();i++) {
				list.get(i).send();
			}
		}catch(Exception e) {
			e.getMessage();
		}
	}
	
	/**
	 * ����ؼ� �� ����� �ѷ��ִ� ���� �ϴ� �޼ҵ�(������)
	 */
	public void run() {
		while(true) {
			try {
				broadCast();
				//1�ʿ� �� �� �� - ���� ���� ���� 
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for(int i = 0; i<list.size(); i++) {
				if(list.get(i).socket.isClosed()) {
					Client removed = list.get(i);
					list.remove(removed);
				}
			}
		}
	}

	
	/**
	 * ������ ���� ���� Ŭ������ �����带 ���
	 * �������� ���̵�� ��й�ȣ�� Ȯ���ϰ� ȸ�� ������ �������ش� 
	 */
	class Client extends Thread{
		Socket socket;
		Socket g_socket = null;		//���Ӽ��� ���� ����
		ObjectInputStream in;
		ObjectOutputStream out;
		User user;
		boolean on = false;			//�������� �α��� ����
		private InetAddress inet;	//���Ӽ��� �ּ�
		private int g_port = 53891;	//���Ӽ��� ��Ʈ
		
		/**
		 * model(����)�� ArrayList<Room>�� ���·� Ŭ���̾�Ʈ�� �����ϴ� �޼ҵ�
		 * @throws IOException
		 */
		public void send() {
			try {
				if(on == true) {
					ArrayList<Room> target = (ArrayList<Room>) roomList2.clone();
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
							}
							out.writeBoolean(!isUser);
							out.flush();
							user = u1;
						}
						else {
							//ȸ���� ������ ����
							user_list.add(u1);
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
		
		
		//Ŭ���̾�Ʈ�κ��� �̺�Ʈ�� �޾ƿ� ��Ȳ�� �´� ó���� ���ش�
		public void run() {
			try {
				loginProc();
				//�� ����� 1ȸ(���۽� �ѷ��ش�)
				on = true;
				send();
				Process p  = new Process(in, roomList, user);
				p.process();
			}catch(Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		MainServer m =new MainServer();
	}
}
