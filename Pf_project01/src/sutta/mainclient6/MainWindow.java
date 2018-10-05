package sutta.mainclient6;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import Gameserver.Client_Ex;
import Gamewindow.Mainwindow;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;

/**
 * ���� ȭ�� â(�ӽ�)
 */
public class MainWindow extends JFrame implements Runnable{
	/**
	 * ȭ�� â ������ ��� ����, ����
	 */
	ObjectOutputStream out;
	ObjectInputStream in;
	Socket socket;
	private InetAddress g_inet;
	private InetAddress w_inet;
//	������Ʈ ��ġ�� ����
	private ArrayList<String[]> room_list;
	private Container con = this.getContentPane();
	private String[] names = {"�� ��ȣ","�� �̸�","�����ڼ�"};
	private DefaultTableModel model1 =new DefaultTableModel(names,0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private DefaultTableModel model2 =new DefaultTableModel(names,0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private DefaultTableModel tg;
	private JTable room = new JTable(model2);
	private JButton join = new JButton("������");
	private JButton addRoom = new JButton("�� �����");
	private JButton quick = new JButton("���� �÷���");
	private JLabel label = new JLabel("�� ��� ");
	private JScrollPane pane = new JScrollPane(room);
	private JPanel panel = new JPanel();
	private JLabel nickname = new JLabel();
	private JLabel money = new JLabel();
	
	private User user;
	

	
	

	
	private void display() {
		con.setLayout(null);
//		con.add(pane);
		con.add(join);
		con.add(addRoom);
		con.add(quick);
		con.add(label);
		con.add(panel);
		con.add(nickname);
		con.add(money);
		
		
		room.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.setLayout(new BorderLayout());
		panel.add(room.getTableHeader(), BorderLayout.NORTH);
		panel.add(pane, BorderLayout.CENTER);
		room.setToolTipText("���� Ŭ���Ͽ� �濡 ����");
		
		
		label.setBounds(12, 0, 800, 25);
		panel.setBounds(12, 27, 800, 678);
		join.setBounds(825, 38, 141, 36);
		addRoom.setBounds(825, 84, 141, 36);
		quick.setBounds(825, 130, 141, 36);
		

		
	}

	/**
	 * �� ���� �޼ҵ�
	 */
	private void joinRoom() {
		try {
			out.writeInt(Signal.JOIN);
			out.flush();
			
			//����Ʈ���� ���� �� ���� �ε����� ���� �� ���� �ε����� �������ش� 
			int index = room.getSelectedRow();
			
			if(room_list != null) {
				if(index == -1) {
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "������ ���� ������ �ּ���");
				}
				else if(room_list.get(index)[2].equals("4/4")) {
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "�̹� �� �� ���Դϴ�");
					}
				else {
					out.writeInt(room.getSelectedRow());
					out.flush();
					Room r = (Room)in.readObject();
					System.out.println(r.getName()+"�濡 ����");
					//�� ������ �޾ƿ� �� ������ ���ӽ����ش�
					Client_Ex client = new Client_Ex(r.getPort(), user.getNickname());
					client.setDaemon(true);
					client.start();
					this.hide();
					Mainwindow mw = new Mainwindow(client);
					client.setWindow(mw);
				}										
			}
			else {
				JOptionPane.showMessageDialog(this, "���� �����ϴ�", "", JOptionPane.PLAIN_MESSAGE);
				out.writeInt(-1);
				out.flush();
			}
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * �ش� ��ư�� ���� ��ȣ ����
	 */
	private void event() {

		WindowListener proc = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				socket.close();
				System.exit(0);
			}
		};
		this.addWindowListener(proc);
		join.addActionListener(e->{
			joinRoom();
		});
		addRoom.addActionListener(e->{
			try {
				out.writeInt(Signal.ADDROOM);
				out.flush();
				//�� ���� ����
				
				String name = JOptionPane.showInputDialog(this, "�� ���� �Է�(��,��,���� 1~8)", "�� �����", JOptionPane.PLAIN_MESSAGE);
				boolean isRoomName = true;
				if(name != null) {
					isRoomName = Pattern.matches("^[\\d��-����-�Ӱ�-�R]{1,8}$", name);					
				}
				if(!isRoomName) {
					name = null;
					JOptionPane.showMessageDialog(this, "�ùٸ��� ���� ������ �Դϴ�", "", JOptionPane.PLAIN_MESSAGE);
				}
				out.writeObject(name);
				out.flush();
				
//				System.out.println(in.readObject());
				Room r = (Room)in.readObject();
				System.out.println(r.getName()+"���� ����");
				//�� ������ �޾ƿ� �� ������ ���ӽ����ش�
				Client_Ex client = new Client_Ex(r.getPort(), user.getNickname());
				client.setDaemon(true);
				client.start();
				this.hide();
				Mainwindow mw = new Mainwindow(client);
				client.setWindow(mw);
				System.out.println(r.getPort()+"��Ʈ �� ����� ����");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		quick.addActionListener(e->{
			try {
				out.writeInt(Signal.QUICKJOIN);
				out.flush();
				Room r = (Room)in.readObject();
				System.out.println(r.getName()+"�濡 ���� ����");
				Client_Ex client = new Client_Ex(r.getPort(), user.getNickname());
				client.setDaemon(true);
				client.start();
				this.hide();
				Mainwindow mw = new Mainwindow(client);
				client.setWindow(mw);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		MouseListener m = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() ==2) {
					joinRoom();
				}
			}
		};
		room.addMouseListener(m);
		
		KeyListener enter = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_ENTER : joinRoom();
				}
			}
		};
		room.addKeyListener(enter);
		
	}

	private void menu() {
		
	}
	
	/**
	 * ���ϰ��� ��θ� �����ϰ� ȭ���� ����
	 * @param socket ���� ����
	 * @param name �г��� ����
	 * @param id ���̵� ����
	 */
	private ObjectOutputStream w_out;
	private ObjectInputStream w_in;
	public MainWindow(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
		try {
			g_inet = InetAddress.getByName("192.168.0.9");
			w_inet = InetAddress.getByName("192.168.0.9");
			Socket w_socket = new Socket(w_inet, 54891);
			w_out = new ObjectOutputStream(w_socket.getOutputStream());
			w_in = new ObjectInputStream(w_socket.getInputStream());
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.socket = socket;
		try {
			this.out = out;
			this.in = in;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.user = (User) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.display();
		this.event();
		this.menu();
		this.setTitle("KG����");
		this.setSize(1000, 800);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		Thread t= new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * ���������� �� ����� �޾ƿ��� ���ؼ� ������ ó��
	 */
	public void run() {
		try {
			while(true) {
				ArrayList<String[]> list;
				try {
					list = (ArrayList<String[]>)w_in.readObject();
//					System.out.println("receive = "+list.hashCode()+" / "+list);
					if(list!=null && list.size() != 0) {
						room_list = list;
						model1.setNumRows(0);
						for(int i = 0 ; i < list.size(); i++) {
							model1.addRow(list.get(i));
						}
						tg = model2;
						model2 = model1;
						model1 = tg;
						room.repaint();
//						room.setModel(model2);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

