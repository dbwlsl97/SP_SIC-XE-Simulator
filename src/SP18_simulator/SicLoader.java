package SP18_simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.tools.DocumentationTool.Location;

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
	ArrayList<Integer> mcodeAddress = new ArrayList<Integer>();
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
				    		char[] abc = rMgr.intToChar(tmem1+tmem2, 1);
				    		rMgr.setMemory(mem, abc);
				    		
				    		i+=2;
				    		mem++;
	    						    		
				    	}
				    	break;
				    case 'M':
				    	mcode.add(line);
				    	mcodeAddress.add(secaddr);
			
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
		    int zerocount=0;
			int chickenSum = 0;
			int nowAddress;
		    for(int i=0;i<mcode.size();i++) {
		    	int a=0, b=0 , c=0;
		    	nowAddress = mcodeAddress.get(i);
		    	pacount = Integer.parseInt((mcode.get(i).substring(5,7)),16);
		    	pacount += nowAddress;
		    	int symAddres = symtab.search(mcode.get(i).substring(10));
		    	

		    	zerocount = (mcode.get(i).charAt(8));
		    	if(zerocount=='5') {
		    		b = rMgr.memory[pacount + 1];
			    	c = rMgr.memory[pacount + 2];
		    	}
		    	else if(zerocount == '6')
		    	{
		    		a = rMgr.memory[pacount];
			    	b = rMgr.memory[pacount + 1];
			    	c = rMgr.memory[pacount + 2];
		    	}
		    
//		    	chickenSum |= a << 16;
//		    	chickenSum |= b << 8;
//		    	chickenSum |= c;
//		    	System.out.println("chickenSum :"+chickenSum);
		    	if(zerocount == '5')
		    	{
		    		if(mcode.get(i).charAt(9) == '+')
//		    			rMgr.setMemory((pacount + 1 ) ,rMgr.intToChar( (symAddres + chickenSum) , 2));
		    			rMgr.setMemory((pacount + 1 ) ,rMgr.intToChar( (symAddres) , 2));
		    		if(mcode.get(i).charAt(9) == '-')
		    			rMgr.setMemory((pacount + 1 ) ,rMgr.intToChar( (symAddres) , 2));
//		    			rMgr.setMemory((pacount + 1 ) ,rMgr.intToChar( (-symAddres + chickenSum) , 2));
		    	}
		    	if(zerocount == '6')
		    	{
		    		if(mcode.get(i).charAt(9) == '+')
		    			rMgr.setMemory((pacount) ,rMgr.intToChar( (symAddres) , 3));
//		    			rMgr.setMemory(pacount  ,rMgr.intToChar( symAddres + chickenSum , 3));
		    		if(mcode.get(i).charAt(9) == '-')
		    			rMgr.setMemory((pacount) ,rMgr.intToChar( (symAddres) , 3));
//		    			rMgr.setMemory(pacount ,rMgr.intToChar( - symAddres + chickenSum , 3));
		    	}
		    		    
		    	}

//		    for(int i=0;i<4219;i++)
//	        	  	System.out.println(String.format("%X   : %02X", i ,(int)(rMgr.memory[i])));

	};	

}
