## Spring Security

### WebSecurity - HttpSecurity

WebSecurity 에 지정되는 경로는 Security filter chain 을 타지 않는다. <br/>
동일한 경로가 WebSecurity, HttpSecurity 모두 적용된 경우 WebSecurity 가 우선순위를 갖고 HttpSecurity 의 인가설정은 무시된다. <br/>