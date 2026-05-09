package store.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Perfume perfume;

    private int quantity;

    public Order() {
    }

    public Order(User user, Perfume perfume, int quantity) {
        this.user = user;
        this.perfume = perfume;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Perfume getPerfume() {
        return perfume;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPerfume(Perfume perfume) {
        this.perfume = perfume;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}