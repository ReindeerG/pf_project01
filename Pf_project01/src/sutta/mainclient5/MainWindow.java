package sutta.mainclient5;

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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import javafx.scene.control.skin.TableColumnHeader;
import sutta.useall.Signal;

/**
 * 메인 화면 창(임시)
 */
public class MainWindow extends JFrame implements Runnable{
	/**
	 * 화면 창 구현시 통로 생성, 연결
	 */
	ObjectOutputStream out;
	ObjectInputStream in;
	Socket socket;
//	컴포넌트 배치용 공간
	private ArrayList<String[]> room_list;
	private Container con = this.getContentPane();
	private String[] names = {"방 번호","방 이름","참가자수"};
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
	private JButton join = new JButton("방참가");
	private JButton addRoom = new JButton("방 만들기");
	private JButton quick = new JButton("빠른 플레이");
	private JLabel label = new JLabel("방 목록 ");
	private JScrollPane pane = new JScrollPane(room);
	private JPanel panel = new JPanel();
	

	
	

	
	private void display() {
		con.setLayout(null);
//		con.add(pane);
		con.add(join);
		con.add(addRoom);
		con.add(quick);
		con.add(label);
		con.add(panel);
		room.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.setLayout(new BorderLayout());
		panel.add(room.getTableHeader(), BorderLayout.NORTH);
		panel.add(room, BorderLayout.CENTER);
		
		label.setBounds(12, 0, 800, 25);
		panel.setBounds(12, 27, 800, 678);
		join.setBounds(825, 38, 141, 36);
		addRoom.setBounds(825, 84, 141, 36);
		quick.setBounds(825, 130, 141, 36);
		

		
	}

	/**
	 * 방 참가 메소드
	 */
	private void joinRoom() {
		try {
			out.writeInt(Signal.JOIN);
			out.flush();
			
			//리스트에서 선택 된 방의 인덱스를 얻어와 그 방의 인덱스를 전송해준다 
			int index = room.getSelectedRow();
			
			if(room_list != null) {
				if(index == -1) {
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "참여할 방을 선택해 주세요");
				}
				else if(room_list.get(index)[2].equals("4/4")) {
					System.out.println("꽉찬 방");
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "이미 꽉 찬 방입니다");					}
				else {
					out.writeInt(room.getSelectedRow());
					out.flush();					
				}										
			}
			else {
				JOptionPane.showMessageDialog(this, "방이 없습니다", "", JOptionPane.PLAIN_MESSAGE);
				out.writeInt(-1);
				out.flush();
			}
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 해당 버튼에 따른 신호 전송
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
				//방 정보 전송
				
				String name = JOptionPane.showInputDialog(this, "방 제목 입력(한,영,숫자 1~8)", "방 만들기", JOptionPane.PLAIN_MESSAGE);
				boolean isRoomName = true;
				if(name != null) {
					isRoomName = Pattern.matches("^[\\dㄱ-ㅎㅏ-ㅣ가-힣]{1,8}$", name);					
				}
				if(!isRoomName) {
					name = null;
					JOptionPane.showMessageDialog(this, "올바르지 않은 방제목 입니다", "", JOptionPane.PLAIN_MESSAGE);
				}
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
	 * 소켓과의 통로를 생성하고 화면을 설정
	 * @param socket 소켓 전달
	 * @param name 닉네임 전달
	 * @param id 아이디 전달
	 */
	public MainWindow(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
		this.socket = socket;
		try {
			this.out = out;
			this.in = in;
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.display();
		this.event();
		this.menu();
		this.setTitle("KG섯다");
		this.setSize(1000, 800);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		Thread t= new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * 지속적으로 방 목록을 받아오기 위해서 스레드 처리
	 */
	public void run() {
		try {
			while(true) {
				ArrayList<String[]> list;
				try {
					list = (ArrayList<String[]>)in.readObject();
//					System.out.println("receive = "+list.hashCode()+" / "+list);
					if(list!=null && list.size() != 0) {
						room_list = list;
						model1.setNumRows(0);
						for(int i = 0 ; i < list.size(); i++) {
							model1.addRow(list.get(i));
							System.out.println("RowCount = "+model1.getRowCount());
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

