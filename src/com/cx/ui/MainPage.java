package com.cx.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Renderer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.cx.bean.BillBean;
import com.cx.bean.GetSetBean;
import com.cx.bean.LiveMoneyBean;
import com.cx.date.DateChooser;
import com.cx.util.ControlBill;
import com.cx.util.ControlLiveMoney;

import java.util.Calendar;
import java.util.Date;

public class MainPage extends JFrame{
	private static final String banben = "3.0";
	String sql = null;
	String StartDate = null;
	String EndDate = null;
	String liveMoney = null;
	String username = Login.username;
	static String riqi = null;
	static String shouzhi = null;
	static String leixing = null;
	static String jine = null;
	JLabel used = new JLabel();
	JLabel livemoney = new JLabel("δ������ʼ���������ڼ������");
	JLabel balance = new JLabel();
	JButton jb1 = new JButton("����");
	JButton jb2 = new JButton("�˵�");
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JButton shouqi = new JButton("����");
	JLabel total = new JLabel();
	JLabel day = new JLabel();
	JComboBox Balanceofpayments	= new JComboBox();
	JScrollPane sp = new JScrollPane();
	JTable tbl = new JTable(new DefaultTableModel()){
		public boolean isCellEditable(int row,int column){
			return false;
		}
	};
	DefaultTableModel m = (DefaultTableModel)this.tbl.getModel();
	JTextField startDate = new JTextField();
	JTextField endDate = new JTextField();
	Font f = new Font("����",Font.PLAIN,20);
	Font f1 = new Font("����",Font.PLAIN,15);
	Font f2 = new Font("����",Font.PLAIN,13);
	JPopupMenu pop = new JPopupMenu();
	public MainPage(){
		setTitle("���˱�"+ banben +"-������");
		setSize(930, 750);
		centerWindow(); //���ھ���
		init(); //������
		addjp1();//jp1������
		addjp2();//jp2������
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				int result = JOptionPane.showConfirmDialog(null,"�Ƿ��˳����˱�?","�˳�",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		Toolkit tk = getToolkit();
		Image icon = tk.getImage("image/jizhang.jpg");
		setIconImage(icon);
		repaint();
	}
	public void centerWindow(){
			Toolkit tk = getToolkit();
			Dimension dm = tk.getScreenSize();
			setLocation((int)(dm.getWidth()-getWidth())/2,(int)(dm.getHeight()-getHeight())/2);
	}
	public static void makeFace(JTable table){
    	try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer(){
				public Component getTableCellRendererComponent(JTable table,Object value,
						boolean isSelected,boolean hasFocus,int row,int column){
					if(row % 2 == 0)
						setBackground(Color.WHITE);
					else if(row % 2 == 1)
						//setBackground(new Color(206,231,255));
						setBackground(new Color(190, 190, 190));
					return super.getTableCellRendererComponent(table, value,
							isSelected, hasFocus, row, column);
				}
			};
			for(int i =0;i < table.getColumnCount();i++)
				table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		jb1.setBounds(330, 50, 80, 50);
		jb1.setFont(f);
		jb1.setBackground(Color.LIGHT_GRAY);
		jb2.setBounds(480, 50, 80, 50);
		jb2.setFont(f);
		jb2.setBackground(Color.LIGHT_GRAY);
		jp1.setBackground(Color.white);
		jp1.setBounds(0, 120, 930, 600);
		jp2.setBackground(Color.white);
		jp2.setBounds(0, 120, 930, 600);
		jp2.setVisible(false);
		c.add(jb1);
		c.add(jb2);
		c.add(jp1);
		c.add(jp2);
		jb1.addActionListener(new ButtonActionListener());
		jb2.addActionListener(new ButtonActionListener());
	}
	
	public static int day(String StartDay , String EndDay) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(StartDay));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(EndDay));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1)/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days)) + 1;
	}
	class ButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == jb1){
				jp1.setVisible(true);
				jp2.setVisible(false);
			}
			if(e.getSource() == jb2){
				jp1.setVisible(false);
				jp2.setVisible(true);
				Calendar c = Calendar.getInstance();
				int month = c.get(Calendar.MONTH) + 1;
				String sql1 = "select * from livemoney where username='"+username+"'and StartMonth="+month+"";
				LiveMoneyBean liveMoneyBean1 = ControlLiveMoney.selectLiveMoney(sql1);
				int startDay = liveMoneyBean1.getStartDay();
				if(c.get(Calendar.DAY_OF_MONTH) < startDay || startDay == 0){
					month = month - 1;
				}
				sql = "select * from livemoney where username='"+username+"'and StartMonth="+month+"";
				LiveMoneyBean liveMoneyBean = ControlLiveMoney.selectLiveMoney(sql);
				if(liveMoneyBean.getStartDate() != null && liveMoneyBean.getEndDate() != null){
					StartDate = liveMoneyBean.getStartDate();
					EndDate = liveMoneyBean.getEndDate();
					liveMoney = String.valueOf(liveMoneyBean.getLivemoney());
					int day = 0;
					try {
						day = day(StartDate, EndDate);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					livemoney.setText(StartDate + " �� " + EndDate + "������ѣ�" + liveMoney + "Ԫ  �� " + day + " ��");
					RefreshTable();
					int rowCount = tbl.getRowCount();
					Rectangle rect = tbl.getCellRect(rowCount-1, 0, true);
					tbl.scrollRectToVisible(rect);
				} else{
					livemoney.setText("δ������ʼ���������ڼ������");
				}
			}
		}}
	public void addjp1(){
		jp1.setLayout(null);
		JButton save = new JButton("����");
		getRootPane().setDefaultButton(save);
		save.setFont(f);
		save.setBounds(360, 350, 80, 50);
		save.setBackground(Color.LIGHT_GRAY);
		JLabel jb1 = new JLabel("��Ŀ����:");
		jb1.setFont(f1);
		jb1.setBounds(350, 150, 70, 20);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		JTextField date = new JTextField(sdf.format(new Date()));
		date.setBounds(425, 150, 120, 20);
		date.setEditable(false);
		date.setBackground(Color.white);
		DateChooser dateChooser1 = DateChooser.getInstance("yyyy-MM-dd");
		dateChooser1.register(date);
		JLabel jb2 = new JLabel("��Ŀ����:");
		jb2.setFont(f1);
		jb2.setBounds(350, 200, 70, 20);
		String []s = {"���","���","���","�۲�","��ʳ","��"};
		JComboBox type	= new JComboBox(s);
		type.setFont(f2);
		type.setEditable(true);
		type.setBounds(425, 200, 120, 20);
		
		JLabel jb4 = new JLabel("��֧���:");jb4.setFont(f1);
		jb4.setBounds(350, 250, 70, 20);
		Balanceofpayments.addItem("����");
		Balanceofpayments.addItem("֧��");
		Balanceofpayments.setSelectedIndex(1);
		Balanceofpayments.setFont(f2);
		Balanceofpayments.setBounds(425, 250, 120, 20);
		
		type.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (type.getSelectedItem().toString().trim().equals("��")) {
					Balanceofpayments.setSelectedIndex(0);
				}else {
					Balanceofpayments.setSelectedIndex(1);
				}
			}
		});
		
		JLabel jb6 = new JLabel("��Ŀ���:");jb6.setFont(f1);
		jb6.setBounds(350, 300, 70, 20);
		JTextField money = new JTextField();money.setBounds(425, 300, 120, 20);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BillBean billBean = new BillBean();
				billBean.setUsername("chenxin666");
				billBean.setBdate(date.getText().trim());
				billBean.setShouzhi(Balanceofpayments.getSelectedItem().toString().trim());
				billBean.setType(type.getSelectedItem().toString().trim());
				String Money = money.getText().trim();
				String Type = type.getSelectedItem().toString().trim();
				if(Type == null || Type.equals("")){
					JOptionPane.showMessageDialog(null, "��Ŀ���Ͳ���Ϊ�գ�","���ʧ��",0);
					type.setSelectedIndex(0);
					return;
				}
				if(Money == null || Money.equals("")){
					JOptionPane.showMessageDialog(null, "��Ŀ����Ϊ�գ�","���ʧ��",0);
					return;
				}
				billBean.setMoney(Float.parseFloat(Money));
				String sql = "insert into bill values(?,?,?,?,?)";
				int result = ControlBill.insertBill(sql, billBean);
				if(result == 1){
					JOptionPane.showMessageDialog(null, "��ӳɹ�","��ӳɹ�",1);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					date.setText(sdf.format(new Date()));
					type.setSelectedIndex(0);
					Balanceofpayments.setSelectedIndex(1);
					money.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "���ʧ��,������Ϣ�ظ�","���ʧ��",0);
				}
			}
		});

		jp1.add(date);jp1.add(jb1);
		jp1.add(jb2);jp1.add(type);
		jp1.add(jb4);jp1.add(Balanceofpayments);
		jp1.add(jb6);jp1.add(money);
		jp1.add(save);
		
		JButton reset = new JButton("����");reset.setFont(f);reset.setBackground(Color.LIGHT_GRAY);
		reset.setBounds(450, 350, 80, 50);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				date.setText(sdf.format(new Date()));
				type.setSelectedIndex(0);
				Balanceofpayments.setSelectedIndex(1);
				money.setText("");
			}
		});
		jp1.add(reset);
	}
	public void addjp2(){
		jp2.setLayout(null);
		Color color = new Color(25,25,112);
		total.setBounds(280, 440, 800, 20);total.setFont(new Font("����",Font.PLAIN,14));
		JButton setmoney = new JButton("���ý��");setmoney.setFont(f);setmoney.setBackground(Color.LIGHT_GRAY);
		setmoney.setForeground(color);
		setmoney.setBounds(70, 100, 160, 20);
		setmoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetMoneyDialog dlgSetMoney = new SetMoneyDialog(null, "���ý��",true);
				dlgSetMoney.setSize(570,70);
				dlgSetMoney.setVisible(true);
				boolean flag = SetMoneyDialog.flag;
				if(flag){
					StartDate = SetMoneyDialog.startdate;
					EndDate = SetMoneyDialog.enddate;
					float Livemoney = SetMoneyDialog.LiveMoney;
					int day = 0;
					try {
						day = day(StartDate, EndDate);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					livemoney.setText(StartDate + " �� " + EndDate + "������ѣ�" + Livemoney + "Ԫ  �� " + day + " ��");
				}
			}
		});
		JButton updatemoney = new JButton("�޸Ľ��");updatemoney.setFont(f);updatemoney.setBackground(Color.LIGHT_GRAY);
		updatemoney.setBounds(70, 160, 160, 20);updatemoney.setForeground(color);
		updatemoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateMoneyDialog dlgUpdateMoney = new UpdateMoneyDialog(null, "�޸Ľ��",true);
				dlgUpdateMoney.setSize(570,350);
				dlgUpdateMoney.setVisible(true);
				boolean flag = UpdateMoneyDialog.flag;
				if(flag){
					String StartDate = UpdateMoneyDialog.startdate;
					String EndDate = UpdateMoneyDialog.enddate;
					float Livemoney = UpdateMoneyDialog.LiveMoney;
					int day = 0;
					try {
						day = day(StartDate, EndDate);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					livemoney.setText(StartDate + " �� " + EndDate + "������ѣ�" + Livemoney + "Ԫ  �� " + day + " ��");
					char[] c1 = StartDate.toCharArray();
					for(int i = 0;i<c1.length;i++){
						if(c1[i] == '��'){
							c1[i] = '-';
						} else if(c1[i] == '��'){
							c1[i] = '-';
						} else if(c1[i] == '��'){
							c1[i] = ' ';
						}
					}
					StartDate = new String(c1).trim();
					char[] c2 = EndDate.toCharArray();
					for(int i = 0;i<c2.length;i++){
						if(c2[i] == '��'){
							c2[i] = '-';
						} else if(c2[i] == '��'){
							c2[i] = '-';
						} else if(c2[i] == '��'){
							c2[i] = ' ';
						}
					}
					EndDate = new String(c2).trim();
					sql = "select * from bill where username = '"+username+"'"
							+ " and Bdate >= '"+StartDate+"' and Bdate <= '"+EndDate+"'";
					try {
						ResultSet rs = ControlBill.selectBill(sql);
						m.setRowCount(0);
						while(rs.next()){
							Vector<Comparable> v = new Vector<Comparable>();
							v.add(rs.getString(2));
							v.add(rs.getString(3));
							v.add(rs.getString(4));
							v.add(rs.getFloat(5));
							m.addRow(v);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		jp2.add(updatemoney);
		JButton update = new JButton("�޸ļ�¼");update.setFont(f);update.setBackground(Color.LIGHT_GRAY);
		update.setBounds(70, 220, 160, 20);update.setForeground(color);
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selIndex = tbl.getSelectedRow();
				if(selIndex <0 || selIndex >= tbl.getRowCount()){
					JOptionPane.showMessageDialog(null,"��ѡ��Ҫ�޸ĵ���");
					return;
				}
				riqi = tbl.getValueAt(selIndex, 0).toString().trim();
				shouzhi = tbl.getValueAt(selIndex, 1).toString().trim();
				leixing = tbl.getValueAt(selIndex, 2).toString().trim();
				jine = tbl.getValueAt(selIndex, 3).toString().trim();
				UpdateBillDialog dlgUpdate = new UpdateBillDialog(null, "�޸��˵���Ϣ",true);
				dlgUpdate.setSize(210,220);
				dlgUpdate.setVisible(true);
				int result = UpdateBillDialog.result;
				if(result != 0){
					RefreshTable();
				}
			}
		});
		jp2.add(update);
		JButton delete = new JButton("ɾ����¼");delete.setFont(f);delete.setBackground(Color.LIGHT_GRAY);
		delete.setBounds(70, 280, 160, 20);delete.setForeground(color);
		jp2.add(delete);
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selIndex = tbl.getSelectedRow();
				if(selIndex <0 || selIndex >= tbl.getRowCount()){
					JOptionPane.showMessageDialog(null,"��ѡ��Ҫɾ������");
					return;
				}
				String Bdate = tbl.getValueAt(selIndex, 0).toString().trim();
				String type = tbl.getValueAt(selIndex, 2).toString().trim();
				String shouzhi = tbl.getValueAt(selIndex, 1).toString().trim();
				int result = JOptionPane.showConfirmDialog(null,"�Ƿ�ɾ������Ϊ:"+Bdate+"����Ϊ:"+type+"��"+shouzhi+"�˵���Ϣ?"
						,"ɾ��",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					sql = "delete from bill where username='"+username+"'and Bdate='"+Bdate+"'"
							+ "and shouzhi='"+shouzhi+"' and type='"+type+"'";
					int rs = ControlBill.deleteBill(sql);
					if(rs != 0){
						JOptionPane.showMessageDialog(null, "ɾ���˵���Ϣ�ɹ�","ɾ���ɹ�",1);
						RefreshTable();
					} else
						JOptionPane.showMessageDialog(null, "ɾ���˵���Ϣʧ��","ɾ��ʧ��",0);
				}
			}
		});
		shouqi.setBounds(300, 490, 58, 20);shouqi.setFont(new Font("����", Font.PLAIN, 12));
		shouqi.setVisible(false);
		jp2.add(shouqi);
		JButton select = new JButton("��    	ѯ");select.setFont(f);select.setBackground(Color.LIGHT_GRAY);
		select.setBounds(70, 340, 160, 20);select.setForeground(color);
		select.setToolTipText("���估ȫ����Ϣ��ѯ�������ѯ������������������Ϸ���ǩ");
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SelectBillDialog sbd = new SelectBillDialog(null, "�˵���Ϣ��ѯ", true);
				sbd.setSize(300,300);
				sbd.setVisible(true);
				String result = SelectBillDialog.result;
				if(result != null){
					if(result.equals("radio1")){
						String date1 = SelectBillDialog.date1;
						String date2 = SelectBillDialog.date2;
						if(date1 != null && date2 != null){
							String sql = "select * from bill where username='"+username+"'"
									+ "and Bdate >= '"+date1+"' and Bdate <= '"+date2+"'";
							try {
								ResultSet rs = ControlBill.selectBill(sql);
								m.setRowCount(0);
								while(rs.next()){
									Vector<Comparable> v = new Vector<Comparable>();
									v.add(rs.getString(2));
									v.add(rs.getString(3));
									v.add(rs.getString(4));
									v.add(rs.getFloat(5));
									m.addRow(v);
								}
								date1 = changeDate(date1);date2 = changeDate(date2);
								if(date1.equals(date2))
									livemoney.setText(date1 + "���˵���Ϣ��ѯ������£�" );
								else{
									livemoney.setText(date1 + " �� " + date2 + "���˵���Ϣ��ѯ������£�" );
									livemoney.setSize(400,30);
								}
								settotal();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					} else if(result.equals("radio2")){
						String sql = "select * from bill where username='"+username+"'";
						try {
							ResultSet rs = ControlBill.selectBill(sql);
							m.setRowCount(0);
							while(rs.next()){
								Vector<Comparable> v = new Vector<Comparable>();
								v.add(rs.getString(2));
								v.add(rs.getString(3));
								v.add(rs.getString(4));
								v.add(rs.getFloat(5));
								m.addRow(v);
							}
							livemoney.setText("ȫ���˵���Ϣ��ѯ������£�");
							settotal();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					} else if(result.equals("radio3")){
						String date1 = SelectBillDialog.date1;
						String date2 = SelectBillDialog.date2;
						if(date1 != null && date2 != null){
							String sql = "select * from bill where username='"+username+"'"
									+ "and Bdate >= '"+date1+"' and Bdate <= '"+date2+"'";
							try {
								ResultSet rs = ControlBill.selectBill(sql);
								m.setRowCount(0);
								while(rs.next()){
									Vector<Comparable> v = new Vector<Comparable>();
									v.add(rs.getString(2));
									v.add(rs.getString(3));
									v.add(rs.getString(4));
									v.add(rs.getFloat(5));
									m.addRow(v);
								}
								date1 = changeDate(date1);date2 = changeDate(date2);
								if(date1.equals(date2))
									livemoney.setText(date1 + "���˵���Ϣ��ѯ������£�" );
								else{
									livemoney.setText(date1 + " �� " + date2 + "���˵���Ϣ��ѯ������£�" );
									livemoney.setSize(400,30);
								}
								settotal();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		jp2.add(select);
		JButton statistics = new JButton("ͳ    	��");statistics.setFont(f);statistics.setBackground(Color.LIGHT_GRAY);
		statistics.setBounds(70, 400, 160, 20);statistics.setForeground(color);
		day.setBounds(280, 460, 800, 20);day.setFont(new Font("����",Font.PLAIN,14));
		jp2.add(total);jp2.add(day);
		statistics.setToolTipText("ͳ�Ʊ�����֧��������");
		statistics.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				float zhichu = 0;
				float shouru = 0;
				float balance = 0;
				sql = "select * from bill where username = '"+username+"'"
						+ " and Bdate >= '"+StartDate+"' and Bdate <= '"+EndDate+"'";
				try {
					ResultSet rs = ControlBill.selectBill(sql);
					while(rs.next()){
						if(rs.getString(3).equals("֧��")){
							zhichu += rs.getFloat(5);
						} else if(rs.getString(3).equals("����")){
							shouru += rs.getFloat(5);
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				zhichu = (float)(Math.round(zhichu*100))/100;
				shouru = (float)(Math.round(shouru*100))/100;
				balance = (float)(Math.round((Float.parseFloat(liveMoney) - zhichu + shouru)*100))/100 ;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String today = sdf.format(new Date()).toString();
				int use_money = 0 , unuse_money = 0;
				try {
					unuse_money = daysBetween(EndDate, today);
					if(daysBetween(today, EndDate) > 0)
						use_money = daysBetween(EndDate, StartDate);
					else
						use_money = daysBetween(today, StartDate);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				float use_avg = (float)(Math.round((zhichu/use_money)*100))/100;
				float unuse_avg = (float)(Math.round(balance/unuse_money)*100)/100;
				total.setText("�����ܼƣ� ֧����" + zhichu + "Ԫ" + "  ���룺" + shouru +"Ԫ   ��" + balance + "Ԫ  ÿ��ƽ��ʹ�ã�" + use_avg + "Ԫ");
				if(unuse_money <= 0){
					day.setText("");
					shouqi.setBounds(300, 470, 58, 20);
				}else{
					day.setText("��������ջ�ʣ��" + unuse_money +" ��  ÿ��ƽ�����ã�" + unuse_avg +"Ԫ");
					shouqi.setBounds(300, 490, 58, 20);
				}
				shouqi.setVisible(true);
				jb2.doClick();
			}
		});
		
		shouqi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				total.setText("");
				day.setText("");
				shouqi.setVisible(false);
			}
		});
		
		livemoney.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				livemoney.setToolTipText("��ѯ�����,���谴�����ѯ���ѯȫ����Ϣ,��ʹ�ò�ѯ����");
				livemoney.setForeground(Color.RED);
			}
			public void mouseExited(MouseEvent e) {
				livemoney.setForeground(Color.black);
			}
			public void mouseClicked(MouseEvent e){
				SelectLiveMoneyDialog slmd = new SelectLiveMoneyDialog(null, "�鿴�����", true);
				slmd.setSize(400, 350);
				slmd.setVisible(true);
				String startDate = SelectLiveMoneyDialog.startDate;
				String endDate = SelectLiveMoneyDialog.endDate;
				String livemoney = SelectLiveMoneyDialog.livemoney;
				float zhichu = 0;
				float shouru = 0;
				float balance = 0;
				if(startDate != null && endDate != null && livemoney != null){
					MainPage.this.livemoney.setText(startDate + " �� " + endDate + "������ѣ�" + livemoney + "Ԫ");
					try {
						char[] c1 = startDate.toCharArray();
						for(int i = 0;i<c1.length;i++){
							if(c1[i] == '��'){
								c1[i] = '-';
							} else if(c1[i] == '��'){
								c1[i] = '-';
							} else if(c1[i] == '��'){
								c1[i] = ' ';
							}
						}
						startDate = new String(c1).trim();
						char[] c2 = endDate.toCharArray();
						for(int i = 0;i<c2.length;i++){
							if(c2[i] == '��'){
								c2[i] = '-';
							} else if(c2[i] == '��'){
								c2[i] = '-';
							} else if(c2[i] == '��'){
								c2[i] = ' ';
							}
						}
						endDate = new String(c2).trim();
						sql = "select * from bill where username = '"+username+"'"
								+ "and Bdate >= '"+startDate+"' and Bdate <= '"+endDate+"'";
						ResultSet rs = ControlBill.selectBill(sql);
						m.setRowCount(0);
						while(rs.next()){
							Vector<Comparable> v = new Vector<Comparable>();
							v.add(rs.getString(2));
							v.add(rs.getString(3));
							v.add(rs.getString(4));
							v.add(rs.getFloat(5));
							m.addRow(v);
							if(rs.getString(3).equals("֧��"))
								zhichu += rs.getFloat(5);
							else if(rs.getString(3).equals("����"))
								shouru += rs.getFloat(5);
						}
						zhichu = (float)(Math.round(zhichu*100))/100;
						shouru = (float)(Math.round(shouru*100))/100;
						balance = (float)(Math.round((Float.parseFloat(liveMoney) - zhichu + shouru)*100))/100 ;
						day.setText("");
						shouqi.setBounds(300, 470, 58, 20);
						total.setText("�����ܼƣ� ֧����" + zhichu + "Ԫ" + "  ���룺" + shouru +"Ԫ   ��" + balance + "Ԫ");
						shouqi.setVisible(true);
					} catch (SQLException e2) {
						// TODO: handle exception
					}
					
				}
			}
		});
		
		JMenuItem del = new JMenuItem("ɾ��(D)",KeyEvent.VK_D);
		pop.add(del);
		jp2.add(statistics);
		sp.setBorder(BorderFactory.createEtchedBorder());
        sp.setBounds(new Rectangle(280, 80, 600, 360));
        sp.getViewport().add(tbl);
        tbl.setFont(new Font("����",Font.PLAIN,14));
        tbl.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e){
        		if(e.getButton() == MouseEvent.BUTTON3){
        			int focusedRowIndex = tbl.rowAtPoint(e.getPoint());
    				if(focusedRowIndex == -1){
    					return;
    				}
    				//�������ѡ����Ϊ��ǰ�Ҽ��������
    				tbl.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
    				//�����˵�
        			pop.show(tbl, e.getX(), e.getY());
        		}
        		if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){
        			int selIndex = tbl.getSelectedRow();
    				if(selIndex <0 || selIndex >= tbl.getRowCount()){
    					JOptionPane.showMessageDialog(null,"��ѡ��Ҫ�޸ĵ���");
    					return;
    				}
    				riqi = tbl.getValueAt(selIndex, 0).toString().trim();
    				shouzhi = tbl.getValueAt(selIndex, 1).toString().trim();
    				leixing = tbl.getValueAt(selIndex, 2).toString().trim();
    				jine = tbl.getValueAt(selIndex, 3).toString().trim();
    				UpdateBillDialog dlgUpdate = new UpdateBillDialog(null, "�޸��˵���Ϣ",true);
    				dlgUpdate.setSize(210,220);
    				dlgUpdate.setVisible(true);
    				int result = UpdateBillDialog.result;
    				if(result != 0){
    					RefreshTable();
    				}
        		}
        	}
		});
        
        del.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selIndex = tbl.getSelectedRow();
				if(selIndex <0 || selIndex >= tbl.getRowCount()){
					JOptionPane.showMessageDialog(null,"��ѡ��Ҫɾ������");
					return;
				}
				String Bdate = tbl.getValueAt(selIndex, 0).toString().trim();
				String type = tbl.getValueAt(selIndex, 2).toString().trim();
				String shouzhi = tbl.getValueAt(selIndex, 1).toString().trim();
				int result = JOptionPane.showConfirmDialog(null,"�Ƿ�ɾ�������˵���Ϣ"
						,"ɾ��",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					sql = "delete from bill where username='"+username+"'and Bdate='"+Bdate+"'"
							+ "and shouzhi='"+shouzhi+"' and type='"+type+"'";
					int rs = ControlBill.deleteBill(sql);
					if(rs != 0){
						JOptionPane.showMessageDialog(null, "ɾ���˵���Ϣ�ɹ�","ɾ���ɹ�",1);
						RefreshTable();
					} else
						JOptionPane.showMessageDialog(null, "ɾ���˵���Ϣʧ��","ɾ��ʧ��",0);
				}
			}
		});
        
        jp2.add(sp);
        jp2.add(setmoney);
        livemoney.setBounds(280, 55, 800, 30);livemoney.setFont(new Font("����",Font.PLAIN,14));
        jp2.add(livemoney);
        ((DefaultTableCellRenderer)tbl.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        m.addColumn("��Ŀ����");
    	m.addColumn("��֧���");
    	m.addColumn("��Ŀ����");
    	m.addColumn("��Ŀ���");
	    m.setRowCount(0);
		m.setColumnCount(4);
		makeFace(tbl);
		jp2.add(balance);
	}
	
	public void RefreshTable(){
		char[] c1 = StartDate.toCharArray();
		for(int i = 0;i<c1.length;i++){
			if(c1[i] == '��'){
				c1[i] = '-';
			} else if(c1[i] == '��'){
				c1[i] = '-';
			} else if(c1[i] == '��'){
				c1[i] = ' ';
			}
		}
		StartDate = new String(c1).trim();
		char[] c2 = EndDate.toCharArray();
		for(int i = 0;i<c2.length;i++){
			if(c2[i] == '��'){
				c2[i] = '-';
			} else if(c2[i] == '��'){
				c2[i] = '-';
			} else if(c2[i] == '��'){
				c2[i] = ' ';
			}
		}
		EndDate = new String(c2).trim();
		sql = "select * from bill where username = '"+username+"'"
				+ " and Bdate >= '"+StartDate+"' and Bdate <= '"+EndDate+"'";
		try {
			ResultSet rs = ControlBill.selectBill(sql);
			m.setRowCount(0);
			while(rs.next()){
				Vector<Comparable> v = new Vector<Comparable>();
				v.add(rs.getString(2));
				v.add(rs.getString(3));
				v.add(rs.getString(4));
				v.add(rs.getFloat(5));
				m.addRow(v);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void settotal(){
		String result = SelectBillDialog.result;
		if(result != null){
			float zhichu = 0;
			float shouru = 0;
			if(result.equals("radio1")){
				String date1 = SelectBillDialog.date1;
				String date2 = SelectBillDialog.date2;
				sql = "select * from bill where username = '"+username+"'"
						+ " and Bdate >= '"+date1+"' and Bdate <= '"+date2+"'";
			} else if(result.equals("radio2")){
				sql = "select * from bill where username = '"+username+"'";
			} else if(result.equals("radio3")){
				String date1 = SelectBillDialog.date1;
				String date2 = SelectBillDialog.date2;
				sql = "select * from bill where username = '"+username+"'"
						+ " and Bdate >= '"+date1+"' and Bdate <= '"+date2+"'";
			}
			try {
				ResultSet rs = ControlBill.selectBill(sql);
				while(rs.next()){
					if(rs.getString(3).equals("֧��")){
						zhichu += rs.getFloat(5);
					} else if(rs.getString(3).equals("����")){
						shouru += rs.getFloat(5);
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			zhichu = (float)(Math.round(zhichu*100))/100;
			day.setText("");
			shouqi.setBounds(300, 470, 58, 20);
			total.setText("�ܼƣ� ֧����" + zhichu + "Ԫ" + "  ���룺" + shouru + "Ԫ ");
			shouqi.setVisible(true);
		}
	}
	
	public String changeDate(String date){
		char[] c1 = new char[date.length() + 1];
		char[] c3 = date.toCharArray();
		for(int i = 0;i <= c3.length;i++){
			if(i == c3.length)
				c1[i] = '��';
			for(int j = c3.length - 1;j >= i;j--){
				if(c3[i] == '-' && c3[j] == '-'){
					c3[i] = '��';
					c3[j] = '��';
				}
				c1[i] = c3[i];
			}
		}
		date = new String(c1);
		return date;
	}
	
	public static int daysBetween(String smdate , String today) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(today));
		long time2 = cal.getTimeInMillis();
		long between_days = (time1 - time2)/(1000*3600*24) + 1;
		return Integer.parseInt(String.valueOf(between_days));
	}
	
//	public static void main(String[] args) {
//		MainPage mpf = new MainPage();
//		mpf.setVisible(true);
//	}

}
