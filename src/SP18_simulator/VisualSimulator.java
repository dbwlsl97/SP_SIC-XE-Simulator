package SP18_simulator;

import java.awt.EventQueue;
import java.io.File;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * VisualSimulator�� ����ڿ��� ��ȣ�ۿ��� ����Ѵ�.<br>
 * ��, ��ư Ŭ������ �̺�Ʈ�� �����ϰ� �׿� ���� ������� ȭ�鿡 ������Ʈ �ϴ� ������ �����Ѵ�.<br>
 * �������� �۾��� SicSimulator���� �����ϵ��� �����Ѵ�.
 */
public class VisualSimulator {
	ResourceManager resourceManager = new ResourceManager();
	SicLoader sicLoader = new SicLoader(resourceManager);
	SicSimulator sicSimulator = new SicSimulator(resourceManager);

	/**
	 * ���α׷� �ε� ����� �����Ѵ�.
	 */
	public void load(File program){
		//...
		sicLoader.load(program);
		sicSimulator.load(program);
	};

	/**
	 * �ϳ��� ��ɾ ������ ���� SicSimulator�� ��û�Ѵ�.
	 */
	public void oneStep(){
		
	};

	/**
	 * �����ִ� ��� ��ɾ ������ ���� SicSimulator�� ��û�Ѵ�.
	 */
	public void allStep(){
		
	};
	
	/**
	 * ȭ���� �ֽŰ����� �����ϴ� ������ �����Ѵ�.
	 */
	public void update(){
		
	};
	

	public static void main(String[] args) {
		MainFrame myFrame = new MainFrame();
	}
}
class MainFrame extends JFrame {
	public  MainFrame() {
		setSize(300, 200);
		setTitle("SIC/XE Simulator");		
		setVisible(true);
	    setLayout(new FlowLayout());//�⺻����BoderLayout ���� FlowLayout���� ����
	    JButton button = new JButton("��ư");
	    this.add(button);
	    setVisible(true);
	}



}

