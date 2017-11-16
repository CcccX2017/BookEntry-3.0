package com.cx.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.cx.util.ControlLiveMoney;

public class SelectLiveMoneyDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String startDate = null;
	static String endDate = null;
	static String livemoney = null;
	JScrollPane sp = new JScrollPane();
	JTable tbl = new JTable(new DefaultTableModel()){
		public boolean isCellEditable(int row,int column){
			return false;
		}
	};
	DefaultTableModel m = (DefaultTableModel)this.tbl.getModel();
	public  SelectLiveMoneyDialog (JFrame owner, String title,boolean model) {
		super(owner,title,model);
		init();
		setResizable(false);
		setLocation(750, 400);
		addTableContent();
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
		
		JPanel jp1 = new JPanel();jp1.setLayout(new FlowLayout());
		JLabel tip = new JLabel("双击表格查看详情");
		tip.setFont(new Font("宋体", Font.BOLD, 20));
		tip.setForeground(Color.RED);
		jp1.add(tip);
		
		JPanel jp2 = new JPanel();jp2.setLayout(null);
		sp.setBorder(BorderFactory.createEtchedBorder());
        sp.getViewport().add(tbl);
        sp.setBounds(0, 0, 350, 250);
        tbl.setFont(new Font("宋体",Font.PLAIN,14));
        tbl.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e){
        		if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){
        			int selectIndex = tbl.getSelectedRow();
//        			for(int i = 0;i<tbl.getColumnCount();i++){
//        				System.out.print(tbl.getValueAt(selectIndex, i) + "  ");
//        			}
//        			System.out.println();
        			startDate = tbl.getValueAt(selectIndex, 0).toString().trim();
        			endDate = tbl.getValueAt(selectIndex, 1).toString().trim();
        			livemoney = tbl.getValueAt(selectIndex, 2).toString().trim();
        			dispose();
        		}
        	}
		});
        ((DefaultTableCellRenderer)tbl.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        m.addColumn("起始日期");
    	m.addColumn("结束日期");
    	m.addColumn("生活费");
	    m.setRowCount(0);
		m.setColumnCount(3);
		makeFace(tbl);
		jp2.add(sp);
		
		jp1.setBounds(0, 0, 400, 50); c.add(jp1);
		jp2.setBounds(25, 50, 350, 250); c.add(jp2);
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
