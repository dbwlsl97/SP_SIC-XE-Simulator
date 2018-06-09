package SP18_simulator;
import java.util.ArrayList;

/**
 * symbol�� ���õ� �����Ϳ� ������ �����Ѵ�.
 * section ���� �ϳ��� �ν��Ͻ��� �Ҵ��Ѵ�.
 */
public class SymbolTable {
	ArrayList<String> symbolList;
	ArrayList<Integer> addressList;
	// ��Ÿ literal, external ���� �� ó������� �����Ѵ�.
	public SymbolTable() {
		symbolList = new ArrayList<String>();
		addressList = new ArrayList<Integer>();
	}

	/**
	 * ���ο� Symbol�� table�� �߰��Ѵ�.
	 * @param symbol : ���� �߰��Ǵ� symbol�� label
	 * @param address : �ش� symbol�� ������ �ּҰ�
	 * <br><br>
	 * ���� : ���� �ߺ��� symbol�� putSymbol�� ���ؼ� �Էµȴٸ� �̴� ���α׷� �ڵ忡 ������ ������ ��Ÿ����. 
	 * ��Ī�Ǵ� �ּҰ��� ������ modifySymbol()�� ���ؼ� �̷������ �Ѵ�.
	 */
	public void putSymbol(String symbol, int address) {
		symbolList.add(symbol);
		addressList.add(address);
		}
	
	/**
	 * ������ �����ϴ� symbol ���� ���ؼ� ����Ű�� �ּҰ��� �����Ѵ�.
	 * @param symbol : ������ ���ϴ� symbol�� label
	 * @param newaddress : ���� �ٲٰ��� �ϴ� �ּҰ�
	 */
	public void modifySymbol(String symbol, int newaddress) {
		if(search(symbol)!=-1) {
//			lit = symbol.split("'"); //���ͷ��̶�� '�� �������� �и��ϰ�
			for(int i=0;i<symbolList.size();i++) { 
				if(symbol.equals(symbolList.get(i))) //���� �ɺ��� �ɺ�����Ʈ�� �ִٸ� �������ֱ�
				addressList.set(i, newaddress);	
					
				}
			
			}
	}
	
	/**
	 * ���ڷ� ���޵� symbol�� � �ּҸ� ��Ī�ϴ��� �˷��ش�. 
	 * @param symbol : �˻��� ���ϴ� symbol�� label
	 * @return symbol�� ������ �ִ� �ּҰ�. �ش� symbol�� ���� ��� -1 ����
	 */
	public int search(String symbol) {
		int address = 0;
		if(symbolList.contains(symbol)) { //�ɺ����̺� ���� �ɺ��� �����Ѵٸ�
			for(int i=0;i<symbolList.size();i++) {
				if(symbol.equals(symbolList.get(i))) { 
					address = addressList.get(i);		// �ּҸ� ����			
				}
			}
		}
		else {
			return -1;
		}
		return address;
	}	
	
	
	
}
