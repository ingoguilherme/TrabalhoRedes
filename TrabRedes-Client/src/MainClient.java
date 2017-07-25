import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

public class MainClient extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private JMenuBar jmBarraMenu;
	private JMenu jmArquivo;
	private JMenuItem jmiArquivoSair;
	
	public static JTextArea jtaChat;
	JTextField jtfName, jtfIp, jtfPort;
	JButton jbConnect;
	JTextField jtfMessageBox;
	
	static Container C;
	
	private ThreadClient client;
	
	private static DefaultTableModel dtmUsers;
	private static JTable jtTableUsers;
	public static ArrayList<Client> connectedClients = new ArrayList<Client>();
	
	public MainClient() {
		super("Client");
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);
		
		C = getContentPane();
		C.setLayout(null);
		
		initComponents();
	}
	
	public void initComponents(){
		jmBarraMenu = new JMenuBar();
		jmBarraMenu.setBounds(0, 0, 800, 20);
		C.add(jmBarraMenu);
		
		//BEGIN - ARQUIVO
		jmArquivo = new JMenu("File");
		jmBarraMenu.add(jmArquivo);
		
		jmiArquivoSair = new JMenuItem("Exit");
		jmiArquivoSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(EXIT_ON_CLOSE);
			}
		});
		jmArquivo.add(jmiArquivoSair);
		//END - ARQUIVO
		
		TitledBorder tbConfiguration = new TitledBorder("Configuration");
		
		JPanel jpConfiguration = new JPanel();
		jpConfiguration.setLayout(null);
		jpConfiguration.setBounds(5, 25, 785, 60);
		jpConfiguration.setBorder(tbConfiguration);
		C.add(jpConfiguration);
		
		JLabel jlName = new JLabel("Name:");
		jlName.setBounds(10, 20, 40, 30);
		jpConfiguration.add(jlName);
		
		jtfName = new JTextField();
		jtfName.setBounds(50, 20, 200, 30);
		jtfName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAction();
			}
		});
		jpConfiguration.add(jtfName);
		
		JLabel jlIp = new JLabel("IP:");
		jlIp.setBounds(260, 20, 20, 30);
		jpConfiguration.add(jlIp);
		
		jtfIp = new JTextField();
		jtfIp.setBounds(280, 20, 250, 30);
		jtfIp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAction();
			}
		});
		jpConfiguration.add(jtfIp);
		
		JLabel jlPort = new JLabel("Port:");
		jlPort.setBounds(540, 20, 40, 30);
		jpConfiguration.add(jlPort);
		
		jtfPort = new JTextField();
		jtfPort.setBounds(575, 20, 100, 30);
		jtfPort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAction();
			}
		});
		jpConfiguration.add(jtfPort);
		
		jbConnect = new JButton("Connect");
		jbConnect.setBounds(680, 20, 95, 30);
		jbConnect.setMargin(new Insets(0, 0, 0, 0));
		jbConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAction();
			}
		});
		jpConfiguration.add(jbConnect);
		
		JPanel jpChat = new JPanel();
		jpChat.setBounds(7, 90, 600, 475);
		jpChat.setBackground(Color.GRAY);
		jpChat.setLayout(null);
		C.add(jpChat);
		
		jtaChat = new JTextArea();
		jtaChat.setEditable(false);
		jtaChat.setLineWrap(true);
		
		DefaultCaret caret = (DefaultCaret) jtaChat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane jsp = new JScrollPane(jtaChat);
		jsp.setBounds(5, 5, 590, 430);
		jpChat.add(jsp);
		
		jtfMessageBox = new JTextField();
		jtfMessageBox.setBounds(5, 440, 500, 30);
		jtfMessageBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: Send Message
				sendAction();
			}
		});
		jpChat.add(jtfMessageBox);
		
		JButton jbSend = new JButton("Send");
		jbSend.setBounds(510, 440, 85, 30);
		jbSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: Send Message
				sendAction();
			}
		});
		jpChat.add(jbSend);
		
		//TODO: tabela de usuarios logados
		dtmUsers = new DefaultTableModel();
		dtmUsers.addColumn("Usu�rios");
		
		jtTableUsers = new JTable(dtmUsers){
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column){  
				return false;  
			}  
		};
		JScrollPane jspUsers = new JScrollPane(jtTableUsers);
		
		jspUsers.setBounds(615, 90, 175, 475);
		C.add(jspUsers);
		
		
		
		
		
		
	}
	
	public static void removeUserRows() {
		for(int i = dtmUsers.getRowCount()-1; i >= 0; i--) {
			dtmUsers.removeRow(i);
		}
	}
	
	public static void updateUserTable(ArrayList<Client> clients) {
		removeUserRows();
		connectedClients = clients;
		
		for(Client c :connectedClients) {
			dtmUsers.addRow(new Object[]{"[" + c.getId() + "] " + c.getName()});
		}
	}
	
	public void sendAction() {
		if(!jtfMessageBox.getText().isEmpty()) {
			
			
			
			int selectedUsers[] = jtTableUsers.getSelectedRows();
			boolean himself = false;
			
			if(selectedUsers.length == 1) {
				for(int j :selectedUsers) {
					if(connectedClients.get(j).getId() == client.getClient().getId()) {
						himself = true;
						break;
					}
				}
			}
			
			if(himself || selectedUsers.length == 0 || selectedUsers.length == dtmUsers.getRowCount()) {
				Message msg = new Message(jtfMessageBox.getText(), Message.TYPE_PLAINTEXT, new Date(), client.getClient());
				client.sendMessage(msg);
				jtfMessageBox.setText("");
			}
			else {
				for(int j :selectedUsers) {
					if(connectedClients.get(j).getId() != client.getClient().getId()) {
						Message msg = new Message(jtfMessageBox.getText(), Message.TYPE_PLAINTEXT, new Date(), client.getClient(), connectedClients.get(j));
						client.sendMessage(msg);
					}
				}
				
				jtfMessageBox.setText("");
			}
			
		}
		
	}
	
	public void connectAction() {
		
		if(client == null) {
			int port = Integer.parseInt(jtfPort.getText());
			String ip = jtfIp.getText();
			String name = jtfName.getText();
			client = new ThreadClient(ip, port, name);
		}
		
		if(client.getSocket() == null || client.getSocket().isClosed()) {
			client.connect();
			jbConnect.setText("Disconnect");
		}
		else {
			client.disconnect();
			client = null;
			jbConnect.setText("Connect");
		}
		
	}
	
	public static void main(String[] args) {
		MainClient main = new MainClient(); 
		main.setVisible(true);
	}

}