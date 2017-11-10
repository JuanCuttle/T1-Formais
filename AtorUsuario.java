import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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
					Interface.mostraGramatica(Principal.gramaticasCriadas.get(0));
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
				Principal.gramaticasCriadas.add(Interface.criarGramatica());
			}
		});
		btnCriarGramtica.setBounds(33, 180, 137, 25);
		getContentPane().add(btnCriarGramtica);
		
		JButton btnCriarExpressoRegular = new JButton("Criar Express\u00E3o Regular");
		btnCriarExpressoRegular.setBounds(198, 180, 197, 25);
		getContentPane().add(btnCriarExpressoRegular);
		
		JButton btnEditarGramtica = new JButton("Editar Gram\u00E1tica");
		btnEditarGramtica.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				String confirmP;
				int i = 0;
				String gramaticas = "";
				for (Gramatica g : Principal.gramaticasCriadas){
					gramaticas += "\nGramatica "+i+":\n";
					gramaticas += Interface.mostraGramatica(g);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Deseja editar qual gramatica?\n"+gramaticas);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					Interface.editarGramatica(Principal.gramaticasCriadas.get(escolhida));
					
				}
			}
		});
		btnEditarGramtica.setBounds(33, 76, 137, 25);
		getContentPane().add(btnEditarGramtica);
		
		JButton btnEditarEr = new JButton("Editar ER");
		btnEditarEr.setBounds(236, 76, 97, 25);
		getContentPane().add(btnEditarEr);
	}
}
