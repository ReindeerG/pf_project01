package sutta.gameserver;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Gaming implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int GAME_UNREADY = 1;
	public static final int GAME_READY = 2;
	public static final int GAME_START = 3;
	public static final int PANDON = 4;
	public static final int MUCHPANDON = 5;
	public static final int GAME_DIE = 10;
	public static final int GAME_CALL = 11;
	public static final int GAME_HALF = 12;
	public static final int GAME_CHECK = 13;
	public static final int MONEY_REFRESH = 89;
	public static final int TURN_REFRESH = 87;
	public static final int RESETCARDS = 501;
	public static final int GETCARD = 500;
	public static final int DRAW2 = 505;
	public static final int GAME_WHOSTURN = 600;
	public static final int GAME_TIMER = 606;
	public static final int GAME_RESULT = 608;
	public static final int GAME_RESULT_OK = 688;
	public static final int BUTTON_OK = 609;
	public static final int CHAT = 330;
	public static final int CHAT_JOIN = 331;
	public static final int CHAT_LEAVE = 332;
	public static final int CHAT_NICKCHANGE = 333;
	public static final int CHAT_WIN = 335;
	public static final int CHAT_RE = 336;
	public static final int BAN = 444;
	public static final int IMBANNED = 445;
	public static final int REFRESH = 88;
	public static final int IDMATCH = 77;
	
	
	private int who;
	private String userid;
	private int what;
	private ArrayList<Player> players;
	
	private String msg;
	private String date;
	
	private int card1;
	private int card2;
	private int card3;
	private int cardset;
	private int thisplaynum;
	private int turn;
	
	private int time;
	
	private int moneythisgame;
	private int minforbet;
	private int pandon;
	
	
	public Gaming() {}
	
	// Ŭ���̾�Ʈ���� ������ � ��� ��û��.
	public Gaming(int what) {
		setWhat(what);
	}
	
	
	// ������ ������ ������ �Ѹ���ddddddddd
	public Gaming(int what, int who) {
		setWhat(what); setWho(who);
	}
	
	// ������ Ÿ�̸� �Ѹ���, �� ���Ť�����������������������
	public Gaming(int what, int who, int time) {
		setWhat(what); setWho(who); setTime(time);
	}
	
	// �������� Ŭ���̾�Ʈ ���ΰ�ħ�䱸����������
	public Gaming(int what, ArrayList<Player> players) {
		setWhat(what); setPlayers(players);
	}
	
	// ù����� ���̵� ��Ī��������������
	public Gaming(int what, String id) {
		setWhat(what); setMsg(id);
	}
	

	
	
	
	
	// �������� Ŭ���̾�Ʈ�鿡�� �������� ������ �ѷ��� ��
	public static Gaming Gamestart(int thisplaynum) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_START); g.setThisplaynum(thisplaynum);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� �������� ������ �ѷ��� ��
	public static Gaming Turn(int who) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_WHOSTURN); g.setWho(who);
		return g;
	}
	// ������ ��������, �������� Ŭ���̾�Ʈ�鿡�� �ǵ��� �󸶷� �ٲ���� �ѷ��� ��
	public static Gaming Pandon(int pandon) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.PANDON); g.setPandon(pandon);
		return g;
	}
	// Ŭ���̾�Ʈ���� ó���� �ǵ� �����κ��� ������ �޾ƿ� ��
	public static Gaming MuchPandon() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.MUCHPANDON);
		return g;
	}
	// �������� ���ӽ��۽� ī�� ������ ��
	public static Gaming GiveCard(ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GETCARD); g.setPlayers(players);
		return g;
	}
	// �������� 2��° ī�� ������� �� ��
	public static Gaming Draw2Phase() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.DRAW2);
		return g;
	}
	// Ŭ���̾�Ʈ���� �����ϸ鼭 ���Ҷ�
	public static Gaming Call(int trash) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_CALL); g.setCard3(trash);
		return g;
	}
	// Ŭ���̾�Ʈ���� �����ϸ鼭 üũ�Ҷ�
	public static Gaming Check(int trash) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_CHECK); g.setCard3(trash);
		return g;
	}
	// Ŭ���̾�Ʈ���� �����ϸ鼭 �����Ҷ�
	public static Gaming Half(int trash) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_HALF); g.setCard3(trash);
		return g;
	}
	// Ŭ���̾�Ʈ���� ������ ���ϸ鼭 ���Ҷ�
	public static Gaming SetNCall(int set) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_CALL); g.setCardset(set);
		return g;
	}
	// Ŭ���̾�Ʈ���� ������ ���ϸ鼭 üũ�Ҷ�
	public static Gaming SetNCheck(int set) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_CHECK); g.setCardset(set);
		return g;
	}
	// Ŭ���̾�Ʈ���� ������ ���ϸ鼭 �����Ҷ�
	public static Gaming SetNHalf(int set) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_HALF); g.setCardset(set);
		return g;
	}
	// �������� ���ӽ��۽� ī�� ���ڸ���ġ�ϰ� �䱸
		public static Gaming ResetCard() {
			Gaming g = new Gaming();
			g.setWhat(Gaming.RESETCARDS);
			return g;
		}
	// �������� Ŭ���̾�Ʈ�鿡�� ���� ������ ���Ž��� ��.
	public static Gaming GameInfo(String userid, int what, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(what); g.setUserid(userid); g.setPlayers(players);
		return g;
	}
	// Ŭ���̾�Ʈ���� �Ϲ� ä�� ����
	public static Gaming Chat(String userid, String msg, String date) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT); g.setUserid(userid); g.setMsg(msg); g.setDate(date);
		return g;
	}
	// Ŭ���̾�Ʈ���� �ڱ� �г��� ���� ��û
	public static Gaming NickChange(String userid, String toNick) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_NICKCHANGE); g.setUserid(userid); g.setMsg(toNick);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� ������ �г��Ӱ� �ð��� �ѷ���
	public static Gaming NickChange(String userid, String toNick, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_NICKCHANGE); g.setUserid(userid); g.setMsg(toNick); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� ���� �¸��ߴ��� ä�þ˸����ε� �ѷ���
	public static Gaming Message_Win(String userid, String date) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_WIN); g.setUserid(userid); g.setDate(date);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� �����Ѵٰ� ä�þ˸����ε� �ѷ���
	public static Gaming Message_Re(String date) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_RE); g.setDate(date);
		return g;
	}
	// Ŭ���̾�Ʈ���� �ڽ��� ������ �˷��޶�� ��Ź��.
	public static Gaming ImIn(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_JOIN); g.setUserid(userid);
		return g;
	}
	// Ŭ���̾�Ʈ���� �ڽ��� ������ �˷��޶�� ��Ź��.
	public static Gaming ImOut(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_LEAVE); g.setUserid(userid);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� �������� ������ �˸��� �����ϱ� ���� �ѷ���.
	public static Gaming HesIn(String userid, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_JOIN); g.setUserid(userid); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� �������� ������ �˸��� �����ϱ� ���� �ѷ���.
	public static Gaming HesOut(String userid, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_LEAVE); g.setUserid(userid); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// Ŭ���̾�Ʈ���� ������ ������ �������� ���� ��û��
	public static Gaming callBan(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.BAN); g.setUserid(userid);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� ���� ������ߴ��� �˷���
	public static Gaming HesBanned(String userid, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.BAN); g.setUserid(userid); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// Ŭ���̾�Ʈ�� ����䱸�� �ްԵ�.
	public static Gaming UrBanned() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.IMBANNED);
		return g;
	}
	// Ŭ���̾�Ʈ���� ������ �ڽ��� ID �˷��� ��
	public static Gaming SendID(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.IDMATCH); g.setUserid(userid);
		return g;
	}
	// Ŭ���̾�Ʈ���� ������ �÷��̾� ��ü ������ �䱸�� ��
	public static Gaming PlzRefresh() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.REFRESH);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�� �÷��̾� ���� ������ �ѷ��� ��
	public static Gaming Refresh(ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.REFRESH); g.setPlayers(players);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� Ÿ�̸� �Ѹ� ��
	public static Gaming Timer(int who, int time, int turn) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_TIMER); g.setWho(who); g.setTime(time); g.setTurn(turn);
		return g;
	}
	// ����-Ŭ���̾�Ʈ ����� �ǵ�/�����ּұݾ� ����
	public static Gaming MoneyRefresh(int moneythisgame, int minforbet) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.MONEY_REFRESH); g.setMoneythisgame(moneythisgame); g.setMinforbet(minforbet);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� �������� ������ ���Ž�����
	public static Gaming TurnRefresh(int turn) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.TURN_REFRESH); g.setWho(turn);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� ���� ���ȭ�� ���� ����
	public static Gaming Gameresult(ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_RESULT); g.setPlayers(players);
		return g;
	}
	// �������� Ŭ���̾�Ʈ�鿡�� ���� ������ ��ưȰ��ȭ�ǰ� ����
	public static Gaming ButtonOk() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.BUTTON_OK);
		return g;
	}
	
	
	

//	public Gaming(Socket socket, int what, String msg) {
//		setSocket(socket); setWhat(what); setMsg(msg);
//	}
	// �Ϲ� ä�� ���⤷������������������������������������������������
	public Gaming(String userid, int what, String msg, String date) {
		setUserid(userid); setWhat(what); setMsg(msg); setDate(date);
	}
	// ä�� �˸�(����, ����) - ������ ��������������������������������
	public Gaming(String userid, int what, String date, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setDate(date); setPlayers(players);
	}
	// ä�� �˸�(�к���) - ������ dddddddddddddddddddddddddddddddddddddddd
	public Gaming(String userid, int what, String toNick, String date, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setMsg(toNick); setDate(date); setPlayers(players);
	}
	// ä�� �˸�(�к���) - Ŭ��� ��������������������������������������������
	public Gaming(String userid, int what, String toNick) {
		setUserid(userid); setWhat(what); setMsg(toNick);
	}
	// �Ϲ� ���� ���, ������ddddddddddddddddddddddddd
	public Gaming(String userid, int what, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setPlayers(players);
	}
	// �Ϲ� ���� ���, ä�� �˸�(����, ����) - Ŭ��뤷����������������������
	public Gaming(String userid, int what) {
		setUserid(userid); setWhat(what);
	}
	
	
	// ī�� �������� ����������������������������
	public Gaming(int what, int card1, int card2, int card3) {
		setWhat(what); this.setCard1(card1); this.setCard2(card2); this.setCard3(card3);
	}
	
	public int getPandon() {
		return pandon;
	}
	public void setPandon(int pandon) {
		this.pandon = pandon;
	}
	
	public int getWhat() {
		return what;
	}
	public void setWhat(int what) {
		this.what = what;
	}
	public int getWho() {
		return who;
	}
	public void setWho(int who) {
		this.who = who;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getCard1() {
		return card1;
	}

	public void setCard1(int card1) {
		this.card1 = card1;
	}

	public int getCard2() {
		return card2;
	}

	public void setCard2(int card2) {
		this.card2 = card2;
	}

	public int getCard3() {
		return card3;
	}

	public void setCard3(int card3) {
		this.card3 = card3;
	}
	
	public int getCardset() {
		return cardset;
	}

	public void setCardset(int cardset) {
		this.cardset = cardset;
	}
	public int getThisplaynum() {
		return thisplaynum;
	}
	public void setThisplaynum(int thisplaynum) {
		this.thisplaynum=thisplaynum;
	}
	
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}

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

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

}
