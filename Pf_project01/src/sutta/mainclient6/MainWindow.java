package sutta.mainclient6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
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

import sutta.gameserver2.Client_Ex;
import sutta.gamewindow2.Mainwindow;
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
	private String[] names = {"�� ��ȣ","�� �̸�","���� ����","������ ��"};
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
	private Image moon = Toolkit.getDefaultToolkit().getImage("Moon\\moon.jpg");
	private JPanel imagePanel = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			g.drawImage(moon, 0, 0, 467, 385, null);
		}
	};
	
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
//		con.setBackground(Color.black);
//		con.add(imagePanel);
//		con.setForeground(Color.WHITE);
		
		
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
		nickname.setBounds(825, 300, 200, 25);
		money.setBounds(825, 330, 200, 25);
		nickname.setText("�г��� : "+user.getNickname());
		money.setText("���� �� : "+user.getMoney()+"��");
		imagePanel.setBounds(517, 366, 467, 385);
		
		

		
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
				else if(room_list.get(index)[2].equals("������")) {
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "�̹� �������� ���Դϴ�");
					}
				else {
					out.writeInt(room.getSelectedRow());
					out.flush();
					Room r = (Room)in.readObject();
					System.out.println(r.getName()+"�濡 ����");
					if(r!=null) {
						//�� ������ �޾ƿ� �� ������ ���ӽ����ش�
						Client_Ex client = new Client_Ex(r.getPort(), user);
						client.setDaemon(true);
						client.start();
						Mainwindow mw = new Mainwindow(client, out, this);
						client.setWindow(mw);
						mw.setVisible(true);
						this.setVisible(false);						
					}
					else {
						JOptionPane.showMessageDialog(this, "������ �������� ���Դϴ�", "", JOptionPane.PLAIN_MESSAGE);
					}
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
				try {
					out.writeInt(6);
					out.flush();
					socket.close();
					w_socket.close();
				} catch (Exception e1) {
					System.out.println("Ŭ���̾�Ʈ�� ���� ����");
					e1.printStackTrace();
				}
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
					isRoomName = Pattern.matches("^[\\da-zA-Z��-����-�Ӱ�-�R]{1,8}$", name);					
				}
				if(!isRoomName) {
					name = null;
					JOptionPane.showMessageDialog(this, "�ùٸ��� ���� ������ �Դϴ�", "", JOptionPane.PLAIN_MESSAGE);
				}
				out.writeObject(name);
				out.flush();
				
				System.out.println("name = "+name);
				if(name!= null) {
					Room r = (Room)in.readObject();
					System.out.println(r.getName()+"���� ����");
					//�� ������ �޾ƿ� �� ������ ���ӽ����ش�
					Client_Ex client = new Client_Ex(r.getPort(), user);
					client.setDaemon(true);
					client.start();
					System.out.println("Client_Ex ����");
					Mainwindow mw = new Mainwindow(client, out, this);
					System.out.println("Mainwindow mw ���� �� ����");
					client.setWindow(mw);
					mw.setVisible(true);
					this.setVisible(false);
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		quick.addActionListener(e->{
			try {
				out.writeInt(Signal.QUICKJOIN);
				out.flush();
				System.out.println(in.readObject());
				Room r = (Room)in.readObject();
				System.out.println(r.getName()+"�濡 ���� ����");
				Client_Ex client = new Client_Ex(r.getPort(), user);
				client.setDaemon(true);
				client.start();
				Mainwindow mw = new Mainwindow(client, out, this);
				client.setWindow(mw);
				mw.setVisible(true);
				this.setVisible(false);
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

	/**
	 * ���ϰ��� ��θ� �����ϰ� ȭ���� ����
	 * @param socket ���� ����
	 * @param name �г��� ����
	 * @param id ���̵� ����
	 */
	private ObjectOutputStream w_out;
	private ObjectInputStream w_in;
	private Socket w_socket;
	public MainWindow(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
		try {
			g_inet = InetAddress.getByName("localhost");
			w_inet = InetAddress.getByName("localhost");
			w_socket = new Socket(w_inet, 54891);
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
			System.out.println("user = "+user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.display();
		this.event();
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
				list = (ArrayList<String[]>)w_in.readObject();
//				System.out.println("receive = "+list.hashCode()+" / "+list);
				if(list!=null ) {
					room_list = list;
					model1.setNumRows(0);
					for(int i = 0 ; i < list.size(); i++) {
						model1.addRow(list.get(i));
					}
					tg = model2;
					model2 = model1;
					model1 = tg;
					room.repaint();
//					room.setModel(model2);
				}
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}
}

