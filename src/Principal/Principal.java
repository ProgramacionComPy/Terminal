package Principal;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Clases.*;
import data.DataConnection;

import Vistas.*;

//import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import javax.swing.JButton;


public class Principal extends JFrame {
	
	private ObjectContainer db = DataConnection.getInstance(); 
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Principal() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				db.close();
			}
		});
		setTitle("Gesti\u00F3n de Terminal de \u00D3mnibus");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 523, 353);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Archivo");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Salir");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 int res=JOptionPane.showConfirmDialog(null,"Estas seguro que desea salir??",".::Pregunta::.",JOptionPane.YES_NO_OPTION);
				 if(res==0){	
			     db.close();	 
				 System.exit(0);				 
				 }
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Acerca de");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,"Gestión de Terminal de ómnibus v1.0.\n\nTodos los derechos reservados.",".::Acerca de::.",JOptionPane.INFORMATION_MESSAGE);	
			}
		});
		menuBar.add(mntmNewMenuItem_1);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Terminales");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    VTerminales age2= new VTerminales();
			    age2.setVisible(true);
			}
		});
		btnNewButton.setBounds(40, 35, 148, 78);
		contentPane.add(btnNewButton);
		
		JButton btnEmpresas = new JButton("Empresas");
		btnEmpresas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    VEmpresas age= new VEmpresas();
			    age.setVisible(true);
			}
		});
		btnEmpresas.setBounds(307, 35, 148, 78);
		contentPane.add(btnEmpresas);
		
		JButton btnControl = new JButton("Control");
		btnControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    VControl age3= new VControl();
			    age3.setVisible(true);	
			}
		});
		btnControl.setBounds(173, 173, 148, 78);
		contentPane.add(btnControl);
	}
}
