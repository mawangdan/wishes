package cn.edu.xmu.wishes.user.controller;

import cn.edu.xmu.wishes.core.util.JwtHelper;
import cn.edu.xmu.wishes.user.UserApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    private static JwtHelper jwtHelper = new JwtHelper();
    private static String token;
    private static String captcha;


    @BeforeEach
    void init() {
        token = jwtHelper.createToken(20L, "zheng5d", 0L, 0, 36000);
    }

    /*注册用户 错误的格式 为空格*/
    @Test
    public void registerusersWithErrorusersname() throws Exception {
        String content="{\"mobile\": \"13998989898\",\"email\": \"zheng5d@qq.com\",\"userName\": \"  \",\"password\": \"Ad!234\",\"name\": \"  \"}";
        String responseString=this.mvc.perform(post("/users")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":503}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    /*注册用户，重复的usersname*/
    @Test
    public void registerusersWithRepeatusersname() throws Exception {
        String content="{\"mobile\": \"13998989898\",\"email\": \"zheng5d@qq.com\",\"userName\": \"zheng5d\",\"password\": \"Ad!234\",\"name\": \"string\"}";
        String responseString=this.mvc.perform(post("/users")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":613,\"errmsg\":\"用户名已被注册\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    /*注册用户，重复的Mobile*/
    @Test
    public void registerusersWithRepeatusersMobile() throws Exception {
        String content="{\"mobile\": \"13912345678\",\"email\": \"zheng55d@qq.com\",\"userName\": \"zheng5dd\",\"password\": \"Ad!234\",\"name\": \"string\"}";
        String responseString=this.mvc.perform(post("/users")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":611,\"errmsg\":\"电话已被注册\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    /*注册用户，位数不足的Mobile*/
    @Test
    public void registerusersWithNotEnoughMobile() throws Exception {
        String content="{\"mobile\": \"139123458\",\"email\": \"zheng55d@qq.com\",\"userName\": \"zheng5dd\",\"password\": \"Ad!234\",\"name\": \"string\"}";
        String responseString=this.mvc.perform(post("/users")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":503,\"errmsg\":\"手机号格式错误;\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    /*注册用户，不符合格式的的Mobile*/
    @Test
    public void registerusersWithErrorusersMobile() throws Exception {
        String content="{\"mobile\": \"16978929643\",\"email\": \"zheng55d@qq.com\",\"userName\": \"zheng5dd\",\"password\": \"Ad!234\",\"name\": \"string\"}";
        String responseString=this.mvc.perform(post("/users")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":503,\"errmsg\":\"手机号格式错误;\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    /*注册用户，含有错误字符的的Mobile*/
    @Test
    public void registerusersWithMobileHasErrorChar() throws Exception {
        String content="{\"mobile\": \"1397e929643\",\"email\": \"zheng55d@qq.com\",\"userName\": \"zheng5dd\",\"password\": \"Ad!234\",\"name\": \"string\"}";
        String responseString=this.mvc.perform(post("/users")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":503,\"errmsg\":\"手机号格式错误;\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    /*注册用户，重复的email*/
    @Test
    public void registerusersWithRepeatEmail() throws Exception {
        String content="{\"mobile\": \"13902345678\",\"email\": \"123456@qq.com\",\"userName\": \"zheng5dd\",\"password\": \"Ad!234\",\"name\": \"string\"}";
        String responseString=this.mvc.perform(post("/users")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":612,\"errmsg\":\"邮箱已被注册\"}";;
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    /*注册用户，命名正确的email*/
    @Test
    public void registerusersWithRightEmail() throws Exception {
        String content="{\"mobile\": \"13902375678\",\"email\": \"123456@qq.cn.com\",\"userName\": \"zheng5dd\",\"password\": \"Ad!234\",\"name\": \"string1\"}";
        String responseString=this.mvc.perform(post("/users")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";;
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
    /*注册用户*/
    @Test
    public void registerusers() throws Exception {
        String content="{\"mobile\": \"13902345678\",\"email\": \"2345@qq.com\",\"userName\": \"zheng5dd\",\"password\": \"Ad!234\",\"name\": \"string\"}";
        String responseString=this.mvc.perform(post("/users")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

//    @Test
//    void registerusers() {
//    }

    @Test
    void usersLogin() throws Exception {
        String content="{\"userName\": \"zheng5d\",\"password\": \"123\"}";
        String responseString=this.mvc.perform(post("/login")
                .content(content)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    @Test
    void getCustomerInfo() throws Exception {
        String content="{\"userName\": \"zheng5d\",\"password\": \"123\"}";
        String responseString=this.mvc.perform(get("/self")
                .content(content)
                .contentType("application/json;charset=UTF-8")
                .header(JwtHelper.LOGIN_TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"data\":{\"userName\":\"zheng5d\",\"password\":\"123\",\"sign\":null,\"address\":null,\"mobile\":\"2\",\"email\":\"2\"},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
}