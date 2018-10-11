package sutta.mainclient6;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sutta.useall.Signal;
import sutta.useall.User;

class SignUp extends JDialog implements Signal{
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
//	������Ʈ ��ġ�� ����
	private Container con = this.getContentPane();
	private User user;
	private JLabel label = new JLabel("ȸ������");
	private JLabel ID = new JLabel("���̵�");
	private JLabel PW = new JLabel("��й�ȣ");
	private JLabel NickName = new JLabel("�г���");
	private JTextField id = new JTextField();
	private JTextField nickname = new JTextField();
	private JPasswordField pw = new JPasswordField();
	private JButton ok = new JButton("Ȯ��");
	private JButton cancel = new JButton("���");
	private JLabel idlabel = new JLabel();
	private JLabel nicklabel = new JLabel();
	private JLabel pwlabel = new JLabel();

	private void display() {
		con.setLayout(null);
		con.add(label);
		con.add(ID);
		con.add(PW);
		con.add(NickName);
		con.add(id);
		con.add(nickname);
		con.add(pw);
		con.add(ok);
		con.add(cancel);
		con.add(idlabel);
		con.add(nicklabel);
		con.add(pwlabel);
		
		label.setBounds(151, 10, 176, 20);
		ID.setBounds(22, 40, 325, 20);
		PW.setBounds(22, 174, 395, 20);
		NickName.setBounds(22, 106, 315, 20);
		id.setBounds(22, 58, 315, 28);
		pw.setBounds(22, 194, 315, 28);
		nickname.setBounds(22, 123, 315, 28);
		ok.setBounds(52, 243, 97, 52);
		cancel.setBounds(203, 243, 97, 52);
		idlabel.setBounds(22, 86, 315, 15);
		nicklabel.setBounds(22, 151, 315, 15);
		pwlabel.setBounds(22, 220, 315, 15);
		
//		JLabel lblNewLabel = new JLabel("(���� ��ҹ���, ���� ���� 6~12��)");
//		lblNewLabel.setBounds(22, 83, 315, 15);
//		getContentPane().add(lblNewLabel);
//		
//		JLabel label_1 = new JLabel("(�ѱ�, ���� ��ҹ���, ���� 2~8��)");
//		label_1.setBounds(22, 149, 315, 15);
//		getContentPane().add(label_1);
//		
//		JLabel label_2 = new JLabel("(���� ��ҹ��� 1����, ����, Ư������!@#$ ���� 6~12��)");
//		label_2.setBounds(22, 218, 315, 15);
//		getContentPane().add(label_2);
	}

	private void event() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		FocusListener fcl = new FocusAdapter() {
			String regexId = "^(?=.*?[a-zA-Z])[a-zA-Z0-9]{6,12}$";
			String regexPassword = "^((?=.*?[a-zA-Z])(?=.*?[0-9])(?=.*?[!@#$])[a-zA-Z0-9!@#$]{6,12})||((?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{6,12})||((?=.*?[a-zA-Z])(?=.*?[!@#$])[a-zA-Z!@#$]{6,12})$";
			String regexNickname = "^[a-zA-Z��-�R0-9]{2,8}$";
			@Override
			public void focusLost(FocusEvent e) {
				//���̵� �ؽ�Ʈ â�� �Է��� ���� ���ǿ� ���� ���� ���·� ��Ŀ���� �Ѿ�� �� �� ����
				boolean isId = Pattern.matches(regexId, id.getText());
				boolean isNickname = Pattern.matches(regexNickname, nickname.getText());
				boolean isPw = Pattern.matches(regexPassword, String.valueOf(pw.getPassword()));
				if(!id.getText().equals("")) {
					if(!isId) {
						idlabel.setText("���� ��ҹ���, ���� ���� 6~12��");
					}
					else {
						idlabel.setText("�ùٸ� ������ ���̵� �Դϴ�");
					}
				}
				if(!nickname.getText().equals("")) {
					if(!isNickname) {
						nicklabel.setText("�ѱ�, ���� ��ҹ���, ���� 2~8��");
					}	
					else {
						nicklabel.setText("���� �г����̳׿�!");
					}
				}
				if(!String.valueOf(pw.getPassword()).equals("")) {
					if(!isPw) {
						pwlabel.setText("���� ��ҹ��� 1����, ����, Ư������!@#$ ���� 6~12��");
					}		
					else {
						pwlabel.setText("�ùٸ� ������ ��й�ȣ�Դϴ�");
					}
				}
			}
		};
		id.addFocusListener(fcl);
		nickname.addFocusListener(fcl);
		pw.addFocusListener(fcl);
		
		
		ok.addActionListener(e->{
			try {
				out.writeInt(NEWMEMBERPROC);
				out.flush();
				
				User u = new User(id.getText(), nickname.getText());
				u.setPw(String.valueOf(pw.getPassword()));
				out.writeObject(u);
				out.flush();
				
				Boolean isSignUp = in.readBoolean();
				if(isSignUp) {
					JOptionPane.showMessageDialog(this, "ȸ�� ������ �Ϸ�Ǿ����ϴ�", "", JOptionPane.PLAIN_MESSAGE);
					this.dispose();
				}
				else {
					JOptionPane.showMessageDialog(this, "�̹� �����ϴ� ���̵� �Դϴ�", "", JOptionPane.PLAIN_MESSAGE);
					id.setText("");
					nickname.setText("");
					pw.setText("");
				}
			}catch(Exception err) {
				err.getMessage();
			}
		});
		cancel.addActionListener(e->{
			dispose();
		});
	}

	private void menu() {
		
	}
	
	private void setDialogLocation() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int w = d.width/2 - getWidth()/2;
		int h = d.height/2 - getHeight()/2;
		this.setLocation(w,h);
	}
	
	public SignUp(ObjectOutputStream out, ObjectInputStream in) {
		this.out = out;
		this.in = in;
		this.display();
		this.event();
		this.menu();
		this.setTitle("ȸ�� ����");
		this.setSize(390, 344);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setDialogLocation();
		this.setModal(true);
	}
}


