package com.qingtao;

import com.qingtao.filter.EncodingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan
public class DemoApplication {
        @Bean
		public FilterRegistrationBean registrationBean()
		{
            FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
            filterRegistrationBean.setFilter(this.encodingFilter());
			filterRegistrationBean.addUrlPatterns("/*");
		    filterRegistrationBean.addInitParameter("encoder","utf-8");
			filterRegistrationBean.setName("EncodingFilter");
		     filterRegistrationBean.setOrder(1);
		     return filterRegistrationBean;
		}
	@Bean(name = "EncodingFilter")
		public EncodingFilter encodingFilter()
		{
			return new EncodingFilter();
		}
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
