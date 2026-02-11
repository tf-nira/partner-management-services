package io.mosip.pms.payment.constant;

import io.mosip.pms.partner.manager.constant.AuditConstant;

public enum PaymentServiceAuditEnum {

	GENERATE_PARTNER_PRN("PMS_PRT_401", AuditConstant.AUDIT_SYSTEM, "POST GENERATE PARTNER PRN",
			"Generating Partner PRN", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_DEFAULT_SERVICE_CODE("PMS_PMP_402", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN", 
			"Default service code set to IDA", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_EXTERNAL_CALL("PMS_PMP_403", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Calling external PRN service", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_EMPTY_RESPONSE("PMS_PMP_404", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Empty response received from PRN provider", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_RESPONSE_PARSE_FAILURE("PMS_PMP_405", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Failed to parse PRN response", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_MISSING("PMS_PMP_406", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "PRN not present in provider response", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_DB_SAVE_FAILURE("PMS_PMP_407", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Failed to save generated PRN", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_SUCCESS("PMS_PMP_408", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "PRN generated successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_RESPONSE_MAPPED("PMS_PMP_409", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "PRN response mapped successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_INTERNAL_FAILURE("PMS_PMP_410", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Internal error occurred while generating PRN", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME,
		    "NO_ID", "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_DB_SAVE_SUCCESS("PMS_PMP_411", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Generated PRN saved successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_EXTERNAL_SERVICE_FAILURE("PMS_PMP_412", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN", 
			"External PRN service call failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PARTNER_PRN("PMS_PRT_413", AuditConstant.AUDIT_SYSTEM, "POST VALIDATE PARTNER PRN",
			"Validating Partner PRN", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_EXTERNAL_CALL("PMS_PMP_414", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "Calling external PRN validation service", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_EMPTY_RESPONSE("PMS_PMP_415", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "Empty response received from PRN validation provider", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME,
		    "NO_ID", "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_RESPONSE_MAPPED("PMS_PMP_416", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "PRN validation response mapped successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_EXTERNAL_SERVICE_FAILURE("PMS_PMP_417", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "External PRN validation service call failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_INVALID_RESPONSE("PMS_PMP_418", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "Invalid structure in PRN validation response", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_SUCCESS("PMS_PMP_419", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
			"PRN validated successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_FAILURE("PMS_PRT_420", AuditConstant.AUDIT_SYSTEM, "GET PARTNER ",
			"Retrieving partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_ACTIVE_FAILURE("PMS_PRT_421", AuditConstant.AUDIT_SYSTEM, "GET ACTIVE PARTNER ",
			"Retrieving active partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_REQUIRED_PAYMENT_FAILURE("PMS_PRT_422", AuditConstant.AUDIT_SYSTEM, "GET REQUIRED PAYMENT PARTNER ",
			"Retrieving required payment partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	BALANCE_DB_SAVE_FAILURE("PMS_PMP_423", AuditConstant.AUDIT_SYSTEM, "SAVE BALANCE",
			"Failed to save balance", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER("PMS_PRT_413", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PAYMENT",
			"Search Payment", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID);
	
	
	private final String eventId;

	private final String type;

	private String name;

	private String description;

	private String moduleId;

	private String moduleName;

	private String id;

	private String idType;

	private String applicationId;

	private String applicationName;

	private PaymentServiceAuditEnum(String eventId, String type, String name, String description, String moduleId,
			String moduleName, String id, String idType, String applicationId, String applicationName) {
		this.eventId = eventId;
		this.type = type;
		this.name = name;
		this.description = description;
		this.moduleId = moduleId;
		this.moduleName = moduleName;
		this.id = id;
		this.idType = idType;
		this.applicationId = applicationId;
		this.applicationName = applicationName;

	}

	public String getEventId() {
		return eventId;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getModuleId() {
		return moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getId() {
		return id;
	}

	public String getIdType() {
		return idType;
	}

	public void setDescription(String des) {
		this.description = des;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplicationName() {
		return applicationName;
	}

	/*
	 * Replace %s value in description and id with second parameter passed
	 */
	public static PaymentServiceAuditEnum getPartnerManageEnumWithValue(PaymentServiceAuditEnum e, String s) {
		e.setDescription(String.format(e.getDescription(), s));
		if (e.getId().equalsIgnoreCase("%s"))
			e.setId(s);
		return e;
	}
}
