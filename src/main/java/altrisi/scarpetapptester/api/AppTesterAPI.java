package altrisi.scarpetapptester.api;

import java.util.Map;

import carpet.script.CarpetEventServer.Event;

public class AppTesterAPI {
	private AppTesterAPI() {}
	
	/**
	 * <p>Registers a new event to be tested in tester apps that contain it.</p>
	 * 
	 * <p>This will be called whenever an app is being tested where {@link Event#isNeeded()} returns {@code true}.</p>
	 * 
	 * <p>Ideally, invoker would provide varied and edge-case invocations of the event in order to test that
	 * the app is ready to accept all kinds of calls to those events, from the most common to the rarest.</p>
	 * 
	 * @param event The reference to the event to be tested, in order to call {@link Event#isNeeded()}.
	 * @param invokers A {@link Map} of {@link Runnable}s that will invoke the event with a more specific name as their key
	 * 					(due to the contract of {@link Map}, key must be unique for each entry in the map). It is expected
	 * 					for this map to contain not only common cases, but a variety of edge-cases that may occur.
	 */
	public static void registerTestableEvent(Event event, Map<String, Runnable> invokers) {
		throw new UnsupportedOperationException();//TODO
	}
}
