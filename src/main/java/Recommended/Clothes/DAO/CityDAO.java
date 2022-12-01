package Recommended.Clothes.DAO;

import Recommended.Clothes.Entity.City;
import Recommended.Clothes.Repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityDAO {
	@Autowired
	private CityRepository cityRepository;

	// 모든 local 조회
	public List<City> getAll() {
		return cityRepository.findAll();
	}

	// 특정 local 조회
	public City getLocal(String city, String local) {
		return cityRepository.findByCityAndLocal(city, local);
	}
}
