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
 * ResourceManager는 컴퓨터의 가상 리소스들을 선언하고 관리하는 클래스이다.
 * 크게 네가지의 가상 자원 공간을 선언하고, 이를 관리할 수 있는 함수들을 제공한다.<br><br>
 * 
 * 1) 입출력을 위한 외부 장치 또는 device<br>
 * 2) 프로그램 로드 및 실행을 위한 메모리 공간. 여기서는 64KB를 최대값으로 잡는다.<br>
 * 3) 연산을 수행하는데 사용하는 레지스터 공간.<br>
 * 4) SYMTAB 등 simulator의 실행 과정에서 사용되는 데이터들을 위한 변수들. 
 * <br><br>
 * 2번은 simulator위에서 실행되는 프로그램을 위한 메모리공간인 반면,
 * 4번은 simulator의 실행을 위한 메모리 공간이라는 점에서 차이가 있다.
 */
public class ResourceManager{
	/**
	 * deviceManager는  디바이스의 이름을 입력받았을 때 해당 디바이스의 파일 입출력 관리 클래스를 리턴하는 역할을 한다.
	 * 예를 들어, 'A1'이라는 디바이스에서 파일을 read모드로 열었을 경우, hashMap에 <"A1", scanner(A1)> 등을 넣음으로서 이를 관리할 수 있다.
	 * <br><br>
	 * 변형된 형태로 사용하는 것 역시 허용한다.<br>
	 * 예를 들면 key값으로 String대신 Integer를 사용할 수 있다.
	 * 파일 입출력을 위해 사용하는 stream 역시 자유로이 선택, 구현한다.
	 * <br><br>
	 * 이것도 복잡하면 알아서 구현해서 사용해도 괜찮습니다.
	 */
	SicSimulator ssm = new SicSimulator(this);
	FileChannel filechannel, fc;
	int byteCount = 0;
	HashMap<String,Object> deviceManager = new HashMap<String,Object>();
	char[] memory = new char[65536]; // String으로 수정해서 사용하여도 무방함.
	int[] register = new int[10];
	double register_F;
	ArrayList<String> progName = new ArrayList<String>();
	ArrayList<String> progLength = new ArrayList<String>();
	ArrayList<Integer> startAddr = new ArrayList<Integer>();
	// 이외에도 필요한 변수 선언해서 사용할 것.

	/**
	 * 메모리, 레지스터등 가상 리소스들을 초기화한다.
	 */
	public void initializeResource(){
		for(int i=0;i<register.length;i++) {
			register[i] = 0;
		}
		register_F = 0;
	}
	
	/**
	 * deviceManager가 관리하고 있는 파일 입출력 stream들을 전부 종료시키는 역할.
	 * 프로그램을 종료하거나 연결을 끊을 때 호출한다.
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
	 * 디바이스를 사용할 수 있는 상황인지 체크. TD명령어를 사용했을 때 호출되는 함수.
	 * 입출력 stream을 열고 deviceManager를 통해 관리시킨다.
	 * @param devName 확인하고자 하는 디바이스의 번호,또는 이름
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
	 * 디바이스로부터 원하는 개수만큼의 글자를 읽어들인다. RD명령어를 사용했을 때 호출되는 함수.
	 * @param devName 디바이스의 이름
	 * @return 가져온 데이터
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
	 * 디바이스로 원하는 개수 만큼의 글자를 출력한다. WD명령어를 사용했을 때 호출되는 함수.
	 * @param devName 디바이스의 이름
	 * @param data 보내는 데이터
	 * @param num 보내는 글자의 개수
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
	 * 메모리의 특정 위치에서 원하는 개수만큼의 글자를 가져온다.
	 * @param location 메모리 접근 위치 인덱스
	 * @param num 데이터 개수
	 * @return 가져오는 데이터
	 */
	public char[] getMemory(int location , int num)	//memory : aa bb cc  num : 3 이면 aa bb cc num 2 이면 aa bb
	{
		char r[] = new char[num];
		for(int i = 0; i < num; i++)
			r[i] = memory[i + location];
		return r;
	}

	/**
	 * 메모리의 특정 위치에 원하는 개수만큼의 데이터를 저장한다. 
	 * @param locate 접근 위치 인덱스
	 * @param cs 저장하려는 데이터
	 * @param num 저장하는 데이터의 개수
	 */
	public void setMemory(int locate, char[] cs){
		for(int i = 0; i < cs.length; i++)
			memory[locate + i] = cs[i];
	}

	/**
	 * 번호에 해당하는 레지스터가 현재 들고 있는 값을 리턴한다. 레지스터가 들고 있는 값은 문자열이 아님에 주의한다.
	 * @param regNum 레지스터 분류번호
	 * @return 레지스터가 소지한 값
	 */
	public int getRegister(int regNum){
		if(regNum==6) {
			return (int)register_F;
		}
		else
			return register[regNum];
	}

	/**
	 * 번호에 해당하는 레지스터에 새로운 값을 입력한다. 레지스터가 들고 있는 값은 문자열이 아님에 주의한다.
	 * @param regNum 레지스터의 분류번호
	 * @param value 레지스터에 집어넣는 값
	 */
	public void setRegister(int regNum, int value){
		if(regNum==6) {
			register_F = value;
		}
		else
			register[regNum] = value;
	}

	/**
	 * 주로 레지스터와 메모리간의 데이터 교환에서 사용된다. int값을 char[]형태로 변경한다.
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
	 * 주로 레지스터와 메모리간의 데이터 교환에서 사용된다. char[]값을 int형태로 변경한다.
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