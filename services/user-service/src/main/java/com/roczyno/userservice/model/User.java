package com.roczyno.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "_user")
public class User implements UserDetails, Principal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(unique = true)
	private String username;
	@Column(unique = true)
	private String email;
	private String password;
	private int projectSize;
	private Boolean enabled;
	private Boolean accountLocked;
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles;
	private LocalDateTime createdAt;


	@Override
	public String getName() {
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
