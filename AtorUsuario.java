import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JLabel;

import java.awt.Font;


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
					//Interface.mostraGramatica(Principal.gramaticasCriadas.get(0));
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
		
		JLabel lblNewLabel = new JLabel("Trabalho 1");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblNewLabel.setBounds(127, 70, 159, 44);
		getContentPane().add(lblNewLabel);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnAutmatos = new JMenu("Automatos");
		menuBar.add(mnAutmatos);
		
		JButton btnGerarGr = new JButton("Gerar GR");
		btnGerarGr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String confirmP;
				int i = 0;
				String automatos = "";
				for (Automato aut : Principal.automatosCriados){
					automatos += "\nAutomato "+i+":\n";
					automatos += Interface.mostraAutomato(aut);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Gerar a partir de qual automato?\n"+automatos);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					Principal.gramaticasCriadas.add(Principal.automatosCriados.get(escolhida).gerarGramatica());
					//Interface.mostraAutomato(Principal.automatosCriados.get(0));
					
				}
			}
		});
		mnAutmatos.add(btnGerarGr);
		
		JButton btnNewButton = new JButton("Determinizar");
		mnAutmatos.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String confirmP;
				int i = 0;
				String automatos = "";
				for (Automato aut : Principal.automatosCriados){
					automatos += "\nAutomato "+i+":\n";
					automatos += Interface.mostraAutomato(aut);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Deseja determinizar qual autômato?\n"+automatos);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					try {
						Principal.automatosCriados.get(escolhida).determinizar();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("Minimizar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String confirmP;
				int i = 0;
				String automatos = "";
				for (Automato aut : Principal.automatosCriados){
					automatos += "\nAutomato "+i+":\n";
					automatos += Interface.mostraAutomato(aut);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Deseja minimizar qual autômato?\n"+automatos);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					Principal.automatosCriados.get(escolhida).minimizar();
					
				}
			}
		});
		mnAutmatos.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Uni\u00E3o");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String confirmP;
				int i = 0;
				String automatos = "";
				for (Automato aut : Principal.automatosCriados){
					automatos += "\nAutomato "+i+":\n";
					automatos += Interface.mostraAutomato(aut);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Selecione o primeiro autômato:\n"+automatos);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					//Principal.automatosCriados.get(escolhida).complemento();
					String confirmP2;
					int i2 = 0;
					String automatos2 = "";
					for (Automato aut : Principal.automatosCriados){
						automatos2 += "\nAutomato "+i2+":\n";
						automatos2 += Interface.mostraAutomato(aut);
						i2++;
					}
					confirmP2 = JOptionPane.showInputDialog(null, "Selecione o segundo autômato:\n"+automatos2);
					if (!confirmP.equals("")){
						int escolhida2 = Integer.parseInt(confirmP2);
						Automato um = Principal.automatosCriados.get(escolhida);
						Automato dois = Principal.automatosCriados.get(escolhida2);
						Principal.automatosCriados.add(um.uniao(dois));
						
					}
					
				}
			}
		});
		mnAutmatos.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Intersec\u00E7\u00E3o");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String confirmP;
				int i = 0;
				String automatos = "";
				for (Automato aut : Principal.automatosCriados){
					automatos += "\nAutomato "+i+":\n";
					automatos += Interface.mostraAutomato(aut);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Selecione o primeiro autômato:\n"+automatos);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					//Principal.automatosCriados.get(escolhida).complemento();
					String confirmP2;
					int i2 = 0;
					String automatos2 = "";
					for (Automato aut : Principal.automatosCriados){
						automatos2 += "\nAutomato "+i2+":\n";
						automatos2 += Interface.mostraAutomato(aut);
						i2++;
					}
					confirmP2 = JOptionPane.showInputDialog(null, "Selecione o segundo autômato:\n"+automatos2);
					if (!confirmP.equals("")){
						int escolhida2 = Integer.parseInt(confirmP2);
						Automato um = Principal.automatosCriados.get(escolhida).complemento();
						Automato dois = Principal.automatosCriados.get(escolhida2).complemento();
						Principal.automatosCriados.add((um.uniao(dois)).complemento());
						
					}
					
				}
			}
		});
		mnAutmatos.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Complemento");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String confirmP;
				int i = 0;
				String automatos = "";
				for (Automato aut : Principal.automatosCriados){
					automatos += "\nAutomato "+i+":\n";
					automatos += Interface.mostraAutomato(aut);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Deseja complementar qual autômato?\n"+automatos);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					Principal.automatosCriados.get(escolhida).complementar();
					
				}
			}
		});
		mnAutmatos.add(btnNewButton_4);
		
		JButton btnLinguagensIguais = new JButton("Linguagens iguais");
		btnLinguagensIguais.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String confirmP;
				int i = 0;
				String automatos = "";
				for (Automato aut : Principal.automatosCriados){
					automatos += "\nAutomato "+i+":\n";
					automatos += Interface.mostraAutomato(aut);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Selecione o primeiro autômato:\n"+automatos);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					//Principal.automatosCriados.get(escolhida).complemento();
					String confirmP2;
					int i2 = 0;
					String automatos2 = "";
					for (Automato aut : Principal.automatosCriados){
						automatos2 += "\nAutomato "+i2+":\n";
						automatos2 += Interface.mostraAutomato(aut);
						i2++;
					}
					confirmP2 = JOptionPane.showInputDialog(null, "Selecione o segundo autômato:\n"+automatos2);
					if (!confirmP.equals("")){
						int escolhida2 = Integer.parseInt(confirmP2);
						Automato um = Principal.automatosCriados.get(escolhida);
						Automato dois = Principal.automatosCriados.get(escolhida2);
						JOptionPane.showMessageDialog(null, (um.linguagensIguais(dois)));
						
					}
					
				}
			}
		});
		mnAutmatos.add(btnLinguagensIguais);
		
		JButton btnLinguagemVaziafinitainfinita = new JButton("Linguagem vazia/finita/infinita");
		btnLinguagemVaziafinitainfinita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String confirmP;
				int i = 0;
				String automatos = "";
				for (Automato aut : Principal.automatosCriados){
					automatos += "\nAutomato "+i+":\n";
					automatos += Interface.mostraAutomato(aut);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Escolha o autômato:\n"+automatos);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					Automato aut = Principal.automatosCriados.get(escolhida);
					if (aut.linguagemVazia()){
						JOptionPane.showMessageDialog(null, ("O estado inicial não é fértil, logo a linguagem é vazia"));
					} else if (aut.linguagemFinita()){
						JOptionPane.showMessageDialog(null, ("Não há recursão direta ou indireta, portanto a linguagem é finita"));
					} else {
						JOptionPane.showMessageDialog(null, ("Há recursão direta ou indireta, portanto a linguagem é infinita"));
					}
				}
			}
		});
		mnAutmatos.add(btnLinguagemVaziafinitainfinita);
		
		JButton btnLrContidaEm = new JButton("LR contida em LR");
		btnLrContidaEm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String confirmP;
				int i = 0;
				String automatos = "";
				for (Automato aut : Principal.automatosCriados){
					automatos += "\nAutomato "+i+":\n";
					automatos += Interface.mostraAutomato(aut);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Selecione o primeiro autômato:\n"+automatos);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					//Principal.automatosCriados.get(escolhida).complemento();
					String confirmP2;
					int i2 = 0;
					String automatos2 = "";
					for (Automato aut : Principal.automatosCriados){
						automatos2 += "\nAutomato "+i2+":\n";
						automatos2 += Interface.mostraAutomato(aut);
						i2++;
					}
					confirmP2 = JOptionPane.showInputDialog(null, "Selecione o segundo autômato:\n"+automatos2);
					if (!confirmP.equals("")){
						int escolhida2 = Integer.parseInt(confirmP2);
						Automato um = Principal.automatosCriados.get(escolhida);
						Automato dois = Principal.automatosCriados.get(escolhida2);
						JOptionPane.showMessageDialog(null, (um.contida(dois)));
						
					}
					
				}
			}
		});
		mnAutmatos.add(btnLrContidaEm);
		
		JMenu mnGramaticas = new JMenu("Gramaticas");
		menuBar.add(mnGramaticas);
		
		JButton btnCriarGramtica = new JButton("Criar Gram\u00E1tica");
		mnGramaticas.add(btnCriarGramtica);
		
		JButton btnEditarGramtica = new JButton("Editar Gram\u00E1tica");
		btnEditarGramtica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		mnGramaticas.add(btnEditarGramtica);
		
		JButton btnGerarAutomato = new JButton("Gerar Automato");
		btnGerarAutomato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String confirmP;
				int i = 0;
				String gramaticas = "";
				for (Gramatica g : Principal.gramaticasCriadas){
					gramaticas += "\nGramatica "+i+":\n";
					gramaticas += Interface.mostraGramatica(g);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Gerar a partir de qual gramatica?\n"+gramaticas);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					Principal.automatosCriados.add(Principal.gramaticasCriadas.get(escolhida).gerarAutomato());
					Interface.mostraAutomato(Principal.automatosCriados.get(0));
					
				}
			}
		});
		mnGramaticas.add(btnGerarAutomato);
		
		JMenu mnExpressoes = new JMenu("Expressoes");
		menuBar.add(mnExpressoes);
		
		JButton btnCriarExpressoRegular = new JButton("Criar Express\u00E3o Regular");
		btnCriarExpressoRegular.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Principal.expressoesCriadas.add(Interface.criarER());
			}
		});
		mnExpressoes.add(btnCriarExpressoRegular);
		
		JButton btnEditarEr = new JButton("Editar ER");
		btnEditarEr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String confirmP;
				int i = 0;
				String expressoes = "";
				for (Expressao g : Principal.expressoesCriadas){
					expressoes += "\nExpressao "+i+":\n";
					expressoes += Interface.mostraER(g);
					i++;
				}
				confirmP = JOptionPane.showInputDialog(null, "Deseja editar qual ER?\n"+expressoes);
				if (!confirmP.equals("")){
					int escolhida = Integer.parseInt(confirmP);
					Interface.editarER(Principal.expressoesCriadas.get(escolhida));
					
				}
			}
		});
		mnExpressoes.add(btnEditarEr);
		btnEditarGramtica.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		btnCriarGramtica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Principal.gramaticasCriadas.add(Interface.criarGramatica());
			}
		});
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
