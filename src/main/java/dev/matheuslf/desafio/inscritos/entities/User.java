package dev.matheuslf.desafio.inscritos.entities;

import dev.matheuslf.desafio.inscritos.audit.Auditable;
import dev.matheuslf.desafio.inscritos.entities.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    String name;

    @Column(length = 150, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Project> ownedProjects = new HashSet<>();

    @ManyToMany(mappedBy = "assignees", fetch = FetchType.LAZY)
    private Set<Project> assignedProjects = new HashSet<>();

    @OneToMany(mappedBy = "assignee")
    private Set<Task> tasks = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Set<Project> getProjects() {
        Set<Project> allProjects = new HashSet<>();

        allProjects.addAll(ownedProjects);
        allProjects.addAll(assignedProjects);

        return allProjects;
    }
}
