package ru.dkt.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "user_account")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "customUserDetails", sequenceName = "user_account_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "customUserDetails")
    private Integer id;

    private String firstName;

    private String lastName;

    private String username;

    @Column(unique = true)
    private String email;

    @Column(length = 256)
    private String password;

    @Column(nullable=false, columnDefinition="boolean default true")
    private boolean enabled = true;

    private boolean isUsing2FA;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.DETACH)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles;

     public UserAccount() {
        super();
        this.enabled = true;
    }

    @Override
    public String toString() {
        return "User [id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + username +
                ", password=" + password +
                ", enabled=" + enabled +
                ", roles=" + roles + "]";
    }
}