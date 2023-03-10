package site.itwill.project1;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;;

public class HpMain extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	JTextField noTF, nameTF, phoneTF, addressTF, birthTF, docnoTF;
	JButton addB, deleteB, updateB, searchB, cancelB;
	JButton addnB, deletenB, updatenB, searchnB, searchnB2;

	JTable table;

	int cmd;

	addWindow aw;
	deleteWindow dw;
	searchNoWindow snw;
	updateWindow uw;
	searchWindow sw;

	public HpMain() throws Exception {

		setTitle("환자 차트");
		setSize(800, 400);

		Dimension dim = getToolkit().getScreenSize();
		setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2 - getHeight() / 2);

//		입력창

//		상태 클릭창
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(1, 5));

		bottom.add(addB = new JButton("입원"));
		addB.addActionListener(this);
		bottom.add(deleteB = new JButton("퇴원"));
		deleteB.addActionListener(this);
		bottom.add(updateB = new JButton("변경"));
		updateB.addActionListener(this);
		bottom.add(searchB = new JButton("검색"));
		searchB.addActionListener(this);
		bottom.add(cancelB = new JButton("취소"));
		cancelB.addActionListener(this);

		Object[] title = { "번호", "이름", "전화번호", "주소", "생년월일", "담당의사" };
		table = new JTable(new DefaultTableModel(title, 0));
		table.setEnabled(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		JScrollPane sp = new JScrollPane(table);

		add(sp, "Center");
		add(bottom, "South");
		displayAllPa();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

	}

	public static void main(String[] args) throws Exception {

		new HpMain();
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Component c = (Component) ev.getSource();
		try {
			if (c == addB) {
				aw = new addWindow();
			}
			if (c == deleteB) {
				dw = new deleteWindow();
			}
			if (c == updateB) {
				snw = new searchNoWindow();
			}
			if (c == searchB) {
				sw = new searchWindow();
			}
			if (c == cancelB) {
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("예외 발생 : " + e.getLocalizedMessage());
		}
	}

	public boolean addPa() {

		String noTemp = noTF.getText();
		if (noTemp.equals("")) {
			JOptionPane.showMessageDialog(this, "환자번호를 반드시 입력해 주세요.");
			noTF.requestFocus();// 입력촛점을 이동하는 메소드
			return false;
		}

		String noReg = "\\d{4}";
		if (!Pattern.matches(noReg, noTemp)) {
			JOptionPane.showMessageDialog(this, "환자번호는 4자리 숫자입니다.");
			noTF.requestFocus();
			return false;
		}

		int no = Integer.parseInt(noTemp);

		if (HpDAO.getDAO().selectNoPatient(no) != null) {
			JOptionPane.showMessageDialog(this, "이미 사용중인 번호입니다.");
			noTF.requestFocus();
			return false;
		}

		String name = nameTF.getText();

		if (name.equals("")) {
			JOptionPane.showMessageDialog(this, "이름을 반드시 입력해 주세요.");
			nameTF.requestFocus();
			return false;
		}

		String nameReg = "[가-힣]{2,5}";
		if (!Pattern.matches(nameReg, name)) {
			JOptionPane.showMessageDialog(this, "이름은 2~5 범위의 한글만 입력해 주세요.");
			nameTF.requestFocus();
			return false;
		}

		String phone = phoneTF.getText();

		if (phone.equals("")) {
			JOptionPane.showMessageDialog(this, "전화번호를 반드시 입력해 주세요.");
			phoneTF.requestFocus();
			return false;
		}

		String phoneReg = "(01[016789])-\\d{3,4}-\\d{4}";
		if (!Pattern.matches(phoneReg, phone)) {
			JOptionPane.showMessageDialog(this, "전화번호를 형식에 맞게 입력해 주세요.");
			phoneTF.requestFocus();
			return false;
		}

		String address = addressTF.getText();

		if (address.equals("")) {
			JOptionPane.showMessageDialog(this, "주소를 반드시 입력해 주세요.");
			addressTF.requestFocus();
			return false;
		}

		String birth = birthTF.getText();

		if (birth.equals("")) {
			JOptionPane.showMessageDialog(this, "생년월일을 반드시 입력해 주세요.");
			birthTF.requestFocus();
			return false;
		}

		String birthdayReg = "(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]||3[01])";
		if (!Pattern.matches(birthdayReg, birth)) {
			JOptionPane.showMessageDialog(this, "생년월일을 형식에 맞게 입력해 주세요.");
			birthTF.requestFocus();
			return false;
		}

		String noTemp2 = docnoTF.getText();
		if (noTemp2.equals("")) {
			JOptionPane.showMessageDialog(this, "의사코드를 반드시 입력해 주세요.");
			docnoTF.requestFocus();// 입력촛점을 이동하는 메소드
			return false;
		}

		String noReg2 = "\\d{2}";
		if (!Pattern.matches(noReg2, noTemp2)) {
			JOptionPane.showMessageDialog(this, "의사코드는 2자리 숫자입니다.");
			docnoTF.requestFocus();
			return false;
		}

		int docno = Integer.parseInt(noTemp2);

		HpDTO hp = new HpDTO();
		hp.setNo(no);
		hp.setName(name);
		hp.setPhone(phone);
		hp.setAddress(address);
		hp.setBirth(birth);
		hp.setDocno(docno);
		int rows = HpDAO.getDAO().insertPatient(hp);

		JOptionPane.showMessageDialog(this, rows + "명의 환자정보를 삽입 하였습니다.");

		displayAllPa();
		return true;
	}

	public void displayAllPa() {
		List<HpDTO> hpList = HpDAO.getDAO().selectAllPatientList();

		if (hpList.isEmpty()) {
			JOptionPane.showMessageDialog(this, "저장된 환자정보가 없습니다.");
			return;
		}

		DefaultTableModel model = (DefaultTableModel) table.getModel();

		for (int i = model.getRowCount(); i > 0; i--) {

			model.removeRow(0);
		}

		for (HpDTO hp : hpList) {
			Vector<Object> rowData = new Vector<Object>();
			rowData.add(hp.getNo());
			rowData.add(hp.getName());
			rowData.add(hp.getPhone());
			rowData.add(hp.getAddress());
			rowData.add(hp.getBirth());
			rowData.add(hp.getDocno());
			model.addRow(rowData);
		}
	}

	public boolean removePa() {
		String noTemp = noTF.getText();
		if (noTemp.equals("")) {
			JOptionPane.showMessageDialog(this, "환자번호를 반드시 입력해 주세요.");
			noTF.requestFocus();
			return false;
		}

		String noReg = "\\d{4}";
		if (!Pattern.matches(noReg, noTemp)) {
			JOptionPane.showMessageDialog(this, "환자번호는 4자리 숫자입니다.");
			noTF.requestFocus();
			return false;
		}

		int no = Integer.parseInt(noTemp);

		int rows = HpDAO.getDAO().deletePatient(no);

		if (rows > 0) {
			JOptionPane.showMessageDialog(this, rows + "명의 환자를 삭제 하였습니다.");
			displayAllPa();
		}

		return true;

	}

	public boolean searchNoPa() {
		String noTemp = noTF.getText();
		if (noTemp.equals("")) {
			JOptionPane.showMessageDialog(this, "환자번호를 반드시 입력해 주세요.");
			noTF.requestFocus();
			return false;
		}

		String noReg = "\\d{4}";
		if (!Pattern.matches(noReg, noTemp)) {
			JOptionPane.showMessageDialog(this, "환자번호는 4자리 숫자입니다.");
			noTF.requestFocus();
			return false;
		}

		int no = Integer.parseInt(noTemp);

		HpDTO hp = HpDAO.getDAO().selectNoPatient(no);
		if (hp == null) {
			JOptionPane.showMessageDialog(this, "변경하고자 하는 환자정보가 없습니다.");
			noTF.requestFocus();
			noTF.setText("");
			return false;
		}

		return true;

	}

	public boolean modifyPa() {
		int no = Integer.parseInt(noTF.getText());

		String name = nameTF.getText();

		if (name.equals("")) {
			JOptionPane.showMessageDialog(this, "이름을 반드시 입력해 주세요.");
			nameTF.requestFocus();
			return false;
		}

		String nameReg = "[가-힣]{2,5}";
		if (!Pattern.matches(nameReg, name)) {
			JOptionPane.showMessageDialog(this, "이름은 2~5 범위의 한글만 입력해 주세요.");
			nameTF.requestFocus();
			return false;
		}

		String phone = phoneTF.getText();

		if (phone.equals("")) {
			JOptionPane.showMessageDialog(this, "전화번호를 반드시 입력해 주세요.");
			phoneTF.requestFocus();
			return false;
		}

		String phoneReg = "(01[016789])-\\d{3,4}-\\d{4}";
		if (!Pattern.matches(phoneReg, phone)) {
			JOptionPane.showMessageDialog(this, "전화번호를 형식에 맞게 입력해 주세요.");
			phoneTF.requestFocus();
			return false;
		}

		String address = addressTF.getText();

		if (address.equals("")) {
			JOptionPane.showMessageDialog(this, "주소를 반드시 입력해 주세요.");
			addressTF.requestFocus();
			return false;
		}

		String birth = birthTF.getText();

		if (birth.equals("")) {
			JOptionPane.showMessageDialog(this, "생년월일을 반드시 입력해 주세요.");
			birthTF.requestFocus();
			return false;
		}

		String birthdayReg = "(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]||3[01])";
		if (!Pattern.matches(birthdayReg, birth)) {
			JOptionPane.showMessageDialog(this, "생년월일을 형식에 맞게 입력해 주세요.");
			birthTF.requestFocus();
			return false;
		}

		String noTemp2 = docnoTF.getText();
		if (noTemp2.equals("")) {
			JOptionPane.showMessageDialog(this, "의사코드를 반드시 입력해 주세요.");
			docnoTF.requestFocus();
			return false;
		}

		String noReg2 = "\\d{2}";
		if (!Pattern.matches(noReg2, noTemp2)) {
			JOptionPane.showMessageDialog(this, "의사코드는 2자리 숫자입니다.");
			docnoTF.requestFocus();
			return false;
		}

		int docno = Integer.parseInt(noTemp2);

		HpDTO hp = new HpDTO();
		hp.setNo(no);
		hp.setName(name);
		hp.setPhone(phone);
		hp.setAddress(address);
		hp.setBirth(birth);
		hp.setDocno(docno);

		int rows = HpDAO.getDAO().updatePatient(hp);

		JOptionPane.showMessageDialog(this, rows + "명의 환자정보를 변경 하였습니다.");

		displayAllPa();
		return true;
	}

	public boolean searchNamePa() {
		String name = nameTF.getText();
		if (name.equals("")) {
			JOptionPane.showMessageDialog(this, "이름을 반드시 입력해 주세요.");
			nameTF.requestFocus();
			return false;
		}

		String nameReg = "[가-힣]{2,5}";
		if (!Pattern.matches(nameReg, name)) {
			JOptionPane.showMessageDialog(this, "이름은 2~5 범위의 한글만 입력해 주세요.");
			nameTF.requestFocus();
			return false;
		}

		List<HpDTO> hpList = HpDAO.getDAO().selectNamePatientList(name);

		if (hpList.isEmpty()) {
			JOptionPane.showMessageDialog(this, "검색된 환자정보가 없습니다.");
			return false;
		}

		DefaultTableModel model = (DefaultTableModel) table.getModel();

		for (int i = model.getRowCount(); i > 0; i--) {
			model.removeRow(0);
		}

		for (HpDTO hp : hpList) {
			Vector<Object> rowData = new Vector<Object>();
			rowData.add(hp.getNo());
			rowData.add(hp.getName());
			rowData.add(hp.getPhone());
			rowData.add(hp.getAddress());
			rowData.add(hp.getBirth());
			rowData.add(hp.getDocno());
			model.addRow(rowData);
		}

		return true;
	}
//	입원, 퇴원, 검색, 변경 시 새로운 창 생성
	class addWindow extends JFrame implements ActionListener {
		private static final long serialVersionUID = 1L;

		addWindow() {
			setTitle("환자 추가");

			JPanel NewWindow = new JPanel();
			setContentPane(NewWindow);

			JPanel c = new JPanel();
			c.setLayout(new GridLayout(8, 1));

			JPanel pno = new JPanel();
			pno.add(new JLabel("번  호"));
			pno.add(noTF = new JTextField(10));

			JPanel pname = new JPanel();
			pname.add(new JLabel("이  름"));
			pname.add(nameTF = new JTextField(10));

			JPanel pbirthday = new JPanel();
			pbirthday.add(new JLabel("생  일"));
			pbirthday.add(birthTF = new JTextField(10));

			JPanel pphone = new JPanel();
			pphone.add(new JLabel("전  화"));
			pphone.add(phoneTF = new JTextField(10));

			JPanel paddress = new JPanel();
			paddress.add(new JLabel("주  소"));
			paddress.add(addressTF = new JTextField(10));

			JPanel pdocno = new JPanel();
			pno.add(new JLabel("코  드"));
			pno.add(docnoTF = new JTextField(10));

			c.add(addnB = new JButton("확  인"));
			addnB.addActionListener(this);

			c.add(pno);
			c.add(pname);
			c.add(pphone);
			c.add(paddress);
			c.add(pbirthday);
			c.add(pdocno);
			c.add(addnB);
			add(c, "Center");

			setSize(500, 400);
			setResizable(false);
			setVisible(true);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Component c = (Component) e.getSource();
			if (c == addnB) {
				if (addPa() == true) {
					aw.dispose();
				}

			}

		}

	}

	class deleteWindow extends JFrame implements ActionListener {

		private static final long serialVersionUID = 1L;

		deleteWindow() {
			setTitle("환자 삭제");

			JPanel NewWindow = new JPanel();
			setContentPane(NewWindow);

			JPanel c = new JPanel();
			c.setLayout(new GridLayout(2, 1));

			JPanel pno = new JPanel();
			pno.add(new JLabel("번  호"));
			pno.add(noTF = new JTextField(10));

			c.add(deletenB = new JButton("확  인"));
			deletenB.addActionListener(this);
			c.add(pno);
			c.add(deletenB);
			add(c, "Center");

			setSize(500, 400);
			setResizable(false);
			setVisible(true);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Component c = (Component) e.getSource();
			if (c == deletenB) {
				if (removePa() == true) {
					dw.dispose();
				}
			}

		}
	}

	class searchNoWindow extends JFrame implements ActionListener {
		private static final long serialVersionUID = 1L;

		searchNoWindow() {
			setTitle("변경할 환자 번호 입력");

			JPanel NewWindow = new JPanel();
			setContentPane(NewWindow);

			JPanel c = new JPanel();
			c.setLayout(new GridLayout(2, 1));

			JPanel pno = new JPanel();
			pno.add(new JLabel("번  호"));
			pno.add(noTF = new JTextField(10));

			c.add(searchnB = new JButton("확  인"));
			searchnB.addActionListener(this);
			c.add(pno);
			c.add(searchnB);
			add(c, "Center");

			setSize(500, 400);
			setResizable(false);
			setVisible(true);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Component c = (Component) e.getSource();
			if (c == searchnB) {
				if (searchNoPa() == true) {
					snw.dispose();
					uw = new updateWindow();
				}
			}
		}

	}

	class updateWindow extends JFrame implements ActionListener {
		private static final long serialVersionUID = 1L;

		updateWindow() {
			setTitle("변경 내용");

			JPanel NewWindow = new JPanel();
			setContentPane(NewWindow);

			JPanel c = new JPanel();
			c.setLayout(new GridLayout(7, 1));

			JPanel pname = new JPanel();
			pname.add(new JLabel("이  름"));
			pname.add(nameTF = new JTextField(10));

			JPanel pbirthday = new JPanel();
			pbirthday.add(new JLabel("생  일"));
			pbirthday.add(birthTF = new JTextField(10));

			JPanel pphone = new JPanel();
			pphone.add(new JLabel("전  화"));
			pphone.add(phoneTF = new JTextField(10));

			JPanel paddress = new JPanel();
			paddress.add(new JLabel("주  소"));
			paddress.add(addressTF = new JTextField(10));

			JPanel pdocno = new JPanel();
			pdocno.add(new JLabel("코  드"));
			pdocno.add(docnoTF = new JTextField(10));

			c.add(updatenB = new JButton("확  인"));
			updatenB.addActionListener(this);

			c.add(pname);
			c.add(pphone);
			c.add(paddress);
			c.add(pbirthday);
			c.add(pdocno);
			c.add(updatenB);
			add(c, "Center");

			setSize(500, 400);
			setResizable(false);
			setVisible(true);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Component c = (Component) e.getSource();
			if (c == updatenB) {
				if (modifyPa() == true) {
					uw.dispose();
				}
			}

		}

	}

	class searchWindow extends JFrame implements ActionListener {
		private static final long serialVersionUID = 1L;

		searchWindow() {

			setTitle("검색할 환자 이름 입력");

			JPanel NewWindow = new JPanel();
			setContentPane(NewWindow);

			JPanel c = new JPanel();
			c.setLayout(new GridLayout(2, 1));

			JPanel pname = new JPanel();
			pname.add(new JLabel("이  름"));
			pname.add(nameTF = new JTextField(10));

			c.add(searchnB2 = new JButton("확  인"));
			searchnB2.addActionListener(this);
			c.add(pname);
			c.add(searchnB2);
			add(c, "Center");

			setSize(500, 400);
			setResizable(false);
			setVisible(true);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Component c = (Component) e.getSource();
			if (c == searchnB2) {
				if (searchNamePa() == true) {
					sw.dispose();
				}
			}

		}

	}

}