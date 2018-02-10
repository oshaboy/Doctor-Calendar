/*
 * Copyright oshaboy, Noam Gilor. 2018
 */
import java.util.GregorianCalendar;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.*;

public class DayScreen extends JFrame {
	private final int PATIENTS_PER_DAY = 4*24;
	private final int PATIENTS_PER_ROW = 4;

	private final int PATIENTS_PER_COLUMN = 24;
	private final int PATIENT_HEIGHT = 16;
	private final int PATIENT_WIDTH = 100;
	private final int WIDTHMARGIN = 60;
	private final int HEIGHTMARGIN = 35;
	public final int BUTTONWIDTH = 89;
	public final int BUTTONHEIGHT = 27;	
	
	private JTextField[] allClients = new JTextField[PATIENTS_PER_DAY];
	private JButton saveButton;
	private JButton loadButton;
	private int year;
	private int month;
	private int day;
	private boolean isToday;
	private JTextField whatday;
	ScheduledExecutorService changecolorevery15 = null;
	
	
	public DayScreen(int year, int month, int day) {
		

		setTitle("Doctor Calendar");
		setLayout(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.year = year;
		this.month = month;
		this.day = day;
		

		isToday = Program.GlobalCalendar.get(GregorianCalendar.DAY_OF_MONTH) == day
				&& Program.GlobalCalendar.get(GregorianCalendar.MONTH) == month
				&& Program.GlobalCalendar.get(GregorianCalendar.YEAR) == year;
		String date_iso8601 = Integer.toString(year) + "-" + Integer.toString(month+1) + "-" + Integer.toString(day);
		setSize(PATIENTS_PER_ROW * (PATIENT_WIDTH+4) + WIDTHMARGIN, PATIENTS_PER_COLUMN * (PATIENT_HEIGHT+3) + BUTTONHEIGHT + HEIGHTMARGIN);
		whatday = new JTextField();
		whatday.setBounds(1 * (PATIENT_WIDTH+4) + WIDTHMARGIN,0, PATIENT_WIDTH*2, PATIENT_HEIGHT);
		add (whatday);
			
		for (int i = 0; i < PATIENTS_PER_DAY; i++) {
			JTextField cur = allClients[i] = new JTextField();
			cur.setBounds(i/PATIENTS_PER_COLUMN * PATIENT_WIDTH+WIDTHMARGIN, PATIENT_HEIGHT * (i%PATIENTS_PER_COLUMN) + HEIGHTMARGIN, PATIENT_WIDTH, PATIENT_HEIGHT);
			add(cur);
		}

		saveButton = new JButton();
		saveButton.setBounds(0, PATIENT_HEIGHT*PATIENTS_PER_COLUMN + HEIGHTMARGIN + 20, BUTTONWIDTH, BUTTONHEIGHT);
		saveButton.setText("save");
		loadButton = new JButton();
		loadButton.setBounds(BUTTONWIDTH+2, PATIENT_HEIGHT*PATIENTS_PER_COLUMN + HEIGHTMARGIN + 20, BUTTONWIDTH, BUTTONHEIGHT);
		loadButton.setText("load");
		
		
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					PrintWriter stream = new PrintWriter(new File("saves", date_iso8601 + ".day"));
					stream.println(whatday.getText());
					for (JTextField field : allClients) {
						stream.println(field.getText());
					}
					stream.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					throw new RuntimeException("FileNotFoundException: " + e1.getMessage());
				}

			}
			
		});
		
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Scanner s;
				String curline = "";
				int debug_counter = 0;
				try {
					s = new Scanner(new File("saves",date_iso8601 + ".day"), "UTF-8");
					whatday.setText(s.nextLine());
					for (JTextField field : allClients) {
						curline = s.nextLine();
						field.setText(curline);
						debug_counter++;
					}
					s.close();
				} catch (FileNotFoundException e1) {
					new Alert("No file made for this day");
					
				} catch (java.util.NoSuchElementException e1) {
					System.out.print(debug_counter+" " + curline);
				}


			}
			
		});
		
		add(saveButton);
		add(loadButton);
		
		JLabel date = new JLabel(date_iso8601);
		date.setBounds(2,2, date.getPreferredSize().width, date.getPreferredSize().height);
		add(date);
		
		int counter = 0;
		for ( int hour = 0; hour < 6; hour++) {
			for (int minute = 0; minute < 60; minute += 15) {
				JLabel l = new JLabel(hour+":"+(minute<10?"0":"")+minute);
				l.setBounds(20,HEIGHTMARGIN + counter * PATIENT_HEIGHT, l.getPreferredSize().width, l.getPreferredSize().height);
				add(l);
				counter++;
			}
		}
		JLabel time;
		time = new JLabel("06:00");
		time.setBounds(WIDTHMARGIN + PATIENT_WIDTH * 1 + time.getPreferredSize().width/2 + 20, HEIGHTMARGIN-time.getPreferredSize().height, time.getPreferredSize().width,time.getPreferredSize().height);
		time.setVisible(true);
		add(time);
		
		time = new JLabel("12:00");
		time.setBounds(WIDTHMARGIN + PATIENT_WIDTH * 2 + time.getPreferredSize().width/2 + 20, HEIGHTMARGIN-time.getPreferredSize().height, time.getPreferredSize().width,time.getPreferredSize().height);
		time.setVisible(true);
		add(time);
		
		time = new JLabel("18:00");
		time.setBounds(WIDTHMARGIN + PATIENT_WIDTH * 3 + time.getPreferredSize().width/2 + 20, HEIGHTMARGIN-time.getPreferredSize().height, time.getPreferredSize().width,time.getPreferredSize().height);
		time.setVisible(true);
		add(time);
		
		time = new JLabel("00:00");
		time.setBounds(WIDTHMARGIN + PATIENT_WIDTH * 0 + time.getPreferredSize().width/2 + 20, HEIGHTMARGIN-time.getPreferredSize().height, time.getPreferredSize().width,time.getPreferredSize().height);
		time.setVisible(true);
		add(time);
		if (isToday) {
			int hour = Program.GlobalCalendar.get(GregorianCalendar.HOUR_OF_DAY);
			int minute = Program.GlobalCalendar.get(GregorianCalendar.MINUTE);
			allClients[minute/15 + hour * 4].setBackground(Color.CYAN);
		}
		//error correction		
		JTextField Invisible = new JTextField();
		Invisible.setVisible(false);
		add(Invisible); 
		loadButton.doClick();
		setVisible(true);
	}
	public boolean getIsToday() {
		return isToday;
	}
	public DayScreen() {
		this(Program.GlobalCalendar.get(GregorianCalendar.YEAR), Program.GlobalCalendar.get(GregorianCalendar.MONTH), Program.GlobalCalendar.get(GregorianCalendar.DAY_OF_MONTH));
	}
	
	public void update() {
		if (isToday) {
			int hour = Program.GlobalCalendar.get(GregorianCalendar.HOUR_OF_DAY);
			int minute = Program.GlobalCalendar.get(GregorianCalendar.MINUTE);
			allClients[minute/15 + hour * 4].setBackground(Color.CYAN);
			allClients[(minute/15 + hour * 4) - 1].setBackground(Color.WHITE);
		}
	}
}
