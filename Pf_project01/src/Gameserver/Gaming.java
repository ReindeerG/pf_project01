package Gameserver;

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
	public static final int GAME_DIE = 10;
	public static final int GAME_CALL = 11;
	public static final int GAME_HALF = 12;
	public static final int GAME_CHECK = 13;
	public static final int MONEY_REFRESH = 89;
	public static final int TURN_REFRESH = 87;
	public static final int GETCARD = 500;
	public static final int GAME_WHOSTURN = 600;
	public static final int GAME_TIMER = 606;
	public static final int CHAT = 330;
	public static final int CHAT_JOIN = 331;
	public static final int CHAT_LEAVE = 332;
	public static final int CHAT_NICKCHANGE = 333;
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
	
	private int time;
	
	private int moneythisgame;
	private int minforbet;
	
	
	public Gaming() {}
	
	// 適虞戚情闘拭辞 辞獄拭 嬢恐 誤敬 推短獣.
	public Gaming(int what) {
		setWhat(what);
	}
	
	
	// 辞獄遂 陥製渡 刊浦走 姿険凶ddddddddd
	public Gaming(int what, int who) {
		setWhat(what); setWho(who);
	}
	
	// 辞獄遂 展戚袴 姿険凶, 儀 飴重しししししししししししし
	public Gaming(int what, int who, int time) {
		setWhat(what); setWho(who); setTime(time);
	}
	
	// 辞獄拭辞 適虞戚情闘 歯稽壱徴推姥ししししし
	public Gaming(int what, ArrayList<Player> players) {
		setWhat(what); setPlayers(players);
	}
	
	// 湛尻衣獣 焼戚巨 古暢凶しししししし
	public Gaming(int what, String id) {
		setWhat(what); setMsg(id);
	}
	

	
	
	
	
	
	// 辞獄拭辞 適虞戚情闘級拭惟 陥製渡戚 刊浦走 姿形匝 凶
	public static Gaming Turn(int who) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_WHOSTURN); g.setWho(who);
		return g;
	}
	// 辞獄拭辞 惟績獣拙獣 朝球 蟹寛匝 凶
	public static Gaming GiveCard(int card1, int card2, int card3) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GETCARD); g.setCard1(card1); g.setCard2(card2); g.setCard3(card3);
		return g;
	}
	// 辞獄拭辞 適虞戚情闘級拭惟 惟績 舛左研 飴重獣佃 捜.
	public static Gaming GameInfo(String userid, int what, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(what); g.setUserid(userid); g.setPlayers(players);
		return g;
	}
	// 適虞戚情闘拭辞 析鋼 辰特 勺窒
	public static Gaming Chat(String userid, String msg, String date) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT); g.setUserid(userid); g.setMsg(msg); g.setDate(date);
		return g;
	}
	// 適虞戚情闘拭辞 切奄 莞革績 痕井 推短
	public static Gaming NickChange(String userid, String toNick) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_NICKCHANGE); g.setUserid(userid); g.setMsg(toNick);
		return g;
	}
	// 辞獄拭辞 適虞戚情闘級拭惟 痕井廃 莞革績引 獣娃聖 姿形捜
	public static Gaming NickChange(String userid, String toNick, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_NICKCHANGE); g.setUserid(userid); g.setMsg(toNick); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// 適虞戚情闘拭辞 切重税 脊舌聖 硝形含虞壱 採店敗.
	public static Gaming ImIn(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_JOIN); g.setUserid(userid);
		return g;
	}
	// 適虞戚情闘拭辞 切重税 盗舌聖 硝形含虞壱 採店敗.
	public static Gaming ImOut(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_LEAVE); g.setUserid(userid);
		return g;
	}
	// 辞獄拭辞 適虞戚情闘級拭惟 刊浦亜税 脊舌聖 硝軒壱 飴重馬奄 是背 姿形捜.
	public static Gaming HesIn(String userid, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_JOIN); g.setUserid(userid); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// 辞獄拭辞 適虞戚情闘級拭惟 刊浦亜税 盗舌聖 硝軒壱 飴重馬奄 是背 姿形捜.
	public static Gaming HesOut(String userid, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_LEAVE); g.setUserid(userid); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// 適虞戚情闘拭辞 辞獄稽 切重税 ID 硝形匝 凶
	public static Gaming SendID(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.IDMATCH); g.setUserid(userid);
		return g;
	}
	// 適虞戚情闘拭辞 辞獄稽 巴傾戚嬢 穿端 飴重聖 推姥拝 凶
	public static Gaming PlzRefresh() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.REFRESH);
		return g;
	}
	// 辞獄拭辞 適虞戚情闘拭 巴傾戚嬢 飴重 舛左研 姿形匝 凶
	public static Gaming Refresh(ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.REFRESH); g.setPlayers(players);
		return g;
	}
	// 辞獄拭辞 適虞戚情闘級拭惟 展戚袴 姿険 凶
	public static Gaming Timer(int who, int time) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_TIMER); g.setWho(who); g.setTime(time);
		return g;
	}
	// 辞獄-適虞戚情闘 丞号狽 毒儀/今特置社榎衝 飴重
	public static Gaming MoneyRefresh(int moneythisgame, int minforbet) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.MONEY_REFRESH); g.setMoneythisgame(moneythisgame); g.setMinforbet(minforbet);
		return g;
	}
	public static Gaming TurnRefresh(int turn) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.TURN_REFRESH); g.setWho(turn);
		return g;
	}
	
	
	
	
	

//	public Gaming(Socket socket, int what, String msg) {
//		setSocket(socket); setWhat(what); setMsg(msg);
//	}
	// 析鋼 辰特 勺窒ししししししししししししししししししししししししし
	public Gaming(String userid, int what, String msg, String date) {
		setUserid(userid); setWhat(what); setMsg(msg); setDate(date);
	}
	// 辰特 硝顕(脊舌, 盗舌) - 辞獄遂 しししししししししししししししし
	public Gaming(String userid, int what, String date, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setDate(date); setPlayers(players);
	}
	// 辰特 硝顕(莞痕井) - 辞獄遂 dddddddddddddddddddddddddddddddddddddddd
	public Gaming(String userid, int what, String toNick, String date, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setMsg(toNick); setDate(date); setPlayers(players);
	}
	// 辰特 硝顕(莞痕井) - 適虞遂 しししししししししししししししししししししし
	public Gaming(String userid, int what, String toNick) {
		setUserid(userid); setWhat(what); setMsg(toNick);
	}
	// 析鋼 惟績 誤敬, 辞獄遂ddddddddddddddddddddddddd
	public Gaming(String userid, int what, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setPlayers(players);
	}
	// 析鋼 惟績 誤敬, 辰特 硝顕(脊舌, 盗舌) - 適虞遂しししししししししししし
	public Gaming(String userid, int what) {
		setUserid(userid); setWhat(what);
	}
	
	
	// 朝球 蟹寛閤聖 凶ししししししししししししし
	public Gaming(int what, int card1, int card2, int card3) {
		setWhat(what); this.setCard1(card1); this.setCard2(card2); this.setCard3(card3);
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

}
