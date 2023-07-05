package Vishwaas;

import org.springframework.stereotype.Component;

@Component
public class CertificateData {
	
	private String name;
	    private String entitytype;
	    private String rebitversion;
	    private int purposecode;
	    private String fitype;
	    private String expired;
	   
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
		
		
		@Override
		public String toString() {
			return "CertificateData [name=" + name + ", entitytype=" + entitytype + ", rebitversion=" + rebitversion
					+ ", purposecode=" + purposecode + ", fitype=" + fitype + ", expired=" + expired 
					+ "]";
		}
		
		
	
	   
		
		
}
