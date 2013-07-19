package uk.org.blackwood.uhresttest;

public class HousingScheme {
	// Fields
	private long id;
	private String short_address;
	private String prop_ref;

	// Accessors
	public long getId() {
		return id;
	}
	public String getShort_address() {
		return short_address;
	}
	public String getProp_ref() {
		return prop_ref;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setShort_address(String short_address) {
		this.short_address = short_address;
	}
	public void setProp_ref(String prop_ref) {
		this.prop_ref = prop_ref;
	}

	// Override for ArrayAdapter
	@Override
	public String toString() {
		return short_address;
	}
}
