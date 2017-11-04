import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class AtorUsuario extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AtorUsuario frame = new AtorUsuario();
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
	public AtorUsuario() {
		setTitle("Trabalho I");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JButton btnCriarGramtica = new JButton("Criar Gram\u00E1tica");
		btnCriarGramtica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Interface.criarGramatica();
			}
		});
		btnCriarGramtica.setBounds(47, 180, 123, 25);
		getContentPane().add(btnCriarGramtica);
		
		JButton btnCriarExpressoRegular = new JButton("Criar Express\u00E3o Regular");
		btnCriarExpressoRegular.setBounds(198, 180, 197, 25);
		getContentPane().add(btnCriarExpressoRegular);
		
		JButton btnEditarGramtica = new JButton("Editar Gram\u00E1tica");
		btnEditarGramtica.setBounds(47, 76, 137, 25);
		getContentPane().add(btnEditarGramtica);
		
		JButton btnEditarEr = new JButton("Editar ER");
		btnEditarEr.setBounds(236, 76, 97, 25);
		getContentPane().add(btnEditarEr);
	}
}
