package com.zwx.bike.security;

import com.zwx.bike.cache.CommonCacheUtil;
import com.zwx.bike.common.constants.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Create By Zhang on 2018/3/17
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private Parameters parameters;

    @Autowired
    private CommonCacheUtil commonCacheUtil;

    private  RestPreAuthentucatedProcessingFilter getPreAuthentucatedProcessingFilter() throws Exception {
        RestPreAuthentucatedProcessingFilter filter=new RestPreAuthentucatedProcessingFilter(parameters.getNoneSecurityPath(),commonCacheUtil);
        filter.setAuthenticationManager(this.authenticationManagerBean());


        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new RestAuthenticationProvider()) ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()     /**csrf跨站脚本攻击,相当于伪造表单,这里把它关了*/
                .authorizeRequests()
                .antMatchers(parameters.getNoneSecurityPath().toArray(new String[parameters.getNoneSecurityPath().size()])).permitAll()/** 把拿到的list转成String，符合条件的路径放过验证*/
                .anyRequest().permitAll()     /**这里先改成permitAll,方便测试，开发完成后再改回authenticated()*/
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)        /**控制创建session策略*/
                .and().httpBasic().authenticationEntryPoint(new RestAuthenticationEntryPoint())     /**添加自定义entrypoint*/
                .and().addFilter(getPreAuthentucatedProcessingFilter())                  /**添加自定义的filter*/


        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS,"/**");  /**忽略OPTION方法的请求*/

    }
}
