package SP18_simulator;

import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.awt.*;

/**
 * VisualSimulator는 사용자와의 상호작용을 담당한다.<br>
 * 즉, 버튼 클릭등의 이벤트를 전달하고 그에 따른 결과값을 화면에 업데이트 하는 역할을 수행한다.<br>
 * 실제적인 작업은 SicSimulator에서 수행하도록 구현한다.
 */
public class VisualSimulator {
	ResourceManager resourceManager = new ResourceManager();
	SicLoader sicLoader = new SicLoader(resourceManager);
	SicSimulator sicSimulator = new SicSimulator(resourceManager);
	ArrayList<String> objcode = new ArrayList<String>();
	/**
	 * 프로그램 로드 명령을 전달한다.
	 */
	public void load(File program){
		
		sicLoader.load(program);
		sicSimulator.load(program);
	};

	/**
	 * 하나의 명령어만 수행할 것을 SicSimulator에 요청한다.
	 */
	public void oneStep(){
		if(sicSimulator.jump==false) {
		sicSimulator.oneStep();
		update();
		}
	};

	/**
	 * 남아있는 모든 명령어를 수행할 것을 SicSimulator에 요청한다.
	 */
	public void allStep(){
		sicSimulator.allStep();
		update();
		
	};
	
	/**
	 * 화면을 최신값으로 갱신하는 역할을 수행한다.
	 */
	public void update(){
		
	};
	

	public static void main(String[] args) {
		MyFrame myframe = new MyFrame();
		myframe.setVisible(true);
	}
}
class MyFrame extends JFrame {
	VisualSimulator visual = new VisualSimulator();
	private JPanel contentPane;
	private JTextField tfName, tfpName, tfHaddr, tfpLength;
	private JTextField aDec, aHex, xDec, xHex, lDec, lHex, pcDec, pcHex, tfSW;
	private JTextField bDec, bHex, sDec, sHex, tDec, tHex, fDec, fHex;
	private JTextField tfEAddr, tfSaddrM, tfTaddr, tfDevice;
	private JButton btRunstep,btRunall;

	public MyFrame() {
		this.setResizable(false);
		setTitle("SIC/XE Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 700);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mFile = new JMenu("File");
		menuBar.add(mFile);
		
		JMenuItem miOpen = new JMenuItem("Open");
		miOpen.addActionListener(new OpenActionListener());
		mFile.add(miOpen);
		
		
		JMenu mAbout = new JMenu("About");
		menuBar.add(mAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbName = new JLabel("FileName :");
		lbName.setFont(new Font("굴림", Font.PLAIN, 12));
		lbName.setBounds(12, 10, 70, 15);
		contentPane.add(lbName);
		
		tfName = new JTextField();
		tfName.setBounds(94, 7, 109, 21);
		contentPane.add(tfName);
		tfName.setColumns(10);
		
		JButton bFopen = new JButton("open");
		bFopen.addActionListener(new OpenActionListener());
		bFopen.setFont(new Font("굴림", Font.PLAIN, 12));
		bFopen.setBounds(215, 6, 65, 23);
		contentPane.add(bFopen);
		
		JPanel pHeader = new JPanel();
		pHeader.setLayout(null);
		pHeader.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "H (Header Record)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pHeader.setBounds(18, 34, 224, 117);
		contentPane.add(pHeader);
		
		JLabel lbPname = new JLabel("Program Name :");
		lbPname.setFont(new Font("굴림", Font.PLAIN, 12));
		lbPname.setBounds(12, 20, 106, 15);
		pHeader.add(lbPname);
		
		tfpName = new JTextField();
		
		tfpName.setBounds(118, 17, 88, 21);
		pHeader.add(tfpName);
		tfpName.setColumns(10);

		JLabel lbHaddr = new JLabel("<html>Start Address of<br/>　Object Program :</html>");
		lbHaddr.setFont(new Font("굴림", Font.PLAIN, 12));
		lbHaddr.setBounds(12, 35, 117, 43);
		pHeader.add(lbHaddr);
		
		tfHaddr = new JTextField();
		tfHaddr.setColumns(10);
		tfHaddr.setBounds(130, 54, 76, 21);
		pHeader.add(tfHaddr);
		
		JLabel lbpLength = new JLabel("Length of Program :");
		lbpLength.setFont(new Font("굴림", Font.PLAIN, 12));
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
		lbDec.setFont(new Font("굴림", Font.PLAIN, 12));
		lbDec.setBounds(65, 20, 50, 15);
		pRegister.add(lbDec);
		
		JLabel lbHex = new JLabel("Hex");
		lbHex.setFont(new Font("굴림", Font.PLAIN, 12));
		lbHex.setBounds(146, 20, 50, 15);
		pRegister.add(lbHex);
		
		JLabel lbA = new JLabel("A (#0)");
		lbA.setFont(new Font("굴림", Font.PLAIN, 12));
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
		lbX.setFont(new Font("굴림", Font.PLAIN, 12));
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
		lbL.setFont(new Font("굴림", Font.PLAIN, 12));
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
		lbPC.setFont(new Font("굴림", Font.PLAIN, 12));
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
		lbSW.setFont(new Font("굴림", Font.PLAIN, 12));
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
		label.setFont(new Font("굴림", Font.PLAIN, 12));
		label.setBounds(65, 20, 50, 15);
		pRegisterXE.add(label);
		
		JLabel label_1 = new JLabel("Hex");
		label_1.setFont(new Font("굴림", Font.PLAIN, 12));
		label_1.setBounds(146, 20, 50, 15);
		pRegisterXE.add(label_1);
		
		JLabel lbB = new JLabel("B (#3)");
		lbB.setFont(new Font("굴림", Font.PLAIN, 12));
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
		lbS.setFont(new Font("굴림", Font.PLAIN, 12));
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
		lbT.setFont(new Font("굴림", Font.PLAIN, 12));
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
		lbF.setFont(new Font("굴림", Font.PLAIN, 12));
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
		
		JLabel lbEaddr = new JLabel("<html>Address of First Instruction<br/>　in Object Program :</html>");
		lbEaddr.setFont(new Font("굴림", Font.PLAIN, 12));
		lbEaddr.setBounds(12, 13, 156, 43);
		pEnd.add(lbEaddr);
		
		tfEAddr = new JTextField();
		tfEAddr.setColumns(10);
		tfEAddr.setBounds(138, 37, 76, 21);
		pEnd.add(tfEAddr);
		
		JLabel lbSaddrM = new JLabel("Start Address in Memory");
		lbSaddrM.setFont(new Font("굴림", Font.PLAIN, 12));
		lbSaddrM.setBounds(263, 127, 141, 15);
		contentPane.add(lbSaddrM);
		
		tfSaddrM = new JTextField();
		tfSaddrM.setHorizontalAlignment(SwingConstants.RIGHT);
		tfSaddrM.setText("0");
		tfSaddrM.setColumns(10);
		tfSaddrM.setBounds(369, 148, 109, 21);
		contentPane.add(tfSaddrM);
		
		JLabel lbTaddr = new JLabel("Target Address :");
		lbTaddr.setFont(new Font("굴림", Font.PLAIN, 12));
		lbTaddr.setBounds(263, 185, 109, 15);
		contentPane.add(lbTaddr);
		
		tfTaddr = new JTextField();
		tfTaddr.setColumns(10);
		tfTaddr.setBounds(369, 182, 109, 21);
		contentPane.add(tfTaddr);
		
		JLabel lbInst = new JLabel("Instructions :");
		lbInst.setFont(new Font("굴림", Font.PLAIN, 12));
		lbInst.setBounds(263, 210, 86, 15);
		contentPane.add(lbInst);
		
		JLabel lbDevice = new JLabel("사용중인 장치");
		lbDevice.setFont(new Font("굴림", Font.PLAIN, 12));
		
		lbDevice.setBounds(384, 235, 97, 15);
		contentPane.add(lbDevice);
		
		tfDevice = new JTextField();
		tfDevice.setColumns(10);
		tfDevice.setBounds(397, 254, 61, 20);
		contentPane.add(tfDevice);
		
		btRunstep = new JButton("실행(1 Step)");
		btRunstep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visual.oneStep();
				visual.update();
			}
		});
		btRunstep.setFont(new Font("굴림", Font.PLAIN, 12));
		btRunstep.setEnabled(false);
		btRunstep.setBounds(384, 376, 103, 23);
		contentPane.add(btRunstep);
		
		btRunall = new JButton("실행(All)");
		btRunall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visual.allStep();
				visual.update();
			}
		});
		btRunall.setFont(new Font("굴림", Font.PLAIN, 12));
		btRunall.setEnabled(false);
		btRunall.setBounds(384, 409, 103, 23);
		contentPane.add(btRunall);
		
		JButton btExit = new JButton("종료");
		btExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btExit.setFont(new Font("굴림", Font.PLAIN, 12));
		btExit.setBounds(384, 442, 103, 23);
		contentPane.add(btExit);
		
		JLabel lbLog = new JLabel("Log (명령어 수행 관련) :");
		lbLog.setFont(new Font("굴림", Font.PLAIN, 12));
		lbLog.setBounds(20, 487, 141, 15);
		contentPane.add(lbLog);
		
		JList listinst = new JList();
		
		listinst.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		listinst.setBounds(263, 235, 109, 229);
		contentPane.add(listinst);
		
		JList listLog = new JList(new DefaultListModel());
		DefaultListModel model = (DefaultListModel) listLog.getModel();
//		listLog.add(new JScrollPane(listLog),"Center");
		
		listLog.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		listLog.setBounds(18, 508, 463, 116);
		contentPane.add(listLog);
		
		JScrollPane instScroll = new JScrollPane();
		instScroll.setBounds(351, 235, 20, 228);
		contentPane.add(instScroll);
		
		JScrollPane logScroll = new JScrollPane();
		logScroll.setBounds(457, 508, 24, 116);
		contentPane.add(logScroll);

	}
	class OpenActionListener implements ActionListener {
		JFileChooser chooser = new JFileChooser();
		OpenActionListener(){
			chooser = new JFileChooser();
		}
		public void actionPerformed(ActionEvent arg0) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Object File(*.obj)", "obj");
			chooser.setFileFilter(filter);
			int isOpen = chooser.showOpenDialog(null);
			if(isOpen != JFileChooser.APPROVE_OPTION){
				JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);	
				return;
			}
			String filePath = chooser.getSelectedFile().getPath();
			visual.load(chooser.getSelectedFile());
			openUpdate();
		}
		void openUpdate(){
			tfName.setText(chooser.getSelectedFile().getName());
			if(tfName.getText()!="\0") {
				btRunstep.setEnabled(true);
				btRunall.setEnabled(true);
				tfpName.setText(visual.sicLoader.rMgr.progName.get(0));
				tfHaddr.setText(String.format("%06X", visual.sicLoader.rMgr.startAddr.get(0)));
				int sum = 0;
				for(int i=0;i<visual.sicLoader.rMgr.progLength.size();i++) {
					sum += Integer.parseInt(visual.sicLoader.rMgr.progLength.get(i),16);
				}
				tfpLength.setText(String.format("%04X", sum));
				aDec.setText("0");
				aHex.setText("0");
				xDec.setText("0");
				xHex.setText("0");
				lDec.setText("0");
				lHex.setText("0");
				pcDec.setText("0");
				pcHex.setText("0");
				tfSW.setText("0");
				bDec.setText("0");
				bHex.setText("0");
				sDec.setText("0");
				sHex.setText("0");
				tDec.setText("0");
				tHex.setText("0");
				fDec.setText("0");
				fHex.setText("0");
				tfEAddr.setText("000000");
				
			}
		}
	}
	
}


