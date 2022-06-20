package cn.edu.xmu.wishes.user.security.config;

import cn.edu.xmu.wishes.user.model.po.User;
import cn.edu.xmu.wishes.user.security.userdetails.PreAuthenticatedUserDetailsService;
import cn.edu.xmu.wishes.user.security.userdetails.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证授权配置
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserDetailsService userDetailsService;

    private final String WEBAPP_CLIENT_ID = "app";

    @Value("${wishes.jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${wishes.jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Bean
    @SneakyThrows
    public ClientDetailsService clientDetailsService() {
        InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder = new InMemoryClientDetailsServiceBuilder();
        inMemoryClientDetailsServiceBuilder
                .withClient(WEBAPP_CLIENT_ID)
                .secret("{noop}bkdwln231-23")
                .scopes("all")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(7200)
                .autoApprove(true);
        return inMemoryClientDetailsServiceBuilder.build();
    }

    /**
     * OAuth2客户端
     */
    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        clients.withClientDetails(clientDetailsService());
    }

    /**
     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // Token增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        endpoints
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                /** refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                 *  1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                 *  2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
                 */
                .reuseRefreshTokens(true)
                .tokenServices(tokenServices(endpoints))
        ;
    }


    public DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetailsService());
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        // 多用户体系下，刷新token再次认证客户端ID和 UserDetailService 的映射Map
        Map<String, UserDetailsService> clientUserDetailsServiceMap = new HashMap<>();
        clientUserDetailsServiceMap.put(WEBAPP_CLIENT_ID, userDetailsService); // 系统管理客户端

        // 刷新token模式下，重写预认证提供者替换其AuthenticationManager，可自定义根据客户端ID和认证方式区分用户体系获取认证用户信息
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedUserDetailsService<>(clientUserDetailsServiceMap));
        tokenServices.setAuthenticationManager(new ProviderManager(provider));
        return tokenServices;

    }

    /**
     * 使用非对称加密算法对token签名
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    /**
     * 密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
       KeyPair keyPair = new KeyPair(publicKey, privateKey);
        return keyPair;
    }

    /**
     * JWT内容增强
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = new HashMap<>();
            Object principal = authentication.getUserAuthentication().getPrincipal();
            if (principal instanceof SecurityUser) {
                SecurityUser securityUser = (SecurityUser) principal;
                User user = securityUser.getUser();
                additionalInfo.put("userId", user.getId());
                additionalInfo.put("userName", user.getUserName());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }
}
