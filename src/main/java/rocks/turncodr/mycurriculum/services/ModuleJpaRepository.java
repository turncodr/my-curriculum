package rocks.turncodr.mycurriculum.services;

import org.springframework.data.jpa.repository.JpaRepository;

import rocks.turncodr.mycurriculum.model.Module;

/**
 * JPARepository for Modules.
 */

public interface ModuleJpaRepository extends JpaRepository<Module, Integer>  {
}
