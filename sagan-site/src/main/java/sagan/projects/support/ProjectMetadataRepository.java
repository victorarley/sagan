package sagan.projects.support;

import java.util.List;

import sagan.projects.Project;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMetadataRepository extends JpaRepository<Project, String> {

	List<Project> findByCategory(String category, Sort sort);

	@Query("SELECT DISTINCT p FROM Project p JOIN FETCH p.releaseList")
	List<Project> findAllWithReleases(Sort sort);

	@EntityGraph(value = "Project.tree", type = EntityGraph.EntityGraphType.LOAD)
	List<Project> findDistinctByCategoryAndParentProjectIsNull(String category, Sort sort);

	@Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.groups WHERE p.parentProject = NULL ORDER BY p.displayOrder ASC")
	List<Project> findTopLevelProjectsWithGroup();
}
