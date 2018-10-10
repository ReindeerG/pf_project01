package sutta.mainserver6;


import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import sutta.gameserver2.Server;
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
			for(User u : user_list) {
				//����ġ ���� ���� �ٿ��� �Ͼ�� �� �α��� ���� ��� falseó��
				u.setLogin(false);
			}
			System.out.println("user_list = "+user_list);
		}
		else {
			user_list = new ArrayList<>();
			System.out.println("user_list = "+user_list);
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
	 *
	 */
	public void broadCast(){
		try {
			roomList2.clear();
			for(int i = 0; i<roomList.size(); i++) {
				Room t = roomList.get(i);
				String[] str = {(i+1)+"����",t.getName(),t.getIng(),t.getCnt()+"/4"};
				roomList2.add(str);
			}
			for(int i=0; i<paint.size();i++) {
				paint.get(i).send();
			}				
		}catch(Exception e) {
			e.printStackTrace();
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
					//1�ʿ� �� �� �� - ���� ���� ���� 
					Thread.sleep(1500);
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
					out.writeObject(target);
					out.flush();	
				}				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * ȸ������ �� ���̵� ��й�ȣ�� ��ġ�ϴ��� Ȯ���ϴ� �޼ҵ� 
		 */
		public void loginProc(){
			try {
				boolean isMember = false;
				boolean isIng = true;
				//�α����� �Ϸ�� �� ���� ���� 
				while(!(isMember&&!isIng)) {
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
							u1.setMoney(10000);
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
								if(!user.isLogin()) {
									u2 = user_list.get(i);
									isIng = false;
								}
								else {
									isIng = true;
								}
								break;
							}
						}
						
						//ȸ���̸鼭 ���� ���������� ���� ȸ���� �α��� ����
						if(isMember && !isIng) {
							user = u2;
							user.setLogin(true);
						}
						
						if(isMember == false) {
							//ȸ���� �ƴ� ��
							out.writeInt(0);
							out.flush();
						}
						else if(isIng) {
							//ȸ���ε� �α��� ���� ���̵� �϶�
							out.writeInt(1);
							out.flush();
						}
						else {
							//ȸ���̸� �α����� �Ϸ�Ǿ��� ��
							out.writeInt(2);
							out.flush();
						}
						break;
					}
				}
			} catch (Exception e) {
//				e.printStackTrace();
				list.remove(this);
//				System.out.println("�α��� â �����ؼ� Ŭ���̾�Ʈ ����");
			}
			
		}
		
		//Client run() : Ŭ���̾�Ʈ�κ��� �̺�Ʈ�� �޾ƿ� ��Ȳ�� �´� ó���� ���ش�
		public void run() {
			try {
				loginProc();
				on = true;
				//�α��� �ϸ� ����� ������ ����ڿ��� �����ش� 
				out.writeObject(user);
				out.flush();
				Process p  = new Process(this, roomList,roomPort, serverList, user_list);
				p.process();
				BackUpManager.backUpUserInfo(f, user_list);
				System.out.println("user_list = "+ user_list);
				System.out.println("���� �α׾ƿ�");
				list.remove(this);
				System.out.println("list.size = "+list.size());
				this.interrupt();
			}catch(Exception e) {
				System.out.println("run����");
				System.out.println("�α׾ƿ�");
				list.remove(this);
				System.out.println("list.size = "+list.size());
				this.interrupt();
			}
		}
	}
	
	public static void main(String[] args) {
		MainServer m =new MainServer();
	}
}
