package Recommended.Cothes.DAO;

import Recommended.Cothes.Entity.City;
import Recommended.Cothes.Repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityDAO {
    @Autowired
    private CityRepository cityRepository;

    // 조회
    public List<City> getAll() {
        return cityRepository.findAll();
    }
}
