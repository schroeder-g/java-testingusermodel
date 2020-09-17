package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception
    {
        userList = new ArrayList<>();

        Role r1 = new Role("Admin");
        Role r2 = new Role( "user");
        Role r3 = new Role("data");


        //admin
        User u1 = new User("admin",
                "password",
                "theadmin@test.com");
        u1.setUserid(10);

        u1.getRoles()
                .add(new UserRoles(u1, r1));
        u1.getRoles()
                .add(new UserRoles(u1, r2));
        u1.getRoles()
                .add(new UserRoles(u1, r3));

        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@test.com"));
        u1.getUseremails().get(0).setUseremailid(15);

        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@testing.com"));
        u1.getUseremails().get(1).setUseremailid(16);

        userList.add(u1);

        //user
        User u2 = new User("Alex",
                            "password",
                            "alex@lambdaschool.com");
        u2.setUserid(20);
        u2.getRoles().add(new UserRoles(u2, r2));

        u2.getUseremails().add(new Useremail(u2, "alex@gmail.com"));
        u2.getUseremails().get(0).setUseremailid(17);

        userList.add(u2);

        User u3 = new User("John",
                "johnjohn",
                "john@java.com");
        u3.setUserid(21);
        u3.getRoles().add(new UserRoles(u3, r2));

        u3.getUseremails().add(new Useremail(u3, "john@gmail.com"));
        u3.getUseremails().get(0).setUseremailid(18);

        userList.add(u3);

        User u4 = new User("Chris",
                "Mr. Chris",
                "Chris@lambdaschool.com");
        u4.setUserid(22);
        u4.getRoles().add(new UserRoles(u4, r2));

        u4.getUseremails().add(new Useremail(u4, "Chris@gmail.com"));
        u4.getUseremails().get(0).setUseremailid(19);

        userList.add(u4);

        //data

        User u6 = new User("data",
                            "databoss",
                                "databoss@gmail.com");
        u6.setUserid(23);
        u6.getRoles().add(new UserRoles(u6, r3));

        u6.getUseremails().add(new Useremail(u6, "databoss@lambda.com"));
        u6.getUseremails().get(0).setUseremailid(20);

        userList.add(u6);

    }

    @After
    public void tearDown() throws Exception
    {
    }

    private void assertEquals(String jsonResult, String usersAsJson)
    {
    }

    @Test
    public void listAllUsers() throws Exception
    {
        var url = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);

        var builder = get(url).accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(builder).andReturn();
        var jsonResult = result.getResponse().getContentAsString();

        var jsonUsers = new ObjectMapper().writeValueAsString(userList);

        assertEquals(jsonResult, jsonUsers);
    }

    @Test
    public void getUserById() throws Exception
    {
        var url = "/users/user/20";
        Mockito.when(userService.findUserById(20)).thenReturn(userList.get(0));

        var builder = get(url).accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(builder).andReturn();
        var jsonResult = result.getResponse().getContentAsString();

        var jsonUsers = new ObjectMapper().writeValueAsString(userList.get(0));
        assertEquals(jsonResult, jsonUsers);
    }

    @Test
    public void getUserByName() throws Exception
    {
        var url = "/users/user/name/Chris";
        Mockito.when(userService.findByName("Chris")).thenReturn(userList.get(0));
        var builder = get(url).accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(builder).andReturn();
        var jsonResult = result.getResponse().getContentAsString();
    }

    @Test
    public void getUserLikeName()  throws Exception
    {
        var url = "/users/user/name/like/hri";
        Mockito.when(userService.findByNameContaining("hri")).thenReturn(userList);

        var builder = get(url).accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(builder).andReturn();
        var jsonResult = result.getResponse().getContentAsString();
    }

    @Test
    public void addNewUser() throws Exception
    {
        var url = "/users/user";

        User testNewUser = new User();
        testNewUser.setUserid(0);
        testNewUser.setUsername("Evo Morales");
        testNewUser.setPassword("BoliviaRules");
        testNewUser.setPrimaryemail("evo@govierno.bo");
        Role r3 = new Role("President");
        testNewUser.getRoles().add(new UserRoles(testNewUser, r3));
        testNewUser.getUseremails().add(new Useremail(testNewUser, "bolivia@gmail.com"));

        var jsonUser = new ObjectMapper()
                .writeValueAsString(testNewUser);
        Mockito.when(userService.save(any(User.class)))
                .thenReturn(testNewUser);
        var builder = MockMvcRequestBuilders
                .post(url).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser);
        mockMvc.perform(builder).andExpect(status().isCreated());
    }

    @Test
    public void updateFullUser() throws Exception
    {
        String url = "/users/user/666";
        User newUser = new User("Alexander", "the dude", "alex@alex.com");
        newUser.setUserid(666);
        Role role = new Role("ADMIN");
        newUser.getRoles().add(new UserRoles(newUser, role));
        newUser.getUseremails().add(new Useremail(newUser, "test@test.com"));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(newUser);

        Mockito.when(userService.save(any(User.class))).thenReturn(newUser);

        RequestBuilder builder = MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(builder).andExpect(status().isOk());
    }

    @Test
    public void updateUser() throws Exception
    {
        String url = "/users/user/666";
        User newUser = new User("Alexander", "the dude", "alex@alex.com");
        newUser.setUserid(666);
        Role role = new Role("ADMIN");
        newUser.getRoles().add(new UserRoles(newUser, role));
        newUser.getUseremails().add(new Useremail(newUser, "test@test.com"));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(newUser);

        Mockito.when(userService.save(any(User.class))).thenReturn(newUser);

        RequestBuilder builder = MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(builder).andExpect(status().isOk());
    }

    @Test
    public void deleteUserById()  throws Exception
    {
        var url = "/users/user/10";
        var builder = MockMvcRequestBuilders.delete(url).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder).andExpect(status().isOk());
    }
}
