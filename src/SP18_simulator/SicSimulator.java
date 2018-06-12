package SP18_simulator;

import java.io.File;
import java.io.IOException;

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
			boolean n = (rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & 0x02) == 0x02;
			boolean i = (rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & 0x01) == 0x01;
			boolean x = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x80) == 0x80;
			boolean b = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x40) == 0x40;
			boolean p = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x20) == 0x20;
			boolean e = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x10) == 0x10;
			
			byte calbyte = 0;
			int m=0;
			int r1=0, r2=0;
			int locctr =0;
			boolean jump = false;
			int address = 0;
			int ta=0; // Target Address
			int pc=0; // ���� ��ɾ��� PC Address
			int disp = 0; // ���� object code �� displacement
			char text = 0;
			String opcode = Integer.toHexString(rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & opFlag).toUpperCase();

			
			if(e) {
				
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 16;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff) << 8;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+3, 1)[0] & 0xff);

				m = disp;

				pc = rMgr.getRegister(regPC)+4;
				
			}
			else if(p) {
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 8;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff);

//				System.out.println("disp : "+disp);
				pc = rMgr.getRegister(regPC)+3;

				ta = disp + pc;

			}


			else if(n) {
				//����
				
			}
			else if(i) {
				//����
				
			}
//			else if(r1) {
//				//2����
//			}

//			System.out.println("pc : "+rMgr.getRegister(regPC)+"\t"+pc);
//			System.out.println("opcode :"+opcode);
			System.out.println("cc : "+cc);
			if(disp>2048) {
				disp -= 4096;
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
				System.out.println("My ta is "+ta);
//				System.out.println((int)rMgr.getMemory(ta, 3)[0]+" "+(int)rMgr.getMemory(ta, 3)[1]+" "+(int)rMgr.getMemory(ta, 3)[2]);
				mem = rMgr.charToInt(rMgr.getMemory(ta, 3));
				System.out.println("MY MEM IS :"+mem);
				rMgr.setRegister(regT, mem);
				rMgr.setRegister(regPC, pc);
				
				addLog("LDT");
				break;
				
			case "E0": //TD
//				System.out.println("�� �ּ� : "+rMgr.getRegister(regPC)+"\t"+pc);
				mem = rMgr.charToInt(rMgr.getMemory(ta, 1));
				rMgr.testDevice(Integer.toHexString(mem));
				rMgr.setRegister(regPC, pc);
				
				addLog("TD");
				break;
				
			case "30": //JEQ
//				cc = rMgr.getRegister(regSW);
				System.out.println("cc���Ŀ�??? "+cc);
				if(cc==-1) {
					ta = pc+disp;
					rMgr.setRegister(regPC, ta);
					System.out.println("ta is "+ta);
				}
				else
					rMgr.setRegister(regPC, pc);
				
				addLog("JEQ");
				break;
			case "D8": //RD 
				text = rMgr.readDevice(Integer.toHexString(mem));
				System.out.println("text : "+ (int)text);
				rMgr.setRegister(regA, text);	
				rMgr.setRegister(regPC, pc);
			//	System.out.println(rMgr.getRegister(text));
				addLog("RD");
				break;
			case "A0": //COMPR
				r1 = rMgr.getRegister(rMgr.charToInt(rMgr.getMemory(rMgr.getRegister(regPC)+1, 1))>>4);
				r2 = rMgr.getRegister(rMgr.charToInt(rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)) & 0x0f);
				pc = rMgr.getRegister(regPC)+2;
				System.out.println("r1: "+r1+" r2 : "+r2);
				if(r1==r2) {
					cc = -1;
					
				}
				
				rMgr.setRegister(regPC, pc);
				
				addLog("COMPR");
				break;
				
			case "54": //STCH
					rMgr.setMemory(m, rMgr.intToChar(rMgr.getRegister(text), 1));
					rMgr.setRegister(regPC, pc);
					
					addLog("STCH");
					break;
			case "B8": //TIXR	
					rMgr.setRegister(regX, rMgr.getRegister(regX)+1);
					System.out.println("T is "+rMgr.getRegister(regT)+"\t X is "+rMgr.getRegister(regX));
					if(rMgr.getRegister(regT) < rMgr.getRegister(regX)) {
						cc = -1;
					}
					pc = rMgr.getRegister(regPC)+2;
					rMgr.setRegister(regPC, pc);
					
					addLog("TIXR");
					break;
			case "38": //JLT
					if(cc==-1) {
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
					System.out.println("L register : "+rMgr.getRegister(regL));
					
					addLog("RSUB");
					break;
			case "00": // LDA
				
				addLog("LDA");
				break;
			
			case "28": //COMP
				
				addLog("COMP");
				break;
			case "DC": //WD
				
				addLog("WD");
				break;
			case "3C": //J
				
				addLog("J");
				break;
				
			}
				
			
				
//			if(opcode.equals("28")) { //COMP
//				addLog("COMP");
//			}

//			else if(opcode.equals("3C")) { //J
//				addLog("J");
//			}
//			else if(opcode.equals("0C")) { //STA
//				addLog("STA");
//			}
//			}
//			else if(opcode.equals("74")) { //LDT
//				addLog("LDT");
//			}
//			else if(opcode.equals("10")) { //STX
//				addLog("STX");
//			}
//			else if(opcode.equals("50")) { //LDCH
//				addLog("LDCH");
//			}
//			else if(opcode.equals("38")) { //JLT
//				
//			}
//			else if(opcode.equals("DC")) { //WD
//				
//			}
//			else if(opcode.equals("4C")) { //RSUB
//				
//			}
//			else if(opcode.equals("54")) { //STCH
//				
//			}
			
		
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
