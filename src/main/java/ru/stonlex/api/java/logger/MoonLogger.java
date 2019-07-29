package ru.stonlex.api.java.logger;

import java.util.logging.*;

public class MoonLogger extends Logger {

	/**
	 * Protected method to construct a logger for a named subsystem.
	 * <p>
	 * The logger will be initially configured with a null Level
	 * and with useParentHandlers set to true.
	 */
	public MoonLogger() {
		super("MoonStudio", null);

		setLevel(Level.ALL);
	}

}

