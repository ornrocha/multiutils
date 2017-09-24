/************************************************************************** 
 * Orlando Rocha (ornrocha@gmail.com)
 *
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 *  
 */
package pt.ornrocha.jsonutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.ornrocha.fileutils.MTUFileUtils;
import pt.ornrocha.ioutils.writers.MTUWriterUtils;

public class MTUJsonIOUtils {
	
	public static String getSimpleJSONPrettyPrintString(JSONObject obj){
		return obj.toString(5);
	}
	
	
	public static String getSimpleJSONPrettyPrintString(JSONObject obj, int indentation){
		return obj.toString(indentation);
	}
	
	
	public static void printPrettySimpleJSONObject(JSONObject obj){
		System.out.println(getSimpleJSONPrettyPrintString(obj));
		
	}
	
	public static void writeJSONObjectToFilePrettyFormat(JSONObject obj, String filepath, int indentation) throws IOException{
		 String filename = MTUFileUtils.buildFilePathWithExtension(filepath, "json");
		 //FileWriter w = new FileWriter(filename);
		// w.write(getSimpleJSONPrettyPrintString(obj,indentation));
		 MTUWriterUtils.writeStringWithFileChannel(getSimpleJSONPrettyPrintString(obj,indentation), filename, 0);
		 //w.close();
	}
	
	public static void writeJSONObjectToFilePrettyFormat(JSONObject obj, String filepath) throws IOException{
		 String filename = MTUFileUtils.buildFilePathWithExtension(filepath, "json");
		 FileWriter w = new FileWriter(filename);
		 w.write(getSimpleJSONPrettyPrintString(obj));
		 w.close();
	}
	
	public static void writeJSONObjectToFilePrettyFormatWithGSON(JSONObject obj, String filepath) throws IOException{
		 String filename = MTUFileUtils.buildFilePathWithExtension(filepath, "json");
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
		 String output = gson.toJson(obj);
		 MTUWriterUtils.writeStringWithFileChannel(output, filename, 0);

	}
	
	
	public static void writeJSONObjectToFilePrettyFormat(JSONObject obj, String directory, String filename) throws IOException{
		 String filepath = MTUFileUtils.buildFilePathWithExtension(directory, filename, "json"); 
		 FileWriter w = new FileWriter(filepath);
		 w.write(getSimpleJSONPrettyPrintString(obj));
		 w.close();
	}
	
	public static void writeJSONObjectToFilePrettyFormat(JSONObject obj, String directory, String filename, int indentation) throws IOException{
		 String filepath = MTUFileUtils.buildFilePathWithExtension(directory, filename, "json"); 
		 FileWriter w = new FileWriter(filepath);
		 w.write(getSimpleJSONPrettyPrintString(obj,indentation));
		 w.close();
	}
	
	
	public static JSONObject readJsonFile(File file) throws FileNotFoundException{
		FileReader input = new FileReader(file);
		JSONTokener tokens = new JSONTokener(input);
		return new JSONObject(tokens);
	}
	
	public static JSONObject readJsonFile(String filePath) throws FileNotFoundException{
		return readJsonFile(new File(filePath));
	}
	
	 public static void ConvertJsonFileToPrettyJson(String file, String savefilepath) throws IOException{
  	   JSONObject obj=readJsonFile(file);
  	   writeJSONObjectToFilePrettyFormat(obj, savefilepath);
     }
	 
	 
	 
	/* Writer write(JSONObject obj, Writer writer, int indentFactor, int indent) throws JSONException {
	        try {
	            boolean commanate = false;
	            final int length = obj.length();
	           // Iterator<String> keys = this.keys();
	            Set<String> keys=obj.keySet();
	            writer.write('{');

	            if (length == 1) {
	                //Object key = keys.next();
	                Object key = keys.
	                writer.write(quote(key.toString()));
	                writer.write(':');
	                if (indentFactor > 0) {
	                    writer.write(' ');
	                }
	                //JSONObject.writeValue(writer, this.map.get(key), indentFactor, indent);
	                JSONObject.writeValue(writer, obj.get((String) key), indentFactor, indent);
	            } else if (length != 0) {
	                final int newindent = indent + indentFactor;
	                while (keys.hasNext()) {
	                    Object key = keys.next();
	                    if (commanate) {
	                        writer.write(',');
	                    }
	                    if (indentFactor > 0) {
	                        writer.write('\n');
	                    }
	                    indent(writer, newindent);
	                    writer.write(quote(key.toString()));
	                    writer.write(':');
	                    if (indentFactor > 0) {
	                        writer.write(' ');
	                    }
	                    writeValue(writer, this.map.get(key), indentFactor, newindent);
	                    commanate = true;
	                }
	                if (indentFactor > 0) {
	                    writer.write('\n');
	                }
	                indent(writer, indent);
	            }
	            writer.write('}');
	            return writer;
	        } catch (IOException exception) {
	            throw new JSONException(exception);
	        }
	    }
	 
	  Writer write(Writer writer, int indentFactor, int indent)
	            throws JSONException {
	        try {
	            boolean commanate = false;
	            int length = this.length();
	            writer.write('[');

	            if (length == 1) {
	                JSONObject.writeValue(writer, this.myArrayList.get(0),
	                        indentFactor, indent);
	            } else if (length != 0) {
	                final int newindent = indent + indentFactor;

	                for (int i = 0; i < length; i += 1) {
	                    if (commanate) {
	                        writer.write(',');
	                    }
	                    if (indentFactor > 0) {
	                        writer.write('\n');
	                    }
	                    JSONObject.indent(writer, newindent);
	                    JSONObject.writeValue(writer, this.myArrayList.get(i),
	                            indentFactor, newindent);
	                    commanate = true;
	                }
	                if (indentFactor > 0) {
	                    writer.write('\n');
	                }
	                JSONObject.indent(writer, indent);
	            }
	            writer.write(']');
	            return writer;
	        } catch (IOException e) {
	            throw new JSONException(e);
	        }
	    }
	 
	 static final Writer writeValue(Writer writer, Object value,int indentFactor, int indent) throws JSONException, IOException {
	        if (value == null || value.equals(null)) {
	            writer.write("null");
	        } else if (value instanceof JSONObject) {
	            ((JSONObject) value).write(writer, indentFactor, indent);
	        } else if (value instanceof JSONArray) {
	            ((JSONArray) value).write(writer, indentFactor, indent);
	        } else if (value instanceof Map) {
	            new JSONObject((Map<String, Object>) value).write(writer, indentFactor, indent);
	        } else if (value instanceof Collection) {
	            new JSONArray((Collection<Object>) value).write(writer, indentFactor,
	                    indent);
	        } else if (value.getClass().isArray()) {
	            new JSONArray(value).write(writer, indentFactor, indent);
	        } else if (value instanceof Number) {
	            writer.write(JSONObject.numberToString((Number) value));
	        } else if (value instanceof Boolean) {
	            writer.write(value.toString());
	        } else if (value instanceof JSONString) {
	            Object o;
	            try {
	                o = ((JSONString) value).toJSONString();
	            } catch (Exception e) {
	                throw new JSONException(e);
	            }
	            writer.write(o != null ? o.toString() : quote(value.toString()));
	        } else {
	            quote(value.toString(), writer);
	        }
	        return writer;
	    }
	 
	
	 
	 
	 static final void indent(Writer writer, int indent) throws IOException {
	        for (int i = 0; i < indent; i += 1) {
	            writer.write(' ');
	        }
	    }
	

	 public static String quote(String string) {
	        StringWriter sw = new StringWriter();
	        synchronized (sw.getBuffer()) {
	            try {
	                return quote(string, sw).toString();
	            } catch (IOException ignored) {
	                // will never happen - we are writing to a string writer
	                return "";
	            }
	        }
	    }

	  public static Writer quote(String string, Writer w) throws IOException {
	        if (string == null || string.length() == 0) {
	            w.write("\"\"");
	            return w;
	        }

	        char b;
	        char c = 0;
	        String hhhh;
	        int i;
	        int len = string.length();

	        w.write('"');
	        for (i = 0; i < len; i += 1) {
	            b = c;
	            c = string.charAt(i);
	            switch (c) {
	            case '\\':
	            case '"':
	                w.write('\\');
	                w.write(c);
	                break;
	            case '/':
	                if (b == '<') {
	                    w.write('\\');
	                }
	                w.write(c);
	                break;
	            case '\b':
	                w.write("\\b");
	                break;
	            case '\t':
	                w.write("\\t");
	                break;
	            case '\n':
	                w.write("\\n");
	                break;
	            case '\f':
	                w.write("\\f");
	                break;
	            case '\r':
	                w.write("\\r");
	                break;
	            default:
	                if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
	                        || (c >= '\u2000' && c < '\u2100')) {
	                    w.write("\\u");
	                    hhhh = Integer.toHexString(c);
	                    w.write("0000", 0, 4 - hhhh.length());
	                    w.write(hhhh);
	                } else {
	                    w.write(c);
	                }
	            }
	        }
	        w.write('"');
	        return w;
	    }*/
}
