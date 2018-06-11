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
	ArrayList<String> mcode = new ArrayList<String>();
	ArrayList<Integer> section = new ArrayList<Integer>();
	int currentSection;
	int pacount=0;
	public SicLoader(ResourceManager resourceManager) {
		// �ʿ��ϴٸ� �ʱ�ȭ
		setResourceManager(resourceManager);
		symtab = new SymbolTable();
		currentSection =0;
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
				    		rMgr.setStartADDR(Integer.parseInt(name[1].substring(0, 6),16)+secaddr);
				    		symtab.putSymbol(name[0],Integer.parseInt(name[1].substring(0, 6),16)+secaddr);
				    	}
				    	else {
				    		rMgr.setStartADDR(Integer.parseInt(name[1].substring(0,6)));
				    		symtab.putSymbol(name[0],Integer.parseInt(name[1].substring(0,6)));
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
				    	int ttmem=0, tmem1=0, tmem2=0;
				    	for(int i=0;i<line.substring(9).length();) {
				  //  		ttmem = Integer.parseInt(line.substring(9+i,11+i),16);
				    		
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
		    int count=0;
		    int temp=0;
		    int save =0;
//		    int refleng=0;
		    for(int i=0;i<mcode.size();i++) {
		    	int a=0, b=0;
		    	pacount = (Integer.parseInt(mcode.get(i).substring(5,7),16)+1);
		    	String refleng = String.format("%04X", symtab.search(mcode.get(i).substring(10)));
//		    	String refleng = symtab.search(mcode.get(i).substring(10));
//		    	System.out.println(refleng);
		    	a = rMgr.memory[pacount+1];
		    	a = a<<8;
		    	b = rMgr.memory[pacount+2];
		    	a |= b;
		    	if(a != 0) {
		    		a = 0;
		    	}
		    		    	
		    	for(int c=0;c<4;c++) {
		
		    		if(refleng.charAt(c)>='0'&&refleng.charAt(c)<='9') {
		    			temp += (char)(refleng.charAt(c) - '0');
		    		}
		    		else if(refleng.charAt(c)>='A'&&refleng.charAt(c)<='F') {
		    			temp += (char)(refleng.charAt(c) - 55);
		    		}
		    		if(count==0) {
		    			temp = (char)(temp<<4);
		    			count=1;
		    		}
		    		else if(count==1) {

				    	if(mcode.get(i).charAt(9)=='+') {
				    		rMgr.setMemory(pacount, (char)(rMgr.getMemory(pacount)+temp ));
				    	}
				    	else if(mcode.get(i).charAt(9)=='-') {
				    		rMgr.setMemory(pacount, (char)(rMgr.getMemory(pacount)-temp ));
				    	}
				    
//		    		rMgr.setMemory(pacount+1, (char)save);
		    		pacount++;
		    		temp = (char)(count=0);
		    		}
		    	}
		    
		    	}
	          for(int i=0;i<4219;i++)
  		System.out.println(Integer.toHexString(i)+"\t"+Integer.toHexString((int)rMgr.memory[i]));

		    	

//	          		    try {
//		    	currentSection =0;
//		    	int pacount=0;
//		    	String refleng="";
//		    	int mmem1=0, mmem2=0;
//		    	int gmem1=0, gmem2=0;
//		    	int realaddr=0;
//		    	String[] ref;
//		    	br = new BufferedReader(new InputStreamReader(new FileInputStream(objectCode)));
//		    	
//			      while((line = br.readLine()) != null){
//			    	  if(line.length() != 0) {
//					    switch(line.charAt(0)) {
//					    case 'M':				    	
//					    	pacount = Integer.parseInt(line.substring(5,7),16)+1;
//					    	
//
//					    		//�ɺ����̺��� ã���ִ� ���̵�
//					    		refleng = Integer.toHexString(symtab.search(line.substring(10)));
//					    		refleng = String.format("%04X",(Integer.parseInt(refleng,16)));
//		//			    		refleng += rMgr.startAddr.get(currentSection);
//			//		    		System.out.println(refleng);
//					    	
//					    	
//						    	for(int i=0;i<4;) {
//						    		if(line.charAt(9)=='+') {
//		
//						    			rMgr.setMemory(pacount, (char)(mmem1));
//						    		}
//						    		else if(line.charAt(9)=='-') {
//						    			rMgr.setMemory(pacount, (char)(mmem1));
//						    		}
//						    		
//						    		mmem1 = Integer.parseInt(String.valueOf(refleng.charAt(i)),16);						    		
//						    		mmem2 = Integer.parseInt(String.valueOf(refleng.charAt(i+1)),16);
//						    		
//						    		
//						    		if((char)mmem1>='0'&&(char)mmem1<='9') {
//						    			refleng = (char)(mmem1 - '0');
//						    		}
//						    		else if(mmem2>='0'&&mmem2<='9') {
//						    			mmem2 -= '0';
//						    		}
//						    		else if(mmem1>='A'&&mmem1<='F') {
//						    			mmem1 -= 55;
//						    		}
//						    		else if(mmem2>='A'&&mmem2<='F') {
//						    			mmem2 -= 55;
//						    		}
////						    		System.out.println(mmem1+"\t"+mmem2);
//						    		mmem1 = mmem1 << 4;
//						    		mmem1 |= mmem2;
//						    		rMgr.setMemory(pacount, (char)(mmem1));
//						    		realaddr = rMgr.startAddr.get(currentSection);
//
//
//						    		i+=2;
//						    		pacount++;
//						    	}
//					    	break;
//				    	
//					    case 'E':
//					    	currentSection++;
//					    	break;
//					    }
//			    	  }
//			      }
//			    	  
//			      
//			          for(int i=0;i<4219;i++)
//		    		System.out.println(Integer.toHexString(i)+"\t"+Integer.toHexString((int)rMgr.memory[i]));
//
//
//		    } catch(IOException e) {
//		    	e.printStackTrace();
//		    }
	};	

}
