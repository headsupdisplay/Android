package com.minhud.demo1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class Net
{
	private static final String HOST = "http://quiet-sands-7804.herokuapp.com/votes";

	private static String get(String path)
	{
		HttpResponse response = null;
		try
		{
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(HOST));
			response = client.execute(request);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try
		{
			InputStream stream = response.getEntity().getContent();
			return convertStreamToString(stream);
		}
		catch(Exception e)
		{
			return "";
		}
	}

	public static String convertStreamToString(InputStream inputStream) throws IOException
	{
		if (inputStream != null)
		{
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
				}
			}
			finally
			{
				inputStream.close();
			}
			return writer.toString();
		}
		else
		{
			return "";
		}
	}

	public static String getServerDirection()
	{
		String html = get(HOST);
		if (html.contains("minhudleft")) return "left";
		else if (html.contains("minhudright")) return "right";
		else if (html.contains("minhudforward")) return "forward";
		else if (html.contains("minhudbackward")) return "backwards";
		else return "";
	}
}
