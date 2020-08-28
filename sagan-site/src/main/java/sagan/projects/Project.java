package sagan.projects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.util.StringUtils;

@Entity
@NamedEntityGraph(name = "Project.tree",
        attributeNodes = @NamedAttributeNode("childProjectList"))
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Project {

    @Id
    private String id;
    private String name;
    private String repoUrl;
    private String siteUrl;
    private String category;
    private String rawBootConfig;
    private String renderedBootConfig;
    private String tagLine;
    private boolean featured;
    private String rawOverview;
    private String renderedOverview;
    private int displayOrder = Integer.MAX_VALUE;
    private String imagePath;

    @ManyToMany
    @JoinTable(name = "project_groups_rel",
            joinColumns = { @JoinColumn(name = "project_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") }
    )
    private Set<ProjectGroup> groups = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    private Project parentProject;

    @OneToMany(mappedBy = "parentProject")
    @OrderBy("displayOrder")
    private List<Project> childProjectList;

    @ElementCollection
    private List<ProjectRelease> releaseList = new ArrayList<>();
    private String stackOverflowTags;

    @ElementCollection
    private List<ProjectSample> sampleList = new ArrayList<>();

    @SuppressWarnings("unused")
    private Project() {
    }

    public Project(String id, String name, String repoUrl, String siteUrl,
                   List<ProjectRelease> releaseList, String category) {
        this.id = id;
        this.name = name;
        this.repoUrl = repoUrl;
        this.siteUrl = siteUrl;
        this.releaseList = releaseList;
        this.category = category;
    }

    public Project(String id, String name, String repoUrl, String siteUrl, int displayOrder, List<ProjectRelease> releaseList,
                   String category, String stackOverflowTags, String bootconfig) {
        this(id, name, repoUrl, siteUrl, releaseList, category);
        this.setDisplayOrder(displayOrder);
        this.setStackOverflowTags(stackOverflowTags);
        this.setRawBootConfig(bootconfig);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProjectReleases(List<ProjectRelease> releases) {
        this.releaseList = releases;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setReleaseList(List<ProjectRelease> releaseList) {
        this.releaseList = releaseList;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    /**
     * @return The list of releases sorted in descending order by version
     */
    public List<ProjectRelease> getProjectReleases() {
        if (releaseList == null) {
            return new ArrayList<>();
        }
        releaseList.sort(Collections.reverseOrder(ProjectRelease::compareTo));
        return releaseList;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public boolean hasSite() {
        return !siteUrl.isEmpty();
    }

    public String getStackOverflowTags() {
        return stackOverflowTags;
    }

    public void setStackOverflowTags(String stackOverflowTags) {
        this.stackOverflowTags = stackOverflowTags != null ?
                stackOverflowTags.replaceAll(" ", "") : "";
    }

    public Set<String> getStackOverflowTagList() {
        return StringUtils.commaDelimitedListToSet(this.stackOverflowTags);
    }

    public String getRawBootConfig() {
        return rawBootConfig;
    }

    public void setRawBootConfig(String rawBootConfig) {
        this.rawBootConfig = rawBootConfig;
    }

    public String getRenderedBootConfig() {
        return renderedBootConfig;
    }

    public void setRenderedBootConfig(String renderedBootConfig) {
        this.renderedBootConfig = renderedBootConfig;
    }

	public String getTagLine() {
		return this.tagLine;
	}

	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public String getRawOverview() {
        return rawOverview;
    }

    public void setRawOverview(String rawOverview) {
        this.rawOverview = rawOverview;
    }

    public String getRenderedOverview() {
        return renderedOverview;
    }

    public void setRenderedOverview(String renderedOverview) {
        this.renderedOverview = renderedOverview;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Project getParentProject() {
        return parentProject;
    }

    public String getParentId() {
        if (parentProject == null) {
            return null;
        }

        return parentProject.getId();
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    public List<Project> getChildProjectList() {
        return childProjectList;
    }

    public void setChildProjectList(List<Project> childProjectList) {
        this.childProjectList = childProjectList;
    }

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Set<ProjectGroup> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<ProjectGroup> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Project project = (Project) o;

        if (id != null ? !id.equals(project.id) : project.id != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", repoUrl='" + repoUrl + '\'' +
                ", siteUrl='" + siteUrl + '\'' +
                ", releaseList=" + releaseList +
                ", stackOverflowTags=" + stackOverflowTags +
                '}';
    }

    public boolean updateProjectRelease(ProjectRelease release) {
        boolean found = false;
        List<ProjectRelease> releases = getProjectReleases();
        for (int i = 0; i < releases.size(); i++) {
            ProjectRelease projectRelease = releases.get(i);
            if (release.getRepository() != null && release.getRepository().equals(projectRelease.getRepository())) {
                release.setRepository(projectRelease.getRepository());
            }
            if (projectRelease.getVersion().equals(release.getVersion())) {
                releases.set(i, release);
                found = true;
                break;
            }
        }
        if (!found) {
            releases.add(release);
        }
        release.replaceVersionPattern();
        return found;
    }

    public ProjectRelease removeProjectRelease(String version) {
        List<ProjectRelease> releases = getProjectReleases();
        ProjectRelease release = null;
        for (int i = 0; i < releases.size(); i++) {
            ProjectRelease projectRelease = releases.get(i);
            if (projectRelease.getVersion().equals(version)) {
                release = releases.remove(i);
                return release;
            }
        }
        return null;
    }

    public ProjectRelease getProjectRelease(String version) {
        List<ProjectRelease> releases = getProjectReleases();
        for (ProjectRelease release : releases) {
            if (release.getVersion().equals(version)) {
                return release;
            }
        }
        return null;
    }

    public Optional<ProjectRelease> getMostCurrentRelease() {
        return this.getProjectReleases().stream()
                .filter(ProjectRelease::isCurrent)
                .findFirst();
    }

    public List<ProjectRelease> getNonMostCurrentReleases() {
        Optional<ProjectRelease> mostCurrentRelease = this.getMostCurrentRelease();
        if (mostCurrentRelease.isPresent()) {
            return this.getProjectReleases().stream()
                    .filter(projectRelease -> !projectRelease.equals(mostCurrentRelease.get()))
                    .collect(Collectors.toList());
        }
        else {
            return this.getProjectReleases();
        }
    }

    public boolean isTopLevelProject() {
        return parentProject == null;
    }

    public List<ProjectSample> getProjectSamples() {
        sampleList.sort(Comparator.comparingInt(ProjectSample::getDisplayOrder));
        return sampleList;
    }

    public void setProjectSamples(List<ProjectSample> sampleList) {
        this.sampleList = sampleList;
    }
}
