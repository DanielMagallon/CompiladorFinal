package Interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Abstract.VoidMethod;
import Managers.ColorManager;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Color;

@SuppressWarnings("serial")
public class DialogWorkspace extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblNewLabel;
	private JTextField textField;

	public String fileRuta,fileSpace="";
	private JTextField txtfolder;
	/**
	 * Create the dialog.
	 */
	public DialogWorkspace(VoidMethod vm) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 475, 202);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(2));
		getContentPane().add(contentPanel, BorderLayout.WEST);
		{
			lblNewLabel = new JLabel("Workspace:");
			lblNewLabel.setForeground(Color.white);
		}
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		
		JButton btnExaminar = new JButton("Examinar");
		btnExaminar.setBackground(ColorManager.ESCALA_AZUL.getMediumScaleColor());
		btnExaminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser jchoser = new JFileChooser();
				jchoser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if(jchoser.showOpenDialog(DialogWorkspace.this)==JFileChooser.APPROVE_OPTION) {
					fileSpace = jchoser.getSelectedFile().getPath();
					textField.setText(fileSpace+"/"+txtfolder.getText().trim());
				}
				
				
			}
		});
		
		JLabel lblCarpeta = new JLabel("Carpeta");
		lblCarpeta.setForeground(Color.WHITE);
		
		txtfolder = new JTextField();
		txtfolder.setColumns(10);
		txtfolder.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyReleased(KeyEvent e) 
			{
				textField.setText(fileSpace+"/"+txtfolder.getText());
			}
		});
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblCarpeta, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtfolder)))
					.addGap(18)
					.addComponent(btnExaminar)
					.addContainerGap(57, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnExaminar))
					.addGap(28)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCarpeta, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtfolder, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(34, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(3));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						if(txtfolder.getText().isEmpty() || fileSpace.isEmpty())
							JOptionPane.showMessageDialog(DialogWorkspace.this, 
									"Las rutas Workspace y Carpeta deben "
									+ "ser introducidas", "Error en rutas", JOptionPane.ERROR_MESSAGE);
						else
						{
							fileRuta = textField.getText().trim();
							vm.method("yes");
						}
					}
				});
				okButton.setActionCommand("OK");
				okButton.setBackground(ColorManager.ESCALA_AZUL.getMediumScaleColor());
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						vm.method("no");
					}
				});
				cancelButton.setActionCommand("Cancel");
				cancelButton.setBackground(ColorManager.ESCALA_AZUL.getMediumScaleColor());
				buttonPane.add(cancelButton);
			}
		}
	}
}
