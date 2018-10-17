package sutta.mainserver6;


import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import sutta.gameserver2.Server;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;


/**
 *���� ���� Ŭ����
 */
public class MainServer extends JFrame implements Runnable{
	/**
	 * ���� ���� ���� ����Ʈ
	 * Ŭ���̾�Ʈ ���� ���� ����Ʈ
	 * ���� �����ϱ� ���� ���� ���� ����Ʈ
	 * ���� ��� ����Ʈ
	 * ���� ���� ����
	 * ���� ���ۿ� ���� ����
	 * �� ��� ���� ����Ʈ
	 * �� ��� ������ ���� �� ��� �̸� ���� ����Ʈ
	 * ���� ������ ���ǰ� ���� ���� ��Ʈ�� �����
	 * ����� ���� 
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
	
	private void setRoomPortList() {
		for(int i = 53001 ; i <= 54000; i++) {
			roomPort.add(i);
		}
	}
	private void setUserList() {
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
	}
	/**
	 * ������(Client)�� ���Ѵ�� �޾�
	 * �����(list)�� �����ϴ� ���� �Ѵ�
	 */
	private MainServer() {
		this.display();
		this.event();
		this.menu();
		this.setTitle("KG���� ����");
		this.setSize(500, 400);
		this.setLocationByPlatform(true);
		this.setVisible(true);
		
		setRoomPortList();//���Ӽ����� ���� ��Ʈ���� �����Ѵ�
		setUserList();	//����Ǿ� �ִ� ���� ������ �ҷ��´�
		
		try {
			m_server = new ServerSocket(54890);
//			����ؼ� �� ����� �ѷ��ִ� ���� ������ ���� 
			Thread tt = new Thread(this);
			tt.setDaemon(true);
			tt.start();
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
	
	public void setMoney(String id, int money) {
		for(User user : user_list) {
			if(user.getId().equals(id)) {
				user.setMoney(money);
				BackUpManager.backUpUserInfo(f, user_list);
				break;
			}
		}
		return;
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
	
	/**
	 * �� ����� �����ִ� Ŭ����
	 */
	class Paint extends Thread{
		Socket w_socket;
		ObjectOutputStream w_out;
		ObjectInputStream w_in;
		
		/**
		 * �� ����� Ŭ���̾�Ʈ���� �������ִ� �޼ҵ� 
		 */
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
					//2�ʿ� �� �� ��  �� ����� ��ü���� ����- ���� ���� ���� 
					broadCast();
					Thread.sleep(1500);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * MainServer�� run()
	 * �� ����� �ѷ��ִ� ������ �����Ű��
	 * ����ؼ� �����ڸ� �޴´�
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
	 * ������ ���� ���� Ŭ����
	 * �������� ���̵�� ��й�ȣ�� Ȯ���ϰ� ȸ�� ������ �������ش�
	 * �� �� Ŭ���̾�Ʈ�� �ϴ� �ൿ�� ���� ó�����ش�(Process)
	 */
	class Client extends Thread implements Signal{
		Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;
		User user;
		
		//ù ������ ��� ���� ���� �� ���� ���� ����ҿ� ���� �� ���
		public void newMember(User u1) {
			u1.setMoney(10000);//�ʱ� �� ���� (���Ƿ� ���� ���� ����)
			user_list.add(u1);
			BackUpManager.backUpUserInfo(f, user_list);
		}
		
		/**
		 * ȸ������ �� ���̵� ��й�ȣ�� ��ġ�ϴ��� Ȯ���ϴ� �޼ҵ� 
		 */
		public void loginProc(){
			try {
				boolean isMember = false;	//�̹� ȸ�� ������ �Ǿ��ִ� ���̵�����
				boolean isIng = true;		//���� �α��� ������
				//�α����� �Ϸ�� �� ���� ���� 
				while(!(isMember&&!isIng)) {
					//� ��ư�� �������Ŀ� ����
					//0  ȸ�� ����
					//1 �α���
					int num = in.readInt();
					switch(num) {
					case NEWMEMBERPROC:
						//ȸ������ ���Ե� ������ �޾ƿ´�(u1)
						User u1 = (User)in.readObject();
						//�̹� ���ԵǾ��ִ� ���̵� Ȥ�� �г������� Ȯ��
						boolean isUser = false;
						//ȸ������ 0�� �ƴ� �� ȸ�� ����
						if(user_list.size()>0) {
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
								newMember(u1);
							}
							out.writeBoolean(!isUser);
							out.flush();
						}
						else {
							//ȸ���� ������ ����
							newMember(u1);
							out.writeBoolean(true);
							out.flush();
						}
						break;
					case LOGINPROC:
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
							out.writeInt(NOTMEMBER);
							out.flush();
						}
						else if(isIng) {
							//ȸ���ε� �α��� ���� ���̵� �϶�
							out.writeInt(PLAYINGMEMBER);
							out.flush();
						}
						else {
							//ȸ���̸� �α����� �Ϸ�Ǿ��� ��
							out.writeInt(SUCCESSLOGIN);
							out.flush();
						}
						break;
					}
				}
			} catch (Exception e) {
				list.remove(this);
			}
			
		}
		
		public void logout() {
			try {
				BackUpManager.backUpUserInfo(f, user_list);//����� ���� ���
				list.remove(this);//���� �����ϴ� ����� ������ ����ҿ��� ����
				this.socket.close();
				this.interrupt();//������ ����
			} catch (Exception e) {
			}//���� ����
		}
		
		//Client run() : Ŭ���̾�Ʈ�κ��� �̺�Ʈ�� �޾ƿ� ��Ȳ�� �´� ó���� ���ش�
		public void run() {
			try {
				loginProc();
				//�α��� �ϸ� ����� ������ ����ڿ��� �����ش� 
				out.writeObject(user);
				out.flush();
				Process p  = new Process(this, roomList,roomPort, serverList, user_list, MainServer.this);
				p.process();
				//�α� �ƿ� �� process()�� �ߴ�
				logout();
			}catch(Exception e) {
				//���� �߻��� ��� �� 
				logout();
			}
		}
	}
	
	private Container con = this.getContentPane();
	private JButton exit = new JButton("���� ����");
	
	private void display() {
		con.add(exit);
	}

	private void event() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		exit.addActionListener(e->{
			for(Client cl : list) {
				try {
					cl.socket.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			for(Paint p : paint) {
				try {
					p.w_socket.close();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
			try {
				w_server.close();
				m_server.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
		});
	}

	private void menu() {
		
	}
	
	
	
	
	public static void main(String[] args) {
		MainServer m =new MainServer();
	}
}
