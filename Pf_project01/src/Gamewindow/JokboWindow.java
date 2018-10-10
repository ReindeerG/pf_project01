package Gamewindow;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class JokboWindow extends JFrame implements ComponentListener{
//	������Ʈ ��ġ�� ����
	Point mainFrameLocation = new Point(0, 0);
    Point mouseClickedLocation = new Point(0, 0);
    
    private Mainwindow mw;
	private Container con = this.getContentPane();
	private JPanel jp = new JPanel(new GridLayout(33, 1));
	private Font font = new Font("����", Font.PLAIN, 17);
	private Font font2 = new Font("�ü�", Font.BOLD, 31);
	private Font font3 = new Font("����", Font.BOLD, 17);
	private Color LabelFontColor = new Color(41, 214, 140);
	private Color BackGroundColor = new Color(0, 64, 0);
	
	private JLabel a1 = new JLabel(" [1]  3��8����"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a2 = new JLabel(" [2]  1��8����"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a3 = new JLabel(" [3]  1��3����"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	
	private JLabel a4 = new JLabel(" [4]  �嶯(10��) "){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a5 = new JLabel(" [5]  9��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a6 = new JLabel(" [6]  8��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a7 = new JLabel(" [7]  7��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a8 = new JLabel(" [8]  6��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a9 = new JLabel(" [9]  5��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a10 = new JLabel("[10] 4��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a11 = new JLabel("[11] 3��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a12 = new JLabel("[12] 2��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a13 = new JLabel("[13] 1��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};

	private JLabel a14 = new JLabel("[14] �˸�(1��2)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a15 = new JLabel("[15] ����(1��4)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a16 = new JLabel("[16] ����(9��1)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a17 = new JLabel("[17] ���(10��1)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a18 = new JLabel("[18] ���(10��4)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a19 = new JLabel("[19] ����(4��6)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	
	private JLabel a20 = new JLabel("[20] ����(9��)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a21 = new JLabel("[21] 8��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a22 = new JLabel("[22] 7��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a23 = new JLabel("[23] 6��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a24 = new JLabel("[24] 5��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a25 = new JLabel("[25] 4��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a26 = new JLabel("[26] 3��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a27 = new JLabel("[27] 2��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a28 = new JLabel("[28] 1��"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a29 = new JLabel("[29] ����(0��)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	
	private JLabel a30 = new JLabel("[30] 4��7������"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a31 = new JLabel("[31] 3��7������"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a32 = new JLabel("[32] ���ֱ�������"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        tip.setFont(font3);
	        return tip;
	    }
	};
	private JLabel a33 = new JLabel("[33] ����(9��4)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        tip.setFont(font3);
	        return tip;
	    }
	};
	private void TooltipColorFont(JToolTip tip) {
		tip.setBackground(Color.CYAN);
        tip.setForeground(Color.BLACK);
        tip.setFont(font3);
	}

	private void display() {
		JLabel[] a = new JLabel[] {a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,a32,a33};
		for(int i=0;i<a.length;i++) {
			a[i].setForeground(LabelFontColor);
		}
		con.add(jp);
		
		Border Border = BorderFactory.createTitledBorder("���� ����");
		jp.setBorder(Border);
		
		jp.setBackground(BackGroundColor);
		TitledBorder titledBorder = (TitledBorder)jp.getBorder(); 
		titledBorder.setTitleFont(font2);
	    titledBorder.setTitleColor(LabelFontColor);
	    
	    for(int i=0;i<a.length;i++) {
	    	jp.add(a[i]);
	    }
		
		a1.setToolTipText(" 3���� + 8������ ���� [���� ���� õ�Ϲ��� ����] ");
		a2.setToolTipText(" 1���� + 8���� ���� [�����翡�� ����] ");
		a3.setToolTipText(" 1���� + 3���� ���� [�����翡�� ����] ");
		
		a4.setToolTipText(" 10�� �� 2�� ���� [���� ���� �ְ� ����] ");
		a5.setToolTipText(" 9�� �� 2�� ���� [�����̿��� ����] ");
		a6.setToolTipText(" 8�� �� 2�� ���� [�����̿��� ����] ");
		a7.setToolTipText(" 7�� �� 2�� ���� [�����̿��� ����] ");
		a8.setToolTipText(" 6�� �� 2�� ���� [�����̿��� ����] ");
		a9.setToolTipText(" 5�� �� 2�� ���� [�����̿��� ����] ");
		a10.setToolTipText(" 4�� �� 2�� ���� [�����̿��� ����] ");
		a11.setToolTipText(" 3�� �� 2�� ���� [�����̿��� ����] ");
		a12.setToolTipText(" 2�� �� 2�� ���� [�����̿��� ����] ");
		a13.setToolTipText(" 1�� �� 2�� ���� [�����̿��� ����] ");
		
		a14.setToolTipText(" 1�� + 2�� ���� [�� �̸����� ���� ���� ����] ");
		a15.setToolTipText(" 1�� + 4�� ���� [�˸�(1��2)���� ����] ");
		a16.setToolTipText(" 9�� + 1�� ���� [����(1��4)���� ����] ");
		a17.setToolTipText(" 10�� + 1�� ���� [����(9��1)���� ����] ");
		a18.setToolTipText(" 10�� + 4�� ���� [���(10��1)���� ����] ");
		a19.setToolTipText(" 4�� + 6�� ���� [����(9��)���� ���� ����] ");
		
		a20.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 9 [�� �߿� ���� ���� ����] ");
		a21.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 8 ");
		a22.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 7 ");
		a23.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 6 ");
		a24.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 5 ");
		a25.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 4 ");
		a26.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 3 ");
		a27.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 2 ");
		a28.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 1 ");
		a29.setToolTipText(" �� ������ ���� ���� ���� �����ڸ� ������ 0 [���ٿ��� ���� ���� ����] ");
		
		a30.setToolTipText(" 4������ + 7������ ���� [1��8����, 1��3�������� �¸�] ");
		a31.setToolTipText(" 3������ + 7������ ���� [1��~9������ �¸�] ");
		a32.setToolTipText(" 9������ + 4������ ���� [9������ ����(�嶯�Ұ�)] ");
		a33.setToolTipText(" 9�� + 4���� ���� (���ֱ������� ��������) [�˸�(1��2)���� ����] ");
		
		for(int i=0;i<a.length;i++) {
			a[i].setFont(font);
		}
	}
	private void event() {
		KeyListener key = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				
//				ESC�� ������ ������â�� ����
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					mw.toemptyjw();
					dispose();
				}
			}
		};
		this.addKeyListener(key);
	}
	
	public JokboWindow(Mainwindow mw) {
		this.mw=mw;
		this.display();
		this.event();
		this.setSize(170, 660);
		this.setResizable(false);
//		this.setLocationByPlatform(true);
		this.setLocationRelativeTo(mw);
		this.setAlwaysOnTop(true);
		addMouseListener(new FrameMove_mouseAdapter(this));
        addMouseMotionListener(new FrameMove_mouseMotionAdapter(this));
 
        /* root frame */
        setUndecorated(true);
//        setLocationRelativeTo(null);
        addComponentListener(this);
	}
	
//	public static void main(String[] args) {
//		try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception e) {e.printStackTrace();}
//		JokboWindow mainFrame = new JokboWindow(null);
//		mainFrame.setVisible(true);
//	}

	public void componentHidden(ComponentEvent arg0) {
    }

    public void componentMoved(ComponentEvent arg0) {
    }
 
    public void componentResized(ComponentEvent arg0) {
    }
 
    public void componentShown(ComponentEvent arg0) {
    }
}	

class FrameMove_mouseAdapter extends MouseAdapter {
    private JokboWindow frame;
 
    FrameMove_mouseAdapter(JokboWindow mainFrame) {
        this.frame = mainFrame;
    }
 
    public void mousePressed(MouseEvent e) {
        frame.mouseClickedLocation.x = e.getX();
        frame.mouseClickedLocation.y = e.getY();
    }
}

class FrameMove_mouseMotionAdapter extends MouseMotionAdapter {
    private JokboWindow frame;
     
    FrameMove_mouseMotionAdapter(JokboWindow mainFrame) {
        this.frame = mainFrame;
    }
 
    public void mouseDragged(MouseEvent e) {
        frame.setLocation(e.getLocationOnScreen().x - frame.mouseClickedLocation.x,
                e.getLocationOnScreen().y - frame.mouseClickedLocation.y);
    }
}