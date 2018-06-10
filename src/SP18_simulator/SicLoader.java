package SP18_simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * SicLoader는 프로그램을 해석해서 메모리에 올리는 역할을 수행한다. 이 과정에서 linker의 역할 또한 수행한다. 
 * <br><br>
 * SicLoader가 수행하는 일을 예를 들면 다음과 같다.<br>
 * - program code를 메모리에 적재시키기<br>
 * - 주어진 공간만큼 메모리에 빈 공간 할당하기<br>
 * - 과정에서 발생하는 symbol, 프로그램 시작주소, control section 등 실행을 위한 정보 생성 및 관리
 */
public class SicLoader {
	ResourceManager rMgr;
	SymbolTable symtab;
	ArrayList<String> mcode = new ArrayList<String>();
	ArrayList<Integer> section = new ArrayList<Integer>();
	public SicLoader(ResourceManager resourceManager) {
		// 필요하다면 초기화
		setResourceManager(resourceManager);
		symtab = new SymbolTable();
	}
	/**
	 * Loader와 프로그램을 적재할 메모리를 연결시킨다.
	 * @param rMgr
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.rMgr=resourceManager;
	}
	/**
	 * object code를 읽어서 load과정을 수행한다. load한 데이터는 resourceManager가 관리하는 메모리에 올라가도록 한다.
	 * load과정에서 만들어진 symbol table 등 자료구조 역시 resourceManager에 전달한다.
	 * @param objectCode 읽어들인 파일
	 */
	public void load(File objectCode){ 
			int currentSection = 0;
		    String line;
		    String[] name;
		    String[] define;
		    int mem =0;
		    int tmem =0;
		    int secaddr = 0;  //이전 시작 주소  변수
		    BufferedReader br = null;
		    // pass 1
		    try{
		      br = new BufferedReader(new InputStreamReader(new FileInputStream(objectCode)));
		      
		      while((line = br.readLine()) != null){
		    	  if(line.length() != 0) {
				    switch(line.charAt(0)) {
				    case 'H':
				    	name = line.substring(1).split("\t");
				    	rMgr.setProgname(name[0], currentSection);
				    	rMgr.setProgLength(name[1].substring(6), currentSection);
				    	if(currentSection!=0) {
				    		rMgr.setStartADDR(Integer.parseInt(name[1].substring(0, 6),16)+secaddr);
				    	}
				    	else {
				    		rMgr.setStartADDR(Integer.parseInt(name[1].substring(0,6)));
				    	}
				    	break;
				    case 'D':
				    	define = line.substring(1).split(" ");
				    	for(int i=0;i<define.length;) {
				    		symtab.putSymbol(define[i], Integer.parseInt(define[i+1],16));
				    		i+=2;
				    	}
				    	break;		    
				    case 'T':
				    	mem = Integer.parseInt(line.substring(1, 7),16) + rMgr.startAddr.get(currentSection);
				    	int tmem1=0, tmem2=0;
				    	for(int i=0;i<line.substring(9).length();) {
				    		
				    		tmem1 = Integer.parseInt(String.valueOf(line.charAt(9+i)),16);
				    		tmem2 = Integer.parseInt(String.valueOf(line.charAt(10+i)),16);
				    		
				    		if(tmem1>='0'&&tmem1<='9') {
				    			tmem1 -= '0';
				    		}
				    		else if(tmem2>='0'&&tmem2<='9') {
				    			tmem2 -= '0';
				    		}
				    		else if(tmem1>='A'&&tmem1<='F') {
				    			tmem1 -= 'A';
				    		}
				    		else if(tmem2>='A'&&tmem2<='F') {
				    			tmem2 -= 'A';
				    		}
				    		tmem1 = tmem1<<4;
				    		
				    		rMgr.setMemory(mem, (char)(tmem1+tmem2));
				    		
				    		i+=2;
				    		mem++;
	    						    		
				    	}
				    	break;
				    case 'M':
				    	mcode.add(line);
//				    	if(currentSection==0) {
//				    		section.add(currentSection);
//				    	}
//				    	name = line.substring(1).split("\t");
//				    	rMgr.setProgname(name[0], currentSection);
//				    	rMgr.setProgLength(name[1].substring(6), currentSection);
//				    	if(currentSection!=0) {
//				    		rMgr.setStartADDR(Integer.parseInt(name[1].substring(0, 6),16)+secaddr);
//				    	}
//				    	else {
//				    		rMgr.setStartADDR(Integer.parseInt(name[1].substring(0,6)));
//				    	}
				    	break;
				    case 'E':
				    	
				    	secaddr += Integer.parseInt(rMgr.progLength.get(currentSection),16);
				    	currentSection++;
				    	
				    	break;	
				    }
		    	  }
		    	   		  
		      }
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    //pass 2

		    try {

		    	currentSection =0;
		    	int pacount=0;
		    	String refleng="";
		    	int mmem1=0, mmem2=0;
		    	int mmem3=0;
		    	int gmem1=0, gmem2=0;
		    	int gmem3=0;
		    	String[] ref;
		    	br = new BufferedReader(new InputStreamReader(new FileInputStream(objectCode)));
		    	
			      while((line = br.readLine()) != null){
			    	  if(line.length() != 0) {
					    switch(line.charAt(0)) {
					    case 'M':				    	
					    	pacount = Integer.parseInt(line.substring(5,7),16);
					    	
					    	if(line.contains("RDREC")||line.contains("WRREC")) {
					    		for(int i=0;i<rMgr.progName.size();i++) {
					    			if(rMgr.progName.get(i).equals(line.substring(10))) {
					    				refleng = Integer.toHexString(rMgr.startAddr.get(i));
					    				break;
					    			}
					    		}
					    	}
					    	else {
					    		//심볼테이블에서 찾아주는 아이들
					    		refleng = Integer.toHexString(symtab.search(line.substring(10)));
					    		refleng = String.format("%04X",(Integer.parseInt(refleng,16)));
		
					    	}

						    	for(int i=0;i<4;) {

						    		mmem1 = Integer.parseInt(String.valueOf(refleng.charAt(i)),16);					    		
						    		mmem2 = Integer.parseInt(String.valueOf(refleng.charAt(i+1)),16);
						    		
						    		if(mmem1>='0'&&mmem1<='9') {
						    			mmem1 -= '0';
						    		}
						    		else if(mmem2>='0'&&mmem2<='9') {
						    			mmem2 -= '0';
						    		}
						    		else if(mmem1>='A'&&mmem1<='F') {
						    			mmem1 -= 55;
						    		}
						    		else if(mmem2>='A'&&mmem2<='F') {
						    			mmem2 -= 55;
						    		}
						    		gmem1 = rMgr.getMemory(pacount+1);
						    		gmem2 = rMgr.getMemory(pacount+2);
//						    		if(line.charAt(9)=='+') {
//						    			gmem1 += mmem1<<4;
//							    		gmem2 += mmem2;
////							    		System.out.println(pacount+1);
//						    			rMgr.setMemory(pacount+1, (char)(gmem1+gmem2));
//						    		}
//						    		else if(line.charAt(9)=='-') {
//						    			gmem1 -= mmem1<<4;
//							    		gmem2 -= mmem2;
////							    		System.out.println(pacount+1);
//						    			rMgr.setMemory(pacount+1, (char)(gmem1+gmem2));
//						    		}
						    		i+=2;
						    		pacount++;
						    	}
					    	break;
				    	
					    case 'E':
					    	currentSection++;
					    	break;
					    }
					    
			    	  }
			      }
			      for(int i=0;i<4219;i++)
			    		System.out.println(Integer.toHexString(i)+"\t"+Integer.toHexString((int)rMgr.memory[i]));


		    } catch(IOException e) {
		    	e.printStackTrace();
		    }
		    


	};	

}
//if(line.contains("+")) {
//gmem1 += tmem1;
//gmem2 += tmem2;
////System.out.println(pacount+1);
//rMgr.setMemory(pacount+1, (char)(gmem1+gmem2));
//}
//else if(line.contains("-")) {
//gmem1 -= tmem1;
//gmem2 -= tmem2;
////System.out.println(pacount+1);
//rMgr.setMemory(pacount+1, (char)(gmem1+gmem2));
//}
