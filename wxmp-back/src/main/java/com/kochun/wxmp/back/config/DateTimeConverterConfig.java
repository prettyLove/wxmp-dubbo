package com.kochun.wxmp.back.config;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.apache.commons.io.Charsets;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author alex
 * @ClassNameJava8DateTimeConfiguration
 * @Description //TODO
 * @Author kochun
 * @Date 2019-10-27 15:43
 * @Version V1.0
 **/
@Configuration
public class DateTimeConverterConfig implements WebMvcConfigurer {

    /** 默认日期时间格式 */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 默认日期格式 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /** 默认时间格式 */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";


    /**
     * 统一json输出风格
     * Json序列化和反序列化转换器，用于转换Post请求体中的json以及将我们的对象序列化为返回响应的json
     * See {@link com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy} for details.
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charsets.UTF_8);
                stringHttpMessageConverter.setWriteAcceptCharset(false);
                converters.set(i, stringHttpMessageConverter);
            }
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objectMapper = new ObjectMapper();

                //SimpleModule simpleModule = new SimpleModule("JsonMapSerializer", Version.unknownVersion());

                //LocalDateTime系列序列化和反序列化模块，继承自jsr310，我们在这里修改了日期格式
                JavaTimeModule simpleModule = new JavaTimeModule();

                simpleModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
                simpleModule.addSerializer(LocalDate.class,new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
                simpleModule.addSerializer(LocalTime.class,new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));


                // 反序列化
                simpleModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
                simpleModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
                simpleModule.addDeserializer(LocalTime.class,new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));


                objectMapper.registerModule(simpleModule);
                // 统一返回数据的输出风格

                //将javaBean属性的驼峰格式 转换成_格式，例如：userName转成成user_name
                //objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());

                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                converter.setObjectMapper(objectMapper);
                converters.set(i, converter);
                break;
            }
        }
    }

    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(@NotNull String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            }
        };
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(@NotNull String source) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
            }
        };
    }

    /**
     * LocalTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(@NotNull String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
            }
        };
    }

    /**
     * Date转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(@NotNull String source) {
                SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
                try {
                    return format.parse(source);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}