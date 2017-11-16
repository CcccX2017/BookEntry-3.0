package com.cx.ui;



import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.Calendar;
import java.util.Date;

import com.cx.date.DateChooser;
import com.cx.util.ControlLiveMoney;
import com.cx.bean.LiveMoneyBean;
import com.cx.bean.GetSetBean;

public class SetMoneyDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static boolean flag = false;
	int StartDay = 0;
	int StartMonth = 0;
	Connection conn = null;
	ResultSet rs = null;
	Statement st = null;
	String sql = null;
	int []year = {0};
	int []month = {0};
	JTextField livemoney = new JTextField(10);
	JTextField startDate,endDate;
	JButton set = new JButton("设置");
	JButton esc = new JButton("取消");
	Font f = new Font("宋体",Font.PLAIN,15);
	Font f1 = new Font("宋体",Font.PLAIN,13);
	LiveMoneyBean liveMoneyBean = new LiveMoneyBean();
	static String startdate = null;
	static String enddate = null;
	static float LiveMoney = 0;
	public SetMoneyDialog(){}
	public SetMoneyDialog (JFrame owner, String title,boolean model) {
		super(owner,title,model);
		init();
		setLocation(700,500);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		getRootPane().setDefaultButton(set);
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date();
		startDate = new JTextField(sdf.format(date));
		startDate.setFont(f1);
		startDate.setEditable(false);
		Calendar calendar = Calendar.getInstance();
		try {
			date = sdf.parse(startDate.getText());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		StartDay = calendar.get(Calendar.DAY_OF_MONTH);
		StartMonth = calendar.get(Calendar.MONTH) + 1;
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		endDate = new JTextField(sdf.format(calendar.getTime()));
		endDate.setFont(f1);
		endDate.setEditable(false);
		DateChooser dateChooser1 = DateChooser.getInstance("yyyy年MM月dd日");
		dateChooser1.register(startDate);
		DateChooser dateChooser2 = DateChooser.getInstance("yyyy年MM月dd日");
		dateChooser2.register(endDate);
		startDate.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				Calendar c = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
				Date date = new Date();
				try {
					date = sdf.parse(startDate.getText());
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				c.setTime(date);
				c.add(Calendar.MONTH, 1);
				c.add(Calendar.DATE, -1);
				endDate.setText(sdf.format(c.getTime()));
				c.setTime(date);
				StartMonth = c.get(Calendar.MONTH) + 1;
				StartDay = c.get(Calendar.DAY_OF_MONTH);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
			}	
		});
		JLabel start = new JLabel("起始:");
		start.setFont(f);
		JLabel end = new JLabel("结束:");
		end.setFont(f);
		JLabel money = new JLabel("金额:");
		money.setFont(f);
		livemoney.setFont(f1);
		
		set.setFont(f);
		esc.setFont(f);
		set.addActionListener(new MyActionListener());
		esc.addActionListener(new MyActionListener());
		
		c.add(start);c.add(startDate);
		c.add(end);c.add(endDate);
		c.add(money);c.add(livemoney);
		c.add(set);c.add(esc);
	}
	class MyActionListener implements ActionListener{
		public MyActionListener(){}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == set){
				if(livemoney.getText() != null){
					liveMoneyBean.setUsername(Login.username);
					liveMoneyBean.setStartDate(startDate.getText());
					liveMoneyBean.setEndDate(endDate.getText());
					liveMoneyBean.setLivemoney(Float.parseFloat(livemoney.getText()));
					liveMoneyBean.setStartMonth(StartMonth);
					liveMoneyBean.setStartDay(StartDay);
					startdate = startDate.getText();enddate = endDate.getText();
					LiveMoney = Float.parseFloat(livemoney.getText());
					String sql = "insert into livemoney values(?,?,?,?,?,?)";
					int result = ControlLiveMoney.insertLiveMoney(sql, liveMoneyBean);
					if(result == 1){
						flag = true;
						JOptionPane.showMessageDialog(null, "设置成功","设置成功",1);
						dispose();
					} else{
						flag = false;
						JOptionPane.showMessageDialog(null, "设置失败,已设置当前月，如需更改请使用修改金额","设置失败",0);
						dispose();
					}
				}
				else{ 
					flag = false;
					dispose();
				}
			}
			else if(e.getSource() == esc){
				dispose();
				flag = false;
			}
		}
	}
}
