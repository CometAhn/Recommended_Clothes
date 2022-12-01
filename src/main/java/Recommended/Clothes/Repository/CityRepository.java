package Recommended.Clothes.Repository;

import Recommended.Clothes.Entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {

	List<City> findAll();

	City findByCityAndLocal(String city_sub, String local);

}
