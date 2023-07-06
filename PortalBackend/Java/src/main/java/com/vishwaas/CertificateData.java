package com.vishwaas;

import java.util.Date;
import org.springframework.stereotype.Component;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "certificates")
public class CertificateData {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
    private String certificateId ;
    private String name;
    private String entitytype;
    private String rebitversion;
    private int purposecode;
    private String fitype;
    private String expired;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    
 

    public void setId(int id) {
    	this.id = id;
    }
   
    public void setCertificateId(String osid) {
        this.certificateId = osid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntitytype() {
        return entitytype;
    }

    public void setEntitytype(String entitytype) {
        this.entitytype = entitytype;
    }

    public String getRebitversion() {
        return rebitversion;
    }

    public void setRebitversion(String rebitversion) {
        this.rebitversion = rebitversion;
    }

    public int getPurposecode() {
        return purposecode;
    }

    public void setPurposecode(int purposecode) {
        this.purposecode = purposecode;
    }

    public String getFitype() {
        return fitype;
    }

    public void setFitype(String fitype) {
        this.fitype = fitype;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }  

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String keycloakClientId) {
        this.createdBy = keycloakClientId;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setDynamicValues() {
        this.createdAt = new Date();
        this.updatedAt = null;
     
        this.updatedBy = null; 
    }

	@Override
	public String toString() {
		return "CertificateData [certificateId=" + certificateId + ", name=" + name + ", entitytype=" + entitytype
				+ ", rebitversion=" + rebitversion + ", purposecode=" + purposecode + ", fitype=" + fitype
				+ ", expired=" + expired + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", createdBy="
				+ createdBy + ", updatedBy=" + updatedBy + ", id=" + id + "]";
	}  
}
