package sutta.mainclient4;

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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import sutta.useall.Room;

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
//	������Ʈ ��ġ�� ����
	private ArrayList<String> room_list;
	private Container con = this.getContentPane();
	private DefaultListModel<String> model1 = new DefaultListModel<>();
	private DefaultListModel<String> model2 = new DefaultListModel<>();
	private DefaultListModel<String> tg;
	private JList<String> room = new JList<String>(model2);
	private JButton join = new JButton("������");
	private JButton addRoom = new JButton("�� �����");
	private JButton quick = new JButton("���� �÷���");
	private JLabel label = new JLabel("�� ���");
	private JButton refresh = new JButton("�� ��� ���� ��ħ");
	private JScrollPane pane = new JScrollPane(room);

	
	
	/**
	 * ���������� �� ����� �޾ƿ��� ���ؼ� ������ ó��
	 */
	public void run() {
		try {
			while(true) {
				ArrayList<String> list;
				try {
					list = (ArrayList<String>)in.readObject();
					System.out.println("receive = "+list.hashCode()+" / "+list);
					if(list!=null && list.size() != 0) {
						room_list = list;
						model1.clear();
						for(int i = 0 ; i < list.size(); i++) {
							model1.addElement(list.get(i));
						}
						tg = model2;
						model2 = model1;
						model1 = tg;
						room.repaint();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void display() {
		con.setLayout(null);
		con.add(pane);
		con.add(join);
		con.add(addRoom);
		con.add(quick);
		con.add(label);
		con.add(refresh);
		
		label.setBounds(12, 0, 130, 25);
		pane.setBounds(12, 27, 900, 678);
		join.setBounds(925, 38, 141, 36);
		addRoom.setBounds(925, 84, 141, 36);
		quick.setBounds(925, 130, 141, 36);
		refresh.setBounds(925, 180, 141, 36);
		
	}

	
	private void joinRoom() {
		try {
			out.writeInt(Signal.JOIN);
			out.flush();
			//����Ʈ���� ���� �� ���� �ε����� ���� �� ���� �ε����� �������ش� 
			int index = room.getSelectedIndex();
			
			if(room_list != null) {
				if(index == -1) {
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "������ ���� ������ �ּ���");
				}
				else if(room_list.get(index).contains("4/4")) {
					System.out.println("���� ��");
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "�̹� �� �� ���Դϴ�");					}
				else {
					out.writeInt(room.getSelectedIndex());
					out.flush();					
				}										
			}
			else {
				JOptionPane.showMessageDialog(this, "���� �����ϴ�", "", JOptionPane.PLAIN_MESSAGE);
				out.writeInt(-1);
				out.flush();
			}
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * �ش� ��ư�� ���� ��ȣ ����
	 */
	private void event() {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				room.repaint();
//			}
//		});
		WindowListener proc = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//socket.close();
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
				
				String name = JOptionPane.showInputDialog("�� ���� �Է�");
				out.writeObject(name);
				out.flush();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		quick.addActionListener(e->{
			try {
				out.writeInt(Signal.QUICKJOIN);
				out.flush();
			} catch (IOException e1) {
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
	public MainWindow(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
		this.socket = socket;
		try {
			this.out = out;
			this.in = in;
		} catch (Exception e) {
			System.out.println("in ��� Ŀ��Ƽ��");
//			e.printStackTrace();
		}
		this.display();
		this.event();
		this.menu();
		this.setTitle("KG����");
		this.setSize(1100, 800);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		Thread t= new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	
	
}

