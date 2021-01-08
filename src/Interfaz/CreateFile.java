package Interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Abstract.ActionEnterEvent;
import Abstract.KeyValidListener;
import Abstract.Variables;
import Interfaz.Buttons.CompGroup;
import Interfaz.Buttons.RoundButton;
import Managers.ColorManager;
import Managers.Created;
import Managers.FileManager;


@SuppressWarnings("serial")
public class CreateFile extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private Created onCreated;
	private JTextField textField;
	private JTextField txtruta;
	
	private Pattern pat = Pattern.compile("[aA-zZ]|[0-9]|_");
	private RoundButton rdCarpeta,rdFile;
	
	public CreateFile(JFrame f,Created cr) 
	{
		onCreated = cr;
		
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				textField.requestFocus();
			}
		});
		
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(2));
		contentPanel.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(12, 150, 58, 15);
		contentPanel.add(lblNombre);
		
		
		 rdCarpeta = new RoundButton(false);
		rdCarpeta.setBounds(12, 19, 25, 27);
		contentPanel.add(rdCarpeta);
		
		 rdFile = new RoundButton(true);
		rdFile.setBounds(162, 19, 25, 27);
		contentPanel.add(rdFile);
		
		new CompGroup(rdCarpeta,rdFile);
		
		JLabel lblCarpeta = new JLabel("Carpeta");
		lblCarpeta.setBounds(47, 27, 58, 15);
		rdCarpeta.labelFor(lblCarpeta);
		
		
		JLabel lblArchivo = new JLabel("Archivo");
		lblArchivo.setBounds(203, 27, 58, 15);
		rdFile.labelFor(lblArchivo);
		

		contentPanel.add(lblArchivo);
		contentPanel.add(lblCarpeta);
		
		textField = new JTextField();
		textField.addKeyListener( new ActionEnterEvent((a)->this.accept(null)));
		textField.addKeyListener(new KeyValidListener(c->{
			
			return !pat.matcher(c.toString()).find();
		}));
		
		
		textField.setBounds(76, 148, 218, 19);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblRuta = new JLabel("Ruta:");
		lblRuta.setBounds(12, 81, 58, 15);
		contentPanel.add(lblRuta);
		
		txtruta = new JTextField();
		txtruta.setEditable(false);
		txtruta.setColumns(10);
		txtruta.setBounds(76, 79, 418, 19);
		contentPanel.add(txtruta);
		
		JButton btnNewButton = new JButton("...");
		btnNewButton.addActionListener((a)->
			{if(FileManager.openFileChosser(onCreated))
				CreateFile.this.dispose();}
		);
		btnNewButton.setBounds(506, 76, 33, 25);
		contentPanel.add(btnNewButton);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(3));
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton button = new JButton("OK");
		button.addActionListener(this::accept);
		button.setActionCommand("OK");
		panel.add(button);
		
		JButton button_1 = new JButton("Cancel");
		button_1.addActionListener((a)->dispose());
		button_1.setActionCommand("Cancel");
		panel.add(button_1);
		
		setSize(545, 270);
		setLocationRelativeTo(f);
		
	}
	
	@Override
	public void setVisible(boolean b) 
	{
		txtruta.setText(Compilador.treeFiles.nodeSelected.isPackage() ? 
				Compilador.treeFiles.nodeSelected.getNodePath():
				Compilador.treeFiles.nodeSelected.padre.getNodePath());
		super.setVisible(b);
		
	}
	
	private void accept(ActionEvent ae)
	{
		if(textField.getText().isEmpty())
		{
			MyOptionPane.showMessage(this, "Debe asignar un\nnombre al archivo",
						"Nombre no valido",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String nombre = textField.getText().trim();
		
		if(rdFile.isSelected())
			nombre+="."+Variables.extension;
		
		String path = txtruta.getText()+nombre;
		
		textField.setText("");

		if(FileManager.createDir_File(path, this, rdCarpeta.isSelected()))
		{
			Compilador.treeFiles.addNode(rdCarpeta.isSelected(), nombre);
			
			onCreated.onCreated(nombre,rdFile.isSelected() ? 1 : 0,path);
		}
		
		dispose();
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		//textField.setText("");
		super.dispose();
	}
}
