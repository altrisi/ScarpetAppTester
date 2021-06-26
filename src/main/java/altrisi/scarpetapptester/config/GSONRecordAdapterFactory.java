package altrisi.scarpetapptester.config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Got it from google/gson#1794. Will use it until it fails.
 * 
 * This is NOT a copy paste. This is a copy paste then make it so it doesn't have a test.
 * 
 * @author sceutre
 *
 */
class GSONRecordAdapterFactory implements TypeAdapterFactory {
	
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       @SuppressWarnings("unchecked")
       Class<T> clazz = (Class<T>) type.getRawType();
       if (!clazz.isRecord()) {
          return null;
       }
       TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

       return new TypeAdapter<T>() {
          @Override
          public void write(JsonWriter out, T value) throws IOException {
             delegate.write(out, value);
          }

          @Override
          public T read(JsonReader reader) throws IOException {
             if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
             } else {
                var recordComponents = clazz.getRecordComponents();
                var typeMap = new HashMap<String, TypeToken<?>>();
                for (int i = 0; i < recordComponents.length; i++) {
                   typeMap.put(recordComponents[i].getName(), TypeToken.get(recordComponents[i].getGenericType()));
                }
                var argsMap = new HashMap<String,Object>();
                reader.beginObject();
                while (reader.hasNext()) {
                   String name = reader.nextName();
                   argsMap.put(name, gson.getAdapter(typeMap.get(name)).read(reader));
                }
                reader.endObject();

                var argTypes = new Class<?>[recordComponents.length];
                var args = new Object[recordComponents.length];
                for (int i = 0; i < recordComponents.length; i++) {
                   argTypes[i] = recordComponents[i].getType();
                   args[i] = argsMap.get(recordComponents[i].getName());
                }
                Constructor<T> constructor;
                try {
                   constructor = clazz.getDeclaredConstructor(argTypes);
                   constructor.setAccessible(true);
                   return constructor.newInstance(args);
                } catch (IllegalArgumentException | ReflectiveOperationException e) {
                   throw new RuntimeException(e);
                }
             }
          }
       };
    }
}
