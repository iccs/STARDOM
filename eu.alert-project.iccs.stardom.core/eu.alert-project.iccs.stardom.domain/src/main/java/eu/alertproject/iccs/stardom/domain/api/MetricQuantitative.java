package eu.alertproject.iccs.stardom.domain.api;

import javax.persistence.*;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:59
 */
@Table(name="metric_quantitative")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("metric_quantitative")
public class MetricQuantitative extends Metric {

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
}
