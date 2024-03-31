package org.example.payment.paymentservice.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll() // Cho phép tất cả mọi người truy cập các URL bắt đầu bằng /public/
                .anyRequest().authenticated() // Những URL còn lại đều cần xác thực
                .and()
                .formLogin() // Cho phép người dùng xác thực bằng form login
                .loginPage("/login") // Đường dẫn đến trang đăng nhập
                .permitAll() // Cho phép tất cả mọi người truy cập
                .and()
                .logout() // Cho phép logout
                .permitAll(); // Cho phép tất cả mọi người truy cập
    }

}
