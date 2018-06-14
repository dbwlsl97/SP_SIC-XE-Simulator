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
	VisualSimulator visual;
	public static final int opFlag = 0xFC;
	public static final int regA = 0;
	public static final int regX = 1;
	public static final int regL = 2;
	public static final int regB = 3;
	public static final int regS = 4;
	public static final int regT = 5;
	public static final int regF = 6;
	public static final int regPC = 8;
	public static final int regSW = 9;
	
	boolean jump = false; // jump해서 처음 시작주소로 갔을 때 구분하는 변수
	int mem =0; //현재 objectcode 에서 가져 온 memory
	int target =0; //target address 
	public SicSimulator(ResourceManager resourceManager) {
		// 필요하다면 초기화 과정 추가
		this.rMgr = resourceManager;		
	}

	/**
	 * 레지스터, 메모리 초기화 등 프로그램 load와 관련된 작업 수행.
	 * 단, object code의 메모리 적재 및 해석은 SicLoader에서 수행하도록 한다. 
	 */
	public void load(File program) {
		/* 메모리 초기화, 레지스터 초기화 등*/
		rMgr.initializeResource();
	}

	/**
	 * 1개의 instruction이 수행된 모습을 보인다.  
	 */
	public void oneStep() {
			
			char text = 0; // COPY 할 TEXT 저장하는 변수
			int pc=0; // 현재 명령어의 PC Address
			int objcode =0; 
			/* memory 에서 opcode 따오기 */
			String opcode = Integer.toHexString(rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & opFlag).toUpperCase();
			opcode = String.format("%02X", Integer.parseInt(opcode,16));
			
			/* 가능한 n i x b p e 연산을 미리하고 opcode에 따라서 메모리나 레지스터 값 설정한다 */
			boolean n = (rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & 0x02) == 0x02;
			boolean i = (rMgr.getMemory(rMgr.getRegister(regPC), 1)[0] & 0x01) == 0x01;
			boolean x = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x80) == 0x80;
			boolean b = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x40) == 0x40;
			boolean p = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x20) == 0x20;
			boolean e = (rMgr.getMemory(rMgr.getRegister(regPC) + 1, 1)[0] & 0x10) == 0x10;

			int m=0; // ex) STL RETADR 중, m = RETADR
			int r1=0, r2=0; //register r1, r2 (2형식 명령어 처리할 때 사용)
			int ta=0; // Target Address 
			int disp = 0; // 현재 object code 의 displacement
			int format =0; // 명령어 format
				
			if(e) {	
				format = 4;
				
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 16;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff) << 8;
				disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+3, 1)[0] & 0xff);
				
				m = disp;
				ta = m;
				pc = rMgr.getRegister(regPC)+format;
				if(x) { 
					m += rMgr.getRegister(regX);
				}
			}
			else {
				format = 3;		
				
				if(p) {
					disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0] & 0x0f) << 8;
					disp |= (rMgr.getMemory(rMgr.getRegister(regPC)+2, 1)[0] & 0xff);
					pc = rMgr.getRegister(regPC)+format;
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
					pc = rMgr.getRegister(regPC)+format;
				}
				else if(n && !i) {
					//간접
					ta = rMgr.charToInt(rMgr.getMemory(disp, 3));
					}
			}
			/* Instructions JList 에 넣을 objcode 가져오기 */
			objcode = rMgr.charToInt(rMgr.getMemory(rMgr.getRegister(regPC), format));
			
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
				format = 2;
				objcode = rMgr.charToInt(rMgr.getMemory(rMgr.getRegister(regPC), format));
				r1 = (rMgr.getMemory(rMgr.getRegister(regPC)+1, 1)[0]) >> 4;	
				rMgr.setRegister(r1, 0);			
				pc = rMgr.getRegister(regPC)+format;
				rMgr.setRegister(regPC, pc);
				
				addLog("CLEAR");
				break;		
				
			case "74": //LDT
				mem = rMgr.charToInt(rMgr.getMemory(ta, 3));
				rMgr.setRegister(regT, mem);
				rMgr.setRegister(regPC, pc);
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
				format = 2;
				objcode = rMgr.charToInt(rMgr.getMemory(rMgr.getRegister(regPC), format));
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
				pc = rMgr.getRegister(regPC)+format;
				rMgr.setRegister(regPC, pc);
				
				addLog("COMPR");
				break;
				
			case "54": //STCH
				rMgr.setMemory(m, rMgr.intToChar(rMgr.getRegister(regA), 1));
				rMgr.setRegister(regPC, pc);
					
				addLog("STCH");
				break;
					
			case "50": //LDCH	
				mem = rMgr.getMemory(m, 3)[0];
				rMgr.setRegister(regA, mem);
				rMgr.setRegister(regPC, pc);
								
				addLog("LDCH");
				break;
				
			case "B8": //TIXR	
				format = 2;
				objcode = rMgr.charToInt(rMgr.getMemory(rMgr.getRegister(regPC), format));
				rMgr.setRegister(regX, rMgr.getRegister(regX)+1);
				if(rMgr.getRegister(regX)==rMgr.getRegister(regT)) {
					rMgr.setRegister(regSW, 0);
				}
				else if(rMgr.getRegister(regX)<rMgr.getRegister(regT)) {
					rMgr.setRegister(regSW, -5);
				}
				else {
					rMgr.setRegister(regSW, 5);
				}
				
				pc = rMgr.getRegister(regPC)+format;
				rMgr.setRegister(regPC, pc);
				
				addLog("TIXR");
				break;
					
			case "30": //JEQ
				if((rMgr.getRegister(regSW))==0) {
					rMgr.setRegister(regPC, ta);
				}
				else {
					rMgr.setRegister(regPC, pc);
				}
				addLog("JEQ");
				break;
				
			case "38": //JLT
					if(rMgr.getRegister(regSW)<0) {
						rMgr.setRegister(regPC, ta);
					}
					else {
						rMgr.setRegister(regPC, pc);
					}
					addLog("JLT");
					break;
					
			case "10": //STX
					rMgr.setMemory(ta, rMgr.intToChar(rMgr.getRegister(regX), 3));
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
				rMgr.writeDevice(Integer.toHexString((int)rMgr.getMemory(ta, 1)[0]), (char)(rMgr.getRegister(regA) & 0xff));
				rMgr.setRegister(regPC,pc);
				addLog("WD");
				break;
				
			case "3C": //J
				rMgr.setRegister(regPC, ta);
				addLog("J");
				
				if(rMgr.getRegister(regPC)==0) { // regPC 가 0을 가리킬 때, jump 변수를 true로 변경하고, 프로그램을 종료함
					jump = true;
					rMgr.closeDevice();
				}
				break;
			}
			/* objcode가 0부터 시작하는 objcode 구분해서 알맞게 넣어주기 */
			if((Integer.parseInt(opcode,16) & 0xf0) == 0) {
				addInst("0"+String.format("%X", objcode));
			}
			else
				addInst(String.format("%X", objcode));
			
			target = ta;
	}
	
	/**
	 * 남은 모든 instruction이 수행된 모습을 보인다.
	 */
	public void allStep() {
		while(jump!=true) {
			oneStep();
		}
	}
	
	/**
	 * 명령어마다 Log 넣은 거 Log JList에 추가하기
	 */
	public void addLog(String log) {
		visual.myframe.instModel.addElement(log);
	}
	
	/**
	 * 명령어마다 objcode opcode JList에 추가하기
	 */
	public void addInst(String opcode) {
		visual.myframe.opcodeModel.addElement(opcode);
	}
}
