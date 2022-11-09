package ch1_functionalInterface;

import ch1_functionalInterface.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ch7_Comparator {
  public static void main(String[] args) {
    List<User> users = new ArrayList<>();
    users.add(new User(1, "kim"));
    users.add(new User(3, "lee"));
    users.add(new User(2, "park"));

    Comparator<User> userIdComparator = (u1, u2) -> u1.getId() - u2.getId(); // id 기준 오름차순 정렬
    Collections.sort(users, userIdComparator);
    System.out.println(users);

    Collections.sort(users, (u1, u2) -> u2.getName().compareTo(u1.getName())); // 이름 기준 내림차순 정렬
    System.out.println(users);
  }
}
