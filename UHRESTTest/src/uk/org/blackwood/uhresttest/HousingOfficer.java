package uk.org.blackwood.uhresttest;

public class HousingOfficer {
	// Fields
	private long id;
	private String lu_ref;
	private String lu_desc;

	// Accessors
	public long getId() {
		return id;
	}
	public String getLu_ref() {
		return lu_ref;
	}
	public String getLu_desc() {
		return lu_desc;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setLu_ref(String lu_ref) {
		this.lu_ref = lu_ref;
	}
	public void setLu_desc(String lu_desc) {
		this.lu_desc = lu_desc;
	}
	
	// Override for ArrayAdapter
	@Override
	public String toString() {
		return lu_desc;
	}
}
