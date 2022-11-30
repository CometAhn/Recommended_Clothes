package Recommended.Cothes;

import Recommended.Cothes.DAO.CityDAO;
import Recommended.Cothes.DAO.WeatherDAO;
import Recommended.Cothes.Entity.City;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Controller
public class RCController {



	final WeatherDAO dao;
	final CityDAO cdao;

	@Autowired
	public RCController(WeatherDAO dao, CityDAO cdao) {
		this.dao = dao;
		this.cdao = cdao;
	}

	@GetMapping("/weather")
	public String restApiGetWeather() throws Exception {

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
				+ "&numOfRows=10"             // 페이지 ROWS
				+ "&pageNo=1"                 // 페이지 번호
				+ "&base_date=" + nowdate      // 발표일자
				+ "&base_time=" + outtime           // 발표시각
				+ "&nx=88"                    // 예보지점 X 좌표
				+ "&ny=77";                  // 예보지점 Y 좌표

		HashMap<String, Object> resultMap = dao.getDataFromJson(url, "UTF-8", "get", "");

		System.out.println("# RESULT : " + resultMap);

		JSONObject jsonObj = new JSONObject();

		jsonObj.put("result", resultMap);

		return jsonObj.toString();
	}


	@GetMapping("")
	public String autocomplete(Model m) throws Exception {

		List<City> result = cdao.getAll();
		int i = 0;
		String val[] = new String[3792];
		for (City check : result) {
			if (!check.getLocal().equals("") || !check.getLocal().equals("null")) {
				val[i] = check.getLocal();
				i++;
			}
		}

		m.addAttribute("val", val);

		return "/index";
	}
}
