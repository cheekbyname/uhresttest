package uk.org.blackwood.uhresttest;

public class HousingTenantsRepairsTable {
	// Table defaults
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ID_DEFAULTS = " INTEGER PRIMARY KEY AUTOINCREMENT ";
	public static final String COLUMN_TEXT_DEFAULTS = " TEXT NOT NULL ";
	public static final String COLUMN_INT_DEFAULTS = " INTEGER ";
	public static final String COLUMN_SEPARATOR = ", ";
	public static final String TABLE_HOUSING_TENANTS_HOUSEHOLD = "housing_tenants_repairs";
	public static final String API_PATH = "/housing/tenants/repairs/";
	public static final String CONTENT_PATH = "/housing/tenants/repairs";

	// Columns
	public static final String COLUMN_HOUSING_TENANTS_REPAIRS_RQ_REF = "rq_ref";
	public static final String COLUMN_HOUSING_TENANTS_REPAIRS_RQ_DATE = "rq_date";
	public static final String COLUMN_HOUSING_TENANTS_REPAIRS_RQ_NAME = "rq_name";
	public static final String COLUMN_HOUSING_TENANTS_REPAIRS_RQ_PROBLEM = "rq_problem";
	public static final String COLUMN_HOUSING_TENANTS_REPAIRS_RQ_STATUS = "rq_status";
	public static final String COLUMN_HOUSING_TENANTS_REPAIRS_RQ_STATUS_DATE = "rq_overall_status_date";
	public static final String COLUMN_HOUSING_TENANTS_REPAIRS_RQ_DATE_DUE = "rq_date_due";
	
	
	
}
