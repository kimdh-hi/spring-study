package ch2_stream;

import ch0_base.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Ch1_max_min_count {
  public static void main(String[] args) {
    /**
     * max
     */
    Optional<Integer> max = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
//      .max((x, y) -> x - y);
//      .max(Comparator.comparingInt(x -> x));
      .max(Integer::compareTo);
    System.out.println("max: " + max.get());

    User user1 = new User();
    user1.setId(1L);
    user1.setName("ccc");
    user1.setEmailAddress("ccc@gmail.com");
    user1.setVerified(true);

    User user2 = new User();
    user2.setId(2L);
    user2.setName("bbb");
    user2.setEmailAddress("bbb@gmail.com");
    user2.setVerified(false);

    User user3 = new User();
    user3.setId(3L);
    user3.setName("aaa");
    user3.setEmailAddress("aaa@gmail.com");
    user3.setVerified(true);
    List<User> users = List.of(user1, user2, user3);

    /**
     * min
     */
    User user = users.stream()
//      .min((u1, u2) -> u1.getName().compareTo(u2.getName())) // 오름차순
      .min(Comparator.comparing(User::getName).reversed()) // 내림차순
      .get();
    System.out.println("min: " + user);

    /**
     * count
     */
    long evenNumCount = Stream.of(2,4,6,7,8,9)
      .filter(x -> x % 2 == 0)
      .count();
    System.out.println("count: " + evenNumCount);
  }
}
