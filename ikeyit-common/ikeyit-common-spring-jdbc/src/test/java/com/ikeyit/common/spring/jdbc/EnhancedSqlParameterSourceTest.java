package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.EnumWithInt;
import com.ikeyit.common.data.JsonField;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.Objects;

class EnhancedSqlParameterSourceTest {
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

    record User(String id, Name name) {
    }

    static class Computer1 {

        private String name;

        private Model model;

        @JsonField
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

    static class Computer3 {

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

        public Material getMaterial() {
            return material;
        }

        @JsonField
        public void setMaterial(Material material) {
            this.material = material;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Computer3 computer3)) return false;
            return Objects.equals(name, computer3.name) && model == computer3.model && Objects.equals(material, computer3.material) && Objects.equals(user, computer3.user);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, model, material, user);
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
    void testAnnotationOnField() {
        Computer1 computer = new Computer1();
        computer.setName("Thinkpad");
        computer.setModel(Model.B);
        Material material = new Material();
        material.setSize(10);
        material.setTitle("Ocean");
        computer.assignMaterial(material);
        computer.assignUser(new User("abc", null));
        jdbcTemplate.update("""
                INSERT INTO `computer` (name, model, material, user_id, user_name_first, user_name_second)
                VALUES (:name, :model, :material, :user.id, :user.name.first, :user.name.second)
                """,
                new EnhancedSqlParameterSource(computer));
        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM computer WHERE name = 'Thinkpad'", Map.of());
        Assertions.assertEquals("Thinkpad", result.get("name"));
        Assertions.assertEquals(Model.B.value(), result.get("model"));
        Assertions.assertEquals("{\"title\":\"Ocean\",\"size\":10}", result.get("material"));
        Assertions.assertEquals("abc", result.get("user_id"));
        Assertions.assertNull(result.get("user_name_first"));
        Assertions.assertNull(result.get("user_name_second"));
    }

    @Test
    void testAnnotationOnGetter() {
        Computer2 computer = new Computer2();
        computer.setName("Thinkpad");
        computer.setModel(Model.B);
        Material material = new Material();
        material.setSize(10);
        material.setTitle("Ocean");
        computer.setMaterial(material);

        jdbcTemplate.update("""
                INSERT INTO `computer` (name, model, material, user_id, user_name_first, user_name_second)
                VALUES (:name, :model, :material, :user.id, :user.name.first, :user.name.second)
                """,
                new EnhancedSqlParameterSource(computer));
        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM computer WHERE name = 'Thinkpad'", Map.of());
        Assertions.assertEquals("Thinkpad", result.get("name"));
        Assertions.assertEquals(Model.B.value(), result.get("model"));
        Assertions.assertEquals("{\"title\":\"Ocean\",\"size\":10}", result.get("material"));
        Assertions.assertNull(result.get("user_id"));
        Assertions.assertNull(result.get("user_name_first"));
        Assertions.assertNull(result.get("user_name_second"));
    }

    @Test
    void testAnnotationOnSetter() {
        Computer3 computer = new Computer3();
        computer.setName("Thinkpad");
        computer.setModel(Model.B);
        Material material = new Material();
        material.setSize(10);
        material.setTitle("Ocean");
        computer.setMaterial(material);

        jdbcTemplate.update("INSERT INTO `computer` (name, model, material) VALUES (:name, :model, :material)",
                new EnhancedSqlParameterSource(computer));
        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM computer WHERE name = 'Thinkpad'", Map.of());
        Assertions.assertEquals("Thinkpad", result.get("name"));
        Assertions.assertEquals(Model.B.value(), result.get("model"));
        Assertions.assertEquals("{\"title\":\"Ocean\",\"size\":10}", result.get("material"));
    }


    @Test
    void testWithNull() {
        Computer3 computer = new Computer3();
        computer.setName("Thinkpad");
        computer.setModel(Model.B);
        computer.setMaterial(null);
        jdbcTemplate.update("INSERT INTO `computer` (name, model, material) VALUES (:name, :model, :material)",
                new EnhancedSqlParameterSource(computer));
        Map<String, Object> result = jdbcTemplate.queryForMap("SELECT * FROM computer WHERE name = 'Thinkpad'", Map.of());
        Assertions.assertEquals("Thinkpad", result.get("name"));
        Assertions.assertEquals(Model.B.value(), result.get("model"));
        Assertions.assertEquals(null, result.get("material"));
    }
}