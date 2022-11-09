package ch2_stream;

import ch0_base.User;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Ch4_toMap {
  public static void main(String[] args) {
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

    Map<Long, User> userMap = users.stream()
      .collect(Collectors.toMap(User::getId, Function.identity()));
    System.out.println(userMap);
  }
}
