package SP18_simulator;

import java.io.File;
import java.io.IOException;

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

	int mem =0; //현재 objectcode 에서 가져 온 memory
	int cc =0; // JEQ, JGT, JLT 할 때 가져와서 비교하는 regSW 의 값

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
			
			byte calbyte = 0;
			int m=0;
			int r1=0, r2=0;
			int locctr =0;
			boolean jump = false;
			int address = 0;
			int ta=0; // Target Address
			int pc=0; // 현재 명령어의 PC Address
			int disp = 0; // 현재 object code 의 displacement
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
				//간접
				
			}
			else if(i) {
				//직접
				
			}
//			else if(r1) {
//				//2형식
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
//				System.out.println("내 주소 : "+rMgr.getRegister(regPC)+"\t"+pc);
				mem = rMgr.charToInt(rMgr.getMemory(ta, 1));
				rMgr.testDevice(Integer.toHexString(mem));
				rMgr.setRegister(regPC, pc);
				
				addLog("TD");
				break;
				
			case "30": //JEQ
//				cc = rMgr.getRegister(regSW);
				System.out.println("cc뭐냐요??? "+cc);
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
	 * 남은 모든 instruction이 수행된 모습을 보인다.
	 */
	public void allStep() {
	}
	
	/**
	 * 각 단계를 수행할 때 마다 관련된 기록을 남기도록 한다.
	 */
	public void addLog(String log) {
		System.out.println("명령어는요 ?? : "+log);
		
	}	
}
