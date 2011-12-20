package eu.alertproject.iccs.stardom.domain.api;

import com.existanze.libraries.orm.domain.SimpleBean;

import javax.persistence.*;

/**
 * User: fotis
 * Date: 18/12/11
 * Time: 13:37
 */
@Entity
@Table(name="path_signature_history")
public class PathSignatureHistory implements SimpleBean{

    @TableGenerator(
            name="tseq",
            table="sequence",
            pkColumnName="sequence_name",
            valueColumnName="sequence_index",
            pkColumnValue="path_signature_history_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.TABLE, generator="tseq")
    @Id
    private Integer id;


    @Column(name="path")
    private String path;
    
    @Column(name="signature")
    private String signature;

    @JoinColumn(name="identity_id")
    @ManyToOne
    private Identity identity;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PathSignatureHistory that = (PathSignatureHistory) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        if (signature != null ? !signature.equals(that.signature) : that.signature != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (signature != null ? signature.hashCode() : 0);
        return result;
    }
}

