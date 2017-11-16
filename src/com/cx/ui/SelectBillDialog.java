package com.cx.ui;

import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.stream.events.StartDocument;

import com.cx.date.DateChooser;

public class SelectBillDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static String result = null;
	int StartDay = 0;
	int StartMonth = 0;
	JButton select = new JButton("查 询");
	JButton esc = new JButton("取 消");
	JTextField startDate = new JTextField();
	JTextField endDate = new JTextField();
	JRadioButton radio1 = new JRadioButton("区间查询");
	JRadioButton radio2 = new JRadioButton("全部账单信息查询");
	JRadioButton radio3 = new JRadioButton("当天账单信息查询");
	ButtonGroup bg = new ButtonGroup();
	Font f = new Font("宋体", Font.BOLD, 14);
	Font f1 = new Font("宋体", Font.PLAIN, 14);
	Font f3 = new Font("黑体",Font.PLAIN,20);
	static String date1 = null;
	static String date2 = null;
	public SelectBillDialog(JFrame owner, String title,boolean model) {
		super(owner,title,model);
		setLocation(800,400);
		setResizable(false);
		init();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		bg.add(radio1);bg.add(radio2);bg.add(radio3);
		radio1.setBounds(160, 20, 100, 20);radio1.setFont(f);
		c.add(radio1);
		radio2.setBounds(10, 150, 150, 20);radio2.setFont(f);
		c.add(radio2);
		radio3.setBounds(10, 20, 150, 20);radio3.setFont(f);radio3.setSelected(true);
		c.add(radio3);
		JLabel start = new JLabel("起始日期：");start.setFont(f1);
		start.setBounds(70, 60, 80, 20);
		c.add(start);
		startDate.setBounds(140, 60,80, 20);startDate.setFont(f1);startDate.setEditable(false);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		startDate.setText(sdf.format(new Date()));
		DateChooser dateChooser1 = DateChooser.getInstance("yyyy-MM-dd");
		dateChooser1.register(startDate);
		c.add(startDate);
		JLabel end = new JLabel("结束日期：");end.setFont(f1);
		end.setBounds(70, 100, 80, 20);
		c.add(end);
		endDate.setBounds(140, 100, 80, 20);endDate.setFont(f1);endDate.setEnabled(false);
		endDate.setText(sdf.format(new Date()));
		DateChooser dateChooser2 = DateChooser.getInstance("yyyy-MM-dd");
		dateChooser2.register(endDate);
		c.add(endDate);
		select.setBounds(new Rectangle(40, 190, 100, 50));select.setFont(f3);
		c.add(select);
		esc.setBounds(new Rectangle(160, 190, 100, 50));esc.setFont(f3);
		c.add(esc);
		
		esc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		radio1.addActionListener(new radioListener());
		radio2.addActionListener(new radioListener());
		radio3.addActionListener(new radioListener());
		
		startDate.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				Calendar c = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				if(radio1.isSelected()){
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
				} else if(radio3.isSelected()){
					endDate.setText(startDate.getText());
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
			}	
		});
		
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(radio1.isSelected()){
					result = "radio1";
					date1 = startDate.getText();
					date2 = endDate.getText();
					dispose();
				}else if(radio2.isSelected()){
					result = "radio2";
					dispose();
				}else if(radio3.isSelected()){
					result = "radio3";
					date1 = startDate.getText();
					date2 = endDate.getText();
					dispose();
				}
			}
		});
	}
	class radioListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(radio1.isSelected()){
				startDate.setEnabled(true);
				endDate.setEnabled(true);
				Calendar c = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				startDate.setText(sdf.format(date));
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
			}else if(radio2.isSelected()){
				startDate.setEnabled(false);
				endDate.setEnabled(false);
			}else if(radio3.isSelected()){
				startDate.setEnabled(true);
				endDate.setEnabled(false);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				startDate.setText(sdf.format(new Date()));
				endDate.setText(sdf.format(new Date()));
			}
		}
	}
}
