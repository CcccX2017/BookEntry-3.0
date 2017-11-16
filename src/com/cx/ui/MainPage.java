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
	JLabel livemoney = new JLabel("未设置起始、结束日期及生活费");
	JLabel balance = new JLabel();
	JButton jb1 = new JButton("记账");
	JButton jb2 = new JButton("账单");
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JButton shouqi = new JButton("收起");
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
	Font f = new Font("黑体",Font.PLAIN,20);
	Font f1 = new Font("宋体",Font.PLAIN,15);
	Font f2 = new Font("宋体",Font.PLAIN,13);
	JPopupMenu pop = new JPopupMenu();
	public MainPage(){
		setTitle("记账本"+ banben +"-主界面");
		setSize(930, 750);
		centerWindow(); //窗口居中
		init(); //添加组件
		addjp1();//jp1添加组件
		addjp2();//jp2添加组件
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				int result = JOptionPane.showConfirmDialog(null,"是否退出记账本?","退出",JOptionPane.YES_NO_OPTION);
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
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
					livemoney.setText(StartDate + " 至 " + EndDate + "的生活费：" + liveMoney + "元  共 " + day + " 天");
					RefreshTable();
					int rowCount = tbl.getRowCount();
					Rectangle rect = tbl.getCellRect(rowCount-1, 0, true);
					tbl.scrollRectToVisible(rect);
				} else{
					livemoney.setText("未设置起始、结束日期及生活费");
				}
			}
		}}
	public void addjp1(){
		jp1.setLayout(null);
		JButton save = new JButton("保存");
		getRootPane().setDefaultButton(save);
		save.setFont(f);
		save.setBounds(360, 350, 80, 50);
		save.setBackground(Color.LIGHT_GRAY);
		JLabel jb1 = new JLabel("账目日期:");
		jb1.setFont(f1);
		jb1.setBounds(350, 150, 70, 20);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		JTextField date = new JTextField(sdf.format(new Date()));
		date.setBounds(425, 150, 120, 20);
		date.setEditable(false);
		date.setBackground(Color.white);
		DateChooser dateChooser1 = DateChooser.getInstance("yyyy-MM-dd");
		dateChooser1.register(date);
		JLabel jb2 = new JLabel("账目类型:");
		jb2.setFont(f1);
		jb2.setBounds(350, 200, 70, 20);
		String []s = {"早餐","午餐","晚餐","聚餐","零食","余额宝"};
		JComboBox type	= new JComboBox(s);
		type.setFont(f2);
		type.setEditable(true);
		type.setBounds(425, 200, 120, 20);
		
		JLabel jb4 = new JLabel("收支情况:");jb4.setFont(f1);
		jb4.setBounds(350, 250, 70, 20);
		Balanceofpayments.addItem("收入");
		Balanceofpayments.addItem("支出");
		Balanceofpayments.setSelectedIndex(1);
		Balanceofpayments.setFont(f2);
		Balanceofpayments.setBounds(425, 250, 120, 20);
		
		type.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (type.getSelectedItem().toString().trim().equals("余额宝")) {
					Balanceofpayments.setSelectedIndex(0);
				}else {
					Balanceofpayments.setSelectedIndex(1);
				}
			}
		});
		
		JLabel jb6 = new JLabel("账目金额:");jb6.setFont(f1);
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
					JOptionPane.showMessageDialog(null, "账目类型不能为空！","添加失败",0);
					type.setSelectedIndex(0);
					return;
				}
				if(Money == null || Money.equals("")){
					JOptionPane.showMessageDialog(null, "账目金额不能为空！","添加失败",0);
					return;
				}
				billBean.setMoney(Float.parseFloat(Money));
				String sql = "insert into bill values(?,?,?,?,?)";
				int result = ControlBill.insertBill(sql, billBean);
				if(result == 1){
					JOptionPane.showMessageDialog(null, "添加成功","添加成功",1);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					date.setText(sdf.format(new Date()));
					type.setSelectedIndex(0);
					Balanceofpayments.setSelectedIndex(1);
					money.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "添加失败,所加信息重复","添加失败",0);
				}
			}
		});

		jp1.add(date);jp1.add(jb1);
		jp1.add(jb2);jp1.add(type);
		jp1.add(jb4);jp1.add(Balanceofpayments);
		jp1.add(jb6);jp1.add(money);
		jp1.add(save);
		
		JButton reset = new JButton("重置");reset.setFont(f);reset.setBackground(Color.LIGHT_GRAY);
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
		total.setBounds(280, 440, 800, 20);total.setFont(new Font("宋体",Font.PLAIN,14));
		JButton setmoney = new JButton("设置金额");setmoney.setFont(f);setmoney.setBackground(Color.LIGHT_GRAY);
		setmoney.setForeground(color);
		setmoney.setBounds(70, 100, 160, 20);
		setmoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetMoneyDialog dlgSetMoney = new SetMoneyDialog(null, "设置金额",true);
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
					livemoney.setText(StartDate + " 至 " + EndDate + "的生活费：" + Livemoney + "元  共 " + day + " 天");
				}
			}
		});
		JButton updatemoney = new JButton("修改金额");updatemoney.setFont(f);updatemoney.setBackground(Color.LIGHT_GRAY);
		updatemoney.setBounds(70, 160, 160, 20);updatemoney.setForeground(color);
		updatemoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateMoneyDialog dlgUpdateMoney = new UpdateMoneyDialog(null, "修改金额",true);
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
					livemoney.setText(StartDate + " 至 " + EndDate + "的生活费：" + Livemoney + "元  共 " + day + " 天");
					char[] c1 = StartDate.toCharArray();
					for(int i = 0;i<c1.length;i++){
						if(c1[i] == '年'){
							c1[i] = '-';
						} else if(c1[i] == '月'){
							c1[i] = '-';
						} else if(c1[i] == '日'){
							c1[i] = ' ';
						}
					}
					StartDate = new String(c1).trim();
					char[] c2 = EndDate.toCharArray();
					for(int i = 0;i<c2.length;i++){
						if(c2[i] == '年'){
							c2[i] = '-';
						} else if(c2[i] == '月'){
							c2[i] = '-';
						} else if(c2[i] == '日'){
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
		JButton update = new JButton("修改记录");update.setFont(f);update.setBackground(Color.LIGHT_GRAY);
		update.setBounds(70, 220, 160, 20);update.setForeground(color);
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selIndex = tbl.getSelectedRow();
				if(selIndex <0 || selIndex >= tbl.getRowCount()){
					JOptionPane.showMessageDialog(null,"请选择要修改的行");
					return;
				}
				riqi = tbl.getValueAt(selIndex, 0).toString().trim();
				shouzhi = tbl.getValueAt(selIndex, 1).toString().trim();
				leixing = tbl.getValueAt(selIndex, 2).toString().trim();
				jine = tbl.getValueAt(selIndex, 3).toString().trim();
				UpdateBillDialog dlgUpdate = new UpdateBillDialog(null, "修改账单信息",true);
				dlgUpdate.setSize(210,220);
				dlgUpdate.setVisible(true);
				int result = UpdateBillDialog.result;
				if(result != 0){
					RefreshTable();
				}
			}
		});
		jp2.add(update);
		JButton delete = new JButton("删除记录");delete.setFont(f);delete.setBackground(Color.LIGHT_GRAY);
		delete.setBounds(70, 280, 160, 20);delete.setForeground(color);
		jp2.add(delete);
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selIndex = tbl.getSelectedRow();
				if(selIndex <0 || selIndex >= tbl.getRowCount()){
					JOptionPane.showMessageDialog(null,"请选择要删除的行");
					return;
				}
				String Bdate = tbl.getValueAt(selIndex, 0).toString().trim();
				String type = tbl.getValueAt(selIndex, 2).toString().trim();
				String shouzhi = tbl.getValueAt(selIndex, 1).toString().trim();
				int result = JOptionPane.showConfirmDialog(null,"是否删除日期为:"+Bdate+"类型为:"+type+"的"+shouzhi+"账单信息?"
						,"删除",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					sql = "delete from bill where username='"+username+"'and Bdate='"+Bdate+"'"
							+ "and shouzhi='"+shouzhi+"' and type='"+type+"'";
					int rs = ControlBill.deleteBill(sql);
					if(rs != 0){
						JOptionPane.showMessageDialog(null, "删除账单信息成功","删除成功",1);
						RefreshTable();
					} else
						JOptionPane.showMessageDialog(null, "删除账单信息失败","删除失败",0);
				}
			}
		});
		shouqi.setBounds(300, 490, 58, 20);shouqi.setFont(new Font("宋体", Font.PLAIN, 12));
		shouqi.setVisible(false);
		jp2.add(shouqi);
		JButton select = new JButton("查    	询");select.setFont(f);select.setBackground(Color.LIGHT_GRAY);
		select.setBounds(70, 340, 160, 20);select.setForeground(color);
		select.setToolTipText("区间及全部信息查询，如需查询生活费情况，请点击表格上方标签");
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SelectBillDialog sbd = new SelectBillDialog(null, "账单信息查询", true);
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
									livemoney.setText(date1 + "的账单信息查询结果如下：" );
								else{
									livemoney.setText(date1 + " 至 " + date2 + "的账单信息查询结果如下：" );
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
							livemoney.setText("全部账单信息查询结果如下：");
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
									livemoney.setText(date1 + "的账单信息查询结果如下：" );
								else{
									livemoney.setText(date1 + " 至 " + date2 + "的账单信息查询结果如下：" );
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
		JButton statistics = new JButton("统    	计");statistics.setFont(f);statistics.setBackground(Color.LIGHT_GRAY);
		statistics.setBounds(70, 400, 160, 20);statistics.setForeground(color);
		day.setBounds(280, 460, 800, 20);day.setFont(new Font("宋体",Font.PLAIN,14));
		jp2.add(total);jp2.add(day);
		statistics.setToolTipText("统计本月收支情况及余额");
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
						if(rs.getString(3).equals("支出")){
							zhichu += rs.getFloat(5);
						} else if(rs.getString(3).equals("收入")){
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
				total.setText("本月总计： 支出：" + zhichu + "元" + "  收入：" + shouru +"元   余额：" + balance + "元  每天平均使用：" + use_avg + "元");
				if(unuse_money <= 0){
					day.setText("");
					shouqi.setBounds(300, 470, 58, 20);
				}else{
					day.setText("距离结算日还剩：" + unuse_money +" 天  每天平均可用：" + unuse_avg +"元");
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
				livemoney.setToolTipText("查询生活费,如需按区间查询或查询全部信息,请使用查询功能");
				livemoney.setForeground(Color.RED);
			}
			public void mouseExited(MouseEvent e) {
				livemoney.setForeground(Color.black);
			}
			public void mouseClicked(MouseEvent e){
				SelectLiveMoneyDialog slmd = new SelectLiveMoneyDialog(null, "查看生活费", true);
				slmd.setSize(400, 350);
				slmd.setVisible(true);
				String startDate = SelectLiveMoneyDialog.startDate;
				String endDate = SelectLiveMoneyDialog.endDate;
				String livemoney = SelectLiveMoneyDialog.livemoney;
				float zhichu = 0;
				float shouru = 0;
				float balance = 0;
				if(startDate != null && endDate != null && livemoney != null){
					MainPage.this.livemoney.setText(startDate + " 至 " + endDate + "的生活费：" + livemoney + "元");
					try {
						char[] c1 = startDate.toCharArray();
						for(int i = 0;i<c1.length;i++){
							if(c1[i] == '年'){
								c1[i] = '-';
							} else if(c1[i] == '月'){
								c1[i] = '-';
							} else if(c1[i] == '日'){
								c1[i] = ' ';
							}
						}
						startDate = new String(c1).trim();
						char[] c2 = endDate.toCharArray();
						for(int i = 0;i<c2.length;i++){
							if(c2[i] == '年'){
								c2[i] = '-';
							} else if(c2[i] == '月'){
								c2[i] = '-';
							} else if(c2[i] == '日'){
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
							if(rs.getString(3).equals("支出"))
								zhichu += rs.getFloat(5);
							else if(rs.getString(3).equals("收入"))
								shouru += rs.getFloat(5);
						}
						zhichu = (float)(Math.round(zhichu*100))/100;
						shouru = (float)(Math.round(shouru*100))/100;
						balance = (float)(Math.round((Float.parseFloat(liveMoney) - zhichu + shouru)*100))/100 ;
						day.setText("");
						shouqi.setBounds(300, 470, 58, 20);
						total.setText("本月总计： 支出：" + zhichu + "元" + "  收入：" + shouru +"元   余额：" + balance + "元");
						shouqi.setVisible(true);
					} catch (SQLException e2) {
						// TODO: handle exception
					}
					
				}
			}
		});
		
		JMenuItem del = new JMenuItem("删除(D)",KeyEvent.VK_D);
		pop.add(del);
		jp2.add(statistics);
		sp.setBorder(BorderFactory.createEtchedBorder());
        sp.setBounds(new Rectangle(280, 80, 600, 360));
        sp.getViewport().add(tbl);
        tbl.setFont(new Font("宋体",Font.PLAIN,14));
        tbl.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e){
        		if(e.getButton() == MouseEvent.BUTTON3){
        			int focusedRowIndex = tbl.rowAtPoint(e.getPoint());
    				if(focusedRowIndex == -1){
    					return;
    				}
    				//将表格所选项设为当前右键点击的行
    				tbl.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
    				//弹出菜单
        			pop.show(tbl, e.getX(), e.getY());
        		}
        		if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){
        			int selIndex = tbl.getSelectedRow();
    				if(selIndex <0 || selIndex >= tbl.getRowCount()){
    					JOptionPane.showMessageDialog(null,"请选择要修改的行");
    					return;
    				}
    				riqi = tbl.getValueAt(selIndex, 0).toString().trim();
    				shouzhi = tbl.getValueAt(selIndex, 1).toString().trim();
    				leixing = tbl.getValueAt(selIndex, 2).toString().trim();
    				jine = tbl.getValueAt(selIndex, 3).toString().trim();
    				UpdateBillDialog dlgUpdate = new UpdateBillDialog(null, "修改账单信息",true);
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
					JOptionPane.showMessageDialog(null,"请选择要删除的行");
					return;
				}
				String Bdate = tbl.getValueAt(selIndex, 0).toString().trim();
				String type = tbl.getValueAt(selIndex, 2).toString().trim();
				String shouzhi = tbl.getValueAt(selIndex, 1).toString().trim();
				int result = JOptionPane.showConfirmDialog(null,"是否删除该条账单信息"
						,"删除",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					sql = "delete from bill where username='"+username+"'and Bdate='"+Bdate+"'"
							+ "and shouzhi='"+shouzhi+"' and type='"+type+"'";
					int rs = ControlBill.deleteBill(sql);
					if(rs != 0){
						JOptionPane.showMessageDialog(null, "删除账单信息成功","删除成功",1);
						RefreshTable();
					} else
						JOptionPane.showMessageDialog(null, "删除账单信息失败","删除失败",0);
				}
			}
		});
        
        jp2.add(sp);
        jp2.add(setmoney);
        livemoney.setBounds(280, 55, 800, 30);livemoney.setFont(new Font("宋体",Font.PLAIN,14));
        jp2.add(livemoney);
        ((DefaultTableCellRenderer)tbl.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        m.addColumn("账目日期");
    	m.addColumn("收支情况");
    	m.addColumn("账目类型");
    	m.addColumn("账目金额");
	    m.setRowCount(0);
		m.setColumnCount(4);
		makeFace(tbl);
		jp2.add(balance);
	}
	
	public void RefreshTable(){
		char[] c1 = StartDate.toCharArray();
		for(int i = 0;i<c1.length;i++){
			if(c1[i] == '年'){
				c1[i] = '-';
			} else if(c1[i] == '月'){
				c1[i] = '-';
			} else if(c1[i] == '日'){
				c1[i] = ' ';
			}
		}
		StartDate = new String(c1).trim();
		char[] c2 = EndDate.toCharArray();
		for(int i = 0;i<c2.length;i++){
			if(c2[i] == '年'){
				c2[i] = '-';
			} else if(c2[i] == '月'){
				c2[i] = '-';
			} else if(c2[i] == '日'){
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
					if(rs.getString(3).equals("支出")){
						zhichu += rs.getFloat(5);
					} else if(rs.getString(3).equals("收入")){
						shouru += rs.getFloat(5);
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			zhichu = (float)(Math.round(zhichu*100))/100;
			day.setText("");
			shouqi.setBounds(300, 470, 58, 20);
			total.setText("总计： 支出：" + zhichu + "元" + "  收入：" + shouru + "元 ");
			shouqi.setVisible(true);
		}
	}
	
	public String changeDate(String date){
		char[] c1 = new char[date.length() + 1];
		char[] c3 = date.toCharArray();
		for(int i = 0;i <= c3.length;i++){
			if(i == c3.length)
				c1[i] = '日';
			for(int j = c3.length - 1;j >= i;j--){
				if(c3[i] == '-' && c3[j] == '-'){
					c3[i] = '年';
					c3[j] = '月';
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
