/*
 * Copyright oshaboy, Noam Gilor. 2018
 */

import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Program {
	public static GregorianCalendar GlobalCalendar = new GregorianCalendar();
	public static MonthScreen mainScreen = new MonthScreen();
	public static DayScreen curDay = new DayScreen();
	public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static Runnable update = new Runnable() { public void run() {
		GlobalCalendar = new GregorianCalendar();
		System.out.println(GlobalCalendar.get(GregorianCalendar.SECOND));
		if (GlobalCalendar.get(GregorianCalendar.HOUR_OF_DAY) == 0
			&& GlobalCalendar.get(GregorianCalendar.MINUTE) == 0
			&& GlobalCalendar.get(GregorianCalendar.SECOND) == 0) {
			System.out.println("day update");
			mainScreen.update();
		}
		else if (GlobalCalendar.get(GregorianCalendar.MINUTE)% 15 == 0 && GlobalCalendar.get(GregorianCalendar.SECOND) == 0) {
			System.out.println("color update");
			curDay.update();
		}
	}};
	public static void main(String[] args) {
		scheduler.scheduleWithFixedDelay(update, 0, 500, TimeUnit.MILLISECONDS);
		   
		
		
	}
	


}
