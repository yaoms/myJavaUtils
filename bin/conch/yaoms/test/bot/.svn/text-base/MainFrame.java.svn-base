package conch.yaoms.test.bot;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MainFrame extends JFrame implements IControler {

	private static final long serialVersionUID = -7615654549862317641L;

	private iAnima anima;

	private Container container;

	public MainFrame(String title, iAnima anima) {
		super(title);

		UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels();
		for (int i = 0, n = plaf.length; i < n; i++) {
			System.out.println("Name: " + plaf[i].getName());
			System.out.println("  Class name: " + plaf[i].getClassName());
		}

		try {
			if (System.getProperty("os.name").toLowerCase().contains("linux")) {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} else {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		setAnima(anima);
		anima.setControler(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(320, 480);

		container = new JPanel();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(container);

		getAnima().birth();

		// pack();
	}

	private Component createButton(String title, final String iAnimaMethodName,
			final iAnima anima) {
		JButton button = new JButton(title);

		button.addActionListener(new ActionListener() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void actionPerformed(ActionEvent e) {

				Class animaClass = anima.getClass();

				try {
					Method iAnimaMethod = animaClass.getMethod(
							iAnimaMethodName, new Class[] {});
					iAnimaMethod.invoke(anima, new Object[] {});
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					e1.printStackTrace();
				}

			}
		});
		return button;
	}

	public void setAnima(iAnima anima) {
		this.anima = anima;
	}

	public iAnima getAnima() {
		return anima;
	}

	@Override
	public void addCallHandler(String title, String methodName, iAnima anima) {
		container.add(createButton(title, methodName, anima));
	}

}
