package sutta.gameserver2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sutta.gamelogic.Logic;
import sutta.mainserver6.MainServer;
import sutta.useall.User;

/**
 * � �÷��̾��� �Ͽ� ���ư��� �� Ÿ�̸Ӿ�����
 */
class Timer extends Thread {
	/**
	 * serv: ���� ���� ������
	 * p: ��� �÷��̾��� Ÿ�̸�����
	 * kill: �ܺο��� Ÿ�̸Ӹ� Ȯ���� ���̱� ����. kill=true�ϸ� Ŭ���̾�Ʈ�鿡�� Ÿ�̸� ��ȣ�� �Ⱥ����� ������ �����ϱ����� �����.
	 */
	Server serv=null;
	Player p=null;
	int turn=0;
	private boolean kill=false;
	public boolean isKill() { return kill; }
	public void setKill(boolean kill) { this.kill = kill; }
	public Timer() {};		// ���� Ÿ�̸�, ������ Ÿ�̸Ӱ� ��ӹ����Ŷ�
	public Timer(Server serv, Player p, int turn) { this.serv=serv; this.p=p; this.turn=turn; }
	public void run() {
		for(int i=0;i<100;i++) {
			if(p.getBetbool()!=0 || isKill()==true) {
				serv.IncreaseTurn();				// Ÿ�̸� ����Ǹ� �� �����ϸ鼭 ������ ��.
				this.interrupt();
				return;
			}
			else {
				try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
				for(Player p2 : serv.getPlayers()) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
						out.writeObject(Gaming.Timer(serv.getWhosturn(), i, turn));
						out.flush();
					} catch(Exception e) {e.printStackTrace();}
				}
			}
		}
		Player p = serv.getPlayers().get(serv.getWhosturn());
		serv.IncreaseTurn();
		p.getUth().toDie();
		this.setKill(true);
		this.interrupt();
		return;
	}
}
/**
 * ó�� �� ���½� ���ư��� �� Ÿ�̸Ӿ�����
 */
class OpenTimer extends Timer {
	/**
	 * serv: ���� ���� ������
	 * kill: �ܺο��� Ÿ�̸Ӹ� Ȯ���� ���̱� ����. kill=true�ϸ� Ŭ���̾�Ʈ�鿡�� Ÿ�̸� ��ȣ�� �Ⱥ����� ������ �����ϱ����� �����.
	 */
	Server serv=null;
	int turn=1;
	private boolean kill=false;
	public boolean isKill() { return kill; }
	public void setKill(boolean kill) { this.kill = kill; }
	public OpenTimer(Server serv) { this.serv=serv; }
	public void run() {
		for(int i=0;i<100;i++) {
			if(isKill()==true) {
				this.interrupt();
				return;
			}
			else {
				boolean allsel = true;
				for(Player p : serv.getPlayers()) {
					if(p.getTrash()==0 && p.getBetbool()!=1) {
						allsel = false;
						break;
					}
				}
				if (allsel==true) {
					serv.setWhosturn(serv.getWhosturn()-1);
					serv.setTurn(serv.getTurn()-1);
					serv.nextTurn();
					this.setKill(true);
					this.interrupt();
					return;
				}
				try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
				for(Player p2 : serv.getPlayers()) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
						out.writeObject(Gaming.Timer(4, i, turn));
						out.flush();
					} catch(Exception e) {e.printStackTrace();}
				}
			}
		}
		for(Player p : serv.getPlayers()) {
			if(p.getTrash()==0 && p.getBetbool()!=1) {
				p.setBetbool(1);
				p.setTrash(1);
			}
		}
		serv.setWhosturn(serv.getWhosturn()-1);
		serv.setTurn(serv.getTurn()-1);
		serv.nextTurn();
		this.setKill(true);
		this.interrupt();
		return;
	}
}
/**
 * �����ڳ��� �а����� ���ư��� �� Ÿ�̸Ӿ�����
 */
class SelSetTimer extends Timer {
	/**
	 * serv: ���� ���� ������
	 * kill: �ܺο��� Ÿ�̸Ӹ� Ȯ���� ���̱� ����. kill=true�ϸ� Ŭ���̾�Ʈ�鿡�� Ÿ�̸� ��ȣ�� �Ⱥ����� ������ �����ϱ����� �����.
	 */
	Server serv=null;
	ArrayList<Player> players=null;
	int turn=1;
	private boolean kill=false;
	public boolean isKill() { return kill; }
	public void setKill(boolean kill) { this.kill = kill; }
	public SelSetTimer(Server serv, ArrayList<Player> players) { this.serv=serv; this.players=players; }
	public void run() {
		for(int i=0;i<100;i++) {
			if(isKill()==true) {
				this.interrupt();
				return;
			}
			else {
				boolean allsel = true;
				for(Player p : players) {
					if(p.getSelCardset()==0 && p.getBetbool()!=1) {
						allsel = false;
						break;
					}
				}
				if (allsel==true) {
					serv.whosWin(players);
					this.setKill(true);
					this.interrupt();
					return;
				}
				try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
				for(Player p2 : serv.getPlayers()) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
						out.writeObject(Gaming.Timer(5, i, turn));
						out.flush();
					} catch(Exception e) {e.printStackTrace();}
				}
			}
		}
		for(Player p : players) {
			if(p.getSelCardset()==0 && p.getBetbool()!=1) {
				p.SelectSet(1);
			}
		}
		serv.whosWin(players);
		this.setKill(true);
		this.interrupt();
		return;
	}
}

/**
 * ������ ������ ������ �� �ش� ���������忡�� ����������� ��û�� ����� ������.
 * ���������忡 �ش� ������ �����ٴ� ��ȣ�� ���Ծ, ���������带 �����Ű�� ������ ���� �� ������(�ش������� return�� �� ������ �����尡 �����°� �Ǽ�)
 * ���� ȣ���� ������� ȣ���ϰ� ���������尡 ���� ����� �� �ְ� �ϱ� ����.
 */
class SelfOutTh extends Thread {
	private Player p;
	private String nick;
	private Server serv;
	public SelfOutTh(Player p, String nick, Server serv) {
		this.p=p; this.nick=nick; this.serv=serv;
	}
	public void run() {
		serv.SelfOut(p, nick);
		return;
	}
}
/**
 * ������ ƨ���� �� �ش� ���������忡�� ����������� ��û�� ����� ������.
 * ���������忡 �ش� ������ ƨ��ٴ� ���ܰ� ���Ծ, ���������带 �����Ű�� ������ ���� �� ������(�ش������� return�� �� ������ �����尡 �����°� �Ǽ�)
 * ���� ȣ���� ������� ȣ���ϰ� ���������尡 ���� ����� �� �ְ� �ϱ� ����.
 */
class HesClosedTh extends Thread {
	private Server serv;
	public HesClosedTh(Server serv) {
		this.serv=serv;
	}
	public void run() {
		serv.HesClosed();
		return;
	}
}
/**
 * ���� ����������� ��û�� ����� ������.
 * �����ȣ�� �޾Ƶ� �ش� ����������� ������ in ��� ���̰�, ���������带 �����Ű�� ������ ���� �� ������(�ش������� return�� �� ������ �����尡 �����°� �Ǽ�)
 * ���� ȣ���� ������� ȣ���ϰ� ���������尡 ���� ����� �� �ְ� �ϱ� ����.
 */
class BanTh extends Thread {
	private Player p;
	private Server serv;
	public BanTh(Player p, Server serv) {
		this.p=p; this.serv=serv;
	}
	public void run() {
		serv.Ban(p);
		return;
	}
}
class UserThread extends Thread {
	/**
	 * ������ ����� ���� 1��� UserThread�� ������.
	 * socket: ������ ����� ������ Socket�� ���(������ ����)
	 * serv: ������ ��ɽ�ȣ�� �޾� ������ ����ó���� ��û�ϱ� ���� ���������� ����(������ ����)
	 * 
	 * stop: �� �����带 �����ϱ� ���� ��ȣ. toStop()�� ����  true�� �ٲٸ� run�� while���� �� �̻� ����� �����ʰ� ������ �����ϱ� ���ؼ��� �����.
	 * f: ä��, �Խ� �� �ð������� �˸����� ǥ�õ� �� �ð������� ����
	 * in: ����� ���� 1��� ��� ����� InputStream. �� �����尡 start(run)�� �� ó�� ����(new)�Ͽ� ��� ���.
	 * 
	 * �ش� �������Լ� ������ ��ȣ�� ������, ���������忡 ��û�Ͽ� ���������忡���� players ����Ʈ�� �ش������� �����ϰ� order�� 0���� ��ƴ���� ������ ��, Ŭ���̾�Ʈ�鿡�� Refresh ���� ��.
	 * 
	 * ������ �ڽŰ� ����� �����忡 �������� ���������� ��û�ϸ�, ����������� ������ �ְ� ���������忡���� ���� ���������� �ൿ�� ��.
	 * �� �� ���� ���� �ڽ��� �����嵵 ����Ǳ⵵ �ϴµ�, ������ Socket�� InputStream�� ����ֱ� ������ �ٷ� InputStream�� �״�� �̾���� �����带 ä���ִµ�, �̰��� 2��° ��������.(InputStream�� ���Ե� ������)
	 */
	private Socket socket;
	private boolean stop;
	private Server serv;
	private Format f = new SimpleDateFormat("a hh:mm");
	private ObjectInputStream in;
	
	public UserThread(Socket socket, Server serv) { stop=false; this.socket=socket; this.serv=serv; this.in=null; }

	public Socket getSocket() { return socket; }
	public boolean isStop() { return stop; }
	public void toStop() { stop=true; return; }
	public void reserStop() { stop=false; return; }
	public ObjectInputStream getIn() { return in; }
	/**
	 * MatchId()
	 * ���� ������ ������ ������� �����尡 ������� ��, userid������ Ŭ���̾�Ʈ���� �޾ƿ��Ե�.. ���μ����� �����ϰԵǸ� ����� �༮
	 */
	public void MatchId() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.writeObject(new Gaming(Gaming.IDMATCH));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	/**
	 * ������ Ÿ�̸� �ð��� ��� �������� ��, Ÿ�̸Ӵ� �ش� ������ �����忡 ���̺��� ����� �����µ�, �� �޼��尡 �̿��.
	 */
	public void toDie() {
		Player p = serv.getPlayers().get(serv.getWhosturn());
		p.setBetbool(1);
		serv.nextTurn();
		return;
	}
	public void run() {
		// in�� ���ٴ°� ������ ó�� ����ȴٴ� ��. userid�� �䱸�ϰ� InputStream�� new��.
		// in�� �ִٴ°� ������ ������ ������������ ���������尡 ����Ǿ� �ٽ� ������Ų ���̶� new�� �ʿ� ����. ������ new�ϸ� ���ܹ߻�.
		if(in==null) MatchId();
		try {
			if(in==null) in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while(true) {
				if(stop==true) { in.close(); this.interrupt(); break; }
				else {
					serv.checkUth();
					try {
							Gaming gm = (Gaming)in.readObject();
							Player q=null;
							for(Player p : serv.getPlayers()) {
								if(p.getSocket().equals(socket)) {
									ArrayList<Player> players = serv.getPlayers();
									q = p;
									switch(gm.getWhat()) {
										case Gaming.IDMATCH: {
											System.out.println("���Ӽ������� ���������͹���");
											q.setUser(gm.getUser());
											System.out.println("������ ���� ����:"+gm.getUser());
											serv.Refresh();
											System.out.println("�������ó���");
											break;
										}
										case Gaming.REFRESH: {
											serv.Refresh();
											break;
										}
										case Gaming.MONEY_REFRESH: {
											serv.MoneyRefresh();
											break;
										}
										case Gaming.GAME_WHOSTURN: {
											serv.WhosTurn();
											break;
										}
										case Gaming.GAME_UNREADY: {
											p.setReady(0);
											p.getUser().setMoney(p.getUser().getMoney()+serv.getPandon());
											serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()+serv.getPandon());
											serv.setMoneythisgame(serv.getMoneythisgame()-serv.getPandon());
											serv.MoneyRefresh();
											serv.Refresh();
											break;
										}
										case Gaming.GAME_READY: {
											p.setReady(1);
											p.getUser().setMoney(p.getUser().getMoney()-serv.getPandon());
											serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()-serv.getPandon());
											serv.setMoneythisgame(serv.getMoneythisgame()+serv.getPandon());
											serv.MoneyRefresh();
											serv.Refresh();
											break;
										}
										case Gaming.GAME_START: {
											serv.GameStartBool();
											break;
										}
										case Gaming.GAME_DIE: {
											toDie();
											break;
										}
										case Gaming.GAME_OPEN: {
											p.setTrash(gm.getCard3());
											break;
										}
										case Gaming.GAME_SELECTSET: {
											p.SelectSet(gm.getCardset());
											break;
										}
										case Gaming.GAME_CALL: {
											p.setBetbool(2);
											p.getUser().setMoney(p.getUser().getMoney()-serv.getMinforbet());
											serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()-serv.getMinforbet());
											p.setThisbet(serv.getMinforbet());
											serv.setMoneythisgame(serv.getMoneythisgame()+serv.getMinforbet());
											serv.MoneyRefresh();
											serv.nextTurn();
											break;
										}
										case Gaming.GAME_HALF: {
											p.setBetbool(3);
											p.getUser().setMoney(p.getUser().getMoney()-serv.getMoneythisgame()/2);
											serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()-serv.getMoneythisgame()/2);
											p.setThisbet(serv.getMoneythisgame()/2);
											serv.setMinforbet(serv.getMoneythisgame()/2);
											serv.setMoneythisgame(serv.getMoneythisgame()+serv.getMoneythisgame()/2);
											serv.MoneyRefresh();
											serv.nextTurn();
											break;
										}
										case Gaming.GAME_CHECK: {
											p.setBetbool(4);
											serv.setMinforbet(0);
											serv.MoneyRefresh();
											serv.nextTurn();
											break;
										}
										case Gaming.MUCHPANDON: {
											serv.Pandon(serv.getPandon());
											break;
										}
										case Gaming.PANDON: {
											serv.Pandon(gm.getPandon());
											break;
										}
										case Gaming.CHAT: {
											String msg = gm.getMsg();
											Date d = new Date();
											for(Player p2 : players) {
												ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
												out.writeObject(Gaming.Chat(q.getUser().getNickname(), msg, f.format(d)));
												out.flush();
											}
											break;
										} 
										case Gaming.CHAT_JOIN: {
											Date d = new Date();
											for(Player p2 : players) {
												ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
												out.writeObject(Gaming.HesIn(q.getUser().getNickname(), f.format(d), players));
												out.flush();
											}
											serv.Pandon(serv.getPandon());
											break;
										}
										case Gaming.CHAT_LEAVE: {
											String strtemp = q.getUser().getNickname();
											toStop();
											while(!isStop()) {
											}
											SelfOutTh tmpt = new SelfOutTh(q, strtemp, serv);
											tmpt.start();
											break;
										}
										case Gaming.BAN: {
											String target=gm.getUserid();
											for(Player pl : players) {
												if(pl.getUser().getId().equals(target)) {
													if(pl.getReady()==1) {
														pl.getUser().setMoney(q.getUser().getMoney()+serv.getPandon());
														serv.getMain().setMoney(q.getUser().getId(), q.getUser().getMoney()+serv.getPandon());
														serv.setMoneythisgame(serv.getMoneythisgame()-serv.getPandon());
														pl.setReady(0);
														serv.MoneyRefresh();
														serv.Refresh();
													}
													try {
														ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(pl.getSocket().getOutputStream()));
														out.writeObject(Gaming.UrBanned());
														out.flush();
													} catch(Exception e) {e.printStackTrace();}
													pl.getUth().toStop();
													while(!pl.getUth().isStop()) {
													}
													pl.getUth().getIn().close();
													pl.getUth().interrupt();
													BanTh tmpt = new BanTh(pl, serv);
													tmpt.setDaemon(true);
													tmpt.start();
													break;
												}
											}
											break;
										}
//										case Gaming.CHAT_NICKCHANGE: {
//											String temp = q.getUser().getNickname();
//											p.getUser().setNickname(gm.getMsg());
//											Date d = new Date();
//											for(Player p2 : players) {
//												ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
//												out.writeObject(Gaming.NickChange(temp, gm.getMsg(), f.format(d), players));
//												out.flush();
//											}
//											break;
//										}
										case Gaming.GAME_RESULT_OK: {
											serv.setResultbacksig(serv.getResultbacksig()-1);
											break;
										}
										default: break;
									}
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
							if(isStop()==false) {
								System.out.println("ƨ��");
								toStop();
								in.close();
								HesClosedTh tmpt = new HesClosedTh(serv);
								tmpt.start();
								interrupt();
							} else {
								
							}
							return;
						}
				}
			}
		}
		catch(Exception e) { e.printStackTrace(); }
		return;
	}
}

public class Server extends Thread {
	/**
	 * port: �� ���� ��Ʈ ��ȣ�� ��� ����. ���μ������� ���� ������ �� �޾��� ����. (������ ����)
	 * server: �� ���� �����μ� ������ Socket��.
	 * players: �� �濡 ������ �������� ��� ����.
	 * cards: 1������ 10������, �� ���� 2�徿�Ͽ� 1~20�� Integer�� ����ִ� ArrayList. �̰��� Shuffle�Ͽ� ������.
	 * inggame: ���� ������ ���� ������ �ƴ��� �� �� �ִ� boolean. ���� ���� ���ɿ��ο� ���� ��. Ŭ���̾�Ʈ������ �ٸ� inggame�� ��ü������ ������/���Ḧ �Ǻ��Ͽ� ��ư�� ��Ȱ��ȭ�� ����Ұ��� ������.
	 * whosturn: ���� ������ ������ ��� ����(0~3)
	 * turn:
	 * moneythisgame: ���� �� ���ñݾ�.
	 * minforbet: �� ����� ������, ���ϱ� ���� �ݾ�(���� �ּұݾ�)
	 * phase2: ���� �� ��⿡�� �ѹ��� ���� 3��° �� ī�带 �޾Ҵ����� ����. ù���������� false / 3��°�� ������ true.
	 * thisplaynum: ���� �����۽� ������ �÷��̾� ��.
	 * f: ä��â�� �˸� � ���� �ð��� ���˾��.
	 * nowtimer: ���� ���ư��� �ִ� Ÿ�̸Ӱ� ���� ��.
	 * pandon: ���ӿ� �����ϱ� ���� �������� �����ϴ� �ʱ� �⺻ ���ñ�.
	 */
	private MainServer main;
	public MainServer getMain() {
		return main;
	}
	public void setMain(MainServer main) {
		this.main = main;
	}
	private int port;
	private ServerSocket server;
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Integer> cards = new ArrayList<>();
	
	private boolean inggame=false;
	private int whosturn=0;
	private int turn;
	private boolean phase2;
	private int thisplaynum;
	
	private int pandon;
	private int moneythisgame=0;
	private int minforbet=0;

	private Format f = new SimpleDateFormat("a hh:mm");
	private Timer nowtimer;

	public ArrayList<Player> getPlayers() { return players; }
	public void setPlayers(ArrayList<Player> players) { this.players=players; }
	
	public boolean isInggame() { return inggame; }
	public void setInggame(boolean inggame) { this.inggame = inggame; }
	public int getWhosturn() { return whosturn; }
	public void setWhosturn(int whosturn) { this.whosturn = whosturn; }
	public int getTurn() { return turn; }
	public void setTurn(int turn) { this.turn = turn; }
	public void increaseTurn() { this.turn++; }
	public boolean isPhase2() { return phase2; }
	public void setPhase2(boolean phase2) { this.phase2 = phase2; }
	public int getThisplaynum() { return thisplaynum; }
	public void setThisplaynum(int thisplaynum) { this.thisplaynum=thisplaynum; }

	public int getPandon() { return pandon; }
	public void setPandon(int pandon) { this.pandon = pandon; }
	public int getMoneythisgame() { return moneythisgame; }
	public void setMoneythisgame(int moneythisgame) { this.moneythisgame = moneythisgame; }
	public int getMinforbet() { return minforbet; }
	public void setMinforbet(int minforbet) { this.minforbet = minforbet; }
	
	
	public Timer getNowtimer() { return nowtimer; }
	public void setNowtimer(Timer nowtimer) { this.nowtimer = nowtimer; }
	
	private List<User> userList;
	private User user;
	
	public void serverClose() {
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(User user) {
		this.user = user;
		userList.add(user);
	}
	public void removeUser(User user) {
		userList.remove(user);
	}
	public List<User> getUserList(){
		return userList;
	}
	
	public Server(int port, List<User> userList) {
		this.port = port;
		this.userList = userList;
		Integer[] temp = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		cards = new ArrayList<>(Arrays.asList(temp));
		setPandon(10);
	}
	public void run() {
		System.out.println("���ӹ���۵�");
		try {
			server = new ServerSocket(port);
			while(true) {				
				Socket receive = server.accept();
				System.out.println("������ ������");
				boolean exit = false;
				for(Player p : players) {
					if(p.getSocket().equals(receive)) {
						exit = true;
						break;
					};
				}
				if(exit==false) {	// accept�� ������ �̹� ������ ������ �ƴ� ��쿡�� ���������带 �����ϰ� �÷��̾ �߰�.
					UserThread uth = new UserThread(receive, this);
					uth.setDaemon(true);
					Player p = new Player(players.size(), receive, uth, user);
					players.add(p);
					uth.start();
				}
				while(players.size()>3) {	// �� �濡 ���ӵ� ������ ���� 4�� �̻��̸� ���̻� �� ������ ���� ����.
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ������ ��� �÷��̾ READY�� �ƴ϶�� ���� Ŭ���̾�Ʈ ��ü������ ���۹�ư�� ����������, �׷��� �����Ͽ� ��� READY�� ����, �ƴϸ� ������. 
	 */
	public void GameStartBool() {
		boolean allready=true;
		for(Player p : players) {
			if(p.getReady()==0) {
				allready=false;
				break;
			}
		}
		if(allready==true) {
			for(Player p : players) {
				p.setReady(0);
			}
			WhosTurn();
			GameStart(players.size(), false);
		}
		return;
	}
	/**
	 * ��� Ŭ���̾�Ʈ�鿡�� players ������ �ѷ��־� ������ �ֽ�ȭ��Ŵ.
	 */
	public void Refresh() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Refresh(players));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * ����Ŭ���̾�Ʈ->��������������� ����q�� ���û->����������κ��� ���޹޾� ������ ����
	 * @param q: �� ���� �÷��̾�
	 */
	public void Ban(Player q) {
		while(q.getUth().isAlive()) {	// �� ���� ���������尡 ���� ����ִٸ� �켱 �׿���.
			q.getUth().toStop();
			q.getUth().interrupt();
		}
		while(!q.getSocket().isClosed()) {
			try{ q.getSocket().close();	}catch(Exception e) { e.printStackTrace(); }	// Ȥ�� ������ Socket�� �ȴ����ٸ�, �ݾ���. ���� ������ �ݺ�.
		}
		String strtemp = q.getUser().getNickname();		// ���� ����ߴ��� �˸��޼����� ǥ�����ֱ� ����, �÷��̾� ������ ������ ���� �г��Ӹ� ���.
		players.remove(q);
		int index=0;
		for(Player p : players) {		// ����� �÷��̾ �߰��� ��������, �ٽ� ������� order�� 0���� ������� ������.
			p.setOrder(index++);
		}
		checkUth();						// Ȥ�� ����ϸ鼭 �ٸ� ������ �����尡 ������ �־����� ���캸��, �ִٸ� ��������.
		MoneyRefresh();					// �� ���ϸ鼭 READY�� �÷��̾��� �ǵ� �����ֹǷ� �� ���ñ� ���� �ֽ�ȭ.
		Date d = new Date();			// ���� �� ���ߴ��� �����ִ� �����鿡�� �˸��޼��� ����.
		try {
			for(Player p : players) {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.HesBanned(strtemp, f.format(d), players));
				out.flush();
			}
		} catch(Exception e) {e.printStackTrace();}
		
		Refresh();						// Ŭ���̾�Ʈ�鿡�� �÷��̾� ���� �ֽ�ȭ������.
		return;
	}
	/**
	 * Ŭ���̾�Ʈ->����������� �ڽ�q�� ������ ��û->����������κ��� ���޹޾� ����q�� ���� ����. (����q�� q��� Ŭ���̾�Ʈ���� ��ü Socket Close �� â Dispose.)
	 * @param q: ������ ����
	 * @param strtemp: ������ ������ �г���
	 */
	public void SelfOut(Player q, String strtemp) {
		while(q.getUth().isAlive()) {	// ������ ��ȣ�� ���� �ش� ����������� ������ ����� ���������� ��Ұ�����, Ȥ�� �𸣴� ����� ����� ������ ��ٸ�.
		}
		while(!q.getSocket().isClosed()) {
			try{ q.getSocket().close();	}catch(Exception e) { e.printStackTrace(); }	// Ȥ�� ������ Socket�� �ȴ����ٸ�, �ݾ���. ���� ������ �ݺ�.
		}
		getPlayers().remove(q);
		int index=0;
		for(Player pl : players) {		// ���� �÷��̾ �߰��� ��������, �ٽ� ������� order�� 0���� ������� ������.
			pl.setOrder(index++);
		}
		checkUth();						// Ȥ�� �����鼭 �ٸ� ������ �����尡 ������ �־����� ���캸��, �ִٸ� ��������.
		Date d = new Date();
		for(Player p2 : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
				out.writeObject(Gaming.HesOut(strtemp, f.format(d), players));
				out.flush();
			} catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * ������ ƨ���� ��. ������ ���� �͵� �ƴϰ�, ������ ������ �͵� �ƴѵ� � ������������ InputStream���� ���ܰ� �߻��ϸ� ƨ�� ���̹Ƿ�, �Ʒ� �޼��尡 ����ǵ��� ��.
	 */
	public void HesClosed() {
		ArrayList<Player> tmp = new ArrayList<>();
		ArrayList<String> tmpstr = new ArrayList<>();
		Date d = new Date();
		for(Player p : players) {
			if(p.getUth().isStop()==true) {			// �������� ���������尡 ������¶��(ƨ�� ���ܹ߻��� ����� toStop() ��ȣ�� ������.)
				tmpstr.add(p.getUser().getNickname());		// ������ �г����� �̸� �����صΰ�
				while(p.getUth().getThreadGroup()!=null) {			// ���������尡 ���� �������̶�� stop=true �ƾ in.close()�� �ƴ����� �Ҿ��ϰ�, �ٸ� �����ɵ� �ʿ��� �� ���Ƽ�.
					try { p.getUth().getIn().close(); } catch(Exception e) { e.printStackTrace(); }
					p.getUth().interrupt();
				}
				if(!p.getSocket().isClosed()) {
					try { p.getSocket().close(); } catch(Exception e) { e.printStackTrace(); }
				}
			} else {								// ���������尡 ����� ���ư��� �ֶ�� ���󸮽�Ʈ(tmp)�� �߰�.
				tmp.add(p);
			}
		}
		int index=0;
		for(Player p : tmp) {		// tmp�� �߰��� Player���� �������� �ֵ��̰�, 0���� order ������ ��, �� tmp�� players�� ���.
			p.setOrder(index++);
		}
		setPlayers(tmp);
		Refresh();					// Ŭ���̾�Ʈ�鿡�� players ���� �ֽ�ȭ������.
		for(Player p : getPlayers()) {			// ���� ����鿡�� ���� ƨ����� �˷���(�޼����� �׳� ������ ���� ����ó�� ����)
			try {
				for(String s : tmpstr) {
					ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
					out.writeObject(Gaming.HesOut(s, f.format(d), players));
					out.flush();
				}
			} catch(Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * Ŭ���̾�Ʈ�鿡�� ���� ����(0~3)�� ������ �ѷ���.
	 */
	public void WhosTurn() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Turn(getWhosturn()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * ���� �� ���� ��. �ǹ̾����� üũ�������� �� �� ����. ���۽� 1��, ī�� ���ι����� 1���̶�, 1���̸鼭 3��°ī�带 �޾Ҵٸ� üũ�� ������.
	 */
	public void IncreaseTurn() {
		increaseTurn();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.TurnRefresh(getTurn()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * ������ ����� ��.
	 * 1�� ���Ҵٸ� �� ����� ������ ����ϰų�,
	 * ���� �ƹ��͵� ���� �÷��̾ �ִٸ� ����� �ʿ���� ���� �Ѿ��,
	 * ���� 3��° ī�带 ���� ���� ���¿��� + 2���̻� ����ִµ� + �ֱ� ���ñݾױ��� ��� ������ 3��° ī�带 �ްԵǰ�,
	 * 3��° ī�嵵 �޾Ҵµ� + 2���̻� ����ְ� + 
	 */
	public void nextTurn() {
		getNowtimer().setKill(true);
		while(!getNowtimer().isAlive()) {
		}
		/**
		 * alive: ���̺������� ���� ������ ���� ��.
		 * sumcall: �ݺ����� ������ ���� ��.
		 * ischecked: ���� üũ�ߴ��� ��. �ݿ� üũ�� ���� ���� �������� ���� ���ٸ�, ������ ����� ���� �������� ��� �����Ḧ ������ ��.
		 * sumone: ���� ��� ���õ� ���� ���� ������ ���� ��. ��ΰ� �������� ���� �̻�, �� ����� ���� �����ϱ� �ؾ���.
		 */
		int alive=0;
		int sumcall=0;
		int ischecked=0;
		int sumnone=0;
		for(Player p : players) {
			if(p.getBetbool()!=1) { alive++; }
			if(p.getBetbool()==2) { sumcall++; }
			if(p.getBetbool()==4) { ischecked++; }
			if(p.getBetbool()==0) { sumnone++; }
		}
		
		// �ƹ��� ������ ���� ���ؼ� �� ����;;
		if(alive==0) {
			for(Player p : players) {
				p.setGameresult(2);	// ��� ���� ��� ó��.
			}
			toresult();					// Ŭ���̾�Ʈ�鿡�� ��������ȭ�� ���� ��.
			Message_Re();				// Ŭ���̾�Ʈ�鿡�� 3���� ����ڳ��� �����Ѵٰ� �˸� �޼��� ����.
			try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
			GameStart(thisplaynum, true);		// �̹��� �ο� ���� ����
			return;
		} else if(alive==1) {
			// ����ִ»�����̱�
			for(Player p : players) {
				if (p.getBetbool()!=1) {
					// �갡 �������
					ArrayList<String> tmplist = new ArrayList<>();
					for(Player q : players) {
						q.setGameresult(0);
					}
					p.setGameresult(1); calc(p);
					tmplist.add(p.getUser().getId());
					toresult();
					Message_Win(p.getUser().getNickname());
					try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
					// ��ưȰ��ȭ
					resetcards();
					rebatch(tmplist, false);
					toButtonOk();
					for(Player q : players) {
						q.setBetbool(0);
					}
					Refresh();
					setInggame(false);
					break;
				}
			}
			return;
		}
		else {
			if(sumnone>0) {
				for(int i=1;i<players.size();i++) {
					Player p = players.get((getWhosturn()+i)%players.size());
					if(p.getBetbool()!=1) {
						setWhosturn(p.getOrder());
						break;
					}
				}
				WhosTurn();
				if(isInggame()==true) {
					players.get(getWhosturn()).setBetbool(0);
					Refresh();
					
					setTurn(getTurn()+1);
					makeTimer();
				}
			} else {
				// �÷��̾���� �ֱ� ���ñݾ��� �������� �Ǻ�
				boolean allbetequal = true;
				int bet = 0;
				for(Player p : players) {
					if(p.getBetbool()!=1) {
						if(bet==0) {
							bet=p.getThisbet();
						} else {
							if(p.getThisbet()!=bet) {
								allbetequal = false;
								break;
							}
						}
					}
				}
				if(alive==ischecked+sumcall && isPhase2()==true) {
					// ����
					ArrayList<Player> tmplist = new ArrayList<>();
					for(Player p : players) {
						if(p.getBetbool()!=1) tmplist.add(p);
					}
					makeSelSetTimer(tmplist);
				} else if(allbetequal==true) {
					if(isPhase2()==false) {
						getNowtimer().setKill(true);
						Draw2Phase();
					} else {
						// ����
						ArrayList<Player> tmplist = new ArrayList<>();
						for(Player p : players) {
							if(p.getBetbool()!=1) tmplist.add(p);
						}
						makeSelSetTimer(tmplist);
					}
				} else {
					// �� �ѱ��
					for(int i=1;i<players.size();i++) {
						Player p = players.get((getWhosturn()+i)%players.size());
						if(p.getBetbool()!=1) {
							setWhosturn(p.getOrder());
							break;
						}
					}
					WhosTurn();
					if(isInggame()==true) {
						players.get(getWhosturn()).setBetbool(0);
						Refresh();
						
						setTurn(getTurn()+1);
						makeTimer();
					}
				}
			}
		}
		return;
	}
	/**
	 * ��Ƴ��� ������ 2�� �̻��ε� �ǳ����Ⱑ ��û�� ���, �Ʒ��� �޼��尡 ����������.
	 * @param players: �� �޼��带 ȣ���� ��, ��Ƴ��� �����鸸 ��� List�� ���ڷ� ȣ���� ����.
	 */
	public void whosWin(ArrayList<Player> players) {
		int num = players.size();
		String userid1=players.get(0).getUser().getId();
		String userid2=players.get(1).getUser().getId();
		int cardresult1=players.get(0).getSelCardset();
		int cardresult2=players.get(1).getSelCardset();
		String userid3=null;
		int cardresult3=0;
		String userid4=null;
		int cardresult4=0;
		if(players.size()>2) {								// ���� ������ ����ؼ� ������, ���� ������ ������ num���� �÷��̾� ���� �����Ƿ�, �Ǻ� Logic������ num �ʰ� ������ null�̶� �������.
			userid3=players.get(2).getUser().getId();
			cardresult3=players.get(2).getSelCardset();
		}
		if(players.size()>3) {
			userid4=players.get(3).getUser().getId();
			cardresult4=players.get(3).getSelCardset();
		}
		// ���� �÷��̾� ���� �����и� ��� List�� ��ȯ����.
		ArrayList<String> result = Logic.GameResult(num, userid1, cardresult1, userid2, cardresult2, userid3, cardresult3, userid4, cardresult4);
		for(Player p : players) {
			p.setGameresult(0);		// �켱 ��� �÷��̾��� ���Ӱ���� 0(�й�)�� �صΰ�,
		}
		if(result.size()==1) {		// ��ȯ���� List�� �÷��̾ 1���̶�� ȥ�� ����� ���̴�, �� 1�� ��� ó��.
			ArrayList<String> tmplist = new ArrayList<>();
			for(Player p : players) {
				if(p.getUser().getId().equals(result.get(0))) { p.setGameresult(1); calc(p); tmplist.add(p.getUser().getId()); Message_Win(p.getUser().getNickname()); break; }
			}
			toresult();					// Ŭ���̾�Ʈ�鿡�� ��������ȭ�� ���� ��.
			try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
			resetcards();				// 5�� �� ī��� �߰��� ��ġ��Ű��, �Ʒ� �÷��̾� ���� �ֽ�ȭ���� ����.
			rebatch(tmplist, false);		// �̱� ����� ��(����)�� �Ǳ� ���� ���ġ
			toButtonOk();				// ������ �������� Ŭ���̾�Ʈ�鿡�� �ɸ� ��ư��Ȱ���� Ǯ�� ���� ���� �ƴ� ���·� ���ư��� ��.
			for(Player q : players) {	// ���� ���û��µ� 0���� ��� �ʱ�ȭ�ϰ�
				q.setBetbool(0);
			}
			Refresh();					// Ŭ���̾�Ʈ�鿡�� �÷��̾� ���� �ֽ�ȭ
			setInggame(false);			// ���������� ���� �� �ƴ϶�� ǥ��.
		} else {		// ��ȯ���� List�� �÷��̾ 2�� �̻��̶�� �����ؾ��ϴ�, ���� ó��.
			for(String s : result) {
				for(Player p : players) {
					if(p.getUser().getId().equals(s)) { p.setGameresult(2); break; }	// ��ȯ���� List�� �ش��ϴ� �÷��̾�� ���Ӱ�� ���� ��� ó��.
				}
			}
			toresult();					// Ŭ���̾�Ʈ�鿡�� ��������ȭ�� ���� ��.
			Message_Re();				// Ŭ���̾�Ʈ�鿡�� 3���� ����ڳ��� �����Ѵٰ� �˸� �޼��� ����.
			try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
			rebatch(result, true);		// ����ڳ��� ������ ��ġ
			GameStart(result.size(), true);		// �ٷ� ����ڸ� ���� ����(������ �÷��̾�� �� ���·� �е� �������� ä ���游 �ϰԵ� ����)
		}
		return;
	}
	private int resultbacksig;
	public int getResultbacksig() { return resultbacksig; }
	public void setResultbacksig(int resultbacksig) { this.resultbacksig = resultbacksig; }
	/**
	 * Ŭ���̾�Ʈ�鿡�� ���� ���ȭ���� ����� ��ȣ ����
	 */
	public void toresult() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Gameresult(players));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * Ŭ���̾�Ʈ�鿡�� ������ �������Ƿ�, ��Ȱ��ȭ�Ǿ��� ��ư�� Ȱ��ȭ�϶�� �˷���.
	 */
	public void toButtonOk() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.ButtonOk());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * Ŭ���̾�Ʈ�鿡�� �Ѻ��ñݰ� �ּҺ��ñ� ������ �ֽ�ȭ��Ŵ.
	 */
	public void MoneyRefresh() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.MoneyRefresh(getMoneythisgame(), getMinforbet()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * Ŭ���̾�Ʈ�鿡�� ī��� �߰����� ������� ������.
	 */
	public void resetcards() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.ResetCard());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * 2������� �Ѿ�� �Ǹ�(3��° ī�带 �޴� �����  ������, 1������� ���̷� ���ǳ��� ������) �÷��̾�� ���� �� �ִ� �����и� ����ص�(4ĭ 1����� �� 3ĭ�� ���� ����)
	 */
	public void timeToCardset() {
		for(Player p : players) {
			if(p.getBetbool()!=1) {
				p.setBetbool(0);
				p.setCardset(Logic.ResultSet(p.getCard1(), p.getCard2(), p.getCard3()));
			}
		}
		return;
	}
	/**
	 * players�� ������ �׿����� order�� ���ġ��. ������ ���(�̱� ����� ����(��))�� ����� ���� ����ڰ� ���ʿ� ��ġ�Ͽ� ��⸦ ������ ��. �� � ���� ������ �� �� ����� ���� ȣ���.
	 * @param replayers: ���ʿ� ��ġ�Ǿ���� ������ id�� ��� ����Ʈ�� ���ڷ� �޾ƿ�.
	 * @param rematch: ���⸦ ���� ��ġ�� true / �׳� ���ġ�� ��� false �� ȣ���.
	 */
	public void rebatch(ArrayList<String> replayers, boolean rematch) {
		ArrayList<Player> init=getPlayers();
		ArrayList<Player> temp1=new ArrayList<>();
		ArrayList<Player> temp2=new ArrayList<>();
		for(Player p : init) {
			if (replayers.contains(p.getUser().getId())) {
				temp1.add(p);
				if(rematch==true) p.setGameresult(2);
			} else {
				temp2.add(p);
				if(rematch==true) p.setGameresult(0);
			}
		}
		ArrayList<Player> newpl = new ArrayList<>();
		int index=0;
		for(Player p : temp1) {
			p.setOrder(index++);
			newpl.add(p);
		}
		for(Player p : temp2) {
			p.setOrder(index++);
			newpl.add(p);
		}
		setPlayers(newpl);
		return;
	}
	/**
	 * ����� �÷��̾�� ��ü ���õ� �ݾ��� �ְ� ��. �� ��ü ���ñ��� 0����, ����� �÷��̾��� ���� �׸�ŭ ����.
	 * @param p: ����� �÷��̾�.
	 */
	public void calc(Player p) {
		p.getUser().setMoney(p.getUser().getMoney()+moneythisgame);
		serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()+moneythisgame);
		setMoneythisgame(0);
		for(Player q : players) {
			q.setGameresult(0);
		}
		p.setGameresult(1);
		Refresh();
	}
	/**
	 * Ŭ���̾�Ʈ�鿡�� ���� �̰�ٰ� �޼����� ����.
	 * @param nick: �̱� �÷��̾��� �г���
	 */
	public void Message_Win(String nick) {
		Date d = new Date();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Message_Win(nick, f.format(d), players));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * Ŭ���̾�Ʈ�鿡�� ������ �ٲ��ڰ� �� �ݾ��� �ǵ����� ������ �ֽ�ȭ.
	 * @param pandon: �ǵ�
	 */
	public void Pandon(int pandon) {
		setPandon(pandon);
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Pandon(pandon));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * Ŭ���̾�Ʈ�鿡�� 3���� ����ڳ��� �����Ѵٴ� �޼��� ����.
	 */
	public void Message_Re() {
		Date d = new Date();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Message_Re(f.format(d)));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void Draw2Phase() {
		getNowtimer().setKill(true);	// ���� �ִ� Ÿ�̸� ����(Ŭ���̾�Ʈ���� ���۵Ǵ� Ÿ�̸� ���� ��� ����.)
		setPhase2(true);				// 3��° ī�� �����ְ� �Ǵ� ��Ȳ�̶�� ǥ��
		timeToCardset();				// ������ ������� ī�� 3������ ���� �� �ִ� �����п� ���� 3���� ����� ���� ��Ƶ�.
		Refresh();						// �÷��̾���� ������ 3���� ����� ���� �ްԵ�.
		for(Player p : players) {		// �÷��̾���� �Ʒ� ��ȣ�� �޾� GUI������ ī�带 �޴� �׼��� ���Ե�.
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Draw2Phase());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }		// ī�� �׼� ��ٸ���
		// 3��° ī�� �޵�, ������ ���� ������ ���ư�(���� �׾����� �� �������� ����ִ� �÷��̾�κ���)
		for(Player p : players) {
			if(p.getBetbool()!=1) {
				setWhosturn(p.getOrder());
				break;
			}
		}
		setTurn(1);
		if(isInggame()==true) {
			players.get(getWhosturn()).setBetbool(0);
			Refresh();
			makeTimer();
		}
		return;
	}
	/**
	 * ī�带 ���� ��, ������ ���ʷ� ī�� ������ ����. ���Ŀ� GIVE CARD ��ȣ�� ���� �� �� ������ ������ ����.
	 */
	public void cardShuffle() {
		Collections.shuffle(cards);
		int index=0;
		for(Player p : players) {
			p.setCard1(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard2(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard3(cards.get(index++));
		}
		return;
	}
	/**
	 * Ÿ�̸� �����带 ����. Ÿ�̸� �����ø��� �� �����带 ȣ��.
	 */
	public void makeTimer() {
		Timer t = new Timer(this, players.get(getWhosturn()), getTurn());
		setNowtimer(t);
		t.setDaemon(true);
		t.start();
		return;
	}
	/**
	 * ���� �� ���� ù Ÿ�̸� �����带 ����. �� ����� �� ó���� ���� ����Ÿ�̸ӿ��� �� �����带 ȣ��.
	 */
	public void makeOpenTimer() {
		Timer t = new OpenTimer(this);
		setNowtimer(t);
		t.setDaemon(true);
		t.start();
		return;
	}
	/**
	 * ���� �� ���� ������ Ÿ�̸� �����带 ����. �� ����� �� �������� ���� ������Ÿ�̸ӿ��� �� �����带 ȣ��.
	 */
	public void makeSelSetTimer(ArrayList<Player> pl) {
		Timer t = new SelSetTimer(this, pl);
		setNowtimer(t);
		t.setDaemon(true);
		t.start();
		return;
	}
	/**
	 * Ŭ���̾�Ʈ�鿡�� ���� ���� ��ȣ�� ����.
	 * @param num: �̹� ���ӿ� ���ۺ��� �����ϴ� �÷��̾� ��
	 */
	public void sendStartSignal(int num) {
		for(Player p : players) {
			System.out.println(p.getUser().getId());
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Gamestart(num, getMoneythisgame(), getMinforbet()));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * Ŭ���̾�Ʈ�鿡�� ī�� 2���� �ָ鼭 ī�带 ���� GUI �׼��� ������.
	 */
	public void giveFirstCards() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.GiveCard(players));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * ���� ������ ���� �޼���
	 * @param num: ���� ���� �ο�
	 * @param rematch: �� ������ ���� ��������, �����ϴ� �������
	 */
	public void GameStart(int num, boolean rematch) {
		setThisplaynum(num);
		setPhase2(false);
		setInggame(true);
		if(rematch==false) setMinforbet(0);		// ���Ⱑ �ƴ϶� �ƿ� �������̶�� �ּ��ǵ��� �ʱ�ȭ 
		for(Player p : players) {
			p.setThisbet(0);
			p.setTrash(0);
			p.setGameresult(0);
			p.setCardset(new int[4]);
			if(rematch==false) {				// �ƿ� �������̶�� ��� ���������� ���ְ�,
				p.setBetbool(0);
			} else {							// ������ ��� ���������� ���̺������� �� ��,
				p.setBetbool(1);
			}
		}
		for(int i=0;i<num;i++) {
			players.get(i).setBetbool(0);		// ���� ����ڸ� ���������� �ʱ�ȭ�Ͽ� ��⿡ ������Ŵ. (�������� ���̺��û��¶� ���� ���� ����.)
		}
		Refresh();
		setTurn(1);
		setWhosturn(0);
		WhosTurn();
		cardShuffle();
		try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
		if(rematch==true) {
			try { Thread.sleep(900); } catch (InterruptedException e) { e.printStackTrace(); }
			sendStartSignal(num);
		} else {
			sendStartSignal(players.size());
		}
		giveFirstCards();
		try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
		makeOpenTimer();
		return;
	}
	/**
	 * ���ư��� ���������� ��, ������Ų �����尡 �ƴѵ��� ������ ���� �����尡 �ִ��� �˻��ϰ�, �ִٸ� ������Ŵ.
	 */
	public void checkUth() {
		for(Player p : players) {
			// ���������尡 �������� ���� ���� �ƴѵ�, ���������尡 ���� ���� �ƴ϶��(������ �׷��� main�� �ƴϰ� null)
			if(p.getUth().isStop()==false && p.getUth().getThreadGroup()==null) {
				p.getUth().setDaemon(true);
				p.getUth().start();		// ���������带 �ٽ� ���������.
			}
		}
		return;
	}
}