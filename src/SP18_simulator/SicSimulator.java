package SP18_simulator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSpinnerUI;

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
	ArrayList<String> addlog = new ArrayList<String>();
	
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
		int charmem = 0;
		char text = 0;
		int pc=0; // 현재 명령어의 PC Address
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
			int disp = 0; // 현재 object code 의 displacement
			
	
			if(e) {
				
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 16;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff) << 8;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+3, 1)[0] & 0xff);
				
				if(disp>10000) {
					disp =0;
				}
				else {
//					System.out.println("displacement 찾습니당!! :"+disp);
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
				//직접
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 8;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff);
				ta = disp;
				rMgr.setMemory(disp, rMgr.intToChar(ta, 3));
				pc = rMgr.getRegister(regPC)+3;
			}
			else if(n && !i) {
				//간접
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
				if(e) { //4형식일 때 메모리 처리
					mem = ta;
//					rMgr.setRegister(regT, ta);
				}
				else { //3형식일 때 메모리 처리
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
//				System.out.println("문제의 mem: "+(int)rMgr.getMemory(mem, 3)[0]);
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
//					System.out.println("regSW 값 ?? "+rMgr.getRegister(regSW));
//					System.out.println("regPC 값 ?? "+rMgr.getRegister(regPC));
				}
				else {
					rMgr.setRegister(regPC, pc);
				}
				addLog("JEQ");
				break;
				
			case "38": //JLT
//				System.out.println("헤이! "+rMgr.getRegister(regSW));
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
	 * 남은 모든 instruction이 수행된 모습을 보인다.
	 */
	public void allStep() {
		for(int i=0;i<4219;i++) {
			oneStep();
		}
	}
	
	/**
	 * 각 단계를 수행할 때 마다 관련된 기록을 남기도록 한다.
	 */
	public void addLog(String log) {
		addlog.add(log);
		System.out.println("명령어 : "+log);
		
	}	
}
