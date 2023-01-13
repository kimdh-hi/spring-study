### @JsonComponent 로 등록한 Serializer 가 동작하지 않는 경우

왜 등록이 되지 않는지는 모르겠음.

`SerializerProvider.findTypedValueSerializer` 에서 등록한 custom serializer 가 보이지 않는 경우 @JsonComponent 가 제대로 동작하지 않는 것으로 판단..

해결
```kotlin
@Configuration
class MvcConfig : WebMvcConfigurer {

  override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
    super.configureMessageConverters(converters)
    val builder = Jackson2ObjectMapperBuilder()
    builder.serializerByType(BindingResult::class.java, BindingResultSerializer())
    converters.add(MappingJackson2HttpMessageConverter(builder.build()))
  }
}
```
직접 등록..