package SP18_simulator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSpinnerUI;

/**
 * �ùķ����ͷμ��� �۾��� ����Ѵ�. VisualSimulator���� ������� ��û�� ������ �̿� ����
 * ResourceManager�� �����Ͽ� �۾��� �����Ѵ�.  
 * 
 * �ۼ����� ���ǻ��� : <br>
 *  1) ���ο� Ŭ����, ���ο� ����, ���ο� �Լ� ������ �󸶵��� ����. ��, ������ ������ �Լ����� �����ϰų� ������ ��ü�ϴ� ���� ������ ��.<br>
 *  2) �ʿ信 ���� ����ó��, �������̽� �Ǵ� ��� ��� ���� ����.<br>
 *  3) ��� void Ÿ���� ���ϰ��� ������ �ʿ信 ���� �ٸ� ���� Ÿ������ ���� ����.<br>
 *  4) ����, �Ǵ� �ܼ�â�� �ѱ��� ��½�Ű�� �� ��. (ä������ ����. �ּ��� ���Ե� �ѱ��� ��� ����)<br>
 * 
 * <br><br>
 *  + �����ϴ� ���α׷� ������ ��������� �����ϰ� ���� �е��� ������ ��� �޺κп� ÷�� �ٶ��ϴ�. ���뿡 ���� �������� ���� �� �ֽ��ϴ�.
 */
public class SicSimulator {
	ResourceManager rMgr;
	public static final int opFlag = 0xFC;
	public static final int nFlag = 2;
	public static final int iFlag = 1;
	public static final int xFlag = 128;
	public static final int bFlag = 64;
	public static final int pFlag = 32;
	public static final int eFlag = 16;
	   
	public static final int regA = 0;
	public static final int regX = 1;
	public static final int regL = 2;
	public static final int regB = 3;
	public static final int regS = 4;
	public static final int regT = 5;
	public static final int regF = 6;
	public static final int regPC = 8;
	public static final int regSW = 9;
	ArrayList<String> addlog = new ArrayList<String>();
	
	int mem =0; //���� objectcode ���� ���� �� memory
	int cc =0; // JEQ, JGT, JLT �� �� �����ͼ� ���ϴ� regSW �� ��
	public SicSimulator(ResourceManager resourceManager) {
		// �ʿ��ϴٸ� �ʱ�ȭ ���� �߰�
		this.rMgr = resourceManager;		
	}

	/**
	 * ��������, �޸� �ʱ�ȭ �� ���α׷� load�� ���õ� �۾� ����.
	 * ��, object code�� �޸� ���� �� �ؼ��� SicLoader���� �����ϵ��� �Ѵ�. 
	 */
	public void load(File program) {
		String inst = "";
		/* �޸� �ʱ�ȭ, �������� �ʱ�ȭ ��*/
		rMgr.initializeResource();

	}

	/**
	 * 1���� instruction�� ����� ����� ���δ�.  
	 */
	public void oneStep() {
		int charmem = 0;
		char text = 0;
		int pc=0; // ���� ��ɾ��� PC Address
		boolean jump = false;

			String opcode = Integer.toHexString(rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & opFlag).toUpperCase();
			opcode = String.format("%02X", Integer.parseInt(opcode,16));
//			System.out.println("51 mem : "+rMgr.getMemory(51, 3)[0]);
//			System.out.println("52 mem : "+rMgr.getMemory(52, 3)[0]);
			boolean n = (rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & 0x02) == 0x02;
			boolean i = (rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & 0x01) == 0x01;
			boolean x = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x80) == 0x80;
			boolean b = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x40) == 0x40;
			boolean p = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x20) == 0x20;
			boolean e = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x10) == 0x10;

//			System.out.println(Integer.toHexString((int)rMgr.getMemory(rMgr.getRegister(regPC), 3)[0]));
//			System.out.println(Integer.toHexString((int)rMgr.getMemory(rMgr.getRegister(regPC), 3)[1]));
//			System.out.println(Integer.toHexString((int)rMgr.getMemory(rMgr.getRegister(regPC), 3)[2]));


			int m=0;
			int r1=0, r2=0;			
			int ta=0; // Target Address
			int disp = 0; // ���� object code �� displacement
			
	
			if(e) {
				
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 16;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff) << 8;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+3, 1)[0] & 0xff);
				
				if(disp>10000) {
					disp =0;
				}
				else {
//					System.out.println("displacement ã���ϴ�!! :"+disp);
				m = disp;
				ta = m;
				pc = rMgr.getRegister(regPC)+4;
				}

				if(x) {
					m += rMgr.getRegister(regX);
				}
			}
			else if(p) {
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 8;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff);
				pc = rMgr.getRegister(regPC)+3;
				if(disp>2048) {
					ta = pc+ (disp-4096);
				}
				else {
					ta = pc+disp;
				}

			}			

			if(!n && i) {
				//����
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 8;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff);
				ta = disp;
				rMgr.setMemory(disp, rMgr.intToChar(ta, 3));
				pc = rMgr.getRegister(regPC)+3;
			}
			else if(n && !i) {
				//����
//				ta = rMgr.getRegister(rMgr.getRegister(disp));
//				System.out.println("disp : "+disp);
				ta = rMgr.getMemory(disp, 3)[0];
//				System.out.println("where? :"+rMgr.getRegister(disp));
//				System.out.println("ta is :"+ta);
			}

			switch(opcode) {
			case "14": // STL
			
				rMgr.setMemory(ta, rMgr.intToChar(regL, 3));
				rMgr.setRegister(regPC, pc);
				
				addLog("STL");
				break;
				
			case "48" : //JSUB
			
				rMgr.setRegister(regL, pc);
				rMgr.setRegister(regPC, m);
				addLog("JSUB");
				break;
				
			case "B4" : //CLEAR

				r1 = (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0]) >> 4;	
				rMgr.setRegister(r1, 0);			
				pc = rMgr.getRegister(regPC)+2;
				rMgr.setRegister(regPC, pc);
				
				addLog("CLEAR");
				break;			
				
			case "74": //LDT
				if(e) { //4������ �� �޸� ó��
					mem = ta;
//					rMgr.setRegister(regT, ta);
				}
				else { //3������ �� �޸� ó��
					mem = rMgr.charToInt(rMgr.getMemory(ta, 3));
//					rMgr.setRegister(regT, rMgr.charToInt(rMgr.getMemory(ta, 3)));
				}
				rMgr.setRegister(regT, mem);
				rMgr.setRegister(regPC, pc);
				System.out.println("mem in LD2T :"+mem);
				addLog("LDT");
				break;
				
			case "E0": //TD
				mem = rMgr.charToInt(rMgr.getMemory(ta, 1));
				rMgr.setRegister(regSW, 100);
				rMgr.testDevice(Integer.toHexString(mem));
				rMgr.setRegister(regPC, pc);
				addLog("TD");
				break;				
			case "D8": //RD 
				text = rMgr.readDevice(Integer.toHexString(mem));
				rMgr.setRegister(regA, text);	
				rMgr.setRegister(regPC, pc);
				addLog("RD");
				break;
				
			case "A0": //COMPR
				r1 = rMgr.getRegister(rMgr.charToInt(rMgr.getMemory(rMgr.getRegister(regPC)+1, 1))>>4);
				r2 = rMgr.getRegister(rMgr.charToInt(rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)) & 0x0f);
				pc = rMgr.getRegister(regPC)+2;
				if(r1==r2) {
					rMgr.setRegister(regSW,0);
				}
				else if(r1<r2) {
					rMgr.setRegister(regSW, -1);
				}
				else {
					rMgr.setRegister(regSW, 1);
				}
				pc = rMgr.getRegister(regPC)+2;
				rMgr.setRegister(regPC, pc);
				
				addLog("COMPR");
				break;
				
			case "54": //STCH
					rMgr.setMemory(m, rMgr.intToChar(rMgr.getRegister(regA), 1));
					rMgr.setRegister(regPC, pc);
					
					addLog("STCH");
					break;
					
			case "50": //LDCH				
//				System.out.println("������ mem: "+(int)rMgr.getMemory(mem, 3)[0]);
				mem = rMgr.getMemory(m, 1)[0];
				rMgr.setRegister(regA, mem);
				rMgr.setRegister(regPC, pc);
								
				addLog("LDCH");
				break;
				
			case "B8": //TIXR	
					rMgr.setRegister(regX, rMgr.getRegister(regX)+1);
					if(rMgr.getRegister(regX)==rMgr.getRegister(regT)) {
						rMgr.setRegister(regSW, 0);
					}
					else if(rMgr.getRegister(regX)<rMgr.getRegister(regT)) {
						rMgr.setRegister(regSW, 5);
					}
					else {
						rMgr.setRegister(regSW, -5);
					}
					
					pc = rMgr.getRegister(regPC)+2;
					rMgr.setRegister(regPC, pc);
					
					addLog("TIXR");
					break;
					
			case "30": //JEQ
				
				if((rMgr.getRegister(regSW))==0) {
					rMgr.setRegister(regPC, ta);
//					System.out.println("regSW �� ?? "+rMgr.getRegister(regSW));
//					System.out.println("regPC �� ?? "+rMgr.getRegister(regPC));
				}
				else {
					rMgr.setRegister(regPC, pc);
				}
				addLog("JEQ");
				break;
				
			case "38": //JLT
//				System.out.println("����! "+rMgr.getRegister(regSW));
					if(rMgr.getRegister(regSW)<0) {
						rMgr.setRegister(regPC, ta);
					}
					else
						rMgr.setRegister(regPC, pc);
		
					addLog("JLT");
					break;
					
			case "10": //STX
					rMgr.setMemory(ta, rMgr.intToChar(regX, 3));
					rMgr.setRegister(regPC, pc);
					
					addLog("STX");
					break;
					
			case "4C": //RSUB
					rMgr.setRegister(regPC, rMgr.getRegister(regL));					
					addLog("RSUB");
					break;
					
			case "00": // LDA
				rMgr.setRegister(regA, rMgr.charToInt(rMgr.getMemory(ta, 3)));
				rMgr.setRegister(regPC, pc);
				
				addLog("LDA");
				
				break;
				
			case "0C": //STA
				rMgr.setMemory(ta, rMgr.intToChar(regA, 3));
				rMgr.setRegister(regPC, pc);
				addLog("STA");
				break;
				
			case "28": //COMP	
				if(rMgr.getRegister(regA)==ta) {
					rMgr.setRegister(regSW, 0);
				}
				else if(rMgr.getRegister(regA)<ta) {
					rMgr.setRegister(regSW, -1);
				}
				else {
					rMgr.setRegister(regSW, 1);
				}
				rMgr.setRegister(regPC, pc);
				
				addLog("COMP");
				break;
				
			case "DC": //WD
//				System.out.println("Memory : "+String.format("%X", (rMgr.getRegister(regA)&0xff)));
				rMgr.writeDevice(Integer.toHexString((int)rMgr.getMemory(ta, 1)[0]), (char)(rMgr.getRegister(regA) & 0xff));
				rMgr.setRegister(regPC,pc);
				addLog("WD");
				break;
				
			case "3C": //J
				rMgr.setRegister(regPC, ta);
				addLog("J");
				break;
				
			}
			
		
	}
	
	/**
	 * ���� ��� instruction�� ����� ����� ���δ�.
	 */
	public void allStep() {
		for(int i=0;i<4219;i++) {
			oneStep();
		}
	}
	
	/**
	 * �� �ܰ踦 ������ �� ���� ���õ� ����� ���⵵�� �Ѵ�.
	 */
	public void addLog(String log) {
		addlog.add(log);
		System.out.println("��ɾ� : "+log);
		
	}	
}
