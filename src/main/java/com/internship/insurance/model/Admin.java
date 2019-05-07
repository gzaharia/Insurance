package com.internship.insurance.model;

@Entity
@Table(name="employees")
public class Admin {
    @Id
    @GenerationType(Strategy=GenetationType.IDENTITY)
    private Long id;
    private String login;
    private Sting password;
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Sting getPassword() {
        return password;
    }

    public void setPassword(Sting password) {
        this.password = password;
    }

    public com.internship.insurance.model.Role getRole() {
        return role;
    }

    public void setRole(com.internship.insurance.model.Role role) {
        this.role = role;
    }
}

enum Role{
    ADMIN,
    MODERATOR,
    USER
}


