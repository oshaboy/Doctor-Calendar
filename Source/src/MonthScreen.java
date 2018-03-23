/*
 * Copyright oshaboy, Noam Gilor. 2018
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.swing.*;
public class MonthScreen extends JFrame {

	private static final long serialVersionUID = -1710837254471908309L;

	//private GregorianCalendar calendar = new GregorianCalendar();
	//private Container container = new JContainer();
	private JButton[][] days;
	private boolean isCurrent = false;
	public final int BUTTONWIDTH = 89;
	public final int BUTTONHEIGHT = 55;	
	public final int DAYSPERWEEK = 7;
	public final int WEEKSPERMONTH = 5;
	public final int TEXTHEIGHT = 18;
	public final int margin = 30;
	private JComboBox<Integer> monthBox = new JComboBox<Integer>();
	private JComboBox<Integer> yearBox = new JComboBox<Integer>();
	private int firstdayofweek;
	private int lastdayofmonth;
	private int month;
	@SuppressWarnings("unused")
	private int year;
	//private JComboBox<Color> colorBox;
	private Color curcolor = Color.CYAN.brighter();
	
	public Color getColor() {
		return curcolor;
	}
	public MonthScreen(int year, int month) {
		
		setTitle("Doctor Calendar");
		this.year = year;
		this.month = month;
		if (month == Program.GlobalCalendar.get(GregorianCalendar.MONTH) && year == Program.GlobalCalendar.get(GregorianCalendar.YEAR)) 
		{
			isCurrent = true;
		}
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(BUTTONWIDTH * (DAYSPERWEEK+1), BUTTONHEIGHT * (WEEKSPERMONTH+1) + margin + TEXTHEIGHT);
		setBackground(Color.LIGHT_GRAY);

		GregorianCalendar calendar = new GregorianCalendar(year, month, 1);
		calendar.setFirstDayOfWeek(GregorianCalendar.SUNDAY);
		days = new JButton[DAYSPERWEEK][WEEKSPERMONTH];
		int counter = 1;
		for (int y = 0; y < WEEKSPERMONTH; y++) {
			for (int x= 0; x < DAYSPERWEEK ; x++) {
				JButton cur = days[x][y] = new JButton();
				cur.setBounds(BUTTONWIDTH*x,BUTTONHEIGHT*y + TEXTHEIGHT + margin, BUTTONWIDTH, BUTTONHEIGHT);

				firstdayofweek = calendar.get(GregorianCalendar.DAY_OF_WEEK);
				lastdayofmonth = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
				ActionListener a;
				if ((x>=firstdayofweek-1 || y > 0) && counter<=lastdayofmonth) {
					cur.setText(Integer.toString(counter++));
					 a = new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							//System.out.println(Integer.paInteger(((JButton)e.getSource()).getText());
							String daystring = ((JButton)e.getSource()).getText();
							if (daystring.indexOf("<p>") != -1) {
								daystring = daystring.substring(6, daystring.indexOf("<p>"));
							}
							int day = Integer.parseInt(daystring);
							new DayScreen(year, month, day ); //got counter that way because counter is local
							//new MonthScreen(year,month);
							
							
						}
					
					};
				}
				else if (counter>lastdayofmonth){
					cur.setText(Integer.toString(counter++ - lastdayofmonth));
					 a = new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							String daystring = ((JButton)e.getSource()).getText();
							if (daystring.indexOf("<p>") != -1) {
								daystring = daystring.substring(6, daystring.indexOf("<p>"));
							}
							int day = Integer.parseInt(daystring);
							new DayScreen(year+(month%12), (month+1)%12,  day); 
							//new MonthScreen(year+(month%12), (month+1)%12);
							
							
						}
					
					};
				}
				else /*if (x<firstdayofweek-1)*/{
					GregorianCalendar previousMonth = new GregorianCalendar(year-(month==0?1:0), (month-1)+ (month==0?1:0)*12,1); ///got counter that way because counter is local
		
					cur.setText(Integer.toString(previousMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) + (x - (firstdayofweek-1) + 1)));
					
					a = new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							String daystring = ((JButton)e.getSource()).getText();
							if (daystring.indexOf("<p>") != -1) {
								daystring = daystring.substring(6, daystring.indexOf("<p>"));
							}
							int day = Integer.parseInt(daystring);
							new DayScreen(year-(month==0?1:0), (month-1) + (month==0?1:0) * 12, day); //got counter that way because counter is local
							

							
						}
					
					};
				}
				cur.addActionListener(a);
				if (counter == Program.GlobalCalendar.get(GregorianCalendar.DAY_OF_MONTH) + 1 && isCurrent) {
					days[x][y].setBackground(Color.CYAN);
				}
				else {
					days[x][y].setBackground(Color.WHITE);
				}
				
				
				
				add(cur);				
			}

		}
		Integer[] monthStringArr = new Integer[12];
		Integer[] yearStringArr = new Integer[150];
		for (int i = 1; i<=monthStringArr.length; i++) {
			monthStringArr[i-1] = i;
		}
		for (int i = 0; i<yearStringArr.length; i++) {
			yearStringArr[i] = 2000+i;
		}
		monthBox = new JComboBox<Integer>(monthStringArr);
		yearBox = new JComboBox<Integer>(yearStringArr);
		yearBox.setSelectedIndex(year-2000);
		monthBox.setSelectedIndex(month);
		yearBox.setBounds(4,0,yearBox.getPreferredSize().width,yearBox.getPreferredSize().height );
		monthBox.setBounds(yearBox.getPreferredSize().width + 8,0,monthBox.getPreferredSize().width,monthBox.getPreferredSize().height );
		
		ActionListener changeMonth = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int month = (int) monthBox.getSelectedItem();
				int year = (int) yearBox.getSelectedItem();
				
				MonthScreen m = new MonthScreen(year, month-1);
				Program.mainScreen.dispose();
				Program.mainScreen = m;
				m.setVisible(true);
				
			}
			
		};
		monthBox.addActionListener(changeMonth);
		yearBox.addActionListener(changeMonth);
		add(monthBox);
		add(yearBox);
		
		JLabel Sunday = new JLabel("Sunday");
		Sunday.setBounds(BUTTONWIDTH * 0 + BUTTONWIDTH/2- Sunday.getPreferredSize().width/2, margin + 2, Sunday.getPreferredSize().width, Sunday.getPreferredSize().height);
		add(Sunday);
		
		JLabel Monday = new JLabel("Monday");
		Monday.setBounds(BUTTONWIDTH * 1 + BUTTONWIDTH/2- Monday.getPreferredSize().width/2, margin + 2, Monday.getPreferredSize().width, Monday.getPreferredSize().height);
		add(Monday);
		
		JLabel Tuesday = new JLabel("Tuesday");
		Tuesday.setBounds(BUTTONWIDTH * 2 + BUTTONWIDTH/2- Tuesday.getPreferredSize().width/2, margin + 2, Tuesday.getPreferredSize().width, Tuesday.getPreferredSize().height);
		add(Tuesday);
		
		JLabel Wednesday = new JLabel("Wednesday");
		Wednesday.setBounds(BUTTONWIDTH * 3 + BUTTONWIDTH/2- Wednesday.getPreferredSize().width/2, margin + 2, Wednesday.getPreferredSize().width, Wednesday.getPreferredSize().height);
		add(Wednesday);
		
		JLabel Thursday = new JLabel("Thursday");
		Thursday.setBounds(BUTTONWIDTH * 4 + BUTTONWIDTH/2 - Thursday.getPreferredSize().width/2, margin + 2, Thursday.getPreferredSize().width, Thursday.getPreferredSize().height);
		add(Thursday);
		
		JLabel Friday = new JLabel("Friday");
		Friday.setBounds(BUTTONWIDTH * 5 + BUTTONWIDTH/2 -  Friday.getPreferredSize().width/2, margin + 2, Friday.getPreferredSize().width, Friday.getPreferredSize().height);
		add(Friday);
		
		JLabel Saturday = new JLabel("Sunday");
		Saturday.setBounds(BUTTONWIDTH * 6 + BUTTONWIDTH/2 -  Saturday.getPreferredSize().width / 2, margin + 2, Saturday.getPreferredSize().width, Saturday.getPreferredSize().height);
		add(Saturday);
		
		//Color[] allBackgroundColors = {Color.CYAN, Color.ORANGE, Color.RED.brighter(), Color.YELLOW.brighter(), Color.PINK, Color.LIGHT_GRAY, Color.GREEN.brighter()};
		/*colorBox = new JComboBox<Color>(allBackgroundColors);
		colorBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				curcolor = (Color) colorBox.getSelectedItem();
			}
			
		});
		colorBox.setBounds(yearBox.getPreferredSize().width  + monthBox.getPreferredSize().width + 32,0, colorBox.getPreferredSize().width, colorBox.getPreferredSize().height);*/
		//add(colorBox);
		
		//error correction
		JButton Invisible = new JButton();
		Invisible.setVisible(false);
		add(Invisible); 
		for (int i=0; i < 35; i++) {
			try {
				JButton day = days[i%DAYSPERWEEK][i/DAYSPERWEEK];
				String date_iso8601 = Integer.toString(year) + "-" + Integer.toString(month+1) + "-" + day.getText();
				Scanner s = new Scanner(new File("saves",date_iso8601 + ".day"), "UTF-8");
				day.setText("<html>" + day.getText() + "<p>" + s.nextLine());
				s.close();
			}
			 catch (FileNotFoundException e1) {
				continue;
				
			} catch (java.util.NoSuchElementException e1) {
				e1.printStackTrace();
			}

		}
		
		
		setVisible(true);
	}
	public MonthScreen() {
		this( 
				Program.GlobalCalendar.get(GregorianCalendar.YEAR),
				Program.GlobalCalendar.get(GregorianCalendar.MONTH)
			);
		//GregorianCalendar calendar = Program.GlobalCalendar;

	}
	public void update() {
		if (this.month != Program.GlobalCalendar.get(GregorianCalendar.MONTH)) {
			Program.mainScreen = new MonthScreen();
			this.dispose();
			return;
		}
		
		Program.curDay.dispose();
		int newday_index = (Program.GlobalCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1) + (firstdayofweek - 1) ;
		JButton newday = days[newday_index%7][newday_index/7];
		JButton oldday = days[(newday_index-1)%7][(newday_index-1)/7];
		oldday.setBackground(Color.WHITE);
		newday.setBackground(curcolor);
		Program.curDay = new DayScreen();
		
		
	}
	
	/*public JButton getDayButton(int day, boolean includeFirstDay) {
		int realday = day + firstdayofweek;
		if (realday > lastdayofmonth + firstdayofweek) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return days[realday%DAYSPERWEEK][realday/DAYSPERWEEK];
	}*/
}
