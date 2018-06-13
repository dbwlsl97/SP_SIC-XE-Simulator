package SP18_simulator;

import java.io.File;
import java.io.IOException;

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
			boolean n = (rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & 0x02) == 0x02;
			boolean i = (rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & 0x01) == 0x01;
			boolean x = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x80) == 0x80;
			boolean b = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x40) == 0x40;
			boolean p = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x20) == 0x20;
			boolean e = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x10) == 0x10;

			System.out.println(Integer.toHexString((int)rMgr.getMemory(rMgr.getRegister(regPC), 3)[0]));
			System.out.println(Integer.toHexString((int)rMgr.getMemory(rMgr.getRegister(regPC), 3)[1]));
			System.out.println(Integer.toHexString((int)rMgr.getMemory(rMgr.getRegister(regPC), 3)[2]));


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
				System.out.println("disp : "+disp);
				ta = rMgr.getMemory(disp, 3)[0];
//				System.out.println("where? :"+rMgr.getRegister(disp));
				System.out.println("ta is :"+ta);
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
				}
				else { //3������ �� �޸� ó��
					mem = rMgr.charToInt(rMgr.getMemory(ta, 3));
				}
				
				rMgr.setRegister(regT, mem);
				rMgr.setRegister(regPC, pc);
				
				addLog("LDT");
				break;
				
			case "E0": //TD
				mem = rMgr.charToInt(rMgr.getMemory(ta, 1));

				rMgr.testDevice(Integer.toHexString(mem));
				rMgr.setRegister(regPC, pc);
				addLog("TD");
				break;
				
			case "30": //JEQ
//				rMgr.setRegister(regSW,-1);
				if(rMgr.getRegister(regSW)==-1) {
//				if(cc==-1) {
					ta = pc+disp;
					rMgr.setRegister(regPC, ta);
				}
					rMgr.setRegister(regPC, pc);
		
					
				addLog("JEQ");
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
					rMgr.setRegister(regSW,-1);
//					cc = -1;		
				}
				rMgr.setRegister(regPC, pc);
				
				addLog("COMPR");
				break;
				
			case "54": //STCH             A ������ ��𰬳�?????????????????????
//					System.out.println(rMgr.getRegister(text));
					rMgr.setMemory(m, rMgr.intToChar(rMgr.getRegister(regA), 1));
//					System.out.println(m);
//					System.out.println((int)rMgr.getMemory(m, 3)[0]);
					rMgr.setRegister(regPC, pc);
					charmem = m;
//					System.out.println(charmem);
					addLog("STCH");
					break;
					
			case "50": //LDCH

				mem = rMgr.getMemory(rMgr.getRegister(regA), 3)[0];
//				System.out.println("mem is "+mem);
				rMgr.setRegister(regA, mem);
				rMgr.setRegister(regPC, pc);
								
				addLog("LDCH");
				break;
				
			case "B8": //TIXR	
					rMgr.setRegister(regX, rMgr.getRegister(regX)+1);
	//				System.out.println("T is "+rMgr.getRegister(regT)+"\t X is "+rMgr.getRegister(regX));
					if(rMgr.getRegister(regT) < rMgr.getRegister(regX)) {
						rMgr.setRegister(regSW,-1);
	//					cc = -1;
					}
					pc = rMgr.getRegister(regPC)+2;
					rMgr.setRegister(regPC, pc);
					
					addLog("TIXR");
					break;
					
			case "38": //JLT
//					if(cc==-1) {
				if(rMgr.getRegister(regSW)==-1) {
						rMgr.setRegister(regPC, pc);
					}
					else {
						ta = pc+disp;
						rMgr.setRegister(regPC, ta);
					}
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

				mem = rMgr.charToInt(rMgr.getMemory(ta, 3));
				rMgr.setRegister(regA, mem);
//				pc = rMgr.getRegister(regPC)+3;
				rMgr.setRegister(regPC, pc);
//				System.out.println("reg PC : "+rMgr.getRegister(regPC)+" PC : "+pc);
				addLog("LDA");
				break;
				
			case "0C": //STA
				rMgr.setMemory(ta, rMgr.intToChar(regA, 3));
				rMgr.setRegister(regPC, pc);
				addLog("STA");
				break;
				
			case "28": //COMP
				if(rMgr.getRegister(regA)==disp) {
					rMgr.setRegister(regSW, -1);
//					cc = -1;
				}
				rMgr.setRegister(rMgr.getRegister(regA), ta);
				rMgr.setRegister(regPC, pc);
				
				addLog("COMP");
				break;
				
			case "DC": //WD
//				System.out.println("Memory : "+String.format("%X", (rMgr.getRegister(regA)&0xff)));
				rMgr.writeDevice(Integer.toHexString(mem), (char)(rMgr.getMemory(charmem, 3)[0] & 0xff));
				rMgr.setRegister(regPC,pc);
				addLog("WD");
				break;
				
			case "3C": //J
				System.out.println(Integer.toHexString((int)rMgr.getMemory(rMgr.getRegister(regPC), 3)[0]));
				System.out.println(Integer.toHexString((int)rMgr.getMemory(rMgr.getRegister(regPC), 3)[1]));
				System.out.println(Integer.toHexString((int)rMgr.getMemory(rMgr.getRegister(regPC), 3)[2]));

//				System.out.println("n : "+n+" i : "+ i);
//				rMgr.setRegister(regPC, ??));
				addLog("J");
				break;
				
			}
			
		
	}
	
	/**
	 * ���� ��� instruction�� ����� ����� ���δ�.
	 */
	public void allStep() {
	}
	
	/**
	 * �� �ܰ踦 ������ �� ���� ���õ� ����� ���⵵�� �Ѵ�.
	 */
	public void addLog(String log) {
		System.out.println("��ɾ�¿� ?? : "+log);
		
	}	
}
