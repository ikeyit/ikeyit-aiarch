package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.Embedded;
import com.ikeyit.common.data.EnumWithInt;
import com.ikeyit.common.data.JsonField;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.Objects;

class EnhancedRowMapperTest {
    enum Model implements EnumWithInt {
        A(1),
        B(2);
        private int value;

        Model(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }
    }
    static class Material {

        private String title;

        private int size;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
           Material material = (Material) o;
            return size == material.size && Objects.equals(title, material.title);
        }
    }

    record Name(String first, String second) {
    }

    record User(String id, @Embedded Name name) {
    }

    static class Computer1 {

        private String name;

        private Model model;

        @JsonField
        private Material material;

        @Embedded
        private User user;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public Material getMaterial() {
            return material;
        }

        public void assignMaterial(Material material) {
            this.material = material;
        }

        public User getUser() {
            return user;
        }

        public void assignUser(User user) {
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Computer1 computer1)) return false;
            return Objects.equals(name, computer1.name) && model == computer1.model && Objects.equals(material, computer1.material) && Objects.equals(user, computer1.user);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, model, material, user);
        }
    }

    static class Computer2 {

        private String name;

        private Model model;

        private Material material;

        private User user;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        @JsonField
        public Material getMaterial() {
            return material;
        }

        public void setMaterial(Material material) {
            this.material = material;
        }
        @Embedded
        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Computer2 computer2)) return false;
            return Objects.equals(name, computer2.name) && model == computer2.model && Objects.equals(material, computer2.material) && Objects.equals(user, computer2.user);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, model, material, user);
        }
    }

    static class NameBean {
        private String first;
        private String second;

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }
    }

    static class UserBean {
        private String id;
        @Embedded(nullable = true)
        private NameBean name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public NameBean getName() {
            return name;
        }

        public void setName(NameBean name) {
            this.name = name;
        }
    }
    static class Computer3 {

        private String name;

        private Model model;

        private Material material;

        private UserBean customer;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public Material getMaterial() {
            return material;
        }

        @JsonField
        public void setMaterial(Material material) {
            this.material = material;
        }

        public UserBean getCustomer() {
            return customer;
        }
        @Embedded(nullable = true, prefix = "user_")
        public void setCustomer(UserBean customer) {
            this.customer = customer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Computer3 computer3)) return false;
            return Objects.equals(name, computer3.name) && model == computer3.model && Objects.equals(material, computer3.material) && Objects.equals(customer, computer3.customer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, model, material, customer);
        }
    }

    NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("sa");
        jdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcTemplate.update("""
            DROP TABLE `computer` IF EXISTS;
            CREATE TABLE `computer`
                (
                    `name`  varchar(128) NOT NULL DEFAULT '',
                    `model` int NOT NULL DEFAULT '0',
                    `material` varchar(256) NULL,
                    `user_id` varchar(256) NULL,
                    `user_name_first` varchar(256) NULL,
                    `user_name_second` varchar(256) NULL,
                    PRIMARY KEY (`name`)
                );
            """, Map.of());
    }

    @Test
    void testMapperAnnotationOnField() {
        jdbcTemplate.update("""
            INSERT INTO `computer` (name, model, material, user_id, user_name_first, user_name_second)
            VALUES ('Mac 100', 2, '{"title":"secret","size":9}', 'u123', 'John', 'Mike')
            """, Map.of());
        Computer1 actual = jdbcTemplate.queryForObject("SELECT * FROM computer", Map.of(), new EnhancedRowMapper<>(Computer1.class));
        Assertions.assertEquals("Mac 100", actual.name);
        Assertions.assertEquals(Model.B, actual.model);
        Assertions.assertEquals("secret", actual.material.title);
        Assertions.assertEquals(9, actual.material.size);
        Assertions.assertEquals("u123", actual.user.id);
        Assertions.assertEquals("John", actual.user.name.first);
        Assertions.assertEquals("Mike", actual.user.name.second);

    }

    @Test
    void testMapperAnnotationOnGetter() {
        jdbcTemplate.update("""
            INSERT INTO `computer` (name, model, material, user_id, user_name_first, user_name_second)
            VALUES ('Mac 100', 2, '{"title":"secret","size":9}', 'u123', null, null)
            """, Map.of());
        Computer2 actual = jdbcTemplate.queryForObject("SELECT * FROM computer", Map.of(), new EnhancedRowMapper<>(Computer2.class));
        Assertions.assertEquals("Mac 100", actual.name);
        Assertions.assertEquals(Model.B, actual.model);
        Assertions.assertEquals("secret", actual.material.title);
        Assertions.assertEquals(9, actual.material.size);
        Assertions.assertEquals("u123", actual.user.id);
        Assertions.assertNull(actual.user.name.first);
        Assertions.assertNull(actual.user.name.second);
    }

    @Test
    void testMapperAnnotationOnSetter() {
        jdbcTemplate.update("""
            INSERT INTO `computer` (name, model, material, user_id, user_name_first, user_name_second)
            VALUES ('Mac 100', 2, '{"title":"secret","size":9}', null, null, null)
            """, Map.of());
        Computer3 actual = jdbcTemplate.queryForObject("SELECT * FROM computer", Map.of(), new EnhancedRowMapper<>(Computer3.class));
        Assertions.assertEquals("Mac 100", actual.name);
        Assertions.assertEquals(Model.B, actual.model);
        Assertions.assertEquals("secret", actual.material.title);
        Assertions.assertEquals(9, actual.material.size);
        Assertions.assertNull(actual.customer);
    }

    @Test
    void testMapperWithNull1() {
        jdbcTemplate.update("""
            INSERT INTO `computer` (name, model, material, user_id, user_name_first, user_name_second)
            VALUES ('Mac 100', 2, null, null, null, 'Mike')
            """, Map.of());
        Computer3 actual = jdbcTemplate.queryForObject("SELECT * FROM computer", Map.of(), new EnhancedRowMapper<>(Computer3.class));
        Assertions.assertEquals("Mac 100", actual.name);
        Assertions.assertEquals(Model.B, actual.model);
        Assertions.assertNull(actual.material);
        Assertions.assertNull(actual.customer.id);
        Assertions.assertNull(actual.customer.name.first);
        Assertions.assertEquals("Mike", actual.customer.name.second);
    }

    @Test
    void testMapperWithNull2() {
        jdbcTemplate.update("""
            INSERT INTO `computer` (name, model, material, user_id, user_name_first, user_name_second)
            VALUES ('Mac 100', 2, null, null, null, null)
            """, Map.of());
        Computer3 actual = jdbcTemplate.queryForObject("SELECT * FROM computer", Map.of(), new EnhancedRowMapper<>(Computer3.class));
        Assertions.assertEquals("Mac 100", actual.name);
        Assertions.assertEquals(Model.B, actual.model);
        Assertions.assertNull(actual.material);
        Assertions.assertNull(actual.customer);
    }

    @Test
    void testMapperWithInvalidJson() {
        jdbcTemplate.update("""
            INSERT INTO `computer` (name, model, material) VALUES ('Mac 100', 2, '{A}');
            """, Map.of());
        Assertions.assertThrows(Exception.class, () -> {
            jdbcTemplate.queryForObject("SELECT * FROM computer", Map.of(), new EnhancedRowMapper<>(Computer3.class));
        });
    }
}