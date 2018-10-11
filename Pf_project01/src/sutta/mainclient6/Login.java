package sutta.mainclient6;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sutta.useall.Signal;
import sutta.useall.User;



public class Login extends JDialog  implements Signal{
//	������Ʈ ��ġ�� ����
	private Container con = this.getContentPane();
	private JTextField id = new JTextField();
	private JPasswordField pw = new JPasswordField();
	private JLabel ID = new JLabel("���̵� :");
	private JLabel PW = new JLabel("��й�ȣ :");
	private JLabel KG = new JLabel("KG���� �α���");
	private JButton ok = new JButton("Ȯ��");
	private JButton cancel = new JButton("���");
	private JButton member = new JButton("ȸ������");
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int login;
	private User user;
	
	public int isLogin() throws IOException {
		return login;
	}
	public String getId() {
		return id.getText();
	}
	public char[] getPw() {
		return pw.getPassword();
	}
	
	private void display() {
		con.setLayout(null);
		con.add(id);
		con.add(pw);
		con.add(ok);
		con.add(cancel);
		con.add(member);
		con.add(ID);
		con.add(PW);
		con.add(KG);
		
		KG.setBounds(57, 10, 93, 23);
		id.setBounds(80, 54, 175, 23);
		pw.setBounds(80, 87, 175, 23);
		ID.setBounds(12,54,93,23);
		PW.setBounds(12,87,93,23);
		ok.setBounds(57, 131, 67, 34);
		member.setBounds(156, 10, 99, 23);
		cancel.setBounds(156, 131, 67, 34);
		
	}

	private void event() {
		WindowListener exit = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		this.addWindowListener(exit);
		ok.addActionListener(e->{
			try {
				out.writeInt(LOGINPROC);
				out.flush();
				
				User u = new User(id.getText());
				u.setPw(String.valueOf(pw.getPassword()));

				out.writeObject(u);
				out.flush();
				
//				System.out.println(in.readBoolean());
				login = in.readInt();
				
				//�α��� �Ǿ��� ��
				if(login == SUCCESSLOGIN) {
					this.dispose();
				}
				//ȸ�������� ��ġ���� ���� ��
				else if(login == NOTMEMBER) {
					JOptionPane.showMessageDialog(this, "�ùٸ��� ���� ���̵� Ȥ�� ��й�ȣ �Դϴ�", "", JOptionPane.PLAIN_MESSAGE);
					id.setText("");
					pw.setText("");
				}
				else if(login == PLAYINGMEMBER) {
					JOptionPane.showMessageDialog(this, "�̹� �������Դϴ�", "", JOptionPane.PLAIN_MESSAGE);
				}
				
			}catch (Exception err) {
				err.printStackTrace();
			}
		});
		cancel.addActionListener(e->{
			System.exit(0);
		});
		member.addActionListener(e->{
			SignUp signup = new SignUp(out, in);
			signup.setVisible(true);
		});
	}
	
	private void setDialogLocation() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int w = d.width/2 - getWidth()/2;
		int h = d.height/2 - getHeight()/2;
		this.setLocation(w,h);
	}
	
	public Login(ObjectOutputStream out, ObjectInputStream in) {
		this.out = out;
		this.in = in;
		this.display();
		this.event();
		this.setTitle("�α���");
		this.setSize(300, 230);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setDialogLocation();

	}
}