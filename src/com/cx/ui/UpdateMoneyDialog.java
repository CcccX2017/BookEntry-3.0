package com.cx.ui;



import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.cx.date.DateChooser;
import com.cx.util.ControlLiveMoney;
import com.cx.bean.LiveMoneyBean;
import com.cx.bean.GetSetBean;

public class UpdateMoneyDialog extends JDialog {
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
	JPopupMenu pop = null;
	JButton set = new JButton("修改");
	JButton esc = new JButton("取消");
	Font f = new Font("宋体",Font.PLAIN,15);
	Font f1 = new Font("宋体",Font.PLAIN,13);
	LiveMoneyBean liveMoneyBean = new LiveMoneyBean();
	static String startdate = null;
	static String enddate = null;
	static float LiveMoney = 0;
	String username = Login.username;
	JScrollPane sp = new JScrollPane();
	JTable tbl = new JTable(new DefaultTableModel()){
		public boolean isCellEditable(int row,int column){
			return false;
		}
	};
	DefaultTableModel m = (DefaultTableModel)this.tbl.getModel();
	public UpdateMoneyDialog(){}
	public  UpdateMoneyDialog (JFrame owner, String title,boolean model) {
		super(owner,title,model);
		init();
		addTableContent();
		setLocation(800,400);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		getRootPane().setDefaultButton(set);
	}
	public void init(){
		Container c = getContentPane();
		c.setLayout(null);
		JPanel jp1 = new JPanel();jp1.setLayout(new FlowLayout());
		startDate = new JTextField(14);
		startDate.setFont(f1);
		startDate.setEditable(false);
		endDate = new JTextField(14);
		endDate.setFont(f1);
		endDate.setEditable(false);
		
		String sql1 = "select * from livemoney where username='"+username+"'";
		LiveMoneyBean liveMoneyBean1 = ControlLiveMoney.selectLiveMoney(sql1);
		StartMonth = liveMoneyBean1.getStartMonth();
		if(liveMoneyBean1.getStartDate() != null && liveMoneyBean1.getEndDate() != null){
			startDate.setText(liveMoneyBean1.getStartDate());
			endDate.setText(liveMoneyBean1.getEndDate());
		}else{
			startDate.setText("未设置起始日期");
			endDate.setText("未设置结束日期");
		}
		
		DateChooser dateChooser1 = DateChooser.getInstance("yyyy年MM月dd日");
		dateChooser1.register(startDate);
		DateChooser dateChooser2 = DateChooser.getInstance("yyyy年MM月dd日");
		dateChooser2.register(endDate);
		
		JLabel start = new JLabel("起始:");
		start.setFont(f);
		JLabel end = new JLabel("结束:");
		end.setFont(f);
		JLabel money = new JLabel("金额:");
		money.setFont(f);
		livemoney.setFont(f1);
		
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
		
		set.setFont(f);
		esc.setFont(f);
		set.addActionListener(new MyActionListener());
		esc.addActionListener(new MyActionListener());
		
		jp1.setOpaque(false);
		jp1.add(start);jp1.add(startDate);
		jp1.add(end);jp1.add(endDate);
		jp1.add(money);jp1.add(livemoney);
		jp1.add(set);jp1.add(esc);
		
		JPanel jp = new JPanel();jp.setLayout(null);jp.setOpaque(false);
		sp.setBorder(BorderFactory.createEtchedBorder());
        sp.getViewport().add(tbl);
        sp.setBounds(0, 0, 400,250);
        tbl.setFont(new Font("宋体",Font.PLAIN,14));
        tbl.addMouseListener(new tblListener());
		((DefaultTableCellRenderer)tbl.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        m.addColumn("起始日期");
    	m.addColumn("结束日期");
    	m.addColumn("生活费");
	    m.setRowCount(0);
		m.setColumnCount(3);
		makeFace(tbl);
		jp.add(sp);
		
		jp1.setBounds(0, 0, 570, 100);
		jp.setBounds(85, 50, 400, 250);
		c.add(jp1);c.add(jp);
		
		pop = new JPopupMenu();
		JMenuItem delete = new JMenuItem("删除(D)",KeyEvent.VK_D);
		pop.add(delete);
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null,"是否删除该条生活费信息"
						,"删除",JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION){
					int selectIndex = tbl.getSelectedRow();
					String startDate = tbl.getValueAt(selectIndex, 0).toString().trim();
					String endDate = tbl.getValueAt(selectIndex, 1).toString().trim();
					String sql = "delete from livemoney where username='"+username+"'"
							+ "and startDate='"+startDate+"' and endDate='"+endDate+"'";
					int rs = ControlLiveMoney.deleteLiveMoney(sql);
					if(rs > 0){
						addTableContent();
					}
				}
			}
		});
	}
	
	class tblListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){
				int selectIndex = tbl.getSelectedRow();
				if(selectIndex >= 0){
					startDate.setText(tbl.getValueAt(selectIndex, 0).toString().trim());
					endDate.setText(tbl.getValueAt(selectIndex, 1).toString().trim());
					livemoney.setText(tbl.getValueAt(selectIndex, 2).toString().trim());
				}
			}
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
		}
	}
	
	class MyActionListener implements ActionListener{
		public MyActionListener(){}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == set){
				if(livemoney.getText() != null){
					liveMoneyBean.setStartDate(startDate.getText());
					liveMoneyBean.setEndDate(endDate.getText());
					liveMoneyBean.setLivemoney(Float.parseFloat(livemoney.getText()));
					liveMoneyBean.setStartMonth(StartMonth);
					startdate = startDate.getText();enddate = endDate.getText();
					LiveMoney = Float.parseFloat(livemoney.getText());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
					Calendar c = Calendar.getInstance();
					try {
						c.setTime(sdf.parse(startDate.getText()));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					StartDay = c.get(Calendar.DAY_OF_MONTH);
					liveMoneyBean.setStartDay(StartDay);
					String sql = "update livemoney set startDate=?,endDate=?,livemoney=?,StartMonth=?,StartDay=? where username='"+username+"' and StartMonth="+StartMonth+"";
					int result = ControlLiveMoney.updateLiveMoney(sql, liveMoneyBean);
					if(result >= 1){
						flag = true;
						JOptionPane.showMessageDialog(null, "修改成功","修改成功",1);
						dispose();
					} else{
						flag = false;
						JOptionPane.showMessageDialog(null, "修改失败,该月份暂未设置生活费","修改失败",0);
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
	
	private void addTableContent() {
		try {
			ResultSet rs = ControlLiveMoney.selectPartLiveMoney();
			m.setRowCount(0);
			while(rs.next()){
				Vector<Comparable> v = new Vector<Comparable>();
				v.add(rs.getString(1));
				v.add(rs.getString(2));
				v.add(rs.getFloat(3));
				m.addRow(v);
			}
		} catch (SQLException e) {
		}
	}
}
