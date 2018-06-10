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
	String checkop;
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
	int memcount=0;
	public SicSimulator(ResourceManager resourceManager) {
		// �ʿ��ϴٸ� �ʱ�ȭ ���� �߰�
		this.rMgr = resourceManager;
		checkop = "";
		
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
		
			checkop = Integer.toHexString(rMgr.getMemory(rMgr.getRegister(regPC)) & opFlag);
			
			System.out.println(checkop);
			
			if(checkop.equals("14")) { //STL				
				
				memcount+=3;
//				rMgr.getMemory(memcount,)
				rMgr.setRegister(regPC, memcount);
				addLog("STL");
			}
			else if(checkop.equals("48")) { //JSUB
				memcount+=4;
				rMgr.setRegister(regPC, memcount);
				addLog("JSUB");
			}
			else if(checkop.equals("00")) { //LDA
				memcount+=3;
				rMgr.setRegister(regPC, memcount);
				addLog("LDA");
			}
			else if(checkop.equals("28")) { //COMP
				addLog("COMP");
			}
			else if(checkop.equals("30")) { //JEQ
				addLog("JEQ");
			}
			else if(checkop.equals("3C")) { //J
				addLog("J");
			}
			else if(checkop.equals("0C")) { //STA
				addLog("STA");
			}
			else if(checkop.equals("B4")) { //CLEAR
				addLog("CLEAR");
			}
			else if(checkop.equals("74")) { //LDT
				addLog("LDT");
			}
			else if(checkop.equals("E0")) { //TD
				addLog("TD");
			}
			else if(checkop.equals("D8")) { //RD
				addLog("RD");
			}
			else if(checkop.equals("A0")) { //COMPR
				addLog("COMPR");
			}
			else if(checkop.equals("B8")) { //TIXR
				addLog("TIXR");
			}
			else if(checkop.equals("10")) { //STX
				addLog("STX");
			}
			else if(checkop.equals("50")) { //LDCH
				addLog("LDCH");
			}
			else if(checkop.equals("38")) { //JLT
				
			}
			else if(checkop.equals("DC")) { //WD
				
			}
			else if(checkop.equals("4C")) { //RSUB
				
			}
			else if(checkop.equals("54")) { //STCH
				
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
