//package pl.zajavka.infrastructure.database;
//
//import lombok.AllArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import pl.zajavka.infrastructure.security.RoleEntity;
//import pl.zajavka.infrastructure.security.RoleRepository;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@AllArgsConstructor(onConstructor = @__(@Autowired))
//class RoleRepositoryDataJpaTest extends AbstractJpaIT{
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Test
//    void thatRoleCanBeFoundByRoleName() {
//        // given
//        RoleEntity role = RoleEntity.builder().role("ROLE_CANDIDATE").build();
//        roleRepository.save(role);
//
//        // when
//        RoleEntity foundRole = roleRepository.findByRole("ROLE_CANDIDATE");
//
//        // then
//        assertThat(foundRole).isNotNull();
//        assertThat(foundRole.getRole()).isEqualTo(role.getRole());
//    }
//
//    @Test
//    void thatNonexistentRoleReturnsNull() {
//        // when
//        RoleEntity foundRole = roleRepository.findByRole("NONEXISTENT_ROLE");
//
//        // then
//        assertThat(foundRole).isNull();
//    }
//
//    @Test
//    void thatRoleCanBeFoundById() {
//        // given
//        RoleEntity role = RoleEntity.builder().role("ROLE_CANDIDATE").build();
//        roleRepository.save(role);
//
//        // when
//        RoleEntity foundRole = roleRepository.findById(role.getId()).orElse(null);
//
//        // then
//        assertThat(foundRole).isNotNull();
//        assertThat(foundRole.getRole()).isEqualTo(role.getRole());
//    }
//
//
//    @Test
//    void roleGetterMethods() {
//        // given
//        String roleName = "ROLE_CANDIDATE";
//        RoleEntity role = RoleEntity.builder().role(roleName).build();
//        roleRepository.save(role);
//
//        // when
//        RoleEntity foundRole = roleRepository.findByRole(roleName);
//
//        // then
//        assertThat(foundRole).isNotNull();
//        assertThat(foundRole.getRole()).isEqualTo(roleName); // Sprawdzamy getter 'getRole()'
//    }
//
//    @Test
//    void thatEmptyRoleReturnsNull() {
//        // when
//        RoleEntity foundRole = roleRepository.findByRole("");
//
//        // then
//        assertThat(foundRole).isNull();
//    }
//
//
//
//    @Test
//    void thatRoleWithEmptyNameCanBeSaved() {
//        // given
//        RoleEntity role = RoleEntity.builder().role("").build();
//
//        // when
//        RoleEntity savedRole = roleRepository.save(role);
//
//        // then
//        assertThat(savedRole.getId()).isNotNull(); // Upewnij się, że rola została zapisana z nadanym identyfikatorem.
//    }
//}