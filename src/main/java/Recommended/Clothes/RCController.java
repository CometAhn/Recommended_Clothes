package Recommended.Clothes;

import Recommended.Clothes.DAO.CityDAO;
import Recommended.Clothes.DAO.WeatherDAO;
import Recommended.Clothes.Entity.City;
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
import java.util.*;

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

		int i = 0;
		while (true) {
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

					Calendar calendar = Calendar.getInstance();

					String outtime;
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
					String nowdate = sdf.format(date);

					// 결과 출력
					System.out.println(nowdate);

					// 현재 시간
					LocalTime time = LocalTime.now();

					// 시
					// 초단기실황은 그냥 그 시 출력하면된다.
					int hour = time.getHour();

					// 정시에 돌리면 요청을 받아도 결과를 못 받아 올 경우가 있음.
					// 이럴경우 이전 시간으로 재요청하여 결과를 받아온다.

					System.out.println("이전 시간" + hour);

					if (hour < 10) {
						if (hour == 0) {
							calendar.add(Calendar.DATE, -1);
							nowdate = sdf.format(calendar.getTime());
							outtime = "2300";
						} else {
							outtime = "0" + Integer.toString(hour - i) + "00";
						}
					} else {
						outtime = Integer.toString(hour - i) + "00";
					}
				/*
				int i = 1;

				if (hour < 5) {
					calendar.add(Calendar.DATE, -1);
					nowdate = sdf.format(calendar.getTime());
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
				*/

					// 시, 분, 초 출력
					System.out.println("시간" + outtime);

				/*
				@ API LIST ~

				getUltraSrtNcst 초단기실황조회
				getUltraSrtFcst 초단기예보조회
				getVilageFcst 동네예보조회
				getFcstVersion 예보버전조회
				*/

					String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"
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

					System.out.println(parse_response);

					JSONObject parse_header = (JSONObject) parse_response.get("header");

					String parse_resultCode = (String) parse_header.get("resultCode");

					System.out.println("결과값 : " + parse_resultCode);

					if (parse_resultCode.equals("00")) {

						// response 로 부터 body 찾아옵니다.
						JSONObject parse_body = (JSONObject) parse_response.get("body");
						// body 로 부터 items 받아옵니다.
						JSONObject parse_items = (JSONObject) parse_body.get("items");

						// items로 부터 itemlist 를 받아오기 itemlist : 뒤에 [ 로 시작하므로 jsonarray이다
						JSONArray parse_item = (JSONArray) parse_items.get("item");


						System.out.println("# RESULT : " + parse_item);

						String category;
						JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용합니다.

						// 필요한 데이터만 가져오려고합니다.
						for (int j = 0; j < parse_item.length(); j++) {
							weather = (JSONObject) parse_item.get(j);
							String baseTime = (String) weather.get("baseTime");
							//String fcst_Timecst_Value = ((String) weather.get("fcstValue")); //실수로된 값과 정수로된 값이 둘다 있어서 실수로 통일했습니다.
							String obsrValue = (String) weather.get("obsrValue");
							category = (String) weather.get("category");

							// 출력합니다.
							System.out.print("배열의 " + j + "번째 요소");
							System.out.print("   시간 : " + baseTime);
							System.out.print("   카테고리 : " + category);
							System.out.print("   값 : " + obsrValue);
							System.out.println();

							if (j == 1) {
						/*StringBuffer buf = new StringBuffer(baseTime);
						buf.insert(2, "시 ");
						buf.insert(6, "분");*/

								baseTime = baseTime.substring(0, 2) + "시 정각";

								jObject.put("baseTime", baseTime);
							}
							if (category.equals("T1H")) { // 온도
								jObject.put("temp", obsrValue);
								if (Double.parseDouble(obsrValue) > 28)
									jObject.put("info", "민소매, 반팔, 반바지, 원피스");
								else if (Double.parseDouble(obsrValue) > 23)
									jObject.put("info", "반팔, 얇은 셔츠, 반바지, 면바지");
								else if (Double.parseDouble(obsrValue) > 20)
									jObject.put("info", "얇은 가디건, 긴팔, 면바지, 청바지");
								else if (Double.parseDouble(obsrValue) > 17)
									jObject.put("info", "얇은 니트, 맨투맨, 가디건, 청바지");
								else if (Double.parseDouble(obsrValue) > 12)
									jObject.put("info", "자켓, 가디건, 야상, 스타킹, 청바지, 면바지");
								else if (Double.parseDouble(obsrValue) > 9)
									jObject.put("info", "자켓, 트렌치코트, 야상, 니트, 청바지, 스타킹");
								else if (Double.parseDouble(obsrValue) > 5)
									jObject.put("info", "코트, 가죽자켓, 히트텍, 니트, 레깅스");
								if (Double.parseDouble(obsrValue) < 5)
									jObject.put("info", "패딩, 두꺼운 코트, 목도리, 기모제품");
							}
							if (category.equals("REH")) // 습도
								jObject.put("reh", obsrValue);
							if (category.equals("WSD")) // 풍속 m/s
								jObject.put("wsd", obsrValue);
							if (category.equals("PTY")) {// 강수형태
								jObject.put("pty", obsrValue);

						/*
						if (obsrValue.equals("0"))
							jObject.put("pty", "없음");
						if (obsrValue.equals("1"))
							jObject.put("pty", "비");
						if (obsrValue.equals("2"))
							jObject.put("pty", "비/눈");
						if (obsrValue.equals("3"))
							jObject.put("pty", "눈");
						if (obsrValue.equals("4"))
							jObject.put("pty", "소나기");
							*/
							}
							if (category.equals("RN1")) // 한시간 강수량 mm
								jObject.put("rn1", obsrValue);
							if (category.equals("UUU")) // 동서바람성분 m/s
								jObject.put("uuu", obsrValue);
							if (category.equals("VVV")) // 동서바람성분 m/s
								jObject.put("vvv", obsrValue);
							if (category.equals("VEC")) {// 풍향 deg
								jObject.put("vec", obsrValue);
								int deg = Integer.parseInt(obsrValue);

						/*
						338 ~ 23 북
						23 ~ 68 북동
						68 ~ 113 동
						113 ~ 158 남동
						158 ~ 203 남
						203 ~ 248 남서
						248 ~ 293 서
						293 ~ 338 북서
						 */

								if ((deg <= 0)) { // 방위
									jObject.put("compass", "북풍");
								} else if (deg <= 23) {
									jObject.put("compass", "북동풍");
								} else if (deg <= 68) {
									jObject.put("compass", "동풍");
								} else if (deg <= 113) {
									jObject.put("compass", "남동풍");
								} else if (deg <= 158) {
									jObject.put("compass", "남풍");
								} else if (deg <= 203) {
									jObject.put("compass", "남서풍");
								} else if (deg <= 248) {
									jObject.put("compass", "서풍");
								} else if (deg <= 293) {
									jObject.put("compass", "북서풍");
								} else if (deg <= 338) {
									jObject.put("compass", "북풍");
								}
							}


						}
						out.print(jObject);
						break;
					} else {

						jObject.put("errorcheck", "1");
						jObject.put("errorhour", hour);
						i++;
						continue;
					}
				} else {
					jObject.put("local", "error");
					out.print(jObject);
					break;
				}
			} else {
				jObject.put("local", "error");
				out.print(jObject);
				break;

			}
		}

	}
}
