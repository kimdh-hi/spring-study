package hello.datasource;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Getter
@ConfigurationProperties("my.datasource")
@Validated
public class MyDataSourcePropertiesV3 {

  @NotEmpty
  private String url;

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;


  private Etc etc;

  @Getter
  public static class Etc {
    @Min(1)
    @Max(99)
    private int maxConnection;

    @DurationMin(seconds = 1)
    @DurationMax(hours = 1)
    private Duration timeout;

    private List<String> options = new ArrayList<>();

    public Etc(int maxConnection, Duration timeout, @DefaultValue({"OptionA", "OptionB"}) List<String> options) {
      this.maxConnection = maxConnection;
      this.timeout = timeout;
      this.options = options;
    }
  }

  public MyDataSourcePropertiesV3(String url, String username, String password, Etc etc) {
    this.url = url;
    this.username = username;
    this.password = password;
    this.etc = etc;
  }
}
