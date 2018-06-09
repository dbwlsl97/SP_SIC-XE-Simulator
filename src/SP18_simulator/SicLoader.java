package SP18_simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * SicLoader�� ���α׷��� �ؼ��ؼ� �޸𸮿� �ø��� ������ �����Ѵ�. �� �������� linker�� ���� ���� �����Ѵ�. 
 * <br><br>
 * SicLoader�� �����ϴ� ���� ���� ��� ������ ����.<br>
 * - program code�� �޸𸮿� �����Ű��<br>
 * - �־��� ������ŭ �޸𸮿� �� ���� �Ҵ��ϱ�<br>
 * - �������� �߻��ϴ� symbol, ���α׷� �����ּ�, control section �� ������ ���� ���� ���� �� ����
 */
public class SicLoader {
	ResourceManager rMgr;
	SymbolTable symtab;
	public SicLoader(ResourceManager resourceManager) {
		// �ʿ��ϴٸ� �ʱ�ȭ
		setResourceManager(resourceManager);
		symtab = new SymbolTable();
	}
	/**
	 * Loader�� ���α׷��� ������ �޸𸮸� �����Ų��.
	 * @param rMgr
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.rMgr=resourceManager;
	}
	/**
	 * object code�� �о load������ �����Ѵ�. load�� �����ʹ� resourceManager�� �����ϴ� �޸𸮿� �ö󰡵��� �Ѵ�.
	 * load�������� ������� symbol table �� �ڷᱸ�� ���� resourceManager�� �����Ѵ�.
	 * @param objectCode �о���� ����
	 */
	public void load(File objectCode){ 
			int currentSection = 0;
		    String line;
		    String[] name;
		    String[] define;
		    int mem =0;
		    int tmem =0;
		    int secaddr = 0;  //���� ���� �ּ�  ����
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
				    	rMgr.setStartADDR(Integer.parseInt(rMgr.progLength.get(currentSection),16)+secaddr);
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
				    case 'E':
				    	secaddr += Integer.parseInt(rMgr.progLength.get(currentSection),16);
				    	currentSection++;
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
				    			tmem1 -= 'a';
				    		}
				    		else if(tmem2>='A'&&tmem2<='F') {
				    			tmem2 -= 'a';
				    		}
				    		
				    		rMgr.memory[mem]+=tmem1<<4;
				    		rMgr.memory[mem]+=tmem2;
				    		i+=2;
				    		mem++;
	    						    		
				    	}
				    	break;
				    }
		    	  }
		    	   		  
		      }
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    //pass 2
		    try {
		    	int zerocount=0;
		    	int pacount=0;
		    	String[] ref;
		    	br = new BufferedReader(new InputStreamReader(new FileInputStream(objectCode)));
		    	
			      while((line = br.readLine()) != null){
			    	  if(line.length() != 0) {
					    switch(line.charAt(0)) {
					    case 'M':
					    	zerocount = Integer.parseInt(String.valueOf(line.charAt(8)),16);
					    	pacount =Integer.parseInt(String.valueOf(line.charAt(6)),16);
					    	
//					    	for(int i=0;i<rMgr.progName.size();i++) {
//					    		if(line.substring(10)==rMgr.progName.get(i)) {
//							    	System.out.println(rMgr.startAddr.get(currentSection));
//					    		}
//					    	}

//					    	System.out.println(rMgr.startAddr.get(currentSection));
//					    	rMgr.memory[pacount+1] << ���⿡ RDREC �� �� �־��ֱ�
					    	break;
					    	
					    }
			    	  }
			      }

		    } catch(IOException e) {
		    	e.printStackTrace();
		    }
		    


	};
	
	

}
/*
 * 				    case 'R':
				    	break; 
				    case 'T':
				    	mem = Integer.parseInt(line.substring(1, 7),16) + rMgr.startAddr.get(currentSection);
				    	for(int i=0;i<line.substring(9).length();i++) {
				    		rMgr.memory += line.substring(9+i);
				    	}
				    	break;
				    case 'M':
				    	break;
				    	*/
//tmem = Integer.parseInt(line.substring(9+i,10+i),16);
//rMgr.memory[mem]+=(char)tmem;
//tmem = Integer.parseInt(line.substring(9).split("\0"),16);
//tmem = Integer.parseInt(line.substring(9+i),16);
//rMgr.memory[mem] += (char)tmem;
