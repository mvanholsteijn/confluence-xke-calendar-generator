package com.xebia.xke;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * generates the XHTML confluence content of the XKE Calendar for a specific year.
 * 
 * @author Mark van Holsteijn
 *
 */
public class ConfluenceXKECalendarGenerator {

	
	/**
	 * Constructor.
	 * 
	 * @param year to generate a calendar for >= 2014.
	 */
	public ConfluenceXKECalendarGenerator(int year) {
		this.year = year;
		if(this.year < 2014) {
			throw new IllegalArgumentException("I can only generate calendars from 2014 onwards");
		}
		first();
	}
	

	/**
	 * representing current XKE date.
	 */
	Calendar calendar;

	/**
	 * to generate the calendar for.
	 */
	int year;
	/**
	 * sequence number of the XKE in this year.
	 */
	int nr;

	public void process() {
		Date date = calendar.getTime();
		SimpleDateFormat xkePageFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat xkePageDisplay = new SimpleDateFormat("EEE dd MMM yyyy");

		String format = "<tr><td>%d</td><td>"
				+ "<span class=\"confluence-link\"> <span class=\"confluence-link\">"
				+ "<ac:link>"
				+ "<ri:page ri:content-title=\"XKE-NL-%s\"/>"
				+ "<ac:plain-text-link-body><![CDATA[%s]]></ac:plain-text-link-body>"
				+ "</ac:link>" + "</span> </span></td>" + "<td>%s</td></tr>";

		String line = String.format(format, nr, xkePageFormat.format(date),
				xkePageDisplay.format(date), "");
		System.out.println(line);
	}

	/**
	 * sets 'calendar' to first XKE date in 'year'.
	 */
	public void first() {
		calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, 2014);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 14);

		while (calendar.get(Calendar.YEAR) < year) {
			next();
		}
		nr = 1;
	}

	/**
	 * is the 'calendar' still in 'year'?
	 * @return
	 */
	public boolean inYear() {
		return calendar.get(Calendar.YEAR) == year;
	}

	/**
	 * sets 'calendar' to next XKE date and increments 'nr'. 
	 */
	public void next() {
		calendar.add(Calendar.DAY_OF_MONTH, 14);
		nr++;
	}

	/**
	 * a complete Confluence XHTML XKE calendar
	 */
	public void generate() {
		start();
		first();
		while (inYear()) {
			process();
			next();
		}
		end();

	}

	/**
	 * start generating..
	 */
	public void start() {
		System.out
				.println(String
						.format("<table><tbody><tr><th>%d.#</th> <th>Event</th><th>Remarks</th></tr>",
								year));
	}

	/**
	 * end of calendar generation.
	 */
	public void end() {
		System.out.println("</tbody></table>");
	}

	public static void main(String[] args) {
		int year;
		if(args.length == 0) {
			year = Calendar.getInstance().get(Calendar.YEAR) + 1;
		} else if(args.length == 1) {
			year = Integer.parseInt(args[0]);
		} else {
			year = 0;
			System.err.println(String.format("Usage: java %s [ year ]", ConfluenceXKECalendarGenerator.class.getName()));
			System.exit(1);
		}
		
		ConfluenceXKECalendarGenerator generator = new ConfluenceXKECalendarGenerator(year);
		generator.generate();
	}
}
