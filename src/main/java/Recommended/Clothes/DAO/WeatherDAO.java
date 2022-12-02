package Recommended.Clothes.DAO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Component
public class WeatherDAO {

	public HashMap<String, Object> getDataFromJson(String url, String encoding, String type, String jsonStr) throws Exception {
		boolean isPost = false;

		if ("post".equals(type)) {
			isPost = true;
		} else {
			url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;
		}

		return getStringFromURL(url, encoding, isPost, jsonStr, "application/json");
	}

	public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter, String contentType) throws Exception {
		URL apiURL = new URL(url);

		HttpURLConnection conn = null;
		BufferedReader br = null;
		BufferedWriter bw = null;

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			conn = (HttpURLConnection) apiURL.openConnection();
			conn.setConnectTimeout(20000);
			conn.setReadTimeout(5000);
			conn.setDoOutput(true);

			if (isPost) {
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", contentType);
				conn.setRequestProperty("Accept", "*/*");
			} else {
				conn.setRequestMethod("GET");
			}

			conn.connect();

			if (isPost) {
				bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
				bw.write(parameter);
				bw.flush();
				bw = null;
			}

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));

			String line = null;

			StringBuffer result = new StringBuffer();

			while ((line = br.readLine()) != null) result.append(line);

			ObjectMapper mapper = new ObjectMapper();

			resultMap = mapper.readValue(result.toString(), HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(url + " interface failed" + e.toString());
		} finally {
			if (conn != null) conn.disconnect();
			if (br != null) br.close();
			if (bw != null) bw.close();
		}

		return resultMap;
	}

}
