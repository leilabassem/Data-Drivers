package store.model;

import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Perfume perfume;

    private int quantity;

    public CartItem() {
    }

    public CartItem(Perfume perfume, int quantity) {
        this.perfume = perfume;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Perfume getPerfume() {
        return perfume;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPerfume(Perfume perfume) {
        this.perfume = perfume;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}