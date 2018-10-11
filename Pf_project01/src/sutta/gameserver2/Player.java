package sutta.gameserver2;

import java.io.Serializable;
import java.net.Socket;

import sutta.useall.User;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * order: ���ӹ� �� �÷��̾� ����(0-3)
	 * socket: �÷��̾ ���� ����
	 * betbool: 0_�ʱ�ȭ / 1_���� / 2_�� / 3_���� / 4_üũ
	 * ready: 0_���� / 1_�غ� / 2_�̹̰�����
	 * gameresult: 0_�ȶ�� / 1_�¸� / 2_����
	 */
	private int order;
	private transient Socket socket;
//	private String userid;
//	private String nickname;
//	private int money=8888;
	private int thisbet;
	private int card1;
	private int card2;
	private int card3;
	private boolean receiveok1=false;
	private boolean receiveok2=false;
	private boolean receiveban=false;
	private boolean cancheck=false;
	private int trash;
	private int[] cardset;
	private int betbool;
	private int ready;
	private int gameresult;
	private User user;
	private transient UserThread uth;
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Socket getSocket() {
		return socket;
	}
//	public void setSocket(Socket socket) {
//		this.socket = socket;
//	}
//	public String getNickname() {
//		return nickname;
//	}
//	public void setNickname(String nickname) {
//		this.nickname = nickname;
//	}
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
	public int[] getCardset() {
		return cardset;
	}
	public void setCardset(int[] cardset) {
		this.cardset = cardset;
	}
	public int getSelCardset() {
		return cardset[3];
	}
	public boolean isReceiveok1() { return receiveok1; }
	public void setReceiveok1(boolean receiveok1) { this.receiveok1 = receiveok1; }
	public boolean isReceiveok2() { return receiveok2; }
	public void setReceiveok2(boolean receiveok2) { this.receiveok2 = receiveok2; }
	public boolean isCancheck() {
		return cancheck;
	}
	public void setCancheck(boolean cancheck) {
		this.cancheck = cancheck;
	}

	public void SelectSet(int num) {
		int[] tmp = getCardset();
		switch(num) {
			case 1: {
				tmp[3]=tmp[0];
				break;
			}
			case 2: {
				tmp[3]=tmp[1];
				break;
			}
			case 3: {
				tmp[3]=tmp[2];
				break;
			}
		}
		setCardset(tmp);
		return;
	}
	public int getBetbool() {
		return betbool;
	}
	public void setBetbool(int betbool) {
		this.betbool = betbool;
	}
	public Player(int order, Socket socket, UserThread uth, User user) {
		this.order=order; this.socket=socket; this.setUth(uth); setReady(0);
//		setUserid(user.getId());
//		setNickname(user.getNickname());
//		setMoney(user.getMoney());
	}
//	public Socket getSocket() {
//		return socket;
//	}
//	public String getNickname() {
//		return nickname;
//	}
	public int getReady() {
		return ready;
	}
	public void setReady(int ready) {
		this.ready = ready;
	}
	public UserThread getUth() {
		return uth;
	}
	public void setUth(UserThread uth) {
		this.uth = uth;
	}
//	public String getUserid() {
//		return userid;
//	}
//	public void setUserid(String userid) {
//		this.userid = userid;
//	}
//	public int getMoney() {
//		return money;
//	}
//	public void setMoney(int money) {
//		this.money = money;
//	}
	public int getGameresult() {
		return gameresult;
	}
	public void setGameresult(int gameresult) {
		this.gameresult = gameresult;
	}
	public int getTrash() {
		return trash;
	}
	public void setTrash(int trash) {
		this.trash = trash;
	}
	public int getThisbet() {
		return thisbet;
	}
	public void setThisbet(int thisbet) {
		this.thisbet = thisbet;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isReceiveban() {
		return receiveban;
	}
	public void setReceiveban(boolean receiveban) {
		this.receiveban = receiveban;
	}
}
