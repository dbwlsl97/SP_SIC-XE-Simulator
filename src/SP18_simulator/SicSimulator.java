package SP18_simulator;

import java.io.File;

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
	int ta=0;
	int pcaddr =0; // ���� ��ɾ��� PC address
	int temp = 0; // displacement �� �� �������� ���� �ӽ� ���� 
	int move =0; // displacement ���� ���� �տ��ִ� ��(<<8�������)
	char[] cti = new char[1]; //CharToInt �� �ʿ��� displacement
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
			String opcode = Integer.toHexString(rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & opFlag);
			byte calbyte = 0;
			int m=0;
			int r1, r2=0;
			int locctr =0;
			boolean jump = false;
			int address = 0;
			if(e) {
				address |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 16;
				address |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff) <<8;
				address |= (rMgr.getMemory(rMgr.getRegister(regPC)+3, 1)[0] & 0xff);
				
			}
			else if(p) {
				
			}
			
			if(opcode.equals("14")) { //STL				
				
				pcaddr = (rMgr.getRegister(regPC))+3; //���� pc addr
				temp = rMgr.getMemory(rMgr.getRegister(regPC)+1,1)[0]&0x0F;
				move = temp << 8;
				cti[0] = rMgr.getMemory(rMgr.getRegister(regPC)+2,1)[0];
				move += rMgr.charToInt(cti);
				ta = pcaddr + move;
				
				rMgr.setMemory(ta, (char)((regL>>16) & 15));
				rMgr.setMemory(ta+1, (char)((regL>>8) & 15));
				rMgr.setMemory(ta+2, (char)(regL & 15));
				
				rMgr.setRegister(regPC, pcaddr);
				System.out.println(pcaddr);
				
				addLog("STL");
			}
			else if(opcode.equals("48")) { //JSUB
				temp = rMgr.getMemory(rMgr.getRegister(regPC)+1,1)[0]&0xF0; //e = 1 �̸� 4�����̹Ƿ� Ȯ�� ó��
			
				if(temp == 0x10) {
					pcaddr = (rMgr.getRegister(regPC)+4);
				}
				rMgr.setRegister(regL, rMgr.getRegister(regPC)); //regPC+4 ������
				
//				if(rMgr.getMemory(rMgr.getRegister(regPC)+2) {
//				rMgr.setRegister(regPC );					
//				}
				System.out.println(pcaddr);

//				rMgr.setRegister(regPC, rMgr.progLent);
				addLog("JSUB");
			}
			else if(opcode.equals("00")) { //LDA
			
				pcaddr = rMgr.getRegister(regPC)+3;
				temp = rMgr.getMemory(rMgr.getRegister(regPC)+1,1)[0]&0x0F;
				move = temp << 8;
				cti[0] = rMgr.getMemory(rMgr.getRegister(regPC)+2,1)[0];
				move += rMgr.charToInt(cti);
				ta = pcaddr + move;
				
				rMgr.setMemory(ta, (char)((regL>>16) & 15));
				rMgr.setMemory(ta+1, (char)((regL>>8) & 15));
				rMgr.setMemory(ta+2, (char)(regL & 15));
				
				rMgr.setRegister(regPC, pcaddr);
				addLog("LDA");
			}
			else if(opcode.equals("28")) { //COMP
				addLog("COMP");
			}
			else if(opcode.equals("30")) { //JEQ
				addLog("JEQ");
			}
			else if(opcode.equals("3C")) { //J
				addLog("J");
			}
			else if(opcode.equals("0C")) { //STA
				addLog("STA");
			}
			else if(opcode.equals("B4")) { //CLEAR
				addLog("CLEAR");
			}
			else if(opcode.equals("74")) { //LDT
				addLog("LDT");
			}
			else if(opcode.equals("E0")) { //TD
				addLog("TD");
			}
			else if(opcode.equals("D8")) { //RD
				addLog("RD");
			}
			else if(opcode.equals("A0")) { //COMPR
				addLog("COMPR");
			}
			else if(opcode.equals("B8")) { //TIXR
				addLog("TIXR");
			}
			else if(opcode.equals("10")) { //STX
				addLog("STX");
			}
			else if(opcode.equals("50")) { //LDCH
				addLog("LDCH");
			}
			else if(opcode.equals("38")) { //JLT
				
			}
			else if(opcode.equals("DC")) { //WD
				
			}
			else if(opcode.equals("4C")) { //RSUB
				
			}
			else if(opcode.equals("54")) { //STCH
				
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
		
	}	
}
