package Gameserver;

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

import Chatserver.Message;
import Logic.Logic;

class Timer extends Thread {
	Server serv=null;
	Player p=null;
	ArrayList<Player> players=null;
	public Timer(Server serv, Player p) {
		this.serv=serv; this.p=p;
	}
	public void run() {
		for(int i=0;i<100;i++) {
			if(p.getBetbool()!=0) {
				p.getUth().IncreaseTurn();
				return;
			}
			else {
				try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
				players = serv.getPlayers();
				for(Player p2 : players) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
						out.writeObject(Gaming.Timer(serv.getWhosturn(), i));
//						out.writeObject(new Gaming(Gaming.GAME_TIMER, serv.getWhosturn(), i));
						out.flush();
					} catch(Exception e) {e.printStackTrace();}
				}
			}
		}
		players = serv.getPlayers();
		Player p = players.get(serv.getWhosturn());
		p.getUth().IncreaseTurn();
		p.getUth().toDie();
//		for(Player p2 : players) {
//			try {
//				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
//				out.writeObject(new Gaming(p.getUserid(), Gaming.GAME_DIE, players));
//				out.flush();
//			} catch(Exception e) {e.printStackTrace();}
//		}
		return;
	}
	
}

class CheckMem extends Thread {
	Server serv;
	public CheckMem(Server serv) {
		this.serv=serv;
	}
	public void run() {
		while(true) {
			ArrayList<Player> players = serv.getPlayers();
			int index=0;
			boolean del=false;
			for(Player pl : players) {
				if(pl.getSocket().isBound()==false) {
					try { pl.getUth().getIn().close(); } catch (IOException e) { e.printStackTrace(); }
					pl.getUth().toStop();
					players.remove(index);
					del=true;
				}
				index++;
			}
			if(del==true) {
				index=0;
				for(Player pl : players) {
					pl.setOrder(index++);
				}
			}
		}
	}
}

class UserThread extends Thread {
	private Socket socket;
	private boolean stop;
	private Server serv;
	private Format f = new SimpleDateFormat("a hh:mm");
	private ObjectInputStream in;
	public ObjectInputStream getIn() {
		return in;
	}
	public UserThread(Socket socket, Server serv) {
		stop=false; this.socket=socket; this.serv=serv;
	}
	public void toStop() {
		stop=true;
	}	
	
	public void IncreaseTurn() {
		serv.increaseTurn();
		ArrayList<Player> players = serv.getPlayers();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.TurnRefresh(serv.getTurn()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void MoneyRefresh() {
		ArrayList<Player> players = serv.getPlayers();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.MoneyRefresh(serv.getMoneythisgame(), serv.getMinforbet()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void MatchId() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.writeObject(new Gaming(Gaming.IDMATCH));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void toDie() {
		ArrayList<Player> players = serv.getPlayers();
		Player p = players.get(serv.getWhosturn());
		p.setBetbool(1);
		serv.nextTurn();
	}
	
	public void run() {
		MatchId();
		try {
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while(true) {
				if(stop==true) { return; }
				else {
					Gaming gm = (Gaming)in.readObject();
					ArrayList<Player> players = serv.getPlayers();
					Player q=null;
					int index=0;
					for(Player p : players) {
						if(p.getSocket().equals(socket)) {
							q = p;
							switch(gm.getWhat()) {
								case Gaming.IDMATCH: {
									q.setUserid(gm.getUserid());
									break;
								}
								case Gaming.REFRESH: {
									serv.Refresh();
									break;
								}
								case Gaming.MONEY_REFRESH: {
									MoneyRefresh();
									break;
								}
								case Gaming.GAME_WHOSTURN: {
									serv.WhosTurn();
									break;
								}
								case Gaming.GAME_UNREADY: {
									p.setReady(0);
									p.setMoney(p.getMoney()+10);
									serv.setMoneythisgame(serv.getMoneythisgame()-10);
									MoneyRefresh();
									serv.Refresh();
									break;
								}
								case Gaming.GAME_READY: {
									p.setReady(1);
									p.setMoney(p.getMoney()-10);
									serv.setMoneythisgame(serv.getMoneythisgame()+10);
									MoneyRefresh();
									serv.Refresh();
									break;
								}
								case Gaming.GAME_START: {
//									serv.GameStartBool();
									for(Player p2 : players) {
										p2.setReady(0);
									}
									serv.WhosTurn();
									serv.GameStart();
									break;
								}
								case Gaming.GAME_DIE: {
//									p.setBetbool(1);
//									nextTurn();
//									if(serv.isInggame()==true) {
//										players.get(serv.getWhosturn()).setBetbool(0);
//										Refresh();
//										
//										Timer t = new Timer(serv, players.get(serv.getWhosturn()));
//										t.setDaemon(true);
//										t.start();
//									}
									toDie();
									break;
								}
								case Gaming.GAME_CALL: {
									p.setBetbool(2);
									p.setMoney(p.getMoney()-serv.getMinforbet());
									serv.setMoneythisgame(serv.getMoneythisgame()+serv.getMinforbet());
									MoneyRefresh();
									serv.nextTurn();
									break;
								}
								case Gaming.GAME_HALF: {
									p.setBetbool(3);
									p.setMoney(p.getMoney()-serv.getMoneythisgame()/2);
									serv.setMinforbet(serv.getMoneythisgame()/2);
									serv.setMoneythisgame(serv.getMoneythisgame()+serv.getMoneythisgame()/2);
									MoneyRefresh();
									serv.nextTurn();
									break;
								}
								case Gaming.GAME_CHECK: {
									p.setBetbool(4);
									serv.nextTurn();
									break;
								}
								case Gaming.CHAT: {
									String msg = gm.getMsg();
									Date d = new Date();
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(Gaming.Chat(q.getNickname(), msg, f.format(d)));
//										out.writeObject(new Gaming(q.getNickname(), gm.getWhat(), msg, f.format(d)));
										out.flush();
									}
									break;
								} 
								case Gaming.CHAT_JOIN: {
									Date d = new Date();
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(Gaming.HesIn(q.getNickname(), f.format(d), players));
//										out.writeObject(new Gaming(q.getNickname(), gm.getWhat(), f.format(d), players));
										out.flush();
									}
									break;
								}
								case Gaming.CHAT_LEAVE: {
									String strtemp = q.getNickname();
									q.getUth().getIn().close();
									q.getUth().toStop();
									players.remove(index);
									index=0;
									for(Player pl : players) {
										pl.setOrder(index++);
									}
									Date d = new Date();
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(Gaming.HesOut(strtemp, f.format(d), players));
//										out.writeObject(new Gaming(strtemp, gm.getWhat(), f.format(d), players));
										out.flush();
									}
									break;
								}
								case Gaming.CHAT_NICKCHANGE: {
									String temp = q.getNickname();
									p.setNickname(gm.getMsg());
									Date d = new Date();
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(Gaming.NickChange(temp, gm.getMsg(), f.format(d), players));
//										out.writeObject(new Gaming(temp, gm.getWhat(), gm.getMsg(), f.format(d), players));
										out.flush();
									}
								}
								default: break;
							}
						}
						else { index++; }
					}
					
				}
			}
		}
		catch(Exception e) {e.printStackTrace();}
		
		
//		while(true) {
//			try {
//				ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
//					if(stop==true) { return; }
//					else {
//						Gaming gm = (Gaming)in.readObject();
//						int num = gm.getWhat();
//						List<Player> players = serv.getPlayers();
//						for(Player p : players) {
//							if(p.getSocket().equals(socket)) {
//								switch(num) {
//									case Gaming.GAME_UNREADY: p.setReady(0); System.out.println("�غ����"); break;
//									case Gaming.GAME_READY: p.setReady(1); System.out.println("�غ���"); break;
//									case Gaming.GAME_DIE: p.setBetbool(1); break;
//									case Gaming.GAME_GO: p.setBetbool(2); break;
//									default: break;
//								}
//								break;
//							}
//						}
//						
//						
//						
//						
//						
//					}
//			}
//			catch(Exception e) {}
//		}
		
		
		
		
	}
	
//	private Player player;
//	
//	public UserThread(Player player) {
//		this.player=player;
//	}
}

public class Server {
	private int port = 53891;
	private ServerSocket server;
	
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Integer> cards = new ArrayList<>();
	/**
	 * �̹� ���� ��� ��ȣ(0-3)
	 */
	private int whosturn=0;
	public int getWhosturn() {
		return whosturn;
	}
	public void setWhosturn(int whosturn) {
		this.whosturn = whosturn;
	}
	private boolean inggame=false;
	public boolean isInggame() {
		return inggame;
	}
	public void setInggame(boolean inggame) {
		this.inggame = inggame;
	}
	private int moneythisgame=0;
	private int minforbet=0;
	public int getMoneythisgame() {
		return moneythisgame;
	}
	public void setMoneythisgame(int moneythisgame) {
		this.moneythisgame = moneythisgame;
	}
	public int getMinforbet() {
		return minforbet;
	}
	public void setMinforbet(int minforbet) {
		this.minforbet = minforbet;
	}
	private int turn;
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public void increaseTurn() {
		this.turn++;
	}
	private int thisplaynum;
	public int getThisplaynum() {
		return thisplaynum;
	}
	public void setThisplaynum(int thisplaynum) {
		this.thisplaynum=thisplaynum;
	}
	
	public Server() {
		Integer[] temp = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		cards = new ArrayList<>(Arrays.asList(temp));
		this.run();
	}
	
//	public void Add() {
//		players.add(new Player(players.size(), null));
//	}
	
	
	public void run() {
		try {
			CheckMem cm = new CheckMem(this);
			cm.setDaemon(true);
			cm.start();
			server = new ServerSocket(port);
			while(true) {				
				Socket receive = server.accept();
				boolean exit = false;
				for(Player p : players) {
					if(p.getSocket().equals(receive)) {
						exit = true;
						break;
					};
				}
				if(exit==false) {
					UserThread uth = new UserThread(receive, this);
					uth.setDaemon(true);
					Player p = new Player(players.size(), receive, uth);
					players.add(p);
					uth.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players=players;
	}
	
	public void GameStartBool() {
		if(players.size()==4) {
			boolean allok = true;
			for(Player p : players) {
				if(p.getReady()!=1) {
					allok = false;
					System.out.println("���� ��� �÷��̾ �غ���� �ʾҽ��ϴ�!");
				}
			}
			if(allok==true) {
				GameStart();
			}
		}
		else {
			System.out.println("�÷��̾� ���� �����մϴ�! (���� "+players.size()+"��)");
		}
	}
	public void Refresh() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Refresh(players));
//				out.writeObject(new Gaming(Gaming.REFRESH, players));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void WhosTurn() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Turn(getWhosturn()));
//				out.writeObject(new Gaming(Gaming.GAME_WHOSTURN, serv.getWhosturn()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void nextTurn() {
		int alive=0;
		int sumcall=0;
		int ischecked=0;
		int sumnone=0;
		for(Player p : players) {
			if(p.getBetbool()!=1) {
				alive++;
			}
			if(p.getBetbool()==2) {
				sumcall++;
			}
			if(p.getBetbool()==4) {
				ischecked++;
			}
			if(p.getBetbool()==0) {
				sumnone++;
			}
		}
		
		
		if(alive==1) {
			// ����ִ»�����̱�
			for(Player p : players) {
				if (p.getBetbool()!=1) {
					// �갡 �������
					ArrayList<String> tmplist = new ArrayList<>();
					for(Player q : players) {
						q.setGameresult(0);
					}
					p.setGameresult(1); calc(p);
					tmplist.add(p.getUserid());
					toresult();
					try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
					// ��ưȰ��ȭ
					resetcards();
					justrebatch(tmplist);
					toButtonOk();
					for(Player q : players) {
						q.setBetbool(0);
					}
					Refresh();
					setInggame(false);
					break;
				}
			}
		}
		else {
			if(sumnone>0) {
				for(int i=1;i<players.size();i++) {
					Player tempp = players.get((getWhosturn()+i)%players.size());
					if(tempp.getBetbool()!=1) {
						setWhosturn(tempp.getOrder());
						break;
					}
				}
				WhosTurn();
				if(isInggame()==true) {
					players.get(getWhosturn()).setBetbool(0);
					Refresh();
					
					Timer t = new Timer(this, players.get(getWhosturn()));
					t.setDaemon(true);
					t.start();
				}
			} else {
				if (alive==sumcall+ischecked) {
					// ����
					ArrayList<Player> tmplist = new ArrayList<>();
					for(Player p : players) {
						if(p.getBetbool()!=1) tmplist.add(p);
					}
					whosWin(tmplist);
				} else {
					for(int i=1;i<players.size();i++) {
						Player tempp = players.get((getWhosturn()+i)%players.size());
						if(tempp.getBetbool()!=1) {
							setWhosturn(tempp.getOrder());
							break;
						}
					}
					WhosTurn();
					if(isInggame()==true) {
						players.get(getWhosturn()).setBetbool(0);
						Refresh();
						
						Timer t = new Timer(this, players.get(getWhosturn()));
						t.setDaemon(true);
						t.start();
					}
				}
			}
		}
			

		return;
	}
	public void whosWin(ArrayList<Player> players) {
		int num = players.size();
		String userid1=players.get(0).getUserid();
		String userid2=players.get(1).getUserid();
		int cardresult1=players.get(0).getCardset();
		int cardresult2=players.get(1).getCardset();
		String userid3=null;
		int cardresult3=0;
		String userid4=null;
		int cardresult4=0;
		if(players.size()>2) {
			userid3=players.get(2).getUserid();
			cardresult3=players.get(2).getCardset();
		}
		if(players.size()>3) {
			userid4=players.get(3).getUserid();
			cardresult4=players.get(3).getCardset();
		}
		ArrayList<String> result = Logic.GameResult(num, userid1, cardresult1, userid2, cardresult2, userid3, cardresult3, userid4, cardresult4);
		for(Player p : players) {
			p.setGameresult(0);
		}
		if(result.size()==1) {
			ArrayList<String> tmplist = new ArrayList<>();
			for(Player p : players) {
				if(p.getUserid().equals(result.get(0))) { p.setGameresult(1); calc(p); tmplist.add(p.getUserid()); break; }
			}
			// result.get(0) �� ���
			toresult();
			try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
			// ��ưȰ��ȭ
			resetcards();
			justrebatch(tmplist);
			
			
			
			toButtonOk();
			for(Player q : players) {
				q.setBetbool(0);
			}
			Refresh();
			setInggame(false);
		} else {
			for(String s : result) {
				for(Player p : players) {
					if(p.getUserid().equals(s)) { p.setGameresult(2); break; }
				}
			}
			// ����������� ����
			toresult();
			try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
			resetcards();
			rebatch(result);
			for(Player q : players) {
				q.setBetbool(0);
			}
			Refresh();
			rematch(result.size());
		}
	}
	public void toresult() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Gameresult(players));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void toButtonOk() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.ButtonOk());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void resetcards() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.ResetCard());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void justrebatch(ArrayList<String> replayers) {
		ArrayList<Player> init=getPlayers();
		ArrayList<Player> temp1=new ArrayList<>();
		ArrayList<Player> temp2=new ArrayList<>();
		for(Player p : init) {
			if (replayers.contains(p.getUserid())) {
				temp1.add(p);
			} else {
				temp2.add(p);
			}
		}
		ArrayList<Player> newpl = new ArrayList<>();
		for(Player p : temp1) {
			newpl.add(p);
		}
		for(Player p : temp2) {
			newpl.add(p);
		}
		setPlayers(newpl);
		return;
	}
	public void rebatch(ArrayList<String> replayers) {
		ArrayList<Player> init=getPlayers();
		ArrayList<Player> temp1=new ArrayList<>();
		ArrayList<Player> temp2=new ArrayList<>();
		for(Player p : init) {
			if (replayers.contains(p.getUserid())) {
				temp1.add(p);
				p.setGameresult(2);
			} else {
				temp2.add(p);
				p.setGameresult(0);
			}
		}
		ArrayList<Player> newpl = new ArrayList<>();
		for(Player p : temp1) {
			newpl.add(p);
		}
		for(Player p : temp2) {
			newpl.add(p);
		}
		setPlayers(newpl);
		return;
	}
	public void calc(Player p) {
		p.setMoney(p.getMoney()+moneythisgame);
		for(Player q : players) {
			q.setGameresult(0);
		}
		p.setGameresult(1);
	}
	public void rematch(int num) {
		for(Player p : players) {
			p.setBetbool(1);
			p.setGameresult(0);
		}
		for(int i=0;i<num;i++) {
			players.get(i).setBetbool(0);
		}
		
		setInggame(true);
		setTurn(1);
		setMinforbet(0);
		Collections.shuffle(cards);
		int index=0;
		for(Player p : players) {
			p.setBetbool(0);
			p.setCard1(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard2(cards.get(index++));
		}
		for(Player p : players) {
			p.setCardset(Logic.Result(p.getCard1(), p.getCard2()));
		}
		for(int i=num;i<players.size();i++) {
			players.get(i).setBetbool(1);
		}
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Gamestart(num));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.GiveCard(p.getCard1(), p.getCard2(), p.getCard3(), p.getCardset()));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		Timer t = new Timer(this, players.get(getWhosturn()));
		t.setDaemon(true);
		t.start();
	}
	public void GameStart() {
		for(Player p : players) {
			p.setBetbool(0);
			p.setGameresult(0);
		}
		setInggame(true);
		setTurn(1);
		setMoneythisgame(0);
		setMinforbet(0);
//		for(Player p : players) {
//			p.setReady(2);
//			p.setBetbool(0);
//			p.setCard1(0);
//			p.setCard2(0);
//			p.setCard3(0);
//			p.setCardset(0);
//		}
//		Collections.shuffle(cards);
//		int index=0;
//		for(int i=0;i<4;i++) {
//			Player now = players.get((start+i)%4);
//			now.setCard1(cards.get(index++));
//		}
//		for(int i=0;i<4;i++) {
//			Player now = players.get((start+i)%4);
//			now.setCard2(cards.get(index++));
//		}
		Collections.shuffle(cards);
		int index=0;
		for(Player p : players) {
			p.setBetbool(0);
			p.setCard1(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard2(cards.get(index++));
		}
		for(Player p : players) {
			p.setCardset(Logic.Result(p.getCard1(), p.getCard2()));
		}
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Gamestart(players.size()));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.GiveCard(p.getCard1(), p.getCard2(), p.getCard3(), p.getCardset()));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		
		
		
		Timer t = new Timer(this, players.get(getWhosturn()));
		t.setDaemon(true);
		t.start();
		
	}
	
	
}