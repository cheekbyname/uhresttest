/** Public Interface for communications between TenantMainActivity and Fragments **/

package uk.org.blackwood.uhresttest;

public interface TenantHandler {
	public long getTenant_id();
	public long getTenant_con_key();
	public String getTenant_house_ref();
	public String getTenant_prop_ref();
}

