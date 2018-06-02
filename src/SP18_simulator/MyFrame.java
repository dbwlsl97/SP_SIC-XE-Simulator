package SP18_simulator;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


public class MyFrame extends JFrame {

	private JPanel contentPane;
	private JTextField tfName, tfpName, tfHaddr, tfpLength;
	private JTextField aDec, aHex, xDec, xHex, lDec, lHex, pcDec, pcHex, tfSW;
	private JTextField bDec, bHex, sDec, sHex, tDec, tHex, fDec, fHex;
	private JTextField tfEAddr, tfSaddrM, tfTaddr, tfDevice;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyFrame frame = new MyFrame();
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
	public MyFrame() {
		this.setResizable(false);
		setTitle("SIC/XE Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 700);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mFile = new JMenu("File");
		menuBar.add(mFile);
		
		JMenu mAbout = new JMenu("About");
		menuBar.add(mAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbName = new JLabel("FileName :");
		lbName.setBounds(12, 10, 70, 15);
		contentPane.add(lbName);
		
		tfName = new JTextField();
		tfName.setBounds(94, 7, 109, 21);
		contentPane.add(tfName);
		tfName.setColumns(10);
		
		JButton bFopen = new JButton("open");
		bFopen.setBounds(215, 6, 70, 23);
		contentPane.add(bFopen);
		
		JPanel pHeader = new JPanel();
		pHeader.setLayout(null);
		pHeader.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "H (Header Record)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pHeader.setBounds(18, 34, 224, 117);
		contentPane.add(pHeader);
		
		JLabel lbPname = new JLabel("Program Name :");
		lbPname.setBounds(12, 20, 106, 15);
		pHeader.add(lbPname);
		
		tfpName = new JTextField();
		tfpName.setBounds(118, 17, 88, 21);
		pHeader.add(tfpName);
		tfpName.setColumns(10);

		JLabel lbHaddr = new JLabel("<html>Start Address of<br/>¡¡Object Program :</html>");
		lbHaddr.setBounds(12, 35, 117, 43);
		pHeader.add(lbHaddr);
		
		tfHaddr = new JTextField();
		tfHaddr.setColumns(10);
		tfHaddr.setBounds(130, 54, 76, 21);
		pHeader.add(tfHaddr);
		
		JLabel lbpLength = new JLabel("Length of Program :");
		lbpLength.setBounds(12, 88, 117, 15);
		pHeader.add(lbpLength);
		
		tfpLength = new JTextField();
		tfpLength.setColumns(10);
		tfpLength.setBounds(130, 85, 76, 21);
		pHeader.add(tfpLength);
		
		JPanel pRegister = new JPanel();
		pRegister.setLayout(null);
		pRegister.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Register", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pRegister.setBounds(18, 154, 224, 174);
		contentPane.add(pRegister);
		
		JLabel lbDec = new JLabel("Dec");
		lbDec.setBounds(65, 20, 50, 15);
		pRegister.add(lbDec);
		
		JLabel lbHex = new JLabel("Hex");
		lbHex.setBounds(146, 20, 50, 15);
		pRegister.add(lbHex);
		
		JLabel lbA = new JLabel("A (#0)");
		lbA.setBounds(12, 42, 50, 15);
		pRegister.add(lbA);
		
		aDec = new JTextField();
		aDec.setColumns(10);
		aDec.setBounds(63, 39, 68, 20);
		pRegister.add(aDec);
		
		aHex = new JTextField();
		aHex.setColumns(10);
		aHex.setBounds(146, 39, 68, 20);
		pRegister.add(aHex);
		
		JLabel lbX = new JLabel("X (#1)");
		lbX.setBounds(12, 67, 50, 15);
		pRegister.add(lbX);
		
		xDec = new JTextField();
		xDec.setColumns(10);
		xDec.setBounds(63, 64, 68, 20);
		pRegister.add(xDec);
		
		xHex = new JTextField();
		xHex.setColumns(10);
		xHex.setBounds(146, 64, 68, 20);
		pRegister.add(xHex);
		
		JLabel lbL = new JLabel("L (#2)");
		lbL.setBounds(12, 92, 50, 15);
		pRegister.add(lbL);
		
		lDec = new JTextField();
		lDec.setColumns(10);
		lDec.setBounds(63, 89, 68, 20);
		pRegister.add(lDec);
		
		lHex = new JTextField();
		lHex.setColumns(10);
		lHex.setBounds(146, 89, 68, 20);
		pRegister.add(lHex);
		
		JLabel lbPC = new JLabel("PC (#8)");
		lbPC.setBounds(12, 117, 50, 15);
		pRegister.add(lbPC);
		
		pcDec = new JTextField();
		pcDec.setColumns(10);
		pcDec.setBounds(63, 114, 68, 20);
		pRegister.add(pcDec);
		
		pcHex = new JTextField();
		pcHex.setColumns(10);
		pcHex.setBounds(146, 114, 68, 20);
		pRegister.add(pcHex);
		
		JLabel lbSW = new JLabel("SW (#9)");
		lbSW.setBounds(12, 142, 50, 15);
		pRegister.add(lbSW);
		
		tfSW = new JTextField();
		tfSW.setColumns(10);
		tfSW.setBounds(63, 139, 151, 20);
		pRegister.add(tfSW);
		
		JPanel pRegisterXE = new JPanel();
		pRegisterXE.setLayout(null);
		pRegisterXE.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Register(for XE)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pRegisterXE.setBounds(18, 331, 224, 144);
		contentPane.add(pRegisterXE);
		
		JLabel label = new JLabel("Dec");
		label.setBounds(65, 20, 50, 15);
		pRegisterXE.add(label);
		
		JLabel label_1 = new JLabel("Hex");
		label_1.setBounds(146, 20, 50, 15);
		pRegisterXE.add(label_1);
		
		JLabel lbB = new JLabel("B (#3)");
		lbB.setBounds(12, 42, 50, 15);
		pRegisterXE.add(lbB);
		
		bDec = new JTextField();
		bDec.setColumns(10);
		bDec.setBounds(63, 39, 68, 20);
		pRegisterXE.add(bDec);
		
		bHex = new JTextField();
		bHex.setColumns(10);
		bHex.setBounds(146, 39, 68, 20);
		pRegisterXE.add(bHex);
		
		JLabel lbS = new JLabel("S (#4)");
		lbS.setBounds(12, 67, 50, 15);
		pRegisterXE.add(lbS);
		
		sDec = new JTextField();
		sDec.setColumns(10);
		sDec.setBounds(63, 64, 68, 20);
		pRegisterXE.add(sDec);
		
		sHex = new JTextField();
		sHex.setColumns(10);
		sHex.setBounds(146, 64, 68, 20);
		pRegisterXE.add(sHex);
		
		JLabel lbT = new JLabel("T (#5)");
		lbT.setBounds(12, 92, 50, 15);
		pRegisterXE.add(lbT);
		
		tDec = new JTextField();
		tDec.setColumns(10);
		tDec.setBounds(63, 89, 68, 20);
		pRegisterXE.add(tDec);
		
		tHex = new JTextField();
		tHex.setColumns(10);
		tHex.setBounds(146, 89, 68, 20);
		pRegisterXE.add(tHex);
		
		JLabel lbF = new JLabel("F (#6)");
		lbF.setBounds(12, 117, 50, 15);
		pRegisterXE.add(lbF);
		
		fDec = new JTextField();
		fDec.setColumns(10);
		fDec.setBounds(63, 114, 68, 20);
		pRegisterXE.add(fDec);
		
		fHex = new JTextField();
		fHex.setColumns(10);
		fHex.setBounds(146, 114, 68, 20);
		pRegisterXE.add(fHex);
		
		JPanel pEnd = new JPanel();
		pEnd.setLayout(null);
		pEnd.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "E (End Record)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pEnd.setBounds(255, 34, 226, 72);
		contentPane.add(pEnd);
		
		JLabel lbEaddr = new JLabel("<html>Address of First Instruction<br/>\u3000in Object Program :</html>");
		lbEaddr.setBounds(12, 13, 156, 43);
		pEnd.add(lbEaddr);
		
		tfEAddr = new JTextField();
		tfEAddr.setColumns(10);
		tfEAddr.setBounds(138, 37, 76, 21);
		pEnd.add(tfEAddr);
		
		JLabel lbSaddrM = new JLabel("Start Address in Memory");
		lbSaddrM.setBounds(263, 127, 141, 15);
		contentPane.add(lbSaddrM);
		
		tfSaddrM = new JTextField();
		tfSaddrM.setHorizontalAlignment(SwingConstants.RIGHT);
		tfSaddrM.setText("0");
		tfSaddrM.setColumns(10);
		tfSaddrM.setBounds(369, 148, 109, 21);
		contentPane.add(tfSaddrM);
		
		JLabel lbTaddr = new JLabel("Target Address :");
		lbTaddr.setBounds(263, 185, 109, 15);
		contentPane.add(lbTaddr);
		
		tfTaddr = new JTextField();
		tfTaddr.setColumns(10);
		tfTaddr.setBounds(369, 182, 109, 21);
		contentPane.add(tfTaddr);
		
		JLabel lbInst = new JLabel("Instructions :");
		lbInst.setBounds(263, 210, 86, 15);
		contentPane.add(lbInst);
		
		JScrollPane scrpInst = new JScrollPane();
		scrpInst.setBounds(263, 235, 109, 233);
		contentPane.add(scrpInst);
		
		JLabel lbDevice = new JLabel("\uC0AC\uC6A9\uC911\uC778 \uC7A5\uCE58");
		lbDevice.setBounds(384, 235, 97, 15);
		contentPane.add(lbDevice);
		
		tfDevice = new JTextField();
		tfDevice.setColumns(10);
		tfDevice.setBounds(397, 254, 61, 20);
		contentPane.add(tfDevice);
		
		JButton btRunstep = new JButton("\uC2E4\uD589(1 Step)");
		btRunstep.setEnabled(false);
		btRunstep.setBounds(384, 376, 103, 23);
		contentPane.add(btRunstep);
		
		JButton btRunall = new JButton("\uC2E4\uD589(All)");
		btRunall.setEnabled(false);
		btRunall.setBounds(384, 409, 103, 23);
		contentPane.add(btRunall);
		
		JButton btExit = new JButton("\uC885\uB8CC");
		btExit.setBounds(384, 442, 103, 23);
		contentPane.add(btExit);
		
		JLabel lbLog = new JLabel("Log (\uBA85\uB839\uC5B4 \uC218\uD589 \uAD00\uB828) :");
		lbLog.setBounds(20, 487, 141, 15);
		contentPane.add(lbLog);
		
		JScrollPane scrpLog = new JScrollPane();
		scrpLog.setBounds(18, 508, 463, 116);
		contentPane.add(scrpLog);

	}
}
