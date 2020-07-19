package rocks.turncodr.mycurriculum.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import rocks.turncodr.mycurriculum.error.LoginAccessDeniedHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spring securtiy configuration class.
 * Handles all non-secured and secured pages.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginAccessDeniedHandler accessDeniedHandler;

    private BCryptPasswordEncoder encoder;

    private final int encoderStrength = 10;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(
                "/home",
                "/curriculum/list",
                "/exreg/list",
                "/exreg/overview",
                "/exreg/syllabus",
                "/exreg/syllabus/pdf",
                "/module/list",
                "/module/details/**",
                "/css/**",
                "/img/**",
                "/js/**",
                "/fragments/**",
                "/webfonts/**").permitAll()
                .antMatchers("/admin/**").hasRole("[ADMIN]")
                .anyRequest().authenticated().and().formLogin().loginPage("/admin/login").loginProcessingUrl("/admin/login")
                .successForwardUrl("/admin").defaultSuccessUrl("/admin").permitAll()
                .and().logout().invalidateHttpSession(true).clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
                .logoutSuccessUrl("/admin/login?logout").permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        List<User> userList = createUsers();
        for (User user : userList) {
            auth.inMemoryAuthentication().withUser(user.getUsername()).password(user.getPassword()).roles(String.valueOf(user.getAuthorities()));
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return encoder;
    }

    private List<User> createUsers() {
        List<User> userList = new ArrayList<>();
        encoder = new BCryptPasswordEncoder(encoderStrength);
        //Password safed with encoding MD5 (raw password: password)
        userList.add(new User("Admin", encoder.encode("5f4dcc3b5aa765d61d8327deb882cf99"),
                Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        userList.add(new User("Dekan", encoder.encode("5f4dcc3b5aa765d61d8327deb882cf99"),
                Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        return userList;
    }
}
