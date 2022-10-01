package ch0_base;

public class User {
  private Long id;
  private String name;
  private boolean verified;
  private String emailAddress;

  public User() {}

  public User(Long id, String name, boolean verified, String emailAddress) {
    this.id = id;
    this.name = name;
    this.verified = verified;
    this.emailAddress = emailAddress;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", verified=" + verified +
      ", emailAddress='" + emailAddress + '\'' +
      '}';
  }
}

