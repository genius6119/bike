package com.zwx.bike;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@PropertySource(value = "classpath:parameter.properties")

public class BikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeApplication.class, args);
	}

	/**
	 * @Author Zhang
	 * @Date 2018/2/8 23:01
	 * @Description  将默认改成fastjson
	 */

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters(){
		FastJsonHttpMessageConverter fastConverter =new FastJsonHttpMessageConverter();
		HttpMessageConverter<?> converter=fastConverter;
		return  new HttpMessageConverters(converter);
	}
	/**
	 * 用于properties文件占位符#解析
	 * */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev(){
		return new PropertySourcesPlaceholderConfigurer();
	}

}
