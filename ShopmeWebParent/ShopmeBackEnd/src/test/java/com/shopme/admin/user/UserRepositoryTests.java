package com.shopme.admin.user;

import com.shopme.admin.user.dao.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role adminRole = entityManager.find(Role.class, 1);
        User userKosta = new User("divideetimpera20182@gmail.com", "alleka20122" , "Kostya", "Nyamtsy");
        userKosta.addRole(adminRole);

        var savedUser = repo.save(userKosta);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User userAnny = new User("aspirido4@gmail.com", "azino777", "Anna", "Spiridovich");
        Role editorRole = new Role(3);
        Role assistantRole = new Role(5);

        userAnny.addRole(editorRole);
        userAnny.addRole(assistantRole);

        var savedUser = repo.save(userAnny);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(System.out::println);
    }

    @Test
    public void testUserById() {
        User userKosta = repo.findById(1).get();
        System.out.println(userKosta);
        assertThat(userKosta).isNotNull();
    }

    @Test
    public void testGetUserByEmail() {
        String email = "divideetimpera20182@gmail.com";

        User user = repo.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userKosta = repo.findById(1).get();
        userKosta.setEnabled(true);
        repo.save(userKosta);
    }

    @Test
    public void testDeleteUserById() {
        Integer id = 2;
        repo.deleteById(id);
    }

    @Test
    public void testUpdateUserRole() {
        User userAnny = repo.findById(2).get();
        Role editorRole = new Role(3);
        Role salesPersonRole = new Role(2);
        userAnny.getRoles().remove(editorRole);
        userAnny.getRoles().add(salesPersonRole);
        repo.save(userAnny);
    }
}