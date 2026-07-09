package it.uniroma3.siw.siwfootball.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import it.uniroma3.siw.siwfootball.model.Utente;


@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    private final DataSource dataSource;


    public AuthConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
            "SELECT username, password, 1 as enabled FROM utente WHERE username=?");
        manager.setAuthoritiesByUsernameQuery(
            "SELECT username, ruolo FROM utente WHERE username=?");
        return manager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Definisce la catena di filtri di sicurezza: regole di autorizzazione, login e logout.
     *
     * Regole di accesso:
     * - Pagine pubbliche (tutti, anche anonimi): home, tornei, squadre, partite,
     *   API di sola lettura, login/register, risorse statiche (CSS, immagini, JS)
     * - POST /login e /register: pubblici (per permettere il login e la registrazione)
     * - /admin/**: solo utenti con ruolo ADMIN
     * - Qualsiasi altra richiesta: richiede autenticazione
     */
    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.cors(cors -> cors.disable());

        httpSecurity.authorizeHttpRequests(authorize -> {


            // Pagine e risorse accessibili a tutti (anche non loggati)
            authorize.requestMatchers(HttpMethod.GET, "/", "/tornei", "/tornei/**",
                    "/squadre", "/squadre/**", "/partite", "/partite/**", "/api/**",
                    "/login", "/register", "/css/**", "/images/**", "/js/**").permitAll();

            // Login e registrazione accessibili a tutti
            authorize.requestMatchers(HttpMethod.POST, "/login", "/register").permitAll();

            // Area amministrazione: solo ruolo ADMIN
            authorize.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Utente.ADMIN_ROLE);
            authorize.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Utente.ADMIN_ROLE);


            // Tutto il resto (es. post commenti): richiede login
            authorize.anyRequest().authenticated();

        });

        // Pagina di login personalizzata; dopo il login si torna alla home.
        httpSecurity.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/", true);
            form.failureUrl("/login?error=true");    // in caso di credenziali errate
        });

        // Logout: invalida la sessione, cancella il cookie e reindirizza alla home
        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/");
            logout.invalidateHttpSession(true);
            logout.deleteCookies("JSESSIONID");
            logout.clearAuthentication(true);
            logout.permitAll();
        });

        return httpSecurity.build();
    }
}
