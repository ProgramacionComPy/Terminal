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

public class VEmpresas extends JFrame {

	private ObjectContainer db = DataConnection.getInstance();
	private JPanel contentPane;
	private JTextField textField_1;
	private JTextField textField_2;
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
    private JTextField textField_3;
    private JTextField textField_4;
	
	//Función para insertar un nuevo registro
	void insertar(ObjectContainer e){
		Clases.Empresas ter3 = new Clases.Empresas(textField.getText(),null,null,null);
		ObjectSet<Object> ter2 = db.queryByExample(ter3);
		if(ter2.size()==0)
		{
		 try{
			Clases.Empresas ter = new Clases.Empresas(textField.getText(),textField_1.getText(),textField_2.getText(),textField_3.getText());
		    db.store(ter);
		    db.commit();
			btnEliminar.setEnabled(false);
			btnActualizar.setEnabled(false);
			btnNuevo.setEnabled(true);
			btnGuardar.setEnabled(false);
			textField.setText("");
			textField_1.setText("");
			textField_2.setText("");
			textField_3.setText("");
			textField_4.setText("");
			textField_1.setEnabled(false);
			textField_3.setEnabled(false);
			textField_2.setEnabled(false);
			textField.setEnabled(false);
	 	   }catch (Db4oException ee) {
		 JOptionPane.showMessageDialog(null,ee.getMessage());
		 db.rollback();
		 }
		 }
		 else{
			 	JOptionPane.showMessageDialog(null,"El nombre "+textField.getText()+" ya existe.");
			 }
	}
	
	//Función para litar registros
	void listar (ObjectContainer e){	 
		 String[] titulos = {"Nombre", "Dirección","Teléfono", "RUC"};	 
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
	    String[] fila = new String [4];
	    
	    List<Empresas> ps = null;
	    
	    if (textField_4.getText().equals("")) {//Si no hay nada
	    ps = db.query(Empresas.class);
	    }
	    else
	    {
	        ps = db.query(new Predicate<Empresas>() {
                public boolean match(Empresas o) {
                    return o.getNombre().toLowerCase().contains(textField_4.getText().toLowerCase());//Convertimos a minúscula y comparamos
                }
            }, new QueryComparator<Empresas>() {
                public int compare(Empresas o1, Empresas o2) {
                    return 0;
                }
            });
                
         }	    	

	    for (Empresas ter : ps) {//Añadimos los registros a la tabla
		                fila[0] = ter.getNombre();
		                fila[1] = ter.getDireccion();
		                fila[2] = ter.getTelefono();
		                fila[3] = ter.getRuc();
		                model.addRow(fila);
		            }
		            //Agrego el default model
		            table.setModel(model);
		        }catch (Exception e1){
		        JOptionPane.showMessageDialog(null, e1);
		       }		 	 	 		 
		}
		 	
	 //Función para modificar un registro
	 void modificar (ObjectContainer e, String nombre){
		 try {	 
		 ObjectSet le = e.queryByExample(new Clases.Empresas(nombre,null,null,null));
		 Clases.Empresas ter = (Clases.Empresas)le.next();
		 ter.setNombre(textField.getText());
		 ter.setDireccion((textField_1.getText()));
		 ter.setTelefono((textField_2.getText()));
		 ter.setRuc((textField_3.getText()));
		 db.store(ter);
		 db.commit();
		 listar(db);
		 textField.enable(false);
			btnEliminar.setEnabled(false);
			btnActualizar.setEnabled(false);
			btnNuevo.setEnabled(true);
			btnGuardar.setEnabled(false);
			textField.setText("");
			textField_1.setText("");
			textField_2.setText("");
			textField_3.setText("");
			textField_4.setText("");
			textField_1.setEnabled(false);
			textField_3.setEnabled(false);
			textField_2.setEnabled(false);
			textField.setEnabled(false);
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
	     String valor= (String) table.getValueAt(fila, 0);
		 ObjectSet le = e.queryByExample(new Clases.Empresas(valor,null,null,null));
		 Clases.Empresas ter = (Clases.Empresas)le.next();
		 db.delete(ter);
		 db.commit();
		 listar(db);
			btnEliminar.setEnabled(false);
			btnActualizar.setEnabled(false);
			btnNuevo.setEnabled(true);
			btnGuardar.setEnabled(false);
			textField.setText("");
			textField_1.setText("");
			textField_2.setText("");
			textField_3.setText("");
			textField_4.setText("");
			textField_1.setEnabled(false);
			textField_3.setEnabled(false);
			textField_2.setEnabled(false);
			textField.setEnabled(false);
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
					VEmpresas frame = new VEmpresas();
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
	public VEmpresas() {
		setTitle("Empresas");
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
			public void windowOpened(WindowEvent e) {
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
		
		JLabel lblId = new JLabel("Nombre");
		lblId.setBounds(10, 25, 46, 14);
		contentPane.add(lblId);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setBounds(81, 22, 136, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setEnabled(false);
		textField_1.setColumns(10);
		textField_1.setBounds(503, 25, 136, 20);
		contentPane.add(textField_1);
		
		JLabel lblApellido = new JLabel("Tel\u00E9fono");
		lblApellido.setBounds(10, 76, 66, 14);
		contentPane.add(lblApellido);
		
		textField_2 = new JTextField();
		textField_2.setEnabled(false);
		textField_2.setColumns(10);
		textField_2.setBounds(81, 73, 136, 20);
		contentPane.add(textField_2);
		
		JLabel lblNombre = new JLabel("Direcci\u00F3n");
		lblNombre.setBounds(424, 28, 74, 14);
		contentPane.add(lblNombre);
		
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
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");			
				btnEliminar.setEnabled(false);
				btnActualizar.setEnabled(false);
				btnNuevo.setEnabled(true);
				btnGuardar.setEnabled(true);
				textField_1.setEnabled(true);
				textField_3.setEnabled(true);
				textField_2.setEnabled(true);
				textField.setEnabled(true);
				textField.requestFocus();
			}
		});
		btnNuevo.setBounds(10, 296, 89, 23);
		contentPane.add(btnNuevo);
		
		btnGuardar = new JButton("Guardar");
		btnGuardar.setEnabled(false);
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().length()>0 && textField_2.getText().length()>0 && textField_1.getText().length()>0 
						&& textField.getText().length()<50 && textField_2.getText().length()<50 && textField_1.getText().length()<50) {
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
				if(textField.getText().length()>0 && textField_2.getText().length()>0 && textField_1.getText().length()>0 
						&& textField.getText().length()<50 && textField_2.getText().length()<50 && textField_1.getText().length()<50) {
							try {	
								modificar(db,textField.getText());
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
				     String valor=(String) table.getValueAt(fila, 0);
					 try {
						 ObjectSet le = db.queryByExample(new Clases.Empresas(valor,null,null,null));					 
						 if (le.hasNext())
						 {
						 Clases.Empresas ter = (Clases.Empresas)le.next();
						 textField.setText(ter.getNombre());
						 textField_1.setText(ter.getDireccion());
						 textField_2.setText(ter.getTelefono());
						 textField_3.setText(ter.getRuc());						
							btnEliminar.setEnabled(true);
							btnActualizar.setEnabled(true);
							btnNuevo.setEnabled(true);
							btnGuardar.setEnabled(false);
							textField.setEnabled(false);
							textField_1.setEnabled(true);
							textField_3.setEnabled(true);
							textField_2.setEnabled(true);
						 }
						 }
						 catch (Exception ee) {
						 System.out.println("No se encuentra nombre");
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
		
		JLabel lblRuc = new JLabel("RUC");
		lblRuc.setBounds(424, 73, 66, 14);
		contentPane.add(lblRuc);
		
		textField_3 = new JTextField();
		textField_3.setEnabled(false);
		textField_3.setColumns(10);
		textField_3.setBounds(503, 73, 136, 20);
		contentPane.add(textField_3);
		
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
	}
}
