package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest
{
    @Autowired
    private UserService useServ;

    @Autowired RoleService rolServ;

    @Before
    public void setUp() throws  Exception
    {
        MockitoAnnotations.initMocks(this);

        List<User> userList = useServ.findAll();
        for (User u : userList)
        {
            System.out.println(u.getUserid() + " " + u.getUsername());
        }
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findUserById()
    {
        assertEquals("puttattest", useServ.findUserById(13).getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findUserByIdNotFound()
    {
        assertEquals("", useServ.findUserById(00000).getUsername());
    }


    @Test
    public void findByNameContaining()
    {
        assertEquals(1, useServ.findByNameContaining("putt").size());
    }

    @Test
    public void findByInvalidNameContaining()
    {
        assertEquals(0, useServ.findByNameContaining("bleep blopp").size());
    }

    @Test
    public void findAll()
    {
        assertEquals(5, useServ.findAll().size());
    }

    @Test
    public void delete() {
        useServ.delete(13);
        assertEquals(5, useServ.findAll().size());
    }

    @Test
    public void findByName()
    {
        assertEquals("misskittytest", useServ.findByName("misskittytest").getUsername());
    }

    @Test
    public void findByInvalidName()
    {
        assertEquals("", useServ.findByName("bleepblppsadfsa").getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByInvalidNameII()
    {
        assertEquals("", useServ.findByName("miss").getUsername());
    }

    @Test
    public void save()
    {
        String name = "test user";
        User testUser = new User("testUSer",
                "",
                "testes@gmail.com");
        Role role = new Role("cool-guy");
        role.setRoleid(4);
        testUser.getRoles().add(new UserRoles(testUser, role));
        User newUser = useServ.save(testUser);
        assertNotNull(newUser);
        assertEquals(name, newUser.getUsername());
    }

    @Test (expected = ResourceNotFoundException.class)
    public void saveBadId()
    {
         User testUser = new User();
         testUser.setUsername("testuser");
         testUser.setPassword("password");
         testUser.setPrimaryemail("testes@gmail.com");
         testUser.setUserid(666666);
         List<Role> roles = rolServ.findAll();
         for (Role r : roles)
         {
             testUser.getRoles().add(new UserRoles(testUser, r));
         }
         var testUserTest = useServ.save(testUser);
         assertEquals("", testUserTest.getUsername());
    }

    @Test
    public void update()
    {
        User updateUser = new User();
        updateUser.setUserid(15);
        updateUser.setUsername("Mr. Test");
        updateUser.setPrimaryemail("test@gmail.com");
        updateUser.setPassword("passwordtest");
        updateUser.getUseremails().add(new Useremail(updateUser, "hi@gmail.com"));

        Role role = new Role("testrole");
        role.setRoleid(4);
        updateUser.getRoles().add(new UserRoles(updateUser, role));

        User details = useServ.update(updateUser, 15);

        assertNotNull(details);
        assertEquals("Mr. Test", useServ.findUserById(15).getUsername());
    }

    @Test
    public void deleteAll()
    {
        useServ.deleteAll();
        assertEquals(0, useServ.findAll().size());
    }
}
