package SP18_simulator;

import java.io.File;

/**
 * 시뮬레이터로서의 작업을 담당한다. VisualSimulator에서 사용자의 요청을 받으면 이에 따라
 * ResourceManager에 접근하여 작업을 수행한다.  
 * 
 * 작성중의 유의사항 : <br>
 *  1) 새로운 클래스, 새로운 변수, 새로운 함수 선언은 얼마든지 허용됨. 단, 기존의 변수와 함수들을 삭제하거나 완전히 대체하는 것은 지양할 것.<br>
 *  2) 필요에 따라 예외처리, 인터페이스 또는 상속 사용 또한 허용됨.<br>
 *  3) 모든 void 타입의 리턴값은 유저의 필요에 따라 다른 리턴 타입으로 변경 가능.<br>
 *  4) 파일, 또는 콘솔창에 한글을 출력시키지 말 것. (채점상의 이유. 주석에 포함된 한글은 상관 없음)<br>
 * 
 * <br><br>
 *  + 제공하는 프로그램 구조의 개선방법을 제안하고 싶은 분들은 보고서의 결론 뒷부분에 첨부 바랍니다. 내용에 따라 가산점이 있을 수 있습니다.
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
	int pcaddr =0; // 현재 명령어의 PC address
	int temp = 0; // displacement 값 만 가져오기 위한 임시 변수 
	int move =0; // displacement 에서 가장 앞에있는 값(<<8해줘야함)
	char[] cti = new char[1]; //CharToInt 가 필요한 displacement
	public SicSimulator(ResourceManager resourceManager) {
		// 필요하다면 초기화 과정 추가
		this.rMgr = resourceManager;

		
	}

	/**
	 * 레지스터, 메모리 초기화 등 프로그램 load와 관련된 작업 수행.
	 * 단, object code의 메모리 적재 및 해석은 SicLoader에서 수행하도록 한다. 
	 */
	public void load(File program) {
		String inst = "";
		/* 메모리 초기화, 레지스터 초기화 등*/
		rMgr.initializeResource();

	}

	/**
	 * 1개의 instruction이 수행된 모습을 보인다. 
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
				
				pcaddr = (rMgr.getRegister(regPC))+3; //현재 pc addr
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
				temp = rMgr.getMemory(rMgr.getRegister(regPC)+1,1)[0]&0xF0; //e = 1 이면 4형식이므로 확장 처리
			
				if(temp == 0x10) {
					pcaddr = (rMgr.getRegister(regPC)+4);
				}
				rMgr.setRegister(regL, rMgr.getRegister(regPC)); //regPC+4 못했음
				
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
	 * 남은 모든 instruction이 수행된 모습을 보인다.
	 */
	public void allStep() {
	}
	
	/**
	 * 각 단계를 수행할 때 마다 관련된 기록을 남기도록 한다.
	 */
	public void addLog(String log) {
		
	}	
}
