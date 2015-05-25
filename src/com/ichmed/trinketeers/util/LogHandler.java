package com.ichmed.trinketeers.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogHandler extends Handler{

	private String printformat = "";
	private List<Character> fields = new ArrayList<>();
	private boolean allerrors;
	private int tracedepth;

	/**
	 * creates a new LogHandler to control the output of a {@link java.util.logging.Logger Logger}
	 * @param allerrors even messages with a priority level lower than
	 * {@link java.util.logging.Level#WARNING WARNING} are written to
	 * {@link java.lang.System#err stderr} (otherwise they will be written to
	 * {@link java.lang.System#out stdout})
	 * @param tracedepth the depth of the stacktrace (if any) where 0 meaning
	 * that no stacktrace will be printed and -1 meaning that all levels will be printed
	 * @param format a format string. The following control strings are recognized:<br>
	 * <table>
	 * <tr><th>String component</th><th>explanation</th></tr>
	 * <tr><td>\l</td><td>the name of the logger</td></tr>
	 * <tr><td>\L</td><td>the {@link java.util.logging.Level Level} of the record</td></tr>
	 * <tr><td>\H</td><td>hours (24 hour format)</td></tr>
	 * <tr><td>\h</td><td>hours (12 hour format)</td></tr>
	 * <tr><td>\m</td><td>minutes</td></tr>
	 * <tr><td>\s</td><td>seconds</td></tr>
	 * <tr><td>\S</td><td>milliseconds</td></tr>
	 * <tr><td>\C</td><td>The full classified name of the class which logged the message.</td></tr>
	 * <tr><td>\c</td><td>The basename of the class which logged the message.</td></tr>
	 * <tr><td>\M</td><td>The name of the invoking method</td></tr>
	 * </table>
	 * All other characters are taken as they are.<br>
	 * Note: the message will be appended to the resulting string.
	 */
	public LogHandler(boolean allerrors, int tracedepth, String format){
		this.allerrors = allerrors;
		this.tracedepth = tracedepth;
		for(int i = 0; i < format.length(); i++){
			if(format.charAt(i) == '\\'){
				switch(format.charAt(i+1)){
				case 'l':
					printformat += "%s";
					fields.add('l');
					i++;
					continue;
				case 'L':
					printformat += "%s";
					fields.add('L');
					i++;
					continue;
				case 'H':
					printformat += "%s";
					fields.add('H');
					i++;
					continue;
				case 'h':
					printformat += "%s";
					fields.add('h');
					i++;
					continue;
				case 'm':
					printformat += "%s";
					fields.add('m');
					i++;
					continue;
				case 's':
					printformat += "%s";
					fields.add('s');
					i++;
					continue;
				case 'S':
					printformat += "%s";
					fields.add('S');
					i++;
					continue;
				case 'c':
					printformat += "%s";
					fields.add('c');
					i++;
					continue;
				case 'C':
					printformat += "%s";
					fields.add('C');
					i++;
					continue;
				case 'M':
					printformat += "%s";
					fields.add('M');
					i++;
				default:
					printformat += '\\';
					continue;
				}
			}
			printformat += format.charAt(i);
		}
	}

	/**
	 * prints out all messages to stderr with the specified format. 
	 * @param format
	 */
	public LogHandler(String format){
		this(true, -1, format);
	}

	/**
	 * prints out messages to stderr in the form:<br>
	 * hh:mm:ss:SSS:class:level:message<br>
	 * hh:mm:ss:SSS:class:level:stacktrace...
	 */
	public LogHandler() {
		this(true, -1, "\\H:\\m:\\s:\\S:\\c.\\M():\\L:");
	}

	@Override
	public void publish(LogRecord record) {
		Logger logger = Logger.getLogger(record.getLoggerName());
		if(record.getLevel().intValue() >= logger.getLevel().intValue()){
			String[] args = new String[fields.size()];
			for(int i = 0; i < fields.size(); i++){
				switch(fields.get(i)){
				case 'l':
					String lname = record.getLoggerName();
					if(lname != null){
						args[i] = record.getLoggerName();
					}
					break;
				case 'L':
					Level level = record.getLevel();
					if(level != null){
						args[i] = level.getName();
					}
					break;
				case 'H':
					args[i] = new SimpleDateFormat("HH").format(new Date(record.getMillis()));
					break;
				case 'h':
					args[i] = new SimpleDateFormat("hh").format(new Date(record.getMillis()));
					break;
				case 'm':
					args[i] = new SimpleDateFormat("mm").format(new Date(record.getMillis()));
					break;
				case 's':
					args[i] = new SimpleDateFormat("ss").format(new Date(record.getMillis()));
					break;
				case 'S':
					args[i] = new SimpleDateFormat("SSS").format(new Date(record.getMillis()));
					break;
				case 'C':
					String classname = record.getSourceClassName();
					if(classname != null){
						args[i] = classname;
					}
					break;
				case 'c':
					String classname1 = record.getSourceClassName();
					if(classname1 != null){
						args[i] = classname1.substring(record.getSourceClassName().lastIndexOf('.') + 1);
					}
					break;
				case 'M':
					String methodname = record.getSourceMethodName();
					if(methodname != null && methodname.endsWith("\\")){
						args[i] = methodname.replace('\\', ' ').trim();
					} else {
						args[i] = methodname;
					}
					break;
				default:
					break;	
				}
			}
			PrintStream p;
			if(allerrors || record.getLevel().intValue() >= Level.WARNING.intValue()){
				p = System.err;
			} else {
				p = System.out;
			}
			print(p, printformat, args, record.getMessage());
			if(tracedepth == -1){
				Throwable t = record.getThrown();
				while(t != null){
					print(p, printformat+" "+t.getClass().getName()+":", args, t.getMessage());
					for(StackTraceElement s: t.getStackTrace()){
						print(p, printformat, args, s.toString());
					}
					t = t.getCause();
				}
			} else if(tracedepth > 0){
				Throwable t = record.getThrown();
				if(t != null){
					print(p, printformat+" "+t.getClass().getName()+":", args, record.getThrown().getMessage());
					StackTraceElement[] stacktrace = t.getStackTrace();
					for(int i = 0; i < (tracedepth > stacktrace.length ?
							stacktrace.length : tracedepth ); i++){
						print(p, printformat, args, t.getStackTrace()[i].toString());
					}
					t = t.getCause();
				}
			}
		}
	}

	private void print(PrintStream p, String printformat, String[] args, String message){
		Object[] arguments = new String[args.length+1];
		for(int i = 0; i < args.length; i++){
			arguments[i] = args[i];
		}
		arguments[arguments.length-1] = message;
		p.printf(printformat + "%s\n", arguments);
	}

	@Override
	public void flush() {
		if(!allerrors)
			System.out.flush();
		System.err.flush();
	}

	@Override
	public void close() throws SecurityException {}

}
