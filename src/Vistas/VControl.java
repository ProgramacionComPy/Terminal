package Vistas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;

import Clases.*;
import data.DataConnection;

//import com.db4o.Db4oEmbedded;
//import com.db4o.query.Predicate;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.Db4oException;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class VControl extends JFrame {

	private ObjectContainer db = DataConnection.getInstance();
	private JPanel contentPane;
	private JButton btnNuevo;
	private JButton btnGuardar;
	private JButton btnEliminar;
	private JButton btnActualizar;
	private JButton btnSalir;

	private JTextField textField;
	
	//Modelo generico para JTable
    DefaultTableModel model;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable table;
    private JTextField textField_4;
    private JLabel lblDia;
    private JLabel lblId_1;
    private JTextField textField_1;
    private JComboBox comboBox;
    private JComboBox comboBox_1;
    private JComboBox comboBox_2;
    private JComboBox comboBox_3;
    
    void LlenarCombos(){
        //Llenar combo de Empresas
        		List<Empresas> ps = null;
        		ps = db.query(Empresas.class);  
        		 for (Empresas ter : ps) {
        		 comboBox_1.addItem(ter.getNombre());
                 }               
        //Llenar combo de Terminales
        		List<Terminales> ps2 = null;
        		ps2 = db.query(Terminales.class);  
        		 for (Terminales ter : ps2) {
        		 comboBox_2.addItem(ter.getNombre());
        		 comboBox_3.addItem(ter.getNombre());
                 }
        		comboBox.setSelectedIndex(-1); 
     			comboBox_1.setSelectedIndex(-1);
    			comboBox_2.setSelectedIndex(-1);
    			comboBox_3.setSelectedIndex(-1);
    }
	
	//Función para insertar un nuevo registro
	void insertar(ObjectContainer e){
		Control ter3 = new Control(Integer.parseInt((textField.getText())),null,null,null,null,null);
		 	 
		ObjectSet<Object> ter2 = db.queryByExample(ter3);
		if(ter2.size()==0)
		{
		 try{
			Control ter = new Control(Integer.parseInt((textField.getText())),textField_1.getText(),(String)comboBox.getSelectedItem(),
					(String)comboBox_3.getSelectedItem(),(String)comboBox_1.getSelectedItem(),(String)comboBox_2.getSelectedItem());
		    db.store(ter);
		    db.commit();
			btnEliminar.setEnabled(false);
			btnActualizar.setEnabled(false);
			btnNuevo.setEnabled(true);
			btnGuardar.setEnabled(false);
			textField.setText("");
			textField_1.setText("");
			textField_4.setText("");
			comboBox.setSelectedIndex(-1);
			comboBox_1.setSelectedIndex(-1);
			comboBox_2.setSelectedIndex(-1);
			comboBox_3.setSelectedIndex(-1);
			textField.setEnabled(false);
			textField_1.setEnabled(false);
			comboBox.setEnabled(false);
			comboBox_1.setEnabled(false);
			comboBox_2.setEnabled(false);
			comboBox_3.setEnabled(false);
			comboBox.setSelectedIndex(-1);
			comboBox_1.setSelectedIndex(-1);
			comboBox_2.setSelectedIndex(-1);
			comboBox_3.setSelectedIndex(-1);
	 	   }catch (Db4oException ee) {
		 JOptionPane.showMessageDialog(null,ee.getMessage());
		 db.rollback();
		 }
		 }
		 else{
			 	JOptionPane.showMessageDialog(null,"El id "+textField.getText()+" ya existe.");
			 }
	}
	
	//Función para listar registros
	void listar (ObjectContainer e){	 
		 String[] titulos = {"ID", "Horarios Salidas","Empresa", "T. Salida", "T. Destino", "Día"};	 
		 try {
		 //encabezados de la tabla			   
			 model = new DefaultTableModel(null, titulos){
					@Override
					public boolean isCellEditable(int fila, int columna) {
							return false;//Le decimos que ninguna celda se puede editar directamente en la tabla
					}
				};
				TableRowSorter sorter = new TableRowSorter(model);
				table.setRowSorter(sorter);//Le decimos que la tabla se pueda ordenar (Haciendo clic en las columnas)	
	    //arreglo fila, para almacenar registros
	    String[] fila = new String [6];
	    
	    List<Control> ps = null;
	    
	    if (textField_4.getText().equals("")) {//Si no hay nada
	    ps = db.query(Control.class);
	    }
	    else
	    {
	        ps = db.query(new Predicate<Control>() {
                public boolean match(Control o) {
                    return o.getTerminaldes().toLowerCase().contains(textField_4.getText().toLowerCase());//Convertimos a minúscula y comparamos
                }
            }, new QueryComparator<Control>() {
                public int compare(Control o1, Control o2) {
                    return 0;
                }
            });
                
         }	    	

	    for (Control ter : ps) {//Añadimos los registros a la tabla
		                fila[0] = String.valueOf(ter.getId());
		                fila[1] = ter.getSalida();
		                fila[2] = ter.getEmpresa();
		                fila[3] = ter.getTerminalsal();
		                fila[4] = ter.getTerminaldes();
		                fila[5] = ter.getDia();
		                model.addRow(fila);
		            }
		            //Agrego el default model
		            table.setModel(model);
		        }catch (Exception e1){
		        JOptionPane.showMessageDialog(null, e1);
		       }		 	 	 		 
		}
		 	
	 //Función para modificar un registro
	 void modificar (ObjectContainer e, int id){
		 try {	 
		 ObjectSet le = e.queryByExample(new Clases.Control(id,null,null,null,null,null));
		 Control ter = (Control)le.next();
		 ter.setId(Integer.parseInt((textField.getText())));
		 ter.setEmpresa((String)comboBox_1.getSelectedItem());
		 ter.setSalida(textField_1.getText());
		 ter.setTerminaldes((String)comboBox_3.getSelectedItem());
		 ter.setTerminalsal((String)comboBox_2.getSelectedItem());
		 ter.setDia((String)comboBox.getSelectedItem());
		 db.store(ter);
		 db.commit();
		 listar(db);
			btnEliminar.setEnabled(false);
			btnActualizar.setEnabled(false);
			btnNuevo.setEnabled(true);
			btnGuardar.setEnabled(false);
			textField.setText("");
			textField_1.setText("");
			textField_4.setText("");
			textField.setEnabled(false);
			textField_1.setEnabled(false);
			comboBox.setEnabled(false);
			comboBox_1.setEnabled(false);
			comboBox_2.setEnabled(false);
			comboBox_3.setEnabled(false);
			comboBox.setSelectedIndex(-1);
			comboBox_1.setSelectedIndex(-1);
			comboBox_2.setSelectedIndex(-1);
			comboBox_3.setSelectedIndex(-1);
	 	   }catch (Db4oException ee) {
		 JOptionPane.showMessageDialog(null,ee.getMessage());
		 db.rollback();
		 }
		 }
	 
	 //Función para eliminar un registro
	void eliminar (ObjectContainer e){
		 if(table.getSelectedRow()!=-1){
			 int confirmado = JOptionPane.showConfirmDialog(null, "¿Desea eliminar "+table.getValueAt(table.getSelectedRow(), 0)+"?");
			    if (JOptionPane.OK_OPTION == confirmado){
		 try {	 
	     int fila = table.getSelectedRow();	 
	     int valor= (int) table.getValueAt(fila, 0);
		 ObjectSet le = e.queryByExample(new Control(valor,null,null,null,null,null));
		 Control ter = (Control)le.next();
		 db.delete(ter);
		 db.commit();
		 listar(db);
			btnEliminar.setEnabled(false);
			btnActualizar.setEnabled(false);
			btnNuevo.setEnabled(true);
			btnGuardar.setEnabled(false);
			textField.setText("");
			textField_1.setText("");
			textField_4.setText("");
			comboBox.setSelectedIndex(-1);
			comboBox_1.setSelectedIndex(-1);
			comboBox_2.setSelectedIndex(-1);
			comboBox_3.setSelectedIndex(-1);
			textField.setEnabled(false);
			textField_1.setEnabled(false);
			comboBox.setEnabled(false);
			comboBox_1.setEnabled(false);
			comboBox_2.setEnabled(false);
			comboBox_3.setEnabled(false);
			comboBox.setSelectedIndex(-1);
			comboBox_1.setSelectedIndex(-1);
			comboBox_2.setSelectedIndex(-1);
			comboBox_3.setSelectedIndex(-1);
	 	   }catch (Db4oException ee) {
		 JOptionPane.showMessageDialog(null,ee.getMessage());
		 db.rollback();
		 }
		 }
		 }
		 else{
			 	JOptionPane.showMessageDialog(null,"Seleccione algún registro");
			 }
	}
	 
	/**
	 * Launch the application.
	 */
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					VControl frame = new VControl();
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
	public VControl() {
		setTitle("Control");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {		
			}
			@Override
			public void windowClosing(WindowEvent e) {
			}
			@Override
			public void windowActivated(WindowEvent arg0) {
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
				LlenarCombos();
				listar(db);	
			}
		});			
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 694, 367);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblId = new JLabel("H. Salida");
		lblId.setBounds(232, 25, 66, 14);
		contentPane.add(lblId);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setBounds(66, 22, 136, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblApellido = new JLabel("Empresa");
		lblApellido.setBounds(481, 28, 66, 14);
		contentPane.add(lblApellido);
		
		JButton btnNewButton = new JButton("Mostrar Todo");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_4.setText("");
				listar(db);
			}
		});
		btnNewButton.setBounds(349, 124, 112, 23);
		contentPane.add(btnNewButton);
		
		btnNuevo = new JButton("Nuevo");
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				textField_1.setText("");
				textField.requestFocus();
				btnEliminar.setEnabled(false);
				btnActualizar.setEnabled(false);
				btnNuevo.setEnabled(true);
				btnGuardar.setEnabled(true);
				textField.setEnabled(true);
				textField_1.setEnabled(true);
				comboBox.setEnabled(true);
				comboBox_1.setEnabled(true);
				comboBox_2.setEnabled(true);
				comboBox_3.setEnabled(true);
				comboBox.setSelectedIndex(-1);
				comboBox_1.setSelectedIndex(-1);
				comboBox_2.setSelectedIndex(-1);
				comboBox_3.setSelectedIndex(-1);
		        //Poner nuevo id automáticamente
				int id=0;
        		List<Control> ps2 = null;
        		ps2 = db.query(Control.class);  
        		 for (Control ter : ps2) {
        		 id=ter.getId();
                 }
        		 id++;
        		 textField.setText(String.valueOf(id));
			}
		});
		btnNuevo.setBounds(10, 296, 89, 23);
		contentPane.add(btnNuevo);
		
		btnGuardar = new JButton("Guardar");
		btnGuardar.setEnabled(false);
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().length()>0 && textField_1.getText().length()>0 && comboBox.getSelectedIndex()>=0 && comboBox_1.getSelectedIndex()>=0 
						&& comboBox_2.getSelectedIndex()>=0 && comboBox_3.getSelectedIndex()>=0
						&& textField.getText().length()<50 && textField_1.getText().length()<50) {
							try {		
								insertar(db);
								listar(db);
							}
							catch (Exception ex) {
					            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
					        }
						}
						else{
						 JOptionPane.showMessageDialog(null, "Complete corectamente todos los campos.","Error",JOptionPane.ERROR_MESSAGE);  
						}
						}
		});
		btnGuardar.setBounds(156, 296, 89, 23);
		contentPane.add(btnGuardar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setEnabled(false);
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminar(db);
			}
		});
		btnEliminar.setBounds(297, 296, 103, 23);
		contentPane.add(btnEliminar);
		
		btnActualizar = new JButton("Actualizar");
		btnActualizar.setEnabled(false);
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				if(textField.getText().length()>0 && textField_1.getText().length()>0 && comboBox.getSelectedIndex()>=0 && comboBox_1.getSelectedIndex()>=0 
						&& comboBox_2.getSelectedIndex()>=0 && comboBox_3.getSelectedIndex()>=0
						&& textField.getText().length()<50 && textField_1.getText().length()<50) {
							try {	
								modificar(db,Integer.parseInt(textField.getText()));
							}
							catch (Exception ex) {
					            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
					        }
						}
						else{
						  JOptionPane.showMessageDialog(null, "Complete corectamente todos los campos.","Error",JOptionPane.ERROR_MESSAGE);  
						}
						}
		});
		btnActualizar.setBounds(436, 296, 103, 23);
		contentPane.add(btnActualizar);
		
		btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();			
			}
		});
		btnSalir.setBounds(571, 296, 89, 23);
		contentPane.add(btnSalir);
		
		panel = new JPanel();
		panel.setBounds(10, 163, 650, 122);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

			}
		});
		panel.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				 if (arg0.getButton() == 1) {
				     int fila = table.getSelectedRow();
				     int valor= Integer.parseInt((String)table.getValueAt(fila, 0));
					 try {
						 ObjectSet le = db.queryByExample(new Control(valor,null,null,null,null,null));					 
						 if (le.hasNext())
						 {
						 Control ter= (Control)le.next();
						 textField.setText(String.valueOf(ter.getId()));
						 textField_1.setText(ter.getSalida());
						 comboBox.setSelectedItem(ter.getDia());
						 comboBox_1.setSelectedItem(ter.getEmpresa());
						 comboBox_2.setSelectedItem(ter.getTerminalsal());
						 comboBox_3.setSelectedItem(ter.getTerminaldes());
							btnEliminar.setEnabled(true);
							btnActualizar.setEnabled(true);
							btnNuevo.setEnabled(true);
							btnGuardar.setEnabled(false);
							textField.setEnabled(false);
							textField_1.setEnabled(true);
							comboBox.setEnabled(true);
							comboBox_1.setEnabled(true);
							comboBox_2.setEnabled(true);
							comboBox_3.setEnabled(true);
						 }
						 }
						 catch (Exception ee) {
						 System.out.println("No se encuentra id");
						 }   
			        }
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
			}
		));
		scrollPane.setViewportView(table);	
		
		JLabel lblRuc = new JLabel("T. Salida");
		lblRuc.setBounds(10, 69, 66, 14);
		contentPane.add(lblRuc);
		
		textField_4 = new JTextField();
		textField_4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				listar(db);
			}
		});
		textField_4.setColumns(10);
		textField_4.setBounds(186, 125, 136, 20);
		contentPane.add(textField_4);
		
		JLabel lblTLlegada = new JLabel("T.Destino");
		lblTLlegada.setBounds(232, 69, 66, 14);
		contentPane.add(lblTLlegada);
		
		comboBox = new JComboBox();
		comboBox.setEnabled(false);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Lunes", "Martes", "Mi\u00E9rcoles", "Jueves", "Viernes", "S\u00E1bado", "Domingo"}));
		comboBox.setBounds(535, 63, 125, 20);
		contentPane.add(comboBox);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setEnabled(false);
		comboBox_1.setBounds(535, 22, 125, 20);
		contentPane.add(comboBox_1);
		
		comboBox_2 = new JComboBox();
		comboBox_2.setEnabled(false);
		comboBox_2.setBounds(66, 66, 136, 20);
		contentPane.add(comboBox_2);
		
		lblDia = new JLabel("D\u00EDa");
		lblDia.setBounds(481, 69, 66, 14);
		contentPane.add(lblDia);
		
		comboBox_3 = new JComboBox();
		comboBox_3.setEnabled(false);
		comboBox_3.setBounds(301, 66, 134, 20);
		contentPane.add(comboBox_3);
		
		lblId_1 = new JLabel("ID");
		lblId_1.setBounds(10, 25, 46, 14);
		contentPane.add(lblId_1);
		
		textField_1 = new JTextField();
		textField_1.setEnabled(false);
		textField_1.setColumns(10);
		textField_1.setBounds(299, 22, 136, 20);
		contentPane.add(textField_1);
	}
}
