package SP18_simulator;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;



/**
 * ResourceManager�� ��ǻ���� ���� ���ҽ����� �����ϰ� �����ϴ� Ŭ�����̴�.
 * ũ�� �װ����� ���� �ڿ� ������ �����ϰ�, �̸� ������ �� �ִ� �Լ����� �����Ѵ�.<br><br>
 * 
 * 1) ������� ���� �ܺ� ��ġ �Ǵ� device<br>
 * 2) ���α׷� �ε� �� ������ ���� �޸� ����. ���⼭�� 64KB�� �ִ밪���� ��´�.<br>
 * 3) ������ �����ϴµ� ����ϴ� �������� ����.<br>
 * 4) SYMTAB �� simulator�� ���� �������� ���Ǵ� �����͵��� ���� ������. 
 * <br><br>
 * 2���� simulator������ ����Ǵ� ���α׷��� ���� �޸𸮰����� �ݸ�,
 * 4���� simulator�� ������ ���� �޸� �����̶�� ������ ���̰� �ִ�.
 */
public class ResourceManager{
	/**
	 * deviceManager��  ����̽��� �̸��� �Է¹޾��� �� �ش� ����̽��� ���� ����� ���� Ŭ������ �����ϴ� ������ �Ѵ�.
	 * ���� ���, 'A1'�̶�� ����̽����� ������ read���� ������ ���, hashMap�� <"A1", scanner(A1)> ���� �������μ� �̸� ������ �� �ִ�.
	 * <br><br>
	 * ������ ���·� ����ϴ� �� ���� ����Ѵ�.<br>
	 * ���� ��� key������ String��� Integer�� ����� �� �ִ�.
	 * ���� ������� ���� ����ϴ� stream ���� �������� ����, �����Ѵ�.
	 * <br><br>
	 * �̰͵� �����ϸ� �˾Ƽ� �����ؼ� ����ص� �������ϴ�.
	 */
	SicSimulator ssm = new SicSimulator(this);
	FileChannel filechannel, fc;
	int byteCount = 0;
	HashMap<String,Object> deviceManager = new HashMap<String,Object>();
	char[] memory = new char[65536]; // String���� �����ؼ� ����Ͽ��� ������.
	int[] register = new int[10];
	double register_F;
	ArrayList<String> progName = new ArrayList<String>();
	ArrayList<String> progLength = new ArrayList<String>();
	ArrayList<Integer> startAddr = new ArrayList<Integer>();
	// �̿ܿ��� �ʿ��� ���� �����ؼ� ����� ��.

	/**
	 * �޸�, �������͵� ���� ���ҽ����� �ʱ�ȭ�Ѵ�.
	 */
	public void initializeResource(){
		for(int i=0;i<register.length;i++) {
			register[i] = 0;
		}
		register_F = 0;
	}
	
	/**
	 * deviceManager�� �����ϰ� �ִ� ���� ����� stream���� ���� �����Ű�� ����.
	 * ���α׷��� �����ϰų� ������ ���� �� ȣ���Ѵ�.
	 */
	public void closeDevice() {
		try {
			fc.close();
			filechannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ����̽��� ����� �� �ִ� ��Ȳ���� üũ. TD��ɾ ������� �� ȣ��Ǵ� �Լ�.
	 * ����� stream�� ���� deviceManager�� ���� ������Ų��.
	 * @param devName Ȯ���ϰ��� �ϴ� ����̽��� ��ȣ,�Ǵ� �̸�
	 */
	public void testDevice(String devName){
		if(!deviceManager.containsKey(devName)) {
		try {
			filechannel = FileChannel.open(
					Paths.get("./"+String.format("%02X", Integer.parseInt(devName,16))+".txt"),
					StandardOpenOption.CREATE,
					StandardOpenOption.READ,
					StandardOpenOption.WRITE

					);
			deviceManager.put(devName, filechannel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			setRegister(ssm.regSW, -1);
			
			e.printStackTrace();
			
		}
		}
	}

	/**
	 * ����̽��κ��� ���ϴ� ������ŭ�� ���ڸ� �о���δ�. RD��ɾ ������� �� ȣ��Ǵ� �Լ�.
	 * @param devName ����̽��� �̸�
	 * @return ������ ������
	 */

	public char readDevice(String devName){
		
			fc = (FileChannel)deviceManager.get(devName);
			ByteBuffer buf = ByteBuffer.allocate(1);
	        try {
	        	byteCount = fc.read(buf);
	        	buf.flip();	   
	            if (byteCount == -1) {
	            	return 0;
	            }
	        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        }
		return (char)buf.get();
		
	}

	/**
	 * ����̽��� ���ϴ� ���� ��ŭ�� ���ڸ� ����Ѵ�. WD��ɾ ������� �� ȣ��Ǵ� �Լ�.
	 * @param devName ����̽��� �̸�
	 * @param data ������ ������
	 * @param num ������ ������ ����
	 */
	public void writeDevice(String devName, char data){
		
		fc = (FileChannel)deviceManager.get(devName);
		try {
			ByteBuffer buf = Charset.defaultCharset().encode(Character.toString(data));
			fc.write(buf);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * �޸��� Ư�� ��ġ���� ���ϴ� ������ŭ�� ���ڸ� �����´�.
	 * @param location �޸� ���� ��ġ �ε���
	 * @param num ������ ����
	 * @return �������� ������
	 */
	public char[] getMemory(int location , int num)	//memory : aa bb cc  num : 3 �̸� aa bb cc num 2 �̸� aa bb
	{
		char r[] = new char[num];
		for(int i = 0; i < num; i++)
			r[i] = memory[i + location];
		return r;
	}

	/**
	 * �޸��� Ư�� ��ġ�� ���ϴ� ������ŭ�� �����͸� �����Ѵ�. 
	 * @param locate ���� ��ġ �ε���
	 * @param cs �����Ϸ��� ������
	 * @param num �����ϴ� �������� ����
	 */
	public void setMemory(int locate, char[] cs){
		for(int i = 0; i < cs.length; i++)
			memory[locate + i] = cs[i];
	}

	/**
	 * ��ȣ�� �ش��ϴ� �������Ͱ� ���� ��� �ִ� ���� �����Ѵ�. �������Ͱ� ��� �ִ� ���� ���ڿ��� �ƴԿ� �����Ѵ�.
	 * @param regNum �������� �з���ȣ
	 * @return �������Ͱ� ������ ��
	 */
	public int getRegister(int regNum){
		if(regNum==6) {
			return (int)register_F;
		}
		else
			return register[regNum];
	}

	/**
	 * ��ȣ�� �ش��ϴ� �������Ϳ� ���ο� ���� �Է��Ѵ�. �������Ͱ� ��� �ִ� ���� ���ڿ��� �ƴԿ� �����Ѵ�.
	 * @param regNum ���������� �з���ȣ
	 * @param value �������Ϳ� ����ִ� ��
	 */
	public void setRegister(int regNum, int value){
		if(regNum==6) {
			register_F = value;
		}
		else
			register[regNum] = value;
	}

	/**
	 * �ַ� �������Ϳ� �޸𸮰��� ������ ��ȯ���� ���ȴ�. int���� char[]���·� �����Ѵ�.
	 * @param data
	 * @return
	 */
	public char[] intToChar(int data , int num) // 0x0000ffff num : 4 00 00 ff ff  num 2 : ff ff
	{
		char r[] = new char[num];
		for(int i = 0; i < num; i++)
		{
			r[i] = (char) ((data >> ((num - i - 1) * 8)) & ((1 << 8) - 1));
		}
		return r;
	}

	/**
	 * �ַ� �������Ϳ� �޸𸮰��� ������ ��ȯ���� ���ȴ�. char[]���� int���·� �����Ѵ�.
	 * @param data
	 * @return
	 */
	public int charToInt(char[] data){	// ff ff ff ff -> 0xfffffff aa bb -> 0x0000aabb
		int r = 0;
		for(int i= 0; i < data.length; i++)
			r |= data[i] << (data.length - i - 1) * 8;
		return r;
	}

	public void setProgname(String name, int currentSection) {
		progName.add(name);
//		 TODO Auto-generated method stub
	}

	public void setProgLength(String length, int currentSection) {
		// TODO Auto-generated method stub
		progLength.add(length);

	}

	public void setStartADDR(int startaddr) {
//		System.out.println(startaddr);
		startAddr.add(startaddr);
		// TODO Auto-generated method stub
		
	}
}