package Recommended.Cothes;

import Recommended.Cothes.DAO.CityDAO;
import Recommended.Cothes.DAO.WeatherDAO;
import Recommended.Cothes.Entity.City;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Controller
@CrossOrigin
public class RCController {


	final WeatherDAO dao;
	final CityDAO cdao;

	@Autowired
	public RCController(WeatherDAO dao, CityDAO cdao) {
		this.dao = dao;
		this.cdao = cdao;
	}
	
	@GetMapping("")
	public String autocomplete(Model m) throws Exception {

		List<City> result = cdao.getAll();
		int i = 0;
		String val[] = new String[3792];
		for (City check : result) {
			if (!check.getLocal().equals("") || !check.getLocal().equals("null")) {
				val[i] = check.getCity() + " " + check.getLocal();
				i++;
			}
		}

		m.addAttribute("val", val);

		return "/index";
	}


	// Local로 날씨 조회하자.
	@PostMapping("/load")
	public void loadweather(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		City city = null;
		System.out.println("값 잘 나와? : " + request.getParameter("id"));
		String[] str = request.getParameter("id").split(" ");

		PrintWriter out = response.getWriter();
		JSONObject jObject = new JSONObject();

		if (str.length == 2) {
			city = cdao.getLocal(str[0], str[1]);

			if (city != null) {

				//배열을 저장할 jObject


				System.out.println("값 잘 나오니? x : " + city.getX() + ", y : " + city.getY());

				jObject.put("local", city.getLocal());
				jObject.put("city", city.getState());
				jObject.put("city_sub", city.getCity());
				jObject.put("position_x", city.getX());
				jObject.put("position_y", city.getY());


        /*
            @ API LIST ~

            getUltraSrtNcst 초단기실황조회
            getUltraSrtFcst 초단기예보조회
            getVilageFcst 동네예보조회
            getFcstVersion 예보버전조회
        */

				String outtime;

				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
				String nowdate = sdf.format(date);

				// 결과 출력
				System.out.println(nowdate);

				// 현재 시간
				LocalTime time = LocalTime.now();

				// 시
				// 시는 05, 08, 11, 14, 17, 20, 23으로 표현돼야 함.
				int hour = time.getHour();

				System.out.println("이전 시간" + hour);

				int i = 1;

				if (hour < 5) {
					hour = 23;
					outtime = Integer.toString(hour) + "00";
				} else {
					while (true) {
						int x = (i * 3) + 2;
						if (x > hour) {
							x = ((i - 1) * 3) + 2;
							if (x < 10) {
								outtime = "0" + Integer.toString(x) + "00";
								break;
							} else {
								outtime = Integer.toString(x) + "00";
								break;
							}

						}
						i++;
						System.out.println("도는중");
						System.out.println(i);

					}
				}


				// 시, 분, 초 출력
				System.out.println("시간" + outtime);

				String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
						+ "?serviceKey=ndK0k4so%2BUvfMY5Oh7XCWIREWTAOm%2BS%2BcE1eMJA9tXyNoTRgxC7kPb7CEqwmq4d%2FqC2BYsipas9jsbR7OkjI%2FA%3D%3D"
						+ "&dataType=JSON"            // JSON, XML
						+ "&numOfRows=13"             // 페이지 ROWS
						+ "&pageNo=1"                 // 페이지 번호
						+ "&base_date=" + nowdate      // 발표일자
						+ "&base_time=" + outtime           // 발표시각
						+ "&nx=" + city.getX()                    // 예보지점 X 좌표
						+ "&ny=" + city.getY();                  // 예보지점 Y 좌표

				HashMap<String, Object> resultMap = null;
				try {

					resultMap = dao.getDataFromJson(url, "UTF-8", "get", "");
				} catch (Exception e) {

					jObject.put("local", "error");
				}

				System.out.println("# RESULT : " + resultMap);
				JSONObject json = new JSONObject(resultMap);
/*
				JSONObject post1Object = json.getJSONObject("response");
				JSONObject post1Object1 = post1Object.getJSONObject("body");
				JSONObject post1Object2 = post1Object1.getJSONObject("items");
				System.out.println("원하는값 나오니? : " + post1Object2);

				// items로 부터 itemlist 를 받아오기 itemlist : 뒤에 [ 로 시작하므로 jsonarray이다
				JSONArray parse_item = (JSONArray) post1Object2.get("item");
*/
				// Top레벨 단계인 response 키를 가지고 데이터를 파싱합니다.
				JSONObject parse_response = json.getJSONObject("response");
				// response 로 부터 body 찾아옵니다.
				JSONObject parse_body = (JSONObject) parse_response.get("body");
				// body 로 부터 items 받아옵니다.
				JSONObject parse_items = (JSONObject) parse_body.get("items");

				// items로 부터 itemlist 를 받아오기 itemlist : 뒤에 [ 로 시작하므로 jsonarray이다
				JSONArray parse_item = (JSONArray) parse_items.get("item");

				String category;
				JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용합니다.

				// 필요한 데이터만 가져오려고합니다.
				for (int j = 0; j < parse_item.length(); j++) {
					weather = (JSONObject) parse_item.get(j);
					//String base_Date = (String) weather.get("baseDate");
					String fcst_Time = (String) weather.get("fcstTime");
					String fcst_Value = ((String) weather.get("fcstValue")); //실수로된 값과 정수로된 값이 둘다 있어서 실수로 통일했습니다.
					//String nX = (String)weather.get("nx");
					//String nY = (String)weather.get("ny");
					category = (String) weather.get("category");
					//String base_Time = (String)weather.get("baseTime");
					//String fcscDate = (String)weather.get("fcscDate");

					// 출력합니다.
					System.out.print("배열의 " + j + "번째 요소");
					System.out.print("   시간 : " + fcst_Time);
					System.out.print("   카테고리 : " + category);
					System.out.print("   값 : " + fcst_Value);
					System.out.println();

					if (category.equals("TMP")) { // 온도
						jObject.put("temp", fcst_Value);
						if (Integer.parseInt(fcst_Value) > 28)
							jObject.put("info", "민소매, 반팔, 반바지, 원피스");
						else if (Integer.parseInt(fcst_Value) > 23)
							jObject.put("info", "반팔, 얇은 셔츠, 반바지, 면바지");
						else if (Integer.parseInt(fcst_Value) > 20)
							jObject.put("info", "얇은 가디건, 긴팔, 면바지, 청바지");
						else if (Integer.parseInt(fcst_Value) > 17)
							jObject.put("info", "얇은 니트, 맨투맨, 가디건, 청바지");
						else if (Integer.parseInt(fcst_Value) > 12)
							jObject.put("info", "자켓, 가디건, 야상, 스타킹, 청바지, 면바지");
						else if (Integer.parseInt(fcst_Value) > 9)
							jObject.put("info", "자켓, 트렌치코트, 야상, 니트, 청바지, 스타킹");
						else if (Integer.parseInt(fcst_Value) > 5)
							jObject.put("info", "코트, 가죽자켓, 히트텍, 니트, 레깅스");
						if (Integer.parseInt(fcst_Value) < 5)
							jObject.put("info", "패딩, 두꺼운 코트, 목도리, 기모제품");

					}
					if (category.equals("WSD")) // 풍속 m/s
						jObject.put("wsd", fcst_Value);
					if (category.equals("SKY")) { // 하늘상태
						if (fcst_Value.equals("1"))
							jObject.put("sky", "맑음");
						if (fcst_Value.equals("3"))
							jObject.put("sky", "구름많음");
						if (fcst_Value.equals("4"))
							jObject.put("sky", "흐림");
					}
					if (category.equals("PTY")) {// 강수형태
						if (fcst_Value.equals("0"))
							jObject.put("pty", "없음");
						if (fcst_Value.equals("1"))
							jObject.put("pty", "비");
						if (fcst_Value.equals("2"))
							jObject.put("pty", "비/눈");
						if (fcst_Value.equals("3"))
							jObject.put("pty", "눈");
						if (fcst_Value.equals("4"))
							jObject.put("pty", "소나기");
					}

				}

				JSONObject jsonObj = new JSONObject();

				jsonObj.put("result", resultMap);
			} else {
				jObject.put("local", "error");
			}
		} else {
			jObject.put("local", "error");


		}

		out.print(jObject);

	}
}
