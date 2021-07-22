package model;

import java.util.Objects;

public class Contributor {
    private String name;
    private String emailAddress;

    public Contributor(String name, String emailAddress) {
        this.name = name;
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contributor that = (Contributor) o;
        return name.equalsIgnoreCase(that.name) &&
                emailAddress.equalsIgnoreCase(that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, emailAddress);
    }

    @Override
    public String toString() {
        return "Contributor: Name= " + name + ", Email address= " + emailAddress;
    }
}
