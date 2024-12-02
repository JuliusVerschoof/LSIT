package lsit.Repositories;

import java.util.*;
import lsit.Models.Beverage;

public interface IBeverageRepository {
    void add(Beverage beverage);
    Beverage get(UUID id);
    void remove(UUID id);
    void update(Beverage updatedBeverage);
    List<Beverage> list();
}
