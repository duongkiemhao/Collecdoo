package com.collecdoo.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;


public class NetworkHelper {


	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
//
//	public static String getStringFromResponse(HttpResponse response)
//			throws IOException {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		response.getEntity().writeTo(out);
//		out.close();
//		response.getEntity().consumeContent();
//		return out.toString();
//	}

//	public static void closeResponse(HttpResponse response) {
//		if (response.getEntity() != null)
//			try {
//				response.getEntity().consumeContent();
//			} catch (IOException e) {
//			}
//	}

	public static String getJsonString(JSONObject object, String key) {
		try {
			return object.getString(key);
		} catch (JSONException e) {
			return "";
		}
	}
//
//	public static String getConnectParaString(List<NameValuePair> params)
//			throws UnsupportedEncodingException {
//		StringBuilder result = new StringBuilder();
//		boolean first = true;
//
//		for (NameValuePair pair : params) {
//			if (first)
//				first = false;
//			else
//				result.append("&");
//
//			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
//			result.append("=");
//			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
//		}
//
//		return result.toString();
//	}

	public static StringBuilder readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuilder sb = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String line = "";
			sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + '\n');
			}
			return sb;
		} catch (IOException e) {
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			sb = null;
		}
	}

//	public static void setConnectionHeader(HttpURLConnection con) {
//		Date date = new Date();
//		con.setRequestProperty(Constant.T_TIMEZONE, Global.getTimeZoon());
//		con.setRequestProperty(Constant.T_API_KEY, Config.API_KEY);
//		con.setRequestProperty(Constant.T_UDID, Utility.getMacAddess());
//		con.setRequestProperty(Constant.T_REQUEST_DATE, DateHelper
//				.parseDateToString(date, DateHelper.SERVER_FORMAT));
//
//		String secret_key = Utility.getMacAddess() + "-" + Config.API_KEY + "-"
//				+ Config.SHARE_SECRET;
//		String t_token = Utility.getTokenKey(
//				secret_key, DateHelper.parseDateToString(date,
//						DateHelper.SERVER_FORMAT));
//		con.setRequestProperty(Constant.T_TOKEN, t_token);
//	}

//	public static void setConnectionAuthorization(HttpURLConnection con) {
//        String[] credential=MyPreference.getLoginCredential();
//        Log.e("credential", credential[0]+" - "+credential[1]);
//        String authen = Base64.encodeToString(
//				(credential[0]+":"+credential[1]).getBytes(),
//				Base64.NO_WRAP);
//		con.setRequestProperty(
//				"Authorization",
//				"Basic "
//						+ Base64.encodeToString(
//								(credential[0]+":"+credential[1]).getBytes(),
//								Base64.NO_WRAP));
//
//	}
//    public static void setConnectionAuthorizationResentEmail(HttpURLConnection con,String username,String password) {
//
//        con.setRequestProperty(
//                "Authorization",
//                "Basic "
//                        + Base64.encodeToString(
//                        (username+":"+password).getBytes(),
//                        Base64.NO_WRAP));
//
//    }
    public static void writeOutputStream(HttpURLConnection con,String content) throws  IOException{
        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(content);
        writer.close();
        os.close();
    }
}
