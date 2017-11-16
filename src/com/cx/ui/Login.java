package com.cx.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.cx.util.LoginCheck;

public class Login extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String username = null;
	static String password = null;
	Font f = new Font("宋体", Font.PLAIN, 14);
	public Login(){
		this.setTitle("记账本-用户登录");
		this.setUndecorated(true);
		this.setSize(430,330);
		Color color = new Color(249,247,236);
		this.setBackground(color);
		centerWindow(); //窗口居中
		addimage();
		init();
		setRemove();
		this.setVisible(true);
	}
	private void addimage() {
		ImageIcon img = new ImageIcon("image/login.png");//这是背景图片
		JLabel imgLabel = new JLabel(img);//将背景图放在标签里。
		getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));//注意这里是关键，将背景标签添加到jfram的LayeredPane面板里。
		imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());//设置背景标签的位置
		Container cp=getContentPane();
		cp.setLayout(new BorderLayout());
		((JPanel)cp).setOpaque(false); //将内容面板设为透明，这样LayeredPane面板中的背景才能显示出来。
		Toolkit tk = getToolkit();
		Image icon = tk.getImage("image/jizhang.jpg");
		setIconImage(icon);
	}
	public void centerWindow(){
		Toolkit tk = getToolkit();
		Dimension dm = tk.getScreenSize();
		setLocation((int)(dm.getWidth()-getWidth())/2,(int)(dm.getHeight()-getHeight())/2);
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		JLabel jl1 = new JLabel("×");
		jl1.setForeground(Color.WHITE);
		jl1.setBounds(410,-15, 55, 55);
		jl1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				System.exit(0);
			}
			public void mouseExited(MouseEvent e) {
				jl1.setForeground(Color.WHITE);
			}
			public void mouseEntered(MouseEvent e) {
				jl1.setForeground(Color.RED);
				jl1.setToolTipText("关闭");
			}
		});
		JLabel jl2 = new JLabel("—");
		jl2.setForeground(Color.WHITE);
		jl2.setBounds(380,-15, 55, 55);
		jl2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setExtendedState(JFrame.ICONIFIED);
			}
			public void mouseExited(MouseEvent e) {
				jl2.setForeground(Color.WHITE);
			}
			public void mouseEntered(MouseEvent e) {
				jl2.setForeground(Color.RED);
				jl2.setToolTipText("最小化");
			}
		});
		JLabel jl3 = new JLabel();
		jl3.setIcon(new ImageIcon("image/1.jpg"));
		jl3.setBounds(60, 188, 75, 75);
		JTextField user = new JTextField();
		user.setText("chenxin666");
		user.setFont(f);
		user.setBounds(new Rectangle(150, 188, 170, 25));
		user.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(144,144,144)));
		user.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				user.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,161,255)));
			}
			public void mouseExited(MouseEvent e) {
				user.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(144,144,144)));
			}
		});
		JLabel jl4 = new JLabel("注册账号");
		Color color1 = new Color(0, 161, 255);
		Color color2 = new Color(120, 180, 255);
		jl4.setForeground(color1);
		jl4.setBounds(330, 188, 50, 20);
		jl4.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				jl4.setForeground(color2);
			}
			public void mouseExited(MouseEvent e) {
				jl4.setForeground(color1);
			}
			public void mouseClicked(MouseEvent e) {
				RegisterDialog registerdialog = new RegisterDialog();
			}
		});
		JTextField pwd = new JPasswordField();
		pwd.setBounds(new Rectangle(150, 212, 170, 25));
		pwd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(144,144,144)));
		pwd.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				pwd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,161,255)));
			}
			public void mouseExited(MouseEvent e) {
				pwd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(144,144,144)));
			}
		});
		Document document = user.getDocument();
		document.addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				pwd.setText("");
			}
			public void insertUpdate(DocumentEvent e) {
			}
			public void changedUpdate(DocumentEvent e) {
			}
		});
		JLabel jl5 = new JLabel("找回密码");
		jl5.setForeground(color1);
		jl5.setBounds(330, 212, 50, 20);
		jl5.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				jl5.setForeground(color2);
			}
			public void mouseExited(MouseEvent e) {
				jl5.setForeground(color1);
			}
		});
		JCheckBox rememberPwd = new JCheckBox("记住密码");
		rememberPwd.setBounds(150, 245, 75, 15);
		rememberPwd.setForeground(new Color(144, 144, 144));
		JCheckBox automaticLogin = new JCheckBox("自动登录");
		automaticLogin.setBounds(250, 245, 75, 15);
		automaticLogin.setForeground(new Color(144, 144, 144));
		automaticLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(automaticLogin.isSelected()){
					rememberPwd.setSelected(true);
				}
			}
		});
		JButton login = new JButton();
		login.setBounds(new Rectangle( 150, 270, 170, 30));
		login.setIcon(new ImageIcon("image/2.png"));
		login.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				login.setIcon(new ImageIcon("image/3.png"));
			}
			public void mouseExited(MouseEvent e) {
				login.setIcon(new ImageIcon("image/2.png"));
			}
		});
		login.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				username = user.getText();
				password = pwd.getText();
				if(username.equals("")){
					JOptionPane.showMessageDialog(null, "请输入用户名");
					return;
				} 
				if(password.equals("")){
					JOptionPane.showMessageDialog(null, "请输入密码");
					return;
				}
				int result = LoginCheck.logincheck(username, password);
				if(result == 1){
					setVisible(false);
					new MainPage().setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "用户名或密码错误","登录失败",0);
				}
			}
		});
		pwd.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(!pwd.getText().equals("")){
					pwd.selectAll();
				}
			}
		});
		getRootPane().setDefaultButton(login);
		c.add(jl1);
		c.add(jl2);
		c.add(jl3);
		c.add(user);add(jl4);
		c.add(pwd);c.add(jl5);
		c.add(rememberPwd);c.add(automaticLogin);
		c.add(login);
	}
	public void setRemove(){//Swing实现鼠标拖动移动窗体
		Point origin = new Point();
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				origin.x = e.getX();
				origin.y = e.getY();
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point p = getLocation();
				int dragX = e.getX();
				int dragY = e.getY();
				int frameNowX = p.x + dragX - origin.x;
				int frameNowY = p.y + dragY - origin.y;
				setLocation(frameNowX,frameNowY);
			}
		});
	}
	public static void main(String[] args) {
//		java.awt.EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");	
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				} catch (InstantiationException e) {
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				} catch (UnsupportedLookAndFeelException e) {
//					e.printStackTrace();
//				}
//				new Login();
//			}
//		});
		try {
	    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }
	    catch (Exception e) {
	    	System.err.println("Couldn't use the system look and feel:"+e);
	    }
		new Login();
	}
}
