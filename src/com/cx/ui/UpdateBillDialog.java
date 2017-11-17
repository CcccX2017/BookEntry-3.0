package com.cx.ui;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.cx.bean.BillBean;
import com.cx.date.DateChooser;
import com.cx.util.ControlBill;

public class UpdateBillDialog extends JDialog{
	
	Font f = new Font("����",Font.PLAIN,15);
	Font f1 = new Font("����",Font.PLAIN,13);
	static int result = 0;
	String username = Login.username;
	public UpdateBillDialog(JFrame owner, String title,boolean model){
		super(owner,title,model);
		setLocation(950,390);
		setResizable(false);
		init();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(new GridLayout(5, 0));
		
		JPanel jp1 = new JPanel();
		JLabel jb1 = new JLabel("��Ŀ����:");
		jb1.setFont(f);
		JTextField date = new JTextField(10);
		date.setText(MainPage.riqi);
		date.setBounds(425, 150, 120, 20);
		date.setEditable(false);
		date.setBackground(Color.white);
		DateChooser dateChooser = DateChooser.getInstance("yyyy-MM-dd");
		dateChooser.register(date);
		jp1.add(jb1);jp1.add(date);
		
		JPanel jp2 = new JPanel();
		JLabel jb2 = new JLabel("��Ŀ����:");
		jb2.setFont(f);
		String []s = {"���","���","���","�۲�","��ʳ"};
		JComboBox<Object> type	= new JComboBox<Object>(s);
		for(int i = 0;i < s.length;i++){
			if(MainPage.leixing.equals(s[i])){
				type.setSelectedItem(MainPage.leixing);
				break;
			}else if(i == s.length - 1){
				type.addItem(MainPage.leixing);
				type.setSelectedItem(MainPage.leixing);
				break;
			}
		}
		type.setFont(f1);
		type.setEditable(true);
		jp2.add(jb2);jp2.add(type);
		
		JPanel jp3 = new JPanel();
		JLabel jb4 = new JLabel("��֧���:");jb4.setFont(f);
		JRadioButton shouru = new JRadioButton("����");shouru.setFont(f1);
		JRadioButton zhichu = new JRadioButton("֧��");zhichu.setFont(f1);
		if(MainPage.shouzhi.equals("����")){
			shouru.setSelected(true);
		}else{
			zhichu.setSelected(true);
		}
		ButtonGroup bg = new ButtonGroup();bg.add(shouru);bg.add(zhichu);
		jp3.add(jb4);jp3.add(shouru);jp3.add(zhichu);
		
		JPanel jp4 = new JPanel();
		JLabel jb6 = new JLabel("��Ŀ���:");jb6.setFont(f);
		JTextField money = new JTextField(10);money.setText(MainPage.jine);
		money.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if(money.getText().equals("")){
					money.setText(MainPage.jine);
				}else{
					money.setText(money.getText());
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				money.setText("");
				
			}
		});
		jp4.add(jb6);jp4.add(money);
		
		JPanel jp5 = new JPanel();
		JButton update = new JButton("�޸�");update.setFont(f);
		JButton esc = new JButton("ȡ��");esc.setFont(f);
		
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BillBean billBean = new BillBean();
				billBean.setBdate(date.getText().trim());
				if(shouru.isSelected()){
					billBean.setShouzhi("����");
				}else{
					billBean.setShouzhi("֧��");
				}
				billBean.setType(type.getSelectedItem().toString().trim());
				billBean.setMoney(Float.parseFloat(money.getText().trim()));
				String riqi = MainPage.riqi;
				String shouzhi = MainPage.shouzhi;
				String leixing = MainPage.leixing;
				String sql = "update bill set Bdate=?,shouzhi=?,type=?,money=? where username = '"+username+"'"
						+ "and Bdate='"+riqi+"'and shouzhi='"+shouzhi+"'and type='"+leixing+"'";
				result = ControlBill.updateBill(sql, billBean);
				if(result != 0){
					JOptionPane.showMessageDialog(null, "�޸��˵���Ϣ�ɹ�","�޸ĳɹ�",1);
				}else
					JOptionPane.showMessageDialog(null, "�޸��˵���Ϣʧ��","�޸�ʧ��",0);
				dispose();
			}
		});
		
		esc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = 0;
				dispose();
			}
		});
		
		getRootPane().setDefaultButton(update);
		
		jp5.add(update);jp5.add(esc);
		
		jp1.setOpaque(false);jp2.setOpaque(false);
		jp3.setOpaque(false);jp4.setOpaque(false);
		jp5.setOpaque(false);
		
		c.add(jp1);c.add(jp2);
		c.add(jp3);c.add(jp4);
		c.add(jp5);
	}
}
