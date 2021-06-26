package altrisi.scarpetapptester.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import altrisi.scarpetapptester.testing.apps.App;
import altrisi.scarpetapptester.testing.apps.ScarpetApp;

public record AppConfig(String name, String hi) {
	public static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(new GSONRecordAdapterFactory()).create();
	
	public App createApp() {
		return new ScarpetApp(name(), this);
	}
}
