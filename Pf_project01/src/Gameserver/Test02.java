package Gameserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import Gamewindow.Mainwindow;

public class Test02 {
	public static void main(String[] args) {
		try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception e) {e.printStackTrace();}
		
		String str = JOptionPane.showInputDialog("���̵�");
		
//		Mainwindow frame = new Mainwindow(null);
//		frame.setVisible(true);
//		Client a = new Client(str);
//		a.setDaemon(true);
//		a.start();
//		frame.setClient(a);
		
		
		
		
		
		Client a = new Client(str);
		a.setDaemon(true);
		a.start();
		
		
		
		Mainwindow frame = new Mainwindow(a);
		a.setWindow(frame);
//		frame.show();
//		while(true) {
//			if(frame.isVisible()==false) {
				frame.setVisible(true);
				frame.show();
//			}
//			frame.hide();
//			System.out.println("dd");
//			break;��
//			if(frame.isVisible()==true) break;
//		}
		

		
		
//		a.callWhosturn();
		
		
//		frame.StartMyJoin();
//		a.callRefresh();
		
//		try {
//			a.getSocket().close();
//			a.interrupt();
//			System.out.println("�Ĵݾ�");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
//		
//
//		try {
//			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(a.getSocket().getOutputStream()));
//			while(true) {
//				String msg = JOptionPane.showInputDialog("�غ����1, �غ�2, ����3, ����4, ����5, 6ä��, 7�к���, 8ī��ޱ�");
//				if(msg.equals("1")) {
//					out.writeObject(new Gaming(a.getUserid(), Gaming.GAME_UNREADY));
//					out.flush();
//				}
//				if(msg.equals("2")) {
//					out.writeObject(new Gaming(a.getUserid(), Gaming.GAME_READY));
//					out.flush();
//				}
//				if(msg.equals("3")) {
//					out.writeObject(new Gaming(a.getUserid(), Gaming.GAME_DIE));
//					out.flush();
//				}
//				if(msg.equals("4")) {
//					out.writeObject(new Gaming(a.getUserid(), Gaming.CHAT_JOIN));
//					out.flush();
//				}
//				if(msg.equals("5")) {
//					out.writeObject(new Gaming(a.getUserid(), Gaming.CHAT_LEAVE));
//					out.flush();
//				}
//				if(msg.equals("6")) {
//					out.writeObject(new Gaming(a.getUserid(), Gaming.CHAT, "ä�ø޽���", ""));
//					out.flush();
//				}
//				if(msg.equals("7")) {
//					out.writeObject(new Gaming(a.getUserid(), Gaming.CHAT_NICKCHANGE, "�ٲܴ�"));
//					out.flush();
//				}
//				if(msg.equals("8")) {
//					out.writeObject(new Gaming(a.getUserid(), Gaming.GAME_START));
//					out.flush();
//				}
//			}
//		}
//		catch(Exception e) {}
		
		
		
		
		
	}
}
