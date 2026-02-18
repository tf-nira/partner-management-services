package io.mosip.pms.partner.constant;

import io.mosip.pms.partner.manager.constant.AuditConstant;

public enum PartnerServiceAuditEnum {

	REGISTER_PARTNER("PMS_PRT_101", AuditConstant.AUDIT_SYSTEM, "POST REGISTER PARTNER",
			"Registering the partner", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	REGISTER_PARTNER_SUCCESS("PMS_PRT_201", AuditConstant.AUDIT_SYSTEM, "POST REGISTER PARTNER",
			"Registering the partner success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	REGISTER_PARTNER_FAILURE("PMS_PRT_401", AuditConstant.AUDIT_SYSTEM, "POST REGISTER PARTNER",
			"Registering the partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	UPLOAD_CA_CERT("PMS_PRT_102", AuditConstant.AUDIT_SYSTEM, "POST UPLOADING CA CERT",
			"Uploading CA cert", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	UPLOAD_CA_CERT_SUCCESS("PMS_PRT_202", AuditConstant.AUDIT_SYSTEM, "POST UPLOADING CA CERT",
			"Uploading CA cert success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	UPLOAD_CA_CERT_FAILURE("PMS_PRT_402", AuditConstant.AUDIT_SYSTEM, "POST UPLOADING CA CERT",
			"Uploading CA cert failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	UPLOAD_PARTNER_CERT("PMS_PRT_103", AuditConstant.AUDIT_SYSTEM, "POST UPLOADING PARTNER CERT",
			"Uploading Partner cert", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	UPLOAD_PARTNER_CERT_SUCCESS("PMS_PRT_203", AuditConstant.AUDIT_SYSTEM, "POST UPLOADING PARTNER CERT",
			"Uploading Partner cert success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	UPLOAD_PARTNER_CERT_FAILURE("PMS_PRT_403", AuditConstant.AUDIT_SYSTEM, "POST UPLOADING PARTNER CERT",
			"Uploading Partner cert failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SUBMIT_API_REQUEST("PMS_PRT_104", AuditConstant.AUDIT_SYSTEM, "PATCH REQUEST FOR API KEY",
			"Requesting for API Key", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SUBMIT_API_REQUEST_SUCCESS("PMS_PRT_204", AuditConstant.AUDIT_SYSTEM, "PATCH REQUEST FOR API KEY",
			"Requesting for API Key success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SUBMIT_API_REQUEST_FAILURE("PMS_PRT_404", AuditConstant.AUDIT_SYSTEM, "PATCH REQUEST FOR API KEY",
			"Requesting for API Key failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	UPDATE_PARTNER("PMS_PRT_105", AuditConstant.AUDIT_SYSTEM, "PUT UPDATE PARTNER",
			"Updating Partner ", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	UPDATE_PARTNER_SUCCESS("PMS_PRT_205", AuditConstant.AUDIT_SYSTEM, "PUT UPDATE PARTNER",
			"Updating Partner success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	UPDATE_PARTNER_FAILURE("PMS_PRT_405", AuditConstant.AUDIT_SYSTEM, "PUT UPDATE PARTNER",
			"Updating Partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	ADD_CONTACTS("PMS_PRT_106", AuditConstant.AUDIT_SYSTEM, "POST ADD CONTACTS",
			"Adding contacts ", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	ADD_CONTACTS_SUCCESS("PMS_PRT_206", AuditConstant.AUDIT_SYSTEM, "POST ADD CONTACTS",
			"Adding contacts success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	ADD_CONTACTS_FAILURE("PMS_PRT_406", AuditConstant.AUDIT_SYSTEM, "POST ADD CONTACTS",
			"Adding contacts failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	ADD_BIO_EXTRACTORS("PMS_PRT_107", AuditConstant.AUDIT_SYSTEM, "POST ADD BIO EXTRACTORS",
			"Adding bio extractors ", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	ADD_BIO_EXTRACTORS_SUCCESS("PMS_PRT_207", AuditConstant.AUDIT_SYSTEM, "POST ADD BIO EXTRACTORS",
			"Adding bio extractors success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	ADD_BIO_EXTRACTORS_FAILURE("PMS_PRT_407", AuditConstant.AUDIT_SYSTEM, "POST ADD BIO EXTRACTORS",
			"Adding bio extractors failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	MAP_POLICY_CREDENTIAL_TYPE("PMS_PRT_108", AuditConstant.AUDIT_SYSTEM, "POST MAP POLICY CREDENTIAL TYPE",
			"Mapping policy and credential type ", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	MAP_POLICY_CREDENTIAL_TYPE_SUCCESS("PMS_PRT_208", AuditConstant.AUDIT_SYSTEM, "POST MAP POLICY CREDENTIAL TYPE",
			"Mapping policy and credential type success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	MAP_POLICY_CREDENTIAL_TYPE_FAILURE("PMS_PRT_408", AuditConstant.AUDIT_SYSTEM, "POST MAP POLICY CREDENTIAL TYPE",
			"Mapping policy and credential type failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER("PMS_PRT_109", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER",
			"Searching Partner ", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_SUCCESS("PMS_PRT_209", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER",
			"Searching Partner success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_FAILURE("PMS_PRT_409", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER",
			"Searching Partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_TYPE("PMS_PRT_110", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER TYPE",
			"Searching Partner type", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_TYPE_SUCCESS("PMS_PRT_210", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER TYPE",
			"Searching Partner type success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_TYPE_FAILURE("PMS_PRT_410", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER TYPE",
			"Searching Partner type failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_APIKEY("PMS_PRT_111", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER APIKEY",
			"Searching Partner apikey", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_APIKEY_SUCCESS("PMS_PRT_211", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER APIKEY",
			"Searching Partner apikey success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_APIKEY_FAILURE("PMS_PRT_411", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER APIKEY",
			"Searching Partner apikey failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_APIKEY_REQUEST("PMS_PRT_112", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER APIKEY REQUEST",
			"Searching Partner apikey request", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_APIKEY_REQUEST_SUCCESS("PMS_PRT_212", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER APIKEY REQUEST",
			"Searching Partner apikey request success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_APIKEY_REQUEST_FAILURE("PMS_PRT_412", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER APIKEY REQUEST",
			"Searching Partner apikey request failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	FILTER_PARTNER("PMS_PRT_113", AuditConstant.AUDIT_SYSTEM, "POST FILTER PARTNER",
			"Filtering Partner", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	FILTER_PARTNER_SUCCESS("PMS_PRT_213", AuditConstant.AUDIT_SYSTEM, "POST FILTER PARTNER",
			"Filtering Partner success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	FILTER_PARTNER_FAILURE("PMS_PRT_413", AuditConstant.AUDIT_SYSTEM, "POST FILTER PARTNER",
			"Filtering Partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	FILTER_PARTNER_APIKEY_REQUESTS("PMS_PRT_114", AuditConstant.AUDIT_SYSTEM, "POST FILTER PARTNER APIKEY REQUESTS",
			"Filtering Partner apikey requests", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	FILTER_PARTNER_APIKEY_REQUESTS_SUCCESS("PMS_PRT_214", AuditConstant.AUDIT_SYSTEM, "POST FILTER PARTNER APIKEY REQUESTS",
			"Filtering Partner apikey requests success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	FILTER_PARTNER_APIKEY_REQUESTS_FAILURE("PMS_PRT_414", AuditConstant.AUDIT_SYSTEM, "POST FILTER PARTNER APIKEY REQUESTS",
			"Filtering Partner apikey requests failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_CERT("PMS_PRT_115", AuditConstant.AUDIT_SYSTEM, "GET PARTNER CERT",
			"Retrieving partner cert", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_CERT_SUCCESS("PMS_PRT_215", AuditConstant.AUDIT_SYSTEM, "GET PARTNER CERT",
			"Retrieving partner cert success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_CERT_FAILURE("PMS_PRT_415", AuditConstant.AUDIT_SYSTEM, "GET PARTNER CERT",
			"Retrieving partner cert failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_APIKEYS("PMS_PRT_117", AuditConstant.AUDIT_SYSTEM, "GET PARTNER APIKEY REQUESTS",
			"Retrieving partner apikey requests", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_APIKEYS_SUCCESS("PMS_PRT_217", AuditConstant.AUDIT_SYSTEM, "GET PARTNER APIKEY REQUESTS",
			"Retrieving partner apikey requests success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_APIKEYS_FAILURE("PMS_PRT_417", AuditConstant.AUDIT_SYSTEM, "GET PARTNER APIKEY REQUESTS",
			"Retrieving partner apikey requests failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_APIKEY_STATUS("PMS_PRT_118", AuditConstant.AUDIT_SYSTEM, "GET PARTNER APIKEY REQUEST STATUS",
			"Retrieving partner apikey status", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_APIKEY_STATUS_SUCCESS("PMS_PRT_218", AuditConstant.AUDIT_SYSTEM, "GET PARTNER APIKEY REQUEST STATUS",
			"Retrieving partner apikey status success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_APIKEY_STATUS_FAILURE("PMS_PRT_418", AuditConstant.AUDIT_SYSTEM, "GET PARTNER APIKEY REQUEST STATUS",
			"Retrieving partner apikey status failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER("PMS_PRT_119", AuditConstant.AUDIT_SYSTEM, "GET PARTNER ",
			"Retrieving partner", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_SUCCESS("PMS_PRT_219", AuditConstant.AUDIT_SYSTEM, "GET PARTNER ",
			"Retrieving partner success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_FAILURE("PMS_PRT_419", AuditConstant.AUDIT_SYSTEM, "GET PARTNER ",
			"Retrieving partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	MAP_POLICY_GROUP("PMS_PRT_121", AuditConstant.AUDIT_SYSTEM, "PUT MAP POLICY GROUP",
			"Mapping policy group to partner", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	MAP_POLICY_GROUP_SUCCESS("PMS_PRT_221", AuditConstant.AUDIT_SYSTEM, "PUT MAP POLICY GROUP",
			"Mapping policy group to partner success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	MAP_POLICY_GROUP_FAILURE("PMS_PRT_421", AuditConstant.AUDIT_SYSTEM, "PUT MAP POLICY GROUP",
			"Mapping policy group to partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	MAP_POLICY_PARTNER("PMS_PRT_122", AuditConstant.AUDIT_SYSTEM, "POST REQUEST FOR POLICY MAP",
			"Requesting for policy mapping", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	MAP_POLICY_PARTNER_SUCCESS("PMS_PRT_222", AuditConstant.AUDIT_SYSTEM, "POST REQUEST FOR POLICY MAP",
			"Requesting for policy mapping success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	MAP_POLICY_PARTNER_FAILURE("PMS_PRT_422", AuditConstant.AUDIT_SYSTEM, "POST REQUEST FOR POLICY MAP",
			"Requesting for policy mapping failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PARTNER_PRN("PMS_PRT_423", AuditConstant.AUDIT_SYSTEM, "POST GENERATE PARTNER PRN",
			"Generating Partner PRN", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_DEFAULT_SERVICE_CODE("PMS_PMP_424", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN", 
			"Default service code set to IDA", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_EXTERNAL_CALL("PMS_PMP_425", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Calling external PRN service", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_EMPTY_RESPONSE("PMS_PMP_426", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Empty response received from PRN provider", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_RESPONSE_PARSE_FAILURE("PMS_PMP_427", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Failed to parse PRN response", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_MISSING("PMS_PMP_428", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "PRN not present in provider response", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_DB_SAVE_FAILURE("PMS_PMP_429", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Failed to save generated PRN", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_SUCCESS("PMS_PMP_430", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "PRN generated successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_RESPONSE_MAPPED("PMS_PMP_431", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "PRN response mapped successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_INTERNAL_FAILURE("PMS_PMP_432", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Internal error occurred while generating PRN", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME,
		    "NO_ID", "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_DB_SAVE_SUCCESS("PMS_PMP_433", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN",
		    "Generated PRN saved successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	GENERATE_PRN_EXTERNAL_SERVICE_FAILURE("PMS_PMP_434", AuditConstant.AUDIT_SYSTEM, "GENERATE PRN", 
			"External PRN service call failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PARTNER_PRN("PMS_PRT_435", AuditConstant.AUDIT_SYSTEM, "POST VALIDATE PARTNER PRN",
			"Validating Partner PRN", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_EXTERNAL_CALL("PMS_PMP_436", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "Calling external PRN validation service", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_EMPTY_RESPONSE("PMS_PMP_437", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "Empty response received from PRN validation provider", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME,
		    "NO_ID", "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_RESPONSE_MAPPED("PMS_PMP_438", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "PRN validation response mapped successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_EXTERNAL_SERVICE_FAILURE("PMS_PMP_439", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "External PRN validation service call failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_INVALID_RESPONSE("PMS_PMP_440", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
		    "Invalid structure in PRN validation response", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	VALIDATE_PRN_SUCCESS("PMS_PMP_441", AuditConstant.AUDIT_SYSTEM, "VALIDATE PRN",
			"PRN validated successfully", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
		    "NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_ACTIVE_FAILURE("PMS_PRT_442", AuditConstant.AUDIT_SYSTEM, "GET ACTIVE PARTNER ",
			"Retrieving active partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	RETRIVE_PARTNER_REQUIRED_PAYMENT_FAILURE("PMS_PRT_443", AuditConstant.AUDIT_SYSTEM, "GET REQUIRED PAYMENT PARTNER ",
			"Retrieving required payment partner failed", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	BALANCE_DB_SAVE_FAILURE("PMS_PRT_444", AuditConstant.AUDIT_SYSTEM, "SAVE BALANCE",
			"Failed to save balance", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PAYMENT("PMS_PRT_445", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PAYMENT",
			"Search Payment", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PAYMENT_SUCCESS("PMS_PRT_446", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PAYMENT SUCCESS",
			"Search Payment Success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_PRN("PMS_PRT_447", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER PRN",
			"Search Partner Prn", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_PRN_SUCCESS("PMS_PRT_448", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER PRN SUCCESS",
			"Search Partner Prn Success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_BALANCE("PMS_PRT_449", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER BALANCE",
			"Search Partner Balance", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
			"NO_ID_TYPE", AuditConstant.APPLICATION_NAME, AuditConstant.APPLICATION_ID),
	SEARCH_PARTNER_BALANCE_SUCCESS("PMS_PRT_450", AuditConstant.AUDIT_SYSTEM, "POST SEARCH PARTNER BALANCE SUCCESS",
			"Search Partner Balance Success", AuditConstant.PARTNER_MODULE_ID, AuditConstant.PARTNER_MODULE_NAME, "NO_ID",
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

	private PartnerServiceAuditEnum(String eventId, String type, String name, String description, String moduleId,
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
	public static PartnerServiceAuditEnum getPartnerManageEnumWithValue(PartnerServiceAuditEnum e, String s) {
		e.setDescription(String.format(e.getDescription(), s));
		if (e.getId().equalsIgnoreCase("%s"))
			e.setId(s);
		return e;
	}
}
