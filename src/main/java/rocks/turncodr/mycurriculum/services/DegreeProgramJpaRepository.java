package rocks.turncodr.mycurriculum.services;

import org.springframework.data.jpa.repository.JpaRepository;

import rocks.turncodr.mycurriculum.model.DegreeProgram;

/**
 * JPARepository for Entity DegreeProgram.
 */
public interface DegreeProgramJpaRepository extends JpaRepository<DegreeProgram, Integer> {
}
