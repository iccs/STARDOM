package eu.alertproject.iccs.stardom.domain.api;

import javax.persistence.*;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:59
 */
@Entity
@Table(name="metric_quantitative")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MetricQuantitative extends Metric {

    @Column(name="quantity")
    private Integer quantity;


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer increaseQuantity(){

        quantity +=1;
        return quantity;
    }

    public Integer decreaseQuantity(){
        quantity -=1;
        return quantity;
    }

    @Override
    public Object getValue() {
        return this.getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetricQuantitative that = (MetricQuantitative) o;

        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return quantity != null ? quantity.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "MetricQuantitative{" +
                "quantity=" + quantity +
                '}';
    }
}
